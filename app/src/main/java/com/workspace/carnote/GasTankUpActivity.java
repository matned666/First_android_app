package com.workspace.carnote;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.workspace.carnote.model.AutoData;
import com.workspace.carnote.model.Record;
import com.workspace.carnote.model.RecordType;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public class GasTankUpActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String TITLE = "Nowe tankowanie";
    public static final String AUTO_DATA_NEW_TANK_UP = "AUTO DATA NEW TANK UP";
    private static final String AUTO_DATA_OBJ = "AUTO DATA OBJ";
    private EditText dateEditText;
    private EditText mileageEditText;
    private EditText litersEditText;
    private EditText costEditText;

    private DateFormat dateFormat;

    private Button confirmButton;
    private AutoData autoData;
    private TextView mileageTextLabel;
    private TextView litersTextLabel;
    private TextView costTextLabel;
    private Date currentDate;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gas_tank_up_layout);
        obtainExtras();
        if (savedInstanceState != null){
            autoData = (AutoData) savedInstanceState.get(AUTO_DATA_OBJ);
        }
        viewInit();
        setTitle(TITLE);
        currentDate = new Date(System.currentTimeMillis());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void viewInit() {
        initFields();
        addOnClickListener_ToConfirmButton();
        addOnFocusChangeListenerTo_Mileage_TextField();
        addOnFocusChangeListenerTo_Liters_TextField();
        addOnFocusChangeListenerTo_Cost_TextField();
    }

    private void initFields() {
        mileageEditText = findViewById(R.id.mileage);
        mileageTextLabel = findViewById(R.id.mileage_label);
        litersEditText = findViewById(R.id.liters);
        litersTextLabel = findViewById(R.id.liters_label);
        costEditText = findViewById(R.id.cost);
        costTextLabel = findViewById(R.id.cost_label);

        dateEditText = findViewById(R.id.date);
        dateEditText.setOnClickListener(v -> datePickDialog());
        dateEditText.setRawInputType(InputType.TYPE_NULL);
        dateEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) datePickDialog();
        });
        dateEditText.setText(getCurrentDate());
        confirmButton = findViewById(R.id.confirm);
    }

    private void datePickDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(GasTankUpActivity.this, GasTankUpActivity.this, year,month,day);
        datePicker.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addOnClickListener_ToConfirmButton() {
        confirmButton.setOnClickListener(v -> {

            if (validateMileage()
                    && validate(litersEditText, litersTextLabel,getResources().getString(R.string.tanked_gas_liters))
                    && validate(costEditText, costTextLabel,getResources().getString(R.string.cost))) {
                Record tank = new Record.Builder(RecordType.TANK_UP, getDate())
                        .mileage(getMileage())
                        .tankedUpGasLiters(getLiters())
                        .costInPLN(getCost())
                        .description("")
                        .build();
                Intent intent = new Intent();
                intent.putExtra(AUTO_DATA_NEW_TANK_UP, tank);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addOnFocusChangeListenerTo_Mileage_TextField() {
        mileageEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validateMileage();
            }
        });
    }

    private void addOnFocusChangeListenerTo_Liters_TextField() {
        litersEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validate(litersEditText, litersTextLabel, getResources().getString(R.string.tanked_gas_liters));
            }
        });
    }

    private void addOnFocusChangeListenerTo_Cost_TextField() {
        costEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validate(costEditText, costTextLabel, getResources().getString(R.string.cost));
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private boolean validate(EditText editText, TextView textView, String backText) {
        boolean isNumeric;
        int cost = 0;
        try {
            cost = Integer.parseInt(editText.getText().toString());
            isNumeric = true;
        } catch (NumberFormatException e) {
            isNumeric = false;
        }
        if(isNumeric) {
             if (cost <= 0) {
                textView.setText("Value must be higher than 0 !");
                textView.setTextColor(Color.parseColor("#ff0000"));
            } else {
                textView.setText(backText);
                textView.setTextColor(Color.parseColor("#000000"));
                return true;
            }
        }else{
            textView.setText("Fill the field correctly !");
            textView.setTextColor(Color.parseColor("#ff0000"));
            return false;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private boolean validateMileage() {
        Integer oldMileage = 0;
        for(int i = getRecordPlaceInHistory(currentDate); i < autoData.getRecords().size(); i++) {
             if(autoData.getRecords().get(i).getRecordType() == RecordType.TANK_UP )   {
                 oldMileage = autoData.getRecords().get(i).getMileage();
                 break;
             }
        }
            if (!mileageEditText.getText().toString().equals("")) {
                int size = autoData.getRecords().size();
                if (size != 0) {
                    Integer newMileage = Integer.valueOf(mileageEditText.getText().toString());

                    if(!isNextMileageRecordHigher(currentDate, newMileage)) {
                        mileageErrorChange("Actual mileage equal/higher than next!");
                        return false;
                    }
                        if (newMileage <= oldMileage) {
                        mileageErrorChange("Actual mileage equal/lower than previous!");
                        return false;
                    } else{
                        mileageTextLabel.setText(getResources().getString(R.string.mileage));
                        mileageTextLabel.setTextColor(Color.parseColor("#000000"));
                        return true;
                    }
                }
                return true;
            } else {
                mileageErrorChange("Actual mileage equal/lower than previous!");
                return false;
            }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean isNextMileageRecordHigher(Date date, Integer newMileage){
        if(getNextTankUpRecord(date) != null) {
            if (getRecordPlaceInHistory(date) == 0) return true;
            else {
                return Objects.requireNonNull(getNextTankUpRecord(date)).getMileage() > newMileage;
            }
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Record getNextTankUpRecord(Date date){
        int newRecordPlace = getRecordPlaceInHistory(date);
        if(newRecordPlace > 0) {
            for (int i = newRecordPlace - 1; i >= 0; i--) {
                if (autoData.getRecords().get(i).getRecordType() == RecordType.TANK_UP)
                    return autoData.getRecords().get(i);
            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private int getRecordPlaceInHistory(Date date){
        int counter = 0;
        for (int i = 0; i < autoData.getRecords().size(); i++ ){
            if(date.compareTo(autoData.getRecords().get(i).getDate()) >= 0) return counter;
            counter ++;
        }
        return counter;
    }

    @SuppressLint("SetTextI18n")
    private void mileageErrorChange(String text) {
        mileageTextLabel.setText(text);
        mileageTextLabel.setTextColor(Color.parseColor("#ff0000"));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void obtainExtras() {
        autoData = (AutoData) Objects.requireNonNull(getIntent().getExtras()).getSerializable(MainMenuActivity.SPECIAL_DATA);
    }

    private Date getDate() {
        try {
            return dateFormat.parse(String.valueOf(dateEditText.getText()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dateFormat = DateFormat.getDateInstance();
        return new Date();
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



    private String getCurrentDate() {
        dateFormat = DateFormat.getDateInstance();
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(AUTO_DATA_OBJ, autoData);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
        currentDate = calendar.getTime();
        dateEditText.setText(dateFormat.format(calendar.getTime()));
    }
}
