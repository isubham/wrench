package io.github.isubham.astra_client.tools;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import io.github.isubham.astra_client.R;

public abstract class CustomSnackbar {


    private String message;
    private View view;
    private String actionMessage;
    private Context context;

    public CustomSnackbar(Context context, String msg, String actionMessage, View v) {
        this.context = context;
        this.message = msg;
        this.view = v;
        this.actionMessage = actionMessage;
    }


    public void show() {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        sbView.setBackgroundColor(Color.parseColor("#418e8e"));
        textView.setTextColor(Color.WHITE);

        snackbar.show();
    }

    public void showWithAction() {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction(actionMessage, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActionClick(view);
            }
        });

        View sbView = snackbar.getView();

        //CustomSetting for SnackBar TextView
        TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        sbView.setBackgroundColor(Color.parseColor("#418e8e"));
        textView.setTextColor(Color.WHITE);
        textView.setTextAppearance(context, R.style.CustomSnackBarTextViewStyle);

        //CustomSetting for SnackBar Action Button
        Button actionButton = sbView.findViewById(com.google.android.material.R.id.snackbar_action);
        actionButton.setTextAppearance(context,R.style.CustomSnackBarActionButtonStyle);

        snackbar.show();
    }

    public abstract void onActionClick(View view);





}
