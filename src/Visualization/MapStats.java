package Visualization;

import Map.JungleMap;

import javax.swing.*;
import java.awt.*;

public class MapStats extends JPanel
{
    public JungleMap map;
    public Visualizer visualizer;
    public boolean inPause;

    private JLabel numberOfAnimalsLabel;
    private JLabel dayLabel;
    private JLabel numberOfGrassLabel;
    private JLabel averageOffspringNumberLabel;
    private JLabel averageEnergyLabel;
    private JLabel dominatingGenotypeLabel;
    private JLabel lifeExpectancyLabel;

    private JFrame frame;
    private JPanel popup;

    private String day;
    private String numberOfAnimals;
    private String numberOfGrass;
    private String averageOffspringNumber;
    private String averageEnergy;
    private String dominatingGenotype;
    private String lifeExpectancy;





    public MapStats(JungleMap map, Visualizer visualizer)
    {
        this.map = map;
        this.visualizer = visualizer;

        frame = new JFrame("Overall statistics");
        frame.getContentPane();

        popup = new JPanel();
        popup.setSize(new Dimension(400, 300));

        updateLabels();

        popup.setLayout(null);

        popup.add(dayLabel);
        popup.add(numberOfAnimalsLabel);
        popup.add(numberOfGrassLabel);
        popup.add(averageEnergyLabel);
        popup.add(averageOffspringNumberLabel);
        popup.add(lifeExpectancyLabel);

        frame.add(popup);

        frame.setSize(400, 300);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    public void paintComponent(Graphics g)
    {
        if(!inPause)
        {
            super.paintComponent(g);

            updateStrings();

            frame.remove(popup);

            popup.remove(dayLabel);
            popup.remove(numberOfAnimalsLabel);
            popup.remove(numberOfGrassLabel);
            popup.remove(averageEnergyLabel);
            popup.remove(averageOffspringNumberLabel);
            popup.remove(lifeExpectancyLabel);

            updateLabels();

            popup.add(dayLabel);
            popup.add(numberOfAnimalsLabel);
            popup.add(numberOfGrassLabel);
            popup.add(averageEnergyLabel);
            popup.add(averageOffspringNumberLabel);
            popup.add(lifeExpectancyLabel);

            frame.add(popup);

            frame.repaint();
        }
    }

    private void updateStrings()
    {
        this.day = "Day: " + map.day + "\n";
        this.numberOfAnimals = "Number of Animals: " + map.currentAnimalQuantity + "\n";
        this.numberOfGrass = "Number of Grass: " + map.currentGrassQuantity + "\n";
        this.averageOffspringNumber = "Average number of offsprings: " + map.averageOffspringNumber + "\n";
        this.averageEnergy = "Average animal energy: " + map.averageEnergy + "\n";
        this.dominatingGenotype = "Dominating genotype: " + map.dominatingGenotype + "\n";
        this.lifeExpectancy = "Life expectancy: " + map.averageLifeExpectancy + "\n\n";
    }

    private void printUpdatedStats()
    {
        updateStrings();

        System.out.println(this.day + this.numberOfAnimals + this.numberOfGrass + this.averageOffspringNumber + this.averageEnergy + this.dominatingGenotype + this.lifeExpectancy);
    }

    private void updateLabels()
    {
        this.dayLabel = new JLabel(day);
        Dimension size1 = this.dayLabel.getPreferredSize();
        this.dayLabel.setBounds(20, 20, size1.width, size1.height);

        this.numberOfAnimalsLabel = new JLabel(numberOfAnimals);
        Dimension size2 = this.numberOfAnimalsLabel.getPreferredSize();
        this.numberOfAnimalsLabel.setBounds(20, 50, size2.width, size2.height);

        this.numberOfGrassLabel = new JLabel(numberOfGrass);
        Dimension size3 = this.numberOfGrassLabel.getPreferredSize();
        this.numberOfGrassLabel.setBounds(20, 80, size3.width, size3.height);

        this.averageOffspringNumberLabel = new JLabel(averageOffspringNumber);
        Dimension size4 = this.averageOffspringNumberLabel.getPreferredSize();
        this.averageOffspringNumberLabel.setBounds(20, 110, size4.width, size4.height);

        this.averageEnergyLabel = new JLabel(averageEnergy);
        Dimension size5 = this.averageEnergyLabel.getPreferredSize();
        this.averageEnergyLabel.setBounds(20, 140, size5.width, size5.height);

        this.dominatingGenotypeLabel = new JLabel(dominatingGenotype);
        Dimension size6 = this.dominatingGenotypeLabel.getPreferredSize();
        this.dominatingGenotypeLabel.setBounds(20, 170, size6.width, size6.height);

        this.lifeExpectancyLabel = new JLabel(lifeExpectancy);
        Dimension size7 = this.lifeExpectancyLabel.getPreferredSize();
        this.lifeExpectancyLabel.setBounds(20, 200, size7.width, size7.height);
    }
}
