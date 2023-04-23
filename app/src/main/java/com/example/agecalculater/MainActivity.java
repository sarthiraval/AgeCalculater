package com.example.agecalculater;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    TextView textViewNextBirthdayMonths;
    TextView textViewNextBirthdayDays;
    TextView textViewFinalYears;
    TextView textViewFinalMonths;
    TextView textViewFinalDays;
    TextView textViewCurrentDay;
    TextView textViewCalculate;
    TextView textViewClear,about;


    ImageView imageViewCalenderFirst;
    ImageView imageViewCalenderSecond;

    EditText editTextBirthDay;
    EditText editTextBirthMonth;
    EditText editTextBirthYear;
    EditText editTextCurrentDay;
    EditText editTextCurrentMonth;
    EditText editTextCurrentYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        about = findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ine = new Intent(MainActivity.this,About.class);
                startActivity(ine);
            }
        });
        textViewNextBirthdayMonths = findViewById(R.id.textViewNextBirthdayMonths);
        textViewNextBirthdayDays = findViewById(R.id.textViewNextBirthdayDays);
        textViewFinalYears = findViewById(R.id.textViewFinalYears);
        textViewFinalMonths = findViewById(R.id.textViewFinalMonths);
        textViewFinalDays = findViewById(R.id.textViewFinalDays);
        textViewCurrentDay = findViewById(R.id.textViewCurrentDay);
        textViewClear = findViewById(R.id.textViewClear);
        textViewCalculate = findViewById(R.id.textViewCalculate);
        imageViewCalenderFirst = findViewById(R.id.imageViewCalenderFirst);
        imageViewCalenderSecond = findViewById(R.id.imageViewCalenderSecond);
        editTextBirthDay = findViewById(R.id.editTextBirthDay);
        editTextBirthYear = findViewById(R.id.editTextBirthYear);
        editTextBirthMonth = findViewById(R.id.editTextBirthMonth);
        editTextCurrentDay = findViewById(R.id.editTextCurrentDay);
        editTextCurrentYear = findViewById(R.id.editTextCurrentYear);
        editTextCurrentMonth = findViewById(R.id.editTextCurrentMonth);

        textViewCalculate.setOnClickListener(this);
        textViewClear.setOnClickListener(this);
        imageViewCalenderSecond.setOnClickListener(this);
        imageViewCalenderFirst.setOnClickListener(this);

        final Calendar c = Calendar.getInstance();
        editTextCurrentYear.setText(String.valueOf(c.get(Calendar.YEAR)));
        editTextCurrentMonth.setText(addZero(c.get(Calendar.MONTH) + 1));
        editTextCurrentDay.setText(addZero(c.get(Calendar.DAY_OF_MONTH)));

        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
        Date date = new Date(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH) - 1);
        String dayOfWeek = simpledateformat.format(date);
        textViewCurrentDay.setText(dayOfWeek);
        textViewCurrentDay.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    private String addZero(int number) {
        String n;
        if (number < 10) {
            n = "0" + number;
        } else {
            n = String.valueOf(number);
        }
        return n;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imageViewCalenderSecond) {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {

                    editTextBirthDay.setText(addZero(dayOfMonth));
                    editTextBirthMonth.setText(addZero(monthOfYear + 1));
                    editTextBirthYear.setText(String.valueOf(year));
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        } else if (view == textViewCalculate) {
            if (!TextUtils.isEmpty(editTextBirthDay.getText()) && !TextUtils.isEmpty(editTextBirthMonth.getText()) && !TextUtils.isEmpty(editTextBirthYear.getText())) {
                calculateAge();
                nextBirthday();
            } else {
                Toasty.warning(MainActivity.this, "All fields are required", Toast.LENGTH_SHORT, true).show();
            }
        } else if (view == textViewClear) {
            editTextBirthDay.setText("");
            editTextBirthMonth.setText("");
            editTextBirthYear.setText("");
            Toasty.success(MainActivity.this, "Successfully reset", Toast.LENGTH_SHORT, true).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.about) {
            startActivity(new Intent(this, About.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void nextBirthday() {
        int currentDay = Integer.parseInt(editTextCurrentDay.getText().toString());
        int currentMonth = Integer.parseInt(editTextCurrentMonth.getText().toString());
        int currentYear = Integer.parseInt(editTextCurrentYear.getText().toString());

        Calendar current = Calendar.getInstance();
        current.set(currentYear, currentMonth, currentDay);

        int birthDay = Integer.parseInt(editTextBirthDay.getText().toString());
        int birthMonth = Integer.parseInt(editTextBirthMonth.getText().toString());
        int birthYear = Integer.parseInt(editTextBirthYear.getText().toString());

        Calendar birthday = Calendar.getInstance();
        birthday.set(birthYear, birthMonth, birthDay);

        long difference = birthday.getTimeInMillis() - current.getTimeInMillis();

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(difference);

        textViewNextBirthdayMonths.setText(String.valueOf(cal.get(Calendar.MONTH)));
        textViewNextBirthdayDays.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
    }

    private void calculateAge() {
        int currentDay = Integer.parseInt(editTextCurrentDay.getText().toString());
        int currentMonth = Integer.parseInt(editTextCurrentMonth.getText().toString());
        int currentYear = Integer.parseInt(editTextCurrentYear.getText().toString());

        Date now = new Date(currentYear, currentMonth, currentDay);

        int birthDay = Integer.parseInt(editTextBirthDay.getText().toString());
        int birthMonth = Integer.parseInt(editTextBirthMonth.getText().toString());
        int birthYear = Integer.parseInt(editTextBirthYear.getText().toString());

        Date dob = new Date(birthYear, birthMonth, birthDay);

        if (dob.after(now)) {
            Toasty.error(MainActivity.this, "Birthday can't in future", Toast.LENGTH_SHORT, true).show();
            return;
        }
        int month[] = {31, 28, 31, 30, 31, 30, 31,
                31, 30, 31, 30, 31};

        if (birthDay > currentDay) {
            currentDay = currentDay + month[birthMonth - 1];
            currentMonth = currentMonth - 1;
        }

        if (birthMonth > currentMonth) {
            currentYear = currentYear - 1;
            currentMonth = currentMonth + 12;
        }

        int calculated_date = currentDay - birthDay;
        int calculated_month = currentMonth - birthMonth;
        int calculated_year = currentYear - birthYear;

        textViewFinalDays.setText(String.valueOf(calculated_date));
        textViewFinalMonths.setText(String.valueOf(calculated_month));
        textViewFinalYears.setText(String.valueOf(calculated_year));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}