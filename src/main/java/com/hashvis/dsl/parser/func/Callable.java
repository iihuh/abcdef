package com.hashvis.dsl.parser.func;

import java.util.List;

public interface Callable {
  public Object call(List<Object> args);
}
