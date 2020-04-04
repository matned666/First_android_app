package com.workspace.carnote;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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
    public static final String AUTO_PREF = "AUTO_PREF";

    private Button addCarButton;
    private Button removeCarButton;
    private Button editCarButton;
    private Button confirm;

    private Spinner autoChooseSpinner;
    private ArrayList cars;
    private ArrayAdapter<AutoData> arrayAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cars_setup_layout);
        cars = new ArrayList<>();
        getIntence();
        initArrayAdapter();
        autoChooseSpinner = findViewById(R.id.auto_choose_spinner);
        autoChooseSpinner.setAdapter(arrayAdapter);
        //TODO nie można wybrać auta - coś się zjebało

        addCarButton = findViewById(R.id.go_to_add_car_form_btn);
        removeCarButton = findViewById(R.id.go_to_remove_car_form_btn);
        editCarButton = findViewById(R.id.go_to_edit_car_form_btn);
        confirm = findViewById(R.id.confirm_cars_changes_btn);

        addCarButton.setOnClickListener(goToAddCarFormActivity());
        removeCarButton.setOnClickListener(goToRemoveCarFormActivity());
        confirm.setOnClickListener(confirmFormActivity());
    }

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
        };
    }

    private void getOutgoingIntent(Intent intent) {
        intent.putExtra("PUT CARS", GsonQuest.make(cars));
        setResult(Activity.RESULT_OK, intent);
    }

    private void getIntence() {
        cars = GsonQuest.getList((String) Objects.requireNonNull(getIntent().getExtras()).get(MainMenuActivity.GIVE_CARS_LIST));
    }

    private View.OnClickListener goToAddCarFormActivity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarSetupActivity.this, AddCarActivity.class);
                startActivityForResult(intent, REQUEST_CODE_AddCarFormActivity);
            }
        };
    }

    private View.OnClickListener goToRemoveCarFormActivity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmCarRemoveDialog();
            }
        };
    }

    private View.OnClickListener goToEditCarFormActivity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_AddCarFormActivity) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    AutoData newAutoData = (AutoData) data.getExtras().get(AddCarActivity.AUTO_DATA_NEW_CAR);
                    Boolean isNewCarDefaultCar = (Boolean) data.getExtras().get(AddCarActivity.IS_NEW_CAR_DEFAULT);

                    if (isNewCarDefaultCar != null && isNewCarDefaultCar)
                        cars.add(0, newAutoData);
                    else cars.add(newAutoData);
                }
            }
        }
    }

    private void initArrayAdapter() {
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cars);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private AutoData getCurrentCar() {
        return (AutoData) autoChooseSpinner.getSelectedItem();
    }

    private void confirmCarRemoveDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog  .setMessage("REMOVE THE SELECTED CAR?\n"+getCurrentCar().toString())
                .setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        cars.remove(getCurrentCar());
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }

}
