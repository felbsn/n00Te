package com.felbsn.a16011075proj;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static RecyclerView mRecyclerView;
    public static NoteItemAdaptor mAdapter;
    public static RecyclerView.LayoutManager mLayoutManager;


    public static MainActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();
        buildRecyclerView();
        setInsertButton();

        MainActivity.instance = this;
    }

    public void saveData() {

        NoteHandler instance =  NoteHandler.getInstance(this);
        instance.saveNotes();
        mAdapter.reflesh();

    }

    private void loadData() {
       NoteHandler instance =  NoteHandler.getInstance(this);
       instance.loadNotes();
    }



    private void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new NoteItemAdaptor(NoteHandler.getInstance(this).getNotes(), getApplicationContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setInsertButton() {
        FloatingActionButton buttonInsert = findViewById(R.id.floatingInsertButton);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertItem();
            }
        });
    }

    private void insertItem() {


        NoteHandler.getInstance(this).insertNote();

        mAdapter.notifyItemInserted(NoteHandler.getInstance(this).getNotes().size() );
    }


}