package com.nitish.nfcqrapp;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Nitish on 5/4/2018.
 */

public class Utility {

    private static String LOGGING_TAG = "Utility";

    public static String executePost(String targetURL, String urlParameters) throws Exception {
        Log.d(LOGGING_TAG, targetURL);
        Log.d(LOGGING_TAG, "executePost");
        String result = null;

        HttpURLConnection connection = null;
        try {
            // Create connection
            StringBuilder builder = new StringBuilder("nfcId=");
            Log.d(LOGGING_TAG, "target Url " + targetURL + "*****************");
            builder.append(urlParameters);
            Log.d(LOGGING_TAG, "url parameter" + urlParameters + "*****************");
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-Type",
//                    "application/x-www-form-urlencoded");
//            connection.setRequestProperty("Content-Type",
//                    "application/json");

            connection.setRequestProperty("Content-Length",
                    Integer.toString(builder.toString().getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setConnectTimeout(20000);
//            connection.setReadTimeout(waitingTime);
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            // Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
            writer.write(builder.toString());
            writer.close();
            wr.close();
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder response = new StringBuilder(); // or StringBuffer if
            // not Java 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            result = response.toString().trim();
            Log.d(LOGGING_TAG, "result = " + result);

        } catch (Exception e) {
//            e.printStackTrace();
//            addServerError(e, context, targetURL, urlParameters);
            Log.d(LOGGING_TAG, "executePost*****************Error");
            throw e;
//            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return result;
    }

    public static boolean downloadFile(String urlParameters, String targetURL, String fileName) {
        HttpURLConnection connection = null;
        try {
            // Create connection

            StringBuilder builder = new StringBuilder("jsonString=");
            builder.append(urlParameters);
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Length",
                    Integer.toString(builder.toString().getBytes().length));
            connection.setDoOutput(true);
            // Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(builder.toString());
            wr.close();

            // Get Response

            // connection.getResponseMessage().
            InputStream is = connection.getInputStream();
            String raw = connection.getHeaderField("Content-Disposition");
            Log.d(LOGGING_TAG, "resume  raw = " + raw);
            BufferedInputStream input = new BufferedInputStream(is);
            File f = new File(fileName);
            OutputStream output = new FileOutputStream(f);
            byte data[] = new byte[1024];

            int count = 0;

            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("", "Error while reading zip", e);
            return true;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return false;
    }

}
