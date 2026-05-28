package com.hashvis.model.collision;

import java.util.List;

import com.hashvis.model.hashfunc.HashFunction;
import com.hashvis.model.table.Table;

/**
 * Defines the contract for collision resolution strategies in a hash table.
 * Implementations provide step-by-step visualization of the resolution process,
 * including pseudocode generation and per-step result reporting.
 */
public interface CollisionResolver {

  /**
   * The type of hash table operation to visualize.
   */
  public enum HashAction {
    INSERT, SEARCH, DELETE
  }

  /**
   * The type of data used for hash table keys.
   */
  public enum DataType {
    STRING, INTEGER
  }

  /**
   * Holds the result of a single visualization step.
   *
   * @param message     the action message or result to display to the user
   * @param currentLine the current line of the pseudocode being executed;
   *                    {@code -1} signals that visualization should stop
   */
  public record Result(String message, int currentLine) {
  }

  /**
   * Returns whether this resolver uses separate chaining (each bucket is a
   * separate row) instead of open addressing (wrapping within the table view).
   *
   * @return {@code true} if separate chaining is used, {@code false} otherwise
   */
  boolean useSeparateChaining();

  /**
   * Returns the list of hash function fields required by this resolver for
   * the given data type. The returned list is auto-synced with the model for
   * the view layer.
   *
   * @param dataType the type of key data
   * @return the hash function fields needed by this resolver
   */
  List<HashFunction> getHashFunctionFields(DataType dataType);

  /**
   * Sets the hash function fields for this resolver.
   *
   * @param fields the hash function fields to use
   */
  void setHashFunctionFields(List<HashFunction> fields);

  /**
   * Prepares the resolver for collision resolution visualization and returns
   * the pseudocode for the given action.
   *
   * @param action the hash table action to visualize
   * @param key    the key involved in the operation
   * @param table  the hash table on which the operation is performed
   * @return the list of pseudocode lines describing the algorithm
   */
  List<String> getAlgorithmAndInitalize(HashAction action, String key, Table table);

  /**
   * Advances the visualization by one step and returns the result.
   *
   * @return the result of the current step, including a message and the
   *         corresponding pseudocode line number
   */
  Result nextStep();
}
