# Copyright 2024, UNSW
#
# SPDX-License-Identifier: BSD-2-Clause

let
    rust_overlay = import (builtins.fetchTarball "https://github.com/oxalica/rust-overlay/archive/master.tar.gz");
    pkgs = import <nixpkgs> { overlays = [ rust_overlay ]; };
    rust = pkgs.rust-bin.fromRustupToolchainFile ./examples/rust/rust-toolchain.toml;
    llvm = pkgs.llvmPackages_16;
in
  pkgs.mkShell {
    buildInputs = with pkgs.buildPackages; [
        zig_0_13
        rust
        qemu
        gnumake
        dtc
        llvm.clang
        llvm.lld
        llvm.libllvm
        llvm.libclang
        dosfstools
        util-linux
        # expect is only needed for CI testing but we include it for
        # completeness
        expect
    ];
    hardeningDisable = [ "all" ];
    # Need to specify this when using Rust with bindgen
    LIBCLANG_PATH = "${llvm.libclang.lib}/lib";
}

