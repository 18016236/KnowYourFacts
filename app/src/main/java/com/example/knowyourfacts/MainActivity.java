package com.example.knowyourfacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int reqCode = 12345;
    ArrayList<Fragment> al;
    MyFragmentPagerAdapter adapter;
    ViewPager vPager;
    SharedPreferences sp;
    Button btnReadLater,btnChangeColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vPager = findViewById(R.id.viewpager1);
        btnChangeColor = findViewById(R.id.btnChangeColour);
        btnReadLater = findViewById(R.id.btnReadLater);

        FragmentManager fm = getSupportFragmentManager();

        sp = getSharedPreferences("index",MODE_PRIVATE);

        al = new ArrayList<Fragment>();
        al.add(new Frag1());
        al.add(new Frag2());
        al.add(new Frag3());

        adapter = new MyFragmentPagerAdapter(fm, al);

        vPager.setAdapter(adapter);



btnReadLater.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 5);

        Intent intent = new Intent(MainActivity.this,
                ScheduleNotificationReceiver.class);

        int reqCode = 12345;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                MainActivity.this, reqCode,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager am = (AlarmManager)
                getSystemService(Activity.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                pendingIntent);

    }
});


    }


    @Override
    protected void onPause() {
        int index = vPager.getCurrentItem();
        SharedPreferences.Editor prefs = sp.edit();
        prefs.putInt("index",index);
        prefs.commit();
        super.onPause();
    }

    @Override
    protected void onResume() {
        int index = sp.getInt("index",0);
        vPager.setCurrentItem(index);
        super.onResume();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_next:
                int max = vPager.getChildCount();
                if (vPager.getCurrentItem() < max-1){
                    int nextPage = vPager.getCurrentItem() + 1;
                    vPager.setCurrentItem(nextPage, true);
                }
                return true;

            case R.id.action_previous:
                if (vPager.getCurrentItem() > 0){
                    int previousPage = vPager.getCurrentItem() - 1;
                    vPager.setCurrentItem(previousPage, true);
                }
            case R.id.action_random:
                Random randomno = new Random();
                int total = vPager.getChildCount();
                vPager.setCurrentItem(randomno.nextInt(total), true);
        }

        return super.onOptionsItemSelected(item);
    }

}