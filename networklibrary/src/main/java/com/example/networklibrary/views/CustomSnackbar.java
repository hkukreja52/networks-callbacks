package com.example.networklibrary.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by aksha_000 on 12/24/2015.
 */
public class CustomSnackbar {
    private static final String TAG = CustomSnackbar.class.getSimpleName();

    private Context context;
    private Snackbar snackbar;
    private TextView messageView;

    public CustomSnackbar(Context context, View rootView) {
        this.context = context.getApplicationContext();

        snackbar = Snackbar.make(rootView, "", Snackbar.LENGTH_LONG);

        View snackbarView = snackbar.getView();

        messageView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) messageView.getLayoutParams();
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        messageView.setMaxLines(999);
    }

    public void setDuration(int duration) {
        snackbar.setDuration(duration);
    }

    public void setMessage(String message) {
        messageView.setText(message);
    }

    public void setMessage(int messageResId) {
        messageView.setText(messageResId);
    }

    public void setMessageColor(int colorResId) {
        messageView.setTextColor(ContextCompat.getColor(context, colorResId));
    }

    public void setActionColor(int colorResId) {
        snackbar.setActionTextColor(ContextCompat.getColor(context, colorResId));
    }

    public void setAction(int actionMessageResId, @Nullable View.OnClickListener listener) {
        snackbar.setAction(actionMessageResId, listener);
    }

    public void show() {
        snackbar.show();
    }

    public void dismiss() {
        snackbar.dismiss();
    }

    public boolean isShown() {
        return snackbar.isShown();
    }
}
