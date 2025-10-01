package com.example.medcard;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView recordsListView;
    private RecordAdapter adapter;
    private Spinner categorySpinner;
    private SharedPreferences sharedPref;
    private WebView webView;
    private ImageButton securityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPref = getSharedPreferences("ThemePref", Context.MODE_PRIVATE);
        String savedTheme = sharedPref.getString("theme_mode", ThemeUtils.DEFAULT_MODE);
        ThemeUtils.applyTheme(savedTheme);

        setContentView(R.layout.activity_main);

        ImageButton themeSwitch = findViewById(R.id.theme_switch);
        updateThemeIcon();

        themeSwitch.setOnClickListener(view -> toggleTheme());

        recordsListView = findViewById(R.id.records_list);
        categorySpinner = findViewById(R.id.category_spinner);

        setupCategorySpinner();
        DataManager.loadFromFile(this);
        loadRecords(null);

        NotificationChannel chanel=null;
        if(DataManager.notification()){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            chanel = new NotificationChannel(
                    "test",
                    "test notifiction",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(chanel);



            Notification notification = new NotificationCompat.Builder(this,"test")
                    .setSmallIcon(R.drawable.shield_watch)
                    .setContentTitle(getString(R.string.notification_title))
                    .setContentText(getString(R.string.notification_body))
                    .build();
            notificationManager.notify(42,notification);
        }}

        recordsListView.setOnItemClickListener((parent, view, position, id) -> {
            Record selectedRecord = (Record) adapter.getItem(position);
            Toast.makeText(this, Integer.toString(selectedRecord.getId()), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, AddEditRecordActivity.class);
            intent.putExtra("record_id", selectedRecord.getId());
            startActivity(intent);
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEditRecordActivity.class);
                startActivity(intent);
            }
        });

        webView = findViewById(R.id.webView);
        securityButton = findViewById(R.id.sequrity);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        securityButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PrivacyPolicyActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        DataManager.saveToFile(this);
    }


    @Override
    public void onBackPressed() {
        if (webView.getVisibility() == View.VISIBLE && webView.canGoBack()) {
            webView.goBack();
        } else if (webView.getVisibility() == View.VISIBLE) {
            webView.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    private void toggleTheme() {
        String currentTheme = sharedPref.getString("theme_mode", ThemeUtils.DEFAULT_MODE);
        if (currentTheme == null) {
            currentTheme = ThemeUtils.DEFAULT_MODE;
        }

        String newTheme;
        switch (currentTheme) {
            case ThemeUtils.LIGHT_MODE:
                newTheme = ThemeUtils.DARK_MODE;
                break;
            case ThemeUtils.DARK_MODE:
                newTheme = ThemeUtils.LIGHT_MODE;
                break;
            default:
                if ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
                        == Configuration.UI_MODE_NIGHT_YES) {
                    newTheme = ThemeUtils.LIGHT_MODE;
                } else {
                    newTheme = ThemeUtils.DARK_MODE;
                }
                break;
        }

        sharedPref.edit().putString("theme_mode", newTheme).apply();
        ThemeUtils.applyTheme(newTheme);
        updateThemeIcon();
    }

    private void updateThemeIcon() {
        ImageButton themeSwitch = findViewById(R.id.theme_switch);
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            themeSwitch.setImageResource(R.drawable.ic_day);
        } else {
            themeSwitch.setImageResource(R.drawable.ic_night);
        }
    }


    private void setupCategorySpinner() {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                String allCategories = getResources().getStringArray(R.array.categories_array)[0];
                if (selectedCategory.equals(allCategories)) {
                    loadRecords(null);
                } else {
                    loadRecords(selectedCategory.toLowerCase());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void loadRecords(String category) {
        List<Record> records;
        if (category == null) {
            records = DataManager.getAllRecords();
        } else {
            records = DataManager.getRecordsByCategory(category);
        }

        adapter = new RecordAdapter(this, records);
        recordsListView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecords(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}