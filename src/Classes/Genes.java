package Classes;

import EnumClasses.MapDirection;

import java.util.Random;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Genes
{
    private static final int N = 32;    // N to liczba wszystkich genów, czy dopuszczalnych wartości genu?

    private final int [] genesArray;

    public Genes()
    {
        this.genesArray = new Random().ints(32, 0, 8).toArray();    // czemu 32?
        this.addMissing();
    }

    public Genes(Genes parent1, Genes parent2)
    {
        this.genesArray = new int[N];
        this.generateOffspringGenes(parent1, parent2);
        this.addMissing();
    }

    public void addMissing()
    {
        Arrays.sort(this.genesArray);

        boolean fix = true;
        while(fix)
        {
            fix = false;
            boolean[] notFound = new boolean[8];
            Arrays.fill(notFound, true);

            for(int gene : this.genesArray)
            {
                notFound[gene] = false;
            }

            for(int i = 0; i < 8; i++)
            {
                if(notFound[i])
                {
                    fix = true;
                    this.genesArray[ThreadLocalRandom.current().nextInt(0, N)] = i;
                }
            }
        }
        Arrays.sort(this.genesArray);
    }

    private void generateOffspringGenes(Genes parent1, Genes parent2)
    {
        int point1 = ThreadLocalRandom.current().nextInt(1, N - 1);
        int point2 = ThreadLocalRandom.current().nextInt(1, N - 1);
        while(point1 == point2) // do-while
            point2 = ThreadLocalRandom.current().nextInt(1, N - 1);

        if(point1 > point2)
        {
            int temp = point1;
            point1 = point2;
            point2 = temp;
        }

        int [] points = {0, point1, point2, point1, point2, N};
        int counter1 = 0;
        int counter2 = 0;


        for(int i = 0; i < 3; i++)
        {
            int rand = ThreadLocalRandom.current().nextInt(0, 2);
            if(rand == 0 || counter2 >= 2)
            {
                if(counter1 < 2)
                {
                    counter1 += 1;
                    System.arraycopy(parent1.getGenesArray(), points[i], this.genesArray, points[i], points[i + 3] - points[i]);
                }
            }

            if(rand == 1 || counter1 >= 2)
            {
                counter2 += 1;
                System.arraycopy(parent2.getGenesArray(), points[i], this.genesArray, points[i], points[i + 3] - points[i]);
            }
        }
    }

    public int[] getGenesArray()
    {
        return this.genesArray; // a tablica nie jest przypadkiem modyfikowalna?
    }

    public int getRandomGene()
    {
        return genesArray[ThreadLocalRandom.current().nextInt(0, N)];
    }


    @Override
    public String toString()
    {
        return Arrays.toString(this.genesArray);
    }

}
