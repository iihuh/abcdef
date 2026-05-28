package com.hashvis.model.hashfunc;

import java.math.BigInteger;

/**
 * A hash function for numeric keys. The default expression computes
 * {@code k % n} and uses the key as the integer variable {@code k}.
 */
public class HashFunctionNumber extends HashFunction {
  /**
   * Constructs the hash function with the default expression {@code k % n} and
   * initialises the key argument {@code k} to zero.
   */
  public HashFunctionNumber() {
    super("k % n");
    codePane.setArgument("k", BigInteger.ZERO);
  }

  /**
   * Parses the key as a {@link BigInteger}, evaluates the hash expression, and
   * returns the result modulo the table size.
   *
   * @param key  the numeric key as a string
   * @param size the number of buckets in the hash table
   * @return an integer hash value in the range {@code [0, size - 1]}
   * @throws RuntimeException if the key is not a valid integer or the
   *                          expression does not return an integer
   */
  @Override
  public int compute(String key, int size) {
    BigInteger k = null;
    try {
      k = new BigInteger(key);
    } catch (NumberFormatException e) {
      throw new RuntimeException("Invalid key: " + key);
    }
    codePane.setArgument("k", k);
    codePane.setArgument("n", BigInteger.valueOf(size));
    Object result = codePane.eval();
    if (!(result instanceof BigInteger))
      throw new RuntimeException("Hash function must return an integer");
    return ((BigInteger) result).mod(BigInteger.valueOf(size)).intValue();
  }
}
