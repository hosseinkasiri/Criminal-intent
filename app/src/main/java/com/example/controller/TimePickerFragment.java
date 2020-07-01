package com.example.controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment {
    private static final String ARG_DATE = "com.example.criminalintent.date";
    private TimePicker mTimePicker;
    private Date mDate;
    public static String getArgDate() {
        return ARG_DATE;
    }

    public static TimePickerFragment newInstance(Date date) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE , date);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time,null);
        mTimePicker = view.findViewById(R.id.time_time_picker);
        mDate = (Date) getArguments().getSerializable(ARG_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        mTimePicker.setHour(calendar.get(Calendar.HOUR));
        mTimePicker.setMinute(calendar.get(Calendar.MINUTE));

        return new AlertDialog.Builder(getActivity())
                .setTitle("Time")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int hour = mTimePicker.getHour();
                        int minute = mTimePicker.getMinute();
                        Calendar c = Calendar.getInstance();
                        c.set(c.HOUR_OF_DAY,hour);
                        c.set(c.MINUTE,minute);
                        setResult(c);
                    }
                })
                .setView(view)
                .create();
    }
    private void setResult(Calendar c){
        Intent intent = new Intent();
        Date date = c.getTime();
        intent.putExtra(ARG_DATE,date);
        getTargetFragment().onActivityResult(getTargetRequestCode(),getTargetRequestCode(),intent);
    }
}

