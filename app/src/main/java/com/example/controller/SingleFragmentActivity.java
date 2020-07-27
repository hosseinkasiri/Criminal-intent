package com.example.controller;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    public abstract Fragment createFragment();
    @LayoutRes
    public abstract int getLayoutResId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = createFragment();
        if (fragmentManager.findFragmentById(R.id.fragment_list_container) == null){

            fragmentManager.beginTransaction().
                    add(R.id.fragment_list_container, fragment)
                    .commit();
        }
    }
}
