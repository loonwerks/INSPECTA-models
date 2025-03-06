# Switch 'Docker Virtual Machine Options' to 'Docker VMM' on Apple Silicon if you get build errors on Apple Silicon machines.
# However switch to 'Apple Virtualization framework' + 'Use Rosetta for x86 ...' when running as Docker VMM appears to be 
# much slower even though Docker claims Docker VMM is the most performant.
#
# docker build -t jasonbelt/microkit_domain_scheduling:latest .
# docker login
# docker push jasonbelt/microkit_domain_scheduling:latest

FROM --platform=linux/amd64 ubuntu:24.04

ENV USER=microkit

RUN apt-get update && apt install -y sudo && \
    rm -rf /var/lib/apt/lists/* && \
    useradd -m -s /bin/bash -N ${USER} && \
    usermod -s /bin/bash ${USER} && \
    echo "${USER} ALL=(ALL) NOPASSWD: ALL" > /etc/sudoers && \
    chmod 0440 /etc/sudoers && \
    chmod g+w /etc/passwd

USER ${USER}
ENV HOME=/home/microkit
WORKDIR ${HOME}

SHELL ["/bin/bash", "-c"]

# don't use cache on next step if any of the repos have a new commit
ADD "https://api.github.com/repos/sel4/sel4/pulls/1308/commits?per_page=1" latest_sel4_commit
ADD "https://api.github.com/repos/sel4/microkit/pulls/175/commits?per_page=1" latest_microkit_commit
ADD "https://api.github.com/repos/ku-sldg/am-cakeml/commits/resolute?per_page=1" latest_am-cakeml_commit
ADD "https://api.github.com/repos/ku-sldg/asp-libs/commits/resolute?per_page=1" latest_asp-libs_commit

RUN sudo apt-get update && sudo apt install -y software-properties-common && \
    sudo add-apt-repository ppa:deadsnakes/ppa && \
    sudo apt install -y \
      curl wget emacs git \
      gcc-riscv64-unknown-elf \
      cmake pandoc device-tree-compiler ninja-build \
      texlive-latex-base texlive-latex-recommended \
      texlive-fonts-recommended texlive-fonts-extra \
      libxml2-utils \
      python3.9 python3-pip python3.9-venv \
      qemu-system-arm qemu-system-misc \
      clang lld \
      libssl-dev pkg-config netcat-openbsd \
      default-jre jq unzip && \
    sudo rm -rf /var/lib/apt/lists/* && \
    rm -rf latest_am-cakeml_commit latest_asp-libs_commit latest_sel4_commit latest_microkit_commit && \
    echo "************************************************" && \
    echo "* Installing Rust - Verus requires 1.82        *" && \
    echo "************************************************" && \
    wget -O rustup-init.sh https://sh.rustup.rs && sh ${HOME}/rustup-init.sh --default-toolchain=1.82.0-x86_64-unknown-linux-gnu -y && rm ${HOME}/rustup-init.sh && . ${HOME}/.cargo/env && \
    rustup target add x86_64-unknown-linux-musl && rustup target add aarch64-unknown-linux-musl && \
    echo "************************************************" && \
    echo "* Installing Verus                             *" && \
    echo "************************************************" && \
    VER=$(curl -s https://api.github.com/repos/verus-lang/verus/tags | jq -r '.[].name') && wget -O verus.zip "https://github.com/verus-lang/verus/releases/download/$VER/verus-$(echo "$VER" | sed 's|.*/||')-x86-linux.zip" && \
    unzip ${HOME}/verus.zip && rm ${HOME}/verus.zip && echo "export PATH=\${HOME}/verus-x86-linux:\${PATH}" >> ${HOME}/.bashrc && \
    echo "************************************************" && \
    echo "* Building Microkit SDK with domain scheduling *" && \
    echo "************************************************" && \
    wget -O aarch64-toolchain.tar.gz https://sel4-toolchains.s3.us-east-2.amazonaws.com/arm-gnu-toolchain-12.2.rel1-x86_64-aarch64-none-elf.tar.xz%3Frev%3D28d5199f6db34e5980aae1062e5a6703%26hash%3DF6F5604BC1A2BBAAEAC4F6E98D8DC35B && \
    tar xf aarch64-toolchain.tar.gz && rm ${HOME}/aarch64-toolchain.tar.gz && \
    echo "export PATH=\${HOME}/arm-gnu-toolchain-12.2.rel1-x86_64-aarch64-none-elf/bin:\${PATH}:." >> ${HOME}/.bashrc && \
    export PATH=${HOME}/arm-gnu-toolchain-12.2.rel1-x86_64-aarch64-none-elf/bin:${PATH}:. && \
    git clone https://github.com/Ivan-Velickovic/seL4 --branch microkit_domains && \
    git clone https://github.com/JE-Archer/microkit --branch domains && cd microkit && \
    python3.9 -m venv pyenv && \
    ./pyenv/bin/pip install --upgrade pip setuptools wheel && \
    ./pyenv/bin/pip install -r requirements.txt && \
    ./pyenv/bin/python build_sdk.py --experimental-domain-support --sel4=$HOME/seL4 --configs debug && \
    microkit=$(find ~/microkit/release/ -type d -name microkit-sdk*) && \
    echo "export MICROKIT_SDK=${microkit}" >> ${HOME}/.bashrc && \
    echo "export MICROKIT_BOARD=qemu_virt_aarch64" >> ${HOME}/.bashrc && \
    mkdir -p ${HOME}/provers && cd ${HOME}/provers && \
    echo "********************************************************************************" && \
    echo "* Installing a very minimal Sireum dist (e.g. uses container's jre rather than *" && \
    echo "* downloading a JDK) so that Slash scripts can be run                          *" && \
    echo "********************************************************************************" && \
    mkdir -p ${HOME}/provers/Sireum/bin/linux/java && ln -s /usr/lib/jvm/java-21-openjdk-amd64/* ${HOME}/provers/Sireum/bin/linux/java/ && \
    wget https://raw.githubusercontent.com/sireum/kekinian/refs/heads/master/versions.properties -O ${HOME}/provers/Sireum/versions.properties && echo "$(grep "^org.sireum.version.java=" provers/Sireum/versions.properties | cut -d'=' -f2)" > ${HOME}/provers/Sireum/bin/linux/java/VER && \
    wget https://raw.githubusercontent.com/sireum/kekinian/refs/heads/master/bin/init.sh -O ${HOME}/provers/Sireum/bin/init.sh && chmod 700 ${HOME}/provers/Sireum/bin/init.sh && SIREUM_NO_SETUP=true ${HOME}/provers/Sireum/bin/init.sh && ${HOME}/provers/Sireum/bin/sireum --init && \
    rm -rf ${HOME}/provers/Sireum/bin/linux/cs ${HOME}/provers/Sireum/bin/linux/cvc* ${HOME}/provers/Sireum/bin/linux/z3 ${HOME}/provers/Sireum/lib/jacoco* ${HOME}/provers/Sireum/lib/marytts_text2wav.jar && rm -rf ${HOME}/Downloads/sireum && \
    echo "export SIREUM_HOME=\${HOME}/provers/Sireum" >> ${HOME}/.bashrc && \
    echo "************************************************" && \
    echo "* Building KU tools                            *" && \
    echo "************************************************" && \
    export PKG_CONFIG_PATH=/usr/lib/x86_64-linux-gnu/pkgconfig/ && \    
    wget https://github.com/CakeML/cakeml/releases/download/v2747/cake-x64-64.tar.gz && tar xzf cake-x64-64.tar.gz && \
    cd cake-x64-64 && make cake && sudo cp cake /usr/bin && cd ${HOME}/provers && rm -rf ${HOME}/provers/cake-x64-64* &&\
    git clone -b resolute https://github.com/ku-sldg/am-cakeml.git && mkdir -p am-cakeml/build && cd am-cakeml/build && \
    cmake .. && make && cd ${HOME}/provers && \
    git clone -b resolute https://github.com/ku-sldg/asp-libs.git && cd asp-libs && make && \
    echo "export ASP_BIN=\${HOME}/provers/asp-libs/bin" >> ${HOME}/.bashrc && \
    echo "export DEMO_ROOT=\${HOME}/provers" >> ${HOME}/.bashrc && \
    echo "alias env='env | sort'" >> ${HOME}/.bash_aliases && \
    echo "alias dir='ls -lFGa'" >> ${HOME}/.bash_aliases && \
    echo "alias ..='cd ..'" >> ${HOME}/.bash_aliases && \
    sudo apt purge --auto-remove -y \
      pandoc texlive-latex-base texlive-latex-recommended \
      texlive-fonts-recommended texlive-fonts-extra libxml2-utils \
      jq unzip


