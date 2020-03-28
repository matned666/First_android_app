package com.workspace.carnote;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.workspace.carnote.model.AutoData;

import java.util.ArrayList;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity {

    public static final String SPECIAL_DATA = "SPECIAL_DATA";
    private ImageButton goToTankFormButton;
    private Spinner autoChooseSpinner;
    private ArrayList<AutoData> cars;
    private ArrayAdapter<AutoData> arrayAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_layout);
        initView();
    }

    private void initView() {
        goToTankFormButton = findViewById(R.id.go_to_tank_btn);
        autoChooseSpinner = findViewById(R.id.auto_choose_spinner);

        cars = new ArrayList<>();
        add_DEMO_Autodata();
        initArrayAdapter();
        autoChooseSpinner.setAdapter(arrayAdapter);

        goToTankFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, GasTankUpActivity.class);
                intent.putExtra(SPECIAL_DATA,getCurentCar());
                startActivity(intent);
            }
        });
    }

    private void initArrayAdapter() {
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cars);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void add_DEMO_Autodata(){
        cars.add(new AutoData.Builder()
                .brand("Ford")
                .model("Turneo")
                .color("Blue")
                .plates("DW 7S301")
                .build());

        cars.add(new AutoData.Builder()
                .brand("Ford")
                .model("Transit")
                .color("Silver")
                .plates("DW 1A642")
                .build());
    }


    private AutoData getCurentCar() {
        return (AutoData) autoChooseSpinner.getSelectedItem();
    }
}
