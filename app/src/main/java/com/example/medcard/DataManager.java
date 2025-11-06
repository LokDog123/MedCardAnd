package com.example.medcard;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.LinkedHashMap;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import kotlin.text.UStringsKt;

public class DataManager {
    private static List<Record> records = new ArrayList<>();
    private static final String FILE_NAME = "records.json";
    private static int nextId = 1;



    public static List<Record> getAllRecords() {
        return new ArrayList<>(records);
    }

    public static String[] Doctor_list(){
        ArrayList<String> doc_list=new ArrayList<String>();
        for(Record record:records){
            doc_list.add(record.getDoctor());
        }
        String[] doctor_list=new String[doc_list.size()];
        for(int i=0;i<doc_list.size();i++){
            doctor_list[i]=doc_list.get(i);
        }
        return doctor_list;
    }


    public static int DocQuantity(String doctor){
      int quantity=0;
      for (Record record: records){
          if(record.getDoctor().equals(doctor)){
              quantity+=1;
          }
      }
      return  quantity;
    }
    public static Boolean notification(){
        Date currentDate = new Date(); // Получаем текущую дату и время

        // Форматируем дату в строку (опционально)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);
        for (Record record: records){
            if(record.getDate().equals(formattedDate)){
                 return true;
            }
        }
        return false;
    }

    public static List<Record> getRecordsByCategory(String category) {
        List<Record> filtered = new ArrayList<>();
        for (Record record : records) {
            if (record.getCategory().equals(category)) {
                filtered.add(record);
            }
        }
        return filtered;
    }

    public static Record getRecordById(int id) {
        for (Record record : records) {
            if (record.getId() == id) {
                return record;
            }
        }
        return null;
    }

    public static void addRecord(Record record) {
        record.setId(nextId++);
        records.add(record);
    }

    public static void updateRecord(Record updatedRecord) {
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getId() == updatedRecord.getId()) {
                records.set(i, updatedRecord);
                break;
            }
        }
    }


    public static void deleteRecord(int id) {
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getId() == id) {
                records.remove(i);
                break;
            }
        }
    }

    public static void saveToFile(Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            String json = gson.toJson(records);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadFromFile(Context context) {
        try {
            FileInputStream fis = context.openFileInput(FILE_NAME);
            InputStreamReader reader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Record>>() {
            }.getType();
            records = gson.fromJson(sb.toString(), listType);

        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
