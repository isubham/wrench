package com.pitavya.astra.astra_common.anim;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class TypeWriterEffect extends androidx.appcompat.widget.AppCompatTextView {

    private CharSequence textToBeDispayed;
    private int countIndex;
    private long animDelay ; //in ms
    private Handler handler = new Handler();
    private Runnable charAdder = new Runnable() {
        @Override
        public void run() {
            setText(textToBeDispayed.subSequence(0, countIndex++));

            if (countIndex < textToBeDispayed.length()) {
                handler.postDelayed(charAdder, animDelay);
            }
        }
    };

    public TypeWriterEffect(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void animateText(CharSequence text) {

        textToBeDispayed = text;
        countIndex = 0;

        setText("");
        handler.removeCallbacks(charAdder);
        handler.postDelayed(charAdder, animDelay);

    }

    public void setAnimDelay(long delay) {
        animDelay = delay;
    }


}
