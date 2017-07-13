package com.example.networklibrary.network.data.inherit;

import android.text.TextUtils;
import android.util.Log;

import com.example.networklibrary.ErrorType;
import com.example.networklibrary.DefaultErrorTypes;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created by aksha_000 on 12/24/2015.
 */
public class Response extends ResponseValidator {
    private static final String KEY_NETWORK = "Network";

    @SerializedName("status")
    private Boolean status;
    @SerializedName("error")
    public HashMap<String, String[]> errors;

    public Boolean getStatus() {
        return status;
    }

    public boolean hasErrors() {
        return errors != null;
    }

    public boolean containsError(ErrorType type) {

        if (hasErrors()) {
            ErrorType errorType = DefaultErrorTypes.typeFor(type.toString());
            return errors.containsKey(errorType.getTypeValue());
        }

        return false;
    }

    public String checkErrorType() {

        if (hasErrors()) {
            for (String key : errors.keySet()) {
                ErrorType errorType = DefaultErrorTypes.typeFor(key.toUpperCase());
                if (errors.containsKey(errorType.getTypeValue()))
                    return key;
                else
                    return "Unknown";
            }
        }

        return "None";
    }

    public DefaultErrorTypes getErrorType() {
        DefaultErrorTypes type;

        if (!hasErrors())
            type = DefaultErrorTypes.NONE;
        else if (errors.containsKey(KEY_NETWORK))
            type = DefaultErrorTypes.NETWORK;
        else
            type = DefaultErrorTypes.UNKNOWN;

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
