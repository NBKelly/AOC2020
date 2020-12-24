package com.nbkelly.helper;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;


/**
 * @author      NB Kelly <nbkelly @ protonmail.com>
 * @version     1.2
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
     * Compares features and returns an integer value.
     * @param params A sequence of parameters to compare, of the form [type(a), type(a)]*
     * @return The first non-zero comparison in the series
     * @since 1.2
     */
    @SuppressWarnings("unchecked")
    public static final int compareTo(Comparable... params) {
	//comparing nothing will always give back no
	if(params.length == 0)
	    return 0;

	if(params.length %2 == 1)
	    throw new IllegalArgumentException("Util.compareTo(params): [comparableTypeA, comparableTypeA]"
					       + " (param count must be an even number (01))");

	int res = 0;
	for(int i = 0; i < params.length; i+= 2) {
	    try {
		//how do we deal with null values?
		//if only the first item is null, we can try (-1 * (+1.compareTo(0)))
		if(params[i] == null)
		    res = -1 * params[i+1].compareTo(params[i]);
		else
		    res = params[i].compareTo(params[i+1]);
		
		if(res != 0)
		    return res;
	    } catch (Exception e) {
		String c1 = (params[i] == null ? "NULL" : params[i].getClass().toString());
		String c2 = (params[i+1] == null ? "NULL" : params[i+1].getClass().toString());
		//if the comparison is invalid, we just need to let it be known
		String exc = String.format("Failed comparison at index (%d/+1), due to an error comparing types"
					   + " '%s' and '%s'. Was the input well formed? Given exception read as:"
					   + " '%s'", i, c1, c2);
		throw new IllegalArgumentException(exc);
	    }
	}
	return res;
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
	    re.add(new ArrayList<T>(v));
	
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
     * List[BigInteger] to BigInteger[]
     *
     * @param a list to convert
     * @return BigInteger[] of input
     * @since 1.1
     */
    public static final BigInteger[] bigIntArray(List<BigInteger> a) {
	BigInteger[] res  = new BigInteger[a.size()];
	int counter = 0;
	for(BigInteger b : a) {
	    res[counter++] = b;
	}

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
     * Finds the 2-sum of a sequence of numbers, if it exists.
     * <p>
     * Finds the 2-sum of a sequence of numbers, if it exists. The 2-sum is two numbers in the list
     * which sum up to the given target. 
     *
     * @param values the values for which to find a 2sum
     * @param target the target sum to find
     * @return Pair(T) containing the first two unique values which to the target, or null
     * @since 1.1
     */
    @SuppressWarnings("unchecked")
    public static final <T extends Number> Pair<T, T> twoSum(T[] values, T target) {
	//assertion: each value in values is unique
	HashSet<T> hashed_values = new HashSet<T>();

	
	for(int index = 0; index < values.length; index++)
	    hashed_values.add(values[index]);

	checking:
	for(int index = 0; index < values.length; index++) {
	    //if hashed_values contains target - value[index], then value[index] + value are the right targets
	    var check_target = add(target, neg(values[index]));
	    if(hashed_values.contains(check_target)) {
		//check that values[index] != check_target
		if(check_target.equals(values[index]))
		    continue checking;

		//assert check_target.getClass() == T.class;
		return new Pair<T, T>(values[index], (T)check_target);
	    }
	}


	//what are the requrements for 2sum? we need to find that target - valueA = valueB
	return null;
    }

    /**
     * Returns Number * -1 for generic classes of type Number
     *
     * @param a number to multiply by -1
     * @return a * -1
     * @since 1.1
     */
    private static final <T extends Number> Number neg(T a)
	throws IllegalArgumentException {
	if(a.getClass() == Short.class)
	    return (Short)a *(Short)(short)(-1);// (Short)b;
	else if(a.getClass() == Long.class)
	    return (Long)a * -1l;
	else if(a.getClass() == Integer.class)
	    return (Integer)a * -1;
	else if(a.getClass() == Float.class)
	    return (Float)a * -1f;
	else if(a.getClass() == Double.class)
	    return (Double)a *1d;
	else if(a.getClass() == Byte.class)
	    return (Byte)a * (Byte)(byte)-1;
	else if(a.getClass() == BigInteger.class)
	    return ((BigInteger)a).multiply(BigInteger.valueOf(-1));
	else if(a.getClass() == BigDecimal.class)
	    return ((BigDecimal)a).multiply(BigDecimal.valueOf(-1));

	throw new IllegalArgumentException("Operands of type " + a.getClass() + " are not supported");
	//	return null;

    }

    /**
     * Returns NumberA * NumberB for generic classes of type Number
     *
     * @param a number to add
     * @param b number to add
     * @return a +b
     * @since 1.1
     */
    private static final <T extends Number> Number add(T a, T b)
	throws IllegalArgumentException  {
	assert a.getClass() == b.getClass(): "Type mismatch with generic add(a, b)";; //I think this is mandatory?

	if(a.getClass() == Short.class)
	    return (Short)a + (Short)b;
	else if(a.getClass() == Long.class)
	    return (Long)a + (Long)b;
	else if(a.getClass() == Integer.class)
	    return (Integer)a + (Integer)b;
	else if(a.getClass() == Float.class)
	    return (Float)a + (Float)b;
	else if(a.getClass() == Double.class)
	    return (Double)a + (Double)b;
	else if(a.getClass() == Byte.class)
	    return (Byte)a + (Byte)b;
	else if(a.getClass() == BigInteger.class)
	    return ((BigInteger)a).add((BigInteger)b);
	else if(a.getClass() == BigDecimal.class)
	    return ((BigDecimal)a).add((BigDecimal)b);

	throw new IllegalArgumentException("Operands of type " + a.getClass() + " are not supported");
	//	return null;
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
