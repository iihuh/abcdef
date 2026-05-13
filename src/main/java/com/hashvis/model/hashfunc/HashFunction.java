package com.hashvis.model.hashfunc;

import java.math.BigInteger;

import com.hashvis.dsl.model.DSLEngine;

public abstract class HashFunction extends DSLEngine {

  public HashFunction() {
    super(HashFunctionBuiltin.getGlobalSymbolTable());
    symbolTable().set("n", BigInteger.ONE);
  }

  public boolean isValidHashFunction() {
    return evalErr() == null;
  }

  @Override
  protected void checkReturnType(Object result) {
    if (!(result instanceof BigInteger)) {
      throw new RuntimeException("Hash function must return an integer");
    }
  }

  public abstract int compute(String key, int size);
}
