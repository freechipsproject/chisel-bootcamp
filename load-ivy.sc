import $ivy.`edu.berkeley.cs::chisel3:3.0.0` 
import $ivy.`edu.berkeley.cs::chisel-iotesters:1.1.0`
import $ivy.`edu.berkeley.cs::dsptools:1.0.0`
import $ivy.`org.scalanlp::breeze:0.13.2`

// Convenience function to invoke Chisel and grab emitted Verilog.
def getVerilog(dut: => chisel3.core.UserModule): String = {
  import firrtl._
  return chisel3.Driver.execute(Array[String](), {() => dut}) match {
    case s:chisel3.ChiselExecutionSuccess => s.firrtlResultOption match {
      case Some(f:FirrtlExecutionSuccess) => f.emitted.drop(216)
    }
  }
}

// Convenience function to invoke Chisel and grab emitted FIRRTL.
def getFirrtl(dut: => chisel3.core.UserModule): String = {
  return chisel3.Driver.emit({() => dut}).drop(156)
}
