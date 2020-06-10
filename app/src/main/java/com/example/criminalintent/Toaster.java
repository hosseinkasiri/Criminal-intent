package com.example.criminalintent;

import android.content.Context;
import android.widget.Toast;

public class Toaster {

    static void showToast(Context context, String text) {
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }



}
