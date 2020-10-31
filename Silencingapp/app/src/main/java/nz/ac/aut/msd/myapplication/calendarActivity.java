package nz.ac.aut.msd.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class calendarActivity extends AppCompatActivity {

    public static final int ADD_NOTE_REQUEST = 1 ;
    public static final int EDIT_NOTE_REQUEST = 2 ;

    public static final String CALENDAR_TITLE = "CALENDAR_TITLE";
    public static final String CALENDAR_LOCATION = "CALENDAR_LOCATION";
    public static final String CALENDAR_TIME = "CALENDAR_TIME";

    private NoteViewModel noteViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //add Note button, open AddActivity
        FloatingActionButton buttonAddNote = findViewById(R.id.bt_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(calendarActivity.this, AddActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.setNotes(notes);
            }
        });

        //swipe to delete the Note
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(calendarActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        //click to edit the Note, open AddActivity
        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(calendarActivity.this,AddActivity.class);
                intent.putExtra(AddActivity.EXTRA_ID, note.getId());
                intent.putExtra(AddActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(AddActivity.EXTRA_LOCATION, note.getLocation());
                String[] part = note.getTime().split(" ");
                String part1 = part[0];
                String part2 = part[1];
                intent.putExtra(AddActivity.EXTRA_TIME, part1);
                intent.putExtra(AddActivity.EXTRA_DATE, part2);
                intent.putExtra(AddActivity.EXTRA_PRIORITY, note.getPriority());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });
    }

    //receive Note detail from AddActivity, ADD and EDIT
    //and update to database
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK){
            String title = data.getStringExtra(AddActivity.EXTRA_TITLE);
            String location = data.getStringExtra(AddActivity.EXTRA_LOCATION);
            String time = data.getStringExtra(AddActivity.EXTRA_TIME);
            String date = data.getStringExtra(AddActivity.EXTRA_DATE);
            int priority = data.getIntExtra(AddActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title, location,time+" "+date,priority);
            noteViewModel.insert(note);

            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        }else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK){
            int id = data.getIntExtra(AddActivity.EXTRA_ID, -1);

            if (id == -1){
                Toast.makeText(this, "Note cant be update", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(AddActivity.EXTRA_TITLE);
            String location = data.getStringExtra(AddActivity.EXTRA_LOCATION);
            String time = data.getStringExtra(AddActivity.EXTRA_TIME);
            String date = data.getStringExtra(AddActivity.EXTRA_DATE);
            int priority = data.getIntExtra(AddActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title, location,time+" "+date,priority);
            note.setId(id);
            noteViewModel.update(note);

            Toast.makeText(this, "Note update",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "!!! Note not saved !!!", Toast.LENGTH_SHORT).show();
        }

        //sent highest priority node detail to main
        // @passtomain the highest Node in calendar
//        Intent intent = new Intent();
//        String title = passtomain.getTitle();
//        String location = passtomain.getLocation();
//        String time = passtomain.getTime();
//        intent.putExtra(CALENDAR_TITLE, title);
//        intent.putExtra(CALENDAR_LOCATION, location);
//        intent.putExtra(CALENDAR_TIME, time);
//        setResult(RESULT_OK, intent);
//        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.calendar_menu,menu);
        return true;
    }

    //feature improve other menu option
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all:
                noteViewModel.deleteAll();
                Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
