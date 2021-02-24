package com.graylabs.hasvi.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.graylabs.hasvi.R;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;

import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhoneAuthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhoneAuthFragment extends Fragment implements View.OnClickListener, OnCountryPickerListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";
    private String intPhoneNumber;
    private MaterialTextView mCodeEditText;
    private TextInputEditText mPhoneEditText;
    private CountryPicker.Builder mBuilder;
    private CountryPicker picker;
    private Country mCountry;
    private ShapeableImageView mFlagImage;
    private MaterialButton mContinueBtn;
    private LinearLayout mCodeLayout;

    public PhoneAuthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhoneAuthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhoneAuthFragment newInstance(String param1, String param2) {
        PhoneAuthFragment fragment = new PhoneAuthFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mBuilder = new CountryPicker.Builder()
                .with(requireContext())
                .listener(this);
        picker = mBuilder.build();
        mCountry = picker.getCountryFromSIM() != null ? picker.getCountryFromSIM() : picker.getCountryByLocale(Locale.US);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_phone_auth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCodeEditText = view.findViewById(R.id.cc_edit_text);
        mPhoneEditText = view.findViewById(R.id.phone_edit_text);
        mFlagImage = view.findViewById(R.id.flag_image);
        mContinueBtn = view.findViewById(R.id.continue_btn);
        mCodeLayout = view.findViewById(R.id.cc_layout);

        mCodeLayout.setOnClickListener(this);
        mFlagImage.setImageResource(mCountry.getFlag());
        mCodeEditText.setText(mCountry.getDialCode());
        mContinueBtn.setOnClickListener(this);
        mPhoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mContinueBtn.setEnabled(mPhoneEditText.getText().length() >= 8);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.cc_layout:
                picker.showDialog((AppCompatActivity) getActivity());
                break;
            case R.id.continue_btn:
                intPhoneNumber = mCodeEditText.getText().toString().trim() + mPhoneEditText.getText().toString().trim();
                gotoNext(intPhoneNumber);
                break;
        }
    }

    private void gotoNext(String phoneNumber) {
        OtpFragment nextFragment = OtpFragment.newInstance(phoneNumber);
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.replace(R.id.onboarding_fcv, nextFragment);
        ft.addToBackStack(BACK_STACK_ROOT_TAG);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onSelectCountry(Country country) {
        mFlagImage.setImageResource(country.getFlag());
        mCodeEditText.setText(country.getDialCode());
    }
}