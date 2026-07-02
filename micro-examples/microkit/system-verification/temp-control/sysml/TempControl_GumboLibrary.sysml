package TempControl_GumboLibrary {
    language "GUMBO" /*{
      library
        functions
          // The coldest and hottest air temperatures ever recorded on Earth, in Fahrenheit:
          //   -128.6 F  Vostok Station, Antarctica  (1983-07-21)
          //    134.0 F  Furnace Creek, Death Valley, USA  (1913-07-10)
          def validTemp(t: TempControl_SysVerif::Temperature): Base_Types::Boolean :=
            t.degrees >= -129 [i32] & t.degrees <= 134 [i32];
    }*/
}
