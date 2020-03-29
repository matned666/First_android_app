package com.workspace.carnote;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.workspace.carnote.model.AutoData;

import java.util.ArrayList;
import java.util.Collection;

public class CarSetupActivity extends AppCompatActivity {

    public static final String CARS_DATA = "CARS DATA";

    private static final int REQUEST_CODE_AddCarFormActivity = 123;

    private Button addCarButton;
    private Button removeCarButton;
    private Button editCarButton;

    private Spinner autoChooseSpinner;
    private ArrayList<AutoData> cars;
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

        addCarButton.setOnClickListener(goToAddCarFormActivity());
        removeCarButton.setOnClickListener(goToRemoveCarFormActivity());

    }

    private void getIntence() {
        cars.addAll((Collection<? extends AutoData>) getIntent().getExtras().getSerializable(MainMenuActivity.GIVE_CARS_LIST));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent();
        intent.putExtra(CARS_DATA, cars);
        setResult(Activity.RESULT_OK, intent);
        finish();
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
        dialog  .setMessage("Are you sure?")
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
