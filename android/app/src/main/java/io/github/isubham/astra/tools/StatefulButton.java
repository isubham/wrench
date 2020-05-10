package io.github.isubham.astra.tools;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StatefulButton {
    private Button active, loading, next;
    TextView successMessage;

    public StatefulButton(Button active, Button loading, Button next, TextView successMessage) {
        this.active = active;
        this.loading = loading;
        this.next = next;
        this.successMessage = successMessage;
        setActive();
    }

    private void hideAll() {
        active.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
        next.setVisibility(View.GONE);

        successMessage.setText("");
        successMessage.setVisibility(View.GONE);
    }


    public void setActive() {
        hideAll();
        active.setVisibility(View.VISIBLE);
    }

    public void setLoading() {
        hideAll();
        loading.setVisibility(View.VISIBLE);
    }


    public void setNext(String message) {
        hideAll();
        successMessage.setText(message);
        successMessage.setVisibility(View.VISIBLE);
        next.setVisibility(View.VISIBLE);
    }

}
