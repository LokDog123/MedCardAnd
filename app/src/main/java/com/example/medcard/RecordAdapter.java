package com.example.medcard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

public class RecordAdapter extends ArrayAdapter<Record> {
    public RecordAdapter(Context context, List<Record> records) {
        super(context, 0, records);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Record record = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_record, parent, false);
        }

        TextView titleTextView = convertView.findViewById(R.id.record_title);
        TextView dateTextView = convertView.findViewById(R.id.record_date);
        TextView categoryTextView = convertView.findViewById(R.id.record_category);
        TextView descriptionTextView = convertView.findViewById(R.id.record_description);
        TextView dateDoctor = convertView.findViewById(R.id.record_docktor);
        TextView dateDosage = convertView.findViewById(R.id.record_dosage);
        TextView dateAllergies = convertView.findViewById(R.id.record_allergies);

        titleTextView.setText(record.getTitle());
        dateTextView.setText(record.getDate());
        categoryTextView.setText(record.getCategory().substring(0, 1).toUpperCase() + record.getCategory().substring(1));
        descriptionTextView.setText(record.getDescription());

        if(Objects.equals(record.getDoctor(), "")){
            dateDoctor.setText(R.string.doctor_null);
        }
        else{
            dateDoctor.setText(record.getDoctor());
        }

        if(Objects.equals(record.getDosage(), "")){
            dateDosage.setText(R.string.dosage_null);
        }
        else{
            dateDosage.setText(record.getDosage());
        }

        if(Objects.equals(record.getReaction(), "")){
            dateAllergies.setText(R.string.allergies_null);
        }
        else{
            dateAllergies.setText(record.getReaction());
        }
        return convertView;
    }
}