package com.hashvis.model.collision;

public class QuadraticProbing extends OpenAddressing {
  @Override
  protected String getcurrent_ResolverType() {
    return "(base + step**2) % size of HT";
  }

  @Override
  protected int getBucketSelection(int probeCount) {
    return hashValue + probeCount * probeCount;
  }
}
