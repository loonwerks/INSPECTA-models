FROM --platform=linux/amd64 ubuntu:24.04

ENV USER=microkit

RUN apt-get update && \
    apt install -y software-properties-common && \
    add-apt-repository ppa:deadsnakes/ppa && \
    apt install -y \
      curl wget emacs git sudo \
      gcc-riscv64-unknown-elf \
      cmake pandoc device-tree-compiler ninja-build \
      texlive-latex-base texlive-latex-recommended \
      texlive-fonts-recommended texlive-fonts-extra \
      libxml2-utils \
      python3.9 python3-pip python3.9-venv \
      qemu-system-arm qemu-system-misc \
      clang lld && \
    useradd -m -s /bin/bash -N ${USER} && \
    usermod -s /bin/bash ${USER} && \
    echo "${USER} ALL=(ALL) NOPASSWD: ALL" > /etc/sudoers && \
    chmod 0440 /etc/sudoers && \
    chmod g+w /etc/passwd

USER ${USER}
ENV HOME=/home/microkit
WORKDIR ${HOME}

SHELL ["/bin/bash", "-c"]

RUN wget -O rustup-init.sh https://sh.rustup.rs && \
    sh ${HOME}/rustup-init.sh -y && \
    . ${HOME}/.cargo/env && \
    rustup target add x86_64-unknown-linux-musl && \
    rustup target add aarch64-unknown-linux-musl && \
    wget -O aarch64-toolchain.tar.gz https://sel4-toolchains.s3.us-east-2.amazonaws.com/arm-gnu-toolchain-12.2.rel1-x86_64-aarch64-none-elf.tar.xz%3Frev%3D28d5199f6db34e5980aae1062e5a6703%26hash%3DF6F5604BC1A2BBAAEAC4F6E98D8DC35B && \
    tar xf aarch64-toolchain.tar.gz && \
    echo "export PATH=${HOME}/arm-gnu-toolchain-12.2.rel1-x86_64-aarch64-none-elf/bin:${PATH}:." >> ${HOME}/.bashrc && \
    export PATH=${HOME}/arm-gnu-toolchain-12.2.rel1-x86_64-aarch64-none-elf/bin:${PATH}:. && \
    git clone --branch microkit https://github.com/seL4/seL4.git && \
    cd seL4 && git fetch origin pull/1308/head:jarcher && git pull && git checkout jarcher && \
    cd .. && \
    git clone https://github.com/seL4/microkit.git && \
    cd microkit/ && git fetch origin pull/175/head:jarcher && git pull && git checkout jarcher && \
    export SDK_VERSION=$(./ci/dev_version.sh) && \
    python3.9 -m venv pyenv && \
    ./pyenv/bin/pip install --upgrade pip setuptools wheel && \
    ./pyenv/bin/pip install -r requirements.txt && \
    ./pyenv/bin/python build_sdk.py --experimental-domain-support --sel4=$HOME/seL4 --version $SDK_VERSION && \
    microkit=$(find ~/microkit/release/ -type d -name microkit-sdk*) && \
    echo "export MICROKIT_SDK=${microkit}" >> ${HOME}/.bashrc && \
    echo "export MICROKIT_BOARD=qemu_virt_aarch64" >> ${HOME}/.bashrc && \
    echo "alias dir='ls -laF'"  >> ${HOME}/.bash_aliases
