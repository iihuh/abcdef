package com.hashvis.model.collision;

import java.util.ArrayList;

import com.hashvis.model.table.Row;
import com.hashvis.model.table.Table;
import java.util.List;
import com.hashvis.model.hashfunc.*;
import com.hashvis.model.table.*;

abstract class ActionProcessor implements CollisionResolver {
  protected HashFunction hashFunc;
  protected String key;
  protected Table table;
  protected Integer hashValue = null;
  protected Row currentRow = null;

  abstract protected String getcurrent_ResolverType();

  abstract protected void uniqueInitalize(HashAction action);

  protected ArrayList<String> getPseudocode(HashAction action) {
    ArrayList<String> pseudocode = new ArrayList<String>();
    pseudocode.add("step = 0 ; i = base =hash(k,n)");
    switch (action) {
      case HashAction.INSERT -> {
        pseudocode.add("while (HT[i] != EMPTY)");
        pseudocode.add("if (HT[i] == DELETED) mark the suitable space");
        pseudocode.add(" ++step; if HT[i] == key || step == size of HT , stop insertion");
        pseudocode.add(getcurrent_ResolverType());
        pseudocode.add("found insertion point, insert key at suitable space or HT[i] ");
      }
      case HashAction.DELETE -> {
        pseudocode.add("while (HT[i] != EMPTY)");
        pseudocode.add("if (HT[i] == key) HT[i] = DELETED  break");
        pseudocode.add(" ++step; if step == size of HT, stop deletion");
        pseudocode.add(getcurrent_ResolverType());
      }
      case HashAction.SEARCH -> {
        pseudocode.add("while (HT[i] != EMPTY)");
        pseudocode.add("if (HT[i] == key) return 'found at index i'");
        pseudocode.add(" ++step; if step == size of HT, stop searching");
        pseudocode.add(getcurrent_ResolverType());
        pseudocode.add("return 'not found' ");
      }
      default -> {
        return new ArrayList<String>();
      }
    }
    return pseudocode;
  }

  @Override
  public List<String> getAlgorithmAndInitalize(HashAction action, String key, Table table) {
    this.key = key;
    this.table = table;
    return getAlgorithm(action);
  }

  protected ArrayList<String> getAlgorithm(HashAction action) {
    uniqueInitalize(action);
    return getPseudocode(action);
  }
}
