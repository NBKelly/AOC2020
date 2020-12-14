package com.nbkelly.helper;


import java.math.BigInteger;
import java.util.Arrays;;

public class Util {

    //CONVERT ARRAYS TO BIGINTS
    public static BigInteger[] bigIntArray(long[] a) {
	BigInteger[] res  = new BigInteger[a.length];
	for(int i = 0; i < a.length; i++)
	    res[i] = BigInteger.valueOf(a[i]);
	return res;
    }

    public static BigInteger[] bigIntArray(int[] a) {
	BigInteger[] res  = new BigInteger[a.length];
	for(int i = 0; i < a.length; i++)
	    res[i] = BigInteger.valueOf(a[i]);
	return res;
    }


    public static BigInteger chineseRemainder(BigInteger[] nom, BigInteger[] denom) {
	BigInteger product = Arrays.stream(nom).reduce(BigInteger.ONE, (i, j) -> i.multiply(j));

	BigInteger p = BigInteger.ZERO;
	BigInteger sm = BigInteger.ZERO;

	for(int i = 0; i < nom.length; i++) {
	    p = product.divide(nom[i]);
	    sm = sm.add(denom[i].multiply(bat_soup(p, nom[i])).multiply(p));
	}

	return sm.mod(product);
    }

    
    private static BigInteger bat_soup(BigInteger a, BigInteger b) {
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
