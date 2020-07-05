package com.example.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.database.CrimeBaseHelper;
import com.example.database.CrimeCursorWrapper;
import com.example.database.CrimeDbSchema;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab instance ;
    private SQLiteDatabase mDatabase;
    private Context mContext;

    private CrimeLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
    }

    public static CrimeLab getInstance(Context context){
        if (instance == null)
            instance = new CrimeLab(context);

        return instance;
    }

    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper crimeCursorWrapper = queryCrime(null, null);
        try {
            if (crimeCursorWrapper.getCount() == 0)
                return crimes;

            crimeCursorWrapper.moveToFirst();
            while (!crimeCursorWrapper.isAfterLast()){
                crimes.add(crimeCursorWrapper.getCrime());
                crimeCursorWrapper.moveToNext();
            }
        }
        finally {
            crimeCursorWrapper.close();
        }
        return crimes;
    }

    public Crime getCrime(UUID id){
        String whereClaus = CrimeDbSchema.CrimeTable.Cols.UUID + " = ?";
        String [] whereArgs = new String[]{id.toString()};
        CrimeCursorWrapper crimeCursorWrapper = queryCrime(whereClaus, whereArgs);
        try {
            if (crimeCursorWrapper.getCount() == 0)
                return null;
            crimeCursorWrapper.moveToFirst();
            return crimeCursorWrapper.getCrime();
        }
        finally {
            crimeCursorWrapper.close();
        }
    }

       private CrimeCursorWrapper queryCrime(String whereClaus, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CrimeDbSchema.CrimeTable.NAME,
                null,
                whereClaus,
                whereArgs,
                null,
                null,
                null);
        return new CrimeCursorWrapper(cursor);
    }

    public void addCrime(Crime crime){
        ContentValues values = getContentValues(crime);
        mDatabase.insert(CrimeDbSchema.CrimeTable.NAME,null,values);
    }

    public void deleteCrime(Crime crime){
        String whereClause = CrimeDbSchema.CrimeTable.Cols.UUID + " = ? ";
        String [] whereArgs = new String[]{crime.getId().toString()};
        mDatabase.delete(CrimeDbSchema.CrimeTable.NAME,whereClause,whereArgs);
    }

    public void update(Crime crime){
        ContentValues values = getContentValues(crime);
        String whereClause = CrimeDbSchema.CrimeTable.Cols.UUID + " = ? ";
        mDatabase.update(CrimeDbSchema.CrimeTable.NAME,values,
                whereClause,new String[]{crime.getId().toString()});
    }

    public ContentValues getContentValues(Crime crime){
        ContentValues values = new ContentValues();
        values.put(CrimeDbSchema.CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeDbSchema.CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeDbSchema.CrimeTable.Cols.DATE, crime.getDate().toString());
        values.put(CrimeDbSchema.CrimeTable.Cols.SOLVED, crime.isSolved());
        return values;
    }
}
