# Notifications at domain start with MCS

The objective of this design is to start a specific thread at precisely the
start of each domain time slice.

This can be achieved without kernel changes using priorities and the MCS
scheduling period setting if the domain schedule has a certain property: the
shortest time distance between two starts of the same domain must be larger than
the longest time slice of that domain.

This is trivially satisfied for instance:

- when each domain occurs only once before the schedule wraps around, or
- when a domain occurs multiple times in the schedule and its length is the same
  for each occurrence.

When a domain occurs multiple times with wildly different time slice lengths,
the requirement comes down to: there must be enough gap between occurrences of
the same domain in the domain schedule.

If only interested in how it works, read "Idea" at the beginning and "Example
with microkit" at the end. The rest describes why it works and how it can be
extended for different scenarios.

## Idea

Given a domain schedule `[(d1, l1), (d2, l2), ...]`, for domains `d1`, `d2` and
their time slice lengths `l1` and `l2`, we want to generate a notification for a
thread `t` in `d1` at every start of `d1`.

For now, assume `d1` only occurs once in the schedule before it wraps around.

The basic idea is to add a starter thread s to domain `d1` with highest
priority, with period `l1 + delta`, with `KWCET < delta < l2`, and a budget
large enough for `s` to complete.

    [      l1     |   l2  | ... ]
     ^--------------^
         l1+delta

We assume that KWCET is smaller than the smallest domain time slice, or at least
smaller than `l2`. In a microkit system, KWCET is likely to be microseconds, and
the smallest domain time slice is one millisecond.

The implementation of `s` is an endless loop that sends a notification to `t`
and then yields the rest of its budget. `t` must run at lower priority than `s`.

## Correctness

This makes use of the following scheduling properties:

1. The kernel always runs the highest-priority thread that is schedulable
2. A thread in domain `d1` is not schedulable in domains `d_i` with `i /= 1`
3. When a thread is out of budget or has yielded, it cannot be scheduled before
   its period has passed.

When `d1` first runs, `s` is the highest priority thread and it is schedulable.
So, by (1) it must run. It sends a notification and yields the rest of its
budget. We have set the budget high enough for this action to complete, and the
program counter of `s` will sit at/after "yield". By (3), it cannot run again in
the same domain time slice, because its period is larger than `l1`. By (2) it
cannot run again until `d1` runs again. By that time `s` is schedulable again,
because the period has passed, since we have set it to be `< l1 + l2`, and `d1`
cannot be scheduled before `d2` has completed. This means, `s` is again the
highest priority thread at domain start and will run. Its program counter is
after "yield", will complete the loop, send a notification, and yield again,
restoring the situation from when it ran last time.

We set delta to be large enough to take into account any jitter at the beginning
and end of the domain. Jitter is minimal in a microkit system, but we would like
to convince ourselves that larger jitter does no impact correctness. Jitter at
the beginning of `d1` (e.g. because some `d0` made a long-running kernel call)
means that `s` starts later than usual, but the domain `d1` still runs for the
same amount of time `l1`, so the scheduling argument above is not impacted.
Jitter at the end of domain `d1` (e.g. because `t` does not complete and ends
the domain in a long-running kernel call) means that `d1` runs longer than `l1`.
However, it cannot run longer than `l1 + KWCET`, so if we picked delta large
enough, the end of the period of s still falls outside `d1`, and the argument
still holds.

If we misestimated KWCET and picked delta too small, the kernel *should* still
not schedule `s`, because it will be switching to domain `d2` after the
long-running kernel call has completed. That means, the period of `s` will still
have expired before `d1` comes around again and we should be robust against
KWCET misestimation.

Note that the MCS kernel picks all timer interrupts at `t - timer_precision`,
with `timer_precision` usually configured to be 1 microsecond. This means the
actual domain time slice is usually one microsecond less than configured in the
domain schedule on MCS.

The MCS period for s cannot drift against the domain schedule, because we the
new deadline for `s` will always be computed after completion of `s` after the
start of the domain. The end will therefore always fall outside `d1`, and the
earliest schedulable point will be the start of the domain. Any drift is reset
each time the deadline is computed again.

Overall, the domain schedule might drift against global time because of jitter
at domain end, but the starter thread will remain in sync with the domain
schedule.

While we picked delta to be `< l2` above for simplicity, it is not important for
correctness that the end of the period falls within `d2`. It is only important
that it does not fall into `d1`.

## Generalisation to multiple occurrences of the same domain

