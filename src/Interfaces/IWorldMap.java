package Interfaces;

import Classes.Animal;
import Classes.Vector2d;

import java.util.Vector;

public interface IWorldMap
{
    //boolean canMoveTo(Vector2d position);

    boolean place(IMapElement object);

    boolean isOccupied(Vector2d position);

    Object objectAt(Vector2d position);

    //Vector2d correctedPosition(Vector2d position);
}
