public class VehicleRunner {
    public static void main(String[] args) {
        HybridVehicle Prius = new HybridVehicle();
        Prius.setMilesfromGas(120);
        Prius.setCostPerGallon(6);
        Prius.setCostPerkWh(350);

        Prius.setElectricMiles(300);
        Prius.setGallonsfromGas(70);
        Prius.setTotalkWh(.24);
    }
}
