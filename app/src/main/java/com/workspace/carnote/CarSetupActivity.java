package com.workspace.carnote;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.workspace.carnote.model.AutoData;
import com.workspace.carnote.model.GsonQuest;

import java.util.ArrayList;
import java.util.Objects;

public class CarSetupActivity extends AppCompatActivity {

    public static final String CARS_DATA = "CARS DATA";

    private static final int REQUEST_CODE_AddCarFormActivity = 123;
    public static final String AUTO_PREF222 = "AUTO_PREF2";
    public static final int REQUEST_CODE_EditCarFormActivity =124;

    private Spinner autoChooseSpinner;
    private ArrayList<AutoData> cars;
    private ArrayAdapter spinnerAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cars_setup_layout);
        getIntence();
//        initAutoList_cars();
        initView();
        initArrayAdapter();
    }

    private void initView() {
        Button addCarButton = findViewById(R.id.go_to_add_car_form_btn);
        Button removeCarButton = findViewById(R.id.go_to_remove_car_form_btn);
        Button editCarButton = findViewById(R.id.go_to_edit_car_form_btn);
        Button confirm = findViewById(R.id.confirm_cars_changes_btn);
        addCarButton.setOnClickListener(goToAddCarFormActivity());
        removeCarButton.setOnClickListener(goToRemoveCarFormActivity());
        editCarButton.setOnClickListener(goToEditCarFormActivity());
        confirm.setOnClickListener(confirmFormActivity());
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cars);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

//    //TODO ----> Saving in case of activity close TO BE REDONE
//    @Override
//    protected void onResume() {
//        super.onResume();
//        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(AUTO_PREF222, GsonQuest.make(cars));
//        editor.apply();
//    }
//
//    private void initAutoList_cars() {
//        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
//        String string = sharedPreferences.getString(AUTO_PREF222, null);
//        ArrayList<AutoData> newCarsList = GsonQuest.getList(string);
//        if (newCarsList != null) {
//            cars = newCarsList;
//        }
//        else cars = new ArrayList<>();
//    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainMenuActivity.class);
        getOutgoingIntent(intent);
        super.onBackPressed();
    }

    private View.OnClickListener confirmFormActivity() {
        return v -> {
            Intent intent = new Intent();
            getOutgoingIntent(intent);
            finish();
        };
    }

    private void getOutgoingIntent(Intent intent) {
        intent.putExtra("PUT CARS", GsonQuest.make(cars));
        System.out.println(GsonQuest.make(cars));
        setResult(Activity.RESULT_OK, intent);
    }

    private void getIntence() {
        cars = GsonQuest.getList((String) Objects.requireNonNull(getIntent().getExtras()).get(MainMenuActivity.GIVE_CARS_LIST));
    }

    private void initArrayAdapter() {
        autoChooseSpinner = findViewById(R.id.auto_choose_spinner);
        autoChooseSpinner.setAdapter(spinnerAdapter);
    }

    private View.OnClickListener goToAddCarFormActivity() {
        return v -> {
            Intent intent = new Intent(CarSetupActivity.this, AddCarActivity.class);
            startActivityForResult(intent, REQUEST_CODE_AddCarFormActivity);
        };
    }

    private View.OnClickListener goToRemoveCarFormActivity() {
        return v -> confirmCarRemoveDialog();
    }

    private View.OnClickListener goToEditCarFormActivity() {
        return v -> {
            if(getCurrentCar() != null) {
                Intent intent = new Intent(CarSetupActivity.this, EditCarActivity.class);
                intent.putExtra("CURRENT_CAR", getCurrentCar());
                startActivityForResult(intent, REQUEST_CODE_EditCarFormActivity);
            }
        };
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_AddCarFormActivity) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    AutoData newAutoData = (AutoData) Objects.requireNonNull(data.getExtras()).get(AddCarActivity.AUTO_DATA_NEW_CAR);
                    Boolean isNewCarDefaultCar = (Boolean) data.getExtras().get(AddCarActivity.IS_NEW_CAR_DEFAULT);
                    if (isNewCarDefaultCar != null && isNewCarDefaultCar) {
                        cars.add(0, newAutoData);
                        autoChooseSpinner.setAdapter(spinnerAdapter);
                        autoChooseSpinner.setSelection(0, false);
                    }
                    else {
                        cars.add(newAutoData);
                        autoChooseSpinner.setAdapter(spinnerAdapter);
                        autoChooseSpinner.setSelection(cars.size()-1, false);
                    }
                }
            }
        }
    if (requestCode == REQUEST_CODE_EditCarFormActivity) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    AutoData editedCarData = (AutoData) Objects.requireNonNull(data.getExtras()).get(EditCarActivity.AUTO_DATA_EDITED_CAR);
                    Boolean isTheCarDefaultCar = (Boolean) data.getExtras().get(EditCarActivity.IS_THE_CAR_DEFAULT);
                    if (isTheCarDefaultCar != null && isTheCarDefaultCar)
                        setCurrentCar(true, editedCarData);
                    else setCurrentCar(false, editedCarData);
                }
            }
        }
        initArrayAdapter();
    }

    private void setCurrentCar(boolean b, AutoData editedCarData) {
        cars.remove(getCurrentCar());
        if (b){
            cars.add(0, editedCarData);
        }else{
            cars.add(editedCarData);
        }
    }

    private AutoData getCurrentCar() {
        return (AutoData) autoChooseSpinner.getSelectedItem();
    }

    private void confirmCarRemoveDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog  .setMessage("REMOVE THE SELECTED CAR?\n"+getCurrentCar().toString())
                .setPositiveButton("Yes", (dialog1, id) -> {
                    cars.remove(getCurrentCar());
                    autoChooseSpinner.setAdapter(spinnerAdapter);
                    autoChooseSpinner.setSelection(0, false);
                    dialog1.dismiss();
                })
                .setNegativeButton("No", (dialog12, id) -> {
                    dialog12.cancel();
                    dialog12.dismiss();
                })
                .show();
    }


}
