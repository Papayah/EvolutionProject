package Interfaces;

import Classes.Animal;
import Classes.Vector2d;

public interface IPositionChangeObserver
{
    void positionChanged(IMapElement object, Vector2d oldPosition, Vector2d newPosition);

    void animalIsDead(Animal animal);
}
