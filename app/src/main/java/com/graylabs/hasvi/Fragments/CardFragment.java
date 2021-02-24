package com.graylabs.hasvi.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.textview.MaterialTextView;
import com.graylabs.hasvi.Adpaters.CardAdapter;
import com.graylabs.hasvi.Models.CardItem;
import com.graylabs.hasvi.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private CardView mCardView;
    private CardItem mCardItem;
    private MaterialTextView mTitleTextView;
    private MaterialTextView mCountTextView;
    private AppCompatImageView mContentImageView;

    public CardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param item Parameter 1.

     * @return A new instance of fragment CardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CardFragment newInstance(CardItem item) {
        CardFragment fragment = new CardFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCardItem = (CardItem) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCardView = view.findViewById(R.id.frag_card_root);
        mTitleTextView = view.findViewById(R.id.doc_name_tv);
        mCountTextView = view.findViewById(R.id.doc_pos_tv);
        mContentImageView = view.findViewById(R.id.content_image_view);

        if(mCardItem == null)
            return;

        mTitleTextView.setText(mCardItem.getTitle());
        mCountTextView.setText(mCardItem.getText());

        mCardView.setMaxCardElevation(mCardView.getCardElevation()
                * CardAdapter.MAX_ELEVATION_FACTOR);

        Glide.with(this)
                .asBitmap()
                .load(mCardItem.getImgId())
                .into(new BitmapImageViewTarget(mContentImageView) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        mContentImageView.setImageBitmap(resource);
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            public void onGenerated(Palette p) {
                                Palette.Swatch vibrant = p.getVibrantSwatch();
                                if(vibrant != null){
                                    mCardView.setCardBackgroundColor(vibrant.getRgb());
                                    mTitleTextView.setTextColor(vibrant.getTitleTextColor());
                                    mCountTextView.setTextColor(vibrant.getBodyTextColor());
                                }
                            }
                        });
                    }
                });
    }

    public CardView getCardView(){
        return mCardView;
    }
}