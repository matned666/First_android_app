package com.workspace.carnote;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.workspace.carnote.model.AutoData;
import com.workspace.carnote.model.TankUpRecord;

import java.util.ArrayList;
import java.util.Collection;

public class MainMenuActivity extends AppCompatActivity {

    public static final String SPECIAL_DATA = "SPECIAL_DATA";
    public static final String GIVE_CARS_LIST = "GIVE CARS LIST";
    private int REQUEST_CODE_CarFormActivity = 12345;
    private int REQUEST_CODE_tankUpActivity = 12346;

    private Button goToTankFormButton;
    private Button goRepairFormButton;
    private Button goCollisionFormButton;
    private Button goCarsFormButton;

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
        initializeButtons();

        cars = new ArrayList<>();
        add_DEMO_Autodata();
        initArrayAdapter();
        autoChooseSpinner.setAdapter(arrayAdapter);

        goToTankFormButton.setOnClickListener(goToTankUpActivity());
        goCarsFormButton.setOnClickListener(goToCarFormActivity());

    }

    private void initializeButtons() {
        goToTankFormButton = findViewById(R.id.go_to_tank_btn);
        goRepairFormButton = findViewById(R.id.go_to_repair_btn);
        goCollisionFormButton = findViewById(R.id.go_to_collision_btn);
        goCarsFormButton = findViewById(R.id.go_to_car_form_btn);
        autoChooseSpinner = findViewById(R.id.auto_choose_spinner);
    }

    private void initArrayAdapter() {
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cars);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }


    private View.OnClickListener goToCarFormActivity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, CarSetupActivity.class);
                intent.putExtra(GIVE_CARS_LIST, cars);
                startActivityForResult(intent, REQUEST_CODE_CarFormActivity);
            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_CarFormActivity) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    cars = new ArrayList<AutoData>();
                    cars.addAll((Collection<? extends AutoData>) getIntent().getExtras().getSerializable(CarSetupActivity.CARS_DATA));
                    initArrayAdapter();
                    autoChooseSpinner.setAdapter(arrayAdapter);
//TODO dane z add car gubią się w main menu

                }
            }
        }else if (requestCode == REQUEST_CODE_tankUpActivity){
            if(data != null) {
                getCurrentCar().getTankUpRecord().add((TankUpRecord) data.getExtras().get(GasTankUpActivity.AUTO_DATA_NEW_TANK_UP));
            }
        }

    }

    private View.OnClickListener goToTankUpActivity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, GasTankUpActivity.class);
                intent.putExtra(SPECIAL_DATA, getCurrentCar());
                startActivityForResult(intent, REQUEST_CODE_tankUpActivity);
            }
        };
    }

    private void add_DEMO_Autodata(){
        cars.add(new AutoData.Builder()
                .brand("DEFAULT")
                .model("CAR")
                .color("BLUE")
                .plates("MILES")
                .build());
    }



    private AutoData getCurrentCar() {
        return (AutoData) autoChooseSpinner.getSelectedItem();
    }
}
