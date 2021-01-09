package Visualization;

import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class JSONLoader // czy to na pewno element wizualizacji?
{
    public int width;
    public int height;
    public int initialEnergy;
    public int moveEnergy;
    public double jungleMultiplier;
    public int numberOfAnimals;
    public int energyFromGrass;

    public JSONLoader(){};

    public void loadParameters() throws IOException, ParseException, FileNotFoundException
    {
        Object parser = new JSONParser().parse(new FileReader("src/parameters.json"));
        JSONObject params = (JSONObject) parser;
        this.width = Integer.parseInt(Long.toString((long) params.get("width")));
        this.height = Integer.parseInt(Long.toString((long) params.get("height")));
        this.initialEnergy = Integer.parseInt(Long.toString((long) params.get("initialEnergy")));
        this.moveEnergy = Integer.parseInt(Long.toString((long) params.get("moveEnergy")));
        this.jungleMultiplier = Double.parseDouble(Double.toString((double) params.get("jungleMultiplier")));
        this.numberOfAnimals = Integer.parseInt(Long.toString((long) params.get("numberOfAnimals")));
        this.energyFromGrass = Integer.parseInt(Long.toString((long) params.get("energyFromGrass")));
    }
}