The setup above assumes that `d1` occurs only once in the domain schedule until
the schedule wraps around. If it occurs multiple times with the same time slice
length `l1` and other domains run in between, nothing in the argument above
changes and it still works. If `d1` occurs multiple times with different lengths
`l_i`, the setup needs to take that into account, because each will run the same
thread `s`, and we need to pick one period that works for all of them. This is not
always possible. It *is* possible for the following situations.

First, pick the period `p` of `s` to be `max(l_i) + delta`, with `delta` = KWCET.

For this to work, we need to know that the shortest time distance between two
domain starts of `d1` is greater than this `p`.

For instance, take

    [(d1, l1a), (d2, l2), (d1,l1b), (d3, l3), (d1, l1c), (d4, l4)]

According to the above, we pick `p` to be the maximum of `{l1a, l1b, l1c}`.
Let's say `l1a` is that maximum. The shortest time durations between domain
starts for `d1` are `l1a+l2`, `l1b+l3`, and `l1c+l5`. If `l1a+delta` is smaller
than the minimum of these, the scheme works.

This implies that a schedule where `d1` is scheduled twice directly after itself
will not work, e.g. `[(d1, l1a), (d1, l1b)]` will not satisfy the condition
above. Such a schedule should not be necessary, i.e. in that case it would be
better to fuse l1a and l1b into one domain time slice anyway.

Another schedule example that will not work is one such as

    [ l1a | l2 |       l1b         | l2 ]

with `l1a + l2 < l1b`. I.e. `l1a` is short, `l2` is short, `l1b` is long. In
that case, we cannot pick a single period for `s` that is long enough to not run
twice in `l1b` and short enough to expire before `l1b` runs after `l1a`.

If such schedules are a requirement, we could either pick a different starter
thread for each domain slice length and hand over between these threads, or, if
sufficient authority is in the system, change the starter thread period after
each domain start.

## Extension to changing schedules

In general, the domain schedule is just a list and we are aiming to add a kernel
call that can update that list. When the schedule changes, the period parameter
of the starter thread `s` may have to be updated accordingly.

Depending on how the schedule changes, it may be possible to statically pick a
period that works for both domain schedules -- as long as the period of `s` is
longer than the longest domain length of `d1` in both schedules and shorter than
the earliest time `d1` runs again in both schedules.

Depending on implementation, the situation may be simpler in the restricted
setting of microkit systems. Since the kernel feature does not yet exist,
schedule changes are also not yet implemented in microkit, and it is not fully
clear yet how they will be described, so the below is based on speculation.

Since microkit system descriptions are static, it is likely that all threads
(microkit protection domains) must already exist at system start, and the only
thing that changes in a schedule change is whether they are running as part of
the new schedule or not.

If that is the case, if a domain occurs only in one of the schedules (e.g. when
switching between init and operation), nothing needs to change and we can get
away with one static period for each `s` that is computed as if there was only
one large schedule that encompasses both schedule options.

If a domain occurs in more than one schedule with different time slices, we
should also be able to run the computation as if it was one large schedule and
reduce it to what we already know, but if time slice lengths are very different
our conditions from above may not be satisfied. In that case, we could use a
different starter thread per schedule.

## Extension to domain overflow detection

With a mechanism in place for receiving a notification at domain start, we can
construct a simple cooperative mechanism for domain overrun detection:

- each worker thread in the domain (`t` in the example) will write a flag for
  "done" at the end of its work into memory that is shared with the starter
  thread s.
- the starter thread `s`, at the start of the next domain checks that all flags
  are set. If they are not, the corresponding worker has overrun its time and
  corresponding action can be taken.
- before starting sending notifications, thread `s` clears all flags again

If shared memory and flags are unwanted for some reason, the same could be
achieved by each worker thread sending a notification to the starter thread `s`
when the worker is done. Thread `s` can then (without waiting) check the state
of notifications at domain start instead of flags.

Instead of checking for an error condition from the last period at domain start,
it is sometimes possible to construct a second domain-end thread `e` that runs
a certain time `delta'` before domain end and checks flags or notifications.
There is then still `delta'` time left to act at the end of the domain.

This only works when the domain `d1` always has the same time slice length or
at least nearly the same time slice length for each occurrence in the domain
schedule, and if the time to the next start of domain `d1` is `>= l1`.

The design is the following: for domain length `l1`, we set the period of `e` to
be `l1` minus delta'. We set its priority one lower than that of the starter
thread `s`, but higher than any worker thread. We set its budget to be slightly
larger than `delta'`. The code for `e` is an endless loop with the following
body:

- wait to be woken by `s`
- immediately yield
- check for flags or notifications, if necessary take corrective action
- clear flags

