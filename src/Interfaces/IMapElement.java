package Interfaces;

import Classes.Vector2d;

public interface IMapElement
{
    Vector2d getPosition();

    boolean canBeEaten();
}
