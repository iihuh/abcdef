package com.hashvis.model.collision;

public class LinearProbing extends OpenAddressing {
  @Override
  protected int getBucketSelection(int probeCount) {
    return hashValue + probeCount;
  }

  @Override
  protected String getcurrent_ResolverType() {
    return "(base + step) % size of HT";
  }
}
