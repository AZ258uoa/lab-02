package com.example.listycity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ListView cityList;
    ArrayAdapter<String> cityAdapter;
    ArrayList<String> dataList;

    int selectedPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        cityList = findViewById(R.id.city_list);
        Button addBtn = findViewById(R.id.btn_add_city);
        Button deleteBtn = findViewById(R.id.btn_delete_city);

        String []cities = {"Edmonton", "Vancouver", "Moscow", "Sydney", "Berlin", "Vienna", "Tokyo", "Beijing", "Osaka", "New Delhi"};

        dataList = new ArrayList<>();
        dataList.addAll(Arrays.asList(cities));

        cityAdapter = new ArrayAdapter<>(this, R.layout.content, dataList);
        cityList.setAdapter(cityAdapter);

        cityList.setOnItemClickListener((parent, view, position, id) -> {
            selectedPos = position;
            cityList.setItemChecked(position, true);
        });

        addBtn.setOnClickListener(v -> showAddCityDialog());

        deleteBtn.setOnClickListener(v -> deleteSelectedCity());


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showAddCityDialog() {
        EditText input = new EditText(this);
        input.setHint("Type city name...");

        new AlertDialog.Builder(this)
                .setTitle("ADD CITY")
                .setView(input)
                .setPositiveButton("CONFIRM", (dialog, which) -> {
                    String cityName = input.getText().toString().trim();

                    if (TextUtils.isEmpty(cityName)) {
                        Toast.makeText(this, "City name cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    if (dataList.contains(cityName)) {
                        Toast.makeText(this, "City already exists", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    dataList.add(cityName);
                    cityAdapter.notifyDataSetChanged();
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }

    private void deleteSelectedCity() {
        if (selectedPos < 0 || selectedPos >= dataList.size()) {
            Toast.makeText(this, "Please tap a city first", Toast.LENGTH_SHORT).show();
            return;
        }

        dataList.remove(selectedPos);
        cityAdapter.notifyDataSetChanged();

        // 删除后清空选中状态
        cityList.clearChoices();
        selectedPos = -1;
    }
}