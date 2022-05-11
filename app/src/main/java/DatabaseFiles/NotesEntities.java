package DatabaseFiles;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "iNotes_table")
public class NotesEntities implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int ID;

    @ColumnInfo(name = "Notes_Title")
    public String Notes_Title = "";

    @ColumnInfo(name = "Notes_SubTitle")
    public String Notes_SubTitle = "";

    @ColumnInfo(name = "Description")
    public String Description = "";

    @ColumnInfo(name = "Date")
    public String Notes_Date = "";


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNotes_Title() {
        return Notes_Title;
    }

    public void setNotes_Title(String notes_Title) {
        Notes_Title = notes_Title;
    }

    public String getNotes_SubTitle() {
        return Notes_SubTitle;
    }

    public void setNotes_SubTitle(String notes_SubTitle) {
        Notes_SubTitle = notes_SubTitle;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String notes) {
        Description = notes;
    }

    public String getNotes_Date() {
        return Notes_Date;
    }

    public void setNotes_Date(String notes_Date) {
        Notes_Date = notes_Date;
    }
}
