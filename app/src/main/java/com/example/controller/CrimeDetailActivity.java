package com.example.controller;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import java.util.UUID;

public class CrimeDetailActivity extends SingleFragmentActivity {

    private static String sEXTRA_id = "com.example.criminalintent.EXTRA ID";

    public static Intent newIntent(Context context , UUID id){

        Intent intent = new Intent(context , CrimeDetailActivity.class);

        intent.putExtra(sEXTRA_id, id);

        return intent;
    }

    @Override
    public Fragment createFragment() {

        UUID CrimeId = (UUID) getIntent().getSerializableExtra(sEXTRA_id);

        return CrimeDetailFragment.newInstance(CrimeId);
    }
}
