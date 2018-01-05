//package com.example.user.groupexpensetracker.fragment;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.Typeface;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.text.SpannableString;
//import android.text.style.ForegroundColorSpan;
//import android.text.style.RelativeSizeSpan;
//import android.text.style.StyleSpan;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.SeekBar;
//
//import com.example.user.groupexpensetracker.R;
//import com.example.user.groupexpensetracker.activity.PieChartActivity;
//import com.github.mikephil.charting.animation.Easing;
//import com.github.mikephil.charting.charts.PieChart;
//import com.github.mikephil.charting.components.Legend;
//import com.github.mikephil.charting.data.Entry;
//import com.github.mikephil.charting.data.PieData;
//import com.github.mikephil.charting.data.PieDataSet;
//import com.github.mikephil.charting.data.PieEntry;
//import com.github.mikephil.charting.formatter.PercentFormatter;
//import com.github.mikephil.charting.highlight.Highlight;
//import android.widget.SeekBar.OnSeekBarChangeListener;
//import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
//import com.github.mikephil.charting.utils.ColorTemplate;
//
//import java.util.ArrayList;
//
//
//public class StatisticsFragment extends Fragment implements OnChartValueSelectedListener {
//    private PieChart mChart;
//
//
//    public StatisticsFragment() {
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//
//        final View v = inflater.inflate(R.layout.fragment_statistics, container, false);
//        mChart = (PieChart) v.findViewById(R.id.chart1);
//        mChart.setUsePercentValues(true);
//        mChart.setDescription("abcd text");
//        mChart.setExtraOffsets(5, 10, 5, 5);
//
//        mChart.setDragDecelerationFrictionCoef(0.95f);
//        mChart.setCenterText("Trip Expenses");
//
//
//        //mChart.setCenterTextTypeface(mTfLight);
//        //mChart.setCenterText(generateCenterSpannableText());
//
//        mChart.setDrawHoleEnabled(true);
//        mChart.setHoleColor(Color.WHITE);
//
//        mChart.setTransparentCircleColor(Color.WHITE);
//        mChart.setTransparentCircleAlpha(110);
//
//        mChart.setHoleRadius(58f);
//        mChart.setTransparentCircleRadius(61f);
//
//        mChart.setDrawCenterText(true);
//
//        mChart.setRotationAngle(0);
//        // enable rotation of the chart by touch
//        mChart.setRotationEnabled(true);
//        mChart.setHighlightPerTapEnabled(true);
//
//        // mChart.setUnit(" €");
//        // mChart.setDrawUnitsInChart(true);
//
//        // add a selection listener
//        mChart.setOnChartValueSelectedListener(this);
//
//        setData(4, 100);
//
//        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
//        // mChart.spin(2000, 0, 360);
//
//
//        Legend l = mChart.getLegend();
//        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
//        l.setXEntrySpace(7f);
//        l.setYEntrySpace(0f);
//        l.setYOffset(0f);
//
//        // entry label styling
//        mChart.setEntryLabelColor(Color.WHITE);
////        mChart.setEntryLabelTypeface(mTfRegular);
//        mChart.setEntryLabelTextSize(12f);
//
//        return v;
//    }
//
//    /*@Override
//    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//    }*/
//
//    private void setData(int count, float range) {
//
//        float mult = range;
//
//        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
//
//        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
//        // the chart.
//
//        PieEntry pieEntry=new PieEntry(0.5f,"breakfast");
//        entries.add(pieEntry);
//
//        pieEntry=new PieEntry(0.5f,"lunch");
//        entries.add(pieEntry);
//
//        pieEntry=new PieEntry(0.5f,"dinner");
//        entries.add(pieEntry);
//
//        pieEntry=new PieEntry(0.5f,"entertainment");
//        entries.add(pieEntry);
//        for (int i = 0; i < count ; i++) {
//            //entries.add(new PieEntry(20.0f));
//        }
//
//        PieDataSet dataSet = new PieDataSet(entries, "Trip Activities");
//        dataSet.setSliceSpace(3f);
//        dataSet.setSelectionShift(50f);
//
//        // add a lot of colors
//
//        ArrayList<Integer> colors = new ArrayList<Integer>();
//
//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);
//
//        colors.add(ColorTemplate.getHoloBlue());
//
//        dataSet.setColors(colors);
//        //dataSet.setSelectionShift(0f);
//
//        PieData data = new PieData(dataSet);
//        data.setValueFormatter(new PercentFormatter());
//        data.setValueTextSize(11f);
//        data.setValueTextColor(Color.WHITE);
////        data.setValueTypeface(mTfLight);
//        mChart.setData(data);
//
//        // undo all highlights
//        mChart.highlightValues(null);
//
//        mChart.invalidate();
//    }
//
//    private SpannableString generateCenterSpannableText() {
//
//        SpannableString s = new SpannableString("Trip   Expenses");
//        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
//        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
//        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
//        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
//        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
//        return s;
//    }
//
//
//
//
//
//    @Override
//    public void onValueSelected(Entry e, Highlight h) {
//        if (e == null)
//            return;
//        Log.i("VAL SELECTED",
//                "Value: " + e.getY() + ", index: " + h.getX()
//                        + ", DataSet index: " + h.getDataSetIndex());
//    }
//
//    @Override
//    public void onNothingSelected() {
//        Log.i("PieChart", "nothing selected");
//
//    }
//}






















