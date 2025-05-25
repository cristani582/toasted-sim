package GUI;

import Geometry.InfiniteCylinder;
import Geometry.PlaneWall;
import Geometry.Sphere;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.math3.special.BesselJ;
import org.apache.commons.math3.special.Erf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

import static java.lang.Math.*;


public class InputPane extends JPanel{
    //public static ArrayList<Double> thetaStarFunction = new ArrayList<>();
    //public static ArrayList<Double> dimensionProblemVariable = new ArrayList<>();
    private boolean isPlaneWall = false;
    private boolean isInfiniteCylinder = false;
    private boolean isSphere = false;
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private JLabel lcLabel = new JLabel("<html>L<sub>C</sub> [m]: </html>");
    private JLabel kLabel = new JLabel("<html>k [W/(m.K)]: </html>");
    private JLabel rhoLabel = new JLabel("<html>ρ [kg/m<sup>3</sup>]: </html>");
    private JLabel cpLabel = new JLabel("<html>C<sub>P</sub> [J/(kg.K)]: </html>");
    private JLabel hLabel = new JLabel("<html>h [W/(m<sup>2</sup>.K)]: </html>");
    private JLabel temperatureProblemLabel = new JLabel("<html>T<sub>?</sub> [K]: </html>");
    private JLabel temperatureInitialLabel = new JLabel("<html>T<sub>0</sub> [K]: </html>");
    private JLabel temperatureInfiniteLabel = new JLabel("<html>T<sub>∞</sub> [K]: </html>");
    private JLabel dimensionProblemLabel = new JLabel("<html>x [m]: </html>");
    private JLabel timeSpentLabel = new JLabel("Time Spent [s]: ");

    private JTextField lcInput;
    private JTextField kInput;
    private JTextField rhoInput;
    private JTextField cpInput;
    private JTextField hInput;
    private JTextField temperatureProblemInput;
    private JTextField temperatureInitialInput;
    private JTextField temperatureInfiniteInput;
    private JTextField dimensionProblemInput;
    private JTextField timeSpentInput;

    Double lc;
    Double k;
    Double rho;
    Double cp;
    Double h;
    Double temperatureProblem = -1.0;
    Double temperatureInitial;
    Double temperatureInfinite;
    Double dimensionProblem;
    Double timeSpent = -1.0;

    private static JButton calculateButton = new JButton("Calculate");
    String[] geometries = {"Select Geometry", "Plane Wall", "Infinite Cylinder", "Sphere"};
    private JComboBox<String> geometrySelector = new JComboBox<>(geometries);
    private PlaneWall planeWall;
    private InfiniteCylinder infiniteCylinder;
    private Sphere sphere;
    private static Double fourier;

