package com.CMPUT301W24T32.brazmascheckin.views;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.GetSuccessListener;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
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

/**
 * Author: Easy One Coders
 * Title: Android Charts | Bar Chart | MP Android Chart | Android Studio 2023
 * Link: https://www.youtube.com/watch?v=WdsmQ3Zyn84
 * Date: April 1, 2024
 */

/**
 * Author: OpenAI
 * Tool: ChatGPT
 * Prompt: "Use mockito to create integration tests using FirestoreDB.java and
 * EventController.java"
 * Date: March 30, 2024
 */

/**
 * Author: PhilJay
 * Title: MPAndroidChart
 * Link: https://github.com/PhilJay/MPAndroidChart
 * Date: April 1, 2024
 */

/**
 * This class displays the event analytics for the organizer's events through bar chart.
 */
public class GraphAnalyticsActivity extends AppCompatActivity {

    private List<String> xValues = Arrays.asList("Checked In", "Signed Up");
    private EventController eventController;

    /**
     * Initializes the activity and sets up the event analytics bar chart
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analytics_graph);

        // Retrieve the event object passed to the fragment
        Event oldEvent = (Event) getIntent().getSerializableExtra("event");
        eventController = new EventController(FirestoreDB.getDatabaseInstance());

        assert oldEvent != null;
        String eventID = oldEvent.getID();
        eventController.getEvent(eventID, event -> {
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

            Log.d(TAG, "Checked In: " + checkedIn);
            Log.d(TAG, "Signed Up: " + signedUp);

            // Find the maximum value among checked-in and signed-up users
            int maxValue = Math.max(checkedIn, signedUp);

            // Configure YAxis
            // Configure YAxis
            YAxis yAxis = barChart.getAxisLeft();
            yAxis.setAxisMinimum(0f);
            yAxis.setAxisMaximum(maxValue + 1); // Adjust padding for better visualization
            yAxis.setAxisLineWidth(2f);
            yAxis.setAxisLineColor(Color.BLACK);
            yAxis.setGranularity(1f); // Set granularity to 1 to force whole number values
            yAxis.setGranularityEnabled(true);
            yAxis.setLabelCount(maxValue + 2, true); // Set label count to maxValue + 2 to include 0 and maxValue

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
        }, e -> {
            Toast.makeText(this, "Unable to retrieve event analytics", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
