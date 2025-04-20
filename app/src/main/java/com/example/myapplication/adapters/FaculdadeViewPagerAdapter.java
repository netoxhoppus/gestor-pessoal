package com.example.myapplication.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.fragments.HorariosFragment;
import com.example.myapplication.fragments.EntregasFragment;
import com.example.myapplication.fragments.DisciplinasFragment;

public class FaculdadeViewPagerAdapter extends FragmentStateAdapter {

    public FaculdadeViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new DisciplinasFragment();
            case 1:
                return new HorariosFragment();
            case 2:
                return new EntregasFragment();
            default:
                return new DisciplinasFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Disciplinas, Horários e Entregas
    }
} 