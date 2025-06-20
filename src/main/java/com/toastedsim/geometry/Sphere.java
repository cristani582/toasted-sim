package com.toastedsim.geometry;


import com.toastedsim.utils.SpreadSheetReader;

import java.util.ArrayList;


public class Sphere extends GeometryProblem implements TransientConduction {
    private Double radius;
    private Double k;
    private Double rho;
    private Double cp;
    private ArrayList<Double> biotConstants = new ArrayList<>();

    public Sphere(Double radius, Double k, Double rho, Double cp) {
        this.radius = radius;
        this.k = k;
        this.rho = rho;
        this.cp = cp;
        if (radius <= 0 || k <= 0 || rho <= 0 || cp <= 0)
            throw new IllegalArgumentException("no parameter can be null or less than zero");
    }

    @Override
    public Double calculateFourier(Double timeSpent) {
        return calculateThermalDiffusivity(k, rho, cp)/(Math.pow(radius, 2));
    }

    @Override
    public Double calculateBiot(Double h) {
        Double biot = (h*radius)/k;
        if(biot<0.1){
            System.out.println("[CAN USE LCA] biot = "+biot);
        }else{
            System.out.println("[CAN NOT USE LCA] biot = "+biot);
        }
        return biot;
    }

    @Override
    public Double lumpedCapacitanceTemperature(Double h, Double timeSpent, Double initialTemperature, Double temperatureInfinite) {
        return ((Math.exp(-(((h*3)/(rho*radius*cp))*timeSpent))*(initialTemperature - temperatureInfinite))+temperatureInfinite);
    }
    @Override
    public Double lumpedCapacitanceTimeSpent(Double h, Double temperatureProblem, Double initialTemperature, Double temperatureInfinite) {
        //defining normalizedTemperature
        Double thetaStar = this.normalizedTemperature(temperatureProblem, initialTemperature, temperatureInfinite);

        return (-(Math.log(thetaStar))*(rho*radius*cp)/(3*h));
    }

    @Override
    public void calculateConstants(Double biotGiven) {
        SpreadSheetReader constantsTable = new SpreadSheetReader("spreadsheets/sphere.txt");
        Double s1 = constantsTable.interpolateS1(biotGiven);
        Double c1 = constantsTable.interpolateC1(biotGiven);
        biotConstants.clear();
        biotConstants.add(s1);
        biotConstants.add(c1);
    }

    @Override
    public Double calculateSpatialEffectsTemperatureProblem(Double h, Double dimensionProblem, Double timeSpent, Double initialTemperature, Double temperatureInfinite) {
        Double temperatureProblem;
        Double biot = calculateBiot(h);
        if(biot < 0.01){
            temperatureProblem = lumpedCapacitanceTemperature(h,timeSpent,initialTemperature,temperatureInfinite);
            System.out.println("Very small Biot number; LCA was used instead of SEA.");
        }
        else{
            calculateConstants(biot);
            Double s1 = biotConstants.get(0);
            Double c1 = biotConstants.get(1);

            Double sinArgument = s1*(dimensionProblem/radius);
            Double sinS1rDividedByArgument = Math.sin(sinArgument)/sinArgument;

            Double fourier = calculateFourier(timeSpent);
            Double s1Fo = -(s1*s1)*fourier;
            Double expS1Fo = Math.exp(s1Fo);

            temperatureProblem = (c1*sinS1rDividedByArgument*expS1Fo*(initialTemperature-temperatureInfinite))+temperatureInfinite; // temperature problem solution
        }
        return temperatureProblem;
    }

    @Override
    public Double calculateSpatialEffectsTimeSpent(Double h, Double dimensionProblem, Double temperatureProblem, Double initialTemperature, Double temperatureInfinite) {
        Double timeSpent;
        Double biot = calculateBiot(h);
        if(biot < 0.01){
            timeSpent = lumpedCapacitanceTimeSpent(h, temperatureProblem, initialTemperature, temperatureInfinite);
            System.out.println("Very small Biot number; LCA was used instead of SEA.");
        }else{
            calculateConstants(biot);
            Double s1 = biotConstants.get(0);
            double minusS1Square = -(s1*s1);
            Double c1 = biotConstants.get(1);

            Double sinArgument = s1*(dimensionProblem/radius);
            Double sinS1rDividedByArgument = Math.sin(sinArgument)/sinArgument;

            Double thetaStar = this.normalizedTemperature(temperatureProblem, initialTemperature, temperatureInfinite);
            Double fourier = Math.log((thetaStar)/(c1*sinS1rDividedByArgument))*(1/(minusS1Square));

            timeSpent = (rho*cp*fourier*radius*radius)/(k); // time spent
        }
        return timeSpent;
    }

    public Double getRadius() {
        return radius;
    }

    public Double getK() {
        return k;
    }

    public Double getRho() {
        return rho;
    }

    public Double getCp() {
        return cp;
    }

    public ArrayList<Double> getBiotConstants() {
        return biotConstants;
    }
}
