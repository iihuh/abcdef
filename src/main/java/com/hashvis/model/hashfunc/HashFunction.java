package com.hashvis.model.hashfunc;

import java.math.BigInteger;
import com.codepane.CodePane;

public abstract class HashFunction {
  protected CodePane codePane;

  public HashFunction(String code) {
    codePane = new CodePane(GlobalSymbolTable.getGlobalSymbolTable(), code, false);
    codePane.setArgument("n", BigInteger.ONE);
    codePane.setValidator(obj -> {
      if (!(obj instanceof BigInteger))
        throw new RuntimeException("Hash function must return an integer");
    });
  }

  public CodePane getView() {
    return codePane.initViewPart();
  }

  public boolean isValidHashFunction() {
    return codePane.isValid();
  }

  public abstract int compute(String key, int size);
}
