# Switch 'Docker Virtual Machine Options' to 'Docker VMM' on Apple Silicon if you get build errors on Apple Silicon machines.
# However switch to 'Apple Virtualization framework' + 'Use Rosetta for x86 ...' when running as Docker VMM appears to be 
# much slower even though Docker claims Docker VMM is the most performant.
#
# docker build --no-cache -t jasonbelt/microkit_domain_scheduling:<date> .
# docker login
# docker push jasonbelt/microkit_domain_scheduling:<date>
# docker tag jasonbelt/microkit_domain_scheduling:<date> jasonbelt/microkit_domain_scheduling:latest
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

RUN sudo apt-get update && sudo apt install -y software-properties-common && \
    sudo add-apt-repository ppa:deadsnakes/ppa && \
    sudo apt install -y \
      curl wget git unzip \
      clang lld && \           
    sudo rm -rf /var/lib/apt/lists/* && \
    export PROVERS_DIR=${HOME}/provers && \
    echo "export PROVERS_DIR=\${HOME}/provers" >> ${HOME}/.bashrc && \
    mkdir -p ${PROVERS_DIR} && \
    echo "*********************************************************************" && \
    echo "* Installing Rust - Verus 0.2025.09.07.6129810 requires 1.88.0      *" && \
    echo "*********************************************************************" && \
      cd ${HOME} && \
      VER=1.88.0-x86_64-unknown-linux-gnu && wget -O rustup-init.sh https://sh.rustup.rs && sh ${HOME}/rustup-init.sh --default-toolchain=$VER -y && rm ${HOME}/rustup-init.sh && . ${HOME}/.cargo/env && \
      rustup target add x86_64-unknown-linux-musl && rustup target add aarch64-unknown-linux-musl && \
      rustup component add rust-src --toolchain $VER && \
    echo "************************************************" && \
    echo "* Installing Verus 0.2025.09.07.6129810        *" && \
    echo "************************************************" && \
      cd ${PROVERS_DIR} && \
      VER=release/0.2025.09.07.6129810 && wget -O verus.zip "https://github.com/verus-lang/verus/releases/download/$VER/verus-$(echo "$VER" | sed 's|.*/||')-x86-linux.zip" && \
      unzip ${PROVERS_DIR}/verus.zip && rm ${PROVERS_DIR}/verus.zip && echo "export PATH=\${PROVERS_DIR}/verus-x86-linux:\${PATH}" >> ${HOME}/.bashrc && \
    echo "************************************************" && \
    echo "* Building Attestation tools                   *" && \
    echo "************************************************" && \
      cd ${HOME} && \
      sudo apt-get update && sudo apt install -y \
        libssl-dev build-essential libgmp-dev libzmq3-dev pkg-config opam && \
      opam init --bare --disable-sandboxing -y && \
      opam switch create . ocaml-system && \
      eval $(opam env) && \
      opam install -y dune coq logs fmt conf-zmq && \
      export AM_REPOS_ROOT=${PROVERS_DIR}/am && mkdir -p ${AM_REPOS_ROOT}/cvm_deps && cd ${AM_REPOS_ROOT}/cvm_deps && \
      echo "export AM_REPOS_ROOT=\${PROVERS_DIR}/am" >> ${HOME}/.bashrc && \
      git clone https://github.com/ku-sldg/rocq-candy.git && cd rocq-candy/ && dune build && dune install && cd .. && \
      git clone https://github.com/ku-sldg/rocq-json.git && cd rocq-json/ && dune build && dune install && cd .. && \
      git clone https://github.com/ku-sldg/copland-spec.git && cd copland-spec/ && dune build && dune install && cd .. && \
      git clone https://github.com/ku-sldg/rocq-cli-tools.git && cd rocq-cli-tools/ && dune build && dune install && cd .. && \
      git clone https://github.com/Durbatuluk1701/EasyBakeCakeML.git && cd EasyBakeCakeML && dune build && dune install && cd .. && \
      git clone https://github.com/Durbatuluk1701/bake.git && cd bake/ && dune build && dune install && cd .. && \
      git clone https://github.com/ku-sldg/copland-manifest-tools.git && cd copland-manifest-tools && dune build && dune install && cd .. && \
      cd $AM_REPOS_ROOT && \
      git clone https://github.com/ku-sldg/cvm.git && cd cvm && dune build && dune install && cd .. && \
      git clone https://github.com/ku-sldg/rust-am-clients.git && cd rust-am-clients && make && cd .. && \
      git clone https://github.com/ku-sldg/asp-libs && cd asp-libs && make && cd .. && \
      git clone https://github.com/ku-sldg/copland-evidence-tools.git && cd copland-evidence-tools && dune build && dune install && cd .. && \
      opam clean -a && \
      opam uninstall -y dune conf-zmq && \
      sudo apt purge --auto-remove -y \
        libssl-dev build-essential libgmp-dev pkg-config && \
      rm -rf ${AM_REPOS_ROOT}/cvm_deps && \
    echo "************************************************" && \
    echo "* Building Microkit SDK with domain scheduling *" && \
    echo "************************************************" && \
      export MICROKIT_DIR=${PROVERS_DIR}/microkit && mkdir -p ${MICROKIT_DIR} && cd ${MICROKIT_DIR} && \
      sudo apt-get update && sudo apt install -y gcc-riscv64-unknown-elf \
        cmake pandoc device-tree-compiler ninja-build \
        texlive-latex-base texlive-latex-recommended \
        texlive-fonts-recommended texlive-fonts-extra \
        libxml2-utils \
        python3.12 python3-pip python3.12-venv \
        qemu-system-arm qemu-system-misc && \
      wget -O aarch64-toolchain.tar.gz https://sel4-toolchains.s3.us-east-2.amazonaws.com/arm-gnu-toolchain-12.2.rel1-x86_64-aarch64-none-elf.tar.xz%3Frev%3D28d5199f6db34e5980aae1062e5a6703%26hash%3DF6F5604BC1A2BBAAEAC4F6E98D8DC35B && \
      tar xf aarch64-toolchain.tar.gz && rm ${MICROKIT_DIR}/aarch64-toolchain.tar.gz && \
      export PATH=${MICROKIT_DIR}/arm-gnu-toolchain-12.2.rel1-x86_64-aarch64-none-elf/bin:${PATH}:. && \
      git clone https://github.com/Ivan-Velickovic/seL4 --branch microkit_domains && \
      git clone https://github.com/JE-Archer/microkit --branch domains && cd microkit && \
      python3.12 -m venv pyenv && \
      ./pyenv/bin/pip install --upgrade pip setuptools wheel && \
      ./pyenv/bin/pip install -r requirements.txt && \
      ./pyenv/bin/python build_sdk.py --experimental-domain-support --sel4=${MICROKIT_DIR}/seL4 --configs debug && \
      microkit=$(find ${MICROKIT_DIR}/microkit/release/ -type d -name microkit-sdk*) && \
      mv ${microkit} ${PROVERS_DIR}/microkit-sdk && \
      echo "export MICROKIT_SDK=\${PROVERS_DIR}/microkit-sdk" >> ${HOME}/.bashrc && \
      echo "export MICROKIT_BOARD=qemu_virt_aarch64" >> ${HOME}/.bashrc && \
      cd ${HOME} && \
      rm -rf ${MICROKIT_DIR} && \
      sudo apt purge --auto-remove -y \
        pandoc texlive-latex-base texlive-latex-recommended \
        texlive-fonts-recommended texlive-fonts-extra libxml2-utils && \
    echo "********************************************************************************" && \
    echo "* Installing a very minimal Sireum dist (e.g. uses container's jre rather than *" && \
    echo "* downloading a JDK) so that Slash scripts can be run                          *" && \
    echo "********************************************************************************" && \
      export SIREUM_HOME=${PROVERS_DIR}/Sireum && cd ${PROVERS_DIR} && \
      echo "export SIREUM_HOME=\${PROVERS_DIR}/Sireum" >> ${HOME}/.bashrc && \
      echo "export PATH=\${SIREUM_HOME}/bin:\${PATH}" >> ${HOME}/.bashrc && \
      sudo apt-get update && sudo apt install -y \
        default-jre jq unzip && \
      mkdir -p ${SIREUM_HOME}/bin/linux/java && ln -s /usr/lib/jvm/java-21-openjdk-amd64/* ${SIREUM_HOME}/bin/linux/java/ && \
      wget https://raw.githubusercontent.com/sireum/kekinian/refs/heads/master/versions.properties -O ${SIREUM_HOME}/versions.properties && echo "$(grep "^org.sireum.version.java=" ${SIREUM_HOME}/versions.properties | cut -d'=' -f2)" > ${SIREUM_HOME}/bin/linux/java/VER && \
      wget https://raw.githubusercontent.com/sireum/kekinian/refs/heads/master/bin/init.sh -O ${PROVERS_DIR}/Sireum/bin/init.sh && chmod 700 ${SIREUM_HOME}/bin/init.sh && SIREUM_NO_SETUP=true ${SIREUM_HOME}/bin/init.sh && ${SIREUM_HOME}/bin/sireum --init && \
      rm -rf ${SIREUM_HOME}/bin/linux/cs ${SIREUM_HOME}/bin/linux/cvc* ${SIREUM_HOME}/bin/linux/z3 ${SIREUM_HOME}/lib/jacoco* ${SIREUM_HOME}/lib/marytts_text2wav.jar && rm -rf ${HOME}/Downloads/sireum && \
      sudo apt purge --auto-remove -y \
        jq unzip && \
    echo "eval $(opam env)" >> ${HOME}/.bash_aliases && \
    echo "alias env='env | sort'" >> ${HOME}/.bash_aliases && \
    echo "alias dir='ls -lFGa'" >> ${HOME}/.bash_aliases && \
    echo "alias ..='cd ..'" >> ${HOME}/.bash_aliases
