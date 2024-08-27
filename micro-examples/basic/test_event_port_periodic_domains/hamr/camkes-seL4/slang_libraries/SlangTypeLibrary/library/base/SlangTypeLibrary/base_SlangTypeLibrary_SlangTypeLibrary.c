#include <all.h>

Unit base_SlangTypeLibrary_SlangTypeLibrary_main_printDataContent(STACK_FRAME art_DataContent a) {
  DeclNewStackFrame(caller, "SlangTypeLibrary.scala", "base.SlangTypeLibrary.SlangTypeLibrary.main", "printDataContent", 0);

  #ifndef SIREUM_NO_PRINT

  sfUpdateLoc(13);
  {
    DeclNewString(t_0);
    String_string_(SF (String) &t_0, string(""));
    art_DataContent_string_(SF (String) &t_0, a);
    String_string_(SF (String) &t_0, string(""));
    String_cprint(((String) &t_0), T);
    cprintln(T);
    cflush(T);
  }

  #endif
}

Z base_SlangTypeLibrary_SlangTypeLibrary_main(STACK_FRAME IS_948B60 args) {
  DeclNewStackFrame(caller, "SlangTypeLibrary.scala", "base.SlangTypeLibrary.SlangTypeLibrary", "main", 0);

  sfUpdateLoc(15);
  {
    DeclNewart_Empty(t_0);
    art_Empty_apply(SF &t_0);
    base_SlangTypeLibrary_SlangTypeLibrary_main_printDataContent(SF (art_DataContent) (&t_0));
  }
  return Z_C(0);
}