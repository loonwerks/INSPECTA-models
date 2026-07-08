#include "tsp_tst.h"

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

#define LOW_SET_POINT  70
#define HIGH_SET_POINT 90

// --- component local state (mirrors the Slang object's fields) ---
static TempControl_SysVerif_Temperature lastTemperature = { .degrees = 72, .unit = Fahrenheit };
static int32_t delta = 4;

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
  // initialize the output port to the default temperature (72 F)
  TempControl_SysVerif_Temperature initialTemp = { .degrees = 72, .unit = Fahrenheit };
  put_currentTemp(&initialTemp);
}

void tsp_tst_timeTriggered(void) {
  printf("%s: tsp_tst_timeTriggered invoked\n", microkit_name);
  // read a (simulated) temperature and publish it on the currentTemp event-data port
  TempControl_SysVerif_Temperature temp = currentTempGet();
  put_currentTemp(&temp);
}

void tsp_tst_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
