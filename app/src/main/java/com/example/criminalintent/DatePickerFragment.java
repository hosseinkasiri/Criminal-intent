package com.example.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class DatePickerFragment extends DialogFragment {

    public static DatePickerFragment newInstance() {

        Bundle args = new Bundle();
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date,null);
        return new AlertDialog.Builder(getActivity())
                .setTitle("Date Of crime:")
                .setPositiveButton(android.R.string.ok , null)
                .setView(view)
                .create();
    }
}