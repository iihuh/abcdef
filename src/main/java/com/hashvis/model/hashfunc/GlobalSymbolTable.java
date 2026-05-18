package com.hashvis.model.hashfunc;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.codepane.parser.SymbolTable;
import com.codepane.parser.func.*;

// Global functions for the interpreter
public class GlobalSymbolTable {
  private GlobalSymbolTable() {
  };

  private static SymbolTable symbolTable = buildGlobalSymbolTable();

  public static SymbolTable getGlobalSymbolTable() {
    return symbolTable;
  }
  // Built-in functions

  // Sum: take an array of integers and return the sum
  private static Object sum(List<Object> args) {
    BigInteger sum = BigInteger.ZERO;
    if (args.size() == 0)
      throw new RuntimeException("Not enough arguments");
    Object temp = args.get(0);
    if (!(temp instanceof ArrayList))
      throw new RuntimeException("Argument is not a array");
    ArrayList<?> arg = (ArrayList<?>) temp;
    for (int i = 0; i < arg.size(); i++) {
      if (!(arg.get(i) instanceof BigInteger))
        throw new RuntimeException("Element " + i + " is not a integer");
      sum = sum.add((BigInteger) arg.get(i));
    }
    return sum;
  }

  // Map: take an array and a function, apply the function to each element
  private static Object map(List<Object> args) {
    if (args.size() < 2)
      throw new RuntimeException("Not enough arguments");
    Object temp = args.get(0);
    if (!(temp instanceof ArrayList))
      throw new RuntimeException("Argument 1 is not a array");
    ArrayList<?> arg = (ArrayList<?>) temp;
    Object func = args.get(1);
    if (!(func instanceof Callable))
      throw new RuntimeException("Argument 2 is not a function");
    Callable function = (Callable) func;
    ArrayList<Object> result = new ArrayList<Object>();
    for (int i = 0; i < arg.size(); i++) {
      result.add(function.call(List.of(arg.get(i))));
    }
    return result;
  }

  // Len: take an array and return the length
  private static Object len(List<Object> args) {
    if (args.size() == 0)
      throw new RuntimeException("Not enough arguments");
    Object temp = args.get(0);
    if (!(temp instanceof ArrayList))
      throw new RuntimeException("Argument is not a array");
    return BigInteger.valueOf(((ArrayList<?>) temp).size());
  }

  // Range: take start, end, step and return the range array
  private static Object range(List<Object> args) {
    if (args.size() == 0)
      throw new RuntimeException("Not enough arguments");
    BigInteger end = BigInteger.ONE;
    if (!(args.get(0) instanceof BigInteger))
      throw new RuntimeException("Input must be integer");
    end = (BigInteger) args.get(0);
    ArrayList<Object> result = new ArrayList<Object>();
    for (BigInteger i = BigInteger.ZERO; i.compareTo(end) < 0; i = i.add(BigInteger.ONE)) {
      result.add(i);
    }
    return result;
  }

  // Log2
  private static Object log2(List<Object> args) {
    if (args.size() == 0)
      throw new RuntimeException("Not enough arguments");
    if (!(args.get(0) instanceof BigInteger))
      throw new RuntimeException("Input must be integer");
    BigInteger inp = (BigInteger) args.get(0);
    return BigInteger.valueOf(inp.bitLength() - 1);
  }

  // Filter: take an array and a predicate, return elements satisfying the
  // predicate
  private static Object filter(List<Object> args) {
    if (args.size() < 2)
      throw new RuntimeException("Not enough arguments");
    Object temp = args.get(0);
    if (!(temp instanceof ArrayList))
      throw new RuntimeException("Argument 1 is not an array");
    ArrayList<?> arg = (ArrayList<?>) temp;
    Object func = args.get(1);
    if (!(func instanceof Callable))
      throw new RuntimeException("Argument 2 is not a function");
    Callable predicate = (Callable) func;
    ArrayList<Object> result = new ArrayList<Object>();
    for (int i = 0; i < arg.size(); i++) {
      Object val = arg.get(i);
      Object keep = predicate.call(List.of(val));
      if (keep instanceof Boolean && (Boolean) keep) {
        result.add(val);
      }
    }
    return result;
  }

