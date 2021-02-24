package com.graylabs.hasvi.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.graylabs.hasvi.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.List;
import java.util.Random;

import static androidx.core.content.FileProvider.getUriForFile;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChooserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooserFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private AppCompatImageButton mCamBtn;
    private AppCompatImageButton mGalleryBtn;
    private AppCompatImageButton mRemoveBtn;
    private ConstraintLayout mRoot;
    private String fileName = "";
    private ActivityResultLauncher<Intent> startForResult;

    public ChooserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChooserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChooserFragment newInstance(String param1, String param2) {
        ChooserFragment fragment = new ChooserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ThemeOverlay_Demo_BottomSheetDialog);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        startForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data  = result.getData();
                        if(data != null){
                            getUriForResult(data.getData());
                        }else{
                            getUriForResult(getCacheImagePath(fileName));
                        }
                    }
                }
        );
    }

    private void getUriForResult(Uri data) {
        Bundle result = new Bundle();
        result.putString("bundleKey", data.toString());
        getParentFragmentManager().setFragmentResult("requestKey", result);
        dismissAllowingStateLoss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chooser, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCamBtn = view.findViewById(R.id.from_camera_view);
        mGalleryBtn = view.findViewById(R.id.from_gallery_view);
        mRemoveBtn = view.findViewById(R.id.remove_image_view);
        mRoot = view.findViewById(R.id.chooser_root);
        mGalleryBtn.setOnClickListener(this);
        mCamBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.from_camera_view:
                askPermission(true);
                break;
            case R.id.from_gallery_view:
                askPermission(false);
                break;
        }
    }

    private void askPermission(boolean isCamera) {
        Dexter.withContext(requireContext())
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if(multiplePermissionsReport.areAllPermissionsGranted()){
                            fileName = generateRandomString(6) + ".jpg";
                            if(isCamera == true){
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, getCacheImagePath(fileName));
                                startForResult.launch(cameraIntent);
                            }
                            else{
                                Intent galleryIntent = new Intent();
                                galleryIntent.setType("image/*");
                                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                                startForResult.launch(galleryIntent);
                            }
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }

    private String queryName(ContentResolver contentResolver, Uri selectedImageUri) {
        Cursor cursor = contentResolver.query(selectedImageUri, null, null, null, null);
        assert(cursor != null);
        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        String name = cursor.getString(nameIndex);
        cursor.close();
        return name;
    }

    private Uri getCacheImagePath(String fileName) {
        File path = new File(requireContext().getExternalCacheDir(), "camera");
        if(!path.exists()) path.mkdirs();
        File image = new File(path, fileName);
        return getUriForFile(requireContext(), requireContext().getPackageName() + ".fileprovider", image);
    }


    private String generateRandomString(int length) {
        Random rnd = new Random();
        char[] allowChars = "ABCDEFGHIJKLOMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        String result = "";
        for(int i = 0; i <= length; i++){
            result += allowChars[rnd.nextInt(allowChars.length)];
        }
        return result;
    }
}