package com.example.user.groupexpensetracker.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.user.groupexpensetracker.R;

import org.w3c.dom.Text;

public class StatisticsFragment extends Fragment {

    private String fromCurrency;
    private String toCurrency;
    EditText txtRate, txtAmount, txtfrom;
    TextView txtcalculated;
    Double rate,amount,calculated_amount;
    Spinner spFrom, spTo;
    Button bt_show;
    LinearLayout linear;
    //    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_statistics, container, false);
        txtRate = (EditText) v.findViewById(R.id.txt_rate);
        txtAmount = (EditText) v.findViewById(R.id.txt_fromamount);
        txtcalculated=(TextView)v.findViewById(R.id.txtcalculated);
        spFrom = (Spinner) v.findViewById(R.id.fromcurrency_spin);
        spTo = (Spinner) v.findViewById(R.id.tocurrency_spin);
        linear=(LinearLayout)v.findViewById(R.id.linearlayout2);

        txtfrom = (EditText) v.findViewById(R.id.txt_fromamount);
        bt_show=(Button)v.findViewById(R.id.bt_show);

        bt_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//            Intent intent=new Intent(getActivity(), ConversionTable.class);
//            intent.putExtra("fromCurrency", fromCurrency);
//            intent.putExtra("toCurrency", toCurrency);
//            intent.putExtra("Rate", txtRate.getText().toString());
//            intent.putExtra("fromAmount",txtAmount.getText().toString());
//            startActivity(intent);
                linear.setVisibility(View.VISIBLE);
                rate=Double.parseDouble(txtRate.getText().toString());
                amount=Double.parseDouble(txtAmount.getText().toString());
                calculated_amount=rate*amount;

                txtcalculated.setText("The calculated amount is:" +calculated_amount);
            }
        });

        return v;



    }

//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }



    public void onResume(){
        super.onResume();
        setUpSpinnerData();

    }

    //This method will be invoked to setup data of the spinner views
    //to show lists of currency types for selection
    public void setUpSpinnerData(){
        String[] currencyList={"AUD","CAD","CHF","EUR","GBP","JPY","NZD","KHR","USD","CNY","THB","INR"};
        ArrayAdapter<String> afrom=new ArrayAdapter<String>(getActivity(),R.layout.spinner_style,currencyList);
        spFrom.setAdapter(afrom);
        spFrom.setOnItemSelectedListener(new ItemSelectedFrom());
        ArrayAdapter<String> ato=new ArrayAdapter<String>(getActivity(),R.layout.spinner_style,currencyList);
        spTo.setAdapter(ato);
        spTo.setOnItemSelectedListener(new ItemSelectedTo());

    }



    private class ItemSelectedFrom implements AdapterView.OnItemSelectedListener {
        public void onNothingSelected(AdapterView<?> av){

        }
        public void onItemSelected(AdapterView<?> av, View view, int position, long id){
            TextView sel=(TextView)view;
            String from=sel.getText().toString();
            fromCurrency=from; //capture the currency of the From side

            txtfrom.setHint("Enter "+fromCurrency+" amount");

        }
    }

    private class ItemSelectedTo implements AdapterView.OnItemSelectedListener {
        public void onNothingSelected(AdapterView<?> av){

        }
        public void onItemSelected(AdapterView<?> av, View view, int position, long id){
            TextView sel=(TextView)view;
            String to=sel.getText().toString();
            toCurrency=to; //capture the currency of the To side


        }
    }


//    public void showResult(){
//             if(txtRate.getText().toString().length()<=0 || txtAmount.getText().toString().length()<=0){
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            builder.setMessage("Please input value in text box.");
//            builder.setCancelable(true);
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
//                public void onClick(DialogInterface di, int which){
//                    di.cancel();
//                }
//            });
//            AlertDialog dialog = builder.create();
//            dialog.show();
//
//        }
//        else{
//            //create intent, place data in it and start the ConversionTable activity
////            Intent intent=new Intent(getActivity(), ConversionTable.class);
////            intent.putExtra("fromCurrency", fromCurrency);
////            intent.putExtra("toCurrency", toCurrency);
////            intent.putExtra("Rate", txtRate.getText().toString());
////            intent.putExtra("fromAmount", txtAmount.getText().toString());
////            startActivity(intent);
//        }
//    }


}

