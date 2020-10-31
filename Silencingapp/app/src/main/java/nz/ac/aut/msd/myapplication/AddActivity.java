package nz.ac.aut.msd.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;


public class AddActivity extends AppCompatActivity {
    public static final String EXTRA_ID =
            "nz.ac.aut.msd.myapplication.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "nz.ac.aut.msd.myapplication.EXTRA_TITLE";
    public static final String EXTRA_LOCATION =
            "nz.ac.aut.msd.myapplication.EXTRA_LOCATION";
    public static final String EXTRA_TIME =
            "nz.ac.aut.msd.myapplication.EXTRA_TIME";
    public static final String EXTRA_DATE =
            "nz.ac.aut.msd.myapplication.EXTRA_DATE";
    public static final String EXTRA_PRIORITY =
            "nz.ac.aut.msd.myapplication.EXTRA_PRIORITY";

    private EditText editTextTitle;
    private EditText editTextLocation;
    private EditText editTextTime;
    private EditText editTextDate;
    private NumberPicker numberPicker;
    //private PlacePicker placePicker;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        editTextTitle = findViewById(R.id.edit_add_title);
        editTextLocation = findViewById(R.id.edit_add_location);
        editTextTime = findViewById(R.id.edit_add_Time);
        editTextDate = findViewById(R.id.edit_add_Date);
        numberPicker = findViewById(R.id.number_add_priority);

        //click open MapActivity, get location back
        editTextLocation.setInputType(InputType.TYPE_NULL);
        editTextLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextLocation.setText(R.string.location_open);

                Intent intent = new Intent(AddActivity.this, MapsActivity.class);
                startActivityForResult(intent,5);

            }
        });
        //time picker, hh:mm
        editTextTime.setInputType(InputType.TYPE_NULL);
        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int min = calendar.get(Calendar.MINUTE);
                // time picker dialog
                timePickerDialog = new TimePickerDialog(AddActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        editTextTime.setText(hourOfDay + ":" + minute);
                    }
                },hour,min,true);
                timePickerDialog.show();
            }
        });
        //date picker, dd/MM/YYYY
        editTextDate.setInputType(InputType.TYPE_NULL);
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                // time picker dialog
                datePickerDialog = new DatePickerDialog(AddActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                editTextDate.setText(dayOfMonth + "/" + month + "/" + year);
                            }
                        },year,month,day);
                datePickerDialog.show();
            }
        });
        //number picker, priority 1-10
        numberPicker.setMaxValue(10);
        numberPicker.setMinValue(1);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Note");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextLocation.setText(intent.getStringExtra(EXTRA_LOCATION));
            editTextTime.setText(intent.getStringExtra(EXTRA_TIME));
            editTextDate.setText(intent.getStringExtra(EXTRA_DATE));
            numberPicker.setValue(intent.getIntExtra(EXTRA_PRIORITY,1));
        }else {
            setTitle("Add Note");
        }
    }

    //sent Note detail to calendar
    private void saveNote(){
        String title = editTextTitle.getText().toString();
        String location = editTextLocation.getText().toString();
        String time = editTextTime.getText().toString();
        String date = editTextDate.getText().toString();
        int priority = numberPicker.getValue();

        if (title.trim().isEmpty() ||
                location.trim().isEmpty() ||
                time.trim().isEmpty() ||
                date.trim().isEmpty()){
            Toast.makeText(this, "Please insert ...", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_LOCATION, location);
        data.putExtra(EXTRA_TIME, time);
        data.putExtra(EXTRA_DATE, date);
        data.putExtra(EXTRA_PRIORITY, priority);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1){
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    //save button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_note) {
            saveNote();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }





}