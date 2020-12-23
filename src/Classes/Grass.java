package Classes;

import Classes.AbstractMapElement;

public class Grass extends AbstractMapElement
{
    final private Vector2d position;

    public Grass(Vector2d position)
    {
        this.position = position;
    }

    public Vector2d getPosition()
    {
        return this.position;
    }

    @Override
    public boolean canBeEaten()
    {
        return true;
    }

    @Override
    public String toString()
    {
        return "*";
    }
}
