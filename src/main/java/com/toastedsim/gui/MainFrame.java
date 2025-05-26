package com.toastedsim.gui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;

public class MainFrame extends JFrame {
    protected JTextField lcInput = new JTextField("");
    protected JTextField kInput = new JTextField("");
    protected JTextField rhoInput = new JTextField("");
    protected JTextField cpInput = new JTextField("");
    protected JTextField hInput = new JTextField("");
    protected JTextField temperatureProblemInput = new JTextField("");
    protected JTextField temperatureInitialInput = new JTextField("");
    protected JTextField temperatureInfiniteInput = new JTextField("");
    protected JTextField dimensionProblemInput = new JTextField("");
    protected JTextField timeSpentInput = new JTextField("");

    public MainFrame(String title) throws HeadlessException, ParseException {
        super(title);
        getContentPane().setBackground(Color.WHITE);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("alignx center"));


        InputPane inputPane = new InputPane( lcInput,  kInput,  rhoInput,  cpInput,  hInput,
                 temperatureProblemInput, temperatureInitialInput,  temperatureInfiniteInput,
                 dimensionProblemInput,  timeSpentInput);

        ChartsPane chartsPane = new ChartsPane();
        add(inputPane, "grow, alignx center, aligny top, wrap");
        add(chartsPane);





        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


}
