package com.felbsn.a16011075proj;


import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Intent;
import android.content.Context;


import java.util.ArrayList;


public class NoteItemAdaptor extends RecyclerView.Adapter<NoteItemAdaptor.NoteViewHolder> {
    private ArrayList<NoteItem> NoteItemList;
    private Context mCtx;

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewLine1;
        public TextView mTextViewLine2;
        public CardView mBaseCard;


        public NoteViewHolder(View itemView) {
            super(itemView);
            mTextViewLine1 = itemView.findViewById(R.id.textview_line1);
            mTextViewLine2 = itemView.findViewById(R.id.textview_line_2);
            mBaseCard =  itemView.findViewById(R.id.cardBase);

        }
    }

    public NoteItemAdaptor(ArrayList<NoteItem> notesList , Context ctx) {
        NoteItemList = notesList;
        mCtx = ctx;

    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        NoteViewHolder evh = new NoteViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(final NoteViewHolder holder, int position) {
        final NoteItem currentItem = NoteItemList.get(position);

        holder.mTextViewLine1.setText(currentItem.getLine1());


        holder.mTextViewLine2.setText(currentItem.getLine2());

       if(currentItem.colorText != null)
        {
           int col = Color.parseColor(currentItem.colorText);
           holder.mBaseCard.setCardBackgroundColor(col);

        }


        holder.mBaseCard.setOnClickListener(new View.OnClickListener() {




            @Override
            public void onClick(View v) {
                Intent intent = new Intent( mCtx, NoteView.class);
                intent.putExtra("name" , holder.mTextViewLine1.getText() );
                intent.putExtra("desc" , currentItem.colorText);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra("targetNoteID"  ,currentItem.id );

                mCtx.startActivity(intent);
            }
        });

    }

    public  void delete( NoteItem item)
    {
        NoteItemList.remove(item);
        notifyDataSetChanged();
    }

    public void reflesh()
    {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return NoteItemList.size();
    }
}