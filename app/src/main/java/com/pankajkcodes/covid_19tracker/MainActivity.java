package com.pankajkcodes.covid_19tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pankajkcodes.covid_19tracker.api.ApiUtilities;
import com.pankajkcodes.covid_19tracker.api.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextView totalConfirm, todayConfirm, totalActive, totalRecovered, todayRecovered;
    private TextView totalDeath, todayDeath, totalTests, updateTime;

    private List<CountryData> list;
    PieChart mPieChart;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        findViewById(R.id.country_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(MainActivity.this,AllCountryActivity.class));
            }
        });


        list = new ArrayList<>();
        dialog.show();
        try {
        ApiUtilities.getApiInterface().getCountryData()
                .enqueue(new Callback<List<CountryData>>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                        list.addAll(response.body());

                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getCountry().equals("India")) {
                                int confirm = Integer.parseInt(list.get(i).getCases());
                                int active = Integer.parseInt(list.get(i).getActive());
                                int recovered = Integer.parseInt(list.get(i).getRecovered());
                                int death = Integer.parseInt(list.get(i).getDeaths());

                                totalConfirm.setText(NumberFormat.getInstance().format(confirm));
                                totalActive.setText(NumberFormat.getInstance().format(active));
                                totalRecovered.setText(NumberFormat.getInstance().format(recovered));
                                totalDeath.setText(NumberFormat.getInstance().format(death));


                                todayConfirm.setText("(+"+NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayCases()))+")");
                                todayRecovered.setText("(+"+NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayRecovered()))+")");
                                todayDeath.setText("(+"+NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayDeaths()))+")");
                                totalTests.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTests())));


                                setDate(list.get(i).getUpdated());

                                mPieChart.addPieSlice(new PieModel("Confirm", confirm, getResources().getColor(R.color.confirmColor)));
                                mPieChart.addPieSlice(new PieModel("Active", active, getResources().getColor(R.color.activeColor)));
                                mPieChart.addPieSlice(new PieModel("Recovered", recovered, getResources().getColor(R.color.recoveredColor)));
                                mPieChart.addPieSlice(new PieModel("Death", death, getResources().getColor(R.color.deathColor)));
                       mPieChart.startAnimation();
                            }
                        }
                        dialog.dismiss();
                    }


                    @Override
                    public void onFailure(Call<List<CountryData>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void setDate(String updated) {
        @SuppressLint("SimpleDateFormat")
        DateFormat format = new SimpleDateFormat("dd MMM yyyy");
        long millisecond = Long.parseLong(updated);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisecond);

        updateTime.setText("Updated at " + format.format(calendar.getTime()));
    }

    private void init() {
        totalConfirm = findViewById(R.id.total_confirm);
        todayConfirm = findViewById(R.id.today_confirm);
        totalActive = findViewById(R.id.total_active);
        totalRecovered = findViewById(R.id.total_recovered);
        todayRecovered = findViewById(R.id.today_recovered);
        totalDeath = findViewById(R.id.total_death);
        todayDeath = findViewById(R.id.today_death);
        totalTests = findViewById(R.id.total_tests);

        mPieChart = (PieChart) findViewById(R.id.pieChart);
        updateTime = findViewById(R.id.update_time);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");


    }
}