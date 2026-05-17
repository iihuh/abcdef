package com.hashvis.model.collision;

import java.util.ArrayList;
import java.util.List;

import com.hashvis.model.hashfunc.*;
import com.hashvis.model.table.Table;

public class DoubleHashing extends OpenAddressing {
  protected Integer hashValue1 = null;
  protected Integer hashValue2 = null;
  protected HashFunction hashFunc1;
  protected HashFunction hashFunc2;
  @Override
  public List<HashFunction> getHashFunctionFields(DataType dataType) {
    switch (dataType) {
      case INTEGER:
        hashFunc1 = new HashFunctionNumber();
        hashFunc2 = new HashFunctionNumber();
        break;
      case STRING:
        hashFunc1 = new HashFunctionString();
        hashFunc2 = new HashFunctionString();
        break;
      default:
        throw new IllegalArgumentException("Unsupported data type: " + dataType);
    }
    ArrayList<HashFunction> result = new ArrayList<HashFunction>();
    result.add(hashFunc1);
    result.add(hashFunc2);
    return result;
  }
  @Override
  public List<String> getAlgorithmAndInitalize(HashAction action, String key, Table table) {
    hashValue1 = null;
    hashValue2 = null;
    return defaultInitialize(action,key,table);
  }
  @Override
  protected Result handleBucketSelection() {
    if (probeCount == table.size())
      return handleFinalization();
    currentRow = table.getRow((hashValue1 + probeCount * hashValue2) % table.size());
    probeCount++;
    return new Result("Accessing bucket index " + currentRow.getIndex(), 0);
  }
  @Override
  public Result nextStep() {
    if (hashValue1 == null){
      hashValue1 = hashFunc1.compute(key, table.size());
      return new Result("Hash value: " + hashValue1, 0);
    }
    if (hashValue2 == null){
      hashValue2 = hashFunc2.compute(key, table.size());
      if (hashValue2==0){hashValue2=1;}
      return new Result("Hash value: " + hashValue2, 0);
    }
    return loop();
  }
}
