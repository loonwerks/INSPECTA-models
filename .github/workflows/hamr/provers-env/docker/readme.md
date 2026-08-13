# PROVERS Container Image

The DARPA PROVERS command-line tools, packaged as a container image for linux/amd64
and linux/arm64.

It is built from the install scripts in [../bin](../bin) -- the same ones the
Vagrant VM and the bare-metal setup run -- so the image and the VM install the
same tools at the same versions.  What differs is only which flags those scripts
are given.  For the VM, see [../vagrant/readme.md](../vagrant/readme.md); for an
overview of all three delivery paths, [../readme.md](../readme.md).

## Contents

* [Using It](#using-it)
* [What Is Installed](#what-is-installed)
* [Building](#building)
  * [Publishing Elsewhere](#publishing-elsewhere)
* [How The Build Is Put Together](#how-the-build-is-put-together)
  * [Layer Caching](#layer-caching)
* [Working In A Container](#working-in-a-container)
  * [Getting A Newer Sireum](#getting-a-newer-sireum)
  * [Behind A Proxy](#behind-a-proxy)
  * [Adding An IDE](#adding-an-ide)

## Using It

```bash
docker run -it --rm jasonbelt/microkit_provers:latest
```

Mount a model directory to work on it:

```bash
docker run -it --rm -v $(pwd):/home/microkit/provers/INSPECTA-models \
  jasonbelt/microkit_provers:latest
```

`:latest` is a multi-arch manifest; docker picks the matching architecture.
`:<YYYY.MM.DD>` pins a build, and `:amd64_<date>` / `:arm64_<date>` name the
single-architecture images behind a manifest.

The user is `microkit`, with passwordless `sudo`, and `$HOME` is
`/home/microkit`.  The environment -- `$PROVERS_DIR`, `$MICROKIT_SDK`,
`$SIREUM_HOME`, `$VERUS_DIR`, `$VERUS_Z3_PATH` and the `PATH` additions -- is
declared as `ENV`, so it is there for `docker run <image> <cmd>` and not only
for an interactive shell.

## What Is Installed

Two places answer this for a given image without guesswork:

```bash
docker inspect --format '{{json .Config.Labels}}' <image> | tr ',' '\n'   # from outside
docker run --rm <image> cat provers/build-info                            # from inside
```

The labels carry the headline versions; `provers/build-info` is the full
manifest, the same file the VM and the OVA carry.


Everything in [../bin](../bin) except the IDEs: Verus (with its Z3), both
Microkit SDKs, LionsOS, the sdfgen venv, Rust, and a Sireum install big enough
to run Slang scripts and HAMR codegen.  See
[../vagrant/readme.md](../vagrant/readme.md#what-gets-installed) for the full
table and the environment variables, which are the same here.

The scripts run with:

| | value | effect |
| --- | --- | --- |
| `PROVERS_DEPS_PROFILE` | `builder`, then `runtime` | build packages in the builder stage; only what the tools need to run in the final image |
| `PROVERS_SIREUM_PROFILE` | `full` | kekinian cloned at `SIREUM_V` and built, the same revision the VM builds; `slim.sh` then drops the checkout and `out/` |
| `PROVERS_IVE` / `_CODEIVE` / `_FMIDE` | `false` | no IDEs |
| `PROVERS_SIREUM_SOLVERS` | `false` | Logika's solvers are not installed; Sireum fetches one on first use |
| `bin/slim.sh` | run, with `PROVERS_SLIM_CACHES=true` | drops build leftovers, the Sireum checkout and `out/`, the dependency caches, any solvers, the LionsOS examples and history, and the parts of the JDK only the IDEs use |

So the image ships without GUI libraries.  That is deliberate, and can be changed
from inside a running container -- see
[Working In A Container](#working-in-a-container).

The Rust and JVM dependency caches are stripped -- `$SIREUM_HOME/lib/cache`,
which is where Sireum's coursier cache actually lives, and `~/.cargo/registry`
-- as are Logika's solvers.  This image exists mainly for CI, where size is what
costs and the runner can refetch; the ~1.2 GB they occupy between them is not
worth carrying in every pull.

If you need an environment that resolves dependencies *without* reaching
crates.io or Maven Central -- a restricted or proxied network -- the VM is the
artifact for that: it keeps both caches.  See
[../vagrant/readme.md](../vagrant/readme.md) and
[Behind A Proxy](#behind-a-proxy).

## Building

```bash
bash docker.sh
```

That builds both architectures in parallel, tags them `<image>:amd64_<date>` and
`<image>:arm64_<date>`, and then offers to push and to publish a multi-arch
`:latest` manifest.  It answers `n` by default, so it is safe to run just to
check the build.

Requires `docker buildx` with a builder that can produce both platforms --
Docker Desktop's default builder does, via QEMU emulation for the foreign one.
Note that the emulated half is *much* slower, and on aarch64 the build compiles
Z3, Verus and sdfgen from source.

Two knobs on the build itself:

| | |
| --- | --- |
| `PROVERS_BUILD_SEQUENTIAL=true` | build one architecture at a time.  Parallel is the default and halves the wall clock, but doubles the concurrent load on GitHub at the moments both builds fetch large artifacts, which is a good way to be throttled mid-build |
| `PROVERS_EXPORT_BUILD_CACHE=true` | also export the layer cache to `.buildx-cache/`.  Off by default: `mode=max` writes every layer of both architectures on every build and nothing prunes it -- 58 GB after a day of iterating, enough to fill a disk.  BuildKit's own cache already makes local rebuilds fast; the export earns its keep in CI, where nothing survives between runs |

Downloads are cached too, in BuildKit cache mounts keyed per architecture -- our
own fetches via `PROVERS_CACHE_DIR`, plus Sireum's, coursier's, cargo's and
zig's.  None of it reaches the image; it lives in BuildKit's store, which
`docker buildx du` reports and `docker buildx prune` clears.

### Publishing Elsewhere

The image name comes from [../bin/versions.sh](../bin/versions.sh), so it can be
pointed at another account or a private registry without editing anything:

```bash
PROVERS_IMAGE=myorg/microkit_provers bash docker.sh
```

Tool versions are overridable the same way, since `docker.sh` sources that file
and passes each one as `--build-arg`:

```bash
VERUS_VER=<other> bash docker.sh
```

## How The Build Is Put Together

One Dockerfile covers both architectures.  Everything that differs between them
-- building Z3 and Verus from source, building sdfgen with zig, the SDK tarball
names, the cross-toolchain host, `bin/linux` vs `bin/linux/arm` -- is decided
inside the scripts from `uname -m`, so the Dockerfile never branches on
architecture.  `docker.sh` selects one with buildx's `--platform`.

The build context is `provers-env/`, the parent of this directory, so that
`bin/` is reachable from it.

### Layer Caching

The builder stage runs one `RUN` per script rather than one big layer.  That is
deliberate: with a single layer, editing any script or bumping any version
re-runs everything, which on aarch64 means recompiling Z3, Verus and the Microkit
SDK.  Each step also `COPY`s only the scripts it needs, so touching `verus.sh`
does not invalidate the Rust layer.  `env.sh` and `versions.sh` are inputs to
every step, so a version bump correctly rebuilds everything after it.

Deleting build leftovers does not have to happen in the layer that created them:
the final stage takes a single `COPY --from=builder` of `$HOME`, so the builder's
own layers never reach the image.  `bin/slim.sh` therefore runs once, at the end
of the builder stage, rather than each script cleaning up after itself.

`bin/check-env.sh` runs in the final stage and fails the build if the declared
`ENV` disagrees with `versions.sh`.  Docker interpolates the `MICROKIT_SDK`
paths into `ENV` itself, from build args, which makes them the one place a
version can drift out of step with the shared config.

## Working In A Container

Both of the following change a running container, not the image.

### Getting A Newer Sireum

The image carries the Sireum revision `bin/versions.sh` pinned when it was
published, built from source.  To move to a newer one without waiting for a new
image, re-run the same install script inside the container:

```bash
SIREUM_V=master bash bin/sireum.sh
```

That clones kekinian afresh -- `slim.sh` removed the checkout at build time, so
there is no history to update -- builds it, and replaces `$SIREUM_HOME` in
place, leaving `$PATH` and the rest of the environment working.  `SIREUM_V`
takes a branch, tag or commit; leave it out to rebuild the pinned revision.

Two things to know:

* The image ships no solvers, so the first Logika run downloads one.  That needs
  network access, and it is the behaviour the image has always had.  Pass
  `PROVERS_SIREUM_SOLVERS=true` to `bin/sireum.sh` to install them instead, if
  you want Logika to work offline -- Sireum's installers fetch every platform's
  build, around 825MB, of which one is usable.

* The bootstrap jar comes from the pinned `SIREUM_INIT_V` release.  If master has
  moved far enough that it will not build with that, pass `SIREUM_INIT_V=dev` to
  bootstrap from the current dev release instead.

Commit the container (`docker commit`) if you want to keep the result; otherwise
it goes away with the container, as any change to a container's filesystem does.

### Behind A Proxy

The image resolves dependencies from the network in two places, and both trip
over TLS interception in a locked-down corporate environment.  The image ships
without warm caches, so this applies from the first build you run in it.  (The
VM does keep its caches, which is one reason it suits restricted sites better.)

**JVM (Sireum, coursier).**  Sireum uses its *own* bundled JDK, so adding the
corporate root CA to the system trust store does not help -- it has to go into
that JDK's keystore:

```bash
sudo $SIREUM_HOME/bin/linux/java/bin/keytool -importcert \
  -cacerts -storepass changeit -noprompt \
  -alias corp-ca -file /path/to/corp-ca.pem
```

(`bin/linux/arm/java` on aarch64.)  Proxy settings reach it through
`JAVA_OPTS="-Dhttps.proxyHost=... -Dhttps.proxyPort=..."`.

**Rust (cargo).**  Cargo has its own CA handling:

```bash
export CARGO_HTTP_CAINFO=/path/to/corp-ca.pem
export https_proxy=http://proxy:port
```

`curl` and `wget`, which the install scripts use, honour `https_proxy` and
`SSL_CERT_FILE` / `CURL_CA_BUNDLE`.

Mount the CA into the container (`-v /path/to/corp-ca.pem:/tmp/corp-ca.pem:ro`)
rather than baking it into the image.

### Adding An IDE

The image installs no IDEs, but it ships their install scripts, so one can be
added inside a running container:

```bash
PROVERS_DEPS_PROFILE=vm bash bin/deps.sh   # the GUI libraries
bash bin/ive.sh                            # or codeive.sh, fmide.sh
```

Running the result needs X forwarding into the container.  If that is what you
want, the VM is usually the better tool.
