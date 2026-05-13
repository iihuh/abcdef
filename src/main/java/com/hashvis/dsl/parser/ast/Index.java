package com.hashvis.dsl.parser.ast;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Represents a array indexing target[index].
 */
public class Index extends HasPreExec {
  public Index(int begin, int end, String content) {
    super(begin, end, content);
  }

  private int warpIndex(int value, int len) {
    if (len == 0)
      throw new EvalException(this, "Index out of range");
    return (value % len + len) % len;
  }

  private int evalAsInt(Ast node, String errorMessage) {
    if (node == null)
      throw new EvalException(this, "Missing operand");
    Object result = node.eval();
    if (!(result instanceof BigInteger)) {
      throw new EvalException(node, errorMessage);
    }
    return ((BigInteger) result).intValue();
  }

  @Override
  public String getBreadcrumb() {
    return getBreadcrumbPreExec() + "[";
  }

  @Override
  public Object eval() {
    // Target check & eval
    if (this.preExec() == null)
      throw new EvalException(this, "Index has no target expression");
    Object temp = this.preExec().eval();
    if (!(temp instanceof ArrayList))
      throw new EvalException(this.preExec(), "Target is not an ArrayList");
    ArrayList<?> list = (ArrayList<?>) temp;
    // Index check & eval
    if (this.children().size() == 0)
      throw new EvalException(this, "Index has no index");
    int base = warpIndex(evalAsInt(this.children().get(0), "Index must be an integer"), list.size());
    // Case A: Single element
    if (this.children().size() == 1)
      return list.get(base);
    // Case B: Slicing
    return handleSlicing(list, base);
  }

  private Object handleSlicing(ArrayList<?> list, int start) {
    int len = list.size();

    // Resolve end position
    int end = warpIndex(evalAsInt(this.children().get(1), "End position must be an integer"), len);

    // Resolve step (defaults to 1)
    int step = 1;
    if (this.children().size() >= 3) {
      step = evalAsInt(this.children().get(2), "Step must be an integer");
    }

    if (step == 0)
      throw new EvalException(this, "Step cannot be zero");

    ArrayList<Object> slice = new ArrayList<>();
    if (step > 0) {
      for (int i = start; i < Math.min(len, end); i += step) {
        slice.add(list.get(i));
      }
    } else {
      for (int i = start; i > Math.max(-1, end); i += step) {
        slice.add(list.get(i));
      }
    }
    return slice;
  }

  @Override
  public String toString() {
    return preExecToString("Index");
  }
}
