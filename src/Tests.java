import Geometry.InfiniteCylinder;
import Geometry.PlaneWall;
import Geometry.Sphere;

public class Tests {
    //testing methods in the terminal
    public static void main(String[] args) {
    //example 8.3 pdf Marcio using Lumped Capacitance Analysis
        System.out.println("------------------------------------------x------------------------------------------");
        //creating system
        InfiniteCylinder cylinder83 = new InfiniteCylinder(0.025, 215.0, 2700.0, 900.0);
        Double biot83 = cylinder83.calculateBiot(525.0);
        Double temperatureProblem83LCA = cylinder83.lumpedCapacitanceTemperature(525.0,60.0, 200.0, 70.0);

        //testing methods
        System.out.println("* The radius R of this infinite cylinder is: " + cylinder83.getRadius() * 100 + "cm");
        System.out.println("* The Biot number for this system is: " + biot83 + " so it's reasonable to use LCA");
        System.out.println("* For this problem, we can use LCA.\n We want a temperature T after 1 minute in a cooling of 70ºC. We obtain: " + temperatureProblem83LCA+"ºC");
        System.out.println("------------------------------------------x------------------------------------------");

    //example çengel 3.4 using LCA:
        //creating system
        InfiniteCylinder cylinder34 = new InfiniteCylinder(0.025,63.9, 7832.0, 434.0);
        Double timeSpent34 = cylinder34.lumpedCapacitanceTimeSpent(450.0,95.0,850.0,40.0);

        //solving the problem
        System.out.println("example 3.4 çengel: \n" +
                "The time obtained was: " + timeSpent34 + "s");
        System.out.println("-------------------------------------------x-----------------------------------------");

    //example 8.8 pdf Marcio
        //creating system
        PlaneWall wall88 = new PlaneWall(0.015, 160.0, 2790.0, 880.0);
        Double timeSpent88 = wall88.lumpedCapacitanceTimeSpent(320.0,50.0, 225.0, 25.0);
        System.out.println("example 8.8 Marcio: \n" +
                "The time obtained was: " + timeSpent88 + "s");
        System.out.println("------------------------------------------x------------------------------------------");
    //example 5.38 Incropera 8ed
        //creating system
        PlaneWall wall538 = new PlaneWall(0.05, 48.0, 7830.0, 550.0);
        Double timeSpent538 = wall538.calculateSpatialEffectsTimeSpent(250.0, 0.05,550.0,200.0,800.0);

        System.out.println("example 5.38 Incropera 8ed: \n" +
                "The time spent was: " + timeSpent538 + "s\n" +
                "biot constants:\n" +
                " s1: "+wall538.biotConstants.get(0)+"\n"+
                " c1: "+wall538.biotConstants.get(1));
        System.out.println("------------------------------------------x------------------------------------------");
    //example 8.3 pdf Marcio using Spatial Effects Analysis
        //I'm going to re-utilize the system cylinder83 created previously
        Double temperatureProblem83SEA = cylinder83.calculateSpatialEffectsTemperatureProblem(525.0,0.0125, 60.0, 200.0, 70.0);
        System.out.println("example 8.3 pdf Marcio: \n" +
                "The temperature obtained in " + 1.25 + "cm from the center is: "+temperatureProblem83SEA+"ºC\n"+
                "biot constants:\n" +
                "[s1]: "+cylinder83.biotConstants.get(0)+"\n"+
                "[c1]: "+cylinder83.biotConstants.get(1));
        System.out.println("------------------------------------------x------------------------------------------");
    //example 8.9 pdf Marcio using Spatial Effects Analysis
        //creating system
        Sphere sphere89 = new Sphere(0.025, 35.0, 7800.0, 460.0);
        Double temperatureProblem89LCA = sphere89.lumpedCapacitanceTemperature(10.0, 3600.0, 450.0, 100.0);
        Double temperatureProblem89SEA = sphere89.calculateSpatialEffectsTemperatureProblem(10.0,0.025,3600.0,450.0,100.0);
        System.out.println("example 8.9 pdf Marcio: \n" +
                "using LCA: temperature on the surface of the sphere is "+temperatureProblem89LCA+"ºC\n"+
                "using SEA: temperature on the surface of the sphere is "+temperatureProblem89SEA+"ºC");
    }
}