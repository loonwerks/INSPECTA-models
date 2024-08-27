cmake_minimum_required(VERSION 3.8.2)

# This file will not be overwritten so is safe to edit

project(arm-vm C)

cmake_minimum_required(VERSION 3.8.2)

project(arm-vm C)

# including https://github.com/seL4/camkes-vm/blob/master/arm_vm_helpers.cmake
include(${CAMKES_ARM_VM_HELPERS_PATH})
#MESSAGE("CAMKES_ARM_VM_HELPERS_PATH = ${CAMKES_ARM_VM_HELPERS_PATH}")

find_package(camkes-vm-linux REQUIRED)

# including https://github.com/seL4/camkes-vm-linux/blob/master/vm-linux-helpers.cmake
include(${CAMKES_VM_LINUX_HELPERS_PATH})
#MESSAGE("CAMKES_VM_LINUX_HELPERS_PATH = ${CAMKES_VM_LINUX_HELPERS_PATH}")

# including https://github.com/seL4/camkes-vm-linux/blob/master/linux-module-helpers.cmake
include(${CAMKES_VM_LINUX_MODULE_HELPERS_PATH})
#MESSAGE("CAMKES_VM_LINUX_MODULE_HELPERS_PATH = ${CAMKES_VM_LINUX_MODULE_HELPERS_PATH}")

# including https://github.com/seL4/camkes-vm-linux/blob/master/linux-source-helpers.cmake
include(${CAMKES_VM_LINUX_SOURCE_HELPERS_PATH})
#MESSAGE("CAMKES_VM_LINUX_SOURCE_HELPERS_PATH = ${CAMKES_VM_LINUX_SOURCE_HELPERS_PATH}")


# This Project Depends on External Project(s)
# see https://cmake.org/cmake/help/latest/module/ExternalProject.html
include(ExternalProject)
include(external-project-helpers)


find_package(camkes-vm-images REQUIRED)
find_package(camkes-arm-vm REQUIRED)


#MESSAGE("CAMKES_VM_LINUX_HELPERS_PATH = ${CAMKES_VM_LINUX_HELPERS_PATH}")
#MESSAGE("KernelARMPlatform = ${KernelARMPlatform}")
#MESSAGE("CAMKES_ARM_VM_DIR = ${CAMKES_ARM_VM_DIR}")
#MESSAGE("CAMKES_VM_IMAGES_DIR = ${CAMKES_VM_IMAGES_DIR}")
#MESSAGE("CAMKES_VM_LINUX_DIR = ${CAMKES_VM_LINUX_DIR}")

#MESSAGE("CMAKE_CURRENT_BINARY_DIR = ${CMAKE_CURRENT_BINARY_DIR}")
#MESSAGE("CMAKE_CURRENT_SOURCE_DIR = ${CMAKE_CURRENT_SOURCE_DIR}")
#MESSAGE("CMAKE_C_COMPILER = ${CMAKE_C_COMPILER}")
#MESSAGE("BASE_C_FLAGS = ${BASE_C_FLAGS}")



# Create our CPP Flags based on ARM VM config variables
if("${KernelARMPlatform}" STREQUAL "exynos5422")
    set(cpp_flags "-DKERNELARMPLATFORM_EXYNOS5422")
    set(linux_repo "https://github.com/hardkernel/linux.git")
    set(linux_tag "4.14.87-153")
    set(linux_arch "arm")
    set(linux_cross_compile "arm-linux-gnueabi-")
elseif("${KernelARMPlatform}" STREQUAL "qemu-arm-virt")
    set(cpp_flags "-DKERNELARMPLATFORM_QEMU-ARM-VIRT")
    set(linux_repo "https://git.kernel.org/pub/scm/linux/kernel/git/stable/linux.git")
    set(linux_tag "v4.9.189")
    set(linux_arch "arm64")
    set(linux_cross_compile "aarch64-linux-gnu-")
    include(simulation)
    set(SIMULATION ON CACHE BOOL "Generate simulation script to run qemu with the proper arguments")
    if(SIMULATION)
        GenerateSimulateScript()
    endif()