  // Reduce: take an array and accumulator function, use first element as initial
  @SuppressWarnings("unchecked")
  private static Object reduce(List<Object> args) {
    if (args.size() < 2)
      throw new RuntimeException("Not enough arguments");
    Object temp = args.get(0);
    if (!(temp instanceof ArrayList))
      throw new RuntimeException("Argument 1 is not an array");
    ArrayList<Object> arg = (ArrayList<Object>) temp;
    if (arg.size() == 0)
      throw new RuntimeException("Cannot reduce empty array");
    Object func = args.get(1);
    if (!(func instanceof Callable))
      throw new RuntimeException("Argument 2 is not a function");
    Callable accumulator = (Callable) func;
    Object acc = arg.get(0);
    for (int i = 1; i < arg.size(); i++) {
      acc = accumulator.call(List.of(acc, arg.get(i)));
    }
    return acc;
  }

  // Reverse: take an array and return a reversed copy
  @SuppressWarnings("unchecked")
  private static Object reverse(List<Object> args) {
    if (args.size() == 0)
      throw new RuntimeException("Not enough arguments");
    Object temp = args.get(0);
    if (!(temp instanceof ArrayList))
      throw new RuntimeException("Argument is not an array");
    ArrayList<Object> arg = (ArrayList<Object>) temp;
    ArrayList<Object> result = new ArrayList<Object>(arg.size());
    for (int i = arg.size() - 1; i >= 0; i--) {
      result.add(arg.get(i));
    }
    return result;
  }

  // Concat: concatenate two arrays
  @SuppressWarnings("unchecked")
  private static Object cat(List<Object> args) {
    if (args.size() < 2)
      throw new RuntimeException("Not enough arguments");
    Object temp1 = args.get(0);
    Object temp2 = args.get(1);
    if (!(temp1 instanceof ArrayList) || !(temp2 instanceof ArrayList))
      throw new RuntimeException("Both arguments must be arrays");
    ArrayList<Object> arg1 = (ArrayList<Object>) temp1;
    ArrayList<Object> arg2 = (ArrayList<Object>) temp2;
    ArrayList<Object> result = new ArrayList<Object>(arg1.size() + arg2.size());
    result.addAll(arg1);
    result.addAll(arg2);
    return result;
  }

  // Contains: check if an array contains a given element
  private static Object contains(List<Object> args) {
    if (args.size() < 2)
      throw new RuntimeException("Not enough arguments");
    Object temp = args.get(0);
    if (!(temp instanceof ArrayList))
      throw new RuntimeException("Argument 1 is not an array");
    ArrayList<?> arg = (ArrayList<?>) temp;
    Object element = args.get(1);
    for (int i = 0; i < arg.size(); i++) {
      if (arg.get(i).equals(element))
        return BigInteger.ONE;
    }
    return BigInteger.ZERO;
  }

  // Product: take an array of integers and return the product
  private static Object product(List<Object> args) {
    BigInteger prod = BigInteger.ONE;
    if (args.size() == 0)
      throw new RuntimeException("Not enough arguments");
    Object temp = args.get(0);
    if (!(temp instanceof ArrayList))
      throw new RuntimeException("Argument is not an array");
    ArrayList<?> arg = (ArrayList<?>) temp;
    for (int i = 0; i < arg.size(); i++) {
      if (!(arg.get(i) instanceof BigInteger))
        throw new RuntimeException("Element " + i + " is not an integer");
      prod = prod.multiply((BigInteger) arg.get(i));
    }
    return prod;
  }

  // Min: take an array of integers and return the minimum
  private static Object min(List<Object> args) {
    if (args.size() == 0)
      throw new RuntimeException("Not enough arguments");
    Object temp = args.get(0);
    if (!(temp instanceof ArrayList))
      throw new RuntimeException("Argument is not an array");
    ArrayList<?> arg = (ArrayList<?>) temp;
    if (arg.size() == 0)
      throw new RuntimeException("Array is empty");
    BigInteger minVal = (BigInteger) arg.get(0);
    for (int i = 1; i < arg.size(); i++) {
      if (!(arg.get(i) instanceof BigInteger))
        throw new RuntimeException("Element " + i + " is not an integer");
      BigInteger val = (BigInteger) arg.get(i);
      if (val.compareTo(minVal) < 0)
        minVal = val;
    }
    return minVal;
  }

  // Max: take an array of integers and return the maximum
  private static Object max(List<Object> args) {
    if (args.size() == 0)
      throw new RuntimeException("Not enough arguments");
    Object temp = args.get(0);
    if (!(temp instanceof ArrayList))
      throw new RuntimeException("Argument is not an array");
    ArrayList<?> arg = (ArrayList<?>) temp;
    if (arg.size() == 0)
      throw new RuntimeException("Array is empty");
    BigInteger maxVal = (BigInteger) arg.get(0);
    for (int i = 1; i < arg.size(); i++) {
      if (!(arg.get(i) instanceof BigInteger))
        throw new RuntimeException("Element " + i + " is not an integer");
      BigInteger val = (BigInteger) arg.get(i);
      if (val.compareTo(maxVal) > 0)
        maxVal = val;
    }
    return maxVal;
  }

