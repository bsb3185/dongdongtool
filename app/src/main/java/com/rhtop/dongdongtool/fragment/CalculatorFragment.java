package com.rhtop.dongdongtool.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;

import com.rhtop.dongdongtool.Bean.CalculatorCounts;
import com.rhtop.dongdongtool.R;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * Created by chenxin on 2017/2/6.
 * email :chenxin@rhtop.cn
 */

public class CalculatorFragment extends Fragment {
    private static final String TAG = "chenxin";
    private EditText print;

    private static String fistNumber = "0";// 第一次输入的值
    private static String secondNumber = "0";// 第二次输入的值
    private static String num = "0";// 显示的结果
    private static int flg = 0;// 结果累加一次
    public CalculatorCounts take = null;

    private int[] btidTake = { R.id.txtdivide, R.id.txtx, R.id.txtmin,
            R.id.txttakesum };

    private Button[] buttonTake = new Button[btidTake.length];

    private int[] btidNum = { R.id.txt0, R.id.txt1, R.id.txt2, R.id.txt3,
            R.id.txt4, R.id.txt5, R.id.txt6, R.id.txt7, R.id.txt8, R.id.txt9,
            R.id.txtspl };
    private Button[] buttons = new Button[btidNum.length];

    private int[] btcl = { R.id.chars, R.id.charx, R.id.txtb, R.id.txtv };
    private Button[] btcls = new Button[btcl.length];
    private GridLayout gly;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "CalculatorFragment-----onCreateView");
        View view = inflater.inflate(R.layout.caculator_fragment, container, false);
        gly=(GridLayout)view.findViewById(R.id.gld);
        print = (EditText) view.findViewById(R.id.print);
        print.setText("0");
        print.setEnabled(false);
        GetNumber get = new GetNumber();
        for (int i = 0; i < btidNum.length; i++) {
            buttons[i] = (Button)view.findViewById(btidNum[i]);
            buttons[i].setOnClickListener(get);
        }
        Compute cm = new Compute();
        for (int i = 0; i < btidTake.length; i++) {
            buttonTake[i] = (Button) view.findViewById(btidTake[i]);
            buttonTake[i].setOnClickListener(cm);
        }

        Button eq = (Button) view.findViewById(R.id.txteq);

        eq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flg == 0) {
                    secondNumber = print.getText().toString();
                    if (take == CalculatorCounts.DIVIDE && secondNumber.equals("0")) {
                        print.setText("0不能为被除数");
                    } else {
                        num = take.Values(fistNumber, secondNumber);
                        fistNumber = num;
                        secondNumber = "0";
                        print.setText(num);
                        flg = 1;
                        gly.setBackgroundResource(R.drawable.jz);
                    }
                }
            }
        });
        Button cleargo = (Button) view.findViewById(R.id.cleargo);
        cleargo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (num.length() > 1) {
                    num = num.substring(0, num.length() - 1);
                } else {
                    num = "0";
                }
                print.setText(num);
            }
        });
        Button clear = (Button) view.findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                num = "0";
                fistNumber = secondNumber = num;
                print.setText(num);
                flg = 0;
            }
        });
        for (int i = 0; i < btcl.length; i++) {
            btcls[i] = (Button) view.findViewById(btcl[i]);
            btcls[i].setOnClickListener(new OnTake());
        }


        return view;

    }

    // 给 EditText赋值
    class GetNumber implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (flg == 1)
                num = "0";
            if (num.equals("0")) {
                print.setText("");
                num = v.getId() == R.id.txtspl ? "0" : "";
            }
            String txt = ((Button) v).getText().toString();
            boolean s = Pattern.matches("-*(\\d+).?(\\d)*", num + txt);
            num = s ? (num + txt) : num;
            gly.setBackgroundResource(R.drawable.js);
            print.setText(num);
        }
    }

    // 根据条件计算
    class Compute implements View.OnClickListener {

        @Override
        public void onClick(View arg0) {

            fistNumber = print.getText().toString();
            // TODO Auto-generated method stub
            switch (arg0.getId()) {
                case R.id.txttakesum:
                    take = CalculatorCounts.ADD;
                    break;
                case R.id.txtmin:
                    take = CalculatorCounts.MINUS;
                    break;
                case R.id.txtx:
                    take = CalculatorCounts.MULTIPLY;
                    break;
                case R.id.txtdivide:
                    take = CalculatorCounts.DIVIDE;
                    break;
            }
            num = "0";
            flg = 0;
            gly.setBackgroundResource(R.drawable.js);
        }

    }

    class OnTake implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.chars:
                    num = "-" + num;
                    break;
                case R.id.charx:
                    if(!num.equals("0"))
                        num = BigDecimal.valueOf(1).divide(new BigDecimal(num),12,BigDecimal.ROUND_UP).stripTrailingZeros()
                                .toString();
                    break;
                case R.id.txtb:
                    num = new BigDecimal(num).divide(BigDecimal.valueOf(100),12,BigDecimal.ROUND_UP).stripTrailingZeros()
                            .toString();
                    break;
                case R.id.txtv:
                    Double numss = Math.sqrt(new BigDecimal(num).doubleValue());
                    int stratindex=numss.toString().contains(".")?numss.toString().indexOf("."):0;
                    num = numss.toString().length()>13?numss.toString().substring(0, 12+stratindex):numss.toString();
            }
            print.setText(num);
            flg=0;
            num = "0";

        }

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

}
