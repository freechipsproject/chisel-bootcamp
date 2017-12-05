# Generator Bootcamp

Elevate the level of your hardware design from instances to generators!
This bootcamp teaches you Chisel, a Berkeley hardware construction DSL written in Scala.
It teaches you Scala along the way, and it frames the learning of Chisel on the idea of *hardware generators*.

## Installation Instructions

First clone the repository.
Then install the following dependencies.

### Jupyter Scala

#### Mac/Linux Installation
Dependencies: openssh-client, openjdk-8-jre, openjdk-8-jdk (-headless OK for both),  ca-certificates-java

First, use pip3 to install jupyter (or pip for python 2): http://jupyter.org/install.html
```
pip3 install --upgrade pip
pip3 install jupyter
```

Then, clone this repo and run the installation script: https://github.com/alexarchambault/jupyter-scala
```
git clone https://github.com/jupyter-scala/jupyter-scala.git
cd jupyter-scala && ./jupyter-scala
```

Install the customization script.
If you already have one, append this script to it.
```
mkdir -p ~/.jupyter/custom
cp source/custom.js ~/.jupyter/custom/custom.js
```

#### Windows Installation
Be sure java is installed (ideally java 8). 
If you type `java` into a command prompt and it says command not found, you need to install java.

Download Coursier: https://git.io/vgvpD

Go to download folder, where "coursier" (file) is
```
java -noverify -jar coursier launch -r sonatype:releases -r sonatype:snapshots -i ammonite -I ammonite:org.jupyter-scala:ammonite-runtime_2.11.11:0.8.3-1 -I ammonite:org.jupyter-scala:scala-api_2.11.11:0.4.2 org.jupyter-scala:scala-cli_2.11.11:0.4.2 -- --id scala --name "Scala"
```

Install the customization script by moving `source/custom.js` to `~/.jupyter/custom/custom.js`.
If you already have one, append this script to it.

### Verilator (Optional)

Verilator is a free, open-source Verilog simulator supported by the Chisel testers.
Follow the instructions on their webiste: https://www.veripool.org/projects/verilator/wiki/Installing

### Cadence AWS Setup

If you don't know or don't have access to Cadence AWS, skip this section.

Navigate to your working directory, which is probably your home directory. 
Then run the following commands.
The default shell is c-shell, but if you switch to bash, source `jupyter_sh` instead of `jupyter_csh`.
```
source /craft/tools/jupyter/jupyter_csh
git clone /proj/craft_flow/source/chisel/generator-bootcamp
cd generator-bootcamp
cp /craft/tools/jupyter/jupyter_support/rocket-dsp-utils-assembly-1.0.jar ../
```

The default browser, Konqueror, won't work with Jupyter.
Launch Firefox and set it as your default browser when it asks.
```
/craft/cdns_sw_inst/firefox/45.3.0esr/firefox
```

Launch Jupyter.
If it asks for a token, copy and paste the *to login with a token* URL seen in the terminal. 
Future launches will be happy for a while.
```
jupyter notebook
```

### Cadence Chamber Setup

If you don't know what the Cadence Chamber is, skip this section.
Navigate to your work directory, likely `/projects/craft_flow/work/<username>/`. 
Then run the following commands. 
Note that `/proj/` is an alias to `/projects/`. 
If you are in bash, source `jupyter_sh` instead of `jupyter_csh`.

```
source /proj/craft_flow/tools/jupyter/jupyter_csh
git clone /proj/craft_flow/source/chisel/generator-bootcamp
cd generator-bootcamp
jupyter notebook
```

## Prerequisites

- familiarity with Verilog, VHDL, or at the very least, hardware design in general
- programming experience in a modern language, be it in Python, Java, C++, etc.
- an earnest desire to learn something new

## What you'll learn

- Why hardware designs are better expressed as generators, not instances
- Basics and some advanced features of Scala, a modern programming language
- Basics and some advanced features of Chisel, a hardware description language embedded in Scala
- How to write unit tests for Chisel designs

## Outline

The bootcamp is divided into modules, which are further subdivided.
This README serves as *Module 0*, an introduction and motivation to learning the material contained within.
*Module 1* gives a quick introduction to Scala.
It teaches you enough to start writing Chisel, but many more Scala concepts are taught along the way.
Chisel is introduced in *Module 2*, starting with a hardware example and breaking it down.
The rest of *Module 2* covers combinational and sequential logic, and software and hardware control flow.
*Module 3* takes advantages of Scala's high-level programming language features to teach writing Chisel hardware generators.
By the end, you will be able to read and understand most of the [Chisel code base](https://github.com/freechipsproject/chisel3) and [Rocket Chip](https://github.com/freechipsproject/rocket-chip) code.
It does not yet cover SBT, FIRRTL, Rocket Chip usage, or ChiselDSP.

## Motivation
All hardware description languages support writing single instances.
However, writing instances is tedious.
Why make the same mistakes writing a slightly modified version of something somebody else has likely already designed?
Verilog supports limited parameterization, such as bitwidths and generate statements, but this only gets you so far.
If we can't write a Verilog generator, we need to write a new instance, thus doubling our code size.
As a better option, we should write one program that generates both hardware instances, which would reduce our code size and make tedious things easier. 
These programs are called generators.

Ideally, we want our generators to be (1) composable, (2) powerful, and (3) enable fine-grain control over the generated design.
Error checking is necessary to make sure a composition is legal; without it, debugging is difficult.
This requires a generator language to understand the semantics of the design (to know what’s legal and what’s not).
Also, the generator should not be overly verbose! 
We want the generator program to concisely express many different designs, without rewriting it in if statements for each instance.
Finally, it should be a zero-cost abstraction. 
Hardware design performance is very sensitive to small changes, and because of that, you need to be able to exactly specify the microarchitecture.
Generators are very different than high-level-synthesis (HLS).

The benefits of Chisel are in how you use it, not in the language itself. 
If you decide to write instances instead of generators, you will not see any advantages of Chisel over Verilog. 
But, if you take the time to learn how to write generators, then the power of Chisel will become apparent and you will realize you can never go back to writing Verilog.
Learning to write generators is difficult, but we hope this tutorial will pave the way for you to become a better hardware designer, programmer, and thinker!


## Contributors
- Stevo Bailey ([stevo@berkeley.edu](mailto:stevo@berkeley.edu))
- Adam Izraelivitz ([azidar@berkeley.edu](mailto:azidar@berkeley.edu))
- Richard Lin ([richard.lin@berkeley.edu](mailto:edwardw@berkeley.edu))
- Chick Markley ([chick@berkeley.edu](mailto:chick@berkeley.edu))
- Paul Rigge([rigge@berkeley.edu](mailto:rigge@berkeley.edu))
- Edward Wang ([edwardw@berkeley.edu](mailto:edwardw@berkeley.edu))
