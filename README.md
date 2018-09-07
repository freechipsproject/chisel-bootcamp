# Generator Bootcamp

Elevate the level of your hardware design from instances to generators!
This bootcamp teaches you Chisel, a Berkeley hardware construction DSL written in Scala.
It teaches you Scala along the way, and it frames the learning of Chisel on the idea of *hardware generators*.

## What you'll learn

- Why hardware designs are better expressed as generators, not instances
- Basics and some advanced features of Scala, a modern programming language
- Basics and some advanced features of Chisel, a hardware description language embedded in Scala
- How to write unit tests for Chisel designs

## Prerequisites

- Familiarity with Verilog, VHDL, or at the very least, hardware design in general
- Programming experience in a modern language, be it in Python, Java, C++, etc.
- An earnest desire to learn something new

## Outline

The bootcamp is divided into modules, which are further subdivided.
This README serves as *Module 0*, an introduction and motivation to learning the material contained within.
*Module 1* gives a quick introduction to Scala.
It teaches you enough to start writing Chisel, but many more Scala concepts are taught along the way.
Chisel is introduced in *Module 2*, starting with a hardware example and breaking it down.
The rest of *Module 2* covers combinational and sequential logic, and software and hardware control flow.
*Module 3* takes advantages of Scala's high-level programming language features to teach writing Chisel hardware generators.
By the end, you will be able to read and understand most of the [Chisel code base](https://github.com/freechipsproject/chisel3) and [Rocket Chip](https://github.com/freechipsproject/rocket-chip) code.
It does not yet cover SBT, Rocket Chip usage, or ChiselDSP.

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

## Setup Instructions

Run the instructions below for your particular situation.
Note that we include a custom javascript file for Jupyter, so if you already have Jupyter installed, you still need to install the custom.js file.

Note: Make sure you are using **Java 8** (NOT Java 9) and have the JDK8 installed. Coursier/jupyter-scala does not appear to be compatible with Java 9 yet as of January 2018.

If you do have multiple version of Java, make sure to select Java 8 (1.8) before running `jupyter notebook`:

* On Windows: https://gist.github.com/rwunsch/d157d5fe09e9f7cdc858cec58c8462d6
* On Mac OS: https://stackoverflow.com/questions/21964709/how-to-set-or-change-the-default-java-jdk-version-on-os-x

### Local Installation - Mac/Linux

First install Jupyter Scala.

Dependencies: openssh-client, openjdk-8-jre, openjdk-8-jdk (-headless OK for both),  ca-certificates-java

First, use pip3 to install jupyter (or pip for python 2): http://jupyter.org/install.html
```
pip3 install --upgrade pip
pip3 install jupyter --ignore-installed
```

If pip3 isn't working out of the box (possibly because your Python3 version is out of date), you can try `python3 -m pip` in lieu of `pip3`.

(To reinstall jupyter later for whatever reason, you can use `--no-deps` to avoid re-installing all the dependencies.)

Then, clone this repo and run the installation script: https://github.com/alexarchambault/jupyter-scala
```
git clone https://github.com/jupyter-scala/jupyter-scala.git
cd jupyter-scala && ./jupyter-scala
```

Now clone this repo and install the customization script.
If you already have one, append this script to it.
```
git clone https://github.com/ucb-bar/generator-bootcamp.git
cd generator-bootcamp
mkdir -p ~/.jupyter/custom
cp source/custom.js ~/.jupyter/custom/custom.js
```

And to start the bootcamp on your local machine:
```
jupyter notebook
```


### Local Installation - Windows

These notes describe, in general, the way to install the Generator Bootcamp under Windows 10.
Many different windows configurations may be encountered and some changes may be required.
Please let us know of there are things out of date, or otherwise should be covered here.

>There are several times where you may want to launch a Command (shell) window.
I have discovered that launching the command window in Administrator Mode is helpful. 
To do that from the bottom left Launcher, find or search for 'CMD' when selecting it from
the menu, right click and choose, "Launch in Administrator Mode".
Find more details on this [here](http://www.thewindowsclub.com/how-to-run-command-prompt-as-an-administrator)
and other places.
It is also best to relauch
command windows between steps in the process so that newly installed software will be recognized.

#### Be sure java is installed (ideally java 8).
If you type `java` into a command prompt and it says command not found, you need to install
[Java](https://java.com/en/download/help/windows_manual_download.xml).

#### Install Jupyter
Jupyter recommends using the Anaconda distribution, here is the 
[Windows download](https://www.anaconda.com/download/#windows).

Near the end of the Jupyter installation is a question about whether to add Jupyter to the PATH.
Windows does not recommend this, but I do.  It will make it easier to run using the command prompt.
 
#### Install Scala components.
The simplest way seens to be to Download Coursier from [https://git.io/vgvpD](https://git.io/vgvpD)

Go to download folder, where "coursier" (file) is
```
java -noverify -jar coursier launch -r sonatype:releases -r sonatype:snapshots -i ammonite -I ammonite:org.jupyter-scala:ammonite-runtime_2.11.11:0.8.3-1 -I ammonite:org.jupyter-scala:scala-api_2.11.11:0.4.2 org.jupyter-scala:scala-cli_2.11.11:0.4.2 -- --id scala --name "Scala"
```

#### Install the generator-bootcamp repo.
Download the [generator-bootcamp](https://github.com/ucb-bar/generator-bootcamp) as a zip file (or use a Windows git client)
and unpack it.
I copied it to my c:\Users\me\generator-bootcamp.
There may be better ways and more standard conventions on where to put this kind of thing.
But the most important thing is that you need to know where it is.

Install the customization script by moving `generator-bootcamp/source/custom.js` to 
`%HOMEDRIVE%%HOMEPATH%\.jupyter\custom\custom.js`.
If you already have a custom.js file, append this script to it.

#### Launch the Jupyter and the bootcamp
In the directory containing the unpacked generator-bootcamp repo, from a new command window type:
```bash
jupyter notebook
```
This should start the bootcamp server and open a top page bootcamp menu in your default browser.  If it does not
look for the something like the following in the command window and copy and paste the link you see into
a browser window.
```bash
    Copy/paste this URL into your browser when you connect for the first time,
    to login with a token:
        http://localhost:8888/?token=9c503729c379fcb3c7a17087f05462c733c1733eb8b31d07
``` 

Good Luck!

### Cadence AWS Setup

If you don't know or don't have access to Cadence AWS, skip this section.

Navigate to your working directory, which is probably your home directory.

```
cd ~
```

Then run the following commands.
The default shell is c-shell, but if you switch to bash, source `jupyter_sh` instead of `jupyter_csh`.
```
source /craft/tools/jupyter/jupyter_csh
```

The default browser, Konqueror, won't work with Jupyter.
Launch Firefox and set it as your default browser when it asks.
```
/craft/cdns_sw_inst/firefox/45.3.0esr/firefox
```

Clone the repo and launch Jupyter.
If it asks for a token, copy and paste the *to login with a token* URL seen in the terminal.
Future launches will be happy for a while.
```
git clone /craft/tools/chisel/generator-bootcamp.git
cd generator-bootcamp
cp /craft/tools/jupyter/jupyter_support/rocket-dsp-utils-assembly-1.0.jar ../
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
- Paul Rigge([rigge@berkeley.edu](mailto:rigge@berkeley.edu))
- Edward Wang ([edwardw@berkeley.edu](mailto:edwardw@berkeley.edu))
