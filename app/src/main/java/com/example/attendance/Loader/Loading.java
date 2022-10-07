package com.example.attendance.Loader;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.ViewGroup;

import com.example.attendance.R;

public class Loading {
    private Activity activity;
    private AlertDialog dialog;

    public Loading(Activity myActivity) {
        this.activity = myActivity;
    }

    public void startLoading() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
        builder.setView(this.activity.getLayoutInflater().inflate(R.layout.loading, (ViewGroup) null));
        builder.setCancelable(false);
        AlertDialog create = builder.create();
        this.dialog = create;
        create.show();
    }

    public void dismissDialog() {
        this.dialog.dismiss();
    }
}
