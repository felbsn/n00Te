package com.felbsn.a16011075proj;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;


import java.util.ArrayList;


public class ReminderAdaptor extends RecyclerView.Adapter<ReminderAdaptor.ReminderViewHolder> {
    private ArrayList<NoteItem.Reminder> mReminderList;
    private Context mCtx;

    public static class ReminderViewHolder extends RecyclerView.ViewHolder {
        public Button mTextViewLine1;
        public Button button;
        public CardView mBaseCard;


        public ReminderViewHolder(View itemView) {
            super(itemView);
            mTextViewLine1 = itemView.findViewById(R.id.dateText);
            button = itemView.findViewById(R.id.t3cancelBtn);


        }
    }

    public ReminderAdaptor(ArrayList<NoteItem.Reminder> notesList , Context ctx) {
        mReminderList = notesList;
        mCtx = ctx;

    }

    @Override
    public ReminderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_item, parent, false);
        ReminderViewHolder evh = new ReminderViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(final ReminderViewHolder holder, int position) {
        final NoteItem.Reminder currentItem = mReminderList.get(position);

        //holder.mTextViewLine1.setText(currentItem.getLine1());



        /*if(currentItem.colorText != null)
        {
            int col = Color.parseColor(currentItem.colorText);
            holder.mBaseCard.setCardBackgroundColor(col);

        }*/

        holder.mTextViewLine1.setText(currentItem.getDateText() );


        holder.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                NotificationEventRecv.cancelAlarm(mCtx , currentItem.alarmID );
                NoteView.mReminderAdaptor.delete(currentItem);


               // Intent intent = new Intent( mCtx, NoteView.class);
               // intent.putExtra("name" , holder.mTextViewLine1.getText() );
              //  intent.putExtra("desc" , currentItem.colorText);
             //   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

              //  MainActivity.selectedNote = currentItem;

              //  mCtx.startActivity(intent);
            }
        });

    }

    public  void delete( NoteItem.Reminder item)
    {
        mReminderList.remove(item);
        notifyDataSetChanged();
    }

    public void reflesh()
    {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mReminderList.size();
    }


    public void addNewReminder(NoteItem.Reminder reminder)
    {
        mReminderList.add(reminder);
        reflesh();
    }

}