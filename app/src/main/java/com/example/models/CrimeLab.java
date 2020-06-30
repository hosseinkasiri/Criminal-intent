package com.example.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class CrimeLab {

    private List<Crime> mCrimes ;

    private static CrimeLab instance ;

    private CrimeLab(){
        mCrimes = new ArrayList<>();
       /* for (int i = 0 ; i < 100 ; i++){

            Crime crime = new Crime();
            crime.setTitle("Crime#" + i);
            crime.setSolved(i % 2 == 0);


            mCrimes.put(crime.getId() , crime);

        }*/
    }

    public static CrimeLab getInstance(){

        if (instance == null){
            instance = new CrimeLab();
        }
        return instance;
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id){
        for (Crime crime : mCrimes){
            if (crime.getId().equals(id))
                return crime;
        }
        return null;
    }

    public int item (UUID id){

        List<Crime> crimes = getCrimes();
        for (int i = 0 ; i < crimes.size() ; i++) {

            if (crimes.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public void addCrime(Crime crime){
        mCrimes.add(crime);
    }
}
