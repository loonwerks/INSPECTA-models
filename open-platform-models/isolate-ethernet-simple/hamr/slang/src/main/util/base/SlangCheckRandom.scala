// #Sireum

package base

import org.sireum._
import org.sireum.Random.Gen64

/*
GENERATED FROM

InternetProtocol.scala

FrameProtocol.scala

ARP_Type.scala

RawEthernetMessage.scala

StructuredEthernetMessage_i.scala

Base_Types.scala

GUMBO__Library.scala

ArduPilot_Impl_seL4_ArduPilot_ArduPilot_Containers.scala

Firewall_Impl_seL4_Firewall_Firewall_Containers.scala

LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Containers.scala

ObservationKind.scala

Container.scala

DataContent.scala

Aux_Types.scala

*/

@msig trait RandomLibI {
  def gen: org.sireum.Random.Gen

  def get_numElement: Z
  def set_numElement(s: Z): Unit

  // ========  Z ==========
    def get_Config_Z: Config_Z
    def set_Config_Z(config: Config_Z): RandomLib

    def nextZ(): Z = {
      val conf = get_Config_Z

      var r: Z = if (conf.low.isEmpty) {
          if (conf.high.isEmpty)
            gen.nextZ()
          else
            gen.nextZBetween(S64.Min.toZ, conf.high.get)
        } else {
          if (conf.high.isEmpty)
            gen.nextZBetween(conf.low.get, S64.Max.toZ)
          else
            gen.nextZBetween(conf.low.get, conf.high.get)
        }

      if(get_Config_Z.attempts >= 0) {
       for (i <- 0 to get_Config_Z.attempts) {
         if (get_Config_Z.filter(r)) {
           return r
         }
         if (get_Config_Z.verbose) {
           println(s"Retrying for failing value: $r")
         }
         r = if (conf.low.isEmpty) {
           if (conf.high.isEmpty)
             gen.nextZ()
           else
              gen.nextZBetween(S64.Min.toZ, conf.high.get)
          } else {
            if (conf.high.isEmpty)
              gen.nextZBetween(conf.low.get, S64.Max.toZ)
            else
             gen.nextZBetween(conf.low.get, conf.high.get)
         }
       }
      } else {
       while(T) {
         if (get_Config_Z.filter(r)) {
           return r
         }
         if (get_Config_Z.verbose) {
           println(s"Retrying for failing value: $r")
         }
         r = if (conf.low.isEmpty) {
           if (conf.high.isEmpty)
             gen.nextZ()
           else
              gen.nextZBetween(S64.Min.toZ, conf.high.get)
          } else {
            if (conf.high.isEmpty)
              gen.nextZBetween(conf.low.get, S64.Max.toZ)
            else
             gen.nextZBetween(conf.low.get, conf.high.get)
         }
       }
      }
      assert(F, "Requirements too strict to generate")
      halt("Requirements too strict to generate")
    }

  // ========  B ==========}
    def get_Config_B: Config_B
    def set_Config_B(config: Config_B): RandomLib

    def nextB(): B = {
      var r = gen.nextB()
      if(get_Config_B.attempts >= 0) {
       for (i <- 0 to get_Config_B.attempts) {
         if (get_Config_B.filter(r)) {
           return r
         }
         if (get_Config_B.verbose) {
           println(s"Retrying for failing value: $r")
         }
         r = gen.nextB()
       }
      } else {
       while(T) {
         if (get_Config_B.filter(r)) {
           return r
         }
         if (get_Config_B.verbose) {
           println(s"Retrying for failing value: $r")
         }
         r = gen.nextB()
       }
      }
      assert(F, "Requirements too strict to generate")
      halt("Requirements too strict to generate")
    }

  // ========  C ==========
    def get_Config_C: Config_C
    def set_Config_C(config: Config_C): RandomLib

    def nextC(): C = {
      val conf = get_Config_C

      var r: C = if (conf.low.isEmpty) {
          if (conf.high.isEmpty)
            gen.nextC()
          else
            gen.nextCBetween(C.fromZ(0), conf.high.get)
        } else {
          if (conf.high.isEmpty)
            gen.nextCBetween(conf.low.get, C.fromZ(1114111))
          else
            gen.nextCBetween(conf.low.get, conf.high.get)
        }

      if(get_Config_C.attempts >= 0) {
       for (i <- 0 to get_Config_C.attempts) {
         if (get_Config_C.filter(r)) {
           return r
         }
         if (get_Config_C.verbose) {
           println(s"Retrying for failing value: $r")
         }
         r = if (conf.low.isEmpty) {
           if (conf.high.isEmpty)
             gen.nextC()
           else
              gen.nextCBetween(C.fromZ(0), conf.high.get)
          } else {
            if (conf.high.isEmpty)
              gen.nextCBetween(conf.low.get, C.fromZ(1114111))
            else
             gen.nextCBetween(conf.low.get, conf.high.get)
         }
       }
      } else {
       while(T) {
         if (get_Config_C.filter(r)) {
           return r
         }
         if (get_Config_C.verbose) {
           println(s"Retrying for failing value: $r")
         }
         r = if (conf.low.isEmpty) {
           if (conf.high.isEmpty)
             gen.nextC()
           else
              gen.nextCBetween(C.fromZ(0), conf.high.get)
          } else {
            if (conf.high.isEmpty)
              gen.nextCBetween(conf.low.get, C.fromZ(1114111))
            else
             gen.nextCBetween(conf.low.get, conf.high.get)
         }
       }
      }
      assert(F, "Requirements too strict to generate")
      halt("Requirements too strict to generate")
    }

  // ========  R ==========
    def get_Config_R: Config_R
    def set_Config_R(config: Config_R): RandomLib

    def nextR(): R = {
      val conf = get_Config_R

      var r: R = if (conf.low.isEmpty) {
          if (conf.high.isEmpty)
            gen.nextR()
          else
            gen.nextRBetween(r"-1.7976931348623157E308", conf.high.get)
        } else {
          if (conf.high.isEmpty)
            gen.nextRBetween(conf.low.get, r"1.7976931348623157E308")
          else
            gen.nextRBetween(conf.low.get, conf.high.get)
        }

      if(get_Config_R.attempts >= 0) {
       for (i <- 0 to get_Config_R.attempts) {
         if (get_Config_R.filter(r)) {
           return r
         }
         if (get_Config_R.verbose) {
           println(s"Retrying for failing value: $r")
         }
         r = if (conf.low.isEmpty) {
           if (conf.high.isEmpty)
             gen.nextR()
           else
              gen.nextRBetween(r"-1.7976931348623157E308", conf.high.get)
          } else {
            if (conf.high.isEmpty)
              gen.nextRBetween(conf.low.get, r"1.7976931348623157E308")
            else
             gen.nextRBetween(conf.low.get, conf.high.get)
         }
       }
      } else {
       while(T) {
         if (get_Config_R.filter(r)) {
           return r
         }
         if (get_Config_R.verbose) {
           println(s"Retrying for failing value: $r")
         }
         r = if (conf.low.isEmpty) {
           if (conf.high.isEmpty)
             gen.nextR()
           else
              gen.nextRBetween(r"-1.7976931348623157E308", conf.high.get)
          } else {
            if (conf.high.isEmpty)
              gen.nextRBetween(conf.low.get, r"1.7976931348623157E308")
            else
             gen.nextRBetween(conf.low.get, conf.high.get)
         }
       }
      }
      assert(F, "Requirements too strict to generate")
      halt("Requirements too strict to generate")
    }

  // ========  F32 ==========
    def get_Config_F32: Config_F32
    def set_Config_F32(config: Config_F32): RandomLib

    def nextF32(): F32 = {
      val conf = get_Config_F32

      var r: F32 = if (conf.low.isEmpty) {
          if (conf.high.isEmpty)
            gen.nextF32()
          else
            gen.nextF32Between(f32"-3.40282347E38f", conf.high.get)
        } else {
          if (conf.high.isEmpty)
            gen.nextF32Between(conf.low.get, f32"3.4028235E38f")
          else
            gen.nextF32Between(conf.low.get, conf.high.get)
        }

      if(get_Config_F32.attempts >= 0) {
       for (i <- 0 to get_Config_F32.attempts) {
         if (get_Config_F32.filter(r)) {
           return r
         }
         if (get_Config_F32.verbose) {
           println(s"Retrying for failing value: $r")
         }
         r = if (conf.low.isEmpty) {
           if (conf.high.isEmpty)
             gen.nextF32()
           else
              gen.nextF32Between(f32"-3.40282347E38f", conf.high.get)
          } else {
            if (conf.high.isEmpty)
              gen.nextF32Between(conf.low.get, f32"3.4028235E38f")
            else
             gen.nextF32Between(conf.low.get, conf.high.get)
         }
       }
      } else {
       while(T) {
         if (get_Config_F32.filter(r)) {
           return r
         }
         if (get_Config_F32.verbose) {
           println(s"Retrying for failing value: $r")
         }
         r = if (conf.low.isEmpty) {
           if (conf.high.isEmpty)
             gen.nextF32()
           else
              gen.nextF32Between(f32"-3.40282347E38f", conf.high.get)
          } else {
            if (conf.high.isEmpty)
              gen.nextF32Between(conf.low.get, f32"3.4028235E38f")
            else
             gen.nextF32Between(conf.low.get, conf.high.get)
         }
       }
      }
      assert(F, "Requirements too strict to generate")
      halt("Requirements too strict to generate")
    }

  // ========  F64 ==========
    def get_Config_F64: Config_F64
    def set_Config_F64(config: Config_F64): RandomLib

    def nextF64(): F64 = {
      val conf = get_Config_F64

      var r: F64 = if (conf.low.isEmpty) {
          if (conf.high.isEmpty)
            gen.nextF64()
          else
            gen.nextF64Between(f64"-1.7976931348623157E308f", conf.high.get)
        } else {
          if (conf.high.isEmpty)
            gen.nextF64Between(conf.low.get, f64"1.7976931348623157E308f")
          else
            gen.nextF64Between(conf.low.get, conf.high.get)
        }

      if(get_Config_F64.attempts >= 0) {
       for (i <- 0 to get_Config_F64.attempts) {
         if (get_Config_F64.filter(r)) {
           return r
         }
         if (get_Config_F64.verbose) {
           println(s"Retrying for failing value: $r")
         }
         r = if (conf.low.isEmpty) {
           if (conf.high.isEmpty)
             gen.nextF64()
           else
              gen.nextF64Between(f64"-1.7976931348623157E308f", conf.high.get)
          } else {
            if (conf.high.isEmpty)
              gen.nextF64Between(conf.low.get, f64"1.7976931348623157E308f")
            else
             gen.nextF64Between(conf.low.get, conf.high.get)
         }
       }
      } else {
       while(T) {
         if (get_Config_F64.filter(r)) {
           return r
         }
         if (get_Config_F64.verbose) {
           println(s"Retrying for failing value: $r")
         }
         r = if (conf.low.isEmpty) {
           if (conf.high.isEmpty)
             gen.nextF64()
           else
              gen.nextF64Between(f64"-1.7976931348623157E308f", conf.high.get)
          } else {
            if (conf.high.isEmpty)
              gen.nextF64Between(conf.low.get, f64"1.7976931348623157E308f")
            else
             gen.nextF64Between(conf.low.get, conf.high.get)
         }
       }
      }
      assert(F, "Requirements too strict to generate")
      halt("Requirements too strict to generate")
    }

  // ========  S8 ==========
    def get_Config_S8: Config_S8
    def set_Config_S8(config: Config_S8): RandomLib

    def nextS8(): S8 = {
      val conf = get_Config_S8

      var r: S8 = if (conf.low.isEmpty) {
          if (conf.high.isEmpty)
            gen.nextS8()
          else
            gen.nextS8Between(S8.Min, conf.high.get)
        } else {
          if (conf.high.isEmpty)
            gen.nextS8Between(conf.low.get, S8.Max)
          else
            gen.nextS8Between(conf.low.get, conf.high.get)
        }

      if(get_Config_S8.attempts >= 0) {
       for (i <- 0 to get_Config_S8.attempts) {
         if (get_Config_S8.filter(r)) {
           return r
         }
         if (get_Config_S8.verbose) {
           println(s"Retrying for failing value: $r")
         }
         r = if (conf.low.isEmpty) {
           if (conf.high.isEmpty)
             gen.nextS8()
           else
              gen.nextS8Between(S8.Min, conf.high.get)
          } else {
            if (conf.high.isEmpty)
              gen.nextS8Between(conf.low.get, S8.Max)
            else
             gen.nextS8Between(conf.low.get, conf.high.get)
         }
       }
      } else {
       while(T) {
         if (get_Config_S8.filter(r)) {
           return r
         }
         if (get_Config_S8.verbose) {
           println(s"Retrying for failing value: $r")
         }
         r = if (conf.low.isEmpty) {
           if (conf.high.isEmpty)
             gen.nextS8()
           else
              gen.nextS8Between(S8.Min, conf.high.get)
          } else {
            if (conf.high.isEmpty)
              gen.nextS8Between(conf.low.get, S8.Max)
            else
             gen.nextS8Between(conf.low.get, conf.high.get)
         }
       }
      }
      assert(F, "Requirements too strict to generate")
      halt("Requirements too strict to generate")
    }

  // ========  S16 ==========
    def get_Config_S16: Config_S16
    def set_Config_S16(config: Config_S16): RandomLib

    def nextS16(): S16 = {
      val conf = get_Config_S16

      var r: S16 = if (conf.low.isEmpty) {
          if (conf.high.isEmpty)
            gen.nextS16()
          else
            gen.nextS16Between(S16.Min, conf.high.get)
        } else {
          if (conf.high.isEmpty)
            gen.nextS16Between(conf.low.get, S16.Max)
          else
            gen.nextS16Between(conf.low.get, conf.high.get)
        }

      if(get_Config_S16.attempts >= 0) {
       for (i <- 0 to get_Config_S16.attempts) {
         if (get_Config_S16.filter(r)) {
           return r
         }
         if (get_Config_S16.verbose) {
           println(s"Retrying for failing value: $r")
         }
         r = if (conf.low.isEmpty) {
           if (conf.high.isEmpty)
             gen.nextS16()
           else
              gen.nextS16Between(S16.Min, conf.high.get)
          } else {
            if (conf.high.isEmpty)
              gen.nextS16Between(conf.low.get, S16.Max)
            else
             gen.nextS16Between(conf.low.get, conf.high.get)
         }
       }
      } else {
       while(T) {
         if (get_Config_S16.filter(r)) {
           return r
         }
         if (get_Config_S16.verbose) {
           println(s"Retrying for failing value: $r")
         }
         r = if (conf.low.isEmpty) {
           if (conf.high.isEmpty)
             gen.nextS16()
           else
              gen.nextS16Between(S16.Min, conf.high.get)
          } else {
            if (conf.high.isEmpty)
              gen.nextS16Between(conf.low.get, S16.Max)
            else
             gen.nextS16Between(conf.low.get, conf.high.get)
         }
       }
      }
      assert(F, "Requirements too strict to generate")
      halt("Requirements too strict to generate")
    }

  // ========  S32 ==========
    def get_Config_S32: Config_S32
    def set_Config_S32(config: Config_S32): RandomLib

    def nextS32(): S32 = {
      val conf = get_Config_S32

      var r: S32 = if (conf.low.isEmpty) {
          if (conf.high.isEmpty)
            gen.nextS32()
          else
            gen.nextS32Between(S32.Min, conf.high.get)
        } else {
          if (conf.high.isEmpty)
            gen.nextS32Between(conf.low.get, S32.Max)
          else
            gen.nextS32Between(conf.low.get, conf.high.get)
        }

      if(get_Config_S32.attempts >= 0) {
       for (i <- 0 to get_Config_S32.attempts) {
         if (get_Config_S32.filter(r)) {
           return r
         }
         if (get_Config_S32.verbose) {
           println(s"Retrying for failing value: $r")
         }
         r = if (conf.low.isEmpty) {
           if (conf.high.isEmpty)
             gen.nextS32()
           else
              gen.nextS32Between(S32.Min, conf.high.get)
          } else {
            if (conf.high.isEmpty)
              gen.nextS32Between(conf.low.get, S32.Max)
            else
             gen.nextS32Between(conf.low.get, conf.high.get)
         }
       }
      } else {
       while(T) {
         if (get_Config_S32.filter(r)) {
           return r
         }
         if (get_Config_S32.verbose) {
           println(s"Retrying for failing value: $r")
         }
         r = if (conf.low.isEmpty) {
           if (conf.high.isEmpty)
             gen.nextS32()
           else
              gen.nextS32Between(S32.Min, conf.high.get)
          } else {
            if (conf.high.isEmpty)
              gen.nextS32Between(conf.low.get, S32.Max)
            else
             gen.nextS32Between(conf.low.get, conf.high.get)
         }
       }
      }
      assert(F, "Requirements too strict to generate")
      halt("Requirements too strict to generate")
    }

  // ========  S64 ==========
    def get_Config_S64: Config_S64
    def set_Config_S64(config: Config_S64): RandomLib

    def nextS64(): S64 = {
      val conf = get_Config_S64

      var r: S64 = if (conf.low.isEmpty) {
          if (conf.high.isEmpty)
            gen.nextS64()
          else
            gen.nextS64Between(S64.Min, conf.high.get)
        } else {
          if (conf.high.isEmpty)
            gen.nextS64Between(conf.low.get, S64.Max)
          else
            gen.nextS64Between(conf.low.get, conf.high.get)
        }

      if(get_Config_S64.attempts >= 0) {
       for (i <- 0 to get_Config_S64.attempts) {
         if (get_Config_S64.filter(r)) {
           return r
         }
         if (get_Config_S64.verbose) {
           println(s"Retrying for failing value: $r")
         }
         r = if (conf.low.isEmpty) {
           if (conf.high.isEmpty)
             gen.nextS64()
           else
              gen.nextS64Between(S64.Min, conf.high.get)
          } else {
            if (conf.high.isEmpty)
              gen.nextS64Between(conf.low.get, S64.Max)
            else
             gen.nextS64Between(conf.low.get, conf.high.get)
         }
       }
      } else {
       while(T) {
         if (get_Config_S64.filter(r)) {
           return r
         }
         if (get_Config_S64.verbose) {
           println(s"Retrying for failing value: $r")
         }
         r = if (conf.low.isEmpty) {
           if (conf.high.isEmpty)
             gen.nextS64()
           else
              gen.nextS64Between(S64.Min, conf.high.get)
          } else {
            if (conf.high.isEmpty)
              gen.nextS64Between(conf.low.get, S64.Max)
            else
             gen.nextS64Between(conf.low.get, conf.high.get)
         }
       }
      }
      assert(F, "Requirements too strict to generate")
      halt("Requirements too strict to generate")
    }

