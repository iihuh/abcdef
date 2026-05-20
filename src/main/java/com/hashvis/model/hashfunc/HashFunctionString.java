package com.hashvis.model.hashfunc;

import java.math.BigInteger;
import java.util.ArrayList;

public class HashFunctionString extends HashFunction {
  public HashFunctionString() {
    super("sum(map(range(len(s)), {i:s[i] * 256 ** i}))");
    ArrayList<BigInteger> s = new ArrayList<BigInteger>();
    s.add(BigInteger.valueOf('a'));
    codePane.setArgument("s", s);
  }

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
