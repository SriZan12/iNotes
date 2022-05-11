package DatabaseFiles;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DAO {

    @Query("SELECT * FROM iNotes_table ORDER BY ID DESC")
    LiveData<List<NotesEntities>> getAllNotes();

    @Insert
    void InsertNotes (NotesEntities notesEntities);

    @Delete
    void DeleteNotes(NotesEntities notes);

    @Update
    void update(NotesEntities notes);
}