  // ========  U8 ==========
    def get_Config_U8: Config_U8
    def set_Config_U8(config: Config_U8): RandomLib

    def nextU8(): U8 = {
      val conf = get_Config_U8

      var r: U8 = if (conf.low.isEmpty) {
          if (conf.high.isEmpty)
            gen.nextU8()
          else
            gen.nextU8Between(U8.Min, conf.high.get)
        } else {
          if (conf.high.isEmpty)
            gen.nextU8Between(conf.low.get, U8.Max)
          else
            gen.nextU8Between(conf.low.get, conf.high.get)
        }

      if(get_Config_U8.attempts >= 0) {
       for (i <- 0 to get_Config_U8.attempts) {
         if (get_Config_U8.filter(r)) {
           return r
         }
         if (get_Config_U8.verbose) {
           println(s"Retrying for failing value: $r")
         }
         r = if (conf.low.isEmpty) {
           if (conf.high.isEmpty)
             gen.nextU8()
           else
              gen.nextU8Between(U8.Min, conf.high.get)
          } else {
            if (conf.high.isEmpty)
              gen.nextU8Between(conf.low.get, U8.Max)
            else
             gen.nextU8Between(conf.low.get, conf.high.get)
         }
       }
      } else {
       while(T) {
         if (get_Config_U8.filter(r)) {
           return r
         }
         if (get_Config_U8.verbose) {
           println(s"Retrying for failing value: $r")
         }
         r = if (conf.low.isEmpty) {
           if (conf.high.isEmpty)
             gen.nextU8()
           else
              gen.nextU8Between(U8.Min, conf.high.get)
          } else {
            if (conf.high.isEmpty)
              gen.nextU8Between(conf.low.get, U8.Max)
            else
             gen.nextU8Between(conf.low.get, conf.high.get)
         }
       }
      }
      assert(F, "Requirements too strict to generate")
      halt("Requirements too strict to generate")
    }

  // ========  U16 ==========
    def get_Config_U16: Config_U16
    def set_Config_U16(config: Config_U16): RandomLib

    def nextU16(): U16 = {
      val conf = get_Config_U16

      var r: U16 = if (conf.low.isEmpty) {
          if (conf.high.isEmpty)
            gen.nextU16()
          else
            gen.nextU16Between(U16.Min, conf.high.get)
        } else {
          if (conf.high.isEmpty)
            gen.nextU16Between(conf.low.get, U16.Max)
          else
            gen.nextU16Between(conf.low.get, conf.high.get)
        }

      if(get_Config_U16.attempts >= 0) {
       for (i <- 0 to get_Config_U16.attempts) {
         if (get_Config_U16.filter(r)) {
           return r
         }
         if (get_Config_U16.verbose) {
           println(s"Retrying for failing value: $r")
         }
         r = if (conf.low.isEmpty) {
           if (conf.high.isEmpty)
             gen.nextU16()
           else
              gen.nextU16Between(U16.Min, conf.high.get)
          } else {
            if (conf.high.isEmpty)
              gen.nextU16Between(conf.low.get, U16.Max)
            else
             gen.nextU16Between(conf.low.get, conf.high.get)
         }
       }
      } else {
       while(T) {
         if (get_Config_U16.filter(r)) {
           return r
         }
         if (get_Config_U16.verbose) {
           println(s"Retrying for failing value: $r")
         }
         r = if (conf.low.isEmpty) {
           if (conf.high.isEmpty)
             gen.nextU16()
           else
              gen.nextU16Between(U16.Min, conf.high.get)
          } else {
            if (conf.high.isEmpty)
              gen.nextU16Between(conf.low.get, U16.Max)
            else
             gen.nextU16Between(conf.low.get, conf.high.get)
         }
       }
      }
      assert(F, "Requirements too strict to generate")
      halt("Requirements too strict to generate")
    }

  // ========  U32 ==========
    def get_Config_U32: Config_U32
    def set_Config_U32(config: Config_U32): RandomLib

    def nextU32(): U32 = {
      val conf = get_Config_U32

      var r: U32 = if (conf.low.isEmpty) {
          if (conf.high.isEmpty)
            gen.nextU32()
          else
            gen.nextU32Between(U32.Min, conf.high.get)
        } else {
          if (conf.high.isEmpty)
            gen.nextU32Between(conf.low.get, U32.Max)
          else
            gen.nextU32Between(conf.low.get, conf.high.get)
        }

      if(get_Config_U32.attempts >= 0) {
       for (i <- 0 to get_Config_U32.attempts) {
         if (get_Config_U32.filter(r)) {
           return r
         }
         if (get_Config_U32.verbose) {
           println(s"Retrying for failing value: $r")
         }
         r = if (conf.low.isEmpty) {
           if (conf.high.isEmpty)
             gen.nextU32()
           else
              gen.nextU32Between(U32.Min, conf.high.get)
          } else {
            if (conf.high.isEmpty)
              gen.nextU32Between(conf.low.get, U32.Max)
            else
             gen.nextU32Between(conf.low.get, conf.high.get)
         }
       }
      } else {
       while(T) {
         if (get_Config_U32.filter(r)) {
           return r
         }
         if (get_Config_U32.verbose) {
           println(s"Retrying for failing value: $r")
         }
         r = if (conf.low.isEmpty) {
           if (conf.high.isEmpty)
             gen.nextU32()
           else
              gen.nextU32Between(U32.Min, conf.high.get)
          } else {
            if (conf.high.isEmpty)
              gen.nextU32Between(conf.low.get, U32.Max)
            else
             gen.nextU32Between(conf.low.get, conf.high.get)
         }
       }
      }
      assert(F, "Requirements too strict to generate")
      halt("Requirements too strict to generate")
    }

  // ========  U64 ==========
    def get_Config_U64: Config_U64
    def set_Config_U64(config: Config_U64): RandomLib

    def nextU64(): U64 = {
      val conf = get_Config_U64

      var r: U64 = if (conf.low.isEmpty) {
          if (conf.high.isEmpty)
            gen.nextU64()
          else
            gen.nextU64Between(U64.Min, conf.high.get)
        } else {
          if (conf.high.isEmpty)
            gen.nextU64Between(conf.low.get, U64.Max)
          else
            gen.nextU64Between(conf.low.get, conf.high.get)
        }

      if(get_Config_U64.attempts >= 0) {
       for (i <- 0 to get_Config_U64.attempts) {
         if (get_Config_U64.filter(r)) {
           return r
         }
         if (get_Config_U64.verbose) {
           println(s"Retrying for failing value: $r")
         }
         r = if (conf.low.isEmpty) {
           if (conf.high.isEmpty)
             gen.nextU64()
           else
              gen.nextU64Between(U64.Min, conf.high.get)
          } else {
            if (conf.high.isEmpty)
              gen.nextU64Between(conf.low.get, U64.Max)
            else
             gen.nextU64Between(conf.low.get, conf.high.get)
         }
       }
      } else {
       while(T) {
         if (get_Config_U64.filter(r)) {
           return r
         }
         if (get_Config_U64.verbose) {
           println(s"Retrying for failing value: $r")
         }
         r = if (conf.low.isEmpty) {
           if (conf.high.isEmpty)
             gen.nextU64()
           else
              gen.nextU64Between(U64.Min, conf.high.get)
          } else {
            if (conf.high.isEmpty)
              gen.nextU64Between(conf.low.get, U64.Max)
            else
             gen.nextU64Between(conf.low.get, conf.high.get)
         }
       }
      }
      assert(F, "Requirements too strict to generate")
      halt("Requirements too strict to generate")
    }

  // ============= String ===================

  def get_Config_String: Config_String
  def set_Config_String(config: Config_String): RandomLib

