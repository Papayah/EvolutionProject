package EnumClasses;

import Classes.Vector2d;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public enum MapDirection
{
    N,
    NE,
    E,
    SE,
    S,
    SW,
    W,
    NW;

    private static final List<MapDirection> Values = List.of(values());
    private static final int size = Values.size();


    @Override
    public String toString()
    {
        return switch(this)
        {
            case N -> "North";
            case NE -> "North-East";
            case E -> "East";
            case SE -> "South-East";
            case S -> "South";
            case SW -> "South-West";
            case W -> "West";
            case NW -> "North-West";
        };
    }

    public MapDirection next()
    {
        return switch(this)
        {
            case N -> NE;
            case NE -> E;
            case E -> SE;
            case SE -> S;
            case S -> SW;
            case SW -> W;
            case W -> NW;
            case NW -> N;
        };
    }

    public MapDirection multipleNext(int n)
    {
        MapDirection orientation = this;

        for(int i = 0; i < n; i++)
            orientation = orientation.next();

        return orientation;
    }

    public MapDirection previous()
    {
        return switch(this)
        {
            case N -> NW;
            case NE -> N;
            case E -> NE;
            case SE -> E;
            case S -> SE;
            case SW -> S;
            case W -> SW;
            case NW -> W;
        };
    }

    public Vector2d toVector2d()
    {
        return switch (this)
        {
            case N -> new Vector2d(0, 1);
            case NE -> new Vector2d(1, 1);
            case E -> new Vector2d(1, 0);
            case SE -> new Vector2d(1, -1);
            case S -> new Vector2d(0, -1);
            case SW -> new Vector2d(-1, -1);
            case W -> new Vector2d(-1, 0);
            case NW -> new Vector2d(-1, 1);
        };
    }

    public static MapDirection getRandomDirection()
    {
        return Values.get(ThreadLocalRandom.current().nextInt(0, size));
    }

//    public MapDirection multiplePrevious(int n)
//    {
//        MapDirection orientation = this;
//
//        for(int i = 0; i < n; i++)
//            orientation = orientation.previous();
//
//        return orientation;
//    }
}
