package com.ensiie.yuheng.moviie.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by solael on 2017/1/23.
 */

public class DateUtil {

    public static String toFRDateString (String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormatFR = new SimpleDateFormat("dd MMMM yyyy");

        try {
            String dateStringFR = simpleDateFormatFR.format(simpleDateFormat.parse(str));
            return dateStringFR;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
