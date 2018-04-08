package com.example.android.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.pets.data.PetContract.PetEntry;
import com.example.android.pets.data.PetDbHelper;

public class CatalogActivity extends AppCompatActivity {

    PetDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        displayDatabaseInfo();
    }

    private void insertPet(){

        mDbHelper = new PetDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PetEntry.COLUMN_PET_NAME,"Addy");
        values.put(PetEntry.COLUMN_PET_BREED,"Karachi");
        values.put(PetEntry.COLUMN_PET_GENDER,PetEntry.GENDER_MALE);
        values.put(PetEntry.COLUMN_PET_WEIGHT,85);

        long newRow = db.insert(PetEntry.TABLE_NAME,null,values);
    }

    private void displayDatabaseInfo() {

        PetDbHelper mDbHelper = new PetDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT
        };

        Cursor cursor = db.query(
                PetEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        try {
            TextView displayView = (TextView) findViewById(R.id.text_view_pet);
            displayView.setText("Total Pets are " + cursor.getCount());

            int id = cursor.getColumnIndex(PetEntry._ID);
            int name = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
            int breed = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);
            int gender = cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER);
            int weight = cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT);

            while (cursor.moveToNext()){

                int currentId = cursor.getInt(id);
                String currentName = cursor.getString(name);
                String currentBreed = cursor.getString(breed);
                int currentGender = cursor.getInt(gender);
                int currentWeight = cursor.getInt(weight);

                displayView.append("\n" +
                        currentId + " | " +
                        currentName + " | " +
                        currentBreed + " | " +
                        currentGender + " | " +
                        currentWeight + " | "
                );

            }
        }

        finally {
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    protected void onStart(){
        super.onStart();
        displayDatabaseInfo();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                insertPet();
                displayDatabaseInfo();
                return true;
            case R.id.action_delete_all_entries:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}