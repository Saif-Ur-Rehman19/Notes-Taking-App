package com.saif.notes.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saif.notes.R;
import com.saif.notes.models.Note;
import com.saif.notes.util.Utility;

import java.util.ArrayList;

//this class is responsible for adapting our note object to our list of note
public class NotesRecycleAdapter extends RecyclerView.Adapter<NotesRecycleAdapter.ViewHolder> {

    private static final String TAG = "Cannot invoke method length() on null object";
    ArrayList<Note> myNotes = new ArrayList<>();
    private OnNoteListener mOnNoteListener;

    public NotesRecycleAdapter(ArrayList<Note> myNotes, OnNoteListener onNoteListener) {
        this.myNotes = myNotes;
        this.mOnNoteListener = onNoteListener;
    }

    //onCreateViewHolder method is responsible for creating or instantiating the viewHolder object
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_note_list_item,
                parent,false);
        return new ViewHolder(view,mOnNoteListener);
    }

    // this method is called for every single entry in the list
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            String month = myNotes.get(position).getTimestamp().substring(0,2);
            month = Utility.getMonthFromNumber(month);
            String year = myNotes.get(position).getTimestamp().substring(3);
            String timestamp = month + " " + year;
            holder.timeStamp.setText(timestamp);
            holder.title.setText(myNotes.get(position).getTitle());
        }catch (NullPointerException e){
            Log.d(TAG, "onBindViewHolder: " + e.getMessage());
        }


    }

    @Override
    public int getItemCount() {
        return myNotes.size();
    }

    //the class below holds all the views in the recyclerview;
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, timeStamp;
        OnNoteListener onNoteListener;

        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            title = itemView.findViewById(R.id.note_title);//connect the layout with the class
            timeStamp = itemView.findViewById(R.id.note_timestamp);
            itemView.setOnClickListener(this);//attach onClickListener to the entire viewHolder
            //this refers to the implemented interface above
            this.onNoteListener = onNoteListener;
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
