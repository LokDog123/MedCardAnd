package com.example.medcard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import java.util.Calendar;
import java.util.Locale;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import androidx.appcompat.widget.Toolbar;

public class AddEditRecordActivity extends AppCompatActivity {
    private EditText titleEditText, dateEditText, descriptionEditText, doctorEditText, dosageEditText, reactionEditText;
    private Spinner categorySpinner;
    private Button saveButton, deleteButton;
    private Record currentRecord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_record);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeViews();
        setupCategorySpinner();

        int recordId = getIntent().getIntExtra("record_id", -1);
        if (recordId != -1) {
            currentRecord = DataManager.getRecordById(recordId);
            populateForm(currentRecord);
        }



        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRecord();
            }
        });

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddEditRecordActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                // Format the date and set it to the EditText
                                String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                                dateEditText.setText(selectedDate);
                            }
                        },
                        year, month, day
                );
                datePickerDialog.show();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });
    }
    private void initializeViews() {
        titleEditText = findViewById(R.id.title_edit_text);
        dateEditText = findViewById(R.id.date_edit_text);
        descriptionEditText = findViewById(R.id.description_edit_text);
        doctorEditText = findViewById(R.id.doctor_edit_text);
        dosageEditText = findViewById(R.id.dosage_edit_text);
        reactionEditText = findViewById(R.id.reaction_edit_text);
        categorySpinner = findViewById(R.id.category_spinner);
        saveButton = findViewById(R.id.save_button);
        deleteButton = findViewById(R.id.delete_button);
    }
    private void setupCategorySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.Delete_Button))
                .setMessage(getString(R.string.confirmation))
                .setPositiveButton(getString(R.string.Delete_Button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteRecord();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void deleteRecord() {
        if (currentRecord != null) {
            DataManager.deleteRecord(currentRecord.getId());
            finish();
        }
    }

    private void populateForm(Record record) {
        titleEditText.setText(record.getTitle());
        dateEditText.setText(record.getDate());
        descriptionEditText.setText(record.getDescription());
        doctorEditText.setText(record.getDoctor());
        dosageEditText.setText(record.getDosage());
        reactionEditText.setText(record.getReaction());

        String[] categories = getResources().getStringArray(R.array.categories_array);
        for (int i = 0; i < categories.length; i++) {
            if (categories[i].equalsIgnoreCase(record.getCategory())) {
                categorySpinner.setSelection(i);
                break;
            }
        }
    }
    private void saveRecord() {
        String title = titleEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String doctor = doctorEditText.getText().toString().trim();
        String dosage = dosageEditText.getText().toString().trim();
        String reaction = reactionEditText.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString().toLowerCase();

        if (title.isEmpty()) {
            Toast.makeText(this, getString(R.string.requirment), Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentRecord == null) {
            Record newRecord = new Record(0, title, category, date, description, doctor, dosage, reaction);
            DataManager.addRecord(newRecord);
        } else {
            currentRecord.setTitle(title);
            currentRecord.setCategory(category);
            currentRecord.setDate(date);
            currentRecord.setDescription(description);
            currentRecord.setDoctor(doctor);
            currentRecord.setDosage(dosage);
            currentRecord.setReaction(reaction);
            DataManager.updateRecord(currentRecord);
        }
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}