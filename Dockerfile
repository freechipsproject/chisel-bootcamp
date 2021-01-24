FROM ubuntu:20.04

RUN \
    apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y \
        ca-certificates-java \
        curl \
        graphviz \
        openjdk-8-jre-headless \
        python3-distutils \
        && \
    rm -rf /var/lib/apt/lists/*

RUN curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py
RUN python3 get-pip.py
RUN pip3 install notebook

RUN useradd -ms /bin/bash bootcamp

USER bootcamp
WORKDIR /home/bootcamp

ENV SCALA_VERSION=2.12.10
ENV ALMOND_VERSION=0.9.1

RUN \
    curl -L -o coursier https://git.io/coursier-cli && \
    chmod +x coursier && \
    ./coursier \
        bootstrap \
        -r jitpack \
        -i user \
        -I user:sh.almond:scala-kernel-api_$SCALA_VERSION:$ALMOND_VERSION \
        sh.almond:scala-kernel_$SCALA_VERSION:$ALMOND_VERSION \
        --sources \
        --default=true \
        -o almond && \
    ./almond --install && \
    rm -f coursier almond

ADD . /home/bootcamp/

RUN mkdir -p ~/.jupyter/custom
RUN cp source/custom.js ~/.jupyter/custom/custom.js

# Execute a notebook to ensure Chisel is downloaded into the image for offline work
RUN jupyter nbconvert --to html --output=/tmp/ --execute 0_demo.ipynb

EXPOSE 8888
ENTRYPOINT jupyter notebook --no-browser --ip 0.0.0.0 --port 8888
