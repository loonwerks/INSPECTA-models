# data_1_prod_2_cons::top.impl

## AADL Architecture
![arch.svg](../../aadl/diagrams/arch.svg)
|System: [data_1_prod_2_cons::top.impl]()|
|:--|

|Thread: data_1_prod_2_cons::producer_t.p |
|:--|
|Type: [producer_t](../../aadl/data_1_prod_2_cons.aadl#L33-L37)<br>Implementation: [producer_t.p](../../aadl/data_1_prod_2_cons.aadl#L38-L44)|
|Periodic : 100 ms|

|Thread: data_1_prod_2_cons::consumer_t.p |
|:--|
|Type: [consumer_t](../../aadl/data_1_prod_2_cons.aadl#L62-L68)<br>Implementation: [consumer_t.p](../../aadl/data_1_prod_2_cons.aadl#L77-L82)|
|Periodic : 100 ms|

|Thread: data_1_prod_2_cons::consumer_t.s |
|:--|
|Type: [consumer_t](../../aadl/data_1_prod_2_cons.aadl#L62-L68)<br>Implementation: [consumer_t.s](../../aadl/data_1_prod_2_cons.aadl#L95-L99)|
|Sporadic : 100 ms|


## Rust Code

[Microkit System Description](microkit.system)

### Behavior Code
#### producer: data_1_prod_2_cons::producer_t.p

 - **Entry Points**



#### consumer: data_1_prod_2_cons::consumer_t.p

 - **Entry Points**



#### consumer: data_1_prod_2_cons::consumer_t.s

 - **Entry Points**


