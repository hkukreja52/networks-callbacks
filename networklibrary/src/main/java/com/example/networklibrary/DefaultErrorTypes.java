package com.example.networklibrary;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Hemant on 7/12/2017.
 */

public enum DefaultErrorTypes implements ErrorType {

    NETWORK("NETWORK", "Network"),
    NONE("NONE", "None"),
    UNKNOWN("UNKNOWN", "Unknown");

    private String typeName;
    private String typeValue;

    DefaultErrorTypes(String typeName, String typeValue) {
        this.typeName = typeName;
        this.typeValue = typeValue;
    }

    @Override public String getTypeKey() {
        return typeName;
    }

    @Override public String getTypeValue() {
        return typeValue;
    }

    public static Map<String, ErrorType> map = new TreeMap<String, ErrorType>();

    static {
        for (ErrorType type : values()) {
            map.put(type.getTypeKey(), type);
        }
    }

    public static ErrorType typeFor(String typeName) {
        return map.get(typeName);
    }
}
