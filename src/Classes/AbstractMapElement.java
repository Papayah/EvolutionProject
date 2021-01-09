package Classes;    // jeśli ten pakiet zawiera klasy, to co zawierają pozostałe?

import Interfaces.IMapElement;

public abstract class AbstractMapElement implements IMapElement
{
    protected Vector2d position;

    @Override
    public Vector2d getPosition()
    {
        return this.position;
    }
}
