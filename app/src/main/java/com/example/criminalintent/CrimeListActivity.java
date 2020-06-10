package com.example.criminalintent;

import androidx.fragment.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity {


    @Override
    public Fragment createFragment() {
        return CrimeListFragment.newInstance();
    }
}