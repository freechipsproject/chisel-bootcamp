# First stage : setup the system and environment
FROM ubuntu:20.04 as base

RUN \
    apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y \
        ca-certificates-java \
        curl \
        graphviz \
        openjdk-8-jre-headless \
        python3-distutils \
        gcc \
        python3-dev \
        && \
    rm -rf /var/lib/apt/lists/*

RUN curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py
RUN python3 get-pip.py
RUN pip3 install notebook

RUN useradd -ms /bin/bash bootcamp

ENV SCALA_VERSION=2.12.10
ENV ALMOND_VERSION=0.9.1

ENV COURSIER_CACHE=/coursier_cache

ADD . /chisel-bootcamp/
WORKDIR /chisel-bootcamp

ENV JUPYTER_CONFIG_DIR=/jupyter/config
ENV JUPITER_DATA_DIR=/jupyter/data

RUN mkdir -p $JUPYTER_CONFIG_DIR/custom
RUN cp source/custom.js $JUPYTER_CONFIG_DIR/custom/

# Second stage - download Scala requirements and the Scala kernel
FROM base as intermediate-builder

RUN mkdir /coursier_cache

RUN \
    curl -L -o coursier https://git.io/coursier-cli && \
    chmod +x coursier && \
    ./coursier \
        bootstrap \
        -r jitpack \
        sh.almond:scala-kernel_$SCALA_VERSION:$ALMOND_VERSION \
        --sources \
        --default=true \
        -o almond && \
    ./almond --install --global && \
    \rm -rf almond couriser /root/.cache/coursier 

# Execute a notebook to ensure Chisel is downloaded into the image for offline work
RUN jupyter nbconvert --to notebook --output=/tmp/0_demo --execute 0_demo.ipynb

# Last stage
FROM base as final

# copy the Scala requirements and kernel into the image 
COPY --from=intermediate-builder /coursier_cache/ /coursier_cache/
COPY --from=intermediate-builder /usr/local/share/jupyter/kernels/scala/ /usr/local/share/jupyter/kernels/scala/

RUN chown -R bootcamp:bootcamp /chisel-bootcamp

USER bootcamp
WORKDIR /chisel-bootcamp

EXPOSE 8888
CMD jupyter notebook --no-browser --ip 0.0.0.0 --port 8888
