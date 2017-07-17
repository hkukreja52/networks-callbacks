package com.example.networklibrary;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Hemant on 7/12/2017.
 */

public enum DefaultErrorTypes implements ErrorType {

    NETWORK("Network"),
    NONE("None"),
    UNKNOWN("Unknown");

    private String typeValue;

    DefaultErrorTypes(String typeValue) {
        this.typeValue = typeValue;
    }

    @Override public String getTypeValue() {
        return typeValue;
    }

    public static ArrayList<ErrorType> typeList = new ArrayList<>();

    static {
        for (ErrorType type : values()) {
            typeList.add(type);
        }
    }

    public static ErrorType typeFor(String typeName) {
        for (int i = 0; i < typeList.size(); i++) {
            if (typeName.equalsIgnoreCase(typeList.get(i).getTypeValue()))
                return typeList.get(i);
        }
        return null;
    }
}
