package com.esq.androidpdfreader;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements FragmentReplaceListener{

    private static final int PICK_PDF_CODE = 1000;
    public static final int READ_PDF_CODE = 2000;
    //Set bottomAppBar
    @BindView(R.id.bottom_app_bar)
    BottomAppBar bottomAppBar;

    @BindView(R.id.fragment_container_home)
    FrameLayout frameLayout;

   final SpeechFragment speechFragment = new SpeechFragment();
   final SettingsFragment  settingsFragment = new SettingsFragment();
    ReadPdfFragment readPdfFragment = new ReadPdfFragment();
   ReadPdfActivity readPdfActivity = new ReadPdfActivity();
   final HomeActivityFragment homeActivityFragment = new HomeActivityFragment();
   final FragmentManager fragmentManager = getSupportFragmentManager();
   //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

   Fragment active = homeActivityFragment;

    private static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String TAG = "HomeActivity";
    private static boolean readPDFPermissionsGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        ButterKnife.bind(this);
        getReadPDFPermission();
        setUpBottomAppBar();
        setUpFab();
        readPdfFragment = new ReadPdfFragment();
        fragmentManager.beginTransaction().add(R.id.fragment_container_home, speechFragment, "Speech").hide(speechFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_container_home, settingsFragment, "Settings").hide(settingsFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_container_home, homeActivityFragment, "Home").commit();

    }

    //Set up bottom bar
    private void setUpBottomAppBar() {
        //Set bottom bar to Action bar as it is similar like toolbar
        setSupportActionBar(bottomAppBar);
        //Click event over Bottom bar navigation item
        bottomAppBar.setNavigationOnClickListener(v -> {
            setFragment(homeActivityFragment);
            active = homeActivityFragment;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null) {
            if (readPDFPermissionsGranted) {
                Log.i(TAG, "onActivityResult: Permissions has been granted");
                Uri selectedPDF = data.getData();
                Intent intent = new Intent(HomeActivity.this, ReadPdfActivity.class);
                intent.putExtra("ViewType", "storage");
                intent.putExtra("FileUri", selectedPDF.toString());
                //start activity silently
                startActivityForResult(intent, READ_PDF_CODE);
                //startActivity(intent);
                Log.i(TAG, "onActivityResult: startActivityForResult() called");
            } else {
                Toast.makeText(HomeActivity.this, "Allow all permissions to read PDF", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == READ_PDF_CODE && resultCode == RESULT_OK && data != null) {
            Log.i(TAG, "onActivityResult: result gotten from intent, readPdfActivity.readPdfFromDevice() is called");

            //ReadPdfActivity was destroyed so mainIntent is now null ... Set it to the intent in SetResult()
            readPdfActivity.mainIntent = getIntent();
            if(readPdfActivity.mainIntent != null){
                Log.i(TAG,  "onActivityResult: MainIntent has been initialized" );
            }else{
                Log.i(TAG, "onActivityResult: MainIntent is null" );
            }
            readPdfActivity.readPdfFromDevice();
            //Change fragment to pdfView fragment
           // readPdfFragment = readPdfActivity.readPdfFragment;
            //Log.i(TAG, "readPdfFromDevice: readPdfFragment is " + readPdfFragment);
            setFragment(readPdfFragment);
        }

    }

    //Set up fab
    private void setUpFab() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Log.i(TAG, "setUpFab: FAB is clicked");
            Intent PDFIntent = new Intent(Intent.ACTION_GET_CONTENT);
            PDFIntent.setType("application/pdf"); //Set type of data
            PDFIntent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(PDFIntent, "Select Your PDF"), PICK_PDF_CODE);
            Log.i(TAG, "setUpFab: startActivityForResult method is called");
        });
    }

    //Get permission to read PDF in the user's device
    private void getReadPDFPermission() {
        Log.i(TAG, "getReadPDFPermission: getting PDF permissions");
        String[] permissions = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                readPDFPermissionsGranted = true;
                Log.i(TAG, "getReadPDFPermission: PDF permissions has been granted");
            } else {
                Log.i(TAG, "getReadPDFPermission: PDF permissions has not been granted");
                ActivityCompat.requestPermissions(HomeActivity.this, permissions, PICK_PDF_CODE);
            }
        } else {
            Log.i(TAG, "getReadPDFPermission: PDF permissions has not been granted");
            ActivityCompat.requestPermissions(HomeActivity.this, permissions, PICK_PDF_CODE);
        }
    }

    //Inflate menu to bottom Bar: this adds the items to the action bar if present
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bottom_bar, menu);
        //return super.onCreateOptionsMenu(menu);
        return true;
    }

    //handle click events of the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                setFragment(settingsFragment);
                active = settingsFragment;
                break;
            case R.id.speech:
                setFragment(speechFragment);
                active = speechFragment;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Change fragment based on what the user clicks
    public void setFragment(Fragment fragment) {

        if(fragmentManager != null){
            Log.i(TAG, "FragmentManager is not null");
        }else{
            Log.i(TAG, "FragmentManager is null");
        }
        try {
            //FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            //fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().hide(active).show(fragment).commit();
            //fragmentManager.executePendingTransactions();
            if(fragment.isAdded()){
                Log.i(TAG, "Fragment can be Added");

            }else{
                Log.i(TAG, "Fragment can't be Added");
            }
            Log.i(TAG, String.format("%s%s%s%s%s", " setFragment: ", " PDF ",
                    R.id.fragment_container_home, "has been replaced with.", fragment.toString()));
        }catch(Exception e){
            Log.i(TAG, "Fragment can't be replaced");
            e.printStackTrace();
        }

    }

}
