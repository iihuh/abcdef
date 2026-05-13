package com.hashvis.dsl.parser;

import com.hashvis.dsl.parser.ast.*;

/**
 * The Lexer class is responsible for performing lexical analysis on a source
 * string.
 * It breaks the input code into a stream of tokens (represented as {@link Ast}
 * nodes),
 * identifying operators, integers, identifiers, and terminal symbols.
 */
public class Lexer {
  private String code;
  private int currentPosition = 0;

  private SymbolTable symbolTable;

  public Lexer(String code) {
    super();
    this.code = code;
  }

  public SymbolTable getSymbolTable() {
    return this.symbolTable;
  }

  public void setSymbolTable(SymbolTable table) {
    this.symbolTable = table;
  }

  private static final String opsChars = "+*/-%^&|<>=!";
  private static final String multicharOpsChars = "*<>=!";
  private static final String termChars = ":()[]{},?";

  private Ast currentToken = null;

  private void skipWhitespace() {
    while (currentPosition < code.length()
        && Character.isWhitespace(code.charAt(currentPosition))) {
      currentPosition++;
    }
  }

  private Ast handleTerminal(int begin) {
    currentPosition++;
    return new Ast(begin, currentPosition, code.substring(begin, currentPosition));
  }

  private Ast handleOperator(int begin) {
    currentPosition++; // Consume first char of operator

    // Look ahead for multi-character operators
    if (currentPosition < code.length()) {
      char nextChar = code.charAt(currentPosition);
      if (multicharOpsChars.indexOf(nextChar) != -1) {
        consumeMultiCharOp();
      }
    }

    int end = currentPosition;
    String content = code.substring(begin, end);
    return new Op(begin, end, content);
  }

  private void consumeMultiCharOp() {
    char prev = code.charAt(currentPosition - 1);
    char curr = code.charAt(currentPosition);

    boolean matched = false;
    switch (prev) {
      case '*' -> matched = (curr == '*');
      case '!' -> matched = (curr == '=');
      case '=' -> matched = (curr == '=');
      case '<' -> matched = (curr == '=' || curr == '<');
      case '>' -> matched = (curr == '=' || curr == '>');
    }

    if (matched)
      currentPosition++;
  }

  private Ast handleIdentifierOrNumber(int begin) {
    // Consume until we hit an operator or a terminal
    while (currentPosition < code.length()) {
      char c = code.charAt(currentPosition);
      if (opsChars.indexOf(c) != -1 || termChars.indexOf(c) != -1) {
        break;
      }
      currentPosition++;
    }

    int end = currentPosition;
    String content = code.substring(begin, end).trim();

    // Adjusted boundaries if we trimmed trailing whitespace
    int actualEnd = end - (end - begin - content.length());

    return createToken(begin, actualEnd, content);
  }

  private Ast createToken(int begin, int end, String content) {
    try {
      // Try to create an Integer token first
      return new Int(begin, end, content);
    } catch (Exception e) {
      // If it's not an integer, it's an Identifier
      return new Id(begin, end, content, this.symbolTable);
    }
  }

  /**
   * The core logic of the lexer. Scans the input string to identify the next
   * valid token based on the defined character sets.
   * 
   * @return The next token as an {@link Ast} node, or null if the end of the
   *         input is reached.
   */
  private Ast nextToken() {
    // Skip whitespace
    skipWhitespace();
    // If eof, return null
    if (currentPosition >= code.length())
      return null;

    int begin = currentPosition;
    char currentChar = code.charAt(currentPosition);
    // Terminal characters, return directly
    if (termChars.indexOf(currentChar) != -1)
      return handleTerminal(begin);

    // Check if it is an operator
    if (opsChars.indexOf(currentChar) != -1)
      return handleOperator(begin);
    return handleIdentifierOrNumber(begin);
  }

  /**
   * Safely extracts a substring from the source code, used for token content
   * rebuilding
   * 
   * @param begin The starting index.
   * @param end   The ending index.
   * @return The extracted string, clamped to the boundaries of the source code.
   */
  public String subString(int begin, int end) {
    end = Math.min(end, code.length());
    begin = Math.max(begin, 0);
    return code.substring(begin, end);
  }

  /**
   * Returns the next token in the stream without consuming it.
   * This allows the parser to "look ahead" to make branching decisions.
   * 
   * @return The next {@link Ast} token.
   */
  public Ast peek() {
    if (this.currentToken == null) {
      this.currentToken = nextToken();
    }
    return this.currentToken;
  }

  /**
   * Returns the next token and advances the lexer's position.
   * 
   * @return The next {@link Ast} token.
   */
  public Ast next() {
    peek(); // Make sure we have a token
    Ast result = this.currentToken;
    this.currentToken = null; // Consume
    return result;
  }

  /**
   * @return The current character index the lexer has reached in the source code.
   */
  public int getPosition() {
    return this.currentPosition;
  }
}
