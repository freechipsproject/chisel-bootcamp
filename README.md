# Generator bootcamp files

Put jupyter files here.

[Discussion document](https://docs.google.com/document/d/10CahcycM6Uk7cqgGHRcegrNueqCWZKNDxZdc1S36ZcE/view)

## Jupyter Scala Windows Install
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
