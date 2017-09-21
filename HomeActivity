package com.kube.kubepuzzletimer.presentation;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kube.kubepuzzletimer.R;
import com.kube.kubepuzzletimer.business.SessionManager;
import com.kube.kubepuzzletimer.services.DataAccessService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class HomeActivity extends AppCompatActivity {

    public static SessionManager sManager;

    Button statsButton;
    Button timerButton;
    Button helpButton;
    Button sessionButton;
    Toast sessionToast;
    RelativeLayout rLayout;

    int toastDuration = Toast.LENGTH_SHORT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        copyDatabaseToDevice();
        DataAccessService.createDataAccess(DataAccessService.dbName);

        if (sManager == null) {
            sManager = new SessionManager();
        }

        rLayout = (RelativeLayout)findViewById(R.id.activity_home);
        rLayout.setBackgroundColor(Color.DKGRAY);

        statsButton = (Button)findViewById(R.id.stats_button);
        statsButton.setTextSize(22);

        timerButton = (Button)findViewById(R.id.timer_button);
        timerButton.setTextSize(24);

        helpButton = (Button) findViewById(R.id.help_button);
        helpButton.setTextSize(21);

        sessionButton = (Button)findViewById(R.id.session_button);
        sessionButton.setTextSize(19);

        sessionToast = Toast.makeText(getApplicationContext(), "New Session Created!", toastDuration);
    }

    protected void onDestroy() {
        super.onDestroy();
        DataAccessService.closeDataAccess();
    }

    private void copyDatabaseToDevice() {
        final String DB_PATH = "db";

        String[] assetNames;
        Context context = getApplicationContext();
        File dataDirectory = context.getDir(DB_PATH, Context.MODE_PRIVATE);
        AssetManager assetManager = getAssets();

        try {
            assetNames = assetManager.list(DB_PATH);
            for (int i = 0; i < assetNames.length; i++) {
                assetNames[i] = DB_PATH + "/" + assetNames[i];
            }

            copyAssetsToDirectory(assetNames, dataDirectory);
            DataAccessService.setDBPathName(dataDirectory.toString() + "/" + DataAccessService.dbName);
        } catch (IOException ioe) {
            ioe.getStackTrace();
        }
    }

    public void copyAssetsToDirectory(String[] assets, File directory) throws IOException {
        AssetManager assetManager = getAssets();

        for (String asset : assets) {
            String[] components = asset.split("/");
            String copyPath = directory.toString() + "/" + components[components.length - 1];
            char[] buffer = new char[1024];
            int count;

            File outFile = new File(copyPath);

            if (!outFile.exists()) {
                InputStreamReader in = new InputStreamReader(assetManager.open(asset));
                FileWriter out = new FileWriter(outFile);

                count = in.read(buffer);
                while (count != -1) {
                    out.write(buffer, 0, count);
                    count = in.read(buffer);
                }

                out.close();
                in.close();
            }
        }
    }

    public void buttonStatsOnClick(View v) {
        cancelToasts();
        Intent statsIntent = new Intent(HomeActivity.this, StatsActivity.class);
        HomeActivity.this.startActivity(statsIntent);
    }

    public void buttonTimerOnClick(View v) {
        cancelToasts();
        Intent timerIntent = new Intent(HomeActivity.this, TimerActivity.class);
        HomeActivity.this.startActivity(timerIntent);
    }

    public void buttonHelpOnClick(View v) {
        cancelToasts();
        Intent helpIntent = new Intent(HomeActivity.this, HelpActivity.class);
        HomeActivity.this.startActivity(helpIntent);
    }
  
    public void buttonSessionOnClick(View v) {
        sManager.endCurrentSession();
        sManager.newSession();
        sessionToast.show();
    }

    private void cancelToasts() {
        sessionToast.cancel();
    }
}
