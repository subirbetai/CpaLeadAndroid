package com.cpalead.app.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.cpalead.app.R;

public class Toasty extends Toast {

    @SuppressLint("InflateParams")
    public Toasty(Context ctx) {
        super(ctx);
        this.setView(LayoutInflater.from(ctx).inflate(R.layout.toast_custom, null));
    }

    public static Toasty success(Context ctx, String message, int duration) {
        Toasty toasty = new Toasty(ctx);
        View view = toasty.getView();
        CardView cardView = view.findViewById(R.id.cardView);
        cardView.setCardBackgroundColor(Color.parseColor("#ff1e7e34"));
        TextView toastMessage = view.findViewById(R.id.toastMessage);
        toastMessage.setText(message);
        toasty.setDuration(duration);
        return toasty;
    }

    public static Toasty error(Context con, String message, int duration) {
        Toasty toasty = new Toasty(con);
        View view = toasty.getView();
        CardView cardView = view.findViewById(R.id.cardView);
        cardView.setCardBackgroundColor(Color.parseColor("#D32F2F"));
        TextView toastMessage = view.findViewById(R.id.toastMessage);
        toastMessage.setText(message);
        toasty.setDuration(duration);
        return toasty;
    }

    @SuppressWarnings("EmptyMethod")
    public void show() {
        super.show();
    }
}