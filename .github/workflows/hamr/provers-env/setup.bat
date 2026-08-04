@echo off
REM Build the PROVERS VM from scratch (Windows hosts).
REM Any existing 'provers-env' VM in this directory is destroyed first.
vagrant destroy -f
set FIRST_RUN=true
vagrant up --no-provision || exit /b
REM Push the mirror override before the first apt run, which happens before the
REM file provisioners have copied bin/ into the VM.
if defined PROVERS_APT_MIRROR (
  vagrant upload bin/apt-mirror.sh /tmp/apt-mirror.sh || exit /b
  vagrant ssh -c "PROVERS_APT_MIRROR='%PROVERS_APT_MIRROR%' PROVERS_APT_SECURITY_MIRROR='%PROVERS_APT_SECURITY_MIRROR%' bash /tmp/apt-mirror.sh" || exit /b
)
vagrant ssh -c "sudo apt-get update"
vagrant ssh -c "sudo DEBIAN_FRONTEND=noninteractive apt-get upgrade -y"
vagrant ssh -c "sudo DEBIAN_FRONTEND=noninteractive apt-get install -y build-essential linux-headers-generic"
vagrant halt
set FIRST_RUN=false
vagrant up --provision || exit /b
vagrant reload
