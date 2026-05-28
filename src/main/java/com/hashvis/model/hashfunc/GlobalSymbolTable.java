package com.hashvis.model.hashfunc;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.codepane.SymbolTable;
import com.codepane.parser.func.*;

/**
 * Provides the global symbol table for the interpreter. The table contains
 * built-in functions (sum, map, filter, reduce, len, range, etc.) and
 * default hash functions (mixerHash, knuthHash) used by the code pane.
 */
public class GlobalSymbolTable {
  /** Private constructor to prevent instantiation. */
  private GlobalSymbolTable() {
  };

  /** The singleton global symbol table, lazily built at class initialisation. */
  private static SymbolTable symbolTable = buildGlobalSymbolTable();

  /**
   * Returns the global symbol table containing all built-in functions.
   *
   * @return the global {@link SymbolTable} instance
   */
  public static SymbolTable getGlobalSymbolTable() {
    return symbolTable;
  }

  /**
   * Asserts that the argument list has at least the given number of elements.
   *
   * @param args the argument list to check
   * @param size the minimum required number of arguments
   * @throws RuntimeException if the list is too short
   */
  private static void assertSize(List<Object> args, int size) {
    if (args.size() < size)
      throw new RuntimeException("Not enough arguments");
  }

  /**
   * Casts an object to {@link BigInteger}, throwing a descriptive exception if
   * it is not an integer.
   *
   * @param obj   the object to cast
   * @param index the argument index for error messages, or {@code -1} for
   *              generic input
   * @return the object as a {@code BigInteger}
   * @throws RuntimeException if the object is not a {@code BigInteger}
   */
  private static BigInteger toInteger(Object obj, int index) {
    if (!(obj instanceof BigInteger)) {
      if (index == -1)
        throw new RuntimeException("Input must be an integer");
      else
        throw new RuntimeException("Argument " + index + " must be an integer");
    }
    return (BigInteger) obj;
  }

  /**
   * Casts an object to an {@link ArrayList}, throwing a descriptive exception
   * if it is not an array.
   *
   * @param obj   the object to cast
   * @param index the argument index for error messages, or {@code -1} for
   *              generic input
   * @return the object as an {@code ArrayList}
   * @throws RuntimeException if the object is not an {@code ArrayList}
   */
  private static ArrayList<?> toList(Object obj, int index) {
    if (!(obj instanceof ArrayList)) {
      if (index == -1)
        throw new RuntimeException("Input must be an array");
      else
        throw new RuntimeException("Argument " + index + " must be an array");
    }
    return (ArrayList<?>) obj;
  }

  /**
   * Casts an object to a {@link Callable}, throwing a descriptive exception
   * if it is not a function.
   *
   * @param obj   the object to cast
   * @param index the argument index for error messages, or {@code -1} for
   *              generic input
   * @return the object as a {@code Callable}
   * @throws RuntimeException if the object is not a {@code Callable}
   */
  private static Callable toFunction(Object obj, int index) {
    if (!(obj instanceof Callable)) {
      if (index == -1)
        throw new RuntimeException("Input must be a function");
      else
        throw new RuntimeException("Argument " + index + " must be a function");
    }
    return (Callable) obj;
  }

  /** Built-in functions. */

  /**
   * Returns the sum of all integers in the given array.
   *
   * @param args a single-element list containing the array of integers
   * @return the sum as a {@code BigInteger}
   */
  private static Object sum(List<Object> args) {
    BigInteger sum = BigInteger.ZERO;
    assertSize(args, 1);
    ArrayList<?> arg = toList(args.get(0), 1);
    for (int i = 0; i < arg.size(); i++) {
      sum = sum.add(toInteger(arg.get(i), -1));
    }
    return sum;
  }

  /**
   * Applies a function to each element of an array and returns a new array
   * containing the results.
   *
   * @param args a two-element list containing the array and the function
   * @return a new {@code ArrayList} of results
   */
  private static Object map(List<Object> args) {
    assertSize(args, 2);
    ArrayList<?> arg = toList(args.get(0), 1);
    Callable function = toFunction(args.get(1), 2);
    ArrayList<Object> result = new ArrayList<Object>();
    for (int i = 0; i < arg.size(); i++) {
      result.add(function.call(List.of(arg.get(i))));
    }
    return result;
  }

  /**
   * Returns the length of the given array.
   *
   * @param args a single-element list containing the array
   * @return the length as a {@code BigInteger}
   */
  private static Object len(List<Object> args) {
    assertSize(args, 1);
    return BigInteger.valueOf(toList(args.get(0), 1).size());
  }

