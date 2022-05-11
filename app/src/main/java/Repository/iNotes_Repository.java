package Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import DatabaseFiles.AppDataBase;
import DatabaseFiles.DAO;
import DatabaseFiles.NotesEntities;

public class iNotes_Repository {
    public DAO NotesDao;
    public LiveData<List<NotesEntities>> getAllNotes;

    public iNotes_Repository(Application application){
        AppDataBase appDataBase = AppDataBase.getInstance(application);
        NotesDao = appDataBase.NotesDao();
        getAllNotes = NotesDao.getAllNotes();
    }

    public void NotesInsert(NotesEntities notes){
        NotesDao.InsertNotes(notes);
    }
    public void NotesDelete(NotesEntities notes){
        NotesDao.DeleteNotes(notes);
    }

    public void NotesUpdate(NotesEntities notes){
        NotesDao.update(notes);
    }
}
