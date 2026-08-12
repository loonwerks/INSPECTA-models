#!/bin/bash
# Build the PROVERS VM from scratch (macOS/Linux hosts).
# Any existing 'provers-env' VM in this directory is destroyed first.
#
# Set the shell options here rather than on the shebang line: the documented
# invocation is 'bash setup.sh', which ignores shebang flags, so '#!/bin/bash -e'
# had no effect.  Without it a failing provisioner is masked by the trailing
# 'vagrant reload' -- the script reports the reload's status and exits 0 as if
# the build had succeeded.
set -ex

vagrant destroy -f
export FIRST_RUN='true'
vagrant up --no-provision

# This first apt run happens before the file provisioners have copied bin/ into
# the VM, so push the mirror override over SSH.  'vagrant upload' is used rather
# than /vagrant/bin so this does not depend on the shared folder being mounted.
if [ -n "${PROVERS_APT_MIRROR:-}" ]; then
  vagrant upload ../bin/apt-mirror.sh /tmp/apt-mirror.sh
  vagrant ssh -c "PROVERS_APT_MIRROR='${PROVERS_APT_MIRROR}' PROVERS_APT_SECURITY_MIRROR='${PROVERS_APT_SECURITY_MIRROR:-}' bash /tmp/apt-mirror.sh"
fi

vagrant ssh -c 'sudo apt-get update'
vagrant ssh -c 'sudo DEBIAN_FRONTEND=noninteractive apt-get upgrade -y'
vagrant ssh -c 'sudo DEBIAN_FRONTEND=noninteractive apt-get install -y build-essential linux-headers-generic'
vagrant halt
export FIRST_RUN='false'
vagrant up --provision
vagrant reload
