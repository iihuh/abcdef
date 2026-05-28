package com.hashvis.model.table;

import java.util.*;

/**
 * Represents a single row in a table. Each row has an index, a list of items,
 * and a selection state that tracks whether the row is currently chosen,
 * previously chosen, or neither.
 */
public class Row {

  /**
   * Possible states of a row during selection and navigation.
   */
  public enum RowState {
    NORMAL,
    SELECTED,
    POSTSELECTED,
  }

  /**
   * Listener interface for receiving row-level change notifications.
   */
  public interface RowListener {
    /**
     * Invoked when the row's selection state has changed.
     *
     * @param state the new row state
     */
    void stateChanged(RowState state);

    /**
     * Invoked when an item has been added to this row.
     *
     * @param item the item that was added
     */
    void itemAdded(Item item);

    /**
     * Invoked when an item has been removed from this row.
     *
     * @param item the item that was removed
     */
    void itemRemoved(Item item);
  }

  private int index;
  private RowState state = RowState.NORMAL;
  private ArrayList<Item> items = new ArrayList<Item>();

  private RowListener listener;

  private int currentItem = -1;

  /**
   * Creates a row with the given index.
   *
   * @param index the zero-based index of this row
   */
  public Row(int index) {
    this.index = index;
  }

  /**
   * Registers a listener to be notified of changes to this row and its
   * items.
   *
   * @param listener the listener to set, or {@code null} to clear
   */
  public void setListener(RowListener listener) {
    this.listener = listener;
  }

  /**
   * Advances to the next item in this row, deselecting the current item and
   * selecting the next one. Returns {@code null} if there are no items or
   * if the end of the list has been reached.
   *
   * @return the newly selected item, or {@code null} if no further items
   *         exist
   */
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

  /**
   * Adds a new item with the given name to this row. The new item is
   * immediately selected and becomes the current item.
   *
   * @param name the display name for the new item
   */
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

  /**
   * Removes the specified item from this row.
   *
   * @param item the item to remove
   */
  public void removeItem(Item item) {
    items.remove(item);
    if (listener != null)
      listener.itemRemoved(item);
  }

  /**
   * Returns an unmodifiable view of the items in this row.
   *
   * @return a list of items in this row
   */
  public List<Item> getItems() {
    return Collections.unmodifiableList(items);
  }

  /**
   * Marks this row as selected. The currently focused item is also selected.
   */
  public void choose() {
    state = RowState.SELECTED;
    if (currentItem != -1)
      items.get(currentItem).choose();
    if (listener != null)
      listener.stateChanged(state);
  }

  /**
   * Marks this row as post-selected after the user moves to another row.
   * The currently focused item is deselected.
   */
  public void unchoose() {
    state = RowState.POSTSELECTED;
    if (currentItem != -1)
      items.get(currentItem).unchoose();
    if (listener != null)
      listener.stateChanged(state);
  }

  /**
   * Resets this row to its initial state: NORMAL state, no current item,
   * and all items reset.
   */
  public void reset() {
    state = RowState.NORMAL;
    currentItem = -1;
    for (Item item : items)
      item.reset();
    if (listener != null)
      listener.stateChanged(state);
  }

  /**
   * Returns the zero-based index of this row.
   *
   * @return the row index
   */
  public int getIndex() {
    return index;
  }

}
