interp.repositories() ::: List(
  coursierapi.MavenRepository.of("https://oss.sonatype.org/content/repositories/snapshots")
)

@

interp.configureCompiler(x => x.settings.source.value = scala.tools.nsc.settings.ScalaVersion("2.11.12"))

// Uncomment and change to use proxy
// System.setProperty("https.proxyHost", "proxy.example.com")
// System.setProperty("https.proxyPort", "3128")

import $ivy.`edu.berkeley.cs::chisel3:3.2.0` 
import $ivy.`edu.berkeley.cs::chisel-iotesters:1.3.0`
import $ivy.`edu.berkeley.cs::chisel-testers2:0.1.0`
import $ivy.`edu.berkeley.cs::dsptools:1.2.0`
import $ivy.`org.scalanlp::breeze:0.13.2`
import $ivy.`edu.berkeley.cs::rocket-dsptools:1.2.0`
import $ivy.`edu.berkeley.cs::firrtl-diagrammer:1.1.0`

// Convenience function to invoke Chisel and grab emitted Verilog.
def getVerilog(dut: => chisel3.core.UserModule): String = {
  import firrtl._
  return chisel3.Driver.execute(Array[String](), {() => dut}) match {
    case s:chisel3.ChiselExecutionSuccess => s.firrtlResultOption match {
      case Some(f:FirrtlExecutionSuccess) => f.emitted
    }
  }
}

// Convenience function to invoke Chisel and grab emitted FIRRTL.
def getFirrtl(dut: => chisel3.core.UserModule): String = {
  return chisel3.Driver.emit({() => dut})
}

def compileFIRRTL(
    inputFirrtl: String,
    compiler: firrtl.Compiler,
    customTransforms: Seq[firrtl.Transform] = Seq.empty,
    infoMode: firrtl.Parser.InfoMode = firrtl.Parser.IgnoreInfo,
    annotations: firrtl.AnnotationSeq = firrtl.AnnotationSeq(Seq.empty)
): String = {
  import firrtl.{Compiler, AnnotationSeq, CircuitState, ChirrtlForm, FIRRTLException}
  import firrtl.Parser._
  import scala.io.Source
  import scala.util.control.ControlThrowable
  import firrtl.passes._
  val outputBuffer = new java.io.CharArrayWriter
  try {
      //val parsedInput = firrtl.Parser.parse(Source.fromFile(input).getLines(), infoMode)
      val parsedInput = firrtl.Parser.parse(inputFirrtl.split("\n").toIterator, infoMode)
      compiler.compile(
         CircuitState(parsedInput, ChirrtlForm, annotations),
         outputBuffer,
         customTransforms)
  }

  catch {
    // Rethrow the exceptions which are expected or due to the runtime environment (out of memory, stack overflow)
    case p: ControlThrowable => throw p
    case p: PassException  => throw p
    case p: FIRRTLException => throw p
     // Treat remaining exceptions as internal errors.
       case e: Exception => firrtl.Utils.throwInternalError(exception = Some(e))
  }

  val outputString = outputBuffer.toString
  outputString
}

def stringifyAST(firrtlAST: firrtl.ir.Circuit): String = {
  var ntabs = 0
  val buf = new StringBuilder
  val string = firrtlAST.toString
  string.zipWithIndex.foreach { case (c, idx) =>
    c match {
      case ' ' =>
      case '(' =>
        ntabs += 1
        buf ++= "(\n" + "| " * ntabs
      case ')' =>
        ntabs -= 1
        buf ++= "\n" + "| " * ntabs + ")"
      case ','=> buf ++= ",\n" + "| " * ntabs
      case  c if idx > 0 && string(idx-1)==')' =>
        buf ++= "\n" + "| " * ntabs + c
      case c => buf += c
    }
  }
  buf.toString
}

// Returns path to module viz and hierarchy viz
def generateVisualizations(gen: () => chisel3.RawModule): (String, String) = {
    import dotvisualizer._
    import dotvisualizer.transforms._

    import java.io._
    import firrtl._
    import firrtl.annotations._

    import almond.interpreter.api.DisplayData
    import almond.api.helpers.Display

    import chisel3._
    import chisel3.experimental._
    import firrtl.ir.Module
    import sys.process._
    
    val targetDir = "build"
    val chiselIR = chisel3.Driver.elaborate(gen)
    val firrtlIR = chisel3.Driver.emit(chiselIR)
    val config = Config(targetDir = "build", firrtlSource = firrtlIR)
  
    val sourceFirrtl = {
      if(config.firrtlSource.nonEmpty) {
        config.firrtlSource
      }
      else {
        scala.io.Source.fromFile(config.firrtlSourceFile).getLines().mkString("\n")
      }
    }

    val ast = Parser.parse(sourceFirrtl)
    val uniqueTop = ast.main + ast.hashCode().toHexString
    val cmdRegex = "cmd[0-9]+([A-Za-z]+.*)".r
    val readableTop = ast.main match {
      case cmdRegex(n) => n
      case other => other
    }
    val newTop = readableTop
    
    val newModules: Seq[firrtl.ir.DefModule] = ast.modules.map {
        case m: Module if m.name == ast.main => m.copy(name = newTop)
        case other => other
    }
    
    val newAst = ast.copy(main = newTop, modules = newModules)
    
    val controlAnnotations: Seq[Annotation] = config.toAnnotations

    val loweredAst = ToLoFirrtl.lower(newAst)

    FileUtils.makeDirectory(targetDir)

    FirrtlDiagrammer.addCss(targetDir)

    val circuitState = CircuitState(loweredAst, LowForm, controlAnnotations)

    if(config.justTopLevel) {
      val justTopLevelTransform = new ModuleLevelDiagrammer
      justTopLevelTransform.execute(circuitState)
    } else {
      val x = new MakeDiagramGroup
      x.execute(circuitState)
    }

    s"cp build/${readableTop}.dot.svg build/${uniqueTop}.dot.svg"!!

    s"cp build/${readableTop}_hierarchy.dot.svg build/${uniqueTop}_hierarchy.dot.svg"!!
    
    val moduleView = targetDir + "/" + uniqueTop + ".dot.svg"
    val x = """<a name="top"></a><img src=" """ + moduleView + """" alt="Module View";" />"""
    
    val instanceView = targetDir + "/" + uniqueTop + "_hierarchy.dot.svg"
    val y = """<a name="top"></a><img src=" """ + instanceView + """" alt="Hierarchy View" style="width:480px;" />"""
    (x, y)

}


def visualize(gen: () => chisel3.RawModule): Unit = {
    val (moduleView, instanceView) = generateVisualizations(gen)
    html(moduleView)
}

def visualizeHierarchy(gen: () => chisel3.RawModule): Unit = {
    val (moduleView, instanceView) = generateVisualizations(gen)
    html(instanceView)
}

