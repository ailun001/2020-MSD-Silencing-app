package nz.ac.aut.msd.myapplication;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private final String title;
    private final String location;
    private final String time;
    private final int priority;

    public Note(String title, String location, String time, int priority) {
        this.title = title;
        this.location = location;
        this.time = time;
        this.priority = priority;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }

    public int getPriority() {
        return priority;
    }
}
