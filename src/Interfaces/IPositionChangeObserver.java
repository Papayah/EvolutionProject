package Interfaces;

import Classes.Animal;
import Classes.Vector2d;

public interface IPositionChangeObserver
{
    void positionChanged(IMapElement object, Vector2d oldPosition, Vector2d newPosition);   // czy potrzebujemy przekazywać nową pozycję, skoro przekazujemy cały element?

    void animalIsDead(Animal animal);   // jak się to ma to nazwy interfejsu?
}
