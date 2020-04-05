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

import java.util.Objects;

public class EditCarActivity extends AppCompatActivity {

    public static final String AUTO_DATA_EDITED_CAR = "AUTO DATA NEW CAR";
    public static final String IS_THE_CAR_DEFAULT = "IS NEW CAR DEFAULT";

    private EditText brandEditText;
    private EditText modelEditText;
    private EditText colorEditText;
    private EditText platesEditText;

    private TextView brandTextView;
    private TextView modelTextView;
    private TextView colorTextView;
    private TextView platesTextView;

    private Switch isDefaultCarSwitch;
    private Button confirmButton;

    private AutoData currentCar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_car_layout);
        currentCar = (AutoData) Objects.requireNonNull(getIntent().getExtras()).get("CURRENT_CAR");
        initializeFields();

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

        confirmButton.setOnClickListener(v -> {
            onClick();
        });


    }


    @SuppressLint("WrongViewCast")
    private void initializeFields() {
        brandEditText = findViewById(R.id.edit_brand_editText);
        brandEditText.setText(currentCar.getBrand());

        modelEditText = findViewById(R.id.edit_model_editText);
        modelEditText.setText(currentCar.getModel());

        colorEditText = findViewById(R.id.edit_color_editText);
        colorEditText.setText(currentCar.getColor());

        platesEditText = findViewById(R.id.edit_plates_editText);
        platesEditText.setText(currentCar.getPlates());

        isDefaultCarSwitch = findViewById(R.id.edit_is_Default_Car);
        confirmButton = findViewById(R.id.edit_confirm);

        brandTextView = findViewById(R.id.edit_textView);
        modelTextView = findViewById(R.id.edit_textView2);
        colorTextView = findViewById(R.id.edit_textView3);
        platesTextView = findViewById(R.id.edit_textView4);
    }

    private void onClick() {
        if (validate(brandEditText, brandTextView, getResources().getString(R.string.brand_label) )
                && validate(modelEditText, modelTextView,getResources().getString(R.string.model_label))
                && validate(platesEditText, platesTextView,getResources().getString(R.string.plates_label))
                && validate(colorEditText, colorTextView,getResources().getString(R.string.color_label))) {
            currentCar.setBrand(brandEditText.getText().toString());
            currentCar.setModel(modelEditText.getText().toString());
            currentCar.setColor(colorEditText.getText().toString());
            currentCar.setPlates(platesEditText.getText().toString());

            Boolean isDefaultCar = isDefaultCarSwitch.isChecked();
            Intent intent = new Intent();
            intent.putExtra(AUTO_DATA_EDITED_CAR, currentCar);
            intent.putExtra(IS_THE_CAR_DEFAULT, isDefaultCar);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    @SuppressLint("SetTextI18n")
    private boolean validate(EditText editText, TextView textView, String backText) {
        String text = editText.getText().toString();
            if (text.equals("")) {
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
