package com.hashvis.test;

import com.hashvis.model.collision.*;
import com.hashvis.model.collision.CollisionResolver.HashAction;
import com.hashvis.model.collision.CollisionResolver.DataType;
import com.hashvis.model.collision.CollisionResolver.Result;
import com.hashvis.model.hashfunc.HashFunction;
import com.hashvis.model.table.*;

import java.util.List;
import java.util.Scanner;

public class ModelTest {

  public static void main(String[] args) {
    ModelTest test = new ModelTest();
    Scanner scanner = new Scanner(System.in);
    while (true) {
      resetScreen();
      int n;
      CollisionResolver resolver;
      System.out.print("Enter collision: ");
      n = scanner.nextInt();
      switch (n) {
        case 1:
          resolver = new LinearProbing();
          break;
        case 2:
          resolver = new QuadraticProbing();
          break;
        case 3:
          resolver = new DoubleHashing();
          break;
        default:
          resolver = new SeparateChaining();
      }
      System.out.print("Enter size: ");
      n = scanner.nextInt();
      test.makeTable(resolver, n, scanner);
    }
  }

  /**
   * This method simulates the GUI's "Next" button / Timer loop.
   */
  private Table table;
  private CollisionResolver resolver;
  private Scanner in;

  public void makeTable(CollisionResolver resolver, int n, Scanner in) {
    this.table = new Table(n);
    this.resolver = resolver;
    this.in = in;
    loopTable();
  }

  private void loopTable() {
    while (true) {
      resetScreen();
      table.reset();
      printTable(table);
      System.out.print("Input ops: ");
      String input = in.next();
      if (input.equals("q")) {
        return;
      }
      String i = in.next();
      HashAction action;
      switch (input) {
        case "i":
          action = HashAction.INSERT;
          break;

        case "d":
          action = HashAction.DELETE;
          break;

        case "s":
          action = HashAction.SEARCH;
          break;

        default:
          return;
      }
      loopAction(action, i);
    }
  }

  private void loopAction(HashAction action, String key) {
    resolver.getAlgorithmAndInitalize(action, key, table);
    Result result = null;
    while (true) {
      result = resolver.nextStep();
      resetScreen();
      printTable(table);
      System.out.println(result.message());
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        // Handle the exception if the thread is interrupted
        Thread.currentThread().interrupt();
      }
      if (result.currentLine() == -1)
        break;
    }
  }

  private static void resetScreen() {
    System.out.println("\u001b[H\u001b[2J");
  }

  /**
   * Renders the Table structure in the terminal.
   */
  private static void printTable(Table table) {
    System.out.println("Current Table State:");
    for (Row row : table.getRows()) {
      StringBuilder rowStr = new StringBuilder();
      rowStr.append(String.format("[%02d] | ", row.getIndex()));

      List<Item> items = row.getItems();
      if (items.isEmpty()) {
        rowStr.append("--- empty ---");
      } else {
        for (Item item : items) {
          String name = item.getName();
          if (item.isGhosted()) {
            rowStr.append("[").append(name).append(" (GHOST)] ");
          } else {
            rowStr.append("[").append(name).append("] ");
          }
        }
      }
      System.out.println(rowStr.toString());
    }
  }
}
