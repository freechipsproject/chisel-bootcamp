interp.repositories() ::: List(
  coursierapi.MavenRepository.of("https://oss.sonatype.org/content/repositories/snapshots")
)

@

interp.configureCompiler(x => x.settings.source.value = scala.tools.nsc.settings.ScalaVersion("2.11.12"))

// Uncomment and change to use proxy
// System.setProperty("https.proxyHost", "proxy.example.com")
// System.setProperty("https.proxyPort", "3128")

import $ivy.`edu.berkeley.cs::chisel3:3.4.+`
import $ivy.`edu.berkeley.cs::chisel-iotesters:1.5.+`
import $ivy.`edu.berkeley.cs::chiseltest:0.3.+`
import $ivy.`edu.berkeley.cs::dsptools:1.4.+`
import $ivy.`org.scalanlp::breeze:0.13.2`
import $ivy.`edu.berkeley.cs::rocket-dsptools:1.2.0`
import $ivy.`edu.berkeley.cs::firrtl-diagrammer:1.3.+`

import $ivy.`org.scalatest::scalatest:3.2.2`

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
    import chisel3.stage._
    import firrtl.ir.Module
    import sys.process._

    val sourceFirrtl = scala.Console.withOut(new PrintStream(new ByteArrayOutputStream())) {
      (new ChiselStage).emitChirrtl(gen())
    }
    val ast = Parser.parse(sourceFirrtl)

    val uniqueTopName = ast.main + ast.hashCode().toHexString

    val targetDir = s"diagrams/$uniqueTopName/"

    val cmdRegex = "cmd[0-9]+([A-Za-z]+.*)".r
    val readableTop = ast.main match {
      case cmdRegex(n) => n
      case other => other
    }
    val newTop = readableTop

    // Console hack prevents unnecessary chatter appearing in cell
    scala.Console.withOut(new PrintStream(new ByteArrayOutputStream())) {
      val sourceFirrtl = (new ChiselStage).emitChirrtl(gen())

    val newModules: Seq[firrtl.ir.DefModule] = ast.modules.map {
      case m: Module if m.name == ast.main => m.copy(name = newTop)
      case other => other
    }
    val newAst = ast.copy(main = newTop, modules = newModules)

    val controlAnnotations: Seq[Annotation] = Seq(
        firrtl.stage.FirrtlSourceAnnotation(sourceFirrtl),
        firrtl.options.TargetDirAnnotation(targetDir),
        dotvisualizer.stage.OpenCommandAnnotation("")
      )

      (new dotvisualizer.stage.DiagrammerStage).execute(Array.empty, controlAnnotations)
    }
    val moduleView = s"""$targetDir/$newTop.dot.svg"""
    val instanceView = s"""$targetDir/${newTop}_hierarchy.dot.svg"""

    val svgModuleText = FileUtils.getText(moduleView)
    val svgInstanceText = FileUtils.getText(instanceView)

    val x = s"""<div width="100%" height="100%" overflow="scroll">$svgModuleText</div>"""
    val y = s"""<div> width="100%" height="100%"  overflow="scroll">$svgInstanceText</div>"""

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

