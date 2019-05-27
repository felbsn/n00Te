package com.felbsn.a16011075proj;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.ContactsContract;

import com.felbsn.a16011075proj.MainActivity;
import com.felbsn.a16011075proj.NoteItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class NoteHandler {




    public  static NoteHandler getInstance(Context ctx)
    {
        if(instance == null)
        {
            instance = new NoteHandler(ctx);
        }
        return instance;
    }

    public ArrayList<NoteItem> getNotes()
    {
        return mNotes;
    }

    public NoteItem getNoteByID(int id)
    {

        for (int i = 0 ; i < mNotes.size() ; i++)
        {
            if(mNotes.get(i).id == id)
            {
                return mNotes.get(i);
            }
        }

        return null;
    }

    public void RemoveExpiredTimers(int noteID)
    {
        Date current = new Date();

        NoteItem item = getNoteByID(noteID);

        for (NoteItem.Reminder reminder :item.reminderDates) {
            if(reminder.date.before(current))
            {
                item.reminderDates.remove(reminder);
            }
        }
    }

    private NoteHandler(Context ctx)
    {
        appContext = ctx;
        mNotes = new ArrayList<>();
        loadNotes();
    }

    public void loadNotes( )
    {
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<NoteItem>>() {}.getType();
        mNotes = gson.fromJson(json, type);

        if (mNotes == null) {
            mNotes = new ArrayList<>();
        }
    }

    public void saveNotes()
    {
        SharedPreferences sharedPreferences =appContext.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mNotes);
        editor.putString("task list", json);
        editor.apply();
    }



    public int insertNote()
    {
        NoteItem item = new NoteItem();
        item.id = getNextNodeID();
        mNotes.add(item);
        return item.id;
    }
    public int insertReminder(NoteItem item , Date  alarmDate)
    {
        NoteItem.Reminder reminder ;

        reminder = new NoteItem().new Reminder();


        reminder.id =  getNextReminderID();

        reminder.alarmID =  reminder.id  + item.id;

        reminder.date = alarmDate;
        reminder.reqCode = item.id;

        item.reminderDates.add(reminder);

        NotificationEventRecv.setupAlarm(appContext,reminder.alarmID  ,reminder.date);
        return reminder.id;
    }


    public int getNextNodeID()
    {
            SharedPreferences sharedPreferences = appContext.getSharedPreferences("shared preferences", 0);
            int id = sharedPreferences.getInt("NoteID" , 1);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            int lastID = (id +1) % (1 << 16) ;
            editor.putInt("NoteID" ,lastID);
            editor.apply();
            return id;

    }

    public  int getNextReminderID()
    {
            SharedPreferences sharedPreferences = appContext.getSharedPreferences("shared preferences", 0);
            int id = sharedPreferences.getInt("ReminderID" , 1);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            int lastID = (id +1) % (1 << 16) ;
            editor.putInt("ReminderID" ,lastID);
            editor.apply();
            return id << 16;
    }

    private static NoteHandler instance = null;
    private  ArrayList<NoteItem> mNotes;
    private  Context appContext = null;


}
