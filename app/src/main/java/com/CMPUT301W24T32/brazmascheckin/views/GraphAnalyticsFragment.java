package com.CMPUT301W24T32.brazmascheckin.views;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GraphAnalyticsFragment extends AppCompatActivity {

    private List<String> xValues = Arrays.asList("Checked In", "Signed Up");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_graph);

        // Retrieve the event object passed to the fragment
        Event event = (Event) getIntent().getSerializableExtra("event");

        // Get the number of checked-in users from the event object
        int checkedIn = event.helperCount();

        // Get the number of signed-up users from the event object
        int signedUp = event.getSignUps().size();

        // Initialize the BarChart
        BarChart barChart = findViewById(R.id.bar_chart);
        barChart.getAxisRight().setDrawLabels(false);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, checkedIn)); // checked in
        entries.add(new BarEntry(1, signedUp)); // signed up

        // Configure YAxis
        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(Math.max(checkedIn, signedUp) + 10); // Adding some padding for better visualization
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setLabelCount(10);

        // Configure BarDataSet
        BarDataSet dataSet = new BarDataSet(entries, "Event Analytics");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        // Configure BarData
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        // Configure chart appearance
        barChart.getDescription().setEnabled(false);
        barChart.invalidate();

        // Configure XAxis
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setGranularityEnabled(true);
    }
}