  // Abs: absolute value of an integer
  private static Object abs(List<Object> args) {
    if (args.size() == 0)
      throw new RuntimeException("Not enough arguments");
    if (!(args.get(0) instanceof BigInteger))
      throw new RuntimeException("Input must be an integer");
    return ((BigInteger) args.get(0)).abs();
  }

  // All: check if all elements in an array satisfy a predicate
  private static Object all(List<Object> args) {
    if (args.size() < 2)
      throw new RuntimeException("Not enough arguments");
    Object temp = args.get(0);
    if (!(temp instanceof ArrayList))
      throw new RuntimeException("Argument 1 is not an array");
    ArrayList<?> arg = (ArrayList<?>) temp;
    Object func = args.get(1);
    if (!(func instanceof Callable))
      throw new RuntimeException("Argument 2 is not a function");
    Callable predicate = (Callable) func;
    for (int i = 0; i < arg.size(); i++) {
      Object result = predicate.call(List.of(arg.get(i)));
      if (!(result instanceof Boolean) || !(Boolean) result)
        return BigInteger.ZERO;
    }
    return BigInteger.ONE;
  }

  // Any: check if any element in an array satisfies a predicate
  private static Object any(List<Object> args) {
    if (args.size() < 2)
      throw new RuntimeException("Not enough arguments");
    Object temp = args.get(0);
    if (!(temp instanceof ArrayList))
      throw new RuntimeException("Argument 1 is not an array");
    ArrayList<?> arg = (ArrayList<?>) temp;
    Object func = args.get(1);
    if (!(func instanceof Callable))
      throw new RuntimeException("Argument 2 is not a function");
    Callable predicate = (Callable) func;
    for (int i = 0; i < arg.size(); i++) {
      Object result = predicate.call(List.of(arg.get(i)));
      if (result instanceof Boolean && (Boolean) result)
        return BigInteger.ONE;
    }
    return BigInteger.ZERO;
  }

  // Default hash functions
  private static Object mixerHash(List<Object> args) {
    if (args.size() == 0)
      throw new RuntimeException("Not enough arguments");
    if (!(args.get(0) instanceof BigInteger))
      throw new RuntimeException("Input must be integer");
    BigInteger inp = (BigInteger) args.get(0);
    inp = inp.shiftRight(16).xor(inp).multiply(BigInteger.valueOf(0x45d9f3b));
    inp = inp.shiftRight(16).xor(inp).multiply(BigInteger.valueOf(0x45d9f3b));
    inp = inp.shiftRight(16).xor(inp);
    return inp;
  }

  private static Object knuthHash(List<Object> args) {
    if (args.size() == 0)
      throw new RuntimeException("Not enough arguments");
    if (!(args.get(0) instanceof BigInteger))
      throw new RuntimeException("Input must be integer");
    BigInteger inp = (BigInteger) args.get(0);
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
    symbolTable.set("contains", new BuiltinFunction(GlobalSymbolTable::contains, "(a, x): Check if a contains x"));
    symbolTable.set("product", new BuiltinFunction(GlobalSymbolTable::product, "(a): Product of a"));
    symbolTable.set("min", new BuiltinFunction(GlobalSymbolTable::min, "(a): Minimum of a"));
    symbolTable.set("max", new BuiltinFunction(GlobalSymbolTable::max, "(a): Maximum of a"));
    symbolTable.set("abs", new BuiltinFunction(GlobalSymbolTable::abs, "(n): Absolute value of n"));
    symbolTable.set("all", new BuiltinFunction(GlobalSymbolTable::all, "(a, p): True if all elements satisfy p"));
    symbolTable.set("any", new BuiltinFunction(GlobalSymbolTable::any, "(a, p): True if any element satisfies p"));
    symbolTable.set("log2", new BuiltinFunction(GlobalSymbolTable::log2, "(n): Log2 of n"));
    symbolTable.set("mixerHash", new BuiltinFunction(GlobalSymbolTable::mixerHash, "(n): Mixer hash of n"));
    symbolTable.set("knuthHash", new BuiltinFunction(GlobalSymbolTable::knuthHash, "(n): Knuth hash of n"));
    return symbolTable;
  }

}
