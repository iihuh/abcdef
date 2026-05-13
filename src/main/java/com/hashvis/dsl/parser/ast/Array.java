package com.hashvis.dsl.parser.ast;

import java.util.ArrayList;

/**
 * Represents an array literal [a, b, c].
 */
public class Array extends NonTerm {
  public Array(int begin, int end, String content) {
    super(begin, end, content);
  }

  @Override
  public String getBreadcrumb() {
    return "[";
  }

  @Override
  public Object eval() {
    ArrayList<Object> result = new ArrayList<Object>();
    for (Ast child : this.children()) {
      result.add(child.eval());
    }
    return result;
  }

  @Override
  public String toString() {
    return this._begin + ":" + this._end + " (Array)" + this.content() + this.childrenToString();
  }
}
