# How to verify

## Via Verus Command Line

```
verus INSPECTA-models/isolette/rust-prototyping/mri/src/main.rs
```

## Via Docker Image

Clone the INSPECTA-models repo, cd to its root directory, and then run the following

```
docker run -it --rm -v $(pwd):/home/microkit/provers/INSPECTA-models jasonbelt/microkit_domain_scheduling bash -ci \
    "verus provers/INSPECTA-models/isolette/rust-prototyping/mri/src/main.rs"
```