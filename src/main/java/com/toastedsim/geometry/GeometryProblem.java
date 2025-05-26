package com.toastedsim.geometry;

/**
 * Provides thermal utility methods for geometry-based heat transfer problems.
 * Can be extended by specific geometry problem classes.
 */

public abstract class GeometryProblem {
    public GeometryProblem() {
    }

    /**
     * Calculates the thermal diffusivity (α) using the relation:
     * α = k / (ρ * cp)
     * @param thermalConductivity thermal conductivity   (k)    [W/m·K]
     * @param density             material density       (ρ)    [kg/m³]
     * @param specificHeat        specific heat capacity (cp)   [J/kg·K]
     * @return thermal diffusivity                       (α)    [m²/s]
     */

    public double calculateThermalDiffusivity(Double thermalConductivity, Double density, Double specificHeat){
        return thermalConductivity/(density*specificHeat);
    }

    /**
     * Calculates the dimensionless temperature (θ*) using:
     * θ* = (T - T∞) / (Ti - T∞)
     *
     * @param temperature         current temperature (T) [K or °C]
     * @param initialTemperature  initial temperature (Ti) [K or °C]
     * @param ambientTemperature  ambient temperature (T∞) [K or °C]
     * @return dimensionless temperature (θ*)
     */

    public double normalizedTemperature(Double temperature, Double initialTemperature, Double ambientTemperature) {
        return (temperature - ambientTemperature)/(initialTemperature - ambientTemperature);
    }
}