    public InputPane(JTextField lcInput, JTextField kInput, JTextField rhoInput, JTextField cpInput, JTextField hInput,
                     JTextField temperatureProblemInput, JTextField temperatureInitialInput, JTextField temperatureInfiniteInput,
                     JTextField dimensionProblemInput, JTextField timeSpentInput) throws ParseException {

        this.lcInput = lcInput;
        this.kInput = kInput;
        this.rhoInput = rhoInput;
        this.cpInput = cpInput;
        this.hInput = hInput;
        this.temperatureProblemInput = temperatureProblemInput;
        this.temperatureInitialInput = temperatureInitialInput;
        this.temperatureInfiniteInput = temperatureInfiniteInput;
        this.dimensionProblemInput = dimensionProblemInput;
        this.timeSpentInput = timeSpentInput;

        setLayout(new MigLayout("alignx center, aligny center"));
        setBackground(Color.WHITE);

        ChartsPane.ButtonHandlerChart buttonHandlerChart = new ChartsPane.ButtonHandlerChart();
        ComboBoxHandler comboBoxHandler = new ComboBoxHandler();
        ButtonHandler buttonHandler = new ButtonHandler();
        TextHandler textHandler = new TextHandler();
        add(geometrySelector, "south,wrap");

        add(lcLabel, "alignx right, aligny center");
        add(lcInput, "aligny center, width max(100,10%)");
        lcInput.addActionListener(textHandler);

        add(kLabel, "alignx right, aligny center");
        add(kInput, "aligny center, width max(100,10%)");
        kInput.addActionListener(textHandler);



        add(rhoLabel, "alignx right, aligny center");
        add(rhoInput, "aligny center, width max(100,10%)");
        rhoInput.addActionListener(textHandler);


        add(cpLabel, "alignx right, aligny center");
        add(cpInput, "aligny center, width max(100,10%)");
        cpInput.addActionListener(textHandler);


        add(hLabel, "alignx right, aligny center");
        add(hInput, "aligny center, width max(100,10%), wrap");
        hInput.addActionListener(textHandler);


        add(temperatureProblemLabel, "alignx right, aligny center");
        add(temperatureProblemInput, "aligny center, width max(100,10%)");
        temperatureProblemInput.addActionListener(textHandler);

        add(temperatureInitialLabel, "alignx right, aligny center");
        add(temperatureInitialInput, "aligny center, width max(100,10%)");
        temperatureInitialInput.addActionListener(textHandler);


        add(temperatureInfiniteLabel, "alignx right, aligny center");
        add(temperatureInfiniteInput,"aligny center, width max(100,10%)");
        temperatureInfiniteInput.addActionListener(textHandler);


        add(timeSpentLabel, "alignx right, aligny center");
        add(timeSpentInput,"aligny center, width max(100,10%)");
        timeSpentInput.addActionListener(textHandler);


        add(dimensionProblemLabel, "alignx right, aligny center");
        add(dimensionProblemInput, "aligny center, width max(100,10%), wrap");
        dimensionProblemInput.addActionListener(textHandler);


        geometrySelector.addItemListener(comboBoxHandler);
        calculateButton.addActionListener(buttonHandler);
        calculateButton.addActionListener(buttonHandlerChart);

        JPanel southPanel = new JPanel(new MigLayout());
        southPanel.setBackground(Color.WHITE);
        southPanel.add(geometrySelector, "aligny center, alignx center, width max(100,38%)");
        southPanel.add(calculateButton, "grow, alignx right");
        add(southPanel, "south");

    }


