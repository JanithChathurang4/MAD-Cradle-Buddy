package com.example.infantcareaidingmobileapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.infantcareaidingmobileapplication.Adapter.DataAdapter;
import com.example.infantcareaidingmobileapplication.Adapter.NewFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class FeedingsActivity extends AppCompatActivity {

    ArrayList<Feeding> feedings = new ArrayList<>();
    RecyclerView recyclerView;
    LineChart lineChart;
    LineData data;
    LineDataSet dataSet;
    ArrayList lineEntries;
    ArrayList<Date> dateList = new ArrayList<>();
    ArrayList<Double> dataList = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("#.#");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedings);

        String feedJson = getIntent().getStringExtra("feedings");

        final Gson gson = new Gson();
        feedings = gson.fromJson(feedJson,new TypeToken<ArrayList<Feeding>>(){}.getType());
        System.out.println(feedJson);
        recyclerView = findViewById(R.id.data_table);
        getData();

    }

    private void setUpChart() {
        lineChart = findViewById(R.id.chart);
        for(int i = 0; i < dateList.size(); i++){
            System.out.println(i);
            System.out.println(dateList.get(i).getTime());
            System.out.println(String.valueOf(dataList.get(i).floatValue()));
            lineEntries.add(new Entry(dateList.get(i).getTime(), dataList.get(i).floatValue()));
        }
        dataSet = new LineDataSet(lineEntries, "");
        data = new LineData(dataSet);
        lineChart.setData(data);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(18f);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new NewFormatter());
    }

    private void getData() {

        lineEntries = new ArrayList<>();
        for (Feeding feeding : feedings) {
            DateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
            try {
                Date date = format.parse(feeding.getDate());
                if(dateList.contains(date)){
                    int index = dateList.indexOf(date);
                    double value = dataList.get(index)+(Math.round(feeding.getPeriod())/60*10)/10;
                    dataList.set(index,value);
                }else{
                    dateList.add(date);
                    dataList.add((double)Math.round(feeding.getPeriod()/60*10)/10);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Collections.reverse(dateList);
        Collections.reverse(dataList);
        setUpRecycler(feedings);
        setUpChart();

    }

    private void setUpRecycler(ArrayList<Feeding> feedings) {
        Collections.reverse(feedings);
        DataAdapter myAdapter = new DataAdapter(FeedingsActivity.this,feedings);
        recyclerView.setLayoutManager(new LinearLayoutManager(FeedingsActivity.this));
        recyclerView.setAdapter(myAdapter);
    }

}