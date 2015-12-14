package com.michael.androidiotest;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startTest();
            }
        });
    }

    private static final int COUNT = 100000;
    void startTest()
    {
        writeTest();

        readTest();
    }

    void writeTest()
    {
        try {
            LinkedList<Byte> numbers = new LinkedList<>();

            FileOutputStream outputStream = openFileOutput("TestIO.txt", MODE_PRIVATE);
            Random random = new Random(System.currentTimeMillis());

            // gen 100000 numbers
            for (int i = 0; i < COUNT; i++)
            {
                byte[] bytes = new byte[1];
                random.nextBytes(bytes);
                numbers.add(bytes[0]);
            }

            long start = System.currentTimeMillis();
            /// use java write data
            for (Byte item : numbers)
            {
                outputStream.write(item);
            }
            outputStream.flush();
            outputStream.close();

            Log.d(getClass().getName(), "Java write " + COUNT + "Bytes spend: " + (System.currentTimeMillis() - start)/1000.0 + " seconds");

            IOTest.writeTest(getFileStreamPath("TestIO.txt").getAbsolutePath(), COUNT);

            // read test


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void readTest()
    {
        try {
            FileInputStream inputStream = openFileInput("TestIO.txt");

            long start = System.currentTimeMillis();
            /// use java write data
            for (int i = 0; i < COUNT; i++)
            {
                inputStream.read();
            }

            Log.d(getClass().getName(), "Java read " + COUNT + "Bytes spend: " + (System.currentTimeMillis() - start)/1000.0 + " seconds");

            IOTest.readTest(getFileStreamPath("TestIO.txt").getAbsolutePath(), COUNT);

            // read test


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
