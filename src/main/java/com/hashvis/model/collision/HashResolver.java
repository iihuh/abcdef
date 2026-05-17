package com.hashvis.model.collision;

import java.util.ArrayList;
import java.util.List;
import com.hashvis.model.hashfunc.*;
import com.hashvis.model.table.*;


public abstract class HashResolver implements CollisionResolver {

    protected HashFunction hashFunc;

    protected HashAction action;
    protected String key;
    protected Table table;

    protected void initializeHashResolver(HashAction action, String key, Table table) {
        this.action = action;
        this.key = key;
        this.table = table;
    }
    protected void initializeHashFunction(DataType type) {
        switch (type) {
            case INTEGER:
                hashFunc = new HashFunctionNumber();
                break;
            case STRING:
                hashFunc = new HashFunctionString();
                break;
            default:
                break;
        }
    }
    @Override
    public List<HashFunction> getHashFunctionFields(DataType dataType) {
        initializeHashFunction(dataType);
        ArrayList<HashFunction> result = new ArrayList<HashFunction>();
        result.add(hashFunc);
        return result;
    }
}