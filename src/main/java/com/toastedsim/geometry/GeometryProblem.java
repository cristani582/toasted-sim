package com.toastedsim.geometry;

public abstract class GeometryProblem {
    public GeometryProblem() {
    }
    public Double calculateAlpha(Double k, Double rho, Double cp){
        return k/(rho*cp);
    }
    public Double thetaStar(Double temperature, Double temperatureInitial, Double temperatureInfinite) {
        return (temperature - temperatureInfinite)/(temperatureInitial - temperatureInfinite);
    }

}
