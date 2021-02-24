package com.graylabs.hasvi.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.graylabs.hasvi.Activities.SettingsActivity;
import com.graylabs.hasvi.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThirdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThirdFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextInputEditText mDobInput;
    private TextInputEditText mNameInput;
    private TextInputEditText mAddressInput;
    private TextInputEditText mEmailInput;
    private TextInputEditText mPhoneInput;
    private FirebaseAuth mAuth;
    private SweetAlertDialog alertDialog;
    private FloatingActionButton mChooseFab;
    private CircleImageView mProfileImg;

    public ThirdFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentThird.
     */
    // TODO: Rename and change types and number of parameters
    public static ThirdFragment newInstance(String param1, String param2) {
        ThirdFragment fragment = new ThirdFragment();
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
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }

        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                String result = bundle.getString("bundleKey");
                Uri imgUri = Uri.parse(result);
                Glide.with(requireActivity())
                        .asBitmap()
                        .load(imgUri)
                        .into(new BitmapImageViewTarget(mProfileImg) {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                mProfileImg.setImageBitmap(resource);
                                Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                    public void onGenerated(Palette p) {
                                        Palette.Swatch vibrant = p.getVibrantSwatch();
                                        if(vibrant != null)
                                            mProfileImg.setBorderColor(vibrant.getRgb());
                                    }
                                });
                            }
                        });
            }
        });

        mAuth = FirebaseAuth.getInstance();
        MaterialDatePicker.Builder mBuilder = MaterialDatePicker.Builder.datePicker();
        mBuilder.setTitleText("Select date of birth");
        MaterialDatePicker<Long> picker = mBuilder.build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDobInput = view.findViewById(R.id.dob_edit_text);
        mNameInput = view.findViewById(R.id.username_edit_text);
        mEmailInput = view.findViewById(R.id.email_edit_text);
        mAddressInput = view.findViewById(R.id.address_edit_text);
        mPhoneInput = view.findViewById(R.id.phone_edit_text);
        mProfileImg = view.findViewById(R.id.user_image);
        RadioGroup mGenderGroup = view.findViewById(R.id.gender_radio);
        AppCompatImageButton mMenuBtn = view.findViewById(R.id.menu_btn);
        mChooseFab = view.findViewById(R.id.choose_image_fab);
        mChooseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.add(new ChooserFragment(), "CHOOSER_TAG");
                ft.commitAllowingStateLoss();
            }
        });

        mMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(requireContext(), v);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.settings_item:
                                showSettingsActivity();
                                return true;
                            default:
                                showAlertDialog("Sure to sign out?");
                                return true;
                        }
                    }
                });
                menu.inflate(R.menu.profile_menu);
                menu.show();
            }
        });
        mGenderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.male_radio:

                        break;
                    default:

                        break;
                }
            }
        });

        mDobInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                builder.setTitleText("Select date of birth");
                MaterialDatePicker<Long> picker = builder.build();
                picker.show(getParentFragmentManager(), "DATE_PICKER_TAG");
                picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        TimeZone utc = TimeZone.getDefault();
                        int offsetFromUTC = utc.getOffset(new Date().getTime()) * -1;
                        // Create a date format, then a date object with our offset
                        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        Date date = new Date(selection + offsetFromUTC);
                        mDobInput.setText(simpleFormat.format(date));
                    }
                });
            }
        });
        setData();
    }

    private void showAlertDialog(String message) {
        alertDialog = new SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE);
        alertDialog.setTitle(getResources().getString(R.string.app_name));
        alertDialog.setContentText(message);
        alertDialog.setConfirmButton("Yes", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                alertDialog.dismissWithAnimation();
                sweetAlertDialog = new SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog.setTitleText("Loading");
                sweetAlertDialog.setCancelable(true);
                sweetAlertDialog.show();
            }
        });
        alertDialog.setCancelButton("No", null);
        alertDialog.show();
    }

    private void setData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        mNameInput.setText(currentUser.getDisplayName());
        mEmailInput.setText(currentUser.getEmail());
        mPhoneInput.setText(currentUser.getPhoneNumber());
    }

    private void showSettingsActivity() {
        Intent settingsIntent = new Intent(requireActivity(), SettingsActivity.class);
        startActivity(settingsIntent);
    }

    private void preventEdit(boolean isActive){
        mNameInput.setEnabled(isActive);
        mEmailInput.setEnabled(isActive);
        mAddressInput.setEnabled(isActive);
        mDobInput.setEnabled(isActive);
        mPhoneInput.setEnabled(isActive);
    }
}