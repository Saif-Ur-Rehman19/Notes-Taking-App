package com.saif.notes.persistence;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.saif.notes.async.DeleteAsyncTask;
import com.saif.notes.async.InsertAsyncTask;
import com.saif.notes.async.UpdateAsyncTask;
import com.saif.notes.models.Note;

import java.util.List;

public class NoteRepository {

    private NoteDatabase mNoteDatabase;

    public NoteRepository(Context context) {
        mNoteDatabase = NoteDatabase.getInstance(context);
    }

    public void insertNoteTask(Note note){

        new InsertAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }

    public void updateNote(Note note){
        new UpdateAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }

    public LiveData<List<Note>> retrieveNotesTask(){
        return mNoteDatabase.getNoteDao().getNotes();
    }

    public void deleteNote(Note note){

        new DeleteAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }
}
