package com.example.universaltransmitter2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Config extends AppCompatActivity {

    private final static String FILENAME = "/storage/emulated/0/Documents/UniTransmitter/setting.conf";
    EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingconfig);
        mEditText = findViewById(R.id.editText);
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(FILENAME), StandardCharsets.UTF_8));
            String curLine;
            while ((curLine = bufferedReader.readLine()) != null) {
                builder.append(curLine);
                builder.append("\n");
            }
            mEditText.setText(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            SaveFile();
            return true;
        }
        return true;
    }

    private void SaveFile() {
        String filenameExternal = "setting.conf";
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/UniTransmitter/");
        File fileT = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/UniTransmitter/SaveConfig/History");
        try {
            if (!file.exists()) file.mkdirs();
            File fileCon = new File(file, filenameExternal);
            fileCon.createNewFile();
            FileOutputStream fileSave = new FileOutputStream(fileCon);
            OutputStreamWriter outputStream = new OutputStreamWriter(fileSave);
            outputStream.write(mEditText.getText().toString());
            outputStream.close();
            Toast.makeText(Config.this, "Конфигурационный файл - сохранен!", Toast.LENGTH_LONG).show();
            if (!fileT.exists()) fileT.mkdirs();
            File fileConT = new File(fileT, String.valueOf(System.currentTimeMillis()));
            fileConT.createNewFile();
            FileOutputStream fileSaveT = new FileOutputStream(fileConT);
            OutputStreamWriter outputStreamT = new OutputStreamWriter(fileSaveT);
            outputStreamT.write(mEditText.getText().toString());
            outputStreamT.close();
            Intent intent=new Intent(Config.this, MainActivity.class);
            startActivity(intent);
        } catch (Throwable t) {
            Toast.makeText(Config.this, "Ошибка: " + t, Toast.LENGTH_LONG).show();
        }
    }
}