FIREWALL_PKGS:=rx tx core
FIREWALL_COV_DIRS=$(patsubst %, %/coverage, $(FIREWALL_PKGS))

.PHONY: test
test:
	mkdir -p coverage 
	CARGO_INCREMENTAL=0 RUSTFLAGS="-Cinstrument-coverage" LLVM_PROFILE_FILE="coverage/cargo-test-%p-%m.profraw" cargo test --workspace
	 grcov . --binary-path ./target/debug/deps/ -s . -t html --branch --ignore-not-existing --ignore "../*" --ignore "/*" --ignore "**/chrono-tz*" --ignore "tests/**" -o coverage/html

.PHONY: clean
clean:
	rm -rf $(FIREWALL_COV_DIRS)
	cargo clean
