package com.example.myapplication.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.fragments.ListaDisciplinasFragment;
import com.example.myapplication.fragments.EntregasFragment;

public class FaculdadeViewPagerAdapter extends FragmentStateAdapter {

    public FaculdadeViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ListaDisciplinasFragment();
            case 1:
                return new EntregasFragment();
            default:
                return new ListaDisciplinasFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Disciplinas e Entregas
    }
} 