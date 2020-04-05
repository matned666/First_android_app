package com.workspace.carnote;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

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

    private TextView brandTextView;
    private TextView modelTextView;
    private TextView platesTextView;
    private TextView colorTextView;

    private Switch isDefaultCarSwitch;
    private Button confirmButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_car_layout);
        initializeFields();
        confirmButton.setOnClickListener(v -> {
            if (validate(brandEditText, brandTextView, getResources().getString(R.string.brand_label))
                    && validate(modelEditText, modelTextView, getResources().getString(R.string.model_label))
                    && validate(platesEditText, platesTextView, getResources().getString(R.string.plates_label))
                    && validate(colorEditText, colorTextView, getResources().getString(R.string.color_label))) {
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
        brandTextView = findViewById(R.id.addCar_brand_textView);
        modelTextView = findViewById(R.id.addCar_model_textView);
        platesTextView = findViewById(R.id.addCar_plates_textView);
        colorTextView = findViewById(R.id.addCar_color_textView);
        isDefaultCarSwitch = findViewById(R.id.is_Default_Car);
        confirmButton = findViewById(R.id.confirm);
        brandEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validate(brandEditText, brandTextView, getResources().getString(R.string.brand_label));
            }
        });
        modelEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validate(modelEditText, modelTextView,getResources().getString(R.string.model_label));
            }
        });
        colorEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validate(colorEditText, colorTextView, getResources().getString(R.string.color_label));
            }
        });
        platesEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validate(platesEditText, platesTextView,getResources().getString(R.string.plates_label));
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private boolean validate(EditText editText, TextView textView, String backText) {
        String text = editText.getText().toString();
        if (text.trim().equals("")) {
            textView.setText("Type something");
            textView.setTextColor(Color.parseColor("#ff0000"));
            return false;
        } else {
            textView.setText(backText);
            textView.setTextColor(Color.parseColor("#000000"));
            return true;
        }
    }
}
