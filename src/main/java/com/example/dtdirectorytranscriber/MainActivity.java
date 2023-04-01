package com.example.dtdirectorytranscriber;

import android.app.Activity;
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
}