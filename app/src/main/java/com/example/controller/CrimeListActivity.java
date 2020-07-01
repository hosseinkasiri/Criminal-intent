package com.example.controller;

import androidx.fragment.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity {


    @Override
    public Fragment createFragment() {
        return CrimeListFragment.newInstance();
    }
}