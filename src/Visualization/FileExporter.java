package Visualization;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileExporter
{
    private String[] data;
    static int numberOfFiles = 0;

    public FileExporter(String [] data)
    {
        this.data = data;
    }

    public FileExporter(){};

    public void exportStatsToFile() throws IOException
    {
        String fileName = "stats" + numberOfFiles;
        File newFile = new File(fileName);

        if(newFile.createNewFile())
        {
            System.out.println("New file created. (" + fileName + ")");
        }
        else
        {
            numberOfFiles++;
            fileName = "stats" + numberOfFiles;
            newFile = new File(fileName);
            newFile.createNewFile();
        }

        FileWriter writer = new FileWriter(fileName);

        writer.write("Statistics for day " + data[0] + ":\n");
        writer.write("   Average number of Animals: " + data[1] + "\n");
        writer.write("   Average number of Grasses: " + data[2] + "\n");
        writer.write("   Average number of offsprings: " + data[3] + "\n");
        writer.write("   Average animals' energy: " + data[4] + "\n");
        writer.write("   Currently dominating genotype: " + data[5] + "\n");
        writer.write("   Average animal life expectancy: " + data[6] + "\n");

        writer.close();
    }

    public void updateData(String [] data)
    {
        this.data = data;
    }
}
