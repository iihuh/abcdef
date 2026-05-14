package com.hashvis.model.hashfunc;

import java.math.BigInteger;
import java.util.ArrayList;

public class HashFunctionString extends HashFunction {
  public HashFunctionString() {
    super("len(s) % n");
    ArrayList<BigInteger> s = new ArrayList<BigInteger>();
    s.add(BigInteger.valueOf('a'));
    symbolTable.set("s", s);
  }

  @Override
  public int compute(String key, int size) {
    ArrayList<BigInteger> s = new ArrayList<BigInteger>();
    for (int i = 0; i < key.length(); i++)
      s.add(BigInteger.valueOf(key.charAt(i)));
    symbolTable.set("s", s);
    symbolTable.set("n", BigInteger.valueOf(size));
    Object result = eval();
    if (!(result instanceof BigInteger))
      throw new RuntimeException("Hash function must return an integer");
    return ((BigInteger) result).mod(BigInteger.valueOf(size)).intValue();
  }
}
