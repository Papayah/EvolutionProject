package Visualization;

import Classes.Animal;
import Classes.Grass;
import Classes.Vector2d;
import Interfaces.IWorldMap;
import Map.JungleMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class RenderPanel extends JPanel implements MouseListener
{
    public JungleMap map;
    public Visualizer visualizer;
    public boolean inPause;
    public boolean showGenotype;

    public String genotype;

    private int widthScale;
    private int heightScale;

    private JFrame frame;

    private JPanel popup;
    private JLabel animalStats;
    private JLabel genotypeLabel;
    private JLabel typeNLabel;
    private JTextField typeNText;
    private JButton showWhenDead;
    private JButton follow;

    public RenderPanel(JungleMap map, Visualizer visualizer)
    {
        this.map = map;
        this.visualizer = visualizer;
        this.inPause = false;
        this.showGenotype = false;
        addMouseListener(this);

    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Random random = new Random();
        int width = 800;
        int height = 800;

        this.widthScale = Math.round(width / map.width);
        this.heightScale = Math.round(height / map.height);

        //background
        g.setColor(new Color(189, 217, 207));
        g.fillRect(0, 0, width, height);

        //steppe
        g.setColor(new Color(175, 175, 39));
        g.fillRect(0, 0, width * widthScale, height * heightScale);

        //jungle
        g.setColor(new Color(41, 113, 18, 149));
        g.fillRect(map.getJungleLowerLeft().x * widthScale, map.getJungleLowerLeft().y * heightScale + 1, map.jungleWidth * widthScale + 1, map.jungleHeight * heightScale);

        //grass
        g.setColor(new Color(71, 224, 15));
        for(Grass grass : map.getGrasses())
            g.fillRect(grass.getPosition().x * widthScale, grass.getPosition().y * heightScale, widthScale, heightScale);

        //animals
        for(LinkedList<Animal> stackedAnimals : map.getAnimalMap().values())
            for(Animal animal : stackedAnimals)
            {
                switch (animal.getEnergyLevel())
                {
                    case 0 -> g.setColor(new Color(255, 0, 58));
                    case 1 -> g.setColor(new Color(255, 100, 130));
                    case 2 -> g.setColor(new Color(255, 150, 255));
                    default -> throw new IllegalArgumentException("Invalid energy level: " + animal.getEnergyLevel());
                }
                g.fillRect(animal.getPosition().x * widthScale, animal.getPosition().y * heightScale, widthScale, heightScale);
            }

        if(showGenotype)
        {
            genotype = map.getDominatingGenotype();
            g.setColor(new Color(131, 16, 191));
            for(LinkedList<Animal> stackedAnimals : map.getAnimalMap().values())
                for(Animal animal : stackedAnimals)
                    if(animal.getGenes().toString().equals(genotype))
                        g.fillRect(animal.getPosition().x * widthScale, animal.getPosition().y * heightScale, widthScale, heightScale);
        }

    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if(inPause)
        {
            int mouseX = e.getX();
            int mouseY = e.getY();
            LinkedList<Animal> stackedAnimals = map.getAnimalMap().get(new Vector2d(mouseX / widthScale, mouseY / heightScale));

            if(stackedAnimals != null && stackedAnimals.size() > 0)
            {
                Animal animal = stackedAnimals.getLast();

                frame = new JFrame("Animal options");
                frame.getContentPane();

                popup = new JPanel();
                popup.setSize(new Dimension(400, 300));

                animalStats = new JLabel("Position: " + animal.getPosition().toString());
                animalStats.setBounds(50, 50, animalStats.getPreferredSize().width, animalStats.getPreferredSize().height);

                genotypeLabel = new JLabel("Genotype: " + animal.genes.toString());
                genotypeLabel.setBounds(50, 10, genotypeLabel.getPreferredSize().width, genotypeLabel.getPreferredSize().height);

                typeNLabel = new JLabel("Type n: ");
                typeNLabel.setBounds(50, 100, typeNLabel.getPreferredSize().width, typeNLabel.getPreferredSize().height);

                typeNText = new JTextField(6);
                typeNText.setBounds(150, 100, typeNText.getPreferredSize().width, typeNText.getPreferredSize().height);

                showWhenDead = new JButton("Show when animal dies.");
                showWhenDead.setBounds(50, 150, showWhenDead.getPreferredSize().width, showWhenDead.getPreferredSize().height);
                showWhenDead.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        animal.isDead = true;
                        showWhenDead.setText("Updating...");
                    }
                });

                follow = new JButton("Follow animal for n days.");
                follow.setBounds(50, 200, follow.getPreferredSize().width, follow.getPreferredSize().height);
                follow.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        animal.isWatched = true;
                        animal.watchingStartingDay = map.day;
                        int n;
                        if(typeNText.getText().equals(""))
                            n = 0;
                        else
                            n = Integer.parseInt(typeNText.getText());
                        animal.n = n;
                        follow.setText("Big Brother initiated.");
                    }
                });

                popup.setLayout(null);
                popup.add(genotypeLabel);
                popup.add(animalStats);
                popup.add(showWhenDead);
                popup.add(typeNLabel);
                popup.add(typeNText);
                popup.add(follow);
                frame.add(popup);

                frame.setSize(400, 300);
                frame.setVisible(true);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e){}

    @Override
    public void mouseReleased(MouseEvent e){}

    @Override
    public void mouseEntered(MouseEvent e){}

    @Override
    public void mouseExited(MouseEvent e){}
}
