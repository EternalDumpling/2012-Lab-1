public class CarRunner {
    public static void main(String[] args) {
        HybridVehicle prius = new HybridVehicle();
        prius.setMilesfromGas(120);
        prius.setGallonsfromGas(6);
        prius.setCostPerGallon(3.50);
        prius.setElectricMiles(300);
        prius.setTotalkWh(70);
        prius.setCostPerkWh(0.24);

        double gasMPG = prius.calcGasMPG();
        double mpg_e = prius.calcMPGe();
        double hybridAvg = (gasMPG + mpg_e) / 2;

        System.out.printf("Fully gas mode: %.2f MPG%n", gasMPG); // MPG while fully using gas
        System.out.printf("Fully electric mode: %.2f MPGe%n", mpg_e); // MPG while fully using electric
        System.out.printf("half-gas/half-electric: %.2f Average MPG%n", hybridAvg); // MPG while hybrid driving
    }
}
