package HAMR_Time_Units {
  public import SI::*;
  
  attribute ps: DurationUnit = s * pico;
  attribute ms: DurationUnit = s * milli;
  attribute us: DurationUnit = s * micro;
  attribute ns: DurationUnit = s * nano;
}
