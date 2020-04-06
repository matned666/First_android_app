package com.workspace.carnote;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.workspace.carnote.model.AutoData;
import com.workspace.carnote.model.GsonQuest;
import com.workspace.carnote.model.Record;
import com.workspace.carnote.model.RecordType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class CostRaportActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView carBrandTextView;
    private TextView carModelTextView;
    private TextView carColorTextView;
    private TextView carPlatesTextView;
    private TextView totalCostsTextView;
    private TextView averageCostTextView;
    private TextView chosenMonthCost;
    private TextView chosenDayCost;
    private TextView carAverageFuelConsumptionTextView;

    private EditText dateMonthEditText;

    private Spinner autoChooseSpinner;
    private ArrayList<AutoData> cars;
    private ArrayAdapter spinnerAdapter;

    private DateFormat dateFormat1;
    private Date currentDate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cost_raport_layout);
        initView();
        getIntence();
        initArrayAdapter();
        Button goToDetailedRaportFormButton = findViewById(R.id.goToDetailedRaportButton);
        goToDetailedRaportFormButton.setOnClickListener(v -> {
            Intent intent = new Intent(CostRaportActivity.this, DetailedCostRaport.class);
            startActivity(intent);
        });
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cars);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayCosts();
        getMonthCosts();
        getAverageFuelConsumptionTextView();
    }

    private void getIntence() {
        cars = GsonQuest.getList((String) Objects.requireNonNull(getIntent().getExtras()).get(MainMenuActivity.GIVE_CARS_LIST));
      }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initView() {
        carBrandTextView = findViewById(R.id.carBrandTextView);
        carModelTextView = findViewById(R.id.carModelTextView);
        carColorTextView = findViewById(R.id.carColorTextView);
        carPlatesTextView = findViewById(R.id.carPlatesTextView);
        totalCostsTextView = findViewById(R.id.carTotalCostsTextView);
        carAverageFuelConsumptionTextView = findViewById(R.id.carAverageFuelConsumptionTextView);
        averageCostTextView = findViewById(R.id.carMonthlyCostsTextView);
        chosenMonthCost = findViewById(R.id.carChosenMonthCostsTextView);
        chosenDayCost = findViewById(R.id.carChosenDayCostsTextView);
        dateMonthEditText = findViewById(R.id.dateMonthPicker);
        autoChooseSpinner = findViewById(R.id.raport_auto_choose_spinner);

        dateMonthEditText.setOnClickListener(v -> datePickDialog());
        dateMonthEditText.setRawInputType(InputType.TYPE_NULL);
        dateMonthEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) datePickDialog();
        });

        dateMonthEditText.setText(getCurrentDate());
    }

    private void initArrayAdapter() {
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cars);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoChooseSpinner.setAdapter(spinnerAdapter);
        autoChooseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                initializeData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initializeData() {
        getCarData();
        if (getCurrentCar().getRecords().size() > 0) {
            getTotalCosts();
            getAverageMonthlyCost();
        }
    }

    @SuppressLint("SetTextI18n")
    private void getCarData() {
        carBrandTextView.setText(String.valueOf(getCurrentCar().getBrand()));
        carModelTextView.setText(String.valueOf(getCurrentCar().getModel()));
        carColorTextView.setText(String.valueOf(getCurrentCar().getColor()));
        carPlatesTextView.setText(String.valueOf(getCurrentCar().getPlates()));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private void getTotalCosts() {
        int result = getTotalCostInner();
        totalCostsTextView.setText(result + " PLN");
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private void getAverageMonthlyCost() {
        double totalCost = getTotalCostInner();
        double months = getMonthlyCostsList().size();
        int averageCosts;
        if (months != 0) averageCosts = (int) (totalCost / months);
        else averageCosts = 0;
        averageCostTextView.setText(averageCosts + " PLN");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private int getTotalCostInner() {
        int result = 0;
        for (Record el : getCurrentCar().getRecords()) {
            result += el.getCostInPLN();
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List getMonthlyCostsList() {
        int actualMonth = getCurrentCar().getRecords().get(0).getDate().getMonth();
        int result = 0;
        List monthlyCostsList = new LinkedList();
        for (int i = 0; i < getCurrentCar().getRecords().size(); i++) {
            Record currentRecord = getCurrentCar().getRecords().get(i);
            if (currentRecord.getDate().getMonth() == actualMonth) {
                result += currentRecord.getCostInPLN();
                if (i == getCurrentCar().getRecords().size() - 1) monthlyCostsList.add(result);

            } else {
                monthlyCostsList.add(result);
                result = currentRecord.getCostInPLN();
                actualMonth = currentRecord.getDate().getMonth();
            }
        }
        if (monthlyCostsList.size() == 0) monthlyCostsList.add(result);
        return monthlyCostsList;
    }

    private AutoData getCurrentCar() {
        return (AutoData) autoChooseSpinner.getSelectedItem();
    }

    private String getCurrentDate() {
        dateFormat1 = DateFormat.getDateInstance();
        Date date = new Date();
        currentDate = new Date(System.currentTimeMillis());
        return dateFormat1.format(date);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private void getAverageFuelConsumptionTextView(){
        int consumption;
        Record firstTankUp = getFirstTankUpRecord();
        Record lastTankUp = getLastTankUpRecord();
        int totalTankUp = getTotalTankUp();
        if (firstTankUp != null && lastTankUp != null && totalTankUp > 0) {
            double kmTotallyDone = lastTankUp.getMileage() - firstTankUp.getMileage();
            if(kmTotallyDone > 0) {
                consumption = (int) (totalTankUp / kmTotallyDone * 100);
                carAverageFuelConsumptionTextView.setText(consumption + " l/100km");
                carAverageFuelConsumptionTextView.setTextColor(Color.parseColor("#000000"));
            }
        }else{
            carAverageFuelConsumptionTextView.setText("Not enough records!");
            carAverageFuelConsumptionTextView.setTextColor(Color.parseColor("#ff0000"));
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private int getTotalTankUp() {
       return getCurrentCar().getRecords()
               .stream()
               .filter(x -> x.getRecordType() == RecordType.TANK_UP)
               .mapToInt(Record::getTankedUpGasLiters)
               .sum();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Record getFirstTankUpRecord() {
        List<Record> tankUpRecords = getCurrentCar().getRecords();
        for (int i = tankUpRecords.size()-1; i>=0 ; i--){
            if(tankUpRecords.get(i).getRecordType() == RecordType.TANK_UP)
                return tankUpRecords.get(i);
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Record getLastTankUpRecord() {
        List<Record> tankUpRecords = getCurrentCar().getRecords();
        for (int i = 0; i < tankUpRecords.size()-1 ; i++){
            if(tankUpRecords.get(i).getRecordType() == RecordType.TANK_UP)
                return tankUpRecords.get(i);
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar1 = new GregorianCalendar(year, month, dayOfMonth);
        currentDate = calendar1.getTime();
        dateMonthEditText.setText(dateFormat1.format(calendar1.getTime()));
        dayCosts();
        getMonthCosts();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private void getMonthCosts() {
        int result = 0;
        for (Record el : getCurrentCar().getRecords()) {
            if (getDateStr(el.getDate(), "MM").equals(getDateStr(currentDate, "MM"))) {
                result += el.getCostInPLN();
            }
        }
        chosenMonthCost.setText(result + " PLN");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private void dayCosts() {
        int result = 0;
        for (Record el : getCurrentCar().getRecords()) {
            if (getDateStr(el.getDate(), "dd/MM/YYYY").equals(getDateStr(currentDate, "dd/MM/YYYY"))) {
                result += el.getCostInPLN();
            }
        }
        chosenDayCost.setText(result + " PLN");
    }

    public static String getDateStr(Date date, String format) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    private void datePickDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(CostRaportActivity.this, CostRaportActivity.this, year, month, day);
        datePicker.show();
    }

}