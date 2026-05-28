package com.hashvis.model.table;

/**
 * Represents a single item within a row. Each item has a name and a state
 * that transitions through NORMAL, SELECTED, POSTSELECTED, and GHOSTED as
 * the user interacts with the table.
 */
public class Item {

  /**
   * Possible states of an item during selection and navigation.
   */
  public enum ItemState {
    NORMAL,
    SELECTED,
    POSTSELECTED,
    GHOSTED
  }

  /**
   * Listener interface for receiving item state change notifications.
   */
  public interface ItemListener {
    /**
     * Invoked when the item's state has changed.
     *
     * @param state the new item state
     */
    void stateChanged(ItemState state);
  }

  private String name;
  private ItemState state = ItemState.NORMAL;

  /**
   * Creates a new item with the given name.
   *
   * @param name the display name for this item
   */
  public Item(String name) {
    this.name = name;
  }

  private ItemListener listener;

  /**
   * Registers a listener to be notified of state changes on this item.
   *
   * @param listener the listener to set, or {@code null} to clear
   */
  public void setListener(ItemListener listener) {
    this.listener = listener;
  }

  private void notifyListener() {
    if (listener != null)
      listener.stateChanged(state);
  }

  /**
   * Marks this item as ghosted, making it permanently inactive and
   * unresponsive to further selection or deselection.
   */
  public void ghost() {
    state = ItemState.GHOSTED;
    notifyListener();
  }

  /**
   * Selects this item. Does nothing if the item is already ghosted.
   */
  public void choose() {
    if (state == ItemState.GHOSTED)
      return;
    state = ItemState.SELECTED;
    notifyListener();
  }

  /**
   * Deselects this item by transitioning to POSTSELECTED. Does nothing
   * if the item is ghosted or already in NORMAL state.
   */
  public void unchoose() {
    if (state == ItemState.GHOSTED)
      return;
    if (state == ItemState.NORMAL)
      return;
    state = ItemState.POSTSELECTED;
    notifyListener();
  }

  /**
   * Resets this item back to NORMAL state. Does nothing if the item is
   * ghosted.
   */
  public void reset() {
    if (state == ItemState.GHOSTED)
      return;
    state = ItemState.NORMAL;
    notifyListener();
  }

  /**
   * Returns whether this item is in the ghosted state.
   *
   * @return {@code true} if the item is ghosted, {@code false} otherwise
   */
  public boolean isGhosted() {
    return state == ItemState.GHOSTED;
  }

  /**
   * Returns the display name of this item.
   *
   * @return the item name
   */
  public String getName() {
    return name;
  }
}
