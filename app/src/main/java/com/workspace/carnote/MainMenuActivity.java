package com.workspace.carnote;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.workspace.carnote.model.AutoData;
import com.workspace.carnote.model.CarsList_DemoFill;
import com.workspace.carnote.model.HistoryAdapter;
import com.workspace.carnote.model.TankUpRecord;

import java.util.ArrayList;

public class MainMenuActivity extends AppCompatActivity {

    public static final String SPECIAL_DATA = "SPECIAL_DATA";
    public static final String GIVE_CARS_LIST = "GIVE CARS LIST";
    private int REQUEST_CODE_CarFormActivity = 12345;
    private int REQUEST_CODE_tankUpActivity = 12346;

    private Button goToTankFormButton;
    private Button goRepairFormButton;
    private Button goCollisionFormButton;
    private Button goCarsFormButton;
    private Button goToAdditionalCostsFormButton;
    private Button goToRaportFormButton;
    private Button goToSettingsFormButton;

    private RecyclerView historyRecyclerView;
    private RecyclerView.Adapter historyAdapter;
    private RecyclerView.LayoutManager historyLayoutManager;

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
        initializeButtonActions();

        add_DEMO_Autodata();
        initArrayAdapter();
        initRecycleView();

    }

    private void initializeButtons() {
        goToTankFormButton = findViewById(R.id.go_to_tank_btn);
        goCarsFormButton = findViewById(R.id.go_to_car_form_btn);
        goRepairFormButton = findViewById(R.id.go_to_repair_btn);
        goCollisionFormButton = findViewById(R.id.go_to_collision_btn);
        autoChooseSpinner = findViewById(R.id.auto_choose_spinner);
        goToAdditionalCostsFormButton = findViewById(R.id.go_to_add_insurance_btn);
        goToRaportFormButton = findViewById(R.id.go_to_raport_btn);
        goToSettingsFormButton = findViewById(R.id.settings_btn);
        historyRecyclerView = findViewById(R.id.history_recycleView);
    }

    private void initializeButtonActions() {
        goToTankFormButton.setOnClickListener(goToTankUpActivity());
        goCarsFormButton.setOnClickListener(goToCarFormActivity());
        goRepairFormButton.setOnClickListener(goToRepairFormActivity());
        goCollisionFormButton.setOnClickListener(goToCollisionFormActivity());
        goToAdditionalCostsFormButton.setOnClickListener(goToAdditionalCostsFormActivity());
        goToRaportFormButton.setOnClickListener(goToRaportFormActivity());
        goToSettingsFormButton.setOnClickListener(goToSettingsFormActivity());
    }

    private void initArrayAdapter() {
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cars);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoChooseSpinner.setAdapter(arrayAdapter);
    }

    private void initRecycleView() {
        historyLayoutManager = new LinearLayoutManager(this);
        historyRecyclerView.setLayoutManager(historyLayoutManager);

        historyRecyclerView.setHasFixedSize(true);
        addNewCarTankUpRecordsList();
        autoChooseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addNewCarTankUpRecordsList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void addNewCarTankUpRecordsList() {
        historyAdapter = new HistoryAdapter(this, getCurrentCar().getTankUpRecord());
        historyRecyclerView.setAdapter(historyAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_CarFormActivity) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
//                    cars.clear();
//                    cars.addAll((ArrayList<AutoData>) getIntent().getExtras().getSerializable(CarSetupActivity.CARS_DATA));

//                    System.out.println((ArrayList<AutoData>) getIntent().getExtras().get(CarSetupActivity.CARS_DATA));

                    addNewCarTankUpRecordsList();

                    System.out.println("MOJA LISTA: " + cars);
//TODO dane z add car gubią się w main menu

                }
            }
        } else if (requestCode == REQUEST_CODE_tankUpActivity) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    getCurrentCar().getTankUpRecord().add(0,(TankUpRecord) data.getExtras().get(GasTankUpActivity.AUTO_DATA_NEW_TANK_UP));
                }
            }
        }
        historyAdapter.notifyDataSetChanged();


    }

    private View.OnClickListener goToCarFormActivity() {
        return v -> {
            Intent intent = new Intent(MainMenuActivity.this, CarSetupActivity.class);
            intent.putExtra(GIVE_CARS_LIST, cars);
            startActivityForResult(intent, REQUEST_CODE_CarFormActivity);
        };
    }

    private View.OnClickListener goToAdditionalCostsFormActivity() {
        return v -> {
            Intent intent = new Intent(MainMenuActivity.this, AdditionalCostsActivity.class);
            startActivity(intent);
        };
    }

    private View.OnClickListener goToCollisionFormActivity() {
        return v -> {
            Intent intent = new Intent(MainMenuActivity.this, CollisionActivity.class);
            startActivity(intent);

        };
    }

    private View.OnClickListener goToRaportFormActivity() {
        return v -> {
            Intent intent = new Intent(MainMenuActivity.this, CostRaportActivity.class);
            startActivity(intent);

        };
    }

    private View.OnClickListener goToRepairFormActivity() {
        return v -> {
            Intent intent = new Intent(MainMenuActivity.this, RepairActivity.class);
            startActivity(intent);

        };
    }

    private View.OnClickListener goToSettingsFormActivity() {
        return v -> {
            Intent intent = new Intent(MainMenuActivity.this, SettingsActivity.class);
            startActivity(intent);

        };
    }

    private View.OnClickListener goToTankUpActivity() {
        return v -> {
            Intent intent = new Intent(MainMenuActivity.this, GasTankUpActivity.class);
            intent.putExtra(SPECIAL_DATA, getCurrentCar());
            startActivityForResult(intent, REQUEST_CODE_tankUpActivity);
        };
    }

    private void add_DEMO_Autodata() {
        cars = new ArrayList<>();
        CarsList_DemoFill.fill(cars,5,20);
    }

    private AutoData getCurrentCar() {
        return (AutoData) autoChooseSpinner.getSelectedItem();
    }
}
