package com.nbkelly.helper;

/**
 * @author      NB Kelly <nbkelly @ protonmail.com>
 * @version     1.2
 * @since       1.1
 */
public final class Pair<T, U> {
    public final T X;
    public final U Y;
    
    public Pair(T X, U Y) {
	this.X = X;
	this.Y = Y;
    }

    public Pair<Integer, Integer> add(Pair<Integer, Integer> a, Pair<Integer, Integer> b) {
	return new Pair<Integer, Integer>(a.X + b.X, a.Y + b.Y);
    }
}
