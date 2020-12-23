import Classes.*;
import Map.JungleMap;
import Visualization.*;
import Visualization.JSONLoader;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class World
{
    public static void main(String [] args){

        try{

            JSONLoader loader = new JSONLoader();
            loader.loadParameters();
            JungleMap map = new JungleMap(loader.width, loader.height, loader.jungleMultiplier, loader.initialEnergy, loader.moveEnergy, loader.energyFromGrass);

            ParametersMenu menu = new ParametersMenu();
            menu.setParameters(loader);


        }catch (IllegalArgumentException exception){
            System.out.println(exception);
            System.exit(1);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}