package com.hashvis.model.hashfunc;

import java.math.BigInteger;
import com.codepane.CodePane;

/**
 * Abstract base class for hash functions. Subclasses define how a key is
 * converted into a hash value, using a user-editable code pane for the hash
 * body.
 */
public abstract class HashFunction {
  /** The code pane containing the hash function's editable expression. */
  protected CodePane codePane;

  /**
   * Constructs a hash function with the given default code expression.
   *
   * @param code the initial hash function expression as a string
   */
  public HashFunction(String code) {
    codePane = new CodePane(GlobalSymbolTable.getGlobalSymbolTable(), code, false);
    codePane.setArgument("n", BigInteger.ONE);
    codePane.setValidator(obj -> {
      if (!(obj instanceof BigInteger))
        throw new RuntimeException("Hash function must return an integer");
    });
  }

  /**
   * Returns the visual component of the code pane for rendering in the UI.
   *
   * @return the initialised view part of the code pane
   */
  public CodePane getView() {
    return codePane.initViewPart();
  }

  /**
   * Checks whether the current hash function expression is syntactically and
   * semantically valid.
   *
   * @return {@code true} if the code pane contains a valid expression,
   *         {@code false} otherwise
   */
  public boolean isValidHashFunction() {
    return codePane.isVaildHashFunction();
  }

  /**
   * Computes the hash of the given key for a table of the given size.
   *
   * @param key  the key to hash
   * @param size the number of buckets in the hash table
   * @return an integer hash value in the range {@code [0, size - 1]}
   */
  public abstract int compute(String key, int size);
}
