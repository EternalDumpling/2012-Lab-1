# 1.)
Supplying the lab instructions, I asked: I am using IntelliJ not codespaces. I've already curl'd the files, and did the mkdir. I don't need help with the code, I just need to know what other set up I have to do.

Claude replied: Here's what's left, IntelliJ-specific.  Add the JARs as a library. File → Project Structure → Libraries → + → Java, then select both junit-4.13.2.jar and hamcrest-core-1.3.jar. Without this IntelliJ won't resolve org.junit.* even though the files are sitting in your project folder. (The quicker path: right-click each jar in the Project pane → Add as Library.)  Set the SDK. Same dialog, Project → SDK. If it's blank or red, pick your installed JDK. Also check Project → Language level matches.  Mark c and u as source roots. Right-click each folder → Mark Directory as → Sources Root for c, Test Sources Root for u. The lab's classpath examples reference c.TestMe and u.TestMeTests, so those folder names are functioning as package names — your files will need matching package c; / package u; declarations at the top, and the test class will need to import from c.  Two run configurations. One application config with CarRunner as the main class. One JUnit config — easiest is to right-click the test class and hit Run once, which creates it automatically. IntelliJ handles the classpath for both, so you won't need the long java -cp "junit-4.13.2.jar:hamcrest-core-1.3.jar:./" invocation unless you want to demo it.  Worth doing anyway: run it once from IntelliJ's built-in terminal using the lab's exact javac and java commands. Part 1 of the lab wants you to observe what happens when you drop ./ from the classpath, and that's a question you may need to answer in the report.  For the handin. The ZIP needs README.md and LAB_REPORT.md at the top level plus a video under a minute. On macOS, Cmd+Shift+5 records the screen. Don't zip the whole IntelliJ project directory blindly — strip .idea/ and out/ first. Do keep the two JARs in the ZIP so the grader can build it without re-downloading.  Since the syllabus requires video of working deliverables for every lab, and this lab wants the AI prompts disclosed, keep a running text file of anything you use as you go — much easier than reconstructing it at the end.
# 2.)
After I wrote the ElectricInterface, I asked: "So the interface will just stay like this, correct? The code goes in the class?"

Claude replied: Correct. Interfaces stay exactly like that — a list of signatures, each ending in a semicolon, no bodies. That's the whole file.  The interface is a contract: it says what a vehicle must be able to do, not how. The how goes in HybridVehicle, which declares implements GasolineInterface, ElectricInterface and then must supply a real body for all ten methods. That's where the fields live and where the actual arithmetic happens.  So the split looks like this:
```
// ElectricInterface.java — the promise
double calcMPGe();
```
```
// HybridVehicle.java — the delivery
@Override
public double calcMPGe() {
    return (electricMiles / totalkWh) * 33.7;
}
```
If you leave any of the ten out of HybridVehicle, it won't compile — the compiler enforces the contract for you. That's the point of doing it this way, and it's worth a sentence in the design section of your lab report.  Java does allow default methods with bodies inside an interface, but that's not what this lab is asking for, so don't reach for it.

# 3.)
I said to Claude: "I finished the HybridVehicle class" and attached my code.

Claude replied:
That's clean — all ten methods implemented, this. used correctly throughout, both formulas right, no stale cached fields. Nothing broken here.  One naming nit: MPG_E reads like it means "MPGe," but the constant is actually the conversion factor, kWh per gallon. KWH_PER_GALLON says what the 33.7 is. Worth renaming since you'll likely reference it in the report.  Now, before CarRunner: your class stores costPerGallon and costPerkWh but nothing ever uses them. Right now they're write-only — set them, get them back, and that's it. The lab's whole framing is about comparing trip costs ($21 gas vs $8.09 electric), and CarRunner has to produce those numbers somewhere.  Two options:  Compute the costs in CarRunner using the getters. Keeps HybridVehicle matching the given interfaces exactly. Add cost methods to the interfaces, like calcGasTripCost() and calcElectricTripCost(), and implement them here.  The lab only mandates the set/get stubs, so the first option is safely within spec. The second is arguably better design — the object that owns the data does the math — and would give you more to write about in the architecture section, plus more to unit test. Your call; either defends fine.  Also worth deciding now, since it affects your tests: for hybrid mode, the lab says average MPG is (MPG + MPGe)/2. That's a straight average of the two efficiency figures, not a weighted one. Just implement it literally as written.

# 4.)
After I wrote the CarRunner I asked Claude to give me a format to drop the MPG down to 2 decimal points.

Claude Replied:
```
System.out.printf("Fully gas mode:      %.2f MPG%n", gasMPG);
System.out.printf("Fully electric mode: %.2f MPGe%n", mpge);
System.out.printf("Hybrid mode:         %.2f average MPG%n", hybridAvg);
```
# 5.)
I then asked Claude to explain what I need in the unit tests.

Claude replied:
Test the class, not the runner. CarRunner has no return values to assert against, so HybridVehicle is your target. Name the file to match — a test class conventionally mirrors the class under test.

Scaffolding you need first. A field holding the vehicle, plus a @Before method that assigns it a fresh instance. Every @Test then starts clean, exactly like the professor's demo. Also declare a delta constant, because assertEquals on doubles takes a third argument — the tolerance. Without it you're comparing floating point exactly, which is the trap the demo file was quietly showing you.

Category 1 — the known-good calculations. Three tests, one per required output: gas MPG, MPGe, hybrid average. Feed each the lab's published numbers and assert against the lab's published answers. These are your regression net; if you refactor HybridVehicle later and break the math, these catch it. Set only the fields each calculation actually needs — the gas test shouldn't set kWh.

Category 2 — setter round-trips. Set a cost, get it back, assert they match. Feels trivial, but it's the only thing that catches the costPerkWh = costPerkWh self-assignment bug. Worth one test per getter you have.

Category 3 — state overwriting. Call a setter twice with different values, then verify the calculation reflects the second one. This proves your object holds mutable state correctly rather than latching the first value. A no-op setter passes a round-trip test in some implementations but fails this one.

Category 4 — the edge cases, and this is where the grade is. Work out on paper what your code does when the divisor is zero, and when nothing has been set at all. Java doubles don't throw on division by zero the way integers do — you get Infinity or NaN depending on the numerator. Assert the actual behavior using Double.isInfinite() and Double.isNaN().

The interesting part isn't the assertion, it's the argument you make about it in the report: your class silently returns nonsense instead of failing loudly. You can defend that as acceptable, or argue it should validate inputs and throw. Either position is defensible if you reason it through — that's what the test strategy section is asking for.

Something to decide before you write. If you compute the hybrid average inside your test, you've duplicated logic that also lives in CarRunner. Two copies of a formula tend to drift. Moving calcHybridAverage() into HybridVehicle fixes that and gives you a cleaner thing to test — and a design decision to write about.

Naming. Descriptive test method names cost nothing and pay off when one goes red: zeroGallonsReturnsInfinity tells you more from the runner output than test3.


# 6.)
Lastly I asked Claude to revise my Readme and Lab Report to be more comprehensive and make sure they were understandable.
Claude provided the current Lab Report and Readme, I just edited them to add in the AI log.