endif()



if(BUILD_CROSSVM)
    MESSAGE("Not using preconfigured rootfs, will download a vanilla linux image instead")

    set(rootfs_file "${CAMKES_VM_IMAGES_DIR}/${KernelARMPlatform}/rootfs.cpio.gz")
    # Checkout and configure linux to build crossvm module
    ExternalProject_Add(
        checkout_linux
        GIT_REPOSITORY
        ${linux_repo}
        GIT_TAG
        ${linux_tag}
        GIT_SHALLOW
        1
        GIT_PROGRESS
        1
        BUILD_COMMAND
        ""
        INSTALL_COMMAND
        ""
        CONFIGURE_COMMAND
        ""
        SOURCE_DIR
        ${CMAKE_CURRENT_BINARY_DIR}/linux_out
    )
    # Linux config and symvers are to be copied to unpacked archive
    set(linux_config "${CAMKES_VM_IMAGES_DIR}/${KernelARMPlatform}/linux_configs/config")
    set(linux_symvers "${CAMKES_VM_IMAGES_DIR}/${KernelARMPlatform}/linux_configs/Module.symvers")
    # Configure unpacked archive with config and symvers
    ConfigureLinux(
        ${CMAKE_CURRENT_BINARY_DIR}/linux_out
        ${linux_config}
        ${linux_symvers}
        configure_vm_linux
        ARCH
        ${linux_arch}
        CROSS_COMPILE
        ${linux_cross_compile}
        DEPENDS
        checkout_linux
    )

    # Compile CrossVM Dataport Module
    DefineLinuxModule(
        ${CAMKES_VM_LINUX_DIR}/camkes-linux-artifacts/camkes-linux-modules/camkes-connector-modules/connection
        output_module
        output_module_target
        KERNEL_DIR
        ${CMAKE_CURRENT_BINARY_DIR}/linux_out
        ARCH
        ${linux_arch}
        CROSS_COMPILE
        ${linux_cross_compile}
        DEPENDS
        checkout_linux
        configure_vm_linux
    )
    AddFileToOverlayDir(
        "connection.ko"
        ${output_module}
        "lib/modules/4.14.87/kernel/drivers/vmm"
        overlay
        DEPENDS
        output_module_target
    )

    # Complile CrossVM Dataport Apps
    ExternalProject_Add(
        dataport-apps
        URL
        file:///${CAMKES_VM_LINUX_DIR}/camkes-linux-artifacts/camkes-linux-apps/camkes-connector-apps/pkgs/dataport
        BINARY_DIR
        ${CMAKE_CURRENT_BINARY_DIR}/dataport_apps
        INSTALL_COMMAND
        ""
        BUILD_ALWAYS
        ON
        EXCLUDE_FROM_ALL
        CMAKE_ARGS
        -DCMAKE_C_COMPILER=${CMAKE_C_COMPILER}
    )
    AddExternalProjFilesToOverlay(
        dataport-apps
        ${CMAKE_CURRENT_BINARY_DIR}/dataport_apps
        overlay
        "usr/bin"
        FILES
        dataport_read
        dataport_write
    )

    # Complile CrossVM Event Apps
    ExternalProject_Add(
        event-apps
        URL
        file:///${CAMKES_VM_LINUX_DIR}/camkes-linux-artifacts/camkes-linux-apps/camkes-connector-apps/pkgs/emits_event
        BINARY_DIR
        ${CMAKE_CURRENT_BINARY_DIR}/emits_event_apps
        INSTALL_COMMAND
        ""
        BUILD_ALWAYS
        ON
        EXCLUDE_FROM_ALL
        CMAKE_ARGS
        -DCMAKE_C_COMPILER=${CMAKE_C_COMPILER}
    )
    AddExternalProjFilesToOverlay(
        event-apps
        ${CMAKE_CURRENT_BINARY_DIR}/emits_event_apps
        overlay
        "usr/bin"
        FILES
        emits_event_emit
    )

    # Complile CrossVM Consume Event Apps
    ExternalProject_Add(
        consume-event-apps
        URL
        file:///${CAMKES_VM_LINUX_DIR}/camkes-linux-artifacts/camkes-linux-apps/camkes-connector-apps/pkgs/consumes_event
        BINARY_DIR
        ${CMAKE_CURRENT_BINARY_DIR}/consume_event_apps
        INSTALL_COMMAND
        ""
        BUILD_ALWAYS
        ON
        EXCLUDE_FROM_ALL
        CMAKE_ARGS
        -DCMAKE_C_COMPILER=${CMAKE_C_COMPILER}
    )
    AddExternalProjFilesToOverlay(
        consume-event-apps
        ${CMAKE_CURRENT_BINARY_DIR}/consume_event_apps
        overlay
        "usr/bin"
        FILES
        consumes_event_wait
    )

    # Add script to initialise dataport module
    AddFileToOverlayDir(
        "S90crossvm_module_init"
        ${CMAKE_CURRENT_SOURCE_DIR}/overlay_files/init_scripts/cross_vm_module_init
        "etc/init.d"
        overlay
    )
