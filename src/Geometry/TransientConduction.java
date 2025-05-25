package Geometry;

public interface TransientConduction {
    public Double calculateFourier(Double timeSpent);
    public Double calculateBiot(Double h);
    public Double lumpedCapacitanceTemperature(Double h, Double timeSpent, Double temperatureInitial, Double temperatureInfinite);
    public Double lumpedCapacitanceTimeSpent(Double h, Double temperatureProblem, Double temperatureInitial, Double temperatureInfinite);
    public void calculateConstants(Double biotGiven);
    public Double calculateSpatialEffectsTemperatureProblem(Double h, Double dimensionProblem,Double timeSpent, Double temperatureInitial, Double temperatureInfinite);
    public Double calculateSpatialEffectsTimeSpent(Double h, Double dimensionProblem,Double temperatureProblem, Double temperatureInitial, Double temperatureInfinite);
}
