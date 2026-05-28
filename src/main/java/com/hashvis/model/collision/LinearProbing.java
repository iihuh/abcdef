package com.hashvis.model.collision;

import java.util.List;

import com.hashvis.model.table.Table;

/**
 * Linear probing collision resolution strategy. When a collision occurs,
 * probes successive buckets at offsets {@code 0, 1, 2, ...} from the
 * original hash value until an empty or available bucket is found.
 */
public class LinearProbing extends OpenAddressing {
  @Override
  public List<String> getAlgorithmAndInitalize(HashAction action, String key, Table table) {
    return defaultInitialize(action,key,table);
  }
  @Override
  protected Result handleBucketSelection() {
    if (probeCount == table.size())
      return handleFinalization();
    currentRow = table.getRow((hashValue + probeCount) % table.size());
    probeCount++;
    return new Result("Accessing bucket index " + currentRow.getIndex(), 0);
  }
}