package com.workspace.carnote;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.workspace.carnote.model.AutoData;
import com.workspace.carnote.model.GsonQuest;
import com.workspace.carnote.model.HistoryAdapter;
import com.workspace.carnote.model.Record;

import java.util.ArrayList;
import java.util.Objects;

public class MainMenuActivity extends AppCompatActivity {

    public static final String SPECIAL_DATA = "SPECIAL_DATA";
    public static final String GIVE_CARS_LIST = "GIVE CARS LIST";
    public static final String AUTO_PREF = "AUTO PREF";
    public static final int REQUEST_CODE_AddCarFormActivity = 12344;
    private static final int REQUEST_CODE_CarFormActivity = 12345;
    private static final int REQUEST_CODE_tankUpActivity = 12346;
    public static final int REQUEST_CODE_repair = 12347;
    public static final int REQUEST_CODE_collision = 12348;
    public static final int REQUEST_CODE_costs = 12349;

    private Button goToTankFormButton;
    private Button goRepairFormButton;
    private Button goCollisionFormButton;
    private Button goCarsFormButton;
    private Button goToAdditionalCostsFormButton;
    private Button goToRaportFormButton;
    private Button goToSettingsFormButton;

    private RecyclerView historyRecyclerView;
    private RecyclerView.Adapter<HistoryAdapter.ViewHolder> historyAdapter;
    private RecyclerView.LayoutManager historyLayoutManager;

    private Spinner autoChooseSpinner;
    private ArrayList cars;
    private ArrayAdapter<AutoData> spinnerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_layout);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(AUTO_PREF, gson.toJson(cars));
        editor.apply();
    }

    private void initView() {
        initializeButtons();
        initializeButtonActions();
        initAutoList_cars();
        initArrayAdapter();
        initRecycleView();
        if(cars.isEmpty()){
            Intent intent = new Intent(MainMenuActivity.this, AddCarActivity.class);
            startActivityForResult(intent, REQUEST_CODE_AddCarFormActivity);
        }
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
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cars);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoChooseSpinner.setAdapter(spinnerAdapter);
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

    private void initAutoList_cars() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        String string = sharedPreferences.getString(AUTO_PREF, null);

        Gson gson = new Gson();
        ArrayList<AutoData> newCarsList = gson.fromJson(string, new TypeToken<ArrayList<AutoData>>() {
        }.getType());

        if (newCarsList != null){
            cars = newCarsList;
        } else cars = new ArrayList<>();
    }

    private void addNewCarTankUpRecordsList() {
        historyAdapter = new HistoryAdapter(this, getCurrentCar() != null ? getCurrentCar().getRecords() : new ArrayList<Record>());
        historyRecyclerView.setAdapter(historyAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_CarFormActivity) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    cars = GsonQuest.getList((String) Objects.requireNonNull(data.getExtras()).get("PUT CARS"));
                    initArrayAdapter();
                    addNewCarTankUpRecordsList();
                }
            }
        } else if (requestCode == REQUEST_CODE_tankUpActivity) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    getCurrentCar().getRecords().add(0,(Record) data.getExtras().get(GasTankUpActivity.AUTO_DATA_NEW_TANK_UP));
                }
            }
        }

        if (requestCode == REQUEST_CODE_AddCarFormActivity) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    AutoData newAutoData = (AutoData) data.getExtras().get(AddCarActivity.AUTO_DATA_NEW_CAR);
                    Boolean isNewCarDefaultCar = (Boolean) data.getExtras().get(AddCarActivity.IS_NEW_CAR_DEFAULT);

                    if (isNewCarDefaultCar != null && isNewCarDefaultCar) {
                        cars.add(0, newAutoData);
                        autoChooseSpinner.setAdapter(spinnerAdapter);
                        autoChooseSpinner.setSelection(0, false);
                    }

                    else cars.add(newAutoData);
                    autoChooseSpinner.setAdapter(spinnerAdapter);
                    autoChooseSpinner.setSelection(cars.size()-1, false);
                }
            }
        }else if (requestCode == REQUEST_CODE_repair) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    getCurrentCar().getRecords().add(0,(Record) data.getExtras().get(RepairActivity.AUTO_DATA));
                }
            }
        }else if (requestCode == REQUEST_CODE_collision) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    getCurrentCar().getRecords().add(0,(Record) data.getExtras().get(RepairActivity.AUTO_DATA));
                }
            }
        }else if (requestCode == REQUEST_CODE_costs) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    getCurrentCar().getRecords().add(0,(Record) data.getExtras().get(RepairActivity.AUTO_DATA));
                }
            }
        }
        historyAdapter.notifyDataSetChanged();
    }


    private View.OnClickListener goToCarFormActivity() {
        return v -> {
            Intent intent = new Intent(MainMenuActivity.this, CarSetupActivity.class);
            intent.putExtra(GIVE_CARS_LIST, GsonQuest.make(cars));
            startActivityForResult(intent, REQUEST_CODE_CarFormActivity);
        };
    }

    private View.OnClickListener goToAdditionalCostsFormActivity() {
        return v -> {
            Intent intent = new Intent(MainMenuActivity.this, AdditionalCostsActivity.class);
            intent.putExtra(SPECIAL_DATA, getCurrentCar());
            startActivityForResult(intent, REQUEST_CODE_costs);
        };
    }

    private View.OnClickListener goToCollisionFormActivity() {
        return v -> {
            Intent intent = new Intent(MainMenuActivity.this, CollisionActivity.class);
            intent.putExtra(SPECIAL_DATA, getCurrentCar());
            startActivityForResult(intent, REQUEST_CODE_collision);

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
            intent.putExtra(SPECIAL_DATA, getCurrentCar());
            startActivityForResult(intent, REQUEST_CODE_repair);

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

    private AutoData getCurrentCar() {
        return (AutoData) autoChooseSpinner.getSelectedItem();
    }

}