    //Listeners
    private class TextHandler implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if(!isPlaneWall && !isSphere && !isInfiniteCylinder){
                JOptionPane.showMessageDialog(new JPanel(), "Select a Geometry", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println(fourier);
            }
            else{

                if (event.getSource() == lcInput) {
                    try {
                        lc = Double.valueOf(NumberFormat.getNumberInstance(Locale.getDefault()).parse(lcInput.getText()).doubleValue());
                        lcInput.setBackground(new Color(186, 143, 244));
                        System.out.println("Lc = "+lc);
                    } catch (ParseException e) {
                        lcInput.setBackground(new Color(244, 143, 172));
                        throw new RuntimeException(e);
                    }
                }
                if (event.getSource() == kInput) {
                    try {
                        k = Double.valueOf(NumberFormat.getNumberInstance(Locale.getDefault()).parse(kInput.getText()).doubleValue());
                        kInput.setBackground(new Color(186, 143, 244));
                        System.out.println("k = "+k);
                    } catch (ParseException e) {
                        kInput.setBackground(new Color(244, 143, 172));
                        throw new RuntimeException(e);
                    }
                }
                if (event.getSource() == rhoInput) {
                    try {
                        rho = Double.valueOf(NumberFormat.getNumberInstance(Locale.getDefault()).parse(rhoInput.getText()).doubleValue());
                        rhoInput.setBackground(new Color(186, 143, 244));
                        System.out.println("rho = "+rho);
                    } catch (ParseException e) {
                        rhoInput.setBackground(new Color(244, 143, 172));
                        throw new RuntimeException(e);
                    }
                }
                if (event.getSource() == cpInput) {
                    try {
                        cp = Double.valueOf(NumberFormat.getNumberInstance(Locale.getDefault()).parse(cpInput.getText()).doubleValue());
                        cpInput.setBackground(new Color(186, 143, 244));
                        System.out.println("cp = "+cp);
                    } catch (ParseException e) {
                        cpInput.setBackground(new Color(244, 143, 172));
                        throw new RuntimeException(e);
                    }
                }
                if (event.getSource() == hInput) {
                    try {
                        h = Double.valueOf(NumberFormat.getNumberInstance(Locale.getDefault()).parse(hInput.getText()).doubleValue());
                        hInput.setBackground(new Color(186, 143, 244));
                        System.out.println("h = "+h);
                    } catch (ParseException e) {
                        hInput.setBackground(new Color(244, 143, 172));
                        throw new RuntimeException(e);
                    }
                }
                if (event.getSource() == temperatureProblemInput) {
                    try {
                        temperatureProblem = Double.valueOf(NumberFormat.getNumberInstance(Locale.getDefault()).parse(temperatureProblemInput.getText()).doubleValue());
                        temperatureProblemInput.setBackground(new Color(186, 143, 244));
                        System.out.println("temperature problem = "+temperatureProblem);
                    } catch (ParseException e) {
                        temperatureProblemInput.setBackground(Color.WHITE);
                        temperatureProblemInput.setText("");
                    }
                }
                if (event.getSource() == temperatureInitialInput) {
                    try {
                        temperatureInitial = Double.valueOf(NumberFormat.getNumberInstance(Locale.getDefault()).parse(temperatureInitialInput.getText()).doubleValue());
                        temperatureInitialInput.setBackground(new Color(186, 143, 244));
                        System.out.println("temperature initial = "+temperatureInitial);
                    } catch (ParseException e) {
                        temperatureInitialInput.setBackground(Color.WHITE);
                        temperatureInitialInput.setText("");
                    }
                }
                if (event.getSource() == temperatureInfiniteInput) {
                    try {
                        temperatureInfinite = Double.valueOf(NumberFormat.getNumberInstance(Locale.getDefault()).parse(temperatureInfiniteInput.getText()).doubleValue());
                        temperatureInfiniteInput.setBackground(new Color(186, 143, 244));
                        System.out.println("temperature infinite = "+temperatureInfinite);
                    } catch (ParseException e) {
                        temperatureInfiniteInput.setBackground(Color.WHITE);
                        temperatureInfiniteInput.setText(null);
                    }
                }
                if (event.getSource() == dimensionProblemInput) {
                    try {
                        dimensionProblem = Double.valueOf(NumberFormat.getNumberInstance(Locale.getDefault()).parse(dimensionProblemInput.getText()).doubleValue());
                        dimensionProblemInput.setBackground(new Color(186, 143, 244));
                        System.out.println("x* = "+dimensionProblem);
                    } catch (ParseException e) {
                        dimensionProblemInput.setBackground(new Color(244, 143, 172));
                        throw new RuntimeException(e);
                    }
                }
                if (event.getSource() == timeSpentInput) {
                    try {
                        timeSpent = Double.valueOf(NumberFormat.getNumberInstance(Locale.getDefault()).parse(timeSpentInput.getText()).doubleValue());
                        timeSpentInput.setBackground(new Color(186, 143, 244));
                        System.out.println("time spent = "+timeSpent);
                    } catch (ParseException e) {
                        timeSpentInput.setBackground(Color.WHITE);
                        timeSpentInput.setText(null);
                    }
                }
            }
            }
    }

    private class ComboBoxHandler implements ItemListener {
        public void itemStateChanged(ItemEvent event) {
            String geometrySelectionValue = (String) geometrySelector.getSelectedItem();
            if (event.getStateChange() == ItemEvent.DESELECTED) {
                return;
            }
            System.out.println(fourier);
            switch (geometrySelectionValue.toLowerCase()) {
                case "select geometry":
                    System.out.println("isPlaneWall = "+isPlaneWall+"\n"
                            +"isInfiniteCylinder = "+isInfiniteCylinder+"\n"
                            +"isSphere = "+isSphere);
                    break;
                case "plane wall":
                    isPlaneWall = true;
                    isInfiniteCylinder = false;
                    isSphere = false;

                    lcInput.setText("");
                    kInput.setText("");
                    rhoInput.setText("");
                    cpInput.setText("");
                    hInput.setText("");
                    temperatureProblemInput.setText("");
                    temperatureInitialInput.setText("");
                    temperatureInfiniteInput.setText("");
                    dimensionProblemInput.setText("");
                    timeSpentInput.setText("");

                    lcInput.setBackground(Color.WHITE);
                    kInput.setBackground(Color.WHITE);
                    rhoInput.setBackground(Color.WHITE);
                    cpInput.setBackground(Color.WHITE);
                    hInput.setBackground(Color.WHITE);
                    temperatureProblemInput.setBackground(Color.WHITE);
                    temperatureInitialInput.setBackground(Color.WHITE);
                    temperatureInfiniteInput.setBackground(Color.WHITE);
                    dimensionProblemInput.setBackground(Color.WHITE);
                    timeSpentInput.setBackground(Color.WHITE);


                    System.out.println("isPlaneWall = "+isPlaneWall+"\n"
                            +"isInfiniteCylinder = "+isInfiniteCylinder+"\n"
                            +"isSphere = "+isSphere);
                    break;
                case "infinite cylinder":
                    isPlaneWall = false;
                    isInfiniteCylinder = true;
                    isSphere = false;

                    lcInput.setText("");
                    kInput.setText("");
                    rhoInput.setText("");
                    cpInput.setText("");
                    hInput.setText("");
                    temperatureProblemInput.setText("");
                    temperatureInitialInput.setText("");
                    temperatureInfiniteInput.setText("");
                    dimensionProblemInput.setText("");
                    timeSpentInput.setText("");

                    lcInput.setBackground(Color.WHITE);
                    kInput.setBackground(Color.WHITE);
                    rhoInput.setBackground(Color.WHITE);
                    cpInput.setBackground(Color.WHITE);
                    hInput.setBackground(Color.WHITE);
                    temperatureProblemInput.setBackground(Color.WHITE);
                    temperatureInitialInput.setBackground(Color.WHITE);
                    temperatureInfiniteInput.setBackground(Color.WHITE);
                    dimensionProblemInput.setBackground(Color.WHITE);
                    timeSpentInput.setBackground(Color.WHITE);

                    System.out.println("isPlaneWall = "+isPlaneWall+"\n"
                            +"isInfiniteCylinder = "+isInfiniteCylinder+"\n"
                            +"isSphere = "+isSphere);
                    break;
                case "sphere":
                    isPlaneWall = false;
                    isInfiniteCylinder = false;
                    isSphere = true;

                    lcInput.setText("");
                    kInput.setText("");
                    rhoInput.setText("");
                    cpInput.setText("");
                    hInput.setText("");
                    temperatureProblemInput.setText("");
                    temperatureInitialInput.setText("");
                    temperatureInfiniteInput.setText("");
                    dimensionProblemInput.setText("");
                    timeSpentInput.setText("");

                    lcInput.setBackground(Color.WHITE);
                    kInput.setBackground(Color.WHITE);
                    rhoInput.setBackground(Color.WHITE);
                    cpInput.setBackground(Color.WHITE);
                    hInput.setBackground(Color.WHITE);
                    temperatureProblemInput.setBackground(Color.WHITE);
                    temperatureInitialInput.setBackground(Color.WHITE);
                    temperatureInfiniteInput.setBackground(Color.WHITE);
                    dimensionProblemInput.setBackground(Color.WHITE);
                    timeSpentInput.setBackground(Color.WHITE);

                    System.out.println("isPlaneWall = "+isPlaneWall+"\n"
                            +"isInfiniteCylinder = "+isInfiniteCylinder+"\n"
                            +"isSphere = "+isSphere);
                    break;
            }
        }
    }

    public static JButton getCalculateButton() {
        return calculateButton;
    }

    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if(event.getSource() == calculateButton){
                if(lcInput.getText().isEmpty() || kInput.getText().isEmpty() || rhoInput.getText().isEmpty() || hInput.getText().isEmpty() || dimensionProblemInput.getText().isEmpty() || temperatureInfiniteInput.getText().isEmpty() || temperatureInitialInput.getText().isEmpty()){
                    JOptionPane.showMessageDialog(new JPanel(), "Some Text Field is Empty", "Error", JOptionPane.ERROR_MESSAGE);
                     if(!isPlaneWall && !isSphere && !isInfiniteCylinder){
                        JOptionPane.showMessageDialog(new JPanel(), "Select a Geometry", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else{
                    if(isPlaneWall){
                        planeWall = new PlaneWall(lc,k,rho,cp);
                        timeSpent = planeWall.calculateSpatialEffectsTimeSpent(h,dimensionProblem,temperatureProblem,temperatureInitial,temperatureInfinite);
                        temperatureProblem = planeWall.calculateSpatialEffectsTemperatureProblem(h, dimensionProblem, timeSpent, temperatureInitial, temperatureInfinite);
                        fourier = planeWall.calculateFourier(timeSpent);

                        for (double dimensionProblemPoints = -1.0; dimensionProblemPoints <= 1.0; dimensionProblemPoints += 0.01) {
                            Double s1 = planeWall.biotConstants.get(0);
                            Double c1 = planeWall.biotConstants.get(1);
                            fourier = planeWall.calculateFourier(timeSpent);
                            double theta = c1*Math.cos(s1*dimensionProblemPoints)*Math.exp(-s1*s1*fourier);
                            ChartsPane.series0.add(dimensionProblemPoints, theta);
                        }
                    }
                    if(isInfiniteCylinder){
                        infiniteCylinder = new InfiniteCylinder(lc, k, rho, cp);
                        timeSpent = infiniteCylinder.calculateSpatialEffectsTimeSpent(h,dimensionProblem,temperatureProblem,temperatureInitial,temperatureInfinite);
                        temperatureProblem = infiniteCylinder.calculateSpatialEffectsTemperatureProblem(h, dimensionProblem, timeSpent, temperatureInitial, temperatureInfinite);

                        for (double dimensionProblemPoints = -1.0; dimensionProblemPoints <= 1.0; dimensionProblemPoints += 0.01) {
                            Double s1 = infiniteCylinder.biotConstants.get(0);
                            Double c1 = infiniteCylinder.biotConstants.get(1);
                            fourier = infiniteCylinder.calculateFourier(timeSpent);

                            //creating bessel function Jo
                            BesselJ bessel = new BesselJ(0); // Jn, n = 0  [Jo]
                            //solving Jo(s1.r*)

                            Double S1r_i = s1 * (dimensionProblemPoints);
                            if (S1r_i<0){
                                S1r_i = S1r_i*(-1); // it's an even function which means J(-x) = J(x)
                            }
                            Double besselS1r_i = bessel.value(S1r_i); // Jo(s1.r*)

                            double theta = c1*besselS1r_i*exp(-s1*s1*fourier);
                            ChartsPane.series0.add(dimensionProblemPoints, theta);
                        }
                    }
                    if(isSphere){
                        sphere = new Sphere(lc, k, rho, cp);
                        timeSpent = sphere.calculateSpatialEffectsTimeSpent(h,dimensionProblem,temperatureProblem,temperatureInitial,temperatureInfinite);
                        temperatureProblem = sphere.calculateSpatialEffectsTemperatureProblem(h, dimensionProblem, timeSpent, temperatureInitial, temperatureInfinite);

                        for (double dimensionProblemPoints = -1.0; dimensionProblemPoints <= 1.0; dimensionProblemPoints += 0.01) {
                            Double s1 = sphere.biotConstants.get(0);
                            Double c1 = sphere.biotConstants.get(1);
                            fourier = sphere.calculateFourier(timeSpent);
                            double theta = c1 * (Math.sin(s1 * dimensionProblemPoints) / (s1 * dimensionProblemPoints)) * Math.exp(-s1 * s1 * fourier);
                            ChartsPane.series0.add(dimensionProblemPoints, theta);
                        }
                    }
                    if(timeSpentInput.getText().isEmpty()){
                        timeSpentInput.setBackground(new Color(106, 244, 171));
                        timeSpentInput.setText(df2.format(timeSpent));
                        System.out.println(timeSpent);
                    }
                    if(temperatureProblemInput.getText().isEmpty()){
                        temperatureProblemInput.setBackground(new Color(106, 244, 171));
                        temperatureProblemInput.setText(df2.format(temperatureProblem));
                    }
                }
            }
        }
    }
}
