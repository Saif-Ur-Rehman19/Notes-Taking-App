package com.saif.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.saif.notes.adapters.NotesRecycleAdapter;
import com.saif.notes.models.Note;
import com.saif.notes.persistence.NoteRepository;
import com.saif.notes.util.VerticalSpacingItemDecorator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NotesRecycleAdapter.OnNoteListener,
        View.OnClickListener {

    //Ui components
    private RecyclerView recyclerView;
    //vars
    private ArrayList<Note> mNotes = new ArrayList<>();
    private NotesRecycleAdapter notesRecycleAdapter;
    private NoteRepository mNoteRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        findViewById(R.id.fab).setOnClickListener(this);
        mNoteRepository = new NoteRepository(this);
        initRecyclerView();
        retrieveNotes();
        //insertFakeNote();
        Toolbar toolbar = findViewById(R.id.notes_toolbar);
        setSupportActionBar((Toolbar) findViewById(R.id.notes_toolbar));
        setTitle("Notes");
    }

    private void retrieveNotes(){
        mNoteRepository.retrieveNotesTask().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                if (mNotes.size() > 0){
                    mNotes.clear();
                }
                if (mNotes != null){
                    mNotes.addAll(notes);
                }
                notesRecycleAdapter.notifyDataSetChanged();
            }
        });
    }

    private void insertFakeNote(){
        for (int i = 0; i < 100; i++){
            mNotes.add(new Note("some title " + i,"some content " + i,"Dec 2018 "));
        }
        notesRecycleAdapter.notifyDataSetChanged();//notify that the data in the list are changed
    }
    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        recyclerView.addItemDecoration(itemDecorator);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        notesRecycleAdapter = new NotesRecycleAdapter(mNotes, this);
        recyclerView.setAdapter(notesRecycleAdapter);
    }

    @Override
    public void onNoteClick(int position) {
        Log.d("TAG", "onNoteClick: " + position);
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra("selected_note", mNotes.get(position));
        startActivity(intent);
        // Extras are data structure that you can attach object to.bundle can be attached to an intent
        // parcelable
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, NoteActivity.class);
        startActivity(intent);
    }

    private void deleteNote(Note note){
        mNotes.remove(note);
        notesRecycleAdapter.notifyDataSetChanged();
        mNoteRepository.deleteNote(note);
    }

    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            deleteNote(mNotes.get(viewHolder.getAdapterPosition()));

        }
    };

}