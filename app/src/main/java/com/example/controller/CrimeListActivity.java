package com.example.controller;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.models.Crime;

public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks , CrimeDetailFragment.Callbacks {

    @Override
    public Fragment createFragment() {
        return CrimeListFragment.newInstance();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_master_detail;
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if (findViewById(R.id.fragment_detail_container) == null) {
            //phone
            Intent intent = CrimePagerActivity.newIntent(this, crime.getId());
            startActivity(intent);
        }else {
            //tablet
            CrimeDetailFragment crimeDetailFragment = CrimeDetailFragment.newInstance(crime.getId());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_detail_container , crimeDetailFragment)
                    .commit();
        }
    }

    @Override
    public void onCrimeUpdate() {
        CrimeListFragment crimeListFragment = (CrimeListFragment) getSupportFragmentManager().
                findFragmentById(R.id.fragment_list_container);
        crimeListFragment.updateUi();
    }
}