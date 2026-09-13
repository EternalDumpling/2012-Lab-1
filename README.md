# CSE 2102 — Lab 1: Java Inheritance and JUnit 4

A hybrid vehicle fuel-efficiency calculator demonstrating Java interfaces and
unit testing with JUnit 4.

## Contents

```
.
├── c/c/                        source code (package c)
│   ├── GasolineInterface.java
│   ├── ElectricInterface.java
│   ├── HybridVehicle.java      implements both interfaces
│   ├── CarRunner.java          main class
│   └── TestMe.java             provided demo class
├── u/u/                        unit tests (package u)
│   ├── HybridVehicleTests.java
│   └── TestMeTests.java        provided demo tests
├── junit-4.13.2.jar
└── hamcrest-core-1.3.jar
```

## Requirements

- JDK 8 or later (developed against OpenJDK 24)
- The two JAR files above, included in this directory

## Build

From the project root:

```
javac -cp junit-4.13.2.jar -d out c/c/*.java u/u/*.java
```

The `-d out` flag writes compiled classes into `out/` using the correct package
layout (`out/c/` and `out/u/`), which makes `out` a single classpath root for
both packages.

## Run

```
java -cp out c.CarRunner
```

Expected output:

```
Fully gas mode: 20.00 MPG
Fully electric mode: 144.43 MPGe
half-gas/half-electric: 82.21 Average MPG
```

## Test

Run the HybridVehicle test suite:

```
java -cp "junit-4.13.2.jar:hamcrest-core-1.3.jar:out" org.junit.runner.JUnitCore u.HybridVehicleTests
```

All eight tests should pass.

Run both suites together:

```
java -cp "junit-4.13.2.jar:hamcrest-core-1.3.jar:out" org.junit.runner.JUnitCore u.HybridVehicleTests u.TestMeTests
```

Note: `TestMeTests.squareTestFails` is designed to fail. It is part of the
provided demo material and demonstrates what a failing assertion looks like in
the JUnit runner. One failure out of ten tests is the expected result here.

## Notes on the classpath

Classpath entries are separated by `:` on macOS and Linux, and by `;` on
Windows. The JUnit annotations (`@Test`, `@Before`) and the `JUnitCore` runner
live in `junit-4.13.2.jar`; `hamcrest-core-1.3.jar` supplies the matchers that
JUnit's assertions depend on. Both are required on the classpath at test
runtime, and omitting the compiled-output entry causes the runner to fail with
a `ClassNotFoundException` for the test class itself.