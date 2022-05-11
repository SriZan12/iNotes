package Adapter;

import DatabaseFiles.NotesEntities;

public interface ClickListener {
    void click(NotesEntities notesEntities);
    void OnLongClick(NotesEntities notes);

}