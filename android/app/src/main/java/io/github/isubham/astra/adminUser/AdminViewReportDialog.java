package io.github.isubham.astra.adminUser;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

import io.github.isubham.astra.R;
import io.github.isubham.astra.databinding.AdminViewReportDialogBinding;
import io.github.isubham.astra.tools.CustomDatePickerFragment;
import io.github.isubham.astra.tools.DateUtils;

public class AdminViewReportDialog extends AppCompatActivity implements CustomDatePickerFragment.TheListener {

    //Dates
    private String currentDate, firstDateOfCurrentMonth, firstDateOfLast3rdMonth, firstDateOfLast9thMonth;

    private AdminViewReportDialogBinding binding;
    private boolean settingFromDate = true;
    private ArrayAdapter<String> adapter;
    private int spinnerPosSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AdminViewReportDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupSpinner();
    }

    private void setupSpinner() {
        List<String> durationList = formDurationList();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, durationList);
        binding.durationSpinner.setAdapter(adapter);

        binding.durationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                binding.errorHint.setVisibility(View.INVISIBLE);
                spinnerPosSelected = i;
                if (i == 4) {
                    binding.selectDateRangeLayout.setVisibility(View.VISIBLE);
                } else {
                    binding.selectDateRangeLayout.setVisibility(View.GONE);
                    binding.reportStartDate.setText(null);
                    binding.reportEndDate.setText(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private List<String> formDurationList() {
        List<String> durationList = new ArrayList<>();

        currentDate = DateUtils.getDate();
        firstDateOfCurrentMonth = DateUtils.getFirstDateOfMonth(0);
        firstDateOfLast3rdMonth = DateUtils.getFirstDateOfMonth(-2);
        firstDateOfLast9thMonth = DateUtils.getFirstDateOfMonth(-5);


        durationList.add("Today  " + "(" + currentDate + ")");
        durationList.add("Current Month  " + "(" + firstDateOfCurrentMonth + " - " + currentDate + ")");
        durationList.add("Last 3 Month  " + "(" + firstDateOfLast3rdMonth + " - " + currentDate + ")");
        durationList.add("Last 6 Month  " + "(" + firstDateOfLast9thMonth + " - " + currentDate + ")");
        durationList.add("Select a date Range  ");
        return durationList;
    }

    @Override
    public void returnDate(String date) {
        if (settingFromDate)
            binding.reportStartDate.setText(date);
        else
            binding.reportEndDate.setText(date);
    }

    public void selectToDate(View view) {
        binding.errorHint.setVisibility(View.INVISIBLE);
        settingFromDate = false;
        DialogFragment fragment = new CustomDatePickerFragment();
        fragment.show(getSupportFragmentManager(), "date picker");
    }

    public void selectFromDate(View view) {
        binding.errorHint.setVisibility(View.INVISIBLE);
        settingFromDate = true;
        DialogFragment fragment = new CustomDatePickerFragment();
        fragment.show(getSupportFragmentManager(), "date picker");
    }

    public void downloadReport(View view) {
        if (spinnerPosSelected == 4) {
            if (TextUtils.isEmpty(binding.reportStartDate.getText()) || TextUtils.isEmpty(binding.reportEndDate.getText())) {
                binding.errorHint.setText(R.string.PLEASE_SELECT_THE_DATES);
                binding.errorHint.setVisibility(View.VISIBLE);
                return;
            }

            if (!DateUtils.isEndDateGreaterThanFromDate(binding.reportStartDate.getText().toString(), binding.reportEndDate.getText().toString())) {
                binding.errorHint.setText(R.string.ERROR_INVALID_DATE_RANGE);
                binding.errorHint.setVisibility(View.VISIBLE);
                return;
            }
        }

        //TODO Write Code for Download Here (Use below date fields )
        // currentDate, firstDateOfCurrentMonth, firstDateOfLast3rdMonth, firstDateOfLast9thMonth;


    }
}
