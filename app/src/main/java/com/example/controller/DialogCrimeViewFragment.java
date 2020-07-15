package com.example.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.helper.PictureUtils;

import java.io.File;

public class DialogCrimeViewFragment extends DialogFragment {

    private static final String ARG_PHOTO = "com.example.controller_photoFile";
    private ImageView mImageView;
    private File mPhotoFile;

    public static DialogCrimeViewFragment newInstance(File photoFile) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_PHOTO , photoFile);
        DialogCrimeViewFragment fragment = new DialogCrimeViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_dialog_crime_view,null);
        mImageView = view.findViewById(R.id.dialog_Image_view);
        mPhotoFile = (File) getArguments().getSerializable(ARG_PHOTO);
        if (mPhotoFile == null || !mPhotoFile.exists())
            mImageView.setImageDrawable(null);

        Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
        mImageView.setImageBitmap(bitmap);
        return new AlertDialog.Builder(getActivity()).
                setView(view).
                setPositiveButton(android.R.string.ok,null).
                create();
    }
}