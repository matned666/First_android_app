package com.workspace.carnote;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.util.Date;

public class GasTankUpActivity extends AppCompatActivity {

    private EditText dateEditText;
    private EditText mileageEditText;
    private EditText litersEditText;
    private EditText costEditText;

    private Button confirmButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gas_tank_up_layout);
        dateEditText = findViewById(R.id.date);
        mileageEditText = findViewById(R.id.mileage);
        litersEditText = findViewById(R.id.liters);
        costEditText = findViewById(R.id.cost);

        dateEditText.setText(getCurrentDate());

        confirmButton = findViewById(R.id.confirm);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GasTankUpActivity.this, getOneLitedCost(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getOneLitedCost() {
        return String.valueOf(Double.parseDouble(String.valueOf(costEditText.getText()))/Double.parseDouble(String.valueOf(litersEditText.getText())));
    }

    private String getCurrentDate() {
        DateFormat dateFormat = DateFormat.getDateInstance();
        Date date = new Date();
        return dateFormat.format(date);
    }
}
