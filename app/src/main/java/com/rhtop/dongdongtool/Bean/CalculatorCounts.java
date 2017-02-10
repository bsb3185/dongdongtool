package com.rhtop.dongdongtool.Bean;

import java.math.BigDecimal;

/**
 * Created by cx on 2017/2/7.
 * email :chenxin@rhtop.cn
 */

public enum CalculatorCounts {
    ADD, MINUS, MULTIPLY, DIVIDE, MARK, ROOT;
    public String Values(String num1, String num2) {
        BigDecimal number1 = new BigDecimal(num1);
        BigDecimal number2 = new BigDecimal(num2);
        BigDecimal number = BigDecimal.valueOf(0);
        switch (this) {
            case ADD:
                number = number1.add(number2);
                break;
            case MINUS:
                number = number1.subtract(number2);
                break;
            case MULTIPLY:
                number = number1.multiply(number2);
                break;
            case DIVIDE:
                number = number1.divide(number2,20,BigDecimal.ROUND_UP);
                break;

        }
        return number.stripTrailingZeros().toString();

    }

}