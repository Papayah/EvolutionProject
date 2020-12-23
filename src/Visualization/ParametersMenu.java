package Visualization;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import Classes.*;
import EnumClasses.*;
import Interfaces.*;
import Map.JungleMap;


public class ParametersMenu extends JPanel implements ActionListener{
    // data
    public int width;
    public int height;
    public double initialEnergy;
    public double moveEnergy;
    public double energyFromGrass;
    public double jungleMultiplier;
    public int numberOfAnimals;


    private JLabel widthLabel;
    private JTextField widthText;

    private JLabel heightLabel;
    private JTextField heightText;

    private JLabel initialEnergyLabel;
    private JTextField initialEnergyText;

    private JLabel moveEnergyLabel;
    private JTextField moveEnergyText;

    private JLabel energyFromGrassLabel;
    private JTextField energyFromGrassText;

    private JLabel jungleMultiplierLabel;
    private JTextField jungleMultiplierText;

    private JLabel numberOfAnimalsLabel;
    private JTextField numberOfAnimalsText;

    private JButton startButton;

    public JFrame menu;

    public ParametersMenu(){
        menu = new JFrame("Start menu");
        JPanel panel = new JPanel();
        menu.getContentPane();


        widthLabel = new JLabel("Map width: ");
        Dimension size1 = widthLabel.getPreferredSize();
        widthLabel.setBounds(40, 20, size1.width, size1.height);

        widthText = new JTextField(6);
        Dimension textSize1 = widthText.getPreferredSize();
        widthText.setBounds(170, 20, textSize1.width, textSize1.height);



        heightLabel = new JLabel("Map height: ");
        Dimension size2 = heightLabel.getPreferredSize();
        heightLabel.setBounds(40, 80, size2.width, size2.height);

        heightText = new JTextField(6);
        Dimension textSize2 = heightText.getPreferredSize();
        heightText.setBounds(170, 80, textSize2.width, textSize2.height);



        initialEnergyLabel = new JLabel("Initial energy: ");
        Dimension size3 = initialEnergyLabel.getPreferredSize();
        initialEnergyLabel.setBounds(40, 140, size3.width, size3.height);

        initialEnergyText = new JTextField(6);
        Dimension textSize3 = initialEnergyText.getPreferredSize();
        initialEnergyText.setBounds(170, 140, textSize3.width, textSize3.height);



        moveEnergyLabel = new JLabel("Move energy: ");
        Dimension size4 = moveEnergyLabel.getPreferredSize();
        moveEnergyLabel.setBounds(40, 200, size4.width, size4.height);

        moveEnergyText = new JTextField(6);
        Dimension textSize4 = moveEnergyText.getPreferredSize();
        moveEnergyText.setBounds(170, 200, textSize4.width, textSize4.height);



        energyFromGrassLabel = new JLabel("Energy from Grass: ");
        Dimension size5 = energyFromGrassLabel.getPreferredSize();
        energyFromGrassLabel.setBounds(40, 260, size5.width, size5.height);

        energyFromGrassText = new JTextField(6);
        Dimension textSize5 = energyFromGrassText.getPreferredSize();
        energyFromGrassText.setBounds(170, 260, textSize5.width, textSize5.height);



        jungleMultiplierLabel = new JLabel("Jungle multiplier: ");
        Dimension size6 = jungleMultiplierLabel.getPreferredSize();
        jungleMultiplierLabel.setBounds(40, 320, size6.width, size6.height);

        jungleMultiplierText = new JTextField(6);
        Dimension textSize6 = jungleMultiplierText.getPreferredSize();
        jungleMultiplierText.setBounds(170, 320, textSize6.width, textSize6.height);



        numberOfAnimalsLabel = new JLabel("Number of animals: ");
        Dimension size7 = numberOfAnimalsLabel.getPreferredSize();
        numberOfAnimalsLabel.setBounds(40,380,size7.width,size7.height);

        numberOfAnimalsText = new JTextField(6);
        Dimension textSize7 = numberOfAnimalsText.getPreferredSize();
        numberOfAnimalsText.setBounds(170,380,textSize7.width,textSize7.height);



        startButton = new JButton("Start!");
        Dimension bsize = startButton.getPreferredSize();
        startButton.setBounds(100, 410, bsize.width, bsize.height);
        startButton.addActionListener(this);



        panel.setLayout(null);
        panel.add(widthLabel);
        panel.add(widthText);

        panel.add(heightLabel);
        panel.add(heightText);

        panel.add(initialEnergyLabel);
        panel.add(initialEnergyText);

        panel.add(moveEnergyLabel);
        panel.add(moveEnergyText);

        panel.add(energyFromGrassLabel);
        panel.add(energyFromGrassText);

        panel.add(jungleMultiplierLabel);
        panel.add(jungleMultiplierText);

        panel.add(numberOfAnimalsLabel);
        panel.add(numberOfAnimalsText);

        panel.add(startButton);

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.add(panel);
        menu.setSize(300, 500);
        menu.setVisible(true);
    }


    public void setParameters(JSONLoader loader){
        this.widthText.setText(Integer.toString(loader.width));
        this.heightText.setText(Integer.toString(loader.height));
        this.initialEnergyText.setText(Double.toString(loader.initialEnergy));
        this.moveEnergyText.setText(Double.toString(loader.moveEnergy));
        this.energyFromGrassText.setText(Double.toString(loader.energyFromGrass));
        this.jungleMultiplierText.setText(Double.toString(loader.jungleMultiplier));
        this.numberOfAnimalsText.setText(Integer.toString(loader.numberOfAnimals));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        width = Integer.parseInt(widthText.getText());
        height = Integer.parseInt(heightText.getText());
        initialEnergy = Double.parseDouble(initialEnergyText.getText());
        moveEnergy = Double.parseDouble(moveEnergyText.getText());
        energyFromGrass = Double.parseDouble(energyFromGrassText.getText());
        jungleMultiplier = Double.parseDouble(jungleMultiplierText.getText());
        numberOfAnimals = Integer.parseInt(numberOfAnimalsText.getText());

        if(checkTheValues()) {
            JungleMap map = new JungleMap(width, height, jungleMultiplier, initialEnergy, moveEnergy, energyFromGrass);
            Visualizer newSimulation = new Visualizer(map, numberOfAnimals);
            newSimulation.startTheSimulation();
        }
    }

    private boolean checkTheValues(){
        if(width <= 2 || width > 800){
            JOptionPane.showMessageDialog(this,"Width must be a value between 3 and 800.", "Wrong value", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(height <= 2 || height > 800){
            JOptionPane.showMessageDialog(this,"Width must be a value between 3 and 800.", "Wrong value", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(jungleMultiplier <= 0 || jungleMultiplier > Math.min(width, height)){
            JOptionPane.showMessageDialog(this,"Jungle multiplier must be a value between 0 and " + Math.min(width, height), "Wrong value", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;

    }
}