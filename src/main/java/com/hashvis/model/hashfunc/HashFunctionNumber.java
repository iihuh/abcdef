package com.hashvis.model.hashfunc;

import java.math.BigInteger;

public class HashFunctionNumber extends HashFunction {
  public HashFunctionNumber() {
    super("k % n");
    symbolTable.set("k", BigInteger.ZERO);
  }

  @Override
  public int compute(String key, int size) {
    BigInteger k = null;
    try {
      k = new BigInteger(key);
    } catch (NumberFormatException e) {
      throw new RuntimeException("Invalid key: " + key);
    }
    symbolTable.set("k", k);
    symbolTable.set("n", BigInteger.valueOf(size));
    Object result = eval();
    if (!(result instanceof BigInteger))
      throw new RuntimeException("Hash function must return an integer");
    return ((BigInteger) result).mod(BigInteger.valueOf(size)).intValue();
  }
}
