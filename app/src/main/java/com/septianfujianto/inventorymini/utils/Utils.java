package com.septianfujianto.inventorymini.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.septianfujianto.inventorymini.App;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.attr.format;

/**
 * Created by Septian A. Fujianto on 1/31/2017.
 */

public class Utils {
    public static String formatCurrency(Double money, String symbol, char groupingSep, char decimalSep) {
        NumberFormat currencyInstance = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol(symbol);
        dfs.setGroupingSeparator(groupingSep);
        dfs.setMonetaryDecimalSeparator(decimalSep);
        ((DecimalFormat) currencyInstance).setDecimalFormatSymbols(dfs);
        currencyInstance.setMaximumFractionDigits(0);


        return currencyInstance.format(money);
    }

    public static Double sumList(List<Double> list){
        if(list == null || list.size()<1)
            return Double.valueOf(0);

        Double sum = Double.valueOf(0);
        for(Double i: list)
            sum = sum + i;

        return sum;
    }

    // validating email id
    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    // validating password with retype password
    public static boolean isFormFilled(String input) {
        if (input != null && input.length() > 2) {
            return true;
        }

        return false;
    }

    public static boolean isValidSpinner(Spinner spinner){
        String selectedView = (String) spinner.getSelectedItem();
        if (selectedView != null) {
            return true;
        }

        return false;
    }

    public static void createSimpleList(Context context, LinearLayout layout, List<String> listContent, int rowFontSize) {
        for (int i = 0; i < listContent.size(); i++) {
            // create a new textview
            final TextView rowTextView = new TextView(context);

            // set some properties of rowTextView or something
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                rowTextView.setText(Html.fromHtml(listContent.get(i), Html.FROM_HTML_MODE_LEGACY));
            } else {
                rowTextView.setText(Html.fromHtml(listContent.get(i)));
            }

            rowTextView.setTextSize(rowFontSize);
            rowTextView.setX(10);
            rowTextView.setPadding(0, 0, 0, 10);

            // add the textview to the linearlayout
            layout.addView(rowTextView);
        }
    }

    public static Intent shareUrl(String url) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);

        return shareIntent;
    }

    public static String getTodayDate(String dateFormat){
        if (TextUtils.equals(dateFormat, "")) {
            dateFormat = "yyyy-MM-dd'T'hh:mm:ss";
        }

        String dateToday = (String) android.text.format.DateFormat.format(dateFormat, new Date());

        return dateToday;
    }

    public static Spanned printhtmlText(String htmlText) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY);

        } else {
            return Html.fromHtml(htmlText);
        }
    }
}
