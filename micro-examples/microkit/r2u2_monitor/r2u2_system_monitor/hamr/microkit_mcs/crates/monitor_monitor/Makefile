# This file will not be overwritten if HAMR codegen is rerun

microkit_sdk_config_dir := $(MICROKIT_SDK)/board/$(MICROKIT_BOARD)/$(MICROKIT_CONFIG)

sel4_include_dirs := $(firstword $(wildcard $(microkit_sdk_config_dir)/include \
                                            $(microkit_sdk_config_dir)/debug/include))

# BEGIN MARKER FOR R2U2_BUILD_DEPS RULE
.DEFAULT_GOAL := all
R2U2_BUILD_DEPS := src/component/spec.bin

r2u2_cli:
	@echo "Checking/Updating r2u2_cli from crates.io..."
	cargo +stable install r2u2_cli --version 4.2.4

$(R2U2_BUILD_DEPS): r2u2_cli
	mkdir -p .cargo && \
	cd src/component && \
	sed '/^--/d' spec.map > temp.map && \
	r2u2_cli compile -o . -b ../../.cargo/config.toml spec.c2po temp.map && \
	rm temp.map
# END MARKER FOR R2U2_BUILD_DEPS RULE

# The toolchain is pinned to a stable release channel (see rust-toolchain.toml),
# which rejects the #![feature(..)] attributes the generated crates declare, so
# every cargo invocation -- building, verifying and testing alike -- needs this.
ENV_VARS = RUSTC_BOOTSTRAP=1

BUILD_ENV_VARS = $(ENV_VARS) \
                 SEL4_INCLUDE_DIRS=$(abspath $(sel4_include_dirs))

CARGO_FLAGS = -Z build-std=core,alloc,compiler_builtins \
              -Z build-std-features=compiler-builtins-mem \
              --target aarch64-unknown-none

# Extra options for cargo-verus itself, for every cargo-verus target below.
# Empty by default. For example
#   CARGO_VERUS_OPTS="--fwd-verus-args-to roots"
# forwards SMT_OPTS to this crate only, so its dependencies (notably vstd) are
# verified with Verus' defaults rather than with options meant for this crate.
CARGO_VERUS_OPTS ?=

# Extra arguments forwarded to Verus (placed after `--`), for every
# cargo-verus target below. These apply to dependency crates too -- notably
# vstd, which is verified from source on a clean build, so its proofs must
# discharge under these settings as well as our own.
#
# --rlimit 100
#   Verus' SMT resource limit for a function body check. Its default of 10
#   is not enough here. 100 is an order-of-magnitude bump that stays inside
#   the range Verus itself uses on its heavier projects.
#
# --smt-option smt.random_seed=7
#   7 is arbitrary. Z3's default seed is 0, and 0 is the one value at which
#   vstd's GhostSubseq::agree_map (resource/impls/seq.rs) fails its
#   postcondition; seeds 1, 2, 3, 7 and 42 all discharge it. The point is to
#   be off the default and identical on every machine, not this number.
SMT_OPTS ?= --rlimit 100 --smt-option smt.random_seed=7

all: build-verus-release

# NOTE: cargo-verus requires Verus-relevant cargo options (e.g. --features,
#       --release, --package, --manifest-path) to precede Verus-irrelevant
#       ones (e.g. --target, -Z ...), otherwise it errors out.  CARGO_FLAGS
#       holds the Verus-irrelevant options, so it must come last.

build-verus-release: $(R2U2_BUILD_DEPS)
	$(BUILD_ENV_VARS) cargo-verus build $(CARGO_VERUS_OPTS) --features sel4 --release $(CARGO_FLAGS) -- $(SMT_OPTS)

build-verus: $(R2U2_BUILD_DEPS)
	$(BUILD_ENV_VARS) cargo-verus build $(CARGO_VERUS_OPTS) --features sel4 $(CARGO_FLAGS) -- $(SMT_OPTS)

build-release: $(R2U2_BUILD_DEPS)
	$(BUILD_ENV_VARS) cargo build --features sel4 $(CARGO_FLAGS) --release

build: $(R2U2_BUILD_DEPS)
	$(BUILD_ENV_VARS) cargo build --features sel4 $(CARGO_FLAGS)

verus: $(R2U2_BUILD_DEPS)
	$(ENV_VARS) cargo-verus verify $(CARGO_VERUS_OPTS) $(CARGO_FLAGS) -- $(SMT_OPTS)

verus-json: $(R2U2_BUILD_DEPS)
	$(ENV_VARS) cargo-verus verify $(CARGO_VERUS_OPTS) $(CARGO_FLAGS) -- $(SMT_OPTS) --output-json --time > verus_results.json

# Test Example:
#   Run all unit tests
#   Usage: make test
#
#   Run only unit tests whose name contains 'proptest'
#   Usage: make test args=proptest

test-release: $(R2U2_BUILD_DEPS)
	$(ENV_VARS) cargo test $(args) --release

test: $(R2U2_BUILD_DEPS)
	$(ENV_VARS) cargo test $(args)

# Coverage Example:
#   Run the unit tests with coverage instrumentation and build the HTML report
#   Usage: make coverage
#
#   Only unit tests whose name contains 'proptest'
#   Usage: make coverage args=proptest
#
#   Rebuild the report from the last run's data, without re-running the tests
#   Usage: make coverage-report
#
# The report is target/coverage/report/index.html (lines, regions, functions and
# branches, measured by llvm-cov), with target/coverage/lcov.info beside it.  It is
# written by bin/coverage-report.cmd, which needs SIREUM_HOME and the toolchain's
# llvm-tools-preview component, and opens in the browser the first time.

COVERAGE_REPORT = ../../bin/coverage-report.cmd

coverage: $(R2U2_BUILD_DEPS)
	@open=; if [ ! -f target/coverage/report/index.html ]; then open=open; fi; \
	$(COVERAGE_REPORT) . $$open $(args)

coverage-report:
	$(COVERAGE_REPORT) . no-run

clean:
	cargo clean
	rm -f src/component/spec.bin