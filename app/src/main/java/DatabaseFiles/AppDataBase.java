package DatabaseFiles;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {NotesEntities.class},version = 1)
public abstract class AppDataBase extends RoomDatabase {


    public static AppDataBase appDataBase;
    public static String AppDatabase_Name = "NotesDB";

    public abstract DAO NotesDao();

    public synchronized static AppDataBase getInstance(Context context){
        if(appDataBase == null){
            appDataBase = Room.databaseBuilder(context.getApplicationContext(),AppDataBase.class,AppDatabase_Name)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return appDataBase;
    }
}
