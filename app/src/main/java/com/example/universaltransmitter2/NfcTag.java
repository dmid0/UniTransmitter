package com.example.universaltransmitter2;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;
import java.util.StringTokenizer;

public class NfcTag extends AppCompatActivity {

    Context context;

    String scanMAIN = "";
    String scanProtocol = "";
    String scanURL = "";
    String scanUSER = "";
    String scanPASS = "";
    String httpname = "";
    String httpGET = "";
    String variable_name = "";
    String variable_file = "";
    String encodedFile = "";
    String NFC = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc_activity);
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
            scanner.next();
            scanURL = scanner.next();
            scanUSER = scanner.next();
            scanPASS = scanner.next();
            httpGET = scanner.next();
            variable_name = scanner.next();
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
            StringTokenizer tokenProt = new StringTokenizer(httpGET, "=");
            if (tokenProt.countTokens()==2) {
                tokenProt.nextToken();
                httpGET = tokenProt.nextToken();
                System.out.println(httpGET);
            }
            StringTokenizer tokenName = new StringTokenizer(variable_name, "=");
            if (tokenName.countTokens()==2) {
                tokenName.nextToken();
                variable_name = tokenName.nextToken();
                System.out.println(variable_name);
            }
            Files.write(path, new String(Files.readAllBytes(path), charset).replace
                    (scanMAIN, "main=NFC").getBytes(charset));
        } catch (IOException e) {
            e.printStackTrace();
        }
        context = NfcTag.this;
        NFC = NfcAdapter.EXTRA_TAG;
    }

    public Activity getActivity(Context context)
    {
        if (context instanceof Activity) return (Activity) context;
        else return getActivity(((ContextWrapper) context).getBaseContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        NFC = NfcAdapter.EXTRA_TAG;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
            Tag tag = intent.getParcelableExtra(NFC);
            if (tag != null) {
                Toast.makeText(this, "Тег NFC обнаружен", Toast.LENGTH_SHORT).show();
                StringBuilder tagInfo = new StringBuilder();
                byte[] tagID = tag.getId();
                for (byte b : tagID) {
                    tagInfo.append(String.format("%02X", b));
                }
                NFC = String.valueOf(tagInfo);
                httpname = variable_name + "=" + NFC;
                encodedFile = variable_file + "=" + encodedFile;
                if (Objects.equals(scanProtocol, "HTTPS")) {
                    Https anotherRun = new Https(scanURL, scanUSER, scanPASS, httpname, encodedFile, httpGET, getActivity(context));
                    Thread childTread = new Thread(anotherRun);
                    childTread.start();
                } else {
                    Http anotherRun = new Http(scanURL, scanUSER, scanPASS, httpname, encodedFile, httpGET, getActivity(context));
                    Thread childTread = new Thread(anotherRun);
                    childTread.start();
                }
                Intent intent1 = new Intent(NfcTag.this, MainActivity.class);
                startActivity(intent1);
            }
    }
}