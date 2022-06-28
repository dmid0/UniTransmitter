package com.example.universaltransmitter2;

import android.app.Activity;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Objects;

class Http implements Runnable {

    HttpURLConnection connection = null;

    private final String scanUrl;
    private final String scanUser;
    private final String scanPass;
    private final String httpname;
    private final String encodedFile;
    private final String httpGET;
    private final Activity httpFile;

    public Http(String scanUrl, String scanUser, String scanPass, String httpname, String encodedFile, String httpGET, Activity httpFile) {
        this.scanUrl = scanUrl;
        this.scanUser = scanUser;
        this.scanPass = scanPass;
        this.httpname = httpname;
        this.encodedFile = encodedFile;
        this.httpGET = httpGET;
        this.httpFile = httpFile;

    }

    public String getBase64() {
        String res = scanUser + ":" + scanPass;
        return Base64.getEncoder().encodeToString(res.getBytes());
    }

    @Override
    public void run() {
        try {
            String res = getBase64();
            connection = (HttpURLConnection) new URL(scanUrl).openConnection();
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            if (Objects.equals(httpGET, "GET")){
                connection.setRequestMethod("GET");
            } else {
                connection.setRequestMethod("POST");
            }
            connection.setRequestProperty("Authorization", "Basic " + res);
            try (DataOutputStream write = new DataOutputStream(connection.getOutputStream())) {
                write.writeBytes(encodedFile);
                write.writeBytes("&");
                write.writeBytes(httpname);
                write.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            int status =  connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    StringBuilder response = new StringBuilder();
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                }
                connection.disconnect();
                httpFile.runOnUiThread(() -> Toast.makeText(httpFile, "Данные отправлены!", Toast.LENGTH_SHORT).show());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
