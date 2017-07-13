package com.example.networklibrary.network.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.networklibrary.NetworkLibrary;
import com.example.networklibrary.R;
import com.example.networklibrary.network.data.inherit.Response;
import com.example.networklibrary.network.parsing.GsonRequest;
import com.example.networklibrary.shared_preferences.PreferenceKeys;
import com.example.networklibrary.views.CustomSnackbar;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by aksha_000 on 12/24/2015.
 */
public class NetworkHelper<T> {
    private static final String TAG = NetworkHelper.class.getSimpleName();

    protected enum MethodType {
        GET, POST, PUT
    }

    public enum Status {
        SUCCESS,
        ERROR_INVALID_DATA,
        ERROR_SERVER,
        ERROR_NETWORK,
        UNKNOWN
    }

    public interface Callback<T> {
        /**
         * Network call complete
         *
         * @param status   Check the status to handle success and errors
         * @param response Response is null only if network error
         * @return True if response was used false if generic response handling should be used
         */
        boolean onComplete(@NonNull Status status, @Nullable T response);
    }

    private Context context;
    private RequestQueue requestQueue;

    public NetworkHelper(Context context) {
        this.context = context.getApplicationContext();
        requestQueue = Volley.newRequestQueue(context);
    }

    protected Context getContext() {
        return context;
    }

    protected void addToQueue(Request request) {
        Log.d(TAG, "Sending request: " + request.getUrl());
        requestQueue.add(request);
    }

    protected void cancelRequests(Object tag) {
        requestQueue.cancelAll(tag);
    }

    protected void createAndMakeGsonRequest(@NonNull Object requestTag, @NonNull MethodType type, @NonNull String endpoint, @Nullable T bodyParams, final Class<T> responseClass, @Nullable final Callback callback) {
        Request request = createGsonRequest(requestTag, type, endpoint, bodyParams, responseClass, callback);
        addToQueue(request);
    }

    protected GsonRequest createGsonRequest(@NonNull final Object requestTag, @NonNull MethodType type, @NonNull String endpoint, @Nullable T bodyParams, final Class<T> responseClass, @Nullable final Callback callback) {
        int methodType;
        String url = NetworkLibrary.getBaseUrl() + endpoint;

        switch (type) {
            case GET:
                methodType = Request.Method.GET;
                break;
            case POST:
                methodType = Request.Method.POST;
                break;
            case PUT:
                methodType = Request.Method.PUT;
                break;
            default:
                methodType = Request.Method.GET;
        }

        GsonRequest request = new GsonRequest(methodType, url, bodyParams, responseClass, getHeaders(), new com.android.volley.Response.Listener() {
            @Override
            public void onResponse(Object response) {
                handleResponse(requestTag, response, callback, false);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Object response;
                boolean invalidData = false;

                try {
                    if (error.getCause() instanceof JsonParseException) {
                        Log.e(TAG, "parsing error", error.getCause());
                        invalidData = true;
                        response = null;
                    }
                    else {
                        Log.e(TAG, "error", error);
                        response = new Gson().fromJson(new String(error.networkResponse.data), responseClass);
                    }
                }
                catch (Throwable e) {
                    Log.e(TAG, "error", e);
                    response = null;
                }

                handleResponse(requestTag, response, callback, invalidData);
            }
        });

        request.setTag(requestTag);

        return request;
    }

    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        headers.put("Accept", "application/json, application/json.v2");

        if (prefs.contains(PreferenceKeys.AUTH_KEY))
            headers.put("X-API-KEY", prefs.getString(PreferenceKeys.AUTH_KEY, ""));

        headers.put("X-DEV-UUID", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));

        return headers;
    }

    private void handleResponse(@NonNull Object requestTag, @Nullable Object responseObject, @Nullable Callback callback, @NonNull boolean invalidData) {
        Response response = (Response) responseObject;
        Status status;

        if (invalidData)
            status = Status.ERROR_INVALID_DATA;
        else if (response == null)
            status = Status.ERROR_NETWORK;
        else if (response.getStatus() == false || response.hasErrors())
            status = Status.ERROR_SERVER;
        else
            status = Status.SUCCESS;

        if (callback != null && !callback.onComplete(status, responseObject)) {
            View rootView = null;

            if (requestTag instanceof Fragment)
                rootView = ((Fragment) requestTag).getView();
            else if (requestTag instanceof Activity)
                rootView = ((Activity) requestTag).findViewById(android.R.id.content);

            if (status == Status.SUCCESS) {
                // Do nothing
            }
            else if (rootView != null) {
                showSnackbarError(rootView, status, response);
            }
        }
    }

    private void showSnackbarError(@NonNull View rootView, @NonNull final Status status, @Nullable Response response) {
        CustomSnackbar snackbar = new CustomSnackbar(context, rootView);
        snackbar.setMessageColor(R.color.white);
        snackbar.setActionColor(R.color.button_positive);

        switch (status) {
            case ERROR_NETWORK:
                snackbar.setMessage(R.string.snackbar_network_message);
                break;
            case ERROR_SERVER:
                switch (response.checkErrorType()) {
                    case "Access":
                        snackbar.setMessage(R.string.snackbar_server_access);
                        snackbar.setAction(R.string.snackbar_action_log_in, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                NetworkLibrary.clearData();
                                NetworkLibrary.restartApp();
                            }
                        });
                        break;

                    case "Authentication":
                        snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
                        snackbar.setMessage(R.string.snackbar_server_authentication);
                        snackbar.setAction(R.string.snackbar_action_log_in, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                NetworkLibrary.clearData();
                                NetworkLibrary.restartApp();
                            }
                        });
                        break;

                    case "System":
                        if (response.hasErrors())
                            snackbar.setMessage(response.generateErrorMessage());
                        else
                            snackbar.setMessage(R.string.snackbar_server_system);
                        break;

                    case "Version":
                        if (response.hasErrors())
                            snackbar.setMessage(response.generateErrorMessage());
                        else
                            snackbar.setMessage(R.string.update_message);
                        snackbar.setAction(R.string.update, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Context context = getContext();

                                if (context == null)
                                    return;

                                String packageName = context.getPackageName();
                                try {
                                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                                }
                                catch (Throwable e) {
                                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
                                }
                            }
                        });
                        break;
                    case "Unknown":
                        if (response.hasErrors())
                            snackbar.setMessage(response.generateErrorMessage());
                        else
                            snackbar.setMessage(R.string.snackbar_server_message);
                        break;
                }
                break;

            case ERROR_INVALID_DATA:
                snackbar.setMessage(R.string.snackbar_data_message);
                break;

            default:
                snackbar.setMessage(R.string.snackbar_server_message);
                break;
        }

        snackbar.show();
    }
}
