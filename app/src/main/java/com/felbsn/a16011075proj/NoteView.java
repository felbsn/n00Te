package com.felbsn.a16011075proj;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
;

import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class NoteView extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    public  NoteItem selectedNote = null;


    private static final int READ_EXTERNAL_STORAGE_CODE= 77;
    private static final int PICKFILE_RESULT_CODE = 17;






    static NoteView instance;



    public static View textRootView = null;
    public static EditText mainTex = null;
    public static FloatingActionButton flButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_view);

        instance = this;

        int target =  getIntent().getIntExtra("targetNoteID" ,  -1);
        if(target != -1)
        {
            NoteHandler hndle = NoteHandler.getInstance(this);

            NoteItem targetNote = hndle.getNoteByID(target);
            if(targetNote != null)
            {
                this.selectedNote = targetNote;
                hndle.RemoveExpiredTimers(this.selectedNote.id);
            }
        }else
            finish();


        checkPerm();

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout =  findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        flButton =  findViewById(R.id.fab);



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                mViewPager.setCurrentItem(pos);

                if(pos == 0)
                {
                    flButton.setImageResource(R.drawable.savew);
                }else
                {
                    // close dat keyboard
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        flButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int st = mViewPager.getCurrentItem();

                if(0 == st)
                {
                    Snackbar.make(view, "Not Kaydedildi", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    NoteHandler.getInstance(getApplicationContext()).saveNotes();
                }else
                if(1 == st)
                {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("file/*");
                    startActivityForResult(intent,PICKFILE_RESULT_CODE);

                }else
                if(2 == st)
                {
                    Snackbar.make(view, "Hatırlatıcı...", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    datePicker();
                }

            }
        });
    }

    public static RecyclerView.LayoutManager mReminderLayout;
    public static ReminderAdaptor mReminderAdaptor;
    public static RecyclerView mReminderRecycler;

    public static void initAct3(View parent)
    {
        if(instance != null && instance.selectedNote != null)
        {

            mReminderRecycler =  parent.findViewById(R.id.recyclerview3);
            mReminderRecycler.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(instance);
            mReminderAdaptor = new ReminderAdaptor(   instance.selectedNote.reminderDates ,  instance.getApplicationContext() );
            mReminderRecycler.setLayoutManager(mLayoutManager);
            mReminderRecycler.setAdapter(instance.mReminderAdaptor );
        }




    }








    Calendar alarmCalender = Calendar.getInstance();

    String date_time = "";
    int mYear;
    int mMonth;
    int mDay ;


    int mHour;
    int mMinute;

    private void datePicker(){

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        //*************Call Time Picker Here ********************

                        setYearMonthDay(year , monthOfYear , dayOfMonth);

                        timePicker();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void setYearMonthDay(int y , int m , int d)
    {
        mYear = y;
        mMonth = m;
        mDay = d;
    }

    private void timePicker(){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;

                        String str = date_time+" "+hourOfDay + ":" + minute;



                        Log.i(getClass().getSimpleName(), "message set "  + str );

                        createAlarm();

                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }


    public void createAlarm()
    {
        Log.i(getClass().getSimpleName(), "Creating alarm but.... ");

        alarmCalender.set(Calendar.YEAR ,mYear);
        alarmCalender.set(Calendar.MONTH ,mMonth);
        alarmCalender.set(Calendar.DAY_OF_MONTH ,mDay);
        alarmCalender.set(Calendar.HOUR_OF_DAY ,mHour);
        alarmCalender.set(Calendar.MINUTE ,mMinute);
        alarmCalender.set(Calendar.SECOND ,0);


        Date nowDate =  new Date();

        SimpleDateFormat df = new SimpleDateFormat("HH:mm dd-MMM-yyyy");
        Toast.makeText( instance.getApplicationContext(), "Cur dat"+  df.format(nowDate) + " cal:" + df.format(alarmCalender.getTime()), Toast.LENGTH_LONG );

        Log.i(getClass().getSimpleName(), "Cur dat"+  df.format(nowDate) + " cal:" + df.format(alarmCalender.getTime()));

        Date alarmdate = alarmCalender.getTime();



        if(alarmdate.after( nowDate))
        {
            NoteHandler.getInstance(this).insertReminder(this.selectedNote  , alarmdate);
            mReminderAdaptor.reflesh();


            Log.i(getClass().getSimpleName(), "okey ");

        }else
        {
            Log.i(getClass().getSimpleName(), "exceed ");
            Toast.makeText(getApplicationContext() , "Şu andan önceye alarm kurulamıyor", Toast.LENGTH_LONG );
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub


        switch(requestCode){
            case PICKFILE_RESULT_CODE:
                if(resultCode==RESULT_OK){
                    String FilePath = data.getData().getPath();

                    Snackbar.make( getWindow().getDecorView().getRootView() ,  FilePath, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    selectedNote.extrasLocations.add(FilePath);
                    mExtrasAdaptor.reflesh();
                }
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        String colorStr = "#e0e0e0";

        if (id == R.id.cAltin) colorStr ="#FFD700";

        if (id == R.id.cBeyaz) colorStr= "#e0e0e0";

        if (id == R.id.cMavi)  colorStr = "#8eafe2";

        if (id == R.id.cPembe) colorStr = "#FFC0CB";

        if(this.selectedNote != null)
        {

            selectedNote.colorText = colorStr;
            if(textRootView != null)   textRootView.setBackgroundColor( Color.parseColor(colorStr) );
        }

        if (id == R.id.action_delete) {

            if(selectedNote != null)
            {

                NoteHandler handler =  NoteHandler.getInstance(this);
                handler.deleteNote(selectedNote);
                handler.saveNotes();

                finish();
            }

        }

        return super.onOptionsItemSelected(item);
    }


    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            if(getArguments().getInt(ARG_SECTION_NUMBER) == 1)
            {
                textRootView = inflater.inflate(R.layout.fragment_note_view1, container, false);

                final EditText ed =  textRootView.findViewById(R.id.t1_mainEdit);

                mainTex = ed;
                ed.setOnKeyListener(new View.OnKeyListener(){
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        instance.selectedNote.mNoteText = ed.getText().toString();


                        if (keyCode == KeyEvent.KEYCODE_ENTER) return true;
                        return false;
                    }
                });

                ed.setText( instance.selectedNote.mNoteText);

                if(instance.selectedNote.colorText != null)
                textRootView.setBackgroundColor( Color.parseColor(instance.selectedNote.colorText) );


                return textRootView;
            }
            if(getArguments().getInt(ARG_SECTION_NUMBER) == 2)
            {
                View rootView = inflater.inflate(R.layout.fragment_note_view2, container, false);

                initAct2(rootView);

                return rootView;
            }

            if(getArguments().getInt(ARG_SECTION_NUMBER) == 3)
            {

                View rootView = inflater.inflate(R.layout.fragment_note_view3, container, false);

                initAct3(rootView);

                return rootView;
            }



            //buraya girmemesi lazım ...
            return null;

        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {



            return PlaceholderFragment.newInstance(position + 1);
        }
        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }



    public static RecyclerView.LayoutManager mLayoutManager;
    public static ExtrasItemAdaptor mExtrasAdaptor;
    public static RecyclerView mExtrasRecycler;


    public static void initAct2(View parent)
    {
        if(instance != null && instance.selectedNote != null)
        {

            mExtrasRecycler =  parent.findViewById(R.id.recyclerview2);
            mExtrasRecycler.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(instance);
            mExtrasAdaptor = new ExtrasItemAdaptor(   instance.selectedNote ,  instance.getApplicationContext() );
            mExtrasRecycler.setLayoutManager(mLayoutManager);
            mExtrasRecycler.setAdapter(instance.mExtrasAdaptor );
        }
    }

    public static void refleshReminders()
    {
        if(mReminderAdaptor!= null)
         mReminderAdaptor.reflesh();
    }


    public static void AddNewExtras( String path)
    {
         instance.selectedNote.extrasLocations.add(path);
         instance.mExtrasAdaptor.reflesh();
    }



    public void checkPerm()
    {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_EXTERNAL_STORAGE_CODE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        NoteHandler handler = NoteHandler.getInstance(this);

        handler.saveNotes();

        if(MainActivity.mAdapter != null) MainActivity.mAdapter.reflesh();

    }
}
