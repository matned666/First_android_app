package com.workspace.carnote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.workspace.carnote.model.AutoData;

public class AddCarActivity extends AppCompatActivity {

    public static final String AUTO_DATA_NEW_CAR = "AUTO DATA NEW CAR";
    public static final String IS_NEW_CAR_DEFAULT = "IS NEW CAR DEFAULT";

    private EditText brandEditText;
    private EditText modelEditText;
    private EditText colorEditText;
    private EditText platesEditText;
    private Switch isDefaultCarSwitch;
    private Button confirmButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_car_layout);
        initializeFields();
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoData autoData = new AutoData.Builder()
                        .brand(brandEditText.getText().toString())
                        .model(modelEditText.getText().toString())
                        .color(colorEditText.getText().toString())
                        .plates(platesEditText.getText().toString())
                        .build();
                Boolean isDefaultCar = isDefaultCarSwitch.isChecked();
                Intent intent = new Intent();
                intent.putExtra(AUTO_DATA_NEW_CAR, autoData);
                intent.putExtra(IS_NEW_CAR_DEFAULT, isDefaultCar);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

        });

    }

    private void initializeFields() {
        brandEditText = findViewById(R.id.brand_editText);
        modelEditText = findViewById(R.id.model_editText2);
        colorEditText = findViewById(R.id.color_editText3);
        platesEditText = findViewById(R.id.plates_editText4);
        isDefaultCarSwitch = findViewById(R.id.is_Default_Car);
        confirmButton = findViewById(R.id.confirm);
    }
}
