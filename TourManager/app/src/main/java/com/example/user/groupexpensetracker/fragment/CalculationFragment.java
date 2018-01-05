package com.example.user.groupexpensetracker.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.groupexpensetracker.R;


public class CalculationFragment extends Fragment implements View.OnClickListener {
//    public CalculationFragment(){}
    Button one, two, three, four, five, six, seven, eight, nine, zero, plus, sub, mul, div, clear, equal,backspace, dot;
    EditText disp;
    int op1;
    int op2;
    double op3;
    double op4;
    String optr;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_calculation, container, false);

        one = (Button) v.findViewById(R.id.one);
        two = (Button) v.findViewById(R.id.two);
        three = (Button) v.findViewById(R.id.three);
        four = (Button) v.findViewById(R.id.four);
        five = (Button) v.findViewById(R.id.five);
        six = (Button)v.findViewById(R.id.six);
        seven = (Button) v.findViewById(R.id.seven);
        eight = (Button) v.findViewById(R.id.eight);
        nine = (Button)v.findViewById(R.id.nine);
        zero = (Button)v.findViewById(R.id.zero);
        plus = (Button) v.findViewById(R.id.plus);
        sub = (Button) v.findViewById(R.id.sub);
        mul = (Button) v.findViewById(R.id.mul);
        div = (Button) v.findViewById(R.id.div);
        clear = (Button) v.findViewById(R.id.clear);
        equal = (Button) v.findViewById(R.id.equal);
        dot=(Button)v.findViewById(R.id.dot);
        backspace=(Button)v.findViewById(R.id.backspace);


        disp = (EditText) v.findViewById(R.id.display);
        try
        {
            one.setOnClickListener(this);
            two.setOnClickListener(this);
            three.setOnClickListener(this);
            four.setOnClickListener(this);
            five.setOnClickListener(this);
            six.setOnClickListener(this);
            seven.setOnClickListener(this);
            eight.setOnClickListener(this);
            nine.setOnClickListener(this);
            zero.setOnClickListener(this);
            clear.setOnClickListener(this);
            plus.setOnClickListener(this);
            sub.setOnClickListener(this);
            mul.setOnClickListener(this);
            div.setOnClickListener(this);
            equal.setOnClickListener(this);
            dot.setOnClickListener(this);
            backspace.setOnClickListener(this);

        }
        catch(Exception e)
        {

        }
        return v;
    }
    public void operation()
    {

        if(optr.equals("+"))
        {
            op4 = Double.parseDouble(disp.getText().toString());
            disp.setText("");
            op3 = op3 + op4;
            disp.setText("Result : "+ Double.toString(op3));
        }

        else if(optr.equals("-"))
        {
            op4 = Double.parseDouble(disp.getText().toString());
            disp.setText("");
            op3 = op3 - op4;
            disp.setText("Result : " + Double.toString(op3));
        }
        else if(optr.equals("*"))
        {
            op4 = Double.parseDouble(disp.getText().toString());
            disp.setText("");
            op3 = op3 * op4;
            disp.setText("Result : " + Double.toString(op3)); }

        else if(optr.equals("/"))
        {
            op4 = Double.parseDouble(disp.getText().toString());
            disp.setText("");
            op3 = op3 / op4;
            disp.setText("Result : " + Double.toString(op3));
        }

    }
    @Override public void onClick(View arg0)
    {
        Editable str = disp.getText();
        switch(arg0.getId())
        {

            case R.id.zero:
                if (op2!=0)
                {
                    op2=0;
                    disp.setText("");
                }
                str=str.append(zero.getText());
                disp.setText(str);
                break;

            case R.id.one:
            if(op2 != 0)
            {
                op2 = 0;
                disp.setText("");
            }
            str = str.append(one.getText());
            disp.setText(str);
            break;

            case R.id.two:
                if(op2 != 0)
                {
                    op2 = 0; disp.setText("");
                }
                str = str.append(two.getText());
                disp.setText(str);
                break;

            case R.id.three:
                if(op2 != 0)
                {
                    op2 = 0;
                    disp.setText("");
                }
                str = str.append(three.getText());
                disp.setText(str);
                break;

            case R.id.four:
                if(op2 != 0)
                {
                    op2 = 0;
                    disp.setText("");
                }
                str = str.append(four.getText());
                disp.setText(str);
                break;

            case R.id.five:
                if(op2 != 0)
                {
                    op2 = 0;
                    disp.setText("");
                }
                str = str.append(five.getText());
                disp.setText(str);
                break;

            case R.id.six:
                if(op2 != 0)
                {
                    op2 = 0;
                    disp.setText("");
                }
                str = str.append(six.getText());
                disp.setText(str);
                break;

            case R.id.seven:
                if(op2 != 0)
                {
                    op2 = 0;
                    disp.setText("");
                }
                str = str.append(seven.getText());
                disp.setText(str);
                break;

            case R.id.eight:
                if(op2 != 0)
                {
                    op2 = 0;
                    disp.setText("");
                }
                str = str.append(eight.getText());
                disp.setText(str);
                break;

            case R.id.nine:
                if(op2 != 0)
                {
                    op2 = 0;
                    disp.setText("");
                }
                str = str.append(nine.getText());
                disp.setText(str);
                break;

            case R.id.clear:
                op1 = 0;
                op2 = 0;
                op3 = 0;
                op4 = 0;
                disp.setText("");
                break;

            case R.id.plus:
                optr = "+";
                if(op3 == 0)
                {
                    op3 = Double.parseDouble(disp.getText().toString());
                    disp.setText("");
                }
                else if
                        (op4 != 0)
                {
                    op4 = 0;
                    disp.setText("");
                }
                else
                {
                    op4 = Double.parseDouble(disp.getText().toString());
                    disp.setText("");
                    op3 = op3 + op4;
                    disp.setText("Result : "+ Double.toString(op3));
                }

                break;

            case R.id.sub:
                optr = "-";
                if(op3 == 0)
                {
                    op3 = Double.parseDouble(disp.getText().toString());
                    disp.setText("");
                }
                else if(op4 != 0)
                {
                    op4 = 0;
                    disp.setText("");
                }
                else
                {
                    op4 = Double.parseDouble(disp.getText().toString());
                    disp.setText("");
                    op3 = op3 - op4;
                    disp.setText("Result : " + Double.toString(op3));
                }

                break;

            case R.id.mul:
                optr = "*";
                if(op3 == 0)
                {
                    op3 = Double.parseDouble(disp.getText().toString());
                    disp.setText("");
                }
                else if(op4 != 0)
                {
                    op4 = 0;
                    disp.setText("");
                }
                else
                {
                    op4 = Double.parseDouble(disp.getText().toString());
                    disp.setText("");
                    op3 = op3 * op4;
                    disp.setText("Result : " + Double.toString(op3));
                }

                break;

            case R.id.div:
                optr = "/";
                if(op3 == 0)
                {
                    op3 = Double.parseDouble(disp.getText().toString());
                    disp.setText("");
                }
                else if(op4 != 0)
                {
                    op4 = 0;
                    disp.setText("");
                }
                else
                {
                    op4 = Double.parseDouble(disp.getText().toString());
                    disp.setText("");
                    op3 = op3 / op4;
                    disp.setText("Result : " + Double.toString(op3));
                }

                break;

            case R.id.equal:
                if(!optr.equals(null))
                {
                    if(op4 != 0)
                    {
                        if(optr.equals("+"))
                        {
                            disp.setText("");
                            /*op1 = op1 + op2;*/
                            disp.setText("Result : " + Double.toString(op3));
                        }
                        else if(optr.equals("-"))
                        {
                            disp.setText("");
                            /* op1 = op1 - op2;*/
                            disp.setText("Result : " + Double.toString(op3));
                        }
                        else if(optr.equals("*"))
                        {
                            disp.setText("");
                            /* op1 = op1 * op2;*/
                            //disp.setText("Result : " + Integer.toString(op1));
                            disp.setText("Result : " + Double.toString(op3));
                            }

                        else if(optr.equals("/"))
                        {
                            disp.setText("");
                            /* op1 = op1 / op2;*/
                            disp.setText("Result : " + Double.toString(op3));
                        }
                    }
                    else
                    {
                        operation();
                    }
                }

                break;

            case R.id.dot:
                if(op4 != 0)
                {
                    op4 = 0;
                    disp.setText("");
                }
                str = str.append(dot.getText());
                disp.setText(str);
                break;

            case R.id.backspace:
                if(op4 != 0)
                {
                    op4 = 0;
                    disp.setText("");
                }
                 String textInBox = disp.getText().toString();

                if(textInBox.length() > 0)
                {
                    String newText = textInBox.substring(0, textInBox.length()-1);
                     disp.setText(newText);
                }
                break;
        }
    }
}

