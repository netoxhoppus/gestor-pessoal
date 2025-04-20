package com.example.myapplication.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.fragments.ListaDisciplinasFragment;
import com.example.myapplication.fragments.HorariosFragment;

public class DisciplinasViewPagerAdapter extends FragmentStateAdapter {

    public DisciplinasViewPagerAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ListaDisciplinasFragment();
            case 1:
                return new HorariosFragment();
            default:
                throw new IllegalStateException("Posição inválida " + position);
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Disciplinas e Horários
    }
} 