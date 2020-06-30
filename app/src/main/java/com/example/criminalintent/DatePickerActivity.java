package com.example.criminalintent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.Date;

public class DatePickerActivity extends SingleFragmentActivity{

    private static final String DATE_EXTRA = "com.example.criminalintent_Date";


    public static Intent newIntent(Context context, Date date){
        Intent intent = new Intent(context,DatePickerActivity.class);
        intent.putExtra(DATE_EXTRA,date);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        Date date =  (Date) getIntent().getSerializableExtra(DATE_EXTRA);

        DatePickerFragment fragment = DatePickerFragment.newInstance(date);
        return fragment;
    }
}