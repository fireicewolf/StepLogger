package com.dukeg.steplogger;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.dukeg.steplogger.accessibilityService.StepLoggerAccessibilityService;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume(){
        Context context = getApplication().getApplicationContext();
        super.onResume();
        if(!StepLoggerAccessibilityService.isAccessibilitySettingsOn(context)){
            showOpenAccessibilityServiceDialog();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showOpenAccessibilityServiceDialog() {
        final AlertDialog.Builder openAccessibilityServiceDialog =
                new AlertDialog.Builder(MainActivity.this);
        openAccessibilityServiceDialog.setTitle(R.string.showOpenAccessibilityServiceDialogTitle);
        openAccessibilityServiceDialog.setMessage(R.string.showOpenAccessibilityServiceDialogMessage);
        openAccessibilityServiceDialog.setPositiveButton(R.string.buttonOk,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openAccessibilityServiceSettings();
                    }
                });
        openAccessibilityServiceDialog.setNegativeButton(R.string.ButtonCancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        openAccessibilityServiceDialog.show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder exitDialog = new AlertDialog.Builder(MainActivity.this);
        exitDialog.setMessage(R.string.exitDiagMessage);
        exitDialog.setPositiveButton(R.string.buttonOk,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        exitDialog.setNegativeButton(R.string.ButtonCancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        exitDialog.show();
    }

    private void openAccessibilityServiceSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
