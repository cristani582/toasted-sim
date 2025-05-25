package com.toastedsim.utils;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class SpreadSheetReader implements Calc {
    private final double[] biot;
    private final double[] s1;
    private final double[] c1;


    public SpreadSheetReader(String spreadSheetPath) {
        ArrayList<Double> biotArrayList = new ArrayList<>();
        ArrayList<Double> s1ArrayList = new ArrayList<>();
        ArrayList<Double> c1ArrayList = new ArrayList<>();

        try{
            File file = new File(spreadSheetPath);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim(); // Trim any leading/trailing whitespace
                String[] columns = line.split("\\s+"); // Split by any whitespace

                if (columns.length >= 3) {
                    // Check if both columns are valid doubles
                    if (isDouble(columns[0]) && isDouble(columns[1])) {
                        biotArrayList.add(Double.parseDouble(columns[0]));
                        s1ArrayList.add(Double.parseDouble(columns[1]));
                        c1ArrayList.add(Double.parseDouble(columns[2]));
                    }
                } else {
                    System.out.println("Skipped line (not enough columns): " + line);
                }
            }
        }
        catch (FileNotFoundException error){
            error.printStackTrace();
            System.out.println("File Not Found");
        }

        // converting the arraylists in arrays
        biot = biotArrayList.stream().mapToDouble(Double::doubleValue).toArray();
        s1 = s1ArrayList.stream().mapToDouble(Double::doubleValue).toArray();
        c1 = c1ArrayList.stream().mapToDouble(Double::doubleValue).toArray();

        /*

        // testing arrays

        System.out.println("T[°C]: ");
        for (double t : temperatureArray) {
            System.out.println(t);
        }

        System.out.println("P[kPa]: ");
        for (double p : pressureArray) {
            System.out.println(p);
        }
         */
    }

    private static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public Double interpolateS1(Double biotGiven) {
        SplineInterpolator interpolator = new SplineInterpolator();
        PolynomialSplineFunction functionS1 = interpolator.interpolate(biot, s1);
        Double s1Returned = functionS1.value(biotGiven);

        return s1Returned;
    }
    @Override
    public Double interpolateC1(Double biotGiven) {
        SplineInterpolator interpolator = new SplineInterpolator();
        PolynomialSplineFunction functionc1 = interpolator.interpolate(biot, c1);
        Double c1Returned = functionc1.value(biotGiven);

        return c1Returned;
    }
}