package com.pitavya.astra.astra_common;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.pitavya.astra.astra_common.databinding.ActivityFragmentContainerForPoliciesBinding;
import com.pitavya.astra.astra_common.tools.Constants;

public class FragmentContainerForPolicies extends AppCompatActivity {

    ActivityFragmentContainerForPoliciesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFragmentContainerForPoliciesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            int typeOfPolicy = b.getInt(Constants.POLICY_TYPE);
            loadFragment(typeOfPolicy);
        }

    }

    private void loadFragment(int typeOfPolicy) {

        if (typeOfPolicy == Constants.POLICY_EULA) {
            loadEulaFragment();
        } else {
            loadPrivacyPolicyFragment();
        }

    }

    private void loadEulaFragment() {
        EulaAgreement fragment = new EulaAgreement();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();

    }

    private void loadPrivacyPolicyFragment() {
        PrivacyPolicy fragment = new PrivacyPolicy();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }
}