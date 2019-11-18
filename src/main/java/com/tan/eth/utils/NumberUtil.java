package com.tan.eth.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author by Tan
 * @create 2019/11/18/018
 */
public class NumberUtil {


    public static String removeZero(String s) {

        if(s.indexOf(".") > 0){

            //正则表达
            s = s.replaceAll("0+?$", "");//去掉后面无用的零
            s = s.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点

        }
        return s;
    }
}