  /**
   * Generates an array of integers from {@code 0} to {@code end} (inclusive).
   *
   * @param args a single-element list containing the end value
   * @return an {@code ArrayList} of {@code BigInteger} values
   */
  private static Object range(List<Object> args) {
    assertSize(args, 1);
    BigInteger end = BigInteger.ONE;
    end = end.add(toInteger(args.get(0), 1));
    ArrayList<Object> result = new ArrayList<Object>();
    for (BigInteger i = BigInteger.ZERO; i.compareTo(end) < 0; i = i.add(BigInteger.ONE)) {
      result.add(i);
    }
    return result;
  }

  /**
   * Returns the base-2 logarithm of the given integer (floor).
   *
   * @param args a single-element list containing the integer
   * @return the floor of the base-2 logarithm as a {@code BigInteger}
   */
  private static Object log2(List<Object> args) {
    assertSize(args, 1);
    return BigInteger.valueOf(toInteger(args.get(0), 1).bitLength() - 1);
  }

  /**
   * Filters an array, returning only the elements that satisfy the given
   * predicate function.
   *
   * @param args a two-element list containing the array and the predicate
   * @return a new {@code ArrayList} containing matching elements
   */
  private static Object filter(List<Object> args) {
    assertSize(args, 2);
    ArrayList<?> arg = toList(args.get(0), 1);
    Callable predicate = toFunction(args.get(1), 2);
    ArrayList<Object> result = new ArrayList<Object>();
    for (int i = 0; i < arg.size(); i++) {
      Object val = arg.get(i);
      Object keep = predicate.call(List.of(val));
      if (toInteger(keep, -1).compareTo(BigInteger.ZERO) != 0)
        result.add(val);
    }
    return result;
  }

  /**
   * Reduces an array to a single value using an accumulator function. The
   * first element is used as the initial accumulator value.
   *
   * @param args a two-element list containing the array and the accumulator
   *             function
   * @return the accumulated result
   * @throws RuntimeException if the array is empty
   */
  private static Object reduce(List<Object> args) {
    assertSize(args, 2);
    ArrayList<?> arg = toList(args.get(0), 1);
    if (arg.size() == 0)
      throw new RuntimeException("Cannot reduce empty array");
    Callable accumulator = toFunction(args.get(1), 2);
    Object acc = arg.get(0);
    for (int i = 1; i < arg.size(); i++) {
      acc = accumulator.call(List.of(acc, arg.get(i)));
    }
    return acc;
  }

  /**
   * Returns a new array containing the elements of the input array in reverse
   * order.
   *
   * @param args a single-element list containing the array
   * @return a reversed copy of the array
   */
  private static Object reverse(List<Object> args) {
    assertSize(args, 1);
    ArrayList<?> arg = toList(args.get(0), 1);
    ArrayList<Object> result = new ArrayList<Object>(arg.size());
    for (int i = arg.size() - 1; i >= 0; i--) {
      result.add(arg.get(i));
    }
    return result;
  }

  /**
   * Concatenates multiple arrays into a single array.
   *
   * @param args a list of arrays to concatenate
   * @return a new {@code ArrayList} containing all elements in order
   */
  private static Object cat(List<Object> args) {
    ArrayList<Object> result = new ArrayList<Object>();
    for (int i = 0; i < args.size(); i++) {
      ArrayList<?> arg = toList(args.get(i), i);
      for (int j = 0; j < arg.size(); j++) {
        result.add(arg.get(j));
      }
    }
    return result;
  }

  /**
   * Returns the absolute value of an integer.
   *
   * @param args a single-element list containing the integer
   * @return the absolute value as a {@code BigInteger}
   */
  private static Object abs(List<Object> args) {
    assertSize(args, 1);
    return toInteger(args.get(0), 1).abs();
  }

  /**
   * Checks whether all elements in an array satisfy a predicate function.
   *
   * @param args a two-element list containing the array and the predicate
   * @return {@code BigInteger.ONE} if all elements satisfy the predicate,
   *         {@code BigInteger.ZERO} otherwise
   */
  private static Object all(List<Object> args) {
    assertSize(args, 2);
    ArrayList<?> arg = toList(args.get(0), 1);
    Callable predicate = toFunction(args.get(1), 2);
    for (int i = 0; i < arg.size(); i++) {
      Object result = predicate.call(List.of(arg.get(i)));
      if (!(result instanceof Boolean) || !(Boolean) result)
        return BigInteger.ZERO;
    }
    return BigInteger.ONE;
  }

