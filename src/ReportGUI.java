package com.budget101;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.budget101.Data.Category;
import com.budget101.Data.Enum.Money;
import com.budget101.Data.Record;
import com.budget101.Database.DatabaseAccess;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import java.util.TimeZone;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class ReportGUI extends MainActivity {
    DatabaseAccess access;
    PieChartView chart;
    double[] totals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_gui);

        this.access = super.getDatabase();
        this.chart = findViewById(R.id.pie);
    }


    public void run(View view) {
        Calendar m = Calendar.getInstance(TimeZone.getTimeZone("America/Dallas"));
        int month = m.get(Calendar.MONTH);
        int year = m.get(Calendar.YEAR);
        Record[] rec = this.access.getRecordByDate(month, year);
        Category[] cat = this.access.getAllCategories();
        totals = findTotals(rec);
        double[] sums = fillSums(rec, cat);
        fillChart(sums, cat);
        TextView textView1 = (TextView) findViewById(R.id.textIncomesVar);
        textView1.setText(Double.toString(totals[1]));
        TextView textView2 = (TextView) findViewById(R.id.textExpenseVar);
        textView2.setText(Double.toString(totals[0]));
    }


    private void fillChart(double[] sum, Category[] cat) {
        List<SliceValue> data = new ArrayList<>(7);
        int[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.YELLOW, Color.CYAN};

        for (int i = 0; i < sum.length; i++) {
            if (sum[i] > 0) {
                data.add(new SliceValue((float) sum[i], colors[i % colors.length]).setLabel(cat[i].getName() + ": $" + sum[i]));
            }
        }


        PieChartData chartData = new PieChartData(data);
        chartData.setHasLabels(true);
        this.chart.setPieChartData(chartData);
    }


    private double[] fillSums(Record[] r, Category[] c) {
        if (r == null || c == null)
            return null;

        double[] sum = new double[c.length]; // Match categories

        for (int i = 0; i < sum.length; i++) {
            if (c[i].getMoney() == Money.EXPENSE) // Only include expense categories
            {
                for (int k = 0; k < r.length; k++) {
                    if (c[i].getID() == r[k].getCategory().getID())
                        sum[i] += r[k].getAmount();
                }
            }
        }

        return sum;
    }

    private double[] findTotals(Record[] r)
    {
        if (r == null )
            return null;

        double[] sum = new double[2];
        double expenseTotal = 0;
        double incomeTotal = 0;
        Category cat;

        for (int i = 0; i < r.length; i++)
        {
            cat = r[i].getCategory();
            if (cat.getMoney() == Money.EXPENSE)
            {
                double catExp = r[i].getAmount();
                expenseTotal = expenseTotal + catExp;
            }
            else if (cat.getMoney() == Money.INCOME)
            {
                double catInc = r[i].getAmount();
                incomeTotal = incomeTotal + catInc;
            }
        }

        sum[0] = expenseTotal;
        sum[1] = incomeTotal;

        return sum;
    }
}
