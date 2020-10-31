package nz.ac.aut.msd.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.polyak.iconswitch.IconSwitch;

public class MainActivity extends AppCompatActivity {

    private static final int HIGHEST_NODE = 3;
    private TextView title;
    private TextView location;
    private TextView time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.tvTitle);
        location = findViewById(R.id.tvLocation);
        time = findViewById(R.id.tvTime);

        //here to set the title, location and time with the highest priority in calendar

        //checked, in the time frame enter the geofence will trigger the silence mode
        //unchecked, nothing happen
        CheckBox checkBox = findViewById(R.id.cb_Mode);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    autoModeOn();
                }else {
                    autoModeOff();
                }
            }
        });

        //hand control the ringer mode
        //feature improve the switch follow the auto
        IconSwitch iconSwitch = findViewById(R.id.sMode);
        iconSwitch.setCheckedChangeListener(new IconSwitch.CheckedChangeListener() {
            @Override
            public void onCheckChanged(IconSwitch.Checked current) {
                //permission
                NotificationManager notificationManager = (NotificationManager) getSystemService(
                        Context.NOTIFICATION_SERVICE);
                if (! notificationManager.isNotificationPolicyAccessGranted()){
                    Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                    startActivity(intent);
                }else {
                    AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    switch (current) {
                        case LEFT:
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                            Toast.makeText(MainActivity.this, "Set to Silent Mode",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case RIGHT:
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                            Toast.makeText(MainActivity.this, "Set to Normal Mode",
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //set the title location time to the highest priority, receive from the calendar
        //NOT working yet
        if (requestCode == HIGHEST_NODE && resultCode == RESULT_OK){
            title.setText(data.getStringExtra(calendarActivity.CALENDAR_TITLE));
            location.setText(data.getStringExtra(calendarActivity.CALENDAR_LOCATION));
            time.setText(data.getStringExtra(calendarActivity.CALENDAR_TIME));
            Toast.makeText(this, "receive"
                    + data.getStringExtra(calendarActivity.CALENDAR_TITLE),
                    Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(this, "no receive", Toast.LENGTH_SHORT).show();
        }
    }

    //open the calendar list activity
    public void calendarClick(View view){
        Intent intent = new Intent(MainActivity.this, calendarActivity.class);
        startActivityForResult(intent, HIGHEST_NODE);
    }

    public void autoModeOn(){
        Toast.makeText(this, "AutoMode Not Working Yet...", Toast.LENGTH_SHORT).show();
    }

    public void autoModeOff(){
        Toast.makeText(this, "Disable AutoMode Not Working Yet...",
                Toast.LENGTH_SHORT).show();
    }

}
