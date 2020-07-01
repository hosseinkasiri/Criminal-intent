package com.example.controller;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.models.Crime;
import com.example.models.CrimeLab;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeListFragment extends Fragment {

    private static final String VISIBLE_SUBTITLE = "visible_subtitle";
    private RecyclerView mRecyclerView;
    private CrimeAdapter mCrimeAdapter;
    private boolean mIsSubtitleVisible;

    public CrimeListFragment() {
        // Required empty public constructor
    }

    public static CrimeListFragment newInstance() {
        Bundle args = new Bundle();
        CrimeListFragment fragment = new CrimeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null)
            mIsSubtitleVisible = savedInstanceState.getBoolean(VISIBLE_SUBTITLE);

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mRecyclerView = view.findViewById(R.id.crime_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUi();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);
        MenuItem item = menu.findItem(R.id.show_subtitle);
        item.setTitle(mIsSubtitleVisible ? R.string.hide_subtitle : R.string.show_subtitle);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_crime:
                Crime crime = new Crime();
                CrimeLab.getInstance().addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(),crime.getId());
                startActivity(intent);
                return true;

            case R.id.show_subtitle:
                mIsSubtitleVisible = ! mIsSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void updateSubtitle() {
        int crimeCount = CrimeLab.getInstance().getCrimes().size();
        String subtitle = getString(R.string.subtitle_format,crimeCount);

        if (!mIsSubtitleVisible)
            subtitle = null;

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUi();
        updateSubtitle();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(VISIBLE_SUBTITLE,mIsSubtitleVisible);
    }

    private void updateUi() {
        CrimeLab crimeLab = CrimeLab.getInstance();
        List<Crime> crimes = crimeLab.getCrimes();
        if (mCrimeAdapter == null) {
            mCrimeAdapter = new CrimeAdapter(crimes);
            mRecyclerView.setAdapter(mCrimeAdapter);
        }
        else {
           // mCrimeAdapter.setCrimes(crimes);
            mCrimeAdapter.notifyDataSetChanged();
        }
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mPoliceImage;
        private Crime mCrime;

        public CrimeHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = itemView.findViewById(R.id.list_item_crime_title);
            mDateTextView = itemView.findViewById(R.id.list_item_crime_date);
            mPoliceImage = itemView.findViewById(R.id.police_image_view);
        }

        public void bind(Crime crime){
            mCrime = crime;
            mTitleTextView.setText(crime.getTitle());
            mDateTextView.setText(crime.getDate().toString());
            mPoliceImage.setVisibility(crime.isSolved() == true ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View v) {
            Intent intent = CrimePagerActivity.newIntent(getActivity() , mCrime.getId());
            startActivity(intent);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        private List<Crime> mCrimes;
        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }
        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_crime ,parent , false);
            CrimeHolder crimeHolder = new CrimeHolder(view);
            return crimeHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}
