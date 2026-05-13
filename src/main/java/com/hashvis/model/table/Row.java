package com.hashvis.model.table;

import java.util.*;

public class Row {
  // Row index
  private int index;
  // Row is already checked (for post-selected highlighting)
  private boolean checked = false;
  // Row is currently selected
  private boolean chosen = false;
  // Items in the row
  private ArrayList<Item> items = new ArrayList<Item>();

  // Current checked item
  private int currentItem = -1;

  public Row(int index) {
    this.index = index;
  }

  public Item nextItem() {
    if (items.size() == 0)
      return null;
    if (currentItem == items.size())
      return null;
    if (currentItem != -1)
      items.get(currentItem).unchoose();
    if (++currentItem == items.size())
      return null;
    Item item = items.get(currentItem);
    item.choose();
    return item;
  }

  public void addItem(String name) {
    Item item = new Item(name);
    items.add(item);
    if (currentItem != -1 && currentItem < items.size())
      items.get(currentItem).unchoose();
    currentItem = items.size() - 1;
    item.choose();
  }

  public void removeItem(Item item) {
    items.remove(item);
  }

  public List<Item> getItems() {
    return Collections.unmodifiableList(items);
  }

  public void choose() {
    checked = true;
    chosen = true;
  }

  public void unchoose() {
    checked = false;
    chosen = false;
  }

  public void reset() {
    checked = false;
    chosen = false;
    currentItem = -1;
    for (Item item : items)
      item.reset();
  }

  public int getIndex() {
    return index;
  }

  public boolean isChosen() {
    return chosen;
  }

  public boolean isChecked() {
    return checked;
  }

}
