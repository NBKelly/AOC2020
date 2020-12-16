package com.nbkelly.helper;


import java.math.BigInteger;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.ArrayList;
/**
 * @author      NB Kelly <nbkelly @ protonmail.com>
 * @version     1.0
 * @since       1.0
 */
public final class Util {
    /**
     * Sets the bit at a given position of an int
     * @param val value to modify
     * @param index index (0=rightmost) of the bit to set
     * @param active true to set bit to 1, false to set to 0
     * @return A copy of val with the one bit changed
     * @since 1.0
     */
    public static final long setBit(long val, int index, boolean active) {
	long bit = (1l << index);
	if(active)
	    return val | bit;
	else
	    return val & ~bit;
    }

    /**
     * Sets the bit at a given position of an int
     * @param val value to modify
     * @param index index (0=rightmost) of the bit to set
     * @param active true to set bit to 1, false to set to 0
     * @return A copy of val with the one bit changed
     * @since 1.0
     */
    public static final int setBit(int val, int index, boolean active) {
	int bit = (1 << index);
	if(active)
	    return val | bit;
	else
	    return val & ~bit;
    }

    /**
     * Reduces a matrix into an identity, if possible. Gives all values which can be singly determined.
     *
     * @param values The values to reduce.
     * @return A list, T, containing (in index) all elements which can be ordered
     * @since 1.0
     */
    public static final <T> ArrayList<T> singleElim(ArrayList<ArrayList<T>> values) {
	ArrayList<T> t = new ArrayList<T>(values.size());

	ArrayList<ArrayList<T>> re = new ArrayList<>();
	for(var v : values)
	    re.add(new ArrayList(v));
	
	for(int i = 0; i < values.size(); i++)
	    t.add(null);
	
	while(true) {
	    T singleton = null;
	    for(int i = 0; i < values.size(); i++) {
		if(values.get(i).size() == 1) {
		    singleton = values.get(i).get(0);
		    t.set(i, values.get(i).get(0));
		    break;
		}
	    }

	    if(singleton == null)
		break;
	    
	    for(var li : values)
		li.remove(singleton);
	}

	values.clear();
	
	for(var v : re)
	    values.add(v);

	return t;
    }
    
    /**
     * Helper interface for combinations function. Given two T, give back a T
     * @author      NB Kelly <nbkelly @ protonmail.com>
     * @version     1.0
     * @since       1.0
     */
    public interface Combinator<T> {
	TreeSet<T> combinations(T val, T component);
    }


    /**
     * Gets all combinations/permutations of a list, based on a combinator and seed value
     *
     * @param seed seed value to base permutations on
     * @param components components to permute
     * @param combinator anonymous class that generates permutations based on a value and component
     * @return TreeSet T of permutations
     * @since 1.0
     */
    public static final <T> TreeSet<T> combinations(T seed, ArrayList<T> components, Combinator<T> combinator) {
	TreeSet<T> vals = new TreeSet<T>();
	vals.add(seed);
	
	for(T component : components) {
	    TreeSet<T> res = new TreeSet<T>();
	    for(T v : vals)
		res.addAll(combinator.combinations(v, component));
	    vals.addAll(res);
	}

	return vals;
    }
    
    /**
     * Converts long[] to BigInteger[]
     *
     * @param a array to convert
     * @return BigInteger[] of input
     * @since 1.0
     */
    public static final BigInteger[] bigIntArray(long[] a) {
	BigInteger[] res  = new BigInteger[a.length];
	for(int i = 0; i < a.length; i++)
	    res[i] = BigInteger.valueOf(a[i]);
	return res;
    }

    /**
     * Converts int[] to BigInteger[]
     *
     * @param a array to convert
     * @return BigInteger[] of input
     * @since 1.0
     */
    public static final BigInteger[] bigIntArray(int[] a) {
	BigInteger[] res  = new BigInteger[a.length];
	for(int i = 0; i < a.length; i++)
	    res[i] = BigInteger.valueOf(a[i]);
	return res;
    }

    /**
     * Gets the answer to chineseRemainder on input
     *
     * <p>Specifically, finds K such that nom % k = denom
     *
     * @param nom argument numbers
     * @param denom expected denominators
     * @return K such that nom % k = denom for all nom-denom
     * @since 1.0
     */
    public static final BigInteger chineseRemainder(BigInteger[] nom, BigInteger[] denom) {
	BigInteger product = Arrays.stream(nom).reduce(BigInteger.ONE, (i, j) -> i.multiply(j));

	BigInteger p = BigInteger.ZERO;
	BigInteger sm = BigInteger.ZERO;

	for(int i = 0; i < nom.length; i++) {
	    p = product.divide(nom[i]);
	    sm = sm.add(denom[i].multiply(bat_soup(p, nom[i])).multiply(p));
	}

	return sm.mod(product);
    }

    /**
     * Used by chinese remainder
     * @since 1.0
     */
    private static final BigInteger bat_soup(BigInteger a, BigInteger b) {
	BigInteger b0 = b;
	BigInteger x0 = BigInteger.ZERO;
	BigInteger x1 = BigInteger.ONE;

	if(b.equals(BigInteger.ONE))
	    return b;

	while (a.compareTo(BigInteger.ONE) > 0) {
	    BigInteger q = a.divide(b);

	    BigInteger tmp = a.mod(b);
	    a = b;
	    b = tmp;

	    tmp = x1.subtract(q.multiply(x0));
	    x1 = x0;
	    x0 = tmp;
	}

	if(x1.compareTo(BigInteger.ZERO) < 0)
	    x1 = x1.add(b0);

	return x1;
    }
}
