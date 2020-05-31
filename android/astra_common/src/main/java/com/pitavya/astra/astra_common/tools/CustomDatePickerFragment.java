package com.pitavya.astra.astra_common.tools;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by faiz on 3/6/18.
 */

public class CustomDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    /*Genral Problem occurs .. that user is not capable of transfering data from dialog to activity
     *
     * Solution to above is  -----
     *
     *
     * Create a custom interface and make it return the date  &&  implement the interface in your required activity
     *
     * */


    private TheListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //         super.onCreateDialog(savedInstanceState);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        listener = (TheListener) getActivity();

        DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), this, year, month, day);

        mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());

        return mDatePicker;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String formattedDate = sdf.format(c.getTime());

        if (listener != null) {
            listener.returnDate(formattedDate);
        }
    }


    public interface TheListener {
        void returnDate(String date);
    }
}

