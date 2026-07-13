#include "tsp_tst.h"
#include "r2u2.h"
#include "sensor_spec.h"

// This file will not be overwritten if HAMR codegen is rerun

// Ported from the Slang temperature sensor TempSensor_tsp_tst. The sensor simulates a
// hardware reading with a bounded random walk that oscillates within the default set
// point [low, high] = [70, 90] degrees Fahrenheit. Note temperatures are int32 degrees
// here (the Slang model used F32); the walk keeps readings well inside the sensor's
// declared temperature range.
//
// The original component split the reading across a currentTemp data port and a
// tempChanged event port; those were merged into the single currentTemp event-data port,
// so a put_currentTemp both delivers the value and signals the change (no separate
// tempChanged put).
//
// --- R2U2 runtime assurance ---
// This C component's GUMBO integration contract
//
//   guarantee Sensor_Temperature_Range:
//     currentTemp.degrees >= -40 [i32] & currentTemp.degrees <= 122 [i32];
//
// is TRUSTED by the Verus system proof (see crates/sys_nominal_proof/TRUSTED_ASSUMPTIONS.md).
// An embedded R2U2 monitor (see the r2u2/ directory) observes every value the sensor emits
// on currentTemp and reports a verdict each frame; a false verdict means this component
// violated the contract the verified TempControl component assumes.  The MLTL spec
// (r2u2/sensor-spec/sensor.c2po) was compiled offline by C2PO and embedded as a byte
// array (sensor_spec.h).

// Set to N > 0 to inject an out-of-range reading (130 F) every Nth dispatch so the
// monitor's violation detection can be observed. 130 violates the sensor's contract
// (> 122) while still satisfying TempControl's integration assumption (<= 134), so
// only the R2U2 monitor -- not the downstream components -- is affected.
#define R2U2_DEMO_FAULT_PERIOD 0

// signal indices fixed by r2u2/sensor-spec/sensor.map when the spec was compiled
#define SIG_CT_EVENT 0
#define SIG_CT_DEGREES 1

#define LOW_SET_POINT  70
#define HIGH_SET_POINT 90

// --- component local state (mirrors the Slang object's fields) ---
static TempControl_SysVerif_Temperature lastTemperature = { .degrees = 72, .unit = Fahrenheit };
static int32_t delta = 4;

// --- R2U2 monitor state (all statically allocated; see R2U2_DEFAULT_MONITOR) ---
static r2u2_monitor_t sensor_monitor = R2U2_DEFAULT_MONITOR;

static r2u2_status_t sensor_monitor_verdict(r2u2_mltl_instruction_t instr, r2u2_verdict *verdict) {
  (void)instr; // single formula (id 0): SensorRange
  if (get_verdict_truth(*verdict)) {
    printf("%s: R2U2: SensorRange satisfied at time %u\n", microkit_name,
           get_verdict_time(*verdict));
  } else {
    printf("%s: R2U2: SensorRange VIOLATED at time %u -- contract assumed by TempControl does not hold\n",
           microkit_name, get_verdict_time(*verdict));
  }
  return R2U2_OK;
}

// publish a reading on currentTemp and feed it to the monitor
static void send_temp(int32_t degrees) {
  TempControl_SysVerif_Temperature temp = { .degrees = degrees, .unit = Fahrenheit };
  put_currentTemp(&temp);
  r2u2_load_bool_signal(&sensor_monitor, SIG_CT_EVENT, true);
  r2u2_load_int_signal(&sensor_monitor, SIG_CT_DEGREES, degrees);
  r2u2_step(&sensor_monitor);
}

// --- self-contained PRNG (no libc rand() in this environment): xorshift64 ---
static uint64_t rng_state = 0x853c49e6748fea9bULL;

static uint64_t next_rand(void) {
  uint64_t x = rng_state;
  x ^= x << 13;
  x ^= x >> 7;
  x ^= x << 17;
  rng_state = x;
  return x;
}

// uniform integer in [lo, hi] (inclusive); returns lo when the range is empty
static int32_t rand_between(int32_t lo, int32_t hi) {
  if (hi <= lo) {
    return lo;
  }
  uint64_t range = (uint64_t)((int64_t)hi - (int64_t)lo) + 1;
  return (int32_t)(lo + (int32_t)(next_rand() % range));
}

// Ported from Slang currentTempGet(): steer toward the set-point band, then step by a
// random amount in the direction of `delta`.
static TempControl_SysVerif_Temperature currentTempGet(void) {
  if (lastTemperature.degrees > HIGH_SET_POINT) {
    // above the band: cool down
    delta = -4;
  } else if (lastTemperature.degrees < LOW_SET_POINT) {
    // below the band: warm up
    delta = 4;
  } // otherwise keep the current direction

  int32_t nextDegrees;
  if (delta > 0) {
    // fan should be off, so the temperature should be increasing
    nextDegrees = rand_between(lastTemperature.degrees, lastTemperature.degrees + delta);
  } else {
    // fan should be on, so the temperature should be decreasing
    nextDegrees = rand_between(lastTemperature.degrees + delta, lastTemperature.degrees);
  }

  lastTemperature.degrees = nextDegrees;
  lastTemperature.unit = Fahrenheit;
  return lastTemperature;
}

void tsp_tst_initialize(void) {
  printf("%s: tsp_tst_initialize invoked\n", microkit_name);
  sensor_monitor.out_func = &sensor_monitor_verdict;
  if (r2u2_load_specification(sensor_spec_bin, &sensor_monitor) != R2U2_OK) {
    printf("%s: R2U2: failed to load specification\n", microkit_name);
  }
  // initialize the output port to the default temperature (72 F)
  send_temp(72);
}

void tsp_tst_timeTriggered(void) {
  printf("%s: tsp_tst_timeTriggered invoked\n", microkit_name);
#if R2U2_DEMO_FAULT_PERIOD > 0
  static int32_t dispatch = 0;
  dispatch++;
  if (dispatch % R2U2_DEMO_FAULT_PERIOD == 0) {
    // injected fault: out-of-range reading the monitor should flag
    send_temp(130);
    return;
  }
#endif
  // read a (simulated) temperature and publish it on the currentTemp event-data port
  TempControl_SysVerif_Temperature temp = currentTempGet();
  send_temp(temp.degrees);
}

void tsp_tst_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
