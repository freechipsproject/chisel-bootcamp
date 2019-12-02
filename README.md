[![Binder](https://mybinder.org/badge_logo.svg)](https://mybinder.org/v2/gh/freechipsproject/chisel-bootcamp/master)

**_For previous users of the bootcamp, we have upgraded from Scala 2.11 to Scala 2.12. If you are encountering errors, please follow the installation instructions to upgrade to 2.12._**

# Chisel Bootcamp

Elevate the level of your hardware design from instances to generators!
This bootcamp teaches you Chisel, a Berkeley hardware construction DSL written in Scala.
It teaches you Scala along the way, and it frames the learning of Chisel on the idea of *hardware generators*.

## What you'll learn

- Why hardware designs are better expressed as generators, not instances
- Basics and some advanced features of Scala, a modern programming language
- Basics and some advanced features of Chisel, a hardware description language embedded in Scala
- How to write unit tests for Chisel designs
- Basic introduction to some useful features in Chisel libraries, including [dsptools](https://github.com/ucb-bar/dsptools/) and [rocketchip](https://github.com/freechipsproject/rocket-chip).

## Prerequisites

- Familiarity with Verilog, VHDL, or at least some digital hardware design
- Programming experience in with a "high-level" language, be it in Python, Java, C++, etc.
- An earnest desire to learn

## Getting Started

Try it out [HERE](https://mybinder.org/v2/gh/freechipsproject/chisel-bootcamp/master)! No local installation required!

If you want to try it out locally, [look at installation instructions here](Install.md).

## Outline

The bootcamp is divided into modules, which are further subdivided.
This README serves as *Module 0*, an introduction and motivation to learning the material contained within.
*Module 1* gives a quick introduction to Scala.
It teaches you enough to start writing Chisel, but many more Scala concepts are taught along the way.
Chisel is introduced in *Module 2*, starting with a hardware example and breaking it down.
The rest of *Module 2* covers combinational and sequential logic, as well as software and hardware control flow.
*Module 3* teaches you how to write hardware generators in Chisel that take advantage of Scala's high-level programming language features.
By the end, you will be able to read and understand most of the [Chisel code base](https://github.com/freechipsproject/chisel3) and begin using [Rocket Chip](https://github.com/freechipsproject/rocket-chip).
This tutorial *does not* yet cover SBT, build systems, backend flows for FPGA or ASIC processes, or analog circuits.

## Motivation
All hardware description languages support writing single instances.
However, writing instances is tedious.
Why make the same mistakes writing a slightly modified version of something somebody else has likely already designed?
Verilog supports limited parameterization, such as bitwidths and generate statements, but this only gets you so far.
If we can't write a Verilog generator, we need to write a new instance, thus doubling our code size.
As a better option, we should write one program that generates both hardware instances, which would reduce our code size and make tedious things easier.
These programs are called generators.

Ideally, we want our generators to be (1) composable, (2) powerful, and (3) enable fine-grained control over the generated design.
Error checking is necessary to make sure a composition is legal; without it, debugging is difficult.
This requires a generator language to understand the semantics of the design (to know what’s legal and what’s not).
Also, the generator should not be overly verbose!
We want the generator program to concisely express many different designs, without rewriting it in if statements for each instance.
Finally, it should be a zero-cost abstraction.
Hardware design performance is very sensitive to small changes, and because of that, you need to be able to exactly specify the microarchitecture.
Generators are very different than high-level-synthesis (HLS).

The benefits of Chisel are in how you use it, not in the language itself.
If you decide to write instances instead of generators, you will see fewer advantages of Chisel over Verilog.
But if you take the time to learn how to write generators, then the power of Chisel will become apparent and you will realize you can never go back to writing Verilog.
Learning to write generators is difficult, but we hope this tutorial will pave the way for you to become a better hardware designer, programmer, and thinker!

## FAQ

### Kernel Crashes Upon Startup

I get the following error upon launching a Scala notebook and Jupyter says that the kernel has crashed:

```
Exception in thread "main" java.lang.RuntimeException: java.lang.NullPointerException
	at jupyter.kernel.server.ServerApp$.apply(ServerApp.scala:174)
	at jupyter.scala.JupyterScalaApp.delayedEndpoint$jupyter$scala$JupyterScalaApp$1(JupyterScala.scala:93)
	at jupyter.scala.JupyterScalaApp$delayedInit$body.apply(JupyterScala.scala:13)
  ...

Caused by: java.lang.NullPointerException
	at ammonite.runtime.Classpath$.classpath(Classpath.scala:31)
	at ammonite.interp.Interpreter.init(Interpreter.scala:93)
	at ammonite.interp.Interpreter.processModule(Interpreter.scala:409)
	at ammonite.interp.Interpreter$$anonfun$10.apply(Interpreter.scala:151)
	at ammonite.interp.Interpreter$$anonfun$10.apply(Interpreter.scala:148)
  ...
```

Make sure that you have **Java 8** selected for running Jupyter (see the instructions above).

## Contributors
- Stevo Bailey ([stevo@berkeley.edu](mailto:stevo@berkeley.edu))
- Adam Izraelevitz ([adamiz@berkeley.edu](mailto:azidar@berkeley.edu))
- Richard Lin ([richard.lin@berkeley.edu](mailto:edwardw@berkeley.edu))
- Chick Markley ([chick@berkeley.edu](mailto:chick@berkeley.edu))
- Paul Rigge ([rigge@berkeley.edu](mailto:rigge@berkeley.edu))
- Edward Wang ([edwardw@berkeley.edu](mailto:edwardw@berkeley.edu))
