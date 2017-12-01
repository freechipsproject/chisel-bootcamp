# Generator bootcamp files

Jupyter notebooks and supporting files for the UC Berkeley generator bootcamp (2017).

#### Written by Stevo Bailey, Adam Izraelivitz, Richard Lin, Chick Markley, Paul Rigge, Edward Wang
[stevo@berkeley.edu](mailto:stevo@berkeley.edu),
[azidar@berkeley.edu](mailto:azidar@berkeley.edu)
[richard.lin@berkeley.edu](mailto:edwardw@berkeley.edu)
[chick@berkeley.edu](mailto:chick@berkeley.edu),
[rigge@berkeley.edu](mailto:rigge@berkeley.edu)
[edwardw@berkeley.edu](mailto:edwardw@berkeley.edu)

## Jupyter Scala Windows Install
Be sure java is installed (ideally java 8). If you type `java` into a command prompt and it says command not found, you need to install java.

Download Coursier: https://git.io/vgvpD

Go to download folder, where "coursier" (file) is
```
java -noverify -jar coursier launch   -r sonatype:releases -r sonatype:snapshots   -i ammonite   -I ammonite:org.jupyter-scala:ammonite-runtime_2.11.11:0.8.3-1   -I ammonite:org.jupyter-scala:scala-api_2.11.11:0.4.2   org.jupyter-scala:scala-cli_2.11.11:0.4.2 -- --id scala --name "Scala"
```

## Jupyter Scala Mac/Linux Install
Dependencies: openssh-client, openjdk-8-jre, openjdk-8-jdk (-headless OK for both),  ca-certificates-java

Some examples in advanced chisel require verilator to be installed and [dsptools](https://github.com/ucb-bar/dsptools) to be `publish-local`'d

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

## Chamber Install

Navigate to your work directory, likely `/projects/craft_flow/work/<username>/`. Then run the following commands. Note that `/proj/` is an alias to `/projects/`. If you are in bash, source `jupyter_sh` instead of `jupyter_csh`.

```
source /proj/craft_flow/tools/jupyter/jupyter_csh
git clone /proj/craft_flow/source/chisel/generator-bootcamp
cd generator-bootcamp
jupyter notebook
```

## Links
[Bootcamp Agenda](https://tinyurl.com/bootcampchiselagenda)

[Question Submission Form](https://tinyurl.com/bootcampchisel)

[Question Form Responses](https://docs.google.com/spreadsheets/d/1IjALLZFOoXxJbY1wcs5Zwpza8ksK0IHgdknq9hAr2lU/edit?usp=sharing)

[Discussion document](https://docs.google.com/document/d/10CahcycM6Uk7cqgGHRcegrNueqCWZKNDxZdc1S36ZcE/view)

[Feedback](https://docs.google.com/a/berkeley.edu/document/d/1mTWe8XoSYpLcVr5Rp9B4MWlZidrUQr-L35oxUPHHhag/edit?usp=sharing)
