package com.hashvis.model.collision;

import java.util.ArrayList;
import java.util.List;

import com.hashvis.model.hashfunc.*;
import com.hashvis.model.table.*;
public abstract class ActionProcessor extends HashResolver{
	protected Integer hashValue = null;
    protected Row currentRow = null;
  	protected ArrayList<String> getPseudocode(HashAction action) {
    	ArrayList<String> pseudocode = new ArrayList<String>();
    	pseudocode.add("TODO: Add pseudocode that reflect actual algorithm well");
    	pseudocode.add("The actual language, syntax, ... will be defined later");
    	return pseudocode;
  	}
  	protected ArrayList<String> initializeActionProcess(HashAction action, String key, Table table) {
	    initializeHashResolver(action, key, table);
	    hashValue = null;
	    currentRow = null;
	    return getPseudocode(action);
	}

  	protected Result handleHashing() {
    	hashValue = hashFunc.compute(key, table.size());
    	return new Result("Hash value: " + hashValue, 0);
  	}
}