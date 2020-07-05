package com.example.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.models.Crime;
import com.example.models.CrimeLab;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {

    private static final int CRIMES_FIRST_INDEX = 0;
    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private static String sEXTRA_id = "com.example.criminalintent.EXTRA ID";
    private Button mLastButton;
    private Button mFirstButton;

    public static Intent newIntent(Context context , UUID crimeId){
        Intent intent = new Intent(context , CrimePagerActivity.class);
        intent.putExtra(sEXTRA_id , crimeId);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        mViewPager = findViewById(R.id.view_pager_activity);
        mCrimes = CrimeLab.getInstance(this).getCrimes();
        mLastButton = findViewById(R.id.last_btn);
        mFirstButton = findViewById(R.id.first_btn);

        final UUID mCrimeId = (UUID) getIntent().getSerializableExtra(sEXTRA_id);

        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                UUID crimeId = (UUID) mCrimes.get(position).getId();
                return CrimeDetailFragment.newInstance(crimeId);
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        for (int i = 0; i < mCrimes.size(); i++){
            if (mCrimes.get(i).getId().equals(mCrimeId))
                mViewPager.setCurrentItem(i);
        }
        mViewPager.setOffscreenPageLimit(5);

       /* mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == mCrimes.size()-1)
                    mLastButton.setVisibility(View.GONE);
                else
                    mLastButton.setVisibility(View.VISIBLE);
                if (position == CRIMES_FIRST_INDEX)
                    mFirstButton.setVisibility(View.GONE);
                else
                    mFirstButton.setVisibility(View.VISIBLE);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
       mLastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mCrimes.size()-1);
            }
        });

       /* mFirstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CrimeLab.getInstance(CrimePagerActivity.this).item(mCrimeId) == 0)
                    mFirstButton.setVisibility(View.GONE);
                mViewPager.setCurrentItem(0);
            }
        });*/

    }
}
