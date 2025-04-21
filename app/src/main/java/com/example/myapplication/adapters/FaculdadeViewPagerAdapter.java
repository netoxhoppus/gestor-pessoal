package com.example.myapplication.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.fragments.HorariosFragment;
import com.example.myapplication.fragments.EntregasFragment;
import com.example.myapplication.fragments.DisciplinasFragment;

public class FaculdadeViewPagerAdapter extends FragmentStateAdapter {
    private DisciplinasFragment disciplinasFragment;
    private HorariosFragment horariosFragment;
    private EntregasFragment entregasFragment;

    public FaculdadeViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                if (disciplinasFragment == null) {
                    disciplinasFragment = new DisciplinasFragment();
                }
                return disciplinasFragment;
            case 1:
                if (horariosFragment == null) {
                    horariosFragment = new HorariosFragment();
                }
                return horariosFragment;
            case 2:
                if (entregasFragment == null) {
                    entregasFragment = new EntregasFragment();
                }
                return entregasFragment;
            default:
                return new DisciplinasFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Disciplinas, Horários e Entregas
    }

    public DisciplinasFragment getDisciplinasFragment() {
        return disciplinasFragment;
    }

    public HorariosFragment getHorariosFragment() {
        return horariosFragment;
    }

    public EntregasFragment getEntregasFragment() {
        return entregasFragment;
    }
} 