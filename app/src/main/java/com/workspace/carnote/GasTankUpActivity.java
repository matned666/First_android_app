package com.workspace.carnote;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.workspace.carnote.model.AutoData;
import com.workspace.carnote.model.TankUpRecord;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public class GasTankUpActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String TITLE = "Nowe tankowanie";
    public static final String AUTO_DATA_NEW_TANK_UP = "AUTO DATA NEW TANK UP";
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gas_tank_up_layout);
        viewInit();
        getIntence();
        setTitle(TITLE);
    }

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
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickDialog();
            }
        });
        dateEditText.setRawInputType(InputType.TYPE_NULL);
        dateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) datePickDialog();
            }
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

    private void addOnClickListener_ToConfirmButton() {
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateMileage() && validate(litersEditText, litersTextLabel) && validate(costEditText, costTextLabel)) {
                    TankUpRecord tank = new TankUpRecord.Builder()
                            .tankUpDate(getDate())
                            .mileage(getMileage())
                            .tankedUpGasLiters(getLiters())
                            .costInPLN(getCost())
                            .build();
                    Intent intent = new Intent();
                    intent.putExtra(AUTO_DATA_NEW_TANK_UP, tank);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    private void addOnFocusChangeListenerTo_Mileage_TextField() {
        mileageEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint({"ResourceType", "SetTextI18n"})
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validateMileage();
                }
            }
        });
    }

    private void addOnFocusChangeListenerTo_Liters_TextField() {
        litersEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint({"ResourceType", "SetTextI18n"})
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validate(litersEditText, litersTextLabel);
                }
            }
        });
    }

    private void addOnFocusChangeListenerTo_Cost_TextField() {
        costEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint({"ResourceType", "SetTextI18n"})
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validate(costEditText, costTextLabel);
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private boolean validate(EditText editText, TextView textView) {
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
                textView.setText(getResources().getString(R.string.cost));
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


    @SuppressLint("SetTextI18n")
    private boolean validateMileage() {
            if (!mileageEditText.getText().toString().equals("")) {
                int size = autoData.getTankUpRecord().size();
                if (autoData.getTankUpRecord().size() != 0) {
                    Integer newMileage = Integer.valueOf(mileageEditText.getText().toString());
                    Integer oldMileage = autoData.getTankUpRecord().get(size - 1).getMileage();
                    if (newMileage <= oldMileage) {
                        mileageErrorChange();
                        return false;
                    } else {
                        mileageTextLabel.setText(getResources().getString(R.string.mileage));
                        mileageTextLabel.setTextColor(Color.parseColor("#000000"));
                        return true;
                    }
                }
                return true;
            } else {
                mileageErrorChange();
                return false;
            }
    }

    @SuppressLint("SetTextI18n")
    private void mileageErrorChange() {
        mileageTextLabel.setText("Actual mileage equal/lower than previous!");
        mileageTextLabel.setTextColor(Color.parseColor("#ff0000"));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getIntence() {
        autoData = (AutoData) Objects.requireNonNull(getIntent().getExtras()).getSerializable(MainMenuActivity.SPECIAL_DATA);
    }

    private Date getDate() {
        try {
            return dateFormat.parse(String.valueOf(dateEditText.getText()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dateFormat = DateFormat.getDateInstance();
        Date date = new Date();
        return date;
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
        return String.valueOf(Double.parseDouble(String.valueOf(costEditText.getText())) / Double.parseDouble(String.valueOf(litersEditText.getText())));
    }

    private String getCurrentDate() {
        dateFormat = DateFormat.getDateInstance();
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
        dateEditText.setText(dateFormat.format(calendar.getTime()));
    }
}
