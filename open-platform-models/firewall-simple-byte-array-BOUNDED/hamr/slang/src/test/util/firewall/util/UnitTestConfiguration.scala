//#Sireum

package firewall.util

import org.sireum._
import firewall.util.Container
import firewall.GumboXUtil.GumboXResult

// Do not edit this file as it will be overwritten if HAMR codegen is rerun

@msig trait UnitTestConfiguration {
  def name: String
  def description: String
  def profile: Profile
  def test(c: Container): GumboXResult.Type
  def genReplay: (Container, String, GumboXResult.Type) => Option[String]
  def verbose: B
}

@msig trait UnitTestConfigurationBatch extends UnitTestConfiguration {
  def numTests: Z
  def numTestVectorGenRetries: Z
  def failOnUnsatPreconditions: B
}

@msig trait Profile {
  def next: Container
}