  def nextString(): String = {

    var length: Z = gen.nextZBetween(get_Config_String.minSize, get_Config_String.maxSize)
    var str: String = ""
    for(r <- 0 until length){
      str = s"${str}${nextC().string}"
    }

    if(get_Config_String.attempts >= 0) {
      for (i <- 0 to get_Config_String.attempts) {
        if(get_Config_String.filter(str)) {
          return str
        }
        if(get_Config_String.verbose) {
          println(s"Retrying for failing value: $str")
        }

        length = gen.nextZBetween(get_Config_String.minSize, get_Config_String.maxSize)
        str = ""
        for (r <- 0 until length) {
          str = s"${str}${nextC().string}"
        }
      }
    } else {
      while(T) {
        if (get_Config_String.filter(str)) {
          return str
        }
        if (get_Config_String.verbose) {
          println(s"Retrying for failing value: $str")
        }

        length = gen.nextZBetween(get_Config_String.minSize, get_Config_String.maxSize)
        str = ""
        for (r <- 0 until length) {
          str = s"${str}${nextC().string}"
        }
      }
    }
    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= art.DataContent ===================

  def get_Config__artDataContent: Config__artDataContent
  def set_Config__artDataContent(config: Config__artDataContent): RandomLib

  def next_artDataContent(): art.DataContent = {
    var callEnum: ISZ[_artDataContent_DataTypeId.Type] = ISZ(_artDataContent_DataTypeId._artEmpty_Id, _artDataContent_DataTypeId.Base_TypesBits_Payload_Id, _artDataContent_DataTypeId.Base_TypesBoolean_Payload_Id, _artDataContent_DataTypeId.Base_TypesCharacter_Payload_Id, _artDataContent_DataTypeId.Base_TypesFloat_32_Payload_Id, _artDataContent_DataTypeId.Base_TypesFloat_64_Payload_Id, _artDataContent_DataTypeId.Base_TypesFloat_Payload_Id, _artDataContent_DataTypeId.Base_TypesInteger_16_Payload_Id, _artDataContent_DataTypeId.Base_TypesInteger_32_Payload_Id, _artDataContent_DataTypeId.Base_TypesInteger_64_Payload_Id, _artDataContent_DataTypeId.Base_TypesInteger_8_Payload_Id, _artDataContent_DataTypeId.Base_TypesInteger_Payload_Id, _artDataContent_DataTypeId.Base_TypesString_Payload_Id, _artDataContent_DataTypeId.Base_TypesUnsigned_16_Payload_Id, _artDataContent_DataTypeId.Base_TypesUnsigned_32_Payload_Id, _artDataContent_DataTypeId.Base_TypesUnsigned_64_Payload_Id, _artDataContent_DataTypeId.Base_TypesUnsigned_8_Payload_Id, _artDataContent_DataTypeId.SWARP_Type_Payload_Id, _artDataContent_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P_Id, _artDataContent_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS_Id, _artDataContent_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P_Id, _artDataContent_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS_Id, _artDataContent_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P_Id, _artDataContent_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS_Id, _artDataContent_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P_Id, _artDataContent_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS_Id, _artDataContent_DataTypeId.SWFrameProtocol_Payload_Id, _artDataContent_DataTypeId.SWInternetProtocol_Payload_Id, _artDataContent_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P_Id, _artDataContent_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS_Id, _artDataContent_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P_Id, _artDataContent_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS_Id, _artDataContent_DataTypeId.SWRawEthernetMessage_Payload_Id, _artDataContent_DataTypeId.SWStructuredEthernetMessage_i_Payload_Id, _artDataContent_DataTypeId.utilEmptyContainer_Id)

    if(get_Config__artDataContent.additiveTypeFiltering) {
       callEnum = get_Config__artDataContent.typeFilter
    } else {
       for(h <- get_Config__artDataContent.typeFilter) {
         callEnum = ops.ISZOps(callEnum).filter(f => h =!= f)
       }
    }

    var c = callEnum(gen.nextZBetween(0, callEnum.size-1))

    var v: art.DataContent = c match {
      case _artDataContent_DataTypeId._artEmpty_Id => (next_artEmpty _).apply()
      case _artDataContent_DataTypeId.Base_TypesBits_Payload_Id => (nextBase_TypesBits_Payload _).apply()
      case _artDataContent_DataTypeId.Base_TypesBoolean_Payload_Id => (nextBase_TypesBoolean_Payload _).apply()
      case _artDataContent_DataTypeId.Base_TypesCharacter_Payload_Id => (nextBase_TypesCharacter_Payload _).apply()
      case _artDataContent_DataTypeId.Base_TypesFloat_32_Payload_Id => (nextBase_TypesFloat_32_Payload _).apply()
      case _artDataContent_DataTypeId.Base_TypesFloat_64_Payload_Id => (nextBase_TypesFloat_64_Payload _).apply()
      case _artDataContent_DataTypeId.Base_TypesFloat_Payload_Id => (nextBase_TypesFloat_Payload _).apply()
      case _artDataContent_DataTypeId.Base_TypesInteger_16_Payload_Id => (nextBase_TypesInteger_16_Payload _).apply()
      case _artDataContent_DataTypeId.Base_TypesInteger_32_Payload_Id => (nextBase_TypesInteger_32_Payload _).apply()
      case _artDataContent_DataTypeId.Base_TypesInteger_64_Payload_Id => (nextBase_TypesInteger_64_Payload _).apply()
      case _artDataContent_DataTypeId.Base_TypesInteger_8_Payload_Id => (nextBase_TypesInteger_8_Payload _).apply()
      case _artDataContent_DataTypeId.Base_TypesInteger_Payload_Id => (nextBase_TypesInteger_Payload _).apply()
      case _artDataContent_DataTypeId.Base_TypesString_Payload_Id => (nextBase_TypesString_Payload _).apply()
      case _artDataContent_DataTypeId.Base_TypesUnsigned_16_Payload_Id => (nextBase_TypesUnsigned_16_Payload _).apply()
      case _artDataContent_DataTypeId.Base_TypesUnsigned_32_Payload_Id => (nextBase_TypesUnsigned_32_Payload _).apply()
      case _artDataContent_DataTypeId.Base_TypesUnsigned_64_Payload_Id => (nextBase_TypesUnsigned_64_Payload _).apply()
      case _artDataContent_DataTypeId.Base_TypesUnsigned_8_Payload_Id => (nextBase_TypesUnsigned_8_Payload _).apply()
      case _artDataContent_DataTypeId.SWARP_Type_Payload_Id => (nextSWARP_Type_Payload _).apply()
      case _artDataContent_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P _).apply()
      case _artDataContent_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS _).apply()
      case _artDataContent_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P _).apply()
      case _artDataContent_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS _).apply()
      case _artDataContent_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P _).apply()
      case _artDataContent_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS _).apply()
      case _artDataContent_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P _).apply()
      case _artDataContent_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS _).apply()
      case _artDataContent_DataTypeId.SWFrameProtocol_Payload_Id => (nextSWFrameProtocol_Payload _).apply()
      case _artDataContent_DataTypeId.SWInternetProtocol_Payload_Id => (nextSWInternetProtocol_Payload _).apply()
      case _artDataContent_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P _).apply()
      case _artDataContent_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS _).apply()
      case _artDataContent_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P _).apply()
      case _artDataContent_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS _).apply()
      case _artDataContent_DataTypeId.SWRawEthernetMessage_Payload_Id => (nextSWRawEthernetMessage_Payload _).apply()
      case _artDataContent_DataTypeId.SWStructuredEthernetMessage_i_Payload_Id => (nextSWStructuredEthernetMessage_i_Payload _).apply()
      case _artDataContent_DataTypeId.utilEmptyContainer_Id => (nextutilEmptyContainer _).apply()
      case _ => halt("Invalid Child")
    }


    if(get_Config__artDataContent.attempts >= 0) {
     for(i <- 0 to get_Config__artDataContent.attempts) {
       if(get_Config__artDataContent.filter(v)) {
        return v
       }
       if (get_Config__artDataContent.verbose) {
         println(s"Retrying for failing value: $v")
       }
       c = callEnum(gen.nextZBetween(0, callEnum.size-1))

       v = c match {
         case _artDataContent_DataTypeId._artEmpty_Id => (next_artEmpty _).apply()
         case _artDataContent_DataTypeId.Base_TypesBits_Payload_Id => (nextBase_TypesBits_Payload _).apply()
         case _artDataContent_DataTypeId.Base_TypesBoolean_Payload_Id => (nextBase_TypesBoolean_Payload _).apply()
         case _artDataContent_DataTypeId.Base_TypesCharacter_Payload_Id => (nextBase_TypesCharacter_Payload _).apply()
         case _artDataContent_DataTypeId.Base_TypesFloat_32_Payload_Id => (nextBase_TypesFloat_32_Payload _).apply()
         case _artDataContent_DataTypeId.Base_TypesFloat_64_Payload_Id => (nextBase_TypesFloat_64_Payload _).apply()
         case _artDataContent_DataTypeId.Base_TypesFloat_Payload_Id => (nextBase_TypesFloat_Payload _).apply()
         case _artDataContent_DataTypeId.Base_TypesInteger_16_Payload_Id => (nextBase_TypesInteger_16_Payload _).apply()
         case _artDataContent_DataTypeId.Base_TypesInteger_32_Payload_Id => (nextBase_TypesInteger_32_Payload _).apply()
         case _artDataContent_DataTypeId.Base_TypesInteger_64_Payload_Id => (nextBase_TypesInteger_64_Payload _).apply()
         case _artDataContent_DataTypeId.Base_TypesInteger_8_Payload_Id => (nextBase_TypesInteger_8_Payload _).apply()
         case _artDataContent_DataTypeId.Base_TypesInteger_Payload_Id => (nextBase_TypesInteger_Payload _).apply()
         case _artDataContent_DataTypeId.Base_TypesString_Payload_Id => (nextBase_TypesString_Payload _).apply()
         case _artDataContent_DataTypeId.Base_TypesUnsigned_16_Payload_Id => (nextBase_TypesUnsigned_16_Payload _).apply()
         case _artDataContent_DataTypeId.Base_TypesUnsigned_32_Payload_Id => (nextBase_TypesUnsigned_32_Payload _).apply()
         case _artDataContent_DataTypeId.Base_TypesUnsigned_64_Payload_Id => (nextBase_TypesUnsigned_64_Payload _).apply()
         case _artDataContent_DataTypeId.Base_TypesUnsigned_8_Payload_Id => (nextBase_TypesUnsigned_8_Payload _).apply()
         case _artDataContent_DataTypeId.SWARP_Type_Payload_Id => (nextSWARP_Type_Payload _).apply()
         case _artDataContent_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P _).apply()
         case _artDataContent_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS _).apply()
         case _artDataContent_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P _).apply()
         case _artDataContent_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS _).apply()
         case _artDataContent_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P _).apply()
         case _artDataContent_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS _).apply()
         case _artDataContent_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P _).apply()
         case _artDataContent_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS _).apply()
         case _artDataContent_DataTypeId.SWFrameProtocol_Payload_Id => (nextSWFrameProtocol_Payload _).apply()
         case _artDataContent_DataTypeId.SWInternetProtocol_Payload_Id => (nextSWInternetProtocol_Payload _).apply()
         case _artDataContent_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P _).apply()
         case _artDataContent_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS _).apply()
         case _artDataContent_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P _).apply()
         case _artDataContent_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS _).apply()
         case _artDataContent_DataTypeId.SWRawEthernetMessage_Payload_Id => (nextSWRawEthernetMessage_Payload _).apply()
         case _artDataContent_DataTypeId.SWStructuredEthernetMessage_i_Payload_Id => (nextSWStructuredEthernetMessage_i_Payload _).apply()
         case _artDataContent_DataTypeId.utilEmptyContainer_Id => (nextutilEmptyContainer _).apply()
         case _ => halt("Invalid Child")
       }
     }
    } else {
     while(T) {
       if(get_Config__artDataContent.filter(v)) {
         return v
       }
       if (get_Config__artDataContent.verbose) {
         println(s"Retrying for failing value: $v")
       }
       c = callEnum(gen.nextZBetween(0, callEnum.size-1))

       v = c match {
         case _artDataContent_DataTypeId._artEmpty_Id => (next_artEmpty _).apply()
         case _artDataContent_DataTypeId.Base_TypesBits_Payload_Id => (nextBase_TypesBits_Payload _).apply()
         case _artDataContent_DataTypeId.Base_TypesBoolean_Payload_Id => (nextBase_TypesBoolean_Payload _).apply()
         case _artDataContent_DataTypeId.Base_TypesCharacter_Payload_Id => (nextBase_TypesCharacter_Payload _).apply()
         case _artDataContent_DataTypeId.Base_TypesFloat_32_Payload_Id => (nextBase_TypesFloat_32_Payload _).apply()
         case _artDataContent_DataTypeId.Base_TypesFloat_64_Payload_Id => (nextBase_TypesFloat_64_Payload _).apply()
         case _artDataContent_DataTypeId.Base_TypesFloat_Payload_Id => (nextBase_TypesFloat_Payload _).apply()
         case _artDataContent_DataTypeId.Base_TypesInteger_16_Payload_Id => (nextBase_TypesInteger_16_Payload _).apply()
         case _artDataContent_DataTypeId.Base_TypesInteger_32_Payload_Id => (nextBase_TypesInteger_32_Payload _).apply()
         case _artDataContent_DataTypeId.Base_TypesInteger_64_Payload_Id => (nextBase_TypesInteger_64_Payload _).apply()
         case _artDataContent_DataTypeId.Base_TypesInteger_8_Payload_Id => (nextBase_TypesInteger_8_Payload _).apply()
         case _artDataContent_DataTypeId.Base_TypesInteger_Payload_Id => (nextBase_TypesInteger_Payload _).apply()
         case _artDataContent_DataTypeId.Base_TypesString_Payload_Id => (nextBase_TypesString_Payload _).apply()
         case _artDataContent_DataTypeId.Base_TypesUnsigned_16_Payload_Id => (nextBase_TypesUnsigned_16_Payload _).apply()
         case _artDataContent_DataTypeId.Base_TypesUnsigned_32_Payload_Id => (nextBase_TypesUnsigned_32_Payload _).apply()
         case _artDataContent_DataTypeId.Base_TypesUnsigned_64_Payload_Id => (nextBase_TypesUnsigned_64_Payload _).apply()
         case _artDataContent_DataTypeId.Base_TypesUnsigned_8_Payload_Id => (nextBase_TypesUnsigned_8_Payload _).apply()
         case _artDataContent_DataTypeId.SWARP_Type_Payload_Id => (nextSWARP_Type_Payload _).apply()
         case _artDataContent_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P _).apply()
         case _artDataContent_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS _).apply()
         case _artDataContent_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P _).apply()
         case _artDataContent_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS _).apply()
         case _artDataContent_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P _).apply()
         case _artDataContent_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS _).apply()
         case _artDataContent_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P _).apply()
         case _artDataContent_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS _).apply()
         case _artDataContent_DataTypeId.SWFrameProtocol_Payload_Id => (nextSWFrameProtocol_Payload _).apply()
         case _artDataContent_DataTypeId.SWInternetProtocol_Payload_Id => (nextSWInternetProtocol_Payload _).apply()
         case _artDataContent_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P _).apply()
         case _artDataContent_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS _).apply()
         case _artDataContent_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P _).apply()
         case _artDataContent_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS _).apply()
         case _artDataContent_DataTypeId.SWRawEthernetMessage_Payload_Id => (nextSWRawEthernetMessage_Payload _).apply()
         case _artDataContent_DataTypeId.SWStructuredEthernetMessage_i_Payload_Id => (nextSWStructuredEthernetMessage_i_Payload _).apply()
         case _artDataContent_DataTypeId.utilEmptyContainer_Id => (nextutilEmptyContainer _).apply()
         case _ => halt("Invalid Child")
       }
     }
    }
    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= art.Empty ===================

  def get_Config__artEmpty: Config__artEmpty
  def set_Config__artEmpty(config: Config__artEmpty): RandomLib

  def next_artEmpty(): art.Empty = {

    var v: art.Empty = art.Empty()

    if(get_Config__artEmpty.attempts >= 0) {
     for(i <- 0 to get_Config__artEmpty.attempts) {
        if(get_Config__artEmpty.filter(v)) {
          return v
        }
        if (get_Config__artEmpty.verbose) {
          println(s"Retrying for failing value: $v")
        }
        v = art.Empty()
     }
    } else {
     while(T) {
       if(get_Config__artEmpty.filter(v)) {
         return v
       }
       if (get_Config__artEmpty.verbose) {
         println(s"Retrying for failing value: $v")
       }
       v = art.Empty()
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= Base_Types.Boolean_Payload ===================

  def get_Config_Base_TypesBoolean_Payload: Config_Base_TypesBoolean_Payload
  def set_Config_Base_TypesBoolean_Payload(config: Config_Base_TypesBoolean_Payload): RandomLib

  def nextBase_TypesBoolean_Payload(): Base_Types.Boolean_Payload = {
    var value: B = nextB()

    var v: Base_Types.Boolean_Payload = Base_Types.Boolean_Payload(value)

    if(get_Config_Base_TypesBoolean_Payload.attempts >= 0) {
     for(i <- 0 to get_Config_Base_TypesBoolean_Payload.attempts) {
        if(get_Config_Base_TypesBoolean_Payload.filter(v)) {
          return v
        }
        if (get_Config_Base_TypesBoolean_Payload.verbose) {
          println(s"Retrying for failing value: $v")
        }
        value = nextB()
        v = Base_Types.Boolean_Payload(value)
     }
    } else {
     while(T) {
       if(get_Config_Base_TypesBoolean_Payload.filter(v)) {
         return v
       }
       if (get_Config_Base_TypesBoolean_Payload.verbose) {
         println(s"Retrying for failing value: $v")
       }
       value = nextB()
       v = Base_Types.Boolean_Payload(value)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= Base_Types.Integer_Payload ===================

  def get_Config_Base_TypesInteger_Payload: Config_Base_TypesInteger_Payload
  def set_Config_Base_TypesInteger_Payload(config: Config_Base_TypesInteger_Payload): RandomLib

  def nextBase_TypesInteger_Payload(): Base_Types.Integer_Payload = {
    var value: Z = nextZ()

    var v: Base_Types.Integer_Payload = Base_Types.Integer_Payload(value)

    if(get_Config_Base_TypesInteger_Payload.attempts >= 0) {
     for(i <- 0 to get_Config_Base_TypesInteger_Payload.attempts) {
        if(get_Config_Base_TypesInteger_Payload.filter(v)) {
          return v
        }
        if (get_Config_Base_TypesInteger_Payload.verbose) {
          println(s"Retrying for failing value: $v")
        }
        value = nextZ()
        v = Base_Types.Integer_Payload(value)
     }
    } else {
     while(T) {
       if(get_Config_Base_TypesInteger_Payload.filter(v)) {
         return v
       }
       if (get_Config_Base_TypesInteger_Payload.verbose) {
         println(s"Retrying for failing value: $v")
       }
       value = nextZ()
       v = Base_Types.Integer_Payload(value)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= Base_Types.Integer_8_Payload ===================

  def get_Config_Base_TypesInteger_8_Payload: Config_Base_TypesInteger_8_Payload
  def set_Config_Base_TypesInteger_8_Payload(config: Config_Base_TypesInteger_8_Payload): RandomLib

  def nextBase_TypesInteger_8_Payload(): Base_Types.Integer_8_Payload = {
    var value: S8 = nextS8()

    var v: Base_Types.Integer_8_Payload = Base_Types.Integer_8_Payload(value)

    if(get_Config_Base_TypesInteger_8_Payload.attempts >= 0) {
     for(i <- 0 to get_Config_Base_TypesInteger_8_Payload.attempts) {
        if(get_Config_Base_TypesInteger_8_Payload.filter(v)) {
          return v
        }
        if (get_Config_Base_TypesInteger_8_Payload.verbose) {
          println(s"Retrying for failing value: $v")
        }
        value = nextS8()
        v = Base_Types.Integer_8_Payload(value)
     }
    } else {
     while(T) {
       if(get_Config_Base_TypesInteger_8_Payload.filter(v)) {
         return v
       }
       if (get_Config_Base_TypesInteger_8_Payload.verbose) {
         println(s"Retrying for failing value: $v")
       }
       value = nextS8()
       v = Base_Types.Integer_8_Payload(value)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= Base_Types.Integer_16_Payload ===================

  def get_Config_Base_TypesInteger_16_Payload: Config_Base_TypesInteger_16_Payload
  def set_Config_Base_TypesInteger_16_Payload(config: Config_Base_TypesInteger_16_Payload): RandomLib

  def nextBase_TypesInteger_16_Payload(): Base_Types.Integer_16_Payload = {
    var value: S16 = nextS16()

    var v: Base_Types.Integer_16_Payload = Base_Types.Integer_16_Payload(value)

    if(get_Config_Base_TypesInteger_16_Payload.attempts >= 0) {
     for(i <- 0 to get_Config_Base_TypesInteger_16_Payload.attempts) {
        if(get_Config_Base_TypesInteger_16_Payload.filter(v)) {
          return v
        }
        if (get_Config_Base_TypesInteger_16_Payload.verbose) {
          println(s"Retrying for failing value: $v")
        }
        value = nextS16()
        v = Base_Types.Integer_16_Payload(value)
     }
    } else {
     while(T) {
       if(get_Config_Base_TypesInteger_16_Payload.filter(v)) {
         return v
       }
       if (get_Config_Base_TypesInteger_16_Payload.verbose) {
         println(s"Retrying for failing value: $v")
       }
       value = nextS16()
       v = Base_Types.Integer_16_Payload(value)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= Base_Types.Integer_32_Payload ===================

  def get_Config_Base_TypesInteger_32_Payload: Config_Base_TypesInteger_32_Payload
  def set_Config_Base_TypesInteger_32_Payload(config: Config_Base_TypesInteger_32_Payload): RandomLib

  def nextBase_TypesInteger_32_Payload(): Base_Types.Integer_32_Payload = {
    var value: S32 = nextS32()

    var v: Base_Types.Integer_32_Payload = Base_Types.Integer_32_Payload(value)

    if(get_Config_Base_TypesInteger_32_Payload.attempts >= 0) {
     for(i <- 0 to get_Config_Base_TypesInteger_32_Payload.attempts) {
        if(get_Config_Base_TypesInteger_32_Payload.filter(v)) {
          return v
        }
        if (get_Config_Base_TypesInteger_32_Payload.verbose) {
          println(s"Retrying for failing value: $v")
        }
        value = nextS32()
        v = Base_Types.Integer_32_Payload(value)
     }
    } else {
     while(T) {
       if(get_Config_Base_TypesInteger_32_Payload.filter(v)) {
         return v
       }
       if (get_Config_Base_TypesInteger_32_Payload.verbose) {
         println(s"Retrying for failing value: $v")
       }
       value = nextS32()
       v = Base_Types.Integer_32_Payload(value)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= Base_Types.Integer_64_Payload ===================

  def get_Config_Base_TypesInteger_64_Payload: Config_Base_TypesInteger_64_Payload
  def set_Config_Base_TypesInteger_64_Payload(config: Config_Base_TypesInteger_64_Payload): RandomLib

  def nextBase_TypesInteger_64_Payload(): Base_Types.Integer_64_Payload = {
    var value: S64 = nextS64()

    var v: Base_Types.Integer_64_Payload = Base_Types.Integer_64_Payload(value)

    if(get_Config_Base_TypesInteger_64_Payload.attempts >= 0) {
     for(i <- 0 to get_Config_Base_TypesInteger_64_Payload.attempts) {
        if(get_Config_Base_TypesInteger_64_Payload.filter(v)) {
          return v
        }
        if (get_Config_Base_TypesInteger_64_Payload.verbose) {
          println(s"Retrying for failing value: $v")
        }
        value = nextS64()
        v = Base_Types.Integer_64_Payload(value)
     }
    } else {
     while(T) {
       if(get_Config_Base_TypesInteger_64_Payload.filter(v)) {
         return v
       }
       if (get_Config_Base_TypesInteger_64_Payload.verbose) {
         println(s"Retrying for failing value: $v")
       }
       value = nextS64()
       v = Base_Types.Integer_64_Payload(value)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= Base_Types.Unsigned_8_Payload ===================

  def get_Config_Base_TypesUnsigned_8_Payload: Config_Base_TypesUnsigned_8_Payload
  def set_Config_Base_TypesUnsigned_8_Payload(config: Config_Base_TypesUnsigned_8_Payload): RandomLib

  def nextBase_TypesUnsigned_8_Payload(): Base_Types.Unsigned_8_Payload = {
    var value: U8 = nextU8()

    var v: Base_Types.Unsigned_8_Payload = Base_Types.Unsigned_8_Payload(value)

    if(get_Config_Base_TypesUnsigned_8_Payload.attempts >= 0) {
     for(i <- 0 to get_Config_Base_TypesUnsigned_8_Payload.attempts) {
        if(get_Config_Base_TypesUnsigned_8_Payload.filter(v)) {
          return v
        }
        if (get_Config_Base_TypesUnsigned_8_Payload.verbose) {
          println(s"Retrying for failing value: $v")
        }
        value = nextU8()
        v = Base_Types.Unsigned_8_Payload(value)
     }
    } else {
     while(T) {
       if(get_Config_Base_TypesUnsigned_8_Payload.filter(v)) {
         return v
       }
       if (get_Config_Base_TypesUnsigned_8_Payload.verbose) {
         println(s"Retrying for failing value: $v")
       }
       value = nextU8()
       v = Base_Types.Unsigned_8_Payload(value)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= Base_Types.Unsigned_16_Payload ===================

  def get_Config_Base_TypesUnsigned_16_Payload: Config_Base_TypesUnsigned_16_Payload
  def set_Config_Base_TypesUnsigned_16_Payload(config: Config_Base_TypesUnsigned_16_Payload): RandomLib

  def nextBase_TypesUnsigned_16_Payload(): Base_Types.Unsigned_16_Payload = {
    var value: U16 = nextU16()

    var v: Base_Types.Unsigned_16_Payload = Base_Types.Unsigned_16_Payload(value)

    if(get_Config_Base_TypesUnsigned_16_Payload.attempts >= 0) {
     for(i <- 0 to get_Config_Base_TypesUnsigned_16_Payload.attempts) {
        if(get_Config_Base_TypesUnsigned_16_Payload.filter(v)) {
          return v
        }
        if (get_Config_Base_TypesUnsigned_16_Payload.verbose) {
          println(s"Retrying for failing value: $v")
        }
        value = nextU16()
        v = Base_Types.Unsigned_16_Payload(value)
     }
    } else {
     while(T) {
       if(get_Config_Base_TypesUnsigned_16_Payload.filter(v)) {
         return v
       }
       if (get_Config_Base_TypesUnsigned_16_Payload.verbose) {
         println(s"Retrying for failing value: $v")
       }
       value = nextU16()
       v = Base_Types.Unsigned_16_Payload(value)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= Base_Types.Unsigned_32_Payload ===================

  def get_Config_Base_TypesUnsigned_32_Payload: Config_Base_TypesUnsigned_32_Payload
  def set_Config_Base_TypesUnsigned_32_Payload(config: Config_Base_TypesUnsigned_32_Payload): RandomLib

  def nextBase_TypesUnsigned_32_Payload(): Base_Types.Unsigned_32_Payload = {
    var value: U32 = nextU32()

    var v: Base_Types.Unsigned_32_Payload = Base_Types.Unsigned_32_Payload(value)

    if(get_Config_Base_TypesUnsigned_32_Payload.attempts >= 0) {
     for(i <- 0 to get_Config_Base_TypesUnsigned_32_Payload.attempts) {
        if(get_Config_Base_TypesUnsigned_32_Payload.filter(v)) {
          return v
        }
        if (get_Config_Base_TypesUnsigned_32_Payload.verbose) {
          println(s"Retrying for failing value: $v")
        }
        value = nextU32()
        v = Base_Types.Unsigned_32_Payload(value)
     }
    } else {
     while(T) {
       if(get_Config_Base_TypesUnsigned_32_Payload.filter(v)) {
         return v
       }
       if (get_Config_Base_TypesUnsigned_32_Payload.verbose) {
         println(s"Retrying for failing value: $v")
       }
       value = nextU32()
       v = Base_Types.Unsigned_32_Payload(value)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= Base_Types.Unsigned_64_Payload ===================

  def get_Config_Base_TypesUnsigned_64_Payload: Config_Base_TypesUnsigned_64_Payload
  def set_Config_Base_TypesUnsigned_64_Payload(config: Config_Base_TypesUnsigned_64_Payload): RandomLib

  def nextBase_TypesUnsigned_64_Payload(): Base_Types.Unsigned_64_Payload = {
    var value: U64 = nextU64()

    var v: Base_Types.Unsigned_64_Payload = Base_Types.Unsigned_64_Payload(value)

    if(get_Config_Base_TypesUnsigned_64_Payload.attempts >= 0) {
     for(i <- 0 to get_Config_Base_TypesUnsigned_64_Payload.attempts) {
        if(get_Config_Base_TypesUnsigned_64_Payload.filter(v)) {
          return v
        }
        if (get_Config_Base_TypesUnsigned_64_Payload.verbose) {
          println(s"Retrying for failing value: $v")
        }
        value = nextU64()
        v = Base_Types.Unsigned_64_Payload(value)
     }
    } else {
     while(T) {
       if(get_Config_Base_TypesUnsigned_64_Payload.filter(v)) {
         return v
       }
       if (get_Config_Base_TypesUnsigned_64_Payload.verbose) {
         println(s"Retrying for failing value: $v")
       }
       value = nextU64()
       v = Base_Types.Unsigned_64_Payload(value)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= Base_Types.Float_Payload ===================

  def get_Config_Base_TypesFloat_Payload: Config_Base_TypesFloat_Payload
  def set_Config_Base_TypesFloat_Payload(config: Config_Base_TypesFloat_Payload): RandomLib

  def nextBase_TypesFloat_Payload(): Base_Types.Float_Payload = {
    var value: R = nextR()

    var v: Base_Types.Float_Payload = Base_Types.Float_Payload(value)

    if(get_Config_Base_TypesFloat_Payload.attempts >= 0) {
     for(i <- 0 to get_Config_Base_TypesFloat_Payload.attempts) {
        if(get_Config_Base_TypesFloat_Payload.filter(v)) {
          return v
        }
        if (get_Config_Base_TypesFloat_Payload.verbose) {
          println(s"Retrying for failing value: $v")
        }
        value = nextR()
        v = Base_Types.Float_Payload(value)
     }
    } else {
     while(T) {
       if(get_Config_Base_TypesFloat_Payload.filter(v)) {
         return v
       }
       if (get_Config_Base_TypesFloat_Payload.verbose) {
         println(s"Retrying for failing value: $v")
       }
       value = nextR()
       v = Base_Types.Float_Payload(value)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= Base_Types.Float_32_Payload ===================

  def get_Config_Base_TypesFloat_32_Payload: Config_Base_TypesFloat_32_Payload
  def set_Config_Base_TypesFloat_32_Payload(config: Config_Base_TypesFloat_32_Payload): RandomLib

  def nextBase_TypesFloat_32_Payload(): Base_Types.Float_32_Payload = {
    var value: F32 = nextF32()

    var v: Base_Types.Float_32_Payload = Base_Types.Float_32_Payload(value)

    if(get_Config_Base_TypesFloat_32_Payload.attempts >= 0) {
     for(i <- 0 to get_Config_Base_TypesFloat_32_Payload.attempts) {
        if(get_Config_Base_TypesFloat_32_Payload.filter(v)) {
          return v
        }
        if (get_Config_Base_TypesFloat_32_Payload.verbose) {
          println(s"Retrying for failing value: $v")
        }
        value = nextF32()
        v = Base_Types.Float_32_Payload(value)
     }
    } else {
     while(T) {
       if(get_Config_Base_TypesFloat_32_Payload.filter(v)) {
         return v
       }
       if (get_Config_Base_TypesFloat_32_Payload.verbose) {
         println(s"Retrying for failing value: $v")
       }
       value = nextF32()
       v = Base_Types.Float_32_Payload(value)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= Base_Types.Float_64_Payload ===================

  def get_Config_Base_TypesFloat_64_Payload: Config_Base_TypesFloat_64_Payload
  def set_Config_Base_TypesFloat_64_Payload(config: Config_Base_TypesFloat_64_Payload): RandomLib

  def nextBase_TypesFloat_64_Payload(): Base_Types.Float_64_Payload = {
    var value: F64 = nextF64()

    var v: Base_Types.Float_64_Payload = Base_Types.Float_64_Payload(value)

    if(get_Config_Base_TypesFloat_64_Payload.attempts >= 0) {
     for(i <- 0 to get_Config_Base_TypesFloat_64_Payload.attempts) {
        if(get_Config_Base_TypesFloat_64_Payload.filter(v)) {
          return v
        }
        if (get_Config_Base_TypesFloat_64_Payload.verbose) {
          println(s"Retrying for failing value: $v")
        }
        value = nextF64()
        v = Base_Types.Float_64_Payload(value)
     }
    } else {
     while(T) {
       if(get_Config_Base_TypesFloat_64_Payload.filter(v)) {
         return v
       }
       if (get_Config_Base_TypesFloat_64_Payload.verbose) {
         println(s"Retrying for failing value: $v")
       }
       value = nextF64()
       v = Base_Types.Float_64_Payload(value)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= Base_Types.Character_Payload ===================

  def get_Config_Base_TypesCharacter_Payload: Config_Base_TypesCharacter_Payload
  def set_Config_Base_TypesCharacter_Payload(config: Config_Base_TypesCharacter_Payload): RandomLib

  def nextBase_TypesCharacter_Payload(): Base_Types.Character_Payload = {
    var value: C = nextC()

    var v: Base_Types.Character_Payload = Base_Types.Character_Payload(value)

    if(get_Config_Base_TypesCharacter_Payload.attempts >= 0) {
     for(i <- 0 to get_Config_Base_TypesCharacter_Payload.attempts) {
        if(get_Config_Base_TypesCharacter_Payload.filter(v)) {
          return v
        }
        if (get_Config_Base_TypesCharacter_Payload.verbose) {
          println(s"Retrying for failing value: $v")
        }
        value = nextC()
        v = Base_Types.Character_Payload(value)
     }
    } else {
     while(T) {
       if(get_Config_Base_TypesCharacter_Payload.filter(v)) {
         return v
       }
       if (get_Config_Base_TypesCharacter_Payload.verbose) {
         println(s"Retrying for failing value: $v")
       }
       value = nextC()
       v = Base_Types.Character_Payload(value)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= Base_Types.String_Payload ===================

  def get_Config_Base_TypesString_Payload: Config_Base_TypesString_Payload
  def set_Config_Base_TypesString_Payload(config: Config_Base_TypesString_Payload): RandomLib

  def nextBase_TypesString_Payload(): Base_Types.String_Payload = {
    var value: String = nextString()

    var v: Base_Types.String_Payload = Base_Types.String_Payload(value)

    if(get_Config_Base_TypesString_Payload.attempts >= 0) {
     for(i <- 0 to get_Config_Base_TypesString_Payload.attempts) {
        if(get_Config_Base_TypesString_Payload.filter(v)) {
          return v
        }
        if (get_Config_Base_TypesString_Payload.verbose) {
          println(s"Retrying for failing value: $v")
        }
        value = nextString()
        v = Base_Types.String_Payload(value)
     }
    } else {
     while(T) {
       if(get_Config_Base_TypesString_Payload.filter(v)) {
         return v
       }
       if (get_Config_Base_TypesString_Payload.verbose) {
         println(s"Retrying for failing value: $v")
       }
       value = nextString()
       v = Base_Types.String_Payload(value)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  //=================== ISZ[B] =====================
  def get_Config_ISZB: Config_ISZB
  def set_Config_ISZB(config: Config_ISZB): RandomLib

  def nextISZB(): ISZ[B] = {

    var length: Z = gen.nextZBetween(0, get_numElement)
    var v: ISZ[B] = ISZ()
    for (r <- 0 until length) {
      v = v :+ nextB()
    }

    if(get_Config_ISZB.attempts >= 0) {
     for(i <- 0 to get_Config_ISZB.attempts) {
        if(get_Config_ISZB.filter(v)) {
          return v
        }
        if (get_Config_ISZB.verbose) {
          println(s"Retrying for failing value: $v")
        }

        length = gen.nextZBetween(0, get_numElement)
        v = ISZ()
        for (r <- 0 until length) {
           v = v :+ nextB()
        }
     }
    } else {
     while(T) {
       if(get_Config_ISZB.filter(v)) {
         return v
       }
       if (get_Config_ISZB.verbose) {
         println(s"Retrying for failing value: $v")
       }

       length = gen.nextZBetween(0, get_numElement)
       v = ISZ()
       for (r <- 0 until length) {
          v = v :+ nextB()
       }
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= Base_Types.Bits_Payload ===================

  def get_Config_Base_TypesBits_Payload: Config_Base_TypesBits_Payload
  def set_Config_Base_TypesBits_Payload(config: Config_Base_TypesBits_Payload): RandomLib

  def nextBase_TypesBits_Payload(): Base_Types.Bits_Payload = {
    var value: ISZ[B] = nextISZB()

    var v: Base_Types.Bits_Payload = Base_Types.Bits_Payload(value)

    if(get_Config_Base_TypesBits_Payload.attempts >= 0) {
     for(i <- 0 to get_Config_Base_TypesBits_Payload.attempts) {
        if(get_Config_Base_TypesBits_Payload.filter(v)) {
          return v
        }
        if (get_Config_Base_TypesBits_Payload.verbose) {
          println(s"Retrying for failing value: $v")
        }
        value = nextISZB()
        v = Base_Types.Bits_Payload(value)
     }
    } else {
     while(T) {
       if(get_Config_Base_TypesBits_Payload.filter(v)) {
         return v
       }
       if (get_Config_Base_TypesBits_Payload.verbose) {
         println(s"Retrying for failing value: $v")
       }
       value = nextISZB()
       v = Base_Types.Bits_Payload(value)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= SW.ARP_Type.Type ===================

  def get_Config_SWARP_TypeType: Config_SWARP_TypeType
  def set_Config_SWARP_TypeType(config: Config_SWARP_TypeType): RandomLib

  def nextSWARP_TypeType(): SW.ARP_Type.Type = {

    var ordinal: Z = gen.nextZBetween(0, base.SW.ARP_Type.numOfElements-1)

    var v: SW.ARP_Type.Type = base.SW.ARP_Type.byOrdinal(ordinal).get
    if(get_Config_SWARP_TypeType.attempts >= 0) {
     for(i <- 0 to get_Config_SWARP_TypeType.attempts) {
       if(get_Config_SWARP_TypeType.filter(v)) {
        return v
       }
       if (get_Config_SWARP_TypeType.verbose) {
         println(s"Retrying for failing value: $v")
       }
       ordinal= gen.nextZBetween(0, base.SW.ARP_Type.numOfElements-1)
       v = base.SW.ARP_Type.byOrdinal(ordinal).get
     }
    } else {
     while(T){
       if(get_Config_SWARP_TypeType.filter(v)) {
        return v
       }
       if (get_Config_SWARP_TypeType.verbose) {
         println(s"Retrying for failing value: $v")
       }
       ordinal= gen.nextZBetween(0, base.SW.ARP_Type.numOfElements-1)
       v = base.SW.ARP_Type.byOrdinal(ordinal).get
     }
    }
    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= SW.ARP_Type_Payload ===================

  def get_Config_SWARP_Type_Payload: Config_SWARP_Type_Payload
  def set_Config_SWARP_Type_Payload(config: Config_SWARP_Type_Payload): RandomLib

  def nextSWARP_Type_Payload(): SW.ARP_Type_Payload = {
    var value: SW.ARP_Type.Type = nextSWARP_TypeType()

    var v: SW.ARP_Type_Payload = SW.ARP_Type_Payload(value)

    if(get_Config_SWARP_Type_Payload.attempts >= 0) {
     for(i <- 0 to get_Config_SWARP_Type_Payload.attempts) {
        if(get_Config_SWARP_Type_Payload.filter(v)) {
          return v
        }
        if (get_Config_SWARP_Type_Payload.verbose) {
          println(s"Retrying for failing value: $v")
        }
        value = nextSWARP_TypeType()
        v = SW.ARP_Type_Payload(value)
     }
    } else {
     while(T) {
       if(get_Config_SWARP_Type_Payload.filter(v)) {
         return v
       }
       if (get_Config_SWARP_Type_Payload.verbose) {
         println(s"Retrying for failing value: $v")
       }
       value = nextSWARP_TypeType()
       v = SW.ARP_Type_Payload(value)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container ===================

  def get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container: Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container
  def set_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container(config: Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container): RandomLib

  def nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container(): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container = {
    var callEnum: ISZ[SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_DataTypeId.Type] = ISZ(SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P_Id, SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS_Id)

    if(get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container.additiveTypeFiltering) {
       callEnum = get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container.typeFilter
    } else {
       for(h <- get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container.typeFilter) {
         callEnum = ops.ISZOps(callEnum).filter(f => h =!= f)
       }
    }

    var c = callEnum(gen.nextZBetween(0, callEnum.size-1))

    var v: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container = c match {
      case SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P _).apply()
      case SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS _).apply()
      case _ => halt("Invalid Child")
    }


    if(get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container.attempts >= 0) {
     for(i <- 0 to get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container.attempts) {
       if(get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container.filter(v)) {
        return v
       }
       if (get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container.verbose) {
         println(s"Retrying for failing value: $v")
       }
       c = callEnum(gen.nextZBetween(0, callEnum.size-1))

       v = c match {
         case SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P _).apply()
         case SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS _).apply()
         case _ => halt("Invalid Child")
       }
     }
    } else {
     while(T) {
       if(get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container.filter(v)) {
         return v
       }
       if (get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container.verbose) {
         println(s"Retrying for failing value: $v")
       }
       c = callEnum(gen.nextZBetween(0, callEnum.size-1))

       v = c match {
         case SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P _).apply()
         case SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS _).apply()
         case _ => halt("Invalid Child")
       }
     }
    }
    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  //=================== Option[SW.StructuredEthernetMessage_i] =====================
  def get_Config_OptionSWStructuredEthernetMessage_i: Config_OptionSWStructuredEthernetMessage_i
  def set_Config_OptionSWStructuredEthernetMessage_i(config: Config_OptionSWStructuredEthernetMessage_i): RandomLib

  def nextOptionSWStructuredEthernetMessage_i(): Option[SW.StructuredEthernetMessage_i] = {

    var none: Z = gen.nextZBetween(0,1)
    var v: Option[SW.StructuredEthernetMessage_i] = if(none == 0) {
      Some(nextSWStructuredEthernetMessage_i())
    } else {
      None()
    }

    if(get_Config_OptionSWStructuredEthernetMessage_i.attempts >= 0) {
     for(i <- 0 to get_Config_OptionSWStructuredEthernetMessage_i.attempts) {
        if(get_Config_OptionSWStructuredEthernetMessage_i.filter(v)) {
          return v
        }
        if (get_Config_OptionSWStructuredEthernetMessage_i.verbose) {
          println(s"Retrying for failing value: $v")
        }

        none = gen.nextZBetween(0,1)
        v = if(none == 0) {
           Some(nextSWStructuredEthernetMessage_i())
        } else {
           None()
        }
     }
    } else {
     while(T) {
       if(get_Config_OptionSWStructuredEthernetMessage_i.filter(v)) {
         return v
       }
       if (get_Config_OptionSWStructuredEthernetMessage_i.verbose) {
         println(s"Retrying for failing value: $v")
       }

       none = gen.nextZBetween(0,1)
       v = if(none == 0) {
          Some(nextSWStructuredEthernetMessage_i())
       } else {
          None()
       }
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P ===================

  def get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P: Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P
  def set_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P(config: Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P): RandomLib

  def nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P(): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P = {
    var api_EthernetFramesRx: Option[SW.StructuredEthernetMessage_i] = nextOptionSWStructuredEthernetMessage_i()

    var v: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P = SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P(api_EthernetFramesRx)

    if(get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P.attempts >= 0) {
     for(i <- 0 to get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P.attempts) {
        if(get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P.filter(v)) {
          return v
        }
        if (get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P.verbose) {
          println(s"Retrying for failing value: $v")
        }
        api_EthernetFramesRx = nextOptionSWStructuredEthernetMessage_i()
        v = SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P(api_EthernetFramesRx)
     }
    } else {
     while(T) {
       if(get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P.filter(v)) {
         return v
       }
       if (get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P.verbose) {
         println(s"Retrying for failing value: $v")
       }
       api_EthernetFramesRx = nextOptionSWStructuredEthernetMessage_i()
       v = SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P(api_EthernetFramesRx)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS ===================

  def get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS: Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS
  def set_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS(config: Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS): RandomLib

  def nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS(): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS = {
    var api_EthernetFramesRx: Option[SW.StructuredEthernetMessage_i] = nextOptionSWStructuredEthernetMessage_i()

    var v: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS = SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS(api_EthernetFramesRx)

    if(get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS.attempts >= 0) {
     for(i <- 0 to get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS.attempts) {
        if(get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS.filter(v)) {
          return v
        }
        if (get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS.verbose) {
          println(s"Retrying for failing value: $v")
        }
        api_EthernetFramesRx = nextOptionSWStructuredEthernetMessage_i()
        v = SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS(api_EthernetFramesRx)
     }
    } else {
     while(T) {
       if(get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS.filter(v)) {
         return v
       }
       if (get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS.verbose) {
         println(s"Retrying for failing value: $v")
       }
       api_EthernetFramesRx = nextOptionSWStructuredEthernetMessage_i()
       v = SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS(api_EthernetFramesRx)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container ===================

  def get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container: Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container
  def set_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container(config: Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container): RandomLib

  def nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container(): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container = {
    var callEnum: ISZ[SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_DataTypeId.Type] = ISZ(SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P_Id, SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS_Id)

    if(get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container.additiveTypeFiltering) {
       callEnum = get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container.typeFilter
    } else {
       for(h <- get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container.typeFilter) {
         callEnum = ops.ISZOps(callEnum).filter(f => h =!= f)
       }
    }

    var c = callEnum(gen.nextZBetween(0, callEnum.size-1))

    var v: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container = c match {
      case SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P _).apply()
      case SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS _).apply()
      case _ => halt("Invalid Child")
    }


    if(get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container.attempts >= 0) {
     for(i <- 0 to get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container.attempts) {
       if(get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container.filter(v)) {
        return v
       }
       if (get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container.verbose) {
         println(s"Retrying for failing value: $v")
       }
       c = callEnum(gen.nextZBetween(0, callEnum.size-1))

       v = c match {
         case SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P _).apply()
         case SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS _).apply()
         case _ => halt("Invalid Child")
       }
     }
    } else {
     while(T) {
       if(get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container.filter(v)) {
         return v
       }
       if (get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container.verbose) {
         println(s"Retrying for failing value: $v")
       }
       c = callEnum(gen.nextZBetween(0, callEnum.size-1))

       v = c match {
         case SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P _).apply()
         case SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS _).apply()
         case _ => halt("Invalid Child")
       }
     }
    }
    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P ===================

  def get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P: Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P
  def set_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P(config: Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P): RandomLib

  def nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P(): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P = {
    var api_EthernetFramesTx: Option[SW.StructuredEthernetMessage_i] = nextOptionSWStructuredEthernetMessage_i()

    var v: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P = SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P(api_EthernetFramesTx)

    if(get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P.attempts >= 0) {
     for(i <- 0 to get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P.attempts) {
        if(get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P.filter(v)) {
          return v
        }
        if (get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P.verbose) {
          println(s"Retrying for failing value: $v")
        }
        api_EthernetFramesTx = nextOptionSWStructuredEthernetMessage_i()
        v = SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P(api_EthernetFramesTx)
     }
    } else {
     while(T) {
       if(get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P.filter(v)) {
         return v
       }
       if (get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P.verbose) {
         println(s"Retrying for failing value: $v")
       }
       api_EthernetFramesTx = nextOptionSWStructuredEthernetMessage_i()
       v = SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P(api_EthernetFramesTx)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS ===================

  def get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS: Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS
  def set_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS(config: Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS): RandomLib

  def nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS(): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS = {
    var api_EthernetFramesTx: Option[SW.StructuredEthernetMessage_i] = nextOptionSWStructuredEthernetMessage_i()

    var v: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS = SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS(api_EthernetFramesTx)

    if(get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS.attempts >= 0) {
     for(i <- 0 to get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS.attempts) {
        if(get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS.filter(v)) {
          return v
        }
        if (get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS.verbose) {
          println(s"Retrying for failing value: $v")
        }
        api_EthernetFramesTx = nextOptionSWStructuredEthernetMessage_i()
        v = SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS(api_EthernetFramesTx)
     }
    } else {
     while(T) {
       if(get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS.filter(v)) {
         return v
       }
       if (get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS.verbose) {
         println(s"Retrying for failing value: $v")
       }
       api_EthernetFramesTx = nextOptionSWStructuredEthernetMessage_i()
       v = SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS(api_EthernetFramesTx)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container ===================

  def get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container: Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container
  def set_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container(config: Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container): RandomLib

  def nextSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container(): SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container = {
    var callEnum: ISZ[SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_DataTypeId.Type] = ISZ(SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P_Id, SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS_Id)

    if(get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container.additiveTypeFiltering) {
       callEnum = get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container.typeFilter
    } else {
       for(h <- get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container.typeFilter) {
         callEnum = ops.ISZOps(callEnum).filter(f => h =!= f)
       }
    }

    var c = callEnum(gen.nextZBetween(0, callEnum.size-1))

    var v: SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container = c match {
      case SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P _).apply()
      case SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS _).apply()
      case _ => halt("Invalid Child")
    }


    if(get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container.attempts >= 0) {
     for(i <- 0 to get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container.attempts) {
       if(get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container.filter(v)) {
        return v
       }
       if (get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container.verbose) {
         println(s"Retrying for failing value: $v")
       }
       c = callEnum(gen.nextZBetween(0, callEnum.size-1))

       v = c match {
         case SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P _).apply()
         case SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS _).apply()
         case _ => halt("Invalid Child")
       }
     }
    } else {
     while(T) {
       if(get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container.filter(v)) {
         return v
       }
       if (get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container.verbose) {
         println(s"Retrying for failing value: $v")
       }
       c = callEnum(gen.nextZBetween(0, callEnum.size-1))

       v = c match {
         case SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P _).apply()
         case SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS _).apply()
         case _ => halt("Invalid Child")
       }
     }
    }
    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P ===================

  def get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P: Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P
  def set_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P(config: Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P): RandomLib

  def nextSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P(): SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P = {
    var api_EthernetFramesRxIn: Option[SW.StructuredEthernetMessage_i] = nextOptionSWStructuredEthernetMessage_i()
    var api_EthernetFramesTxIn: Option[SW.StructuredEthernetMessage_i] = nextOptionSWStructuredEthernetMessage_i()

    var v: SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P = SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P(api_EthernetFramesRxIn, api_EthernetFramesTxIn)

    if(get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P.attempts >= 0) {
     for(i <- 0 to get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P.attempts) {
        if(get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P.filter(v)) {
          return v
        }
        if (get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P.verbose) {
          println(s"Retrying for failing value: $v")
        }
        api_EthernetFramesRxIn = nextOptionSWStructuredEthernetMessage_i()
        api_EthernetFramesTxIn = nextOptionSWStructuredEthernetMessage_i()
        v = SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P(api_EthernetFramesRxIn, api_EthernetFramesTxIn)
     }
    } else {
     while(T) {
       if(get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P.filter(v)) {
         return v
       }
       if (get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P.verbose) {
         println(s"Retrying for failing value: $v")
       }
       api_EthernetFramesRxIn = nextOptionSWStructuredEthernetMessage_i()
       api_EthernetFramesTxIn = nextOptionSWStructuredEthernetMessage_i()
       v = SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P(api_EthernetFramesRxIn, api_EthernetFramesTxIn)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS ===================

  def get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS: Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS
  def set_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS(config: Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS): RandomLib

  def nextSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS(): SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS = {
    var api_EthernetFramesRxIn: Option[SW.StructuredEthernetMessage_i] = nextOptionSWStructuredEthernetMessage_i()
    var api_EthernetFramesTxIn: Option[SW.StructuredEthernetMessage_i] = nextOptionSWStructuredEthernetMessage_i()

    var v: SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS = SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS(api_EthernetFramesRxIn, api_EthernetFramesTxIn)

    if(get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS.attempts >= 0) {
     for(i <- 0 to get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS.attempts) {
        if(get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS.filter(v)) {
          return v
        }
        if (get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS.verbose) {
          println(s"Retrying for failing value: $v")
        }
        api_EthernetFramesRxIn = nextOptionSWStructuredEthernetMessage_i()
        api_EthernetFramesTxIn = nextOptionSWStructuredEthernetMessage_i()
        v = SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS(api_EthernetFramesRxIn, api_EthernetFramesTxIn)
     }
    } else {
     while(T) {
       if(get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS.filter(v)) {
         return v
       }
       if (get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS.verbose) {
         println(s"Retrying for failing value: $v")
       }
       api_EthernetFramesRxIn = nextOptionSWStructuredEthernetMessage_i()
       api_EthernetFramesTxIn = nextOptionSWStructuredEthernetMessage_i()
       v = SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS(api_EthernetFramesRxIn, api_EthernetFramesTxIn)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container ===================

  def get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container: Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container
  def set_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container(config: Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container): RandomLib

  def nextSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container(): SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container = {
    var callEnum: ISZ[SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_DataTypeId.Type] = ISZ(SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P_Id, SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS_Id)

    if(get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container.additiveTypeFiltering) {
       callEnum = get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container.typeFilter
    } else {
       for(h <- get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container.typeFilter) {
         callEnum = ops.ISZOps(callEnum).filter(f => h =!= f)
       }
    }

    var c = callEnum(gen.nextZBetween(0, callEnum.size-1))

    var v: SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container = c match {
      case SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P _).apply()
      case SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS _).apply()
      case _ => halt("Invalid Child")
    }


    if(get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container.attempts >= 0) {
     for(i <- 0 to get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container.attempts) {
       if(get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container.filter(v)) {
        return v
       }
       if (get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container.verbose) {
         println(s"Retrying for failing value: $v")
       }
       c = callEnum(gen.nextZBetween(0, callEnum.size-1))

       v = c match {
         case SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P _).apply()
         case SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS _).apply()
         case _ => halt("Invalid Child")
       }
     }
    } else {
     while(T) {
       if(get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container.filter(v)) {
         return v
       }
       if (get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container.verbose) {
         println(s"Retrying for failing value: $v")
       }
       c = callEnum(gen.nextZBetween(0, callEnum.size-1))

       v = c match {
         case SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P _).apply()
         case SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS _).apply()
         case _ => halt("Invalid Child")
       }
     }
    }
    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P ===================

  def get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P: Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P
  def set_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P(config: Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P): RandomLib

  def nextSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P(): SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P = {
    var api_EthernetFramesRxOut: Option[SW.StructuredEthernetMessage_i] = nextOptionSWStructuredEthernetMessage_i()
    var api_EthernetFramesTxOut: Option[SW.StructuredEthernetMessage_i] = nextOptionSWStructuredEthernetMessage_i()

    var v: SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P = SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P(api_EthernetFramesRxOut, api_EthernetFramesTxOut)

    if(get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P.attempts >= 0) {
     for(i <- 0 to get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P.attempts) {
        if(get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P.filter(v)) {
          return v
        }
        if (get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P.verbose) {
          println(s"Retrying for failing value: $v")
        }
        api_EthernetFramesRxOut = nextOptionSWStructuredEthernetMessage_i()
        api_EthernetFramesTxOut = nextOptionSWStructuredEthernetMessage_i()
        v = SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P(api_EthernetFramesRxOut, api_EthernetFramesTxOut)
     }
    } else {
     while(T) {
       if(get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P.filter(v)) {
         return v
       }
       if (get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P.verbose) {
         println(s"Retrying for failing value: $v")
       }
       api_EthernetFramesRxOut = nextOptionSWStructuredEthernetMessage_i()
       api_EthernetFramesTxOut = nextOptionSWStructuredEthernetMessage_i()
       v = SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P(api_EthernetFramesRxOut, api_EthernetFramesTxOut)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS ===================

  def get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS: Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS
  def set_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS(config: Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS): RandomLib

  def nextSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS(): SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS = {
    var api_EthernetFramesRxOut: Option[SW.StructuredEthernetMessage_i] = nextOptionSWStructuredEthernetMessage_i()
    var api_EthernetFramesTxOut: Option[SW.StructuredEthernetMessage_i] = nextOptionSWStructuredEthernetMessage_i()

    var v: SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS = SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS(api_EthernetFramesRxOut, api_EthernetFramesTxOut)

    if(get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS.attempts >= 0) {
     for(i <- 0 to get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS.attempts) {
        if(get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS.filter(v)) {
          return v
        }
        if (get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS.verbose) {
          println(s"Retrying for failing value: $v")
        }
        api_EthernetFramesRxOut = nextOptionSWStructuredEthernetMessage_i()
        api_EthernetFramesTxOut = nextOptionSWStructuredEthernetMessage_i()
        v = SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS(api_EthernetFramesRxOut, api_EthernetFramesTxOut)
     }
    } else {
     while(T) {
       if(get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS.filter(v)) {
         return v
       }
       if (get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS.verbose) {
         println(s"Retrying for failing value: $v")
       }
       api_EthernetFramesRxOut = nextOptionSWStructuredEthernetMessage_i()
       api_EthernetFramesTxOut = nextOptionSWStructuredEthernetMessage_i()
       v = SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS(api_EthernetFramesRxOut, api_EthernetFramesTxOut)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= SW.FrameProtocol.Type ===================

  def get_Config_SWFrameProtocolType: Config_SWFrameProtocolType
  def set_Config_SWFrameProtocolType(config: Config_SWFrameProtocolType): RandomLib

  def nextSWFrameProtocolType(): SW.FrameProtocol.Type = {

    var ordinal: Z = gen.nextZBetween(0, base.SW.FrameProtocol.numOfElements-1)

    var v: SW.FrameProtocol.Type = base.SW.FrameProtocol.byOrdinal(ordinal).get
    if(get_Config_SWFrameProtocolType.attempts >= 0) {
     for(i <- 0 to get_Config_SWFrameProtocolType.attempts) {
       if(get_Config_SWFrameProtocolType.filter(v)) {
        return v
       }
       if (get_Config_SWFrameProtocolType.verbose) {
         println(s"Retrying for failing value: $v")
       }
       ordinal= gen.nextZBetween(0, base.SW.FrameProtocol.numOfElements-1)
       v = base.SW.FrameProtocol.byOrdinal(ordinal).get
     }
    } else {
     while(T){
       if(get_Config_SWFrameProtocolType.filter(v)) {
        return v
       }
       if (get_Config_SWFrameProtocolType.verbose) {
         println(s"Retrying for failing value: $v")
       }
       ordinal= gen.nextZBetween(0, base.SW.FrameProtocol.numOfElements-1)
       v = base.SW.FrameProtocol.byOrdinal(ordinal).get
     }
    }
    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= SW.FrameProtocol_Payload ===================

  def get_Config_SWFrameProtocol_Payload: Config_SWFrameProtocol_Payload
  def set_Config_SWFrameProtocol_Payload(config: Config_SWFrameProtocol_Payload): RandomLib

  def nextSWFrameProtocol_Payload(): SW.FrameProtocol_Payload = {
    var value: SW.FrameProtocol.Type = nextSWFrameProtocolType()

    var v: SW.FrameProtocol_Payload = SW.FrameProtocol_Payload(value)

    if(get_Config_SWFrameProtocol_Payload.attempts >= 0) {
     for(i <- 0 to get_Config_SWFrameProtocol_Payload.attempts) {
        if(get_Config_SWFrameProtocol_Payload.filter(v)) {
          return v
        }
        if (get_Config_SWFrameProtocol_Payload.verbose) {
          println(s"Retrying for failing value: $v")
        }
        value = nextSWFrameProtocolType()
        v = SW.FrameProtocol_Payload(value)
     }
    } else {
     while(T) {
       if(get_Config_SWFrameProtocol_Payload.filter(v)) {
         return v
       }
       if (get_Config_SWFrameProtocol_Payload.verbose) {
         println(s"Retrying for failing value: $v")
       }
       value = nextSWFrameProtocolType()
       v = SW.FrameProtocol_Payload(value)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= SW.InternetProtocol.Type ===================

  def get_Config_SWInternetProtocolType: Config_SWInternetProtocolType
  def set_Config_SWInternetProtocolType(config: Config_SWInternetProtocolType): RandomLib

  def nextSWInternetProtocolType(): SW.InternetProtocol.Type = {

    var ordinal: Z = gen.nextZBetween(0, base.SW.InternetProtocol.numOfElements-1)

    var v: SW.InternetProtocol.Type = base.SW.InternetProtocol.byOrdinal(ordinal).get
    if(get_Config_SWInternetProtocolType.attempts >= 0) {
     for(i <- 0 to get_Config_SWInternetProtocolType.attempts) {
       if(get_Config_SWInternetProtocolType.filter(v)) {
        return v
       }
       if (get_Config_SWInternetProtocolType.verbose) {
         println(s"Retrying for failing value: $v")
       }
       ordinal= gen.nextZBetween(0, base.SW.InternetProtocol.numOfElements-1)
       v = base.SW.InternetProtocol.byOrdinal(ordinal).get
     }
    } else {
     while(T){
       if(get_Config_SWInternetProtocolType.filter(v)) {
        return v
       }
       if (get_Config_SWInternetProtocolType.verbose) {
         println(s"Retrying for failing value: $v")
       }
       ordinal= gen.nextZBetween(0, base.SW.InternetProtocol.numOfElements-1)
       v = base.SW.InternetProtocol.byOrdinal(ordinal).get
     }
    }
    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= SW.InternetProtocol_Payload ===================

  def get_Config_SWInternetProtocol_Payload: Config_SWInternetProtocol_Payload
  def set_Config_SWInternetProtocol_Payload(config: Config_SWInternetProtocol_Payload): RandomLib

  def nextSWInternetProtocol_Payload(): SW.InternetProtocol_Payload = {
    var value: SW.InternetProtocol.Type = nextSWInternetProtocolType()

    var v: SW.InternetProtocol_Payload = SW.InternetProtocol_Payload(value)

    if(get_Config_SWInternetProtocol_Payload.attempts >= 0) {
     for(i <- 0 to get_Config_SWInternetProtocol_Payload.attempts) {
        if(get_Config_SWInternetProtocol_Payload.filter(v)) {
          return v
        }
        if (get_Config_SWInternetProtocol_Payload.verbose) {
          println(s"Retrying for failing value: $v")
        }
        value = nextSWInternetProtocolType()
        v = SW.InternetProtocol_Payload(value)
     }
    } else {
     while(T) {
       if(get_Config_SWInternetProtocol_Payload.filter(v)) {
         return v
       }
       if (get_Config_SWInternetProtocol_Payload.verbose) {
         println(s"Retrying for failing value: $v")
       }
       value = nextSWInternetProtocolType()
       v = SW.InternetProtocol_Payload(value)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container ===================

  def get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container: Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container
  def set_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container(config: Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container): RandomLib

  def nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container(): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container = {
    var callEnum: ISZ[SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_DataTypeId.Type] = ISZ(SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P_Id, SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS_Id)

    if(get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container.additiveTypeFiltering) {
       callEnum = get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container.typeFilter
    } else {
       for(h <- get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container.typeFilter) {
         callEnum = ops.ISZOps(callEnum).filter(f => h =!= f)
       }
    }

    var c = callEnum(gen.nextZBetween(0, callEnum.size-1))

    var v: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container = c match {
      case SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P _).apply()
      case SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS _).apply()
      case _ => halt("Invalid Child")
    }


    if(get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container.attempts >= 0) {
     for(i <- 0 to get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container.attempts) {
       if(get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container.filter(v)) {
        return v
       }
       if (get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container.verbose) {
         println(s"Retrying for failing value: $v")
       }
       c = callEnum(gen.nextZBetween(0, callEnum.size-1))

       v = c match {
         case SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P _).apply()
         case SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS _).apply()
         case _ => halt("Invalid Child")
       }
     }
    } else {
     while(T) {
       if(get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container.filter(v)) {
         return v
       }
       if (get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container.verbose) {
         println(s"Retrying for failing value: $v")
       }
       c = callEnum(gen.nextZBetween(0, callEnum.size-1))

       v = c match {
         case SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P _).apply()
         case SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS _).apply()
         case _ => halt("Invalid Child")
       }
     }
    }
    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P ===================

  def get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P: Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P
  def set_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(config: Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P): RandomLib

  def nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P = {
    var api_EthernetFramesTx: Option[SW.StructuredEthernetMessage_i] = nextOptionSWStructuredEthernetMessage_i()

    var v: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P = SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(api_EthernetFramesTx)

    if(get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P.attempts >= 0) {
     for(i <- 0 to get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P.attempts) {
        if(get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P.filter(v)) {
          return v
        }
        if (get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P.verbose) {
          println(s"Retrying for failing value: $v")
        }
        api_EthernetFramesTx = nextOptionSWStructuredEthernetMessage_i()
        v = SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(api_EthernetFramesTx)
     }
    } else {
     while(T) {
       if(get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P.filter(v)) {
         return v
       }
       if (get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P.verbose) {
         println(s"Retrying for failing value: $v")
       }
       api_EthernetFramesTx = nextOptionSWStructuredEthernetMessage_i()
       v = SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(api_EthernetFramesTx)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS ===================

  def get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS: Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS
  def set_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(config: Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS): RandomLib

  def nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS = {
    var api_EthernetFramesTx: Option[SW.StructuredEthernetMessage_i] = nextOptionSWStructuredEthernetMessage_i()

    var v: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS = SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(api_EthernetFramesTx)

    if(get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS.attempts >= 0) {
     for(i <- 0 to get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS.attempts) {
        if(get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS.filter(v)) {
          return v
        }
        if (get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS.verbose) {
          println(s"Retrying for failing value: $v")
        }
        api_EthernetFramesTx = nextOptionSWStructuredEthernetMessage_i()
        v = SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(api_EthernetFramesTx)
     }
    } else {
     while(T) {
       if(get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS.filter(v)) {
         return v
       }
       if (get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS.verbose) {
         println(s"Retrying for failing value: $v")
       }
       api_EthernetFramesTx = nextOptionSWStructuredEthernetMessage_i()
       v = SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(api_EthernetFramesTx)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container ===================

  def get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container: Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container
  def set_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container(config: Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container): RandomLib

  def nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container(): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container = {
    var callEnum: ISZ[SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_DataTypeId.Type] = ISZ(SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P_Id, SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS_Id)

    if(get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container.additiveTypeFiltering) {
       callEnum = get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container.typeFilter
    } else {
       for(h <- get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container.typeFilter) {
         callEnum = ops.ISZOps(callEnum).filter(f => h =!= f)
       }
    }

    var c = callEnum(gen.nextZBetween(0, callEnum.size-1))

    var v: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container = c match {
      case SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P _).apply()
      case SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS _).apply()
      case _ => halt("Invalid Child")
    }


    if(get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container.attempts >= 0) {
     for(i <- 0 to get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container.attempts) {
       if(get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container.filter(v)) {
        return v
       }
       if (get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container.verbose) {
         println(s"Retrying for failing value: $v")
       }
       c = callEnum(gen.nextZBetween(0, callEnum.size-1))

       v = c match {
         case SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P _).apply()
         case SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS _).apply()
         case _ => halt("Invalid Child")
       }
     }
    } else {
     while(T) {
       if(get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container.filter(v)) {
         return v
       }
       if (get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container.verbose) {
         println(s"Retrying for failing value: $v")
       }
       c = callEnum(gen.nextZBetween(0, callEnum.size-1))

       v = c match {
         case SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P _).apply()
         case SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS _).apply()
         case _ => halt("Invalid Child")
       }
     }
    }
    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P ===================

  def get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P: Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P
  def set_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(config: Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P): RandomLib

  def nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P = {
    var api_EthernetFramesRx: Option[SW.StructuredEthernetMessage_i] = nextOptionSWStructuredEthernetMessage_i()

    var v: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P = SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(api_EthernetFramesRx)

    if(get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P.attempts >= 0) {
     for(i <- 0 to get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P.attempts) {
        if(get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P.filter(v)) {
          return v
        }
        if (get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P.verbose) {
          println(s"Retrying for failing value: $v")
        }
        api_EthernetFramesRx = nextOptionSWStructuredEthernetMessage_i()
        v = SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(api_EthernetFramesRx)
     }
    } else {
     while(T) {
       if(get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P.filter(v)) {
         return v
       }
       if (get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P.verbose) {
         println(s"Retrying for failing value: $v")
       }
       api_EthernetFramesRx = nextOptionSWStructuredEthernetMessage_i()
       v = SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(api_EthernetFramesRx)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS ===================

  def get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS: Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS
  def set_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(config: Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS): RandomLib

  def nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS = {
    var api_EthernetFramesRx: Option[SW.StructuredEthernetMessage_i] = nextOptionSWStructuredEthernetMessage_i()

    var v: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS = SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(api_EthernetFramesRx)

    if(get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS.attempts >= 0) {
     for(i <- 0 to get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS.attempts) {
        if(get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS.filter(v)) {
          return v
        }
        if (get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS.verbose) {
          println(s"Retrying for failing value: $v")
        }
        api_EthernetFramesRx = nextOptionSWStructuredEthernetMessage_i()
        v = SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(api_EthernetFramesRx)
     }
    } else {
     while(T) {
       if(get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS.filter(v)) {
         return v
       }
       if (get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS.verbose) {
         println(s"Retrying for failing value: $v")
       }
       api_EthernetFramesRx = nextOptionSWStructuredEthernetMessage_i()
       v = SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(api_EthernetFramesRx)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  //=================== IS[SW.RawEthernetMessage.I, U8] =====================
  def get_Config_ISSWRawEthernetMessageIU8: Config_ISSWRawEthernetMessageIU8
  def set_Config_ISSWRawEthernetMessageIU8(config: Config_ISSWRawEthernetMessageIU8): RandomLib

  def nextISSWRawEthernetMessageIU8(): IS[SW.RawEthernetMessage.I, U8] = {

    var length: Z = gen.nextZBetween(0, get_numElement)
    var v: IS[SW.RawEthernetMessage.I, U8] = IS()
    for (r <- 0 until length) {
      v = v :+ nextU8()
    }

    if(get_Config_ISSWRawEthernetMessageIU8.attempts >= 0) {
     for(i <- 0 to get_Config_ISSWRawEthernetMessageIU8.attempts) {
        if(get_Config_ISSWRawEthernetMessageIU8.filter(v)) {
          return v
        }
        if (get_Config_ISSWRawEthernetMessageIU8.verbose) {
          println(s"Retrying for failing value: $v")
        }

        length = gen.nextZBetween(0, get_numElement)
        v = IS()
        for (r <- 0 until length) {
           v = v :+ nextU8()
        }
     }
    } else {
     while(T) {
       if(get_Config_ISSWRawEthernetMessageIU8.filter(v)) {
         return v
       }
       if (get_Config_ISSWRawEthernetMessageIU8.verbose) {
         println(s"Retrying for failing value: $v")
       }

       length = gen.nextZBetween(0, get_numElement)
       v = IS()
       for (r <- 0 until length) {
          v = v :+ nextU8()
       }
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= SW.RawEthernetMessage ===================

  def get_Config_SWRawEthernetMessage: Config_SWRawEthernetMessage
  def set_Config_SWRawEthernetMessage(config: Config_SWRawEthernetMessage): RandomLib

  def nextSWRawEthernetMessage(): SW.RawEthernetMessage = {
    var value: IS[SW.RawEthernetMessage.I, U8] = nextISSWRawEthernetMessageIU8()

    var v: SW.RawEthernetMessage = SW.RawEthernetMessage(value)

    if(get_Config_SWRawEthernetMessage.attempts >= 0) {
     for(i <- 0 to get_Config_SWRawEthernetMessage.attempts) {
        if(get_Config_SWRawEthernetMessage.filter(v)) {
          return v
        }
        if (get_Config_SWRawEthernetMessage.verbose) {
          println(s"Retrying for failing value: $v")
        }
        value = nextISSWRawEthernetMessageIU8()
        v = SW.RawEthernetMessage(value)
     }
    } else {
     while(T) {
       if(get_Config_SWRawEthernetMessage.filter(v)) {
         return v
       }
       if (get_Config_SWRawEthernetMessage.verbose) {
         println(s"Retrying for failing value: $v")
       }
       value = nextISSWRawEthernetMessageIU8()
       v = SW.RawEthernetMessage(value)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= SW.RawEthernetMessage_Payload ===================

  def get_Config_SWRawEthernetMessage_Payload: Config_SWRawEthernetMessage_Payload
  def set_Config_SWRawEthernetMessage_Payload(config: Config_SWRawEthernetMessage_Payload): RandomLib

  def nextSWRawEthernetMessage_Payload(): SW.RawEthernetMessage_Payload = {
    var value: SW.RawEthernetMessage = nextSWRawEthernetMessage()

    var v: SW.RawEthernetMessage_Payload = SW.RawEthernetMessage_Payload(value)

    if(get_Config_SWRawEthernetMessage_Payload.attempts >= 0) {
     for(i <- 0 to get_Config_SWRawEthernetMessage_Payload.attempts) {
        if(get_Config_SWRawEthernetMessage_Payload.filter(v)) {
          return v
        }
        if (get_Config_SWRawEthernetMessage_Payload.verbose) {
          println(s"Retrying for failing value: $v")
        }
        value = nextSWRawEthernetMessage()
        v = SW.RawEthernetMessage_Payload(value)
     }
    } else {
     while(T) {
       if(get_Config_SWRawEthernetMessage_Payload.filter(v)) {
         return v
       }
       if (get_Config_SWRawEthernetMessage_Payload.verbose) {
         println(s"Retrying for failing value: $v")
       }
       value = nextSWRawEthernetMessage()
       v = SW.RawEthernetMessage_Payload(value)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= SW.StructuredEthernetMessage_i ===================

  def get_Config_SWStructuredEthernetMessage_i: Config_SWStructuredEthernetMessage_i
  def set_Config_SWStructuredEthernetMessage_i(config: Config_SWStructuredEthernetMessage_i): RandomLib

  def nextSWStructuredEthernetMessage_i(): SW.StructuredEthernetMessage_i = {
    var malformedFrame: B = nextB()
    var internetProtocol: SW.InternetProtocol.Type = nextSWInternetProtocolType()
    var frameProtocol: SW.FrameProtocol.Type = nextSWFrameProtocolType()
    var portIsWhitelisted: B = nextB()
    var arpType: SW.ARP_Type.Type = nextSWARP_TypeType()
    var rawMessage: SW.RawEthernetMessage = nextSWRawEthernetMessage()

    var v: SW.StructuredEthernetMessage_i = SW.StructuredEthernetMessage_i(malformedFrame, internetProtocol, frameProtocol, portIsWhitelisted, arpType, rawMessage)

    if(get_Config_SWStructuredEthernetMessage_i.attempts >= 0) {
     for(i <- 0 to get_Config_SWStructuredEthernetMessage_i.attempts) {
        if(get_Config_SWStructuredEthernetMessage_i.filter(v)) {
          return v
        }
        if (get_Config_SWStructuredEthernetMessage_i.verbose) {
          println(s"Retrying for failing value: $v")
        }
        malformedFrame = nextB()
        internetProtocol = nextSWInternetProtocolType()
        frameProtocol = nextSWFrameProtocolType()
        portIsWhitelisted = nextB()
        arpType = nextSWARP_TypeType()
        rawMessage = nextSWRawEthernetMessage()
        v = SW.StructuredEthernetMessage_i(malformedFrame, internetProtocol, frameProtocol, portIsWhitelisted, arpType, rawMessage)
     }
    } else {
     while(T) {
       if(get_Config_SWStructuredEthernetMessage_i.filter(v)) {
         return v
       }
       if (get_Config_SWStructuredEthernetMessage_i.verbose) {
         println(s"Retrying for failing value: $v")
       }
       malformedFrame = nextB()
       internetProtocol = nextSWInternetProtocolType()
       frameProtocol = nextSWFrameProtocolType()
       portIsWhitelisted = nextB()
       arpType = nextSWARP_TypeType()
       rawMessage = nextSWRawEthernetMessage()
       v = SW.StructuredEthernetMessage_i(malformedFrame, internetProtocol, frameProtocol, portIsWhitelisted, arpType, rawMessage)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= SW.StructuredEthernetMessage_i_Payload ===================

  def get_Config_SWStructuredEthernetMessage_i_Payload: Config_SWStructuredEthernetMessage_i_Payload
  def set_Config_SWStructuredEthernetMessage_i_Payload(config: Config_SWStructuredEthernetMessage_i_Payload): RandomLib

  def nextSWStructuredEthernetMessage_i_Payload(): SW.StructuredEthernetMessage_i_Payload = {
    var value: SW.StructuredEthernetMessage_i = nextSWStructuredEthernetMessage_i()

    var v: SW.StructuredEthernetMessage_i_Payload = SW.StructuredEthernetMessage_i_Payload(value)

    if(get_Config_SWStructuredEthernetMessage_i_Payload.attempts >= 0) {
     for(i <- 0 to get_Config_SWStructuredEthernetMessage_i_Payload.attempts) {
        if(get_Config_SWStructuredEthernetMessage_i_Payload.filter(v)) {
          return v
        }
        if (get_Config_SWStructuredEthernetMessage_i_Payload.verbose) {
          println(s"Retrying for failing value: $v")
        }
        value = nextSWStructuredEthernetMessage_i()
        v = SW.StructuredEthernetMessage_i_Payload(value)
     }
    } else {
     while(T) {
       if(get_Config_SWStructuredEthernetMessage_i_Payload.filter(v)) {
         return v
       }
       if (get_Config_SWStructuredEthernetMessage_i_Payload.verbose) {
         println(s"Retrying for failing value: $v")
       }
       value = nextSWStructuredEthernetMessage_i()
       v = SW.StructuredEthernetMessage_i_Payload(value)
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= util.Container ===================

  def get_Config_utilContainer: Config_utilContainer
  def set_Config_utilContainer(config: Config_utilContainer): RandomLib

  def nextutilContainer(): util.Container = {
    var callEnum: ISZ[utilContainer_DataTypeId.Type] = ISZ(utilContainer_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P_Id, utilContainer_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS_Id, utilContainer_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P_Id, utilContainer_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS_Id, utilContainer_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P_Id, utilContainer_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS_Id, utilContainer_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P_Id, utilContainer_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS_Id, utilContainer_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P_Id, utilContainer_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS_Id, utilContainer_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P_Id, utilContainer_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS_Id, utilContainer_DataTypeId.utilEmptyContainer_Id)

    if(get_Config_utilContainer.additiveTypeFiltering) {
       callEnum = get_Config_utilContainer.typeFilter
    } else {
       for(h <- get_Config_utilContainer.typeFilter) {
         callEnum = ops.ISZOps(callEnum).filter(f => h =!= f)
       }
    }

    var c = callEnum(gen.nextZBetween(0, callEnum.size-1))

    var v: util.Container = c match {
      case utilContainer_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P _).apply()
      case utilContainer_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS _).apply()
      case utilContainer_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P _).apply()
      case utilContainer_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS _).apply()
      case utilContainer_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P _).apply()
      case utilContainer_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS _).apply()
      case utilContainer_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P _).apply()
      case utilContainer_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS _).apply()
      case utilContainer_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P _).apply()
      case utilContainer_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS _).apply()
      case utilContainer_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P _).apply()
      case utilContainer_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS _).apply()
      case utilContainer_DataTypeId.utilEmptyContainer_Id => (nextutilEmptyContainer _).apply()
      case _ => halt("Invalid Child")
    }


    if(get_Config_utilContainer.attempts >= 0) {
     for(i <- 0 to get_Config_utilContainer.attempts) {
       if(get_Config_utilContainer.filter(v)) {
        return v
       }
       if (get_Config_utilContainer.verbose) {
         println(s"Retrying for failing value: $v")
       }
       c = callEnum(gen.nextZBetween(0, callEnum.size-1))

       v = c match {
         case utilContainer_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P _).apply()
         case utilContainer_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS _).apply()
         case utilContainer_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P _).apply()
         case utilContainer_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS _).apply()
         case utilContainer_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P _).apply()
         case utilContainer_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS _).apply()
         case utilContainer_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P _).apply()
         case utilContainer_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS _).apply()
         case utilContainer_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P _).apply()
         case utilContainer_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS _).apply()
         case utilContainer_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P _).apply()
         case utilContainer_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS _).apply()
         case utilContainer_DataTypeId.utilEmptyContainer_Id => (nextutilEmptyContainer _).apply()
         case _ => halt("Invalid Child")
       }
     }
    } else {
     while(T) {
       if(get_Config_utilContainer.filter(v)) {
         return v
       }
       if (get_Config_utilContainer.verbose) {
         println(s"Retrying for failing value: $v")
       }
       c = callEnum(gen.nextZBetween(0, callEnum.size-1))

       v = c match {
         case utilContainer_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P _).apply()
         case utilContainer_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS _).apply()
         case utilContainer_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P _).apply()
         case utilContainer_DataTypeId.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS_Id => (nextSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS _).apply()
         case utilContainer_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P _).apply()
         case utilContainer_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS _).apply()
         case utilContainer_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P _).apply()
         case utilContainer_DataTypeId.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS_Id => (nextSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS _).apply()
         case utilContainer_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P _).apply()
         case utilContainer_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS _).apply()
         case utilContainer_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P _).apply()
         case utilContainer_DataTypeId.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS_Id => (nextSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS _).apply()
         case utilContainer_DataTypeId.utilEmptyContainer_Id => (nextutilEmptyContainer _).apply()
         case _ => halt("Invalid Child")
       }
     }
    }
    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= util.EmptyContainer ===================

  def get_Config_utilEmptyContainer: Config_utilEmptyContainer
  def set_Config_utilEmptyContainer(config: Config_utilEmptyContainer): RandomLib

  def nextutilEmptyContainer(): util.EmptyContainer = {

    var v: util.EmptyContainer = util.EmptyContainer()

    if(get_Config_utilEmptyContainer.attempts >= 0) {
     for(i <- 0 to get_Config_utilEmptyContainer.attempts) {
        if(get_Config_utilEmptyContainer.filter(v)) {
          return v
        }
        if (get_Config_utilEmptyContainer.verbose) {
          println(s"Retrying for failing value: $v")
        }
        v = util.EmptyContainer()
     }
    } else {
     while(T) {
       if(get_Config_utilEmptyContainer.filter(v)) {
         return v
       }
       if (get_Config_utilEmptyContainer.verbose) {
         println(s"Retrying for failing value: $v")
       }
       v = util.EmptyContainer()
     }
    }

    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

  // ============= runtimemonitor.ObservationKind.Type ===================

  def get_Config_runtimemonitorObservationKindType: Config_runtimemonitorObservationKindType
  def set_Config_runtimemonitorObservationKindType(config: Config_runtimemonitorObservationKindType): RandomLib

  def nextruntimemonitorObservationKindType(): runtimemonitor.ObservationKind.Type = {

    var ordinal: Z = gen.nextZBetween(0, base.runtimemonitor.ObservationKind.numOfElements-1)

    var v: runtimemonitor.ObservationKind.Type = base.runtimemonitor.ObservationKind.byOrdinal(ordinal).get
    if(get_Config_runtimemonitorObservationKindType.attempts >= 0) {
     for(i <- 0 to get_Config_runtimemonitorObservationKindType.attempts) {
       if(get_Config_runtimemonitorObservationKindType.filter(v)) {
        return v
       }
       if (get_Config_runtimemonitorObservationKindType.verbose) {
         println(s"Retrying for failing value: $v")
       }
       ordinal= gen.nextZBetween(0, base.runtimemonitor.ObservationKind.numOfElements-1)
       v = base.runtimemonitor.ObservationKind.byOrdinal(ordinal).get
     }
    } else {
     while(T){
       if(get_Config_runtimemonitorObservationKindType.filter(v)) {
        return v
       }
       if (get_Config_runtimemonitorObservationKindType.verbose) {
         println(s"Retrying for failing value: $v")
       }
       ordinal= gen.nextZBetween(0, base.runtimemonitor.ObservationKind.numOfElements-1)
       v = base.runtimemonitor.ObservationKind.byOrdinal(ordinal).get
     }
    }
    assert(F, "Requirements too strict to generate")
    halt("Requirements too strict to generate")
  }

}

@record class RandomLib(val gen: org.sireum.Random.Gen) extends RandomLibI {

  var numElem: Z = 50

  var _verbose: B = F
  def verbose: RandomLib = {
    _verbose = !_verbose
    return this
  }

  def get_numElement: Z = {return numElem}

  def set_numElement(s: Z): Unit ={
    numElem = s
  }

  // ============= String =============

  def alwaysTrue_String(v: String): B = {return T}

  var config_String: Config_String = Config_String(0, numElem, 100, _verbose, alwaysTrue_String _)

  def get_Config_String: Config_String = {return config_String}

  def set_Config_String(config: Config_String): RandomLib = {
    config_String = config
    return this
  }

  // ============= Z ===================
  def alwaysTrue_Z(v: Z): B = {return T}

  var config_Z: Config_Z = Config_Z(None(), None(), 100, _verbose, alwaysTrue_Z _)
  def get_Config_Z: Config_Z = {return config_Z}

  def set_Config_Z(config: Config_Z): RandomLib ={
    config_Z = config
    return this
  }

  // ============= B ===================
  def alwaysTrue_B(v: B): B = {return T}

  var config_B: Config_B = Config_B(100, _verbose, alwaysTrue_B _)
  def get_Config_B: Config_B = {return config_B}

  def set_Config_B(config: Config_B): RandomLib ={
    config_B = config
    return this
  }

  // ============= C ===================
  def alwaysTrue_C(v: C): B = {return T}

  var config_C: Config_C = Config_C(None(), None(), 100, _verbose, alwaysTrue_C _)
  def get_Config_C: Config_C = {return config_C}

  def set_Config_C(config: Config_C): RandomLib ={
    config_C = config
    return this
  }

  // ============= R ===================
  def alwaysTrue_R(v: R): B = {return T}

  var config_R: Config_R = Config_R(None(), None(), 100, _verbose, alwaysTrue_R _)
  def get_Config_R: Config_R = {return config_R}

  def set_Config_R(config: Config_R): RandomLib ={
    config_R = config
    return this
  }

  // ============= F32 ===================
  def alwaysTrue_F32(v: F32): B = {return T}

  var config_F32: Config_F32 = Config_F32(None(), None(), 100, _verbose, alwaysTrue_F32 _)
  def get_Config_F32: Config_F32 = {return config_F32}

  def set_Config_F32(config: Config_F32): RandomLib ={
    config_F32 = config
    return this
  }

  // ============= F64 ===================
  def alwaysTrue_F64(v: F64): B = {return T}

  var config_F64: Config_F64 = Config_F64(None(), None(), 100, _verbose, alwaysTrue_F64 _)
  def get_Config_F64: Config_F64 = {return config_F64}

  def set_Config_F64(config: Config_F64): RandomLib ={
    config_F64 = config
    return this
  }

  // ============= S8 ===================
  def alwaysTrue_S8(v: S8): B = {return T}

  var config_S8: Config_S8 = Config_S8(None(), None(), 100, _verbose, alwaysTrue_S8 _)
  def get_Config_S8: Config_S8 = {return config_S8}

  def set_Config_S8(config: Config_S8): RandomLib ={
    config_S8 = config
    return this
  }

  // ============= S16 ===================
  def alwaysTrue_S16(v: S16): B = {return T}

  var config_S16: Config_S16 = Config_S16(None(), None(), 100, _verbose, alwaysTrue_S16 _)
  def get_Config_S16: Config_S16 = {return config_S16}

  def set_Config_S16(config: Config_S16): RandomLib ={
    config_S16 = config
    return this
  }

  // ============= S32 ===================
  def alwaysTrue_S32(v: S32): B = {return T}

  var config_S32: Config_S32 = Config_S32(None(), None(), 100, _verbose, alwaysTrue_S32 _)
  def get_Config_S32: Config_S32 = {return config_S32}

  def set_Config_S32(config: Config_S32): RandomLib ={
    config_S32 = config
    return this
  }

  // ============= S64 ===================
  def alwaysTrue_S64(v: S64): B = {return T}

  var config_S64: Config_S64 = Config_S64(None(), None(), 100, _verbose, alwaysTrue_S64 _)
  def get_Config_S64: Config_S64 = {return config_S64}

  def set_Config_S64(config: Config_S64): RandomLib ={
    config_S64 = config
    return this
  }

  // ============= U8 ===================
  def alwaysTrue_U8(v: U8): B = {return T}

  var config_U8: Config_U8 = Config_U8(None(), None(), 100, _verbose, alwaysTrue_U8 _)
  def get_Config_U8: Config_U8 = {return config_U8}

  def set_Config_U8(config: Config_U8): RandomLib ={
    config_U8 = config
    return this
  }

  // ============= U16 ===================
  def alwaysTrue_U16(v: U16): B = {return T}

  var config_U16: Config_U16 = Config_U16(None(), None(), 100, _verbose, alwaysTrue_U16 _)
  def get_Config_U16: Config_U16 = {return config_U16}

  def set_Config_U16(config: Config_U16): RandomLib ={
    config_U16 = config
    return this
  }

  // ============= U32 ===================
  def alwaysTrue_U32(v: U32): B = {return T}

  var config_U32: Config_U32 = Config_U32(None(), None(), 100, _verbose, alwaysTrue_U32 _)
  def get_Config_U32: Config_U32 = {return config_U32}

  def set_Config_U32(config: Config_U32): RandomLib ={
    config_U32 = config
    return this
  }

  // ============= U64 ===================
  def alwaysTrue_U64(v: U64): B = {return T}

  var config_U64: Config_U64 = Config_U64(None(), None(), 100, _verbose, alwaysTrue_U64 _)
  def get_Config_U64: Config_U64 = {return config_U64}

  def set_Config_U64(config: Config_U64): RandomLib ={
    config_U64 = config
    return this
  }

  // ============= art.DataContent ===================
  def alwaysTrue__artDataContent(v: art.DataContent): B = {return T}

  var config__artDataContent: Config__artDataContent = Config__artDataContent(100, _verbose, F, ISZ(), alwaysTrue__artDataContent _)

  def get_Config__artDataContent: Config__artDataContent = {return config__artDataContent}

  def set_Config__artDataContent(config: Config__artDataContent): RandomLib ={
    config__artDataContent = config
    return this
  }

  // ============= art.Empty ===================
  def alwaysTrue__artEmpty(v: art.Empty): B = {return T}

  var config__artEmpty: Config__artEmpty = Config__artEmpty(100, _verbose, alwaysTrue__artEmpty _)

  def get_Config__artEmpty: Config__artEmpty = {return config__artEmpty}

  def set_Config__artEmpty(config: Config__artEmpty): RandomLib ={
    config__artEmpty = config
    return this
  }

  // ============= Base_Types.Boolean_Payload ===================
  def alwaysTrue_Base_TypesBoolean_Payload(v: Base_Types.Boolean_Payload): B = {return T}

  var config_Base_TypesBoolean_Payload: Config_Base_TypesBoolean_Payload = Config_Base_TypesBoolean_Payload(100, _verbose, alwaysTrue_Base_TypesBoolean_Payload _)

  def get_Config_Base_TypesBoolean_Payload: Config_Base_TypesBoolean_Payload = {return config_Base_TypesBoolean_Payload}

  def set_Config_Base_TypesBoolean_Payload(config: Config_Base_TypesBoolean_Payload): RandomLib ={
    config_Base_TypesBoolean_Payload = config
    return this
  }

  // ============= Base_Types.Integer_Payload ===================
  def alwaysTrue_Base_TypesInteger_Payload(v: Base_Types.Integer_Payload): B = {return T}

  var config_Base_TypesInteger_Payload: Config_Base_TypesInteger_Payload = Config_Base_TypesInteger_Payload(100, _verbose, alwaysTrue_Base_TypesInteger_Payload _)

  def get_Config_Base_TypesInteger_Payload: Config_Base_TypesInteger_Payload = {return config_Base_TypesInteger_Payload}

  def set_Config_Base_TypesInteger_Payload(config: Config_Base_TypesInteger_Payload): RandomLib ={
    config_Base_TypesInteger_Payload = config
    return this
  }

  // ============= Base_Types.Integer_8_Payload ===================
  def alwaysTrue_Base_TypesInteger_8_Payload(v: Base_Types.Integer_8_Payload): B = {return T}

  var config_Base_TypesInteger_8_Payload: Config_Base_TypesInteger_8_Payload = Config_Base_TypesInteger_8_Payload(100, _verbose, alwaysTrue_Base_TypesInteger_8_Payload _)

  def get_Config_Base_TypesInteger_8_Payload: Config_Base_TypesInteger_8_Payload = {return config_Base_TypesInteger_8_Payload}

  def set_Config_Base_TypesInteger_8_Payload(config: Config_Base_TypesInteger_8_Payload): RandomLib ={
    config_Base_TypesInteger_8_Payload = config
    return this
  }

  // ============= Base_Types.Integer_16_Payload ===================
  def alwaysTrue_Base_TypesInteger_16_Payload(v: Base_Types.Integer_16_Payload): B = {return T}

  var config_Base_TypesInteger_16_Payload: Config_Base_TypesInteger_16_Payload = Config_Base_TypesInteger_16_Payload(100, _verbose, alwaysTrue_Base_TypesInteger_16_Payload _)

  def get_Config_Base_TypesInteger_16_Payload: Config_Base_TypesInteger_16_Payload = {return config_Base_TypesInteger_16_Payload}

  def set_Config_Base_TypesInteger_16_Payload(config: Config_Base_TypesInteger_16_Payload): RandomLib ={
    config_Base_TypesInteger_16_Payload = config
    return this
  }

  // ============= Base_Types.Integer_32_Payload ===================
  def alwaysTrue_Base_TypesInteger_32_Payload(v: Base_Types.Integer_32_Payload): B = {return T}

  var config_Base_TypesInteger_32_Payload: Config_Base_TypesInteger_32_Payload = Config_Base_TypesInteger_32_Payload(100, _verbose, alwaysTrue_Base_TypesInteger_32_Payload _)

  def get_Config_Base_TypesInteger_32_Payload: Config_Base_TypesInteger_32_Payload = {return config_Base_TypesInteger_32_Payload}

  def set_Config_Base_TypesInteger_32_Payload(config: Config_Base_TypesInteger_32_Payload): RandomLib ={
    config_Base_TypesInteger_32_Payload = config
    return this
  }

  // ============= Base_Types.Integer_64_Payload ===================
  def alwaysTrue_Base_TypesInteger_64_Payload(v: Base_Types.Integer_64_Payload): B = {return T}

  var config_Base_TypesInteger_64_Payload: Config_Base_TypesInteger_64_Payload = Config_Base_TypesInteger_64_Payload(100, _verbose, alwaysTrue_Base_TypesInteger_64_Payload _)

  def get_Config_Base_TypesInteger_64_Payload: Config_Base_TypesInteger_64_Payload = {return config_Base_TypesInteger_64_Payload}

  def set_Config_Base_TypesInteger_64_Payload(config: Config_Base_TypesInteger_64_Payload): RandomLib ={
    config_Base_TypesInteger_64_Payload = config
    return this
  }

  // ============= Base_Types.Unsigned_8_Payload ===================
  def alwaysTrue_Base_TypesUnsigned_8_Payload(v: Base_Types.Unsigned_8_Payload): B = {return T}

  var config_Base_TypesUnsigned_8_Payload: Config_Base_TypesUnsigned_8_Payload = Config_Base_TypesUnsigned_8_Payload(100, _verbose, alwaysTrue_Base_TypesUnsigned_8_Payload _)

  def get_Config_Base_TypesUnsigned_8_Payload: Config_Base_TypesUnsigned_8_Payload = {return config_Base_TypesUnsigned_8_Payload}

  def set_Config_Base_TypesUnsigned_8_Payload(config: Config_Base_TypesUnsigned_8_Payload): RandomLib ={
    config_Base_TypesUnsigned_8_Payload = config
    return this
  }

  // ============= Base_Types.Unsigned_16_Payload ===================
  def alwaysTrue_Base_TypesUnsigned_16_Payload(v: Base_Types.Unsigned_16_Payload): B = {return T}

  var config_Base_TypesUnsigned_16_Payload: Config_Base_TypesUnsigned_16_Payload = Config_Base_TypesUnsigned_16_Payload(100, _verbose, alwaysTrue_Base_TypesUnsigned_16_Payload _)

  def get_Config_Base_TypesUnsigned_16_Payload: Config_Base_TypesUnsigned_16_Payload = {return config_Base_TypesUnsigned_16_Payload}

  def set_Config_Base_TypesUnsigned_16_Payload(config: Config_Base_TypesUnsigned_16_Payload): RandomLib ={
    config_Base_TypesUnsigned_16_Payload = config
    return this
  }

  // ============= Base_Types.Unsigned_32_Payload ===================
  def alwaysTrue_Base_TypesUnsigned_32_Payload(v: Base_Types.Unsigned_32_Payload): B = {return T}

  var config_Base_TypesUnsigned_32_Payload: Config_Base_TypesUnsigned_32_Payload = Config_Base_TypesUnsigned_32_Payload(100, _verbose, alwaysTrue_Base_TypesUnsigned_32_Payload _)

  def get_Config_Base_TypesUnsigned_32_Payload: Config_Base_TypesUnsigned_32_Payload = {return config_Base_TypesUnsigned_32_Payload}

  def set_Config_Base_TypesUnsigned_32_Payload(config: Config_Base_TypesUnsigned_32_Payload): RandomLib ={
    config_Base_TypesUnsigned_32_Payload = config
    return this
  }

  // ============= Base_Types.Unsigned_64_Payload ===================
  def alwaysTrue_Base_TypesUnsigned_64_Payload(v: Base_Types.Unsigned_64_Payload): B = {return T}

  var config_Base_TypesUnsigned_64_Payload: Config_Base_TypesUnsigned_64_Payload = Config_Base_TypesUnsigned_64_Payload(100, _verbose, alwaysTrue_Base_TypesUnsigned_64_Payload _)

  def get_Config_Base_TypesUnsigned_64_Payload: Config_Base_TypesUnsigned_64_Payload = {return config_Base_TypesUnsigned_64_Payload}

  def set_Config_Base_TypesUnsigned_64_Payload(config: Config_Base_TypesUnsigned_64_Payload): RandomLib ={
    config_Base_TypesUnsigned_64_Payload = config
    return this
  }

  // ============= Base_Types.Float_Payload ===================
  def alwaysTrue_Base_TypesFloat_Payload(v: Base_Types.Float_Payload): B = {return T}

  var config_Base_TypesFloat_Payload: Config_Base_TypesFloat_Payload = Config_Base_TypesFloat_Payload(100, _verbose, alwaysTrue_Base_TypesFloat_Payload _)

  def get_Config_Base_TypesFloat_Payload: Config_Base_TypesFloat_Payload = {return config_Base_TypesFloat_Payload}

  def set_Config_Base_TypesFloat_Payload(config: Config_Base_TypesFloat_Payload): RandomLib ={
    config_Base_TypesFloat_Payload = config
    return this
  }

  // ============= Base_Types.Float_32_Payload ===================
  def alwaysTrue_Base_TypesFloat_32_Payload(v: Base_Types.Float_32_Payload): B = {return T}

  var config_Base_TypesFloat_32_Payload: Config_Base_TypesFloat_32_Payload = Config_Base_TypesFloat_32_Payload(100, _verbose, alwaysTrue_Base_TypesFloat_32_Payload _)

  def get_Config_Base_TypesFloat_32_Payload: Config_Base_TypesFloat_32_Payload = {return config_Base_TypesFloat_32_Payload}

  def set_Config_Base_TypesFloat_32_Payload(config: Config_Base_TypesFloat_32_Payload): RandomLib ={
    config_Base_TypesFloat_32_Payload = config
    return this
  }

  // ============= Base_Types.Float_64_Payload ===================
  def alwaysTrue_Base_TypesFloat_64_Payload(v: Base_Types.Float_64_Payload): B = {return T}

  var config_Base_TypesFloat_64_Payload: Config_Base_TypesFloat_64_Payload = Config_Base_TypesFloat_64_Payload(100, _verbose, alwaysTrue_Base_TypesFloat_64_Payload _)

  def get_Config_Base_TypesFloat_64_Payload: Config_Base_TypesFloat_64_Payload = {return config_Base_TypesFloat_64_Payload}

  def set_Config_Base_TypesFloat_64_Payload(config: Config_Base_TypesFloat_64_Payload): RandomLib ={
    config_Base_TypesFloat_64_Payload = config
    return this
  }

  // ============= Base_Types.Character_Payload ===================
  def alwaysTrue_Base_TypesCharacter_Payload(v: Base_Types.Character_Payload): B = {return T}

  var config_Base_TypesCharacter_Payload: Config_Base_TypesCharacter_Payload = Config_Base_TypesCharacter_Payload(100, _verbose, alwaysTrue_Base_TypesCharacter_Payload _)

  def get_Config_Base_TypesCharacter_Payload: Config_Base_TypesCharacter_Payload = {return config_Base_TypesCharacter_Payload}

  def set_Config_Base_TypesCharacter_Payload(config: Config_Base_TypesCharacter_Payload): RandomLib ={
    config_Base_TypesCharacter_Payload = config
    return this
  }

  // ============= Base_Types.String_Payload ===================
  def alwaysTrue_Base_TypesString_Payload(v: Base_Types.String_Payload): B = {return T}

  var config_Base_TypesString_Payload: Config_Base_TypesString_Payload = Config_Base_TypesString_Payload(100, _verbose, alwaysTrue_Base_TypesString_Payload _)

  def get_Config_Base_TypesString_Payload: Config_Base_TypesString_Payload = {return config_Base_TypesString_Payload}

  def set_Config_Base_TypesString_Payload(config: Config_Base_TypesString_Payload): RandomLib ={
    config_Base_TypesString_Payload = config
    return this
  }

  // ============= ISZ[B] ===================
  def alwaysTrue_ISZB(v: ISZ[B]): B = {return T}

  var config_ISZB: Config_ISZB = Config_ISZB(0, 20, 100, _verbose, alwaysTrue_ISZB _)
  def get_Config_ISZB: Config_ISZB = {return config_ISZB}

  def set_Config_ISZB(config: Config_ISZB): RandomLib ={
    config_ISZB = config
    return this
  }

  // ============= Base_Types.Bits_Payload ===================
  def alwaysTrue_Base_TypesBits_Payload(v: Base_Types.Bits_Payload): B = {return T}

  var config_Base_TypesBits_Payload: Config_Base_TypesBits_Payload = Config_Base_TypesBits_Payload(100, _verbose, alwaysTrue_Base_TypesBits_Payload _)

  def get_Config_Base_TypesBits_Payload: Config_Base_TypesBits_Payload = {return config_Base_TypesBits_Payload}

  def set_Config_Base_TypesBits_Payload(config: Config_Base_TypesBits_Payload): RandomLib ={
    config_Base_TypesBits_Payload = config
    return this
  }

  // ============= SW.ARP_Type.Type ===================
  def alwaysTrue_SWARP_TypeType(v: SW.ARP_Type.Type): B = {return T}

  var config_SWARP_TypeType: Config_SWARP_TypeType = Config_SWARP_TypeType(100, _verbose, alwaysTrue_SWARP_TypeType _)

  def get_Config_SWARP_TypeType: Config_SWARP_TypeType = {return config_SWARP_TypeType}

  def set_Config_SWARP_TypeType(config: Config_SWARP_TypeType): RandomLib ={
    config_SWARP_TypeType = config
    return this
  }

  // ============= SW.ARP_Type_Payload ===================
  def alwaysTrue_SWARP_Type_Payload(v: SW.ARP_Type_Payload): B = {return T}

  var config_SWARP_Type_Payload: Config_SWARP_Type_Payload = Config_SWARP_Type_Payload(100, _verbose, alwaysTrue_SWARP_Type_Payload _)

  def get_Config_SWARP_Type_Payload: Config_SWARP_Type_Payload = {return config_SWARP_Type_Payload}

  def set_Config_SWARP_Type_Payload(config: Config_SWARP_Type_Payload): RandomLib ={
    config_SWARP_Type_Payload = config
    return this
  }

  // ============= SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container ===================
  def alwaysTrue_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container(v: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container): B = {return T}

  var config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container: Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container = Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container(100, _verbose, F, ISZ(), alwaysTrue_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container _)

  def get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container: Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container = {return config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container}

  def set_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container(config: Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container): RandomLib ={
    config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container = config
    return this
  }

  // ============= Option[SW.StructuredEthernetMessage_i] ===================
  def alwaysTrue_OptionSWStructuredEthernetMessage_i(v: Option[SW.StructuredEthernetMessage_i]): B = {return T}

  var config_OptionSWStructuredEthernetMessage_i: Config_OptionSWStructuredEthernetMessage_i = Config_OptionSWStructuredEthernetMessage_i(0, 20, 100, _verbose, alwaysTrue_OptionSWStructuredEthernetMessage_i _)
  def get_Config_OptionSWStructuredEthernetMessage_i: Config_OptionSWStructuredEthernetMessage_i = {return config_OptionSWStructuredEthernetMessage_i}

  def set_Config_OptionSWStructuredEthernetMessage_i(config: Config_OptionSWStructuredEthernetMessage_i): RandomLib ={
    config_OptionSWStructuredEthernetMessage_i = config
    return this
  }

  // ============= SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P ===================
  def alwaysTrue_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P(v: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P): B = {return T}

  var config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P: Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P = Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P(100, _verbose, alwaysTrue_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P _)

  def get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P: Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P = {return config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P}

  def set_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P(config: Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P): RandomLib ={
    config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P = config
    return this
  }

  // ============= SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS ===================
  def alwaysTrue_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS(v: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS): B = {return T}

  var config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS: Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS = Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS(100, _verbose, alwaysTrue_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS _)

  def get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS: Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS = {return config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS}

  def set_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS(config: Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS): RandomLib ={
    config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS = config
    return this
  }

  // ============= SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container ===================
  def alwaysTrue_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container(v: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container): B = {return T}

  var config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container: Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container = Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container(100, _verbose, F, ISZ(), alwaysTrue_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container _)

  def get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container: Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container = {return config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container}

  def set_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container(config: Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container): RandomLib ={
    config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container = config
    return this
  }

  // ============= SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P ===================
  def alwaysTrue_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P(v: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P): B = {return T}

  var config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P: Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P = Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P(100, _verbose, alwaysTrue_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P _)

  def get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P: Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P = {return config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P}

  def set_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P(config: Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P): RandomLib ={
    config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P = config
    return this
  }

  // ============= SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS ===================
  def alwaysTrue_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS(v: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS): B = {return T}

  var config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS: Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS = Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS(100, _verbose, alwaysTrue_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS _)

  def get_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS: Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS = {return config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS}

  def set_Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS(config: Config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS): RandomLib ={
    config_SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS = config
    return this
  }

  // ============= SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container ===================
  def alwaysTrue_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container(v: SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container): B = {return T}

  var config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container: Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container = Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container(100, _verbose, F, ISZ(), alwaysTrue_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container _)

  def get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container: Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container = {return config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container}

  def set_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container(config: Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container): RandomLib ={
    config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container = config
    return this
  }

  // ============= SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P ===================
  def alwaysTrue_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P(v: SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P): B = {return T}

  var config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P: Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P = Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P(100, _verbose, alwaysTrue_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P _)

  def get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P: Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P = {return config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P}

  def set_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P(config: Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P): RandomLib ={
    config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P = config
    return this
  }

  // ============= SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS ===================
  def alwaysTrue_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS(v: SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS): B = {return T}

  var config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS: Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS = Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS(100, _verbose, alwaysTrue_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS _)

  def get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS: Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS = {return config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS}

  def set_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS(config: Config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS): RandomLib ={
    config_SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS = config
    return this
  }

  // ============= SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container ===================
  def alwaysTrue_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container(v: SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container): B = {return T}

  var config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container: Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container = Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container(100, _verbose, F, ISZ(), alwaysTrue_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container _)

  def get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container: Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container = {return config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container}

  def set_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container(config: Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container): RandomLib ={
    config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container = config
    return this
  }

  // ============= SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P ===================
  def alwaysTrue_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P(v: SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P): B = {return T}

  var config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P: Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P = Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P(100, _verbose, alwaysTrue_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P _)

  def get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P: Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P = {return config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P}

  def set_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P(config: Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P): RandomLib ={
    config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P = config
    return this
  }

  // ============= SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS ===================
  def alwaysTrue_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS(v: SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS): B = {return T}

  var config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS: Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS = Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS(100, _verbose, alwaysTrue_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS _)

  def get_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS: Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS = {return config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS}

  def set_Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS(config: Config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS): RandomLib ={
    config_SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS = config
    return this
  }

  // ============= SW.FrameProtocol.Type ===================
  def alwaysTrue_SWFrameProtocolType(v: SW.FrameProtocol.Type): B = {return T}

  var config_SWFrameProtocolType: Config_SWFrameProtocolType = Config_SWFrameProtocolType(100, _verbose, alwaysTrue_SWFrameProtocolType _)

  def get_Config_SWFrameProtocolType: Config_SWFrameProtocolType = {return config_SWFrameProtocolType}

  def set_Config_SWFrameProtocolType(config: Config_SWFrameProtocolType): RandomLib ={
    config_SWFrameProtocolType = config
    return this
  }

  // ============= SW.FrameProtocol_Payload ===================
  def alwaysTrue_SWFrameProtocol_Payload(v: SW.FrameProtocol_Payload): B = {return T}

  var config_SWFrameProtocol_Payload: Config_SWFrameProtocol_Payload = Config_SWFrameProtocol_Payload(100, _verbose, alwaysTrue_SWFrameProtocol_Payload _)

  def get_Config_SWFrameProtocol_Payload: Config_SWFrameProtocol_Payload = {return config_SWFrameProtocol_Payload}

  def set_Config_SWFrameProtocol_Payload(config: Config_SWFrameProtocol_Payload): RandomLib ={
    config_SWFrameProtocol_Payload = config
    return this
  }

  // ============= SW.InternetProtocol.Type ===================
  def alwaysTrue_SWInternetProtocolType(v: SW.InternetProtocol.Type): B = {return T}

  var config_SWInternetProtocolType: Config_SWInternetProtocolType = Config_SWInternetProtocolType(100, _verbose, alwaysTrue_SWInternetProtocolType _)

  def get_Config_SWInternetProtocolType: Config_SWInternetProtocolType = {return config_SWInternetProtocolType}

  def set_Config_SWInternetProtocolType(config: Config_SWInternetProtocolType): RandomLib ={
    config_SWInternetProtocolType = config
    return this
  }

  // ============= SW.InternetProtocol_Payload ===================
  def alwaysTrue_SWInternetProtocol_Payload(v: SW.InternetProtocol_Payload): B = {return T}

  var config_SWInternetProtocol_Payload: Config_SWInternetProtocol_Payload = Config_SWInternetProtocol_Payload(100, _verbose, alwaysTrue_SWInternetProtocol_Payload _)

  def get_Config_SWInternetProtocol_Payload: Config_SWInternetProtocol_Payload = {return config_SWInternetProtocol_Payload}

  def set_Config_SWInternetProtocol_Payload(config: Config_SWInternetProtocol_Payload): RandomLib ={
    config_SWInternetProtocol_Payload = config
    return this
  }

  // ============= SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container ===================
  def alwaysTrue_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container(v: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container): B = {return T}

  var config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container: Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container = Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container(100, _verbose, F, ISZ(), alwaysTrue_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container _)

  def get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container: Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container = {return config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container}

  def set_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container(config: Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container): RandomLib ={
    config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container = config
    return this
  }

  // ============= SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P ===================
  def alwaysTrue_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(v: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P): B = {return T}

  var config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P: Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P = Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(100, _verbose, alwaysTrue_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P _)

  def get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P: Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P = {return config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P}

  def set_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(config: Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P): RandomLib ={
    config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P = config
    return this
  }

  // ============= SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS ===================
  def alwaysTrue_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(v: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS): B = {return T}

  var config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS: Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS = Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(100, _verbose, alwaysTrue_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS _)

  def get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS: Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS = {return config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS}

  def set_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(config: Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS): RandomLib ={
    config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS = config
    return this
  }

  // ============= SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container ===================
  def alwaysTrue_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container(v: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container): B = {return T}

  var config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container: Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container = Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container(100, _verbose, F, ISZ(), alwaysTrue_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container _)

  def get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container: Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container = {return config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container}

  def set_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container(config: Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container): RandomLib ={
    config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container = config
    return this
  }

  // ============= SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P ===================
  def alwaysTrue_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(v: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P): B = {return T}

  var config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P: Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P = Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(100, _verbose, alwaysTrue_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P _)

  def get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P: Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P = {return config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P}

  def set_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(config: Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P): RandomLib ={
    config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P = config
    return this
  }

  // ============= SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS ===================
  def alwaysTrue_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(v: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS): B = {return T}

  var config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS: Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS = Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(100, _verbose, alwaysTrue_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS _)

  def get_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS: Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS = {return config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS}

  def set_Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(config: Config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS): RandomLib ={
    config_SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS = config
    return this
  }

  // ============= IS[SW.RawEthernetMessage.I] ===================
  def alwaysTrue_ISSWRawEthernetMessageIU8(v: IS[SW.RawEthernetMessage.I, U8]): B = {return T}

  var config_ISSWRawEthernetMessageIU8: Config_ISSWRawEthernetMessageIU8 = Config_ISSWRawEthernetMessageIU8(0, 20, 100, _verbose, alwaysTrue_ISSWRawEthernetMessageIU8 _)
  def get_Config_ISSWRawEthernetMessageIU8: Config_ISSWRawEthernetMessageIU8 = {return config_ISSWRawEthernetMessageIU8}

  def set_Config_ISSWRawEthernetMessageIU8(config: Config_ISSWRawEthernetMessageIU8): RandomLib ={
    config_ISSWRawEthernetMessageIU8 = config
    return this
  }

  // ============= SW.RawEthernetMessage ===================
  def alwaysTrue_SWRawEthernetMessage(v: SW.RawEthernetMessage): B = {return T}

  var config_SWRawEthernetMessage: Config_SWRawEthernetMessage = Config_SWRawEthernetMessage(100, _verbose, alwaysTrue_SWRawEthernetMessage _)

  def get_Config_SWRawEthernetMessage: Config_SWRawEthernetMessage = {return config_SWRawEthernetMessage}

  def set_Config_SWRawEthernetMessage(config: Config_SWRawEthernetMessage): RandomLib ={
    config_SWRawEthernetMessage = config
    return this
  }

  // ============= SW.RawEthernetMessage_Payload ===================
  def alwaysTrue_SWRawEthernetMessage_Payload(v: SW.RawEthernetMessage_Payload): B = {return T}

  var config_SWRawEthernetMessage_Payload: Config_SWRawEthernetMessage_Payload = Config_SWRawEthernetMessage_Payload(100, _verbose, alwaysTrue_SWRawEthernetMessage_Payload _)

  def get_Config_SWRawEthernetMessage_Payload: Config_SWRawEthernetMessage_Payload = {return config_SWRawEthernetMessage_Payload}

  def set_Config_SWRawEthernetMessage_Payload(config: Config_SWRawEthernetMessage_Payload): RandomLib ={
    config_SWRawEthernetMessage_Payload = config
    return this
  }

  // ============= SW.StructuredEthernetMessage_i ===================
  def alwaysTrue_SWStructuredEthernetMessage_i(v: SW.StructuredEthernetMessage_i): B = {return T}

  var config_SWStructuredEthernetMessage_i: Config_SWStructuredEthernetMessage_i = Config_SWStructuredEthernetMessage_i(100, _verbose, alwaysTrue_SWStructuredEthernetMessage_i _)

  def get_Config_SWStructuredEthernetMessage_i: Config_SWStructuredEthernetMessage_i = {return config_SWStructuredEthernetMessage_i}

  def set_Config_SWStructuredEthernetMessage_i(config: Config_SWStructuredEthernetMessage_i): RandomLib ={
    config_SWStructuredEthernetMessage_i = config
    return this
  }

  // ============= SW.StructuredEthernetMessage_i_Payload ===================
  def alwaysTrue_SWStructuredEthernetMessage_i_Payload(v: SW.StructuredEthernetMessage_i_Payload): B = {return T}

  var config_SWStructuredEthernetMessage_i_Payload: Config_SWStructuredEthernetMessage_i_Payload = Config_SWStructuredEthernetMessage_i_Payload(100, _verbose, alwaysTrue_SWStructuredEthernetMessage_i_Payload _)

  def get_Config_SWStructuredEthernetMessage_i_Payload: Config_SWStructuredEthernetMessage_i_Payload = {return config_SWStructuredEthernetMessage_i_Payload}

  def set_Config_SWStructuredEthernetMessage_i_Payload(config: Config_SWStructuredEthernetMessage_i_Payload): RandomLib ={
    config_SWStructuredEthernetMessage_i_Payload = config
    return this
  }

  // ============= util.Container ===================
  def alwaysTrue_utilContainer(v: util.Container): B = {return T}

  var config_utilContainer: Config_utilContainer = Config_utilContainer(100, _verbose, F, ISZ(), alwaysTrue_utilContainer _)

  def get_Config_utilContainer: Config_utilContainer = {return config_utilContainer}

  def set_Config_utilContainer(config: Config_utilContainer): RandomLib ={
    config_utilContainer = config
    return this
  }

  // ============= util.EmptyContainer ===================
  def alwaysTrue_utilEmptyContainer(v: util.EmptyContainer): B = {return T}

  var config_utilEmptyContainer: Config_utilEmptyContainer = Config_utilEmptyContainer(100, _verbose, alwaysTrue_utilEmptyContainer _)

  def get_Config_utilEmptyContainer: Config_utilEmptyContainer = {return config_utilEmptyContainer}

  def set_Config_utilEmptyContainer(config: Config_utilEmptyContainer): RandomLib ={
    config_utilEmptyContainer = config
    return this
  }

  // ============= runtimemonitor.ObservationKind.Type ===================
  def alwaysTrue_runtimemonitorObservationKindType(v: runtimemonitor.ObservationKind.Type): B = {return T}

  var config_runtimemonitorObservationKindType: Config_runtimemonitorObservationKindType = Config_runtimemonitorObservationKindType(100, _verbose, alwaysTrue_runtimemonitorObservationKindType _)

  def get_Config_runtimemonitorObservationKindType: Config_runtimemonitorObservationKindType = {return config_runtimemonitorObservationKindType}

  def set_Config_runtimemonitorObservationKindType(config: Config_runtimemonitorObservationKindType): RandomLib ={
    config_runtimemonitorObservationKindType = config
    return this
  }
}

