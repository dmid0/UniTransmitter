package com.example.universaltransmitter2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Environment;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Download extends AppCompatActivity {

    ProgressDialog progress;
    Context context;

    String scanMAIN = "";
    String scanProtocol = "";
    String scanURL = "";
    String scanUSER = "";
    String scanPASS = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_activity);
        String fileNew = "/storage/emulated/0/Documents/UniTransmitter/setting.conf";
        Path path = Paths.get(fileNew);
        Charset charset = StandardCharsets.UTF_8;
        try {
            Scanner scanner = new Scanner(path);
            scanner.useDelimiter("\n");
            System.out.println(scanner.next());
            scanMAIN = scanner.next();

            scanProtocol = scanner.next();
            StringTokenizer tokenProtocol = new StringTokenizer(scanProtocol, "=");
            tokenProtocol.nextToken();
            scanProtocol = tokenProtocol.nextToken();
            System.out.println(scanProtocol);

            for(int i=19; i>0; i--) scanner.next();

            scanURL = scanner.next();
            scanUSER = scanner.next();
            scanPASS = scanner.next();

            StringTokenizer tokenURL = new StringTokenizer(scanURL, "=");
            if (tokenURL.countTokens()==2) {
                tokenURL.nextToken();
                scanURL = tokenURL.nextToken();
                System.out.println(scanURL);
            }
            StringTokenizer tokenUSER = new StringTokenizer(scanUSER, "=");
            if (tokenUSER.countTokens()==2) {
                tokenUSER.nextToken();
                scanUSER = tokenUSER.nextToken();
                System.out.println(scanUSER);
            }
            StringTokenizer tokenPASS = new StringTokenizer(scanPASS, "=");
            if (tokenPASS.countTokens()==2) {
                tokenPASS.nextToken();
                scanPASS = tokenPASS.nextToken();
                System.out.println(scanPASS);
            }
            Files.write(path, new String(Files.readAllBytes(path), charset).replace(scanMAIN, "main=Download").getBytes(charset));
        } catch (IOException e) {
            e.printStackTrace();
        }
        context = Download.this;
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/UniTransmitter/File");
        if (!file.exists()) file.mkdirs();
        StatDownload();
        DownloadHttp anotherRun = new DownloadHttp(scanURL, scanUSER, scanPASS, file, progress, getActivity(context));
        Thread childTread = new Thread(anotherRun);
        childTread.start();

    }

    private void StatDownload(){
        progress = new ProgressDialog(this);
        progress.setMessage("Загрузка файла...");
        progress.setCancelable(false);
        progress.setMax(100);
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setButton ("Закрыть", (dialog, whichButton) -> {
            Intent intent = new Intent(Download.this, MainActivity.class);
            startActivity(intent);
        });
        progress.show();
    }

    public Activity getActivity(Context context)
    {
        if (context instanceof Activity) return (Activity) context;
        else return getActivity(((ContextWrapper) context).getBaseContext());
    }

}

class DownloadHttp implements Runnable {

    HttpURLConnection connection = null;

    private final String scanUrl;
    private final String scanUser;
    private final String scanPass;
    private final File file;
    private final ProgressDialog progress;
    private final Activity download;


    public DownloadHttp(String scanUrl, String scanUser, String scanPass, File file, ProgressDialog progress, Activity download) {
        this.scanUrl = scanUrl;
        this.scanUser = scanUser;
        this.scanPass = scanPass;
        this.file = file;
        this.progress = progress;
        this.download = download;
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
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Basic " + res);
            int fileLen = connection.getContentLength();
            String filePathUrl = connection.getURL().getFile();
            String fileName = filePathUrl.substring(filePathUrl.lastIndexOf(File.separator) + 1);
            File fileDownload = new File(file, fileName);
            fileDownload.createNewFile();
            BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileDownload));
            int prog = 0;
            int result = bis.read();
            while(result != -1) {
                bos.write((byte) result);
                buf.write((byte) result);
                result = bis.read();
                prog++;
                progress.setProgress(prog * 100 / fileLen);
            }
            bos.flush();
            bos.close();

            // System.out.println("Server: " + connection.getResponseCode());

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
                download.runOnUiThread(() -> Toast.makeText(download, "Файл загружен!", Toast.LENGTH_SHORT).show());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}