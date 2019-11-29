package com.budget101;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.budget101.Data.Category;
import com.budget101.Data.Enum.Money;
import com.budget101.Data.Record;
import com.budget101.Database.DatabaseAccess;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class Report extends MainActivity
{
    DatabaseAccess access;
    PieChartView chart;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        this.access = super.getDatabase();
        this.chart = findViewById(R.id.pie);
    }


    public void run(View view)
    {
        final int month = 10;
        final int year = 2019;

        Record[] rec = this.access.getRecordByDate(month, year);
        Category[] cat = this.access.getAllCategories();
        double[] sums = fillSums(rec, cat);
        fillChart(sums, cat);
    }


    private void fillChart(double[] sum, Category[] cat)
    {
        List<SliceValue> data = new ArrayList<>(7);
        int[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.YELLOW, Color.CYAN};

        for(int i = 0; i < sum.length; i++)
        {
            if(sum[i] > 0)
            {
                data.add(new SliceValue((float) sum[i], colors[i % colors.length]).setLabel(cat[i].getName()));
            }
        }


        PieChartData chartData = new PieChartData(data);
        chartData.setHasLabels(true);
        this.chart.setPieChartData(chartData);
    }


    private double[] fillSums(Record[] r,  Category[] c)
    {
        if(r == null || c == null)
            return null;

        double[] sum = new double[c.length]; // Match categories

        for(int i = 0; i < sum.length; i++)
        {
            if(c[i].getMoney() == Money.EXPENSE) // Only include expense categories
            {
                for (int k = 0; k < r.length; k++)
                {
                    if (c[i].getID() == r[k].getCategory().getID())
                        sum[i] += r[k].getAmount();
                }
            }
        }

        return sum;
    }
}
