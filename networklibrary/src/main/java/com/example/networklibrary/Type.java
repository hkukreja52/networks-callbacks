package com.example.networklibrary;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Hemant on 7/12/2017.
 */

public enum Type implements ErrorType {

    /*AUTHENTICATION("AUTHENTICATION", "Authentication"),
    ACCESS("ACCESS", "Access"),
    SYSTEM("SYSTEM", "System"),
    VERSION("VERSION", "version"),*/
    NETWORK("NETWORK","Network"),
    NONE("NONE", "None"),
    UNKNOWN("UNKNOWN", "Unknown");

    private String typeName;
    private String typeValue;

    Type(String typeName, String typeValue) {
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

    /*public static void addNewType(TypeInterface type) {
        if (!map.containsKey(type.getTypeKey())) {
            map.put(type.getTypeKey(), type);
        }
    }*/
}
