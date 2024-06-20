package com.estudio.oss_dns_resolver_v1.no_di.network;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("deprecation")
public class HttpService extends AsyncTask<String, Void, String> {

    private final String TAG = "CustomHttpService";
    private final String ERROR_STR = "ERROR";

    private HttpServiceBuilder httpServiceBuilder;
    private ServiceCallBack serviceCallBack;
    public HttpService(
            HttpServiceBuilder httpServiceBuilder,
            ServiceCallBack serviceCallBack
    ) {
        this.httpServiceBuilder = httpServiceBuilder;
        this.serviceCallBack = serviceCallBack;

        execute();
    }

    @Override
    protected String doInBackground(String... strings) {
        UnsafeTrustManager.allowAllSSL();

        // Example : https://baidu.ac + /api/v1/login
        String urlString = httpServiceBuilder.getURL();
        String response;

        InputStream in = null;
        OutputStream out = null;

        try {

            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(httpServiceBuilder.getMethod());
            urlConnection.setReadTimeout(50000);

            // ADD HEADERS
            for (Map.Entry<String, String> entry : httpServiceBuilder.getHeaders().entrySet()) {
                urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }

            // LOGGER
            Log.d(TAG, "-- HTTP Service Start ---");
            Log.d(TAG, "URL : "+url);
            Log.d(TAG, "Headers: "+new Gson().toJson(httpServiceBuilder.getHeaders()));
            Log.d(TAG, "Parameters: "+new Gson().toJson(httpServiceBuilder.getParams()));
            //

            if(httpServiceBuilder.getMethod().equalsIgnoreCase("POST")){
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                out = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                writer.write(getPostDataString(httpServiceBuilder.getParams()));
                writer.flush();
                writer.close();
                out.close();
            }

            int responseCode = urlConnection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK || responseCode == 0){
                in = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                StringBuilder stringBuilder = new StringBuilder();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }

                bufferedReader.close();
                response = stringBuilder.toString();
            } else {

                // ERROR CODE CATCH
                Log.d(TAG, "Error Code: "+responseCode);
                response = ERROR_STR +" : "+responseCode;
                serviceCallBack.onFailure("Error Code: "+responseCode);
            }

        }catch (Exception e) {
            // CALL CATCH
            response = ERROR_STR +": "+e.getMessage();
            serviceCallBack.onFailure(e.getMessage());
            Log.d(TAG, "doInBackground: 1 "+e.getMessage());
            return response;
        } finally {
            try {
                if(in != null){
                    in.close();
                }
            }catch (Exception e){
                // CLOSING CATCH
                Log.d(TAG, "doInBackground: 2 "+e.getMessage());
                serviceCallBack.onFailure(e.getMessage());
            }
        }

        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(!s.contains(ERROR_STR)) {
            serviceCallBack.onResponse(s);
            Log.d(TAG, "Response "+s);
        }
    }

    // Helper method to encode POST data
    private String getPostDataString(HashMap<String, String> params) throws Exception {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}

