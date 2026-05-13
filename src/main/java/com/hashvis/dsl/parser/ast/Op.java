package com.hashvis.dsl.parser.ast;

import java.math.BigInteger;

public class Op extends NonTerm {

  public Op(int begin, int end, String content) {
    super(begin, end, content);
  }

  @Override
  public Object eval() {
    if (this.children().size() == 0)
      throw new EvalException(this, "No operand");
    Object lhs = this.children().get(0).eval();
    if (!(lhs instanceof BigInteger))
      throw new EvalException(this.children().get(0), "Operand is not an integer");
    BigInteger ilhs = (BigInteger) lhs;
    if (this.children().size() == 1) {
      switch (this.content()) {
        case "-":
          return ilhs.negate();
        case "+":
          return ilhs;
        case "!":
          return BigInteger.valueOf(ilhs.compareTo(BigInteger.ZERO) == 0 ? 1 : 0);
        default:
          throw new EvalException(this, "Unknown unary operator");
      }
    }
    if (this.content().equals("?")) {
      if (this.children().size() < 3)
        throw new EvalException(this, "Missing operand");
      return this.children().get(ilhs.compareTo(BigInteger.ZERO) != 0 ? 1 : 2).eval();
    }
    Object rhs = this.children().get(1).eval();
    if (!(rhs instanceof BigInteger))
      throw new EvalException(this.children().get(1), "Operand is not an integer");
    BigInteger irhs = (BigInteger) rhs;
    try {
      return switch (this.content()) {
        case "+" -> ilhs.add(irhs);
        case "-" -> ilhs.subtract(irhs);
        case "*" -> ilhs.multiply(irhs);
        case "/" -> ilhs.divide(irhs);
        case "%" -> ilhs.remainder(irhs);
        case "**" -> {
          if (irhs.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0)
            throw new EvalException(this.children().get(1), "Exponent too large");
          yield ilhs.pow(irhs.intValue());
        }
        case "&" -> ilhs.and(irhs);
        case "|" -> ilhs.or(irhs);
        case "^" -> ilhs.xor(irhs);
        case "<<" -> ilhs.shiftLeft((irhs).intValue());
        case ">>" -> ilhs.shiftRight((irhs).intValue());
        case "==" -> BigInteger.valueOf(ilhs.compareTo(irhs) == 0 ? 1 : 0);
        case "!=" -> BigInteger.valueOf(ilhs.compareTo(irhs) != 0 ? 1 : 0);
        case "<" -> BigInteger.valueOf(ilhs.compareTo(irhs) < 0 ? 1 : 0);
        case ">" -> BigInteger.valueOf(ilhs.compareTo(irhs) > 0 ? 1 : 0);
        case "<=" -> BigInteger.valueOf(ilhs.compareTo(irhs) <= 0 ? 1 : 0);
        case ">=" -> BigInteger.valueOf(ilhs.compareTo(irhs) >= 0 ? 1 : 0);
        default ->
          throw new EvalException(this, "Unknown binary operator");
      };
    } catch (ArithmeticException e) {
      throw new EvalException(this, "Division by zero");
    }
  }

  @Override
  public String toString() {
    return this._begin + ":" + this._end + " (Op)" + this.content() + " : " + this.childrenToString();
  }
}
