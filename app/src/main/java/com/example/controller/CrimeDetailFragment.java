package com.example.controller;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.models.Crime;
import com.example.models.CrimeLab;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeDetailFragment extends Fragment {

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton ,mTimeButton;
    private CheckBox mSolvedCheckBox;
    private static final String mArgsId = "ARGS ID",mDIALOG_TAG = "dialog tag",mDIALOG_TIME = "dialog time";
    private static final int mREQ_CODE = 0,mREQ_CODE_time = 1;

    public static CrimeDetailFragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(mArgsId , id);
        CrimeDetailFragment fragment = new CrimeDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(mArgsId);
        mCrime = CrimeLab.getInstance().getCrime(crimeId);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_crime, container, false);
        findViews(view);
        mTitleField.setText(mCrime.getTitle());
        mDateButton.setText(mCrime.getDate().toString());
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mTimeButton.setText(getTime());

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mCrime.getDate());
                    datePickerFragment.setTargetFragment(CrimeDetailFragment.this, mREQ_CODE);
                    datePickerFragment.show(getFragmentManager(), mDIALOG_TAG);
            }
        });
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mCrime.getDate());
               timePickerFragment.setTargetFragment(CrimeDetailFragment.this, mREQ_CODE_time);
               timePickerFragment.show(getFragmentManager(),mDIALOG_TIME);
            }
        });

        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_detail,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_crime:
                CrimeLab.getInstance().deleteCrime(mCrime);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String getTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mCrime.getDate());
        Date date = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(date);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mREQ_CODE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.getArgDate());
            mCrime.setDate(date);
            mDateButton.setText(date.toString());
        }
        if (requestCode == mREQ_CODE_time){
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.getArgDate());
            mCrime.setDate(date);
            mTimeButton.setText(getTime());
        }
    }
    
    private void findViews(View view) {
        mTitleField = view.findViewById(R.id.edit_txt);
        mDateButton = view.findViewById(R.id.date_button);
        mSolvedCheckBox = view.findViewById(R.id.crime_solved);
        mTimeButton = view.findViewById(R.id.time_button);
    }

    private boolean phoneOrTablet(){
        TelephonyManager manager = (TelephonyManager) getActivity().getSystemService(getActivity().TELEPHONY_SERVICE);
        if (manager.getPhoneType()==TelephonyManager.PHONE_TYPE_NONE){
            return true;
        }
        else
            return false;
    }
}
