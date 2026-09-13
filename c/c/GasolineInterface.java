public interface GasolineInterface {
    double calcGasMPG();

    void setMilesfromGas(double miles);

    void setGallonsfromGas(double gallons);

    void setCostPerGallon(double costPerGallon);

    double getCostPerGallon();
}