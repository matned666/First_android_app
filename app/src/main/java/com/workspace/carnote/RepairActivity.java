package com.workspace.carnote;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.workspace.carnote.model.Record;
import com.workspace.carnote.model.RecordType;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RepairActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String AUTO_DATA = "AUTO_DATA";

    private Button confirmButton;
    private EditText dateEditText;
    private EditText costEditText;
    private EditText descriptionEditText;
    private TextView descriptionLabel;
    private TextView costLabel;
    private TextView dateLabel;
    private DateFormat dateFormat;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repair_layout);
        initView();
    }

    private void initView() {
        confirmButton = findViewById(R.id.repair_confirm);
        dateEditText = findViewById(R.id.repair_date);
        costEditText = findViewById(R.id.repair_cost);
        descriptionEditText = findViewById(R.id.repair_description);
        dateLabel = findViewById(R.id.repair_date_label);
        costLabel = findViewById(R.id.repair_cost_label);
        descriptionLabel = findViewById(R.id.repair_description_label);

        dateEditText.setOnClickListener(v -> datePickDialog());
        dateEditText.setText(getCurrentDate());
        addOnClickListener_ToConfirmButton();
        addOnFocusChangeListenerTo_Cost_TextField();
        addOnFocusChangeListenerTo_Description_TextField();

    }

    private void addOnClickListener_ToConfirmButton() {
        confirmButton.setOnClickListener(v -> {

            if (textValidate(descriptionEditText, descriptionLabel, getResources().getString(R.string.description))
                    && numberValidate(costEditText, costLabel,getResources().getString(R.string.cost))) {
                Record repair = new Record.Builder(RecordType.REPAIR, getDate())
                        .costInPLN(getCost())
                        .description(getDescription())
                        .mileage(0)
                        .tankedUpGasLiters(0)
                        .build();
                Intent intent = new Intent();
                intent.putExtra(AUTO_DATA, repair);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    private void addOnFocusChangeListenerTo_Cost_TextField() {
        costEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                textValidate(descriptionEditText, descriptionLabel, getResources().getString(R.string.description));
            }
        });
    }

    private void addOnFocusChangeListenerTo_Description_TextField() {
        descriptionEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                numberValidate(costEditText, costLabel,getResources().getString(R.string.cost));
            }
        });
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

    private String getDescription() {
        return String.valueOf(descriptionEditText.getText());
    }


    private void datePickDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(RepairActivity.this, (DatePickerDialog.OnDateSetListener) RepairActivity.this, year,month,day);
        datePicker.show();
    }

    private String getCurrentDate() {
        dateFormat = DateFormat.getDateInstance();
        Date date = new Date();
        return dateFormat.format(date);
    }

    @SuppressLint("SetTextI18n")
    private boolean numberValidate(EditText editText, TextView textView, String backText) {
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

    @SuppressLint("SetTextI18n")
    private boolean textValidate(EditText editText, TextView textView, String backText) {
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
        dateEditText.setText(dateFormat.format(calendar.getTime()));
    }
}
