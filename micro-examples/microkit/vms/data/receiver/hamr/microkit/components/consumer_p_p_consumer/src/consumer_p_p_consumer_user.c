#include <consumer_p_p_consumer.h>
#include <consumer_p_p_consumer_user.h>
#include <libvmm/guest.h>
#include <libvmm/guest_ram.h>
#include <libvmm/virq.h>
#include <libvmm/util/util.h>
#include <libvmm/arch/aarch64/linux.h>
#include <libvmm/arch/aarch64/fault.h>

// This file will not be overwritten if HAMR codegen is rerun

// Data for the guest's kernel image.
extern char _guest_kernel_image[];
extern char _guest_kernel_image_end[];

// Data for the device tree to be passed to the kernel.
extern char _guest_dtb_image[];
extern char _guest_dtb_image_end[];

// Data for the initial RAM disk to be passed to the kernel.
extern char _guest_initrd_image[];
extern char _guest_initrd_image_end[];

// Microkit will set this variable to the start of the guest RAM memory region.
uintptr_t top_impl_Instance_consumer_p_p_consumer_VM_Guest_RAM_vaddr;

static bool is_passthrough_irq_ch(microkit_channel ch);

void consumer_p_p_consumer_initialize(void) {
  // Initialise the VMM, the VCPU(s), and start the guest
  LOG_VMM("starting \"%s\"\n", microkit_name);

  // Declare the guest's RAM layout before anything else. guest_init brings up
  // the architectural subsystems everything below depends on, in particular the
  // virtual GIC, and registers the RAM regions that guest physical addresses are
  // resolved against.
  arch_guest_init_t guest_args = {
    .pci_init.mmio_aperature_size = 0, // no virtual PCI bus
    .num_vcpus = 1,
    .num_guest_ram_regions = 1,
    .guest_ram_regions = { (struct guest_ram_region) {
      .gpa_start = GUEST_RAM_START_GPA,
      .size = GUEST_RAM_SIZE,
      .vmm_vaddr = (void *) top_impl_Instance_consumer_p_p_consumer_VM_Guest_RAM_vaddr } }
  };

  bool success = guest_init(guest_args);
  if (!success) {
    LOG_VMM_ERR("Failed to initialise guest\n");
    return;
  }

  // Place all the binaries in the right locations before starting the guest

  size_t kernel_size = _guest_kernel_image_end - _guest_kernel_image;
  size_t dtb_size = _guest_dtb_image_end - _guest_dtb_image;
  size_t initrd_size = _guest_initrd_image_end - _guest_initrd_image;

  uintptr_t kernel_pc = linux_setup_images(GUEST_RAM_START_GPA,
                                          (uintptr_t) _guest_kernel_image,
                                          kernel_size,
                                          (uintptr_t) _guest_dtb_image,
                                          GUEST_DTB_GPA,
                                          dtb_size,
                                          (uintptr_t) _guest_initrd_image,
                                          GUEST_INIT_RAM_DISK_GPA,
                                          initrd_size);

  if (!kernel_pc) {
    LOG_VMM_ERR("Failed to initialise guest images\n");
    return;
  }

  // Register the pass-through device IRQs. libvmm acks the hardware IRQ itself
  // once the guest acks the virtual one, so no ack handler is needed here.
  for(int i=0; i < MAX_IRQS; i++) {
    success = virq_register_passthrough(ARM_GIC_IRQ_ROUTE(GUEST_BOOT_VCPU_ID, mk_irqs[i].irq), mk_irqs[i].channel);
    if (!success) {
      LOG_VMM_ERR("Failed to register pass-through IRQ %d\n", mk_irqs[i].irq);
      return;
    }
    // Just in case there are already interrupts available to handle, we ack them here.
    microkit_irq_ack(mk_irqs[i].channel);
  }

  // Finally start the guest
  success = guest_start(kernel_pc, GUEST_DTB_GPA, GUEST_INIT_RAM_DISK_GPA);
  if (!success) {
    LOG_VMM_ERR("Failed to start guest\n");
    return;
  }

  LOG_VMM("Guest started, leaving consumer_p_p_consumer_initialize\n");
}

void consumer_p_p_consumer_timeTriggered(void) {
  printf("%s: consumer_p_p_consumer_timeTriggered invoked\n", microkit_name);
}

void consumer_p_p_consumer_notify(microkit_channel ch) {
  if (is_passthrough_irq_ch(ch)) {
    if (!virq_handle_passthrough(ch)) {
      LOG_VMM_ERR("IRQ dropped on channel 0x%x\n", ch);
    }
    return;
  }

  printf("Unexpected channel, ch: 0x%x\n", ch);
}

/*
 * The primary purpose of the VMM after initialisation is to act as a fault-handler.
 * Whenever our guest causes an exception, it gets delivered to this entry point for
 * the VMM to handle.
 */
seL4_Bool fault(microkit_child child, microkit_msginfo msginfo, microkit_msginfo *reply_msginfo) {
    bool success = fault_handle(child, msginfo);
    if (success) {
        // Now that we have handled the fault successfully, we reply to it so
        // that the guest can resume execution.
        *reply_msginfo = microkit_msginfo_new(0, 0);
        return seL4_True;
    }

    return seL4_False;
}

static bool is_passthrough_irq_ch(microkit_channel ch) {
  for(int i=0; i < MAX_IRQS; i++) {
    if (mk_irqs[i].channel == ch) {
      return true;
    }
  }

  return false;
}
