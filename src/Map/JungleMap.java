package Map;

import Classes.Animal;
import Classes.Grass;
import Classes.Vector2d;
import Interfaces.IMapElement;
import Interfaces.IPositionChangeObserver;
import Interfaces.IWorldMap;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class JungleMap implements IWorldMap, IPositionChangeObserver
{
    public int width;
    public int height;
    public int jungleWidth;
    public int jungleHeight;

    private final Vector2d upperRight;
    private final Vector2d lowerLeft;
    private final Vector2d jungleLowerLeft;
    private final Vector2d jungleUpperRight;

    private final double energyFromGrass;
    private final double moveEnergy;
    private final double initialEnergy;

    //data for stats
    public int day = 0;
    public int currentAnimalQuantity;
    public int currentGrassQuantity;
    public double averageOffspringNumber;
    public double averageEnergy;
    public double averageLifeExpectancy;
    public String dominatingGenotype;

    private List<Grass> grasses = new LinkedList<>();
    private Map<Vector2d, Grass> grassMap = new HashMap<>();
    private List<Animal> animals = new LinkedList<>();
    private Map<Vector2d, LinkedList<Animal>> animalMap = new HashMap<>();

    public JungleMap(int width, int height, double jungleMultiplier, double initialEnergy, double moveEnergy, double energyFromGrass)
    {
        this.width = width;
        this.height = height;

        this.upperRight = new Vector2d(width - 1, height - 1);
        this.lowerLeft = new Vector2d(0 , 0);
        Vector2d[] jungleBounds = calculateJungleBounds(jungleMultiplier);
        this.jungleLowerLeft = jungleBounds[0];
        this.jungleUpperRight = jungleBounds[1];

        this.energyFromGrass = energyFromGrass;
        this.moveEnergy = moveEnergy;
        this.initialEnergy = initialEnergy;
    }

    private Vector2d[] calculateJungleBounds(double jungleMultiplier)
    {
        this.jungleWidth = (int)(jungleMultiplier * this.width);
        this.jungleHeight = (int)(jungleMultiplier * this.height);

        int widthDifference = this.width - this.jungleWidth;
        int heightDifference = this.height - this.jungleHeight;

        Vector2d lowerLeft = new Vector2d(widthDifference / 2, heightDifference / 2);
        Vector2d upperRight = new Vector2d(this.width - (widthDifference / 2) - 1, this.height - (heightDifference / 2) - 1);

        if(widthDifference % 2 == 1)
            upperRight.subtract(new Vector2d(1, 0));
        if(heightDifference % 2 == 1)
            upperRight.subtract(new Vector2d(0, 1));

        return new Vector2d[]{lowerLeft, upperRight};
    }

    public void prepareMapElements(int numberOfAnimals)
    {
        this.spawnAnimalsRandomly(numberOfAnimals);
        this.growRandomGrass();
    }

    public void nextDay()
    {
        this.moveAnimals();
        this.eating();
        this.copulation();
        this.growRandomGrass();
        for(Animal animal : animals)
            animal.daysAlive++;
        this.day++;

        this.dominatingGenotype = getDominatingGenotype();
        this.averageLifeExpectancy = calculateAverageDaysAlive();
        this.averageOffspringNumber = calculateAverageOffspringNumber();
        this.averageEnergy = calculateAverageEnergy();
        this.currentGrassQuantity = grassCounter();
        this.currentAnimalQuantity = animalCounter();

    }

    public Vector2d correctedPosition(Vector2d position)
    {
        int x, y;
        if(position.x < this.lowerLeft.x)
            x = (this.width - Math.abs(position.x % this.width)) % this.width;
        else
            x = Math.abs(position.x % this.width);

        if(position.y < this.lowerLeft.y)
            y = (this.height - Math.abs(position.y % this.height)) % this.height;
        else
            y = Math.abs(position.y % this.height);

        return new Vector2d(x, y);
    }

    public boolean isOccupied(Vector2d position)
    {
        return objectAt(position).isPresent();
    }

    public Optional<IMapElement> objectAt(Vector2d position1)
    {
        Vector2d position = correctedPosition(position1);
        if(animalMap.containsKey(position))
            return Optional.of(animalMap.get(position).getFirst());
        else if(grassMap.containsKey(position))
            return Optional.of(grassMap.get(position));
        return Optional.empty();
    }

    @Override
    public boolean place(IMapElement object)
    {
        Vector2d position = correctedPosition(object.getPosition());
        if(object.canBeEaten())
        {
            if(!isOccupied(position))
            {
                grassMap.put(position, (Grass) object);
                grasses.add((Grass) object);
                return true;
            }
            else
                return false;
        }
        else
        {
                animals.add((Animal) object);
                addToHashMap(position, (Animal) object);
                ((Animal) object).addObserver(this);
                return true;
        }
    }

    public void moveAnimals()
    {
        LinkedList<Animal> newAnimals = new LinkedList<>(this.animals);
        for(Animal animal : newAnimals)
        {
            animal.spin();
            animal.move();
            animal.updateEnergy(-moveEnergy);
        }
    }

    public void eating()
    {
        LinkedList<Grass> newGrasses = new LinkedList<>(this.grasses);
        for(Grass grass : newGrasses)
        {
            Vector2d position = grass.getPosition();
            LinkedList<Animal> stackedAnimals = animalMap.get(position);

            if(stackedAnimals != null && stackedAnimals.size() > 0)
            {
                double currMaxEnergy = 0;
                LinkedList<Animal> strongest = new LinkedList<>();

                for(Animal animal : stackedAnimals)
                {
                    if(animal.getEnergy() > currMaxEnergy)
                    {
                        currMaxEnergy = animal.getEnergy();
                        strongest.clear();
                        strongest.add(animal);
                    }
                    else if(animal.getEnergy() == currMaxEnergy)
                        strongest.add(animal);
                }

                for(Animal animal : strongest)
                    animal.updateEnergy(this.energyFromGrass / strongest.size());

                grasses.remove(grass);
                grassMap.remove(position);
            }
        }
    }

    public void copulation()
    {
        LinkedList<LinkedList<Animal>> hashMapToList = new LinkedList<>(animalMap.values());

        for(LinkedList<Animal> stackedAnimals : hashMapToList)
        {
            if(stackedAnimals != null && stackedAnimals.size() > 1)
            {
                Vector2d offspringPosition = getOffspringPosition(stackedAnimals.getFirst().getPosition());
                double maxEnergy = 0;
                double secondMaxEnergy = -21.37;
                LinkedList<Animal> strongest = new LinkedList<>();
                LinkedList<Animal> secondStrongest = new LinkedList<>();
                int hornyCounter = 0;

                for(Animal animal : stackedAnimals)
                {
                    if(animal.canCopulate())
                    {
                        hornyCounter++;
                        if(animal.getEnergy() > maxEnergy)
                        {
                            secondMaxEnergy = maxEnergy;
                            maxEnergy = animal.getEnergy();
                            secondStrongest.clear();
                            secondStrongest.addAll(strongest);
                            strongest.clear();
                            strongest.add(animal);
                        }
                        else if(animal.getEnergy() > secondMaxEnergy)
                        {
                            secondMaxEnergy = animal.getEnergy();
                            secondStrongest.clear();
                            secondStrongest.add(animal);
                        }
                        else if(animal.getEnergy() == maxEnergy)
                            strongest.add(animal);
                        else if(animal.getEnergy() == secondMaxEnergy)
                            secondStrongest.add(animal);
                    }
                }
                if(hornyCounter < 2)
                    break;
                else if(strongest.size() > 1)
                {
                    Animal parent1 = strongest.get(ThreadLocalRandom.current().nextInt(0, strongest.size()));
                    Animal parent2 = parent1;
                    while(parent1.equals(parent2))
                        parent2 = strongest.get(ThreadLocalRandom.current().nextInt(0, strongest.size()));
                    Animal offspring = parent1.copulate(parent2, offspringPosition);
                    parent1.offspringNumber++;
                    parent2.offspringNumber++;
                    place(offspring);
                }
                else
                {
                    Animal parent1 = strongest.getFirst();
                    Animal parent2 = secondStrongest.get(ThreadLocalRandom.current().nextInt(0, secondStrongest.size()));
                    Animal offspring = parent1.copulate(parent2, offspringPosition);
                    parent1.offspringNumber++;
                    parent2.offspringNumber++;
                    place(offspring);
                }
            }
        }
    }

    private Vector2d getOffspringPosition(Vector2d parentPosition)
    {
        List<Vector2d> emptyFields = new LinkedList<>();
        List<Vector2d> availableFields = new LinkedList<>();
        int xParent = parentPosition.x;
        int yParent = parentPosition.y;

        for(int x = xParent - 1; x < xParent + 2; x++)
            for(int y = yParent - 1; y < yParent + 2; y++)
            {
                Vector2d position = correctedPosition(new Vector2d(x, y));
                if(!position.equals(parentPosition))
                {
                    availableFields.add(position);
                    if(!isOccupied(position))
                        emptyFields.add(position);
                }
            }
        if(emptyFields.size() > 0)
            return emptyFields.get(ThreadLocalRandom.current().nextInt(0, emptyFields.size()));
        else
            return availableFields.get(ThreadLocalRandom.current().nextInt(0, availableFields.size()));
    }

    public void spawnAnimalsRandomly(int n)
    {
        for(int i = 0; i < n; i++)
        {
            Animal animal = new Animal(randomPosition(), this.initialEnergy, this);
            place(animal);
        }
    }

    public boolean isJungle(Vector2d position)
    {
        if(this.jungleLowerLeft.precedes(position) && this.jungleUpperRight.follows(position))
            return true;
        return false;
    }

    public void growRandomGrass()
    {
        LinkedList<Vector2d> emptySteppeFields = new LinkedList<>();
        LinkedList<Vector2d> emptyJungleFields = new LinkedList<>();

        for(int i = 0; i < this.width; i++)
            for(int j = 0; j < this.height; j++)
            {
                Vector2d position = new Vector2d(i, j);
                if(!isOccupied(position))
                    if(isJungle(position))
                        emptyJungleFields.add(position);
                    else
                        emptySteppeFields.add(position);
            }

        if(emptyJungleFields.size() != 0)
            place(new Grass(emptyJungleFields.get(ThreadLocalRandom.current().nextInt(0, emptyJungleFields.size()))));
        if(emptySteppeFields.size() != 0)
            place(new Grass(emptySteppeFields.get(ThreadLocalRandom.current().nextInt(0, emptySteppeFields.size()))));
    }

    private Vector2d randomPosition()
    {
        return new Vector2d(ThreadLocalRandom.current().nextInt(0, this.width), ThreadLocalRandom.current().nextInt(0, this.height));
    }

    private void addToHashMap(Vector2d position1, Animal animal)
    {
        Vector2d position = correctedPosition(position1);
        LinkedList<Animal> stackedAnimals = animalMap.get(position);
        if(stackedAnimals == null)
        {
            stackedAnimals = new LinkedList<>();
            stackedAnimals.add(animal);
            animalMap.put(position, stackedAnimals);
        }
        else
            stackedAnimals.add(animal);
    }

    private void removeFromHashMap(Vector2d position1, Animal animal)
    {
        Vector2d position = correctedPosition(position1);
        if(animalMap.get(position) != null)
        {
            animalMap.get(position).remove(animal);
            if(animalMap.get(position).size() == 0)
                animalMap.remove(position);
        }
        else
            throw new IllegalArgumentException("Field " + position + " is empty.");
    }

    public Map<Vector2d, LinkedList<Animal>> getAnimalMap()
    {
        return this.animalMap;
    }

    public Map<Vector2d, Grass> getGrassMap()
    {
        return this.grassMap;
    }

    @Override
    public void positionChanged(IMapElement object, Vector2d oldPosition1, Vector2d newPosition1)
    {
        Vector2d oldPosition = correctedPosition(oldPosition1);
        Vector2d newPosition = correctedPosition(newPosition1);

        removeFromHashMap(oldPosition, (Animal) object);
        addToHashMap(newPosition, (Animal) object);
    }

    @Override
    public void animalIsDead(Animal animal)
    {
        animals.remove(animal);
        removeFromHashMap(animal.getPosition(), animal);
        //animal.isDead = true;
        animal.removeObserver(this);
    }

    public Vector2d getJungleLowerLeft()
    {
        return this.jungleLowerLeft;
    }

    public Vector2d getJungleUpperRight()
    {
        return this.jungleUpperRight;
    }

    public List<Grass> getGrasses()
    {
        return this.grasses;
    }

    public int animalCounter()
    {
        return animals.size();
    }

    public int grassCounter()
    {
        return grasses.size();
    }

    public double calculateAverageOffspringNumber()
    {
        double temp = 0;
        for(Animal animal : animals)
            temp += animal.offspringNumber;
        return temp / (double) animalCounter();
    }

    public double calculateAverageEnergy()
    {
        double temp = 0;
        for(Animal animal : animals)
            temp += animal.getEnergy();
        return temp / (double) animalCounter();
    }

    public double calculateAverageDaysAlive()
    {
        double temp = 0;
        for(Animal animal : animals)
            temp += animal.daysAlive;
        return temp / (double) animalCounter();
    }

    public String getDominatingGenotype()
    {
        int max = 0;
        HashMap<String, Integer> geneMap = new HashMap<>();

        for(Animal animal : animals)
        {
            String temp = animal.getGenes().toString();
            if(geneMap.containsKey(temp))
                geneMap.replace(temp, geneMap.get(temp) + 1);
            else
                geneMap.put(temp, 1);
        }

        String dominatingGenotype = "";
        for(String s : geneMap.keySet())
            if(geneMap.get(s) > max)
            {
                dominatingGenotype = s;
                max = geneMap.get(s);
            }
        return dominatingGenotype;
    }
}
