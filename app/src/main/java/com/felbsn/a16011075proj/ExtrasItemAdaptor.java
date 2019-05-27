package com.felbsn.a16011075proj;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.File;
import java.util.ArrayList;


    public class ExtrasItemAdaptor extends RecyclerView.Adapter<ExtrasItemAdaptor.ExtrasViewHolder> {
        private ArrayList<String> extrasPaths;
        private Context mCtx;

        public static class ExtrasViewHolder extends RecyclerView.ViewHolder {
            public TextView pathText;
            public ImageView imView;
            public Button delButton;


            public ExtrasViewHolder(View itemView) {
                super(itemView);
                pathText = itemView.findViewById(R.id.pathText);
                imView = itemView.findViewById(R.id.imgViewExtras);
                delButton =  itemView.findViewById(R.id.delExtrasButton);
            }
        }

        public ExtrasItemAdaptor(NoteItem item , Context ctx) {
            extrasPaths = item.extrasLocations;
            mCtx = ctx;

        }

        @Override
        public ExtrasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.extras_item, parent, false);
            ExtrasViewHolder evh = new ExtrasViewHolder(v);
            return evh;
        }

        @Override
        public void onBindViewHolder(final ExtrasViewHolder holder, int position) {
            final String currentItem = extrasPaths.get(position);

          //  holder.mTextViewLine1.setText(currentItem.getLine1());


          //  holder.mTextViewLine2.setText(currentItem.getLine2());

            String fname=currentItem.substring(currentItem.lastIndexOf("/")+1);

            holder.pathText.setText(fname);


            String ext=currentItem.substring(currentItem.lastIndexOf(".")+1);
            if(     ext.compareToIgnoreCase("png") == 0  ||
                    ext.compareToIgnoreCase("jpg") == 0  ||
                    ext.compareToIgnoreCase("jpeg") == 0 ||
                    ext.compareToIgnoreCase("gif") == 0
            )
            {


                Bitmap bitmap = BitmapFactory.decodeFile(currentItem);
                holder.imView.setImageBitmap(bitmap);
            }


            holder.imView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String path= currentItem;

                    Intent intent = new Intent();
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    File file = new File(path);

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                    MimeTypeMap mime = MimeTypeMap.getSingleton();
                    String ext=file.getName().substring(file.getName().lastIndexOf(".")+1);




                    String type = mime.getMimeTypeFromExtension(ext);

                    intent.setDataAndType(Uri.fromFile(file),type);

                    try {
                        mCtx.startActivity(intent);

                    }catch (Exception e)
                    {
                        Snackbar.make( v,  "Hata! " + e.getMessage()  , Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                   // Toast.makeText(mCtx,"Hello " + ext,  Toast.LENGTH_SHORT).show();

                }
            });

            holder.delButton.setOnClickListener(new View.OnClickListener() {




                @Override
                public void onClick(View v) {

                    delete(currentItem);

                }
            });

        }

        public  void delete( String item)
        {
            extrasPaths.remove(item);
            notifyDataSetChanged();
        }

        public void reflesh()
        {
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return extrasPaths.size();
        }
    }