  /**
   * Checks whether any element in an array satisfies a predicate function.
   *
   * @param args a two-element list containing the array and the predicate
   * @return {@code BigInteger.ONE} if any element satisfies the predicate,
   *         {@code BigInteger.ZERO} otherwise
   */
  private static Object any(List<Object> args) {
    assertSize(args, 2);
    ArrayList<?> arg = toList(args.get(0), 1);
    Callable predicate = toFunction(args.get(1), 2);
    for (int i = 0; i < arg.size(); i++) {
      Object result = predicate.call(List.of(arg.get(i)));
      if (result instanceof Boolean && (Boolean) result)
        return BigInteger.ONE;
    }
    return BigInteger.ZERO;
  }

  /** Default hash functions. */

  /**
   * Applies the mixer hash algorithm (xor-shift-multiply) to an integer.
   *
   * @param args a single-element list containing the integer to hash
   * @return the mixed hash value as a {@code BigInteger}
   */
  private static Object mixerHash(List<Object> args) {
    assertSize(args, 1);
    BigInteger inp = toInteger(args.get(0), 1);
    inp = inp.shiftRight(16).xor(inp).multiply(BigInteger.valueOf(0x45d9f3b));
    inp = inp.shiftRight(16).xor(inp).multiply(BigInteger.valueOf(0x45d9f3b));
    inp = inp.shiftRight(16).xor(inp);
    return inp;
  }

  /**
   * Applies the Knuth multiplicative hash algorithm to an integer.
   *
   * @param args a single-element list containing the integer to hash
   * @return the Knuth hash value as a {@code BigInteger}
   */
  private static Object knuthHash(List<Object> args) {
    assertSize(args, 1);
    BigInteger inp = toInteger(args.get(0), 1);
    BigInteger scramble = BigInteger.valueOf(0x9e3779b9); // 2**32 * (sqrt(5)-1)/2
    int log = inp.bitLength();
    int base = 32;
    while (log > base) {
      scramble = scramble.pow(2);
      base *= 2;
    }
    int shift = base - log;
    inp = inp.multiply(scramble).shiftRight(shift);
    return inp;
  }

  /**
   * Builds and populates the global symbol table with all built-in and default
   * hash functions.
   *
   * @return the populated {@link SymbolTable}
   */
  private static SymbolTable buildGlobalSymbolTable() {
    SymbolTable symbolTable = new SymbolTable();
    symbolTable.set("sum", new BuiltinFunction(GlobalSymbolTable::sum, "(a): Sum of a"));
    symbolTable.set("map", new BuiltinFunction(GlobalSymbolTable::map, "(a, f): Apply f to each element of a"));
    symbolTable.set("filter", new BuiltinFunction(GlobalSymbolTable::filter, "(a, p): Filter a by predicate p"));
    symbolTable.set("reduce",
        new BuiltinFunction(GlobalSymbolTable::reduce, "(a, f): Reduce a with f, first element as initial"));
    symbolTable.set("len", new BuiltinFunction(GlobalSymbolTable::len, "(a): Length of a"));
    symbolTable.set("range", new BuiltinFunction(GlobalSymbolTable::range, "(end): Array of [0..end]"));
    symbolTable.set("reverse", new BuiltinFunction(GlobalSymbolTable::reverse, "(a): Reverse of a"));
    symbolTable.set("cat", new BuiltinFunction(GlobalSymbolTable::cat, "(a, b): Concatenate a and b"));
    symbolTable.set("abs", new BuiltinFunction(GlobalSymbolTable::abs, "(n): Absolute value of n"));
    symbolTable.set("all", new BuiltinFunction(GlobalSymbolTable::all, "(a, p): True if all elements satisfy p"));
    symbolTable.set("any", new BuiltinFunction(GlobalSymbolTable::any, "(a, p): True if any element satisfies p"));
    symbolTable.set("log2", new BuiltinFunction(GlobalSymbolTable::log2, "(n): Log2 of n"));
    symbolTable.set("mixerHash", new BuiltinFunction(GlobalSymbolTable::mixerHash, "(n): Mixer hash of n"));
    symbolTable.set("knuthHash", new BuiltinFunction(GlobalSymbolTable::knuthHash, "(n): Knuth hash of n"));
    return symbolTable;
  }

}
