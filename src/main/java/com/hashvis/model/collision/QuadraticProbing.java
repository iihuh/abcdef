package com.hashvis.model.collision;

import java.util.List;

import com.hashvis.model.table.Table;

/**
 * Quadratic probing collision resolution strategy. When a collision occurs,
 * probes buckets at offsets {@code 0, 1, 4, 9, ...} (the square of the
 * probe count) from the original hash value.
 */
public class QuadraticProbing extends OpenAddressing  {
  @Override
  public List<String> getAlgorithmAndInitalize(HashAction action, String key, Table table) {
    return defaultInitialize(action,key,table);
  }
  @Override
  protected Result handleBucketSelection() {
    if (probeCount == table.size())
      return handleFinalization();
    currentRow = table.getRow((hashValue + probeCount * probeCount) % table.size());
    probeCount++;
    return new Result("Accessing bucket index " + currentRow.getIndex(), 0);
  }
}