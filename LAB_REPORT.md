# Lab 1 Report — Java Inheritance and JUnit 4

**Name:** Alexander Ryan
**Course:** CSE 2102, Software Engineering — Prof. Bradford

## 1. Objective

The objective of this lab was to understand Java inheritance and to use JUnit 4
to write basic unit tests. The lab also introduced the mechanics of managing a
classpath by hand — compiling against external JAR files and invoking a test
runner directly — rather than relying on a build tool to handle dependencies.

The specific problem was to model fuel efficiency for a hybrid vehicle, which
must report efficiency in two different units: MPG for gasoline, and MPGe for
electric, where one gallon of gasoline is defined as equivalent to 33.7 kWh.

## 2. Design and Architecture

The solution separates the two efficiency models into two interfaces:

- `GasolineInterface` declares `calcGasMPG()`, setters for miles and gallons,
  and a setter/getter pair for cost per gallon.
- `ElectricInterface` declares `calcMPGe()`, setters for electric miles and
  total kWh, and a setter/getter pair for cost per kWh.

`HybridVehicle` implements both interfaces. Because a hybrid draws on both
power sources, it holds six independent fields — gas miles, gallons, and cost
per gallon on the gasoline side; electric miles, kWh, and cost per kWh on the
electric side. Keeping the two sides separate means a single object can report
both figures without one overwriting the other.

`CarRunner` holds `main`. It instantiates one `HybridVehicle`, loads it with
the sample values from the lab writeup, and prints the three required results.

The interface split is what makes this arrangement worth doing. Each interface
is a contract describing a capability rather than a type of vehicle, so a
gasoline-only or electric-only class could implement just one of them, while
`HybridVehicle` implements both. The compiler enforces the contract: leaving
out any of the ten declared methods is a compile error rather than a runtime
surprise.

## 3. Implementation Details

The conversion factor 33.7 is stored as a `private static final double`
constant rather than appearing inline, so the value is named and defined in one
place.

Neither MPG nor MPGe is stored as a field. Both are computed on demand from the
stored values and returned. Caching them would risk returning a stale figure
after a setter changed one of the inputs.

Every setter uses `this.` to disambiguate the field from the parameter, since
the two share a name. Without it, `costPerkWh = costPerkWh` assigns the
parameter to itself and silently does nothing — the code compiles and runs, but
the field is never written.

Output is formatted with `printf` and `%.2f`. The raw MPGe value is
144.42857142857144, which is unreadable in a console demo.

> c/c and u/u were intentionally kept nested. This was because od the IntelliJ IDE recognizing u/ as a source directory, and u/u as a package. The same is true for c/ and c/c. 

## 4. Test Strategy and Edge Cases

The tests target `HybridVehicle` rather than `CarRunner`. `CarRunner` is a
`main` method that prints to stdout and returns nothing, so there is no return
value to assert against; testing it would mean capturing `System.out`, which
tests output formatting rather than logic. The calculations all live in
`HybridVehicle`, so that is where the assertions belong.

A `@Before` method constructs a fresh `HybridVehicle` before each test, so no
test can be affected by state left behind by another.

The suite covers four categories:

**Known-good calculations.** Three tests feed in the values published in the
lab writeup and assert the published answers: 120 miles on 6 gallons gives 20
MPG, and 300 miles on 70 kWh gives 144.43 MPGe. These act as a regression net —
if a later refactor breaks the arithmetic, these fail immediately.

**Setter round-trips.** Each cost value is set and then read back. These look
trivial, but they are the only tests that would catch the self-assignment bug
described in section 3.

**State overwriting.** One test calls a setter twice with different values and
confirms the calculation reflects the second. This verifies the object holds
mutable state correctly rather than latching the first value written.

**Edge cases.** Two tests cover division by zero, which behaves differently for
doubles than for integers. Dividing a nonzero double by zero yields `Infinity`
rather than throwing `ArithmeticException`, and 0/0 yields `NaN`. A vehicle
with no values set therefore returns `NaN` from both calculations rather than
failing loudly. The tests assert this using `Double.isInfinite()` and
`Double.isNaN()`.

All assertions on doubles use the three-argument `assertEquals(expected,
actual, delta)` with a delta of 0.01. Exact equality is unsafe for computed
floating-point values: the true MPGe is 144.42857…, which is not equal to
144.43 under `==`. The delta is set to match the two-decimal precision the
program actually reports.

## 5. Conclusion and Analysis

The implementation meets the requirements and produces the figures given in the
writeup. Separating the two efficiency models into independent interfaces kept
`HybridVehicle` straightforward to write and made the unit tests easy to scope,
since each calculation depends on only two fields.

The clearest weakness is the absence of input validation. Because the class
silently returns `NaN` or `Infinity` instead of rejecting bad input, a caller
that forgets a setter gets a nonsense number rather than an error. Whether that
is acceptable depends on the caller: it is a reasonable default for a small
program where the values come from code, but a class handed unvalidated user
input should probably throw `IllegalArgumentException` on a non-positive
divisor.

A second observation is that the hybrid average is computed in `CarRunner`
rather than in `HybridVehicle`, which means the formula is duplicated between
the runner and the test. Moving a `calcHybridAverage()` method into
`HybridVehicle` would put the logic with the data it operates on and remove the
duplication.

## AI Disclosure

**AI system used:** Claude (Anthropic)

**How it was used:** I used Claude to help set up my IDE, IntelliJ, with the JAR files necessary for this lab. I also had it help explain to me how the interface implementations work. I also had it review some code, explain necessary parts of the HybridVehicleTests.

**AI-generated code:** `u/u/HybridVehicleTests.java` Claude wrote the `unsetVehicleGivesNaNTest` method for the test class. All other code was written by me. The Lab Report and Readme were also heavily revised by Claude.

**Prompts:** Located in AI_PROMPT_LOG.md

# Technology not covered in class

The `-d` flag on javac, `Double.isNaN`, `printf` format specifiers