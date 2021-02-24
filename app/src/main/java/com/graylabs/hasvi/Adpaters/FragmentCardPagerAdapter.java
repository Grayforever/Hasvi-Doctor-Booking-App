package com.graylabs.hasvi.Adpaters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.graylabs.hasvi.Fragments.CardFragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentCardPagerAdapter extends FragmentStatePagerAdapter implements CardAdapter {
    private List<CardFragment> mFragments;
    private float mBaseElevation;

    public FragmentCardPagerAdapter(@NonNull FragmentManager fm, float baseElevation) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mFragments = new ArrayList<>();
        mBaseElevation = baseElevation;
    }

    public void addCardFragment(CardFragment cardFragment) {
        mFragments.add(cardFragment);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mFragments.get(position).getCardView();
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        mFragments.set(position, (CardFragment) fragment);
        return fragment;
    }
}
