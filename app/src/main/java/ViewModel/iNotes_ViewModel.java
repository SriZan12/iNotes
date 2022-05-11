package ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import DatabaseFiles.NotesEntities;
import Repository.iNotes_Repository;

public class iNotes_ViewModel extends AndroidViewModel {

    iNotes_Repository repository;
    public LiveData<List<NotesEntities>> getAllNotes;

    public iNotes_ViewModel(@NonNull Application application) {
        super(application);

        repository = new iNotes_Repository(application);
        getAllNotes = repository.getAllNotes;
    }


    public void InsertNotes(NotesEntities notes){
        repository.NotesInsert(notes);
    }

    public void DeleteNotes(NotesEntities notes){
        repository.NotesDelete(notes);
    }

    public void UpdateNotes(NotesEntities notes){
        repository.NotesUpdate(notes);
    }
}
