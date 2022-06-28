package com.example.universaltransmitter2;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.documentfile.provider.DocumentFile;
import com.example.universaltransmitter2.ui.file.FilleUpFragment;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;
import java.util.Scanner;
import java.util.StringTokenizer;

public class FilleUp extends AppCompatActivity {

    Uri uri;
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
    String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fille_up_activity);
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

            for(int i=13; i>0; i--) scanner.next();
            scanURL = scanner.next();
            scanUSER = scanner.next();
            scanPASS = scanner.next();
            variable_name = scanner.next();
            variable_file = scanner.next();

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
            StringTokenizer tokenName = new StringTokenizer(variable_name, "=");
            if (tokenName.countTokens()==2) {
                tokenName.nextToken();
                variable_name = tokenName.nextToken();
                System.out.println(variable_name);
            }
            StringTokenizer tokenFile = new StringTokenizer(variable_file, "=");
            if (tokenFile.countTokens()==2) {
                tokenFile.nextToken();
                variable_file = tokenFile.nextToken();
                System.out.println(variable_file);
            }
            Files.write(path, new String(Files.readAllBytes(path), charset).replace(scanMAIN, "main=File").getBytes(charset));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, FilleUpFragment.newInstance()).commitNow();
        }
        Intent FileIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        FileIntent.setType("*/*");
        startActivityForResult(FileIntent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                DocumentFile documentFile = DocumentFile.fromSingleUri(this, data.getData());
                name = documentFile.getName();
                uri = data.getData();
                Context context = FilleUp.this;
                String filePath = FileUtils.getPath(context,uri);
                File file = new File(filePath);
                byte[] fileContent;
                try {
                    fileContent = Files.readAllBytes(file.toPath());
                    encodedFile = Base64.getUrlEncoder().encodeToString(fileContent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                httpname = variable_name + "=" + name;
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
                Intent intent = new Intent(FilleUp.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    public Activity getActivity(Context context)
    {
        if (context instanceof Activity) return (Activity) context;
        else return getActivity(((ContextWrapper) context).getBaseContext());
    }
}