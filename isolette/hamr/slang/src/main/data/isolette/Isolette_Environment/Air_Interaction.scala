// #Sireum

package isolette.Isolette_Environment

import org.sireum._
import isolette._

// Do not edit this file as it will be overwritten if HAMR codegen is rerun

@enum object Air_Interaction {
  "Dummy_Air_Interaction_Enum"
}

object Air_Interaction_Payload {
  def example(): Air_Interaction_Payload = {
    return Air_Interaction_Payload(Isolette_Environment.Air_Interaction.byOrdinal(0).get)
  }
}

@datatype class Air_Interaction_Payload(value: Isolette_Environment.Air_Interaction.Type) extends art.DataContent
