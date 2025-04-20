package com.example.myapplication.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.fragments.GastosFragment;
import com.example.myapplication.fragments.GastosFuturosFragment;
import com.example.myapplication.fragments.CategoriasFragment;
import com.example.myapplication.fragments.ContasFragment;
import com.example.myapplication.fragments.FolhaPagamentoFragment;

public class FinancasViewPagerAdapter extends FragmentStateAdapter {

    public FinancasViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new GastosFragment();
            case 1:
                return new GastosFuturosFragment();
            case 2:
                return new CategoriasFragment();
            case 3:
                return new ContasFragment();
            case 4:
                return new FolhaPagamentoFragment();
            default:
                return new GastosFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5; // Número de tabs
    }
} 