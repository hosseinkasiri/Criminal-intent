package com.example.controller;

import androidx.fragment.app.Fragment;

import java.io.Serializable;

public class FragmentContainer implements Serializable {

    int requestCode;
    Fragment mFragment;

    public FragmentContainer(int requestCode, Fragment fragment) {
        this.requestCode = requestCode;
        mFragment = fragment;
    }
}
