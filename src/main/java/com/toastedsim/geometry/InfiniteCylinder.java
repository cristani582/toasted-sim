package com.toastedsim.geometry;


import com.toastedsim.utils.SpreadSheetReader;
import org.apache.commons.math3.special.BesselJ;

import java.util.ArrayList;


public class InfiniteCylinder extends GeometryProblem implements TransientConduction {
    private Double radius;
    private Double thermalConductivity;
    private Double density;
    private Double specificHeat;
    private ArrayList<Double> biotConstants = new ArrayList<>();

    public InfiniteCylinder(Double radius, Double thermalConductivity, Double density, Double specificHeat) {
        this.radius = radius;
        this.thermalConductivity = thermalConductivity;
        this.density = density;
        this.specificHeat = specificHeat;
        if (radius <= 0 || thermalConductivity <= 0 || density <= 0 || specificHeat <= 0)
            throw new IllegalArgumentException("no parameter can be null or less than zero");
    }

    @Override
    public Double calculateFourier(Double timeSpent) {
        return calculateThermalDiffusivity(thermalConductivity, density, specificHeat)/(Math.pow(radius, 2));
    }
    @Override
    public Double calculateBiot(Double h) {
        double biot = (h*radius)/thermalConductivity;
        if(biot<0.1){
            System.out.println("[CAN USE LCA] biot = "+biot);
        }else{
            System.out.println("[CAN NOT USE LCA] biot = "+biot);
        }
        return biot;
    }

    @Override
    public Double lumpedCapacitanceTemperature(Double h, Double timeSpent, Double temperatureInitial, Double temperatureInfinite) {
        return ((Math.exp(-(((h*2)/(density*radius*specificHeat))*timeSpent))*(temperatureInitial - temperatureInfinite))+temperatureInfinite); //Temperature problem
    }

    @Override
    public Double lumpedCapacitanceTimeSpent(Double h, Double temperatureProblem, Double temperatureInitial, Double temperatureInfinite) {
        //defining normalizedTemperature
        Double thetaStar = this.normalizedTemperature(temperatureProblem, temperatureInitial, temperatureInfinite);

        return (-(Math.log(thetaStar))*(density*radius*specificHeat)/(2*h)); //Time spent
    }

    @Override
    public void calculateConstants(Double biotGiven) {
        SpreadSheetReader constantsTable = new SpreadSheetReader("spreadsheets/infinite-cylinder.txt");
        Double s1 = constantsTable.interpolateS1(biotGiven);
        Double c1 = constantsTable.interpolateC1(biotGiven);
        biotConstants.clear();
        biotConstants.add(s1);
        biotConstants.add(c1);
    }

    @Override
    public Double calculateSpatialEffectsTemperatureProblem(Double h, Double dimensionProblem, Double timeSpent, Double temperatureInitial, Double temperatureInfinite) {
        Double temperatureProblem;
        Double biot = calculateBiot(h);
        if(biot<0.01){
            temperatureProblem = lumpedCapacitanceTemperature(h,timeSpent,temperatureInitial,temperatureInfinite);
            System.out.println("Very small Biot number; LCA was used instead of SEA.");
        }else{
            calculateConstants(biot);
            Double s1 = biotConstants.get(0);
            Double c1 = biotConstants.get(1);

            //creating bessel function Jo
            BesselJ bessel = new BesselJ(0); // Jn, n = 0  [Jo]

            //solving Jo(s1.r*)
            Double besselS1r = bessel.value(s1*(dimensionProblem/radius)); // Jo(s1.r*)

            Double fourier = calculateFourier(timeSpent);
            Double s1Fo = -(s1*s1)*fourier;
            Double expS1Fo = Math.exp(s1Fo);

            temperatureProblem = (c1*besselS1r*expS1Fo*(temperatureInitial-temperatureInfinite))+temperatureInfinite; // temperature problem solution
        }
        return temperatureProblem;
    }

    @Override
    public Double calculateSpatialEffectsTimeSpent(Double h, Double dimensionProblem, Double temperatureProblem, Double temperatureInitial, Double temperatureInfinite) {
        Double timeSpent;
        Double biot = calculateBiot(h);
        if(biot < 0.01){
            timeSpent = lumpedCapacitanceTimeSpent(h, temperatureProblem, temperatureInitial, temperatureInfinite);
            System.out.println("Very small Biot number; LCA was used instead of SEA.");
        }else{
            calculateConstants(biot);
            Double s1 = biotConstants.get(0);
            double minusS1Square = -(s1*s1);
            Double c1 = biotConstants.get(1);

            //creating bessel function Jo
            BesselJ bessel = new BesselJ(0); // Jn, n = 0  [Jo]
            //solving Jo(s1.r*)
            Double besselS1r = bessel.value(s1*(dimensionProblem/radius)); // Jo(s1.r*)

            Double thetaStar = this.normalizedTemperature(temperatureProblem, temperatureInitial, temperatureInfinite);
            Double fourier = Math.log((thetaStar)/(c1*besselS1r))*(1/(minusS1Square));

            timeSpent = (density*specificHeat*fourier*radius*radius)/(thermalConductivity); // time spent
        }
        return timeSpent;
    }

    public Double getRadius() {
        return radius;
    }

    public Double getK() {
        return thermalConductivity;
    }

    public Double getRho() {
        return density;
    }

    public Double getCp() {
        return specificHeat;
    }

    public ArrayList<Double> getBiotConstants() {
        return biotConstants;
    }
}
