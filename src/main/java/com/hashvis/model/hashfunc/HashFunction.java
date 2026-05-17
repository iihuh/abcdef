package com.hashvis.model.hashfunc;

import java.math.BigInteger;
import com.codepane.CodePane;

public abstract class HashFunction extends CodePane {

  public HashFunction(String code) {
    super(HashFunctionBuiltin.getGlobalSymbolTable(), code, false);
    symbolTable.set("n", BigInteger.ONE);
  }

  public boolean isValidHashFunction() {
    return evalException == null;
  }

  @Override
  protected void validateResultType(Object obj) {
    if (!(obj instanceof BigInteger)) {
      throw new RuntimeException("Hash function must return an integer");
    }

  }

  public abstract int compute(String key, int size);
}
