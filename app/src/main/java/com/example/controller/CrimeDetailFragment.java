package com.example.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.provider.MediaStore;
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
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.helper.PictureUtils;
import com.example.models.Crime;
import com.example.models.CrimeLab;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeDetailFragment extends Fragment {

    private static final String mArgsId = "ARGS ID",mDIALOG_TAG = "dialog tag",mDIALOG_TIME = "dialog time";
    private static final int REQ_CODE = 0, REQ_CODE_TIME = 1,REQ_CONTACT = 2,REQ_CAMERA = 3;
    private static final String TAG_PHOTO = "com.example.controller_photo_tag";
    private Crime mCrime;
    private File mPhotoFile;
    private EditText mTitleField;
    private Button mDateButton ,mTimeButton;
    private CheckBox mSolvedCheckBox;
    private Button mSuspectButton,mReportButton;
    private ImageView mPhotoView;
    private ImageButton mPhotoButton;
    private Callbacks mCallbacks;

    public interface Callbacks{
        void onCrimeUpdate();
    }

    public static CrimeDetailFragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(mArgsId , id);
        CrimeDetailFragment fragment = new CrimeDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Callbacks)
            mCallbacks = (Callbacks) context;
        else
            throw new RuntimeException("Activity not implement callbacks");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(mArgsId);
        mCrime = CrimeLab.getInstance(getActivity()).getCrime(crimeId);
        mPhotoFile = CrimeLab.getInstance(getActivity()).getPhotoFile(mCrime);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_crime_detail, container, false);
        findViews(view);
        handelDateButton();
        handelTimeButton();
        handelSuspectButton();
        handelReportButton();
        handelPhotoButton();
        handelTitle();
        handelSolvedCheckBox();
        updatePhotoView();
        handelPhotoView();
        return view;
    }

    private void handelPhotoView() {
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCrimeViewFragment dialogCrimeViewFragment = DialogCrimeViewFragment.newInstance(mPhotoFile);
                dialogCrimeViewFragment.show(getFragmentManager(),TAG_PHOTO);
            }
        });
    }

    private void handelTimeButton() {
        mTimeButton.setText(getTime());
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mCrime.getDate());
               timePickerFragment.setTargetFragment(CrimeDetailFragment.this, REQ_CODE_TIME);
               timePickerFragment.show(getFragmentManager(),mDIALOG_TIME);
            }
        });
    }

    private void handelPhotoButton() {
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri uri = getPhotoFileUri();
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                PackageManager packageManager = getActivity().getPackageManager();
                List<ResolveInfo> activities = packageManager.queryIntentActivities(captureIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo activity : activities){
                    getActivity().grantUriPermission(activity.activityInfo.packageName,
                            uri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureIntent,REQ_CAMERA);
            }
        });
    }

    private void handelReportButton() {
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reportIntent = new Intent();
                reportIntent.setAction(Intent.ACTION_SEND);
                reportIntent.setType("text/plain");
                reportIntent.putExtra(Intent.EXTRA_TEXT,getCrimeReport());
                reportIntent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.crime_report_suspect));
                startActivity(Intent.createChooser(reportIntent,getString(R.string.send_report)));
            }
        });
    }

    private void handelSuspectButton() {
        if (mCrime.getSuspect() != null)
            mSuspectButton.setText(mCrime.getSuspect());

        final Intent chooseContact = new Intent(Intent.ACTION_PICK);
        chooseContact.setType(ContactsContract.Contacts.CONTENT_TYPE);
        final PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(chooseContact,PackageManager.MATCH_DEFAULT_ONLY) == null)
            mSuspectButton.setEnabled(false);

        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(chooseContact,REQ_CONTACT);
            }
        });
    }

    private void handelDateButton() {
        mDateButton.setText(mCrime.getDate().toString());
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mCrime.getDate());
                    datePickerFragment.setTargetFragment(CrimeDetailFragment.this, REQ_CODE);
                    datePickerFragment.show(getFragmentManager(), mDIALOG_TAG);
            }
        });
    }

    private void handelSolvedCheckBox() {
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
                onCrimeUpdate();
            }
        });
    }

    private void handelTitle() {
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
                onCrimeUpdate();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private Uri getPhotoFileUri() {
        return FileProvider.getUriForFile(getActivity(),
                            "com.example.controller.fileProvider",
                            mPhotoFile);
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
                CrimeLab.getInstance(getActivity()).deleteCrime(mCrime);
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
        if (requestCode == REQ_CODE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.getArgDate());
            mCrime.setDate(date);
            mDateButton.setText(date.toString());
            onCrimeUpdate();
        }
        if (requestCode == REQ_CODE_TIME){
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.getArgDate());
            mCrime.setDate(date);
            mTimeButton.setText(getTime());
            onCrimeUpdate();
        }
        else if (requestCode == REQ_CONTACT){
            Uri contactUri = data.getData();

            Cursor cursor = getActivity().getContentResolver().query(
                    contactUri,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            if (cursor.getCount() == 0)
                return;

            try {
                cursor.moveToFirst();
                String suspect = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
                onCrimeUpdate();

            } finally {
                cursor.close();
            }
        }
        else if (requestCode == REQ_CAMERA){
            Uri uri = getPhotoFileUri();
            getActivity().revokeUriPermission(uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updatePhotoView();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.getInstance(getActivity()).update(mCrime);
    }

    private void findViews(View view) {
        mTitleField = view.findViewById(R.id.edit_txt);
        mDateButton = view.findViewById(R.id.date_button);
        mSolvedCheckBox = view.findViewById(R.id.crime_solved);
        mTimeButton = view.findViewById(R.id.time_button);
        mSuspectButton = view.findViewById(R.id.crime_suspect);
        mReportButton = view.findViewById(R.id.crime_report);
        mPhotoView = view.findViewById(R.id.crime_image);
        mPhotoButton = view.findViewById(R.id.crime_camera);
    }

    private String getCrimeReport(){
        String report = null;
        String dateString = new SimpleDateFormat("yyyy/MM/dd").format(mCrime.getDate());
        String solvedString = mCrime.isSolved() ? getString(R.string.crime_report_solved)
                : getString(R.string.crime_report_unsolved);
        String suspectString = mCrime.getSuspect() == null ? getString(R.string.crime_report_no_suspect)
                : getString(R.string.crime_report_suspect,mCrime.getSuspect());

        report = getString(R.string.crime_report,mCrime.getTitle(),dateString,solvedString,suspectString);
        return report;
    }

    private void updatePhotoView(){
        if (mPhotoFile == null || !mPhotoFile.exists())
             mPhotoView.setImageDrawable(null);
        else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(),getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    private void onCrimeUpdate(){
        CrimeLab.getInstance(getActivity()).update(mCrime);
        mCallbacks.onCrimeUpdate();
    }
}
