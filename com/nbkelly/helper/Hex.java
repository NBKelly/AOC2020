package com.nbkelly.helper;

import com.nbkelly.helper.Pair;

/**
 * @author      NB Kelly <nbkelly @ protonmail.com>
 * @version     1.2
 * @since       1.2
 */
public final class Hex {
    public final Pair<Integer, Integer> NORTH = new Pair<Integer, Integer>(0, 2);
    public final Pair<Integer, Integer> SOUTH = new Pair<Integer, Integer>(0, -2);

    public final Pair<Integer, Integer> NORTH_EAST = new Pair<Integer, Integer>(1, 1);
    public final Pair<Integer, Integer> NORTH_WEST = new Pair<Integer, Integer>(-1, 1);

    public final Pair<Integer, Integer> SOUTH_EAST = new Pair<Integer, Integer>(1, -1);
    public final Pair<Integer, Integer> SOUTH_WEST = new Pair<Integer, Integer>(-1, -1);
    
    public final Pair<Integer, Integer> EAST = new Pair<Integer, Integer>(2, 0);
    public final Pair<Integer, Integer> WEST = new Pair<Integer, Integer>(-2, 0);
}
