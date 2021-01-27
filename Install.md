## Local Setup Instructions

If you want to run the bootcamp locally, run the following instructions below for your particular situation.
Note that we include a custom javascript file for Jupyter, so if you already have Jupyter installed, you still need to install the custom.js file.

Note: Make sure you are using **Java 8** (NOT Java 9) and have the JDK8 installed. Coursier/jupyter-scala does not appear to be compatible with Java 9 yet as of January 2018.

If you do have multiple version of Java, make sure to select Java 8 (1.8) before running `jupyter notebook`:

* On Windows: https://gist.github.com/rwunsch/d157d5fe09e9f7cdc858cec58c8462d6
* On Mac OS: https://stackoverflow.com/questions/21964709/how-to-set-or-change-the-default-java-jdk-version-on-os-x

### Local Installation using Docker - Linux/Mac/Windows

Make sure you have Docker [installed](https://docs.docker.com/get-docker/) on your system.

Run the following command:

```
docker run -it --rm -p 8888:8888 ucbbar/chisel-bootcamp
```

This will download a Dokcer image for the bootcamp and run it. The output will end in the following message:

```
    To access the notebook, open this file in a browser:
        file:///home/bootcamp/.local/share/jupyter/runtime/nbserver-6-open.html
    Or copy and paste one of these URLs:
        http://79b8df8411f2:8888/?token=LONG_RANDOM_TOKEN
     or http://127.0.0.1:8888/?token=LONG_RANDOM_TOKEN
```

Copy the last link, the one starting with https://127.0.0.1:8888 to your browser and follow the Bootcamp.

### Local Installation - Mac/Linux

This bootcamp uses Jupyter notebooks.
Jupyter notebooks allow you to interactively run code in your browser.
It supports multiple programming languages.
For this bootcamp, we'll install jupyter first and then the Scala-specific jupyter backend (now called almond).


#### Jupyter
First install Jupyter.

Dependencies: openssh-client, openjdk-8-jre, openjdk-8-jdk (-headless OK for both),  ca-certificates-java

First, use pip3 to install jupyter (or pip for python 2): http://jupyter.org/install.html
```
pip3 install --upgrade pip
pip3 install jupyter --ignore-installed
```

If pip3 isn't working out of the box (possibly because your Python3 version is out of date), you can try `python3 -m pip` in lieu of `pip3`.

(To reinstall jupyter later for whatever reason, you can use `--no-deps` to avoid re-installing all the dependencies.)

You may want to try out Jupyter lab, the newer interface developed by Project Jupyter.
It is especially useful if you want to be able to run a terminal emulator in your browser.
It can be installed with `pip3`:
```
pip3 install jupyterlab
```

#### Jupyter Backend for Scala

If you experience errors or issues with this section, try running `rm -rf ~/.local/share/jupyter/kernels/scala/` first.

Next, download coursier and use it to install almond (see [here](https://almond.sh/docs/quick-start-install) for the source for these instructions):
```
curl -L -o coursier https://git.io/coursier-cli && chmod +x coursier
SCALA_VERSION=2.12.10 ALMOND_VERSION=0.9.1
./coursier bootstrap -r jitpack \
    -i user -I user:sh.almond:scala-kernel-api_$SCALA_VERSION:$ALMOND_VERSION \
    sh.almond:scala-kernel_$SCALA_VERSION:$ALMOND_VERSION \
    --sources --default=true \
    -o almond
./almond --install
```

You can delete `coursier` and `almond` files if you so desire.

#### Install bootcamp
Now clone the bootcamp repo and install the customization script.
If you already have one, append this script to it.

```
git clone https://github.com/freechipsproject/chisel-bootcamp.git
cd chisel-bootcamp
mkdir -p ~/.jupyter/custom
cp source/custom.js ~/.jupyter/custom/custom.js
```

And to start the bootcamp on your local machine:
```
jupyter notebook
```

If you installed Jupyter Lab, run `jupyter-lab` instead.


### Local Installation - Windows

These notes describe, in general, the way to install the Generator Bootcamp under Windows 10.
Many different Windows configurations may be encountered and some changes may be required.
Please let us know of there are things out of date, or should otherwise be covered here.

>There are several times where you may want to launch a Command (shell) window.
I have discovered that launching the command window in Administrator Mode is helpful.
To do that from the bottom left Launcher, find or search for 'CMD' when selecting it from
the menu, right click and choose, "Launch in Administrator Mode".
Find more details on this [here](http://www.thewindowsclub.com/how-to-run-command-prompt-as-an-administrator)
and other places.
It is also best to relauch any command windows between steps in the process (e.g. after installing Java)
so that any newly installed software will be recognized.

#### Be sure Java is installed (ideally Java 8).
If you type `java` into a command prompt and it says command not found, you need to install
[Java](https://adoptopenjdk.net/installation.html).

#### Install Jupyter
Jupyter recommends using the Anaconda distribution, here is the
[Windows download](https://www.anaconda.com/download/#windows).

Near the end of the Jupyter installation is a question about whether to add Jupyter to the PATH.
Windows does not recommend this, but I do.  It will make it easier to run using the command prompt.

If you did not elect to add Jupyter to the PATH, start a prompt using the
"Anaconda Prompt (Anaconda3)" shortcut from the Start Menu.

#### Install Scala components.

The simplest way seems to be to download Coursier from [here](https://github.com/coursier/coursier/releases/download/v2.0.0-RC6-24/coursier).

Go to download folder, where `coursier` (file) is

```
java -noverify -jar coursier launch --fork almond:0.10.6 --scala 2.12.8 -- --install
```

#### Install the chisel-bootcamp repo.
Download the [chisel-bootcamp](https://github.com/freechipsproject/chisel-bootcamp) as a zip file (or use a Windows git client)
and unpack it in a directory you have access to.
Ideally, you should put it in a path that has no spaces.

Install the customization script by moving `chisel-bootcamp/source/custom.js` to 
`%HOMEDRIVE%%HOMEPATH%\.jupyter\custom\custom.js`.
If you already have a custom.js file, append this script to it.

#### Launch the Jupyter and the bootcamp
In the directory containing the unpacked chisel-bootcamp repo, from a new command window type:
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

##### Proxy usage
If you require a proxy, try uncommenting and changing the relevant lines at the start of `source/load-ivy.sc`.

Good Luck!

### Cadence AWS Setup

If you don't know what is Cadence AWS, or don't have access to Cadence AWS, skip this section.

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
Launch Firefox in the background and set it as your default browser when it asks.
```
/craft/cdns_sw_inst/firefox/45.3.0esr/firefox &
```

Clone the repo and launch Jupyter.
If it asks for a token, copy and paste the *to login with a token* URL seen in the terminal.
Future launches will be happy for a while.
```
git clone /craft/tools/chisel/generator-bootcamp.git
cd generator-bootcamp
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

