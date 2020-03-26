package com.workspace.carnote;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.workspace.carnote.model.AutoData;
import com.workspace.carnote.model.TankUpRecord;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

public class GasTankUpActivity extends AppCompatActivity {

    private EditText dateEditText;
    private EditText mileageEditText;
    private EditText litersEditText;
    private EditText costEditText;

    private DateFormat dateFormat;

    private Button confirmButton;
    private AutoData autoData;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gas_tank_up_layout);
        viewInit();
        getIntence();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getIntence() {
        autoData = (AutoData) Objects.requireNonNull(getIntent().getExtras()).getSerializable(MainMenuActivity.SPECIAL_DATA);
    }

    private void viewInit() {
        dateEditText = findViewById(R.id.date);
        mileageEditText = findViewById(R.id.mileage);
        litersEditText = findViewById(R.id.liters);
        costEditText = findViewById(R.id.cost);
        confirmButton = findViewById(R.id.confirm);
        dateEditText.setText(getCurrentDate());
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GasTankUpActivity.this, getOneLiterCost(), Toast.LENGTH_SHORT).show();
                TankUpRecord tank = new TankUpRecord.Builder()
                        .tankUpDate(getDate())
                        .mileage(getMileage())
                        .tankedUpGasLiters(getLiters())
                        .costInPLN(getCost())
                        .build();
                autoData.getTankUpRecord().add(tank);
            }
        });
    }

    private Date getDate() {
        try {
            return dateFormat.parse(String.valueOf(dateEditText.getText()));
        } catch (ParseException e) {
            return new Date();
        }
    }

    private Integer getCost() {
        return Integer.valueOf(String.valueOf(costEditText.getText()));
    }

    private Integer getLiters() {
        return Integer.valueOf(String.valueOf(litersEditText.getText()));
    }

    private Integer getMileage() {
        return Integer.valueOf(String.valueOf(mileageEditText.getText()));
    }



    private String getOneLiterCost() {
        return String.valueOf(Double.parseDouble(String.valueOf(costEditText.getText()))/Double.parseDouble(String.valueOf(litersEditText.getText())));
    }

    private String getCurrentDate() {
        dateFormat = DateFormat.getDateInstance();
        Date date = new Date();
        return dateFormat.format(date);
    }
}
