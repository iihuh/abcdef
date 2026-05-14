package com.hashvis.model.table;

import java.util.*;

public class Row {

  public enum RowState {
    NORMAL,
    SELECTED,
    POSTSELECTED,
  }

  public interface RowListener {
    void stateChanged(RowState state);

    void itemAdded(Item item);

    void itemRemoved(Item item);
  }

  // Row index
  private int index;
  // Row is already checked (for post-selected highlighting)
  private RowState state = RowState.NORMAL;
  // Items in the row
  private ArrayList<Item> items = new ArrayList<Item>();

  private RowListener listener;

  // Current checked item
  private int currentItem = -1;

  public Row(int index) {
    this.index = index;
  }

  public void setListener(RowListener listener) {
    this.listener = listener;
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
    if (listener != null)
      listener.itemAdded(item);
    if (currentItem != -1 && currentItem < items.size())
      items.get(currentItem).unchoose();
    currentItem = items.size() - 1;
    item.choose();
  }

  public void removeItem(Item item) {
    items.remove(item);
    if (listener != null)
      listener.itemRemoved(item);
  }

  public List<Item> getItems() {
    return Collections.unmodifiableList(items);
  }

  public void choose() {
    state = RowState.SELECTED;
    if (currentItem != -1)
      items.get(currentItem).choose();
    if (listener != null)
      listener.stateChanged(state);
  }

  public void unchoose() {
    state = RowState.POSTSELECTED;
    if (currentItem != -1)
      items.get(currentItem).unchoose();
    if (listener != null)
      listener.stateChanged(state);
  }

  public void reset() {
    state = RowState.NORMAL;
    currentItem = -1;
    for (Item item : items)
      item.reset();
    if (listener != null)
      listener.stateChanged(state);
  }

  public int getIndex() {
    return index;
  }

}
