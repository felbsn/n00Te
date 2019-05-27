package com.felbsn.a16011075proj;


import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;




public class NoteItem {
    private String mLine1;
    private String mLine2;

    public String mNoteText = "Yeni Not";
    public String colorText = "#e0e0e0";

    public String DateCreated;

    public ArrayList<String> extrasLocations = new ArrayList<String>();

    public ArrayList<Reminder> reminderDates = new ArrayList<Reminder>();

    public int id;


    public NoteItem()  {


        id = 0;

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("HH:mm dd-MMM-yyyy");
        DateCreated= df.format(c);

    }

    public String getLine1() {

        String head = mNoteText.split("\n" ,30)[0];

        return head;
    }

    public String getLine2() {
        return DateCreated + " " + id;
    }



    public class Reminder
    {
        public Reminder()
        {
            date = new Date();
            reqCode = 0;

        }

        public Date date;
        public int reqCode;

        public int id;
        public int alarmID;


        public String getDateText()
        {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm dd-MMM-yyyy");
            return  df.format(date);
        }
    }



}
