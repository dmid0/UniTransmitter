package com.example.universaltransmitter2;

import android.os.Environment;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.util.Date;
import java.util.Objects;

public class HistoryConfig extends AppCompatActivity {

    String history = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_config);
        TextView text = findViewById(R.id.his);
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/UniTransmitter/SaveConfig/History");
        File[] files = directory.listFiles();
        for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
            Date lastModDate = new Date(files[i].lastModified());
            history += "N: " + files[i].getName() + "; TD: " + lastModDate + ";\n";
        }
        text.setText(history);
    }
}