package com.hashvis.model.table;

public class Item {

  public enum ItemState {
    NORMAL,
    SELECTED,
    POSTSELECTED,
    GHOSTED
  }

  public interface ItemListener {
    void stateChanged(ItemState state);
  }

  // Item name
  private String name;
  private ItemState state = ItemState.NORMAL;

  public Item(String name) {
    this.name = name;
  }

  private ItemListener listener;

  public void setListener(ItemListener listener) {
    this.listener = listener;
  }

  private void notifyListener() {
    if (listener != null)
      listener.stateChanged(state);
  }

  public void ghost() {
    state = ItemState.GHOSTED;
    notifyListener();
  }

  public void choose() {
    if (state == ItemState.GHOSTED)
      return;
    state = ItemState.SELECTED;
    notifyListener();
  }

  public void unchoose() {
    if (state == ItemState.GHOSTED)
      return;
    if (state == ItemState.NORMAL)
      return;
    state = ItemState.POSTSELECTED;
    notifyListener();
  }

  public void reset() {
    if (state == ItemState.GHOSTED)
      return;
    state = ItemState.NORMAL;
    notifyListener();
  }

  public boolean isGhosted() {
    return state == ItemState.GHOSTED;
  }

  public String getName() {
    return name;
  }
}
