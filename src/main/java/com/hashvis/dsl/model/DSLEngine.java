package com.hashvis.dsl.model;

import com.hashvis.dsl.parser.*;
import com.hashvis.dsl.parser.ast.Ast.EvalException;

public class DSLEngine {
  private ParseTree parseTree = null;
  private SymbolTable symbolTable;
  private BracketParser bracketParser = null;
  private EvalException evalException = null;

  public DSLEngine(SymbolTable symbolTable) {
    this.symbolTable = new SymbolTable(symbolTable);
    this.parseTree = null;
  }

  public void update(String source) {
    parseTree = new ParseTree(source, symbolTable);
    bracketParser = new BracketParser(source);
    evalException = null;
    try {
      checkReturnType(parseTree.eval());
    } catch (EvalException e) {
      evalException = e;
    } catch (Exception e) {
      evalException = new EvalException(null, e.getMessage());
    }
  }

  public Object eval() {
    if (parseTree == null)
      return null;
    if (evalException != null)
      return null;
    return parseTree.eval();
  }

  protected void checkReturnType(Object result) {
    // Do nothing, will be overridden
  }

  public EvalException evalErr() {
    return evalException;
  }

  public ParseTree tree() {
    return parseTree;
  }

  public SymbolTable symbolTable() {
    return symbolTable;
  }

  public BracketParser bracket() {
    return bracketParser;
  }
}