else()
    MESSAGE("Using pre-configured rootfs")

    # Use pre-configured rootfs file with crossvm modules and apps installed
    set(rootfs_file "${CAMKES_VM_IMAGES_DIR}/${KernelARMPlatform}/rootfs_crossvm.cpio.gz")
endif()


# Complile CrossVM Event Apps vmp3
foreach(item IN ITEMS vmp3)
    ExternalProject_Add(
        ${item}
        SOURCE_DIR
        ${CMAKE_CURRENT_SOURCE_DIR}/apps/${item}
        BINARY_DIR
        ${CMAKE_CURRENT_BINARY_DIR}/${item}
        INSTALL_COMMAND
        ""
        BUILD_ALWAYS
        ON
        EXCLUDE_FROM_ALL
        CMAKE_ARGS
        -DCMAKE_C_COMPILER=${CMAKE_C_COMPILER}
        -DSB_Type_Library_DIR=${CMAKE_CURRENT_SOURCE_DIR}/../../types
        -DCMAKE_C_FLAGS=${BASE_C_FLAGS}
    )

    AddExternalProjFilesToOverlay(
        ${item}
        ${CMAKE_CURRENT_BINARY_DIR}/${item}
        overlay
        "usr/bin"
        FILES
        ${item}
    )
endforeach()

# Overwrite inittab file for using the virtio console hvc0.
AddFileToOverlayDir(
    "inittab"
    ${CMAKE_CURRENT_SOURCE_DIR}/overlay_files/init_scripts/inittab_hvc0
    "etc"
    overlay
)

# Construct new rootfs
AddOverlayDirToRootfs(
    overlay
    ${rootfs_file}
    "buildroot"
    "rootfs_install"
    output_overlayed_rootfs_location
    rootfs_target
    GZIP
)

AddToFileServer("linux-initrd"
                ${output_overlayed_rootfs_location}
                DEPENDS rootfs_target)

# Add linux kernel image to file server
AddToFileServer("linux"
                "${CAMKES_VM_IMAGES_DIR}/${KernelARMPlatform}/linux")

AddCamkesCPPFlag(
    cpp_flags
    CONFIG_VARS
    VmEmmc2NoDMA
    VmVUSB
    VmVchan
    Tk1DeviceFwd
    Tk1Insecure
    VmVirtioNetVirtqueue
)

DefineCAmkESVMFileServer()

CAmkESAddImportPath(${CMAKE_CURRENT_SOURCE_DIR}/${KernelARMPlatform}/)

# Define our VM Component with our cross vm dataports glue code
DeclareCamkesARMVM(
  VM_P3_i_p3
  EXTRA_SOURCES src/cross_vm_connections_vmp3.c
  EXTRA_LIBS SB_Type_Library)

CAmkESAddCPPInclude(${CAMKES_ARM_VM_DIR}/components/VM)