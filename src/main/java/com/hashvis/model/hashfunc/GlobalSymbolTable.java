package com.hashvis.model.hashfunc;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.codepane.SymbolTable;
import com.codepane.parser.func.*;

// Global functions for the interpreter
public class GlobalSymbolTable {
  private GlobalSymbolTable() {
  };

  private static SymbolTable symbolTable = buildGlobalSymbolTable();

  public static SymbolTable getGlobalSymbolTable() {
    return symbolTable;
  }

  // Helper functions
  private static void assertSize(List<Object> args, int size) {
    if (args.size() < size)
      throw new RuntimeException("Not enough arguments");
  }

  private static BigInteger toInteger(Object obj, int index) {
    if (!(obj instanceof BigInteger)) {
      if (index == -1)
        throw new RuntimeException("Input must be an integer");
      else
        throw new RuntimeException("Argument " + index + " must be an integer");
    }
    return (BigInteger) obj;
  }

  private static ArrayList<?> toList(Object obj, int index) {
    if (!(obj instanceof ArrayList)) {
      if (index == -1)
        throw new RuntimeException("Input must be an array");
      else
        throw new RuntimeException("Argument " + index + " must be an array");
    }
    return (ArrayList<?>) obj;
  }

  private static Callable toFunction(Object obj, int index) {
    if (!(obj instanceof Callable)) {
      if (index == -1)
        throw new RuntimeException("Input must be a function");
      else
        throw new RuntimeException("Argument " + index + " must be a function");
    }
    return (Callable) obj;
  }

  // Built-in functions

  // Sum: take an array of integers and return the sum
  private static Object sum(List<Object> args) {
    BigInteger sum = BigInteger.ZERO;
    assertSize(args, 1);
    ArrayList<?> arg = toList(args.get(0), 1);
    for (int i = 0; i < arg.size(); i++) {
      sum = sum.add(toInteger(arg.get(i), -1));
    }
    return sum;
  }

  // Map: take an array and a function, apply the function to each element
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

  // Len: take an array and return the length
  private static Object len(List<Object> args) {
    assertSize(args, 1);
    return BigInteger.valueOf(toList(args.get(0), 1).size());
  }

  // Range: take start, end, step and return the range array
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

  // Log2
  private static Object log2(List<Object> args) {
    assertSize(args, 1);
    return BigInteger.valueOf(toInteger(args.get(0), 1).bitLength() - 1);
  }

  // Filter: take an array and a predicate, return elements satisfying the
  // predicate
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

  // Reduce: take an array and accumulator function, use first element as initial
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

  // Reverse: take an array and return a reversed copy
  private static Object reverse(List<Object> args) {
    assertSize(args, 1);
    ArrayList<?> arg = toList(args.get(0), 1);
    ArrayList<Object> result = new ArrayList<Object>(arg.size());
    for (int i = arg.size() - 1; i >= 0; i--) {
      result.add(arg.get(i));
    }
    return result;
  }

  // Cat: concatenate arrays
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

  // Abs: absolute value of an integer
  private static Object abs(List<Object> args) {
    assertSize(args, 1);
    return toInteger(args.get(0), 1).abs();
  }

  // All: check if all elements in an array satisfy a predicate
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

  // Any: check if any element in an array satisfies a predicate
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

  // Default hash functions
  private static Object mixerHash(List<Object> args) {
    assertSize(args, 1);
    BigInteger inp = toInteger(args.get(0), 1);
    inp = inp.shiftRight(16).xor(inp).multiply(BigInteger.valueOf(0x45d9f3b));
    inp = inp.shiftRight(16).xor(inp).multiply(BigInteger.valueOf(0x45d9f3b));
    inp = inp.shiftRight(16).xor(inp);
    return inp;
  }

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
