package com.graylabs.hasvi.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.bitvale.switcher.SwitcherX;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.graylabs.hasvi.Adpaters.FragmentCardPagerAdapter;
import com.graylabs.hasvi.Models.CardItem;
import com.graylabs.hasvi.R;
import com.graylabs.hasvi.Utils.ShadowTransformer;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import kotlin.jvm.functions.Function1;

public class FirstFragment extends Fragment implements Function1 {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ViewPager mViewPager;
    private MaterialTextView mainHeader;
    private SwitcherX mLocationSwitch;

    private FragmentCardPagerAdapter mCardFragmentAdapter;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private FirebaseUser mCurrentUser;
    private FragmentManager childFragmentManager;

    private ActivityResultLauncher<Intent> startForResult;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSecond.
     */
    // TODO: Rename and change types and number of parameters
    @NotNull
    public static FirstFragment newInstance(String param1, String param2) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildLocationCallBack();
        childFragmentManager = getChildFragmentManager();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLocationSwitch = view.findViewById(R.id.location_btn);
        mViewPager = view.findViewById(R.id.carousel_scroller);
        mainHeader = view.findViewById(R.id.main_header);
        setUpViewPager();
        SetCurrentUsername();

        mLocationSwitch.setOnCheckedChangeListener(this);
    }

    private void SetCurrentUsername() {
        String fullName = mCurrentUser.getDisplayName();
        String firstName = "User";

        assert fullName != null;
        String[] parts = fullName.split("\\s+");
        if(parts.length==2) firstName = parts[0];
        else if(parts.length==3) firstName = parts[0];

        mainHeader.setText("Hello " + firstName + ", \nFind your doctor");
    }

    private void setUpViewPager() {
        CardFragment card1 = CardFragment.newInstance(new
                CardItem("Cardiology", "10 Doctors", R.drawable.ic_cardiologist));
        CardFragment card2 = CardFragment.newInstance(new
                CardItem("Gynecology", "8 Doctors", R.drawable.ic_gynecologist));
        CardFragment card3 = CardFragment.newInstance(new
                CardItem("Paediatrics", "6 Doctors", R.drawable.ic_pediatrician));
        CardFragment card4 = CardFragment.newInstance(new
                CardItem("Ophthalmology", "6 Doctors", R.drawable.ic_opthalmologist));
        CardFragment card5 = CardFragment.newInstance(new
                CardItem("Dermatology", "6 Doctors", R.drawable.ic_dermatologist));

        mCardFragmentAdapter = new FragmentCardPagerAdapter(childFragmentManager,
                dpToPixels(2, requireContext()));
        mCardFragmentAdapter.addCardFragment(card1);
        mCardFragmentAdapter.addCardFragment(card2);
        mCardFragmentAdapter.addCardFragment(card3);
        mCardFragmentAdapter.addCardFragment(card4);
        mCardFragmentAdapter.addCardFragment(card5);

        ShadowTransformer mFragmentCardShadowTransformer = new ShadowTransformer(mViewPager, mCardFragmentAdapter);
        mViewPager.setAdapter(mCardFragmentAdapter);
        mViewPager.setPageTransformer(false, mFragmentCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);
    }

    private void askPermission() {
        Dexter.withContext(requireContext())
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if(multiplePermissionsReport.areAllPermissionsGranted()){
                            Toast.makeText(requireContext(), "Location on", Toast.LENGTH_SHORT).show();
                            locationWizardry();
                        }
                        else {
                            mLocationSwitch.setChecked(false, true);
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }

    @SuppressLint("MissingPermission")
    private void locationWizardry() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        mFusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
            Location location = task.getResult();
            if(location != null){
                String loc = location.getProvider() + ":Accu:(" + location.getAccuracy() + "). Lat:" + location.getLatitude() + ",Lon:" + location.getLongitude();
                Toast.makeText(requireContext(), loc, Toast.LENGTH_SHORT).show();
            }
        });

        buildLocationRequest();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(requireActivity());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    startLocationUpdates();
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                resolvable.startResolutionForResult(requireActivity(), 500);

                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        if(mFusedLocationClient == null)
            return;
        startLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();

        if(mFusedLocationClient == null)
            return;
        stopLocationUpdates();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        startForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    assert result.getData() != null;
                    final LocationSettingsStates states = LocationSettingsStates.fromIntent(result.getData());
                    if(result.getResultCode() == Activity.RESULT_CANCELED){
                        if(mLocationSwitch.isChecked()) mLocationSwitch.setChecked(false, true);
                    }
                }
        );
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        if(!mLocationSwitch.isChecked()) mLocationSwitch.setChecked(true, true);
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(locationCallback);
        if(mLocationSwitch.isChecked()) mLocationSwitch.setChecked(false, true);
        Toast.makeText(requireContext(), "Location off", Toast.LENGTH_SHORT).show();
    }

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    @Override
    public Object invoke(Object o) {
        boolean isChecked = (boolean)o;
        if(isChecked) askPermission();
        else stopLocationUpdates();
        return null;
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(5000);
    }

    private void buildLocationCallBack() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(locationResult == null)
                    return;

                for (Location loc : locationResult.getLocations()) {

                }
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };
    }
}