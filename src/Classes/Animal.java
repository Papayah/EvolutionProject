package Classes;

import EnumClasses.MapDirection;
import Interfaces.IMapElement;
import Interfaces.IPositionChangeObserver;
import Interfaces.IWorldMap;

import java.util.LinkedList;
import java.util.List;

public class Animal extends AbstractMapElement
{
    private MapDirection orientation;
    private double energy;
    private final double initialEnergy;
    public Genes genes;

    public int offspringNumber = 0;
    public int daysAlive = 0;

    public boolean isDead = false;
    public boolean isWatched = false;
    public int watchingStartingDay;
    public int n;


    private final IWorldMap map;
    private final List<IPositionChangeObserver> observers = new LinkedList<>();

    // Adam and Eve constructor
    public Animal(Vector2d position, double initialEnergy, IWorldMap map)
    {
        this.position = position;
        this.orientation = MapDirection.getRandomDirection();
        this.initialEnergy = initialEnergy;
        this.energy = initialEnergy;
        this.genes = new Genes();
        this.map = map;
    }

    // offspring constructor
    public Animal(Vector2d position, double initialEnergy, Genes genes1, Genes genes2, IWorldMap map)
    {
        this.position = position;
        this.orientation = MapDirection.getRandomDirection();
        this.initialEnergy = initialEnergy;
        this.energy = initialEnergy;
        this.genes = new Genes(genes1, genes2);
        this.map = map;
    }

    public void updateEnergy(double energy)
    {
        this.energy += energy;
        if(this.energy <= 0)
            for(IPositionChangeObserver observer : observers)
                observer.animalIsDead(this);
    }

    public Vector2d getPosition()
    {
        return this.position;
    }

    public boolean canBeEaten()
    {
        return false;
    }

    public void move()
    {
        Vector2d oldPosition = this.position;
        this.position = this.position.add(this.orientation.toVector2d());
        this.positionChanged(this, oldPosition, this.position);
    }

    public void spin()
    {
        int gene = this.genes.getRandomGene();
        this.orientation = this.orientation.multipleNext(gene);
    }

    public Animal copulate(Animal matingPartner, Vector2d offspringPosition)
    {
        double offspringEnergy = (double) (int) (0.25 * this.energy) + (int) (0.25 * matingPartner.energy);
        this.updateEnergy(-(0.25 * this.energy));
        matingPartner.updateEnergy(-(0.25 * matingPartner.energy));
        return new Animal(offspringPosition, offspringEnergy, this.genes, matingPartner.genes, map);
    }

    public double getEnergy()
    {
        return this.energy;
    }

    public boolean canCopulate()
    {
        return this.energy >= this.initialEnergy / 2;
    }

    public void addObserver(IPositionChangeObserver observer)
    {
        observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer)
    {
        observers.remove(observer);
    }

    public void positionChanged(IMapElement object, Vector2d oldPosition, Vector2d newPosition)
    {
        this.daysAlive++;
        for(IPositionChangeObserver observer : observers)
            observer.positionChanged(object, oldPosition, newPosition);
    }

    public int getEnergyLevel()
    {
        if(this.energy < 0.33 * this.initialEnergy)
            return 0;
        else if(this.energy < 0.66 * this.initialEnergy)
            return 1;
        else
            return 2;
    }

    public Genes getGenes()
    {
        return this.genes;
    }

    @Override
    public String toString()
    {
        return "Animal:" +
                "\nPosition: " + position +
                "\nOrientation:" + orientation +
                "\nEnergy: " + energy +
                '\n';
    }
}
