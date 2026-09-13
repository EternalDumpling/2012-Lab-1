public class HybridVehicle implements GasolineInterface, ElectricInterface{

    private static final double MPG_E = 33.7;

    private double gasMiles;
    private double gallons;
    private double costPerGallon;

    private double electricMiles;
    private double totalkWh;
    private double costPerkWh;


    @Override
    public double calcMPGe() {
        return (electricMiles / totalkWh) * MPG_E;
    }

    @Override
    public void setElectricMiles(double totalElectricMiles) {
        this.electricMiles = totalElectricMiles;
    }

    @Override
    public void setTotalkWh(double totalkWh) {
        this.totalkWh = totalkWh;
    }

    @Override
    public void setCostPerkWh(double costPerkWh) {
        this.costPerkWh = costPerkWh;
    }

    @Override
    public double getCostPerkWh() {
        return costPerkWh;
    }

    @Override
    public double calcGasMPG() {
        return gasMiles / gallons;
    }

    @Override
    public void setMilesfromGas(double miles) {
        this.gasMiles = miles;
    }

    @Override
    public void setGallonsfromGas(double gallons) {
        this.gallons = gallons;
    }

    @Override
    public void setCostPerGallon(double costPerGallon) {
        this.costPerGallon = costPerGallon;
    }

    @Override
    public double getCostPerGallon() {
        return costPerGallon;
    }
}
