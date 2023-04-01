package com.example.dtdirectorytranscriber;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.dtdirectorytranscriber.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends Activity {

    private EditText directoryEditText;
    private Button grabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if the app has permission to access the external storage
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                // If not, request the permission from the user
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        }


        directoryEditText = findViewById(R.id.editTextDirectory);
        grabButton = findViewById(R.id.buttonGrab);

        grabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String directoryPath = directoryEditText.getText().toString();

                if (directoryPath.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a directory path", Toast.LENGTH_SHORT).show();
                    return;
                }

                File directory = new File(directoryPath);

                if (!directory.isDirectory()) {
                    Toast.makeText(MainActivity.this, "Please enter a valid directory path", Toast.LENGTH_SHORT).show();
                    return;
                }

                File[] files = directory.listFiles();

                if (files == null) {
                    Toast.makeText(MainActivity.this, "Error reading files in directory", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    File outputFile = new File(getExternalFilesDir(null), "file_list.csv");
                    FileWriter writer = new FileWriter(outputFile);

                    for (File file : files) {
                        writer.write(file.getName() + "\n");
                    }

                    writer.close();

                    Toast.makeText(MainActivity.this, "File names saved to " + outputFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error writing file names to output file", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If the user grants the permission, proceed with the app's normal operation
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
                }
                // If the user denies the permission, display a message and do not proceed with the app's operation
                else {
                    Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

}