package com.hashvis.model.hashfunc;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * A hash function for string keys. The default expression computes a
 * polynomial hash over the character codes of the string, and uses the key
 * as the array variable {@code s}.
 */
public class HashFunctionString extends HashFunction {
  /**
   * Constructs the hash function with the default polynomial expression and
   * initialises the key argument {@code s} to a single-character array.
   */
  public HashFunctionString() {
    super("sum(map(range(len(s)), {i:s[i] * 256 ** i}))");
    ArrayList<BigInteger> s = new ArrayList<BigInteger>();
    s.add(BigInteger.valueOf('a'));
    codePane.setArgument("s", s);
  }

  /**
   * Converts the key into an {@code ArrayList<BigInteger>} of character codes,
   * evaluates the hash expression, and returns the result modulo the table
   * size.
   *
   * @param key  the string key to hash
   * @param size the number of buckets in the hash table
   * @return an integer hash value in the range {@code [0, size - 1]}
   * @throws RuntimeException if the expression does not return an integer
   */
  @Override
  public int compute(String key, int size) {
    ArrayList<BigInteger> s = new ArrayList<BigInteger>();
    for (int i = 0; i < key.length(); i++)
      s.add(BigInteger.valueOf(key.charAt(i)));
    codePane.setArgument("s", s);
    codePane.setArgument("n", BigInteger.valueOf(size));
    Object result = codePane.eval();
    if (!(result instanceof BigInteger))
      throw new RuntimeException("Hash function must return an integer");
    return ((BigInteger) result).mod(BigInteger.valueOf(size)).intValue();
  }
}
