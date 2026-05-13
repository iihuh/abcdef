package com.hashvis.dsl.parser.ast;

import java.util.ArrayList;

import com.hashvis.dsl.parser.func.Callable;

/**
 * Represents a function call target(args).
 */
public class Call extends HasPreExec {
  public Call(int begin, int end, String content) {
    super(begin, end, content);
  }

  @Override
  public String getBreadcrumb() {
    return super.getBreadcrumbPreExec() + "(";
  }

  @Override
  public String getBreadcrumbPreExec() {
    return super.getBreadcrumbPreExec() + content();
  }

  @Override
  public Object eval() {
    Object temp = this.preExec().eval();
    if (!(temp instanceof Callable))
      throw new EvalException(this.preExec(), "not a Callable");
    ArrayList<Object> args = new ArrayList<>();
    for (Ast child : this.children()) {
      args.add(child.eval());
    }
    try {
      return ((Callable) temp).call(args);
    } catch (RuntimeException e) {
      if (e.getClass() != EvalException.class)
        throw new EvalException(this, e.getMessage());
      throw (EvalException) e;
    }
  }

  @Override
  public String toString() {
    return preExecToString("Call");
  }
}
