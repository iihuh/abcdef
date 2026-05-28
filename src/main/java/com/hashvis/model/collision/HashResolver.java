package com.hashvis.model.collision;

import java.util.ArrayList;
import java.util.List;
import com.hashvis.model.hashfunc.*;
import com.hashvis.model.table.*;

/**
 * Abstract base class for collision resolvers. Provides common state and
 * helper methods shared by all collision resolution strategies, including
 * hash function initialization and resolver configuration.
 */
public abstract class HashResolver implements CollisionResolver {

  /** The hash function used to compute bucket indices. */
  protected HashFunction hashFunc;

  /** The current hash table action being visualized. */
  protected HashAction action;
  /** The key involved in the current operation. */
  protected String key;
  /** The hash table on which the operation is performed. */
  protected Table table;

  /**
   * Initializes the common resolver state for a visualization run.
   *
   * @param action the hash table action to visualize
   * @param key    the key involved in the operation
   * @param table  the hash table on which the operation is performed
   */
  protected void initializeHashResolver(HashAction action, String key, Table table) {
    this.action = action;
    this.key = key;
    this.table = table;
  }

  /**
   * Creates a hash function appropriate for the given data type.
   *
   * @param type the type of key data
   * @return a new hash function instance for the specified type
   */
  protected HashFunction initializeHashFunction(DataType type) {
    switch (type) {
      case INTEGER:
        return new HashFunctionNumber();
      case STRING:
        return new HashFunctionString();
      default:
        throw new IllegalArgumentException("Unsupported data type: " + type);
    }
  }

  @Override
  public List<HashFunction> getHashFunctionFields(DataType dataType) {
    ArrayList<HashFunction> result = new ArrayList<HashFunction>();
    result.add(initializeHashFunction(dataType));
    return result;
  }

  @Override
  public void setHashFunctionFields(List<HashFunction> hashFunctions) {
    hashFunc = hashFunctions.get(0);
  }
}
