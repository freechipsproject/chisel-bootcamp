FROM jupyter/minimal-notebook

USER root

RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    curl verilator openssh-client \
    openjdk-8-jre-headless openjdk-8-jdk-headless ca-certificates-java && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

ADD . /home/$NB_USER/chisel-bootcamp

RUN chown -R $NB_USER ~/chisel-bootcamp

USER $NB_USER

RUN curl -L -o coursier https://git.io/coursier && chmod +x coursier

ARG SCALA_VERSION=2.12.8
ARG ALMOND_VERSION=0.2.1

RUN ./coursier bootstrap -r jitpack \
    -i user -I user:sh.almond:scala-kernel-api_$SCALA_VERSION:$ALMOND_VERSION \
    sh.almond:scala-kernel_$SCALA_VERSION:$ALMOND_VERSION \
    --sources --default=true \
    -o almond

RUN ./almond --install && rm almond coursier

RUN mkdir -p ~/.jupyter/custom/ && cp ~/chisel-bootcamp/source/custom.js ~/.jupyter/custom/