After `e` is started and yields, it will be scheduled again once its period has
expired. That will be `delta'` before domain end. When it ends its loop and
waits for notification from `s`, it may still have time budget left from the
last period, but that budget will be cleared when it yields after receiving the
notification. This means, we have established the same scheduling and program
counter situation as at the start.

Both mechanisms (in `s` or in `e`) are cooperative in the sense that the worker
thread could just declare being done and then continue computation. Domain time
boundaries and start notifications will be preserved, but the worker thread(s)
inside the domain may not longer function as planned. Similarly, worker threads
could also not listen for the domain start notification. Either of these would
only impact their own behaviour, not the behaviour of other domains.

## Performance and information flow implications

### Information flow

The seL4 information flow theorem shows that the (non-MCS) kernel can enforce
the absence of logical and spatial information flow between domains that are
trying to collaborate to break that protections. It currently does not show (and
seL4 does not enforce) the absence of timing channels, but research is underway
to change that.

The setting of collaborating domains to break information flow is realistic --
all of the Spectre-like attacks are of that form (albeit for timing channels).
One domain is trying to observe, the other domain is tricked into collaborating
by the Spectre attack.

The theorem allows shared memory communication between domains and can achieve
one-way information flow via one-way notifications.

Keeping scheduling within each domain means that the information flow theorem
will be applicable once MCS verification is far enough.

A central user-level component or a central domain that sends and receives
information from every other domain does not satisfy the assumptions of the
theorem, because it represents information flow in all directions.

Even showing that such a central component is written carefully and does not
leak is not enough: contrary to the integrity theorem, the information flow
theorem has no provision for trusted user behaviour, because it is unclear how
to express that property formally. This is not an oversight, but rooted in the
fact, that "the trusted component does not leak" is not strong enough.
Information flow is a non-local property. While not leaking itself, a trusted
component may innocently cause the kernel to leak, or allow other code to leak
either directly or via the kernel, because the kernel has not enough information
or the wrong information, or because the kernel can now be tricked into
behaviour that will cause leakage elsewhere.

The scheme here keeps scheduling within each domain and does not introduce any
components that violate the assumptions of that theorem.

### Performance

The proposed scheme has better performance than using a separate pacer domain,
because the starter thread can complete its work in minimal time, e.g. within
single digit microseconds on the Odroid C4 board, whereas the minimum time slice
for a domain is 3 orders of magnitude higher (one millisecond).

While the minimum domain time slice could theoretically be reduced with a kernel
change, switching domains will remain a more expensive operation than switching
threads or address spaces within the same domain. This difference will increase
with the addition of time protection mechanisms in the future.

The default in microkit would be to use a separate address space for the starter
thread. Very little memory is needed for the start thread. Depending on trust
and safety analysis, even this may not be necessary, so memory could potentially
be further saved by using the same address space as the thread it starts. This
would also slightly increase performance, because no address space switch is
necessary. Doing this is likely not worth it.

## Example with microkit

The example uses the experimental domain support for microkit.

Note that a microkit protection domain is a separate concept from an seL4
domain. The former mostly describes its own address space and channel interface,
whereas seL4 domains would typically contain one or more microkit protection
domains.

The `domains.system` file describes 3 domains with different lengths, 3 different
worker threads each in its own protection domain, one for each seL4 domain, and
one starter thread implementation. The same starter elf file is used for 3
different protection domains, again one for each seL4 domain, but runs
separately in each.

The starter sends a notification to channel 0, which it expects to be connected
to the corresponding worker thread. If multiple threads need to be woken up, the
implementation can easily be extended to send multiple notifications.

### Build instructions

The example uses the experimental domain support for microkit from here:

<https://github.com/JE-Archer/microkit/tree/domains>

and a corresponding small seL4 patch from here:

<https://github.com/Ivan-Velickovic/seL4/tree/microkit_domains>

To build the microkit, install toolchains as explained in the [microkit
repository](https://github.com/seL4/microkit#sel4-microkit) and run the
following:

```sh
git clone https://github.com/JE-Archer/microkit --branch domains
cd microkit
git clone https://github.com/Ivan-Velickovic/seL4 --branch microkit_domains
python3 -m venv venv
./venv/bin/pip install -r requirements.txt
python3 build_sdk.py --sel4=seL4 --boards odroidc4 --configs debug --experimental-domain-support
```

To build the example, set the path to the microkit release built above
(subdirectory `release` in the repo checkout after build), create a `tmp_build`
directory in the example directory, and run `make`. This will build an image
`tmp_build/loader.img` for the Odroid C4 board.

Nothing in the example is board specific, so any board supported by microkit
should work.
