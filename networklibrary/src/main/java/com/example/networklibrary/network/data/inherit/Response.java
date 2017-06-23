package com.example.networklibrary.network.data.inherit;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created by aksha_000 on 12/24/2015.
 */
public class Response extends ResponseValidator {
    private static final String KEY_AUTHENTICATION = "Authentication";
    private static final String KEY_ACCESS = "Access";
    private static final String KEY_SYSTEM = "System";
    private static final String KEY_VERSION = "version";

    public enum Type {
        AUTHENTICATION,
        ACCESS,
        SYSTEM,
        VERSION,
        NONE,
        UNKNOWN
    }

    @SerializedName("status")
    private Boolean status;
    @SerializedName("error")
    private HashMap<String, String[]> errors;

    public Boolean getStatus() {
        return status;
    }

    public boolean hasErrors() {
        return errors != null;
    }

    public Type getErrorType() {
        Type type;

        if (!hasErrors())
            type = Type.NONE;
        else if (errors.containsKey(KEY_AUTHENTICATION))
            type = Type.AUTHENTICATION;
        else if (errors.containsKey(KEY_VERSION))
            type = Type.VERSION;
        else if (errors.containsKey(KEY_ACCESS))
            type = Type.ACCESS;
        else if (errors.containsKey(KEY_SYSTEM))
            type = Type.SYSTEM;
        else
            type = Type.UNKNOWN;

        return type;
    }

    public String generateErrorMessage() {
        if (!hasErrors()) {
            return "No errors found";
        }

        String message = null;

        for (String key : errors.keySet()) {
            for (String reason : errors.get(key)) {
                if (TextUtils.isEmpty(message))
                    message = reason;
                else
                    message = message.concat("\n" + reason);
            }
        }

        return message;
    }
}