package u;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import c.HybridVehicle;

public class HybridVehicleTests {

    private static final double DELTA = 0.01;

    private HybridVehicle car;

    @Before
    public void setUp() {
        car = new HybridVehicle();
    }

    @Test
    public void calcGasMPGTest() {
        car.setMilesfromGas(120);
        car.setGallonsfromGas(6);
        assertEquals(20.0, car.calcGasMPG(), DELTA);
    }

    @Test
    public void calcMPGeTest() {
        car.setElectricMiles(300);
        car.setTotalkWh(70);
        assertEquals(144.43, car.calcMPGe(), DELTA);
    }

    @Test
    public void hybridAverageTest() {
        car.setMilesfromGas(120);
        car.setGallonsfromGas(6);
        car.setElectricMiles(300);
        car.setTotalkWh(70);

        double average = (car.calcGasMPG() + car.calcMPGe()) / 2;
        assertEquals(82.21, average, DELTA);
    }

    @Test
    public void costPerGallonRoundTripTest() {
        car.setCostPerGallon(3.50);
        assertEquals(3.50, car.getCostPerGallon(), DELTA);
    }

    @Test
    public void costPerkWhRoundTripTest() {
        car.setCostPerkWh(0.24);
        assertEquals(0.24, car.getCostPerkWh(), DELTA);
    }

    @Test
    public void settersOverwritePreviousValuesTest() {
        car.setMilesfromGas(100);
        car.setGallonsfromGas(5);
        car.setMilesfromGas(200);
        assertEquals(40.0, car.calcGasMPG(), DELTA);
    }

    @Test
    public void zeroGallonsGivesInfinityTest() {
        car.setMilesfromGas(120);
        car.setGallonsfromGas(0);
        assertTrue(Double.isInfinite(car.calcGasMPG()));
    }

    @Test
    public void unsetVehicleGivesNaNTest() {
        assertTrue(Double.isNaN(car.calcGasMPG()));
        assertTrue(Double.isNaN(car.calcMPGe()));
    }
}