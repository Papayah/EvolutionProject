package Visualization;

import Map.JungleMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;

public class Visualizer extends JPanel implements ActionListener
{
    public JungleMap map;
    public int numberOfAnimals;

    public JFrame simulation;
    public JPanel panel;

    private JButton pause;
    private JButton resume;
    private JButton showGenotype;
    private JButton exportToFile;

    public RenderPanel render;
    public MapStats stats;

    Timer timer = new Timer(0, this);


    private int averageAnimalQuantity = 0;
    private int averageEnergyLevel = 0;
    private double lifeExpectancy = 0;
    private double averageOffspringNumber = 0;
    private double averageGrassQuantity = 0;
    private String dominatingGenotype = "";

    private HashMap<String, Integer> geneMap = new HashMap<>();

    public Visualizer(JungleMap map, int numberOfAnimals)
    {
        this.map = map;
        this.numberOfAnimals = numberOfAnimals;

        FileExporter fileExporter = new FileExporter();
        String [] data = new String[7];

        this.simulation = new JFrame("Evolution Simulation");
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);

        this.panel = new JPanel();
        panel.setBackground(new Color(125, 169, 203));
        panel.setBounds(0, 800, 800, 200);

        this.render = new RenderPanel(map, this);
        render.setSize(new Dimension(800, 800));

        this.stats = new MapStats(map, this);
        stats.setBounds(0, 0, 1, 1);

        this.pause = new JButton("Pause");
        pause.setBounds(30, 820, pause.getPreferredSize().width, pause.getPreferredSize().height);
        pause.addActionListener(e -> render.inPause = true);

        this.resume = new JButton("Resume");
        resume.setBounds(180, 820, resume.getPreferredSize().width, resume.getPreferredSize().height);
        resume.addActionListener(e ->
        {
            render.showGenotype = false;
            render.inPause = false;
        });

        this.showGenotype = new JButton("Show dominating genotype");
        showGenotype.setBounds(330, 820, showGenotype.getPreferredSize().width, showGenotype.getPreferredSize().height);
        showGenotype.addActionListener(e ->
        {
            if(!render.inPause)
                JOptionPane.showMessageDialog(simulation, "Pause the simulation first.");
            else
            {
                render.showGenotype = true;
                render.repaint();
            }
        });

        this.exportToFile = new JButton("Export to file");
        exportToFile.setBounds(580, 820, exportToFile.getPreferredSize().width, exportToFile.getPreferredSize().height);
        exportToFile.addActionListener(e ->
        {
            if(!render.inPause)
                JOptionPane.showMessageDialog(simulation, "Pause the simulation first.");
            else
            {
                data[0] = Integer.toString(map.day);
                data[1] = Double.toString((double) averageAnimalQuantity / (double) map.day);
                data[2] = Double.toString((double) averageGrassQuantity / (double) map.day);
                data[3] = Double.toString((double) averageOffspringNumber / (double) map.day);
                data[4] = Double.toString((double) averageEnergyLevel / (double) map.day);
                data[5] = findMostOftenGenotype();
                data[6] = Double.toString((double) lifeExpectancy / map.day);
                fileExporter.updateData(data);
                try{
                    fileExporter.exportStatsToFile();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        });

        stats.setLayout(null);

        panel.setLayout(null);
        panel.add(pause);
        panel.add(resume);
        panel.add(showGenotype);
        panel.add(exportToFile);

        simulation.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        simulation.add(render);
        simulation.add(stats);
        simulation.add(panel);
        simulation.setSize(800, 900);
        simulation.setPreferredSize(new Dimension(800, 900));
        simulation.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(!render.inPause)
        {
            try
            {
                Thread.sleep(50);
            } catch (InterruptedException interruptedException)
            {
                interruptedException.printStackTrace();
            }

            simulation.repaint();
            //stats.repaint();
            getDayData();
            map.nextDay();
        }
    }

    public void startTheSimulation(){
        map.spawnAnimalsRandomly(numberOfAnimals);
        map.growRandomGrass();
        timer.start();
    }

    private void getDayData()
    {
        this.averageAnimalQuantity += map.animalCounter();
        this.averageGrassQuantity += map.grassCounter();
        this.averageOffspringNumber += map.calculateAverageOffspringNumber();
        this.averageEnergyLevel += map.calculateAverageEnergy();
        this.lifeExpectancy += map.calculateAverageDaysAlive();

        if(geneMap.containsKey(map.dominatingGenotype))
            geneMap.replace(map.dominatingGenotype, geneMap.get(map.dominatingGenotype) + 1);
        else
            geneMap.put(map.dominatingGenotype, 1);
    }

    private String findMostOftenGenotype()
    {
        int max = 0;
        String genotype = "";

        for(String s : geneMap.keySet())
            if(geneMap.get(s) > max)
            {
                max = geneMap.get(s);
                genotype = s;
            }

        return genotype;
    }
}
