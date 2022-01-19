package com.example.readsupport;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    private Context mContext;
    private List<setget> Yourtext;
    private OnItemClickListener mListener;


    public RecyclerAdapter(Context context, List<setget> uploads) {
        mContext = context;
        Yourtext = uploads;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(mContext).inflate(R.layout.activity_rowmodel,parent,false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {

        setget currentText=Yourtext.get(position);
        holder.nameTextView.setText(currentText.getName());

        holder.dateTextView.setText(getDateToday());
        Picasso.with(mContext)
                .load(currentText.getImageUrl())
                .into(holder.ImageView);

    }

    @Override
    public int getItemCount() {
        return Yourtext.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener  {

        public TextView nameTextView,descriptionTextView,dateTextView;
        public ImageView ImageView;


        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView          = itemView.findViewById(R.id.nameTextView);
            descriptionTextView   = itemView.findViewById(R.id.dateTextView);
            dateTextView          = itemView.findViewById(R.id.dateTextView);
            ImageView          = itemView.findViewById(R.id.ImageView);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);


        }

        @Override
        public void onClick(View v) {
            if(mListener !=null){

                int position =getAbsoluteAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }



        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle("Select Action");
            MenuItem showItem =menu.add(Menu.NONE,1,1,"show");
            MenuItem deleteItem =menu.add(Menu.NONE,2,2,"Delete");

            showItem.setOnMenuItemClickListener(this);
            deleteItem.setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()) {
                        case 1:
                            mListener.onShowItemClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteItemClick(position);
                            return true;
                    }
                }
            }
            return false;

        }


    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onShowItemClick(int position);
        void onDeleteItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener =  listener;
    }
    private String getDateToday(){
        DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
        Date date=new Date();
        String today= dateFormat.format(date);
        return today;
    }
}
