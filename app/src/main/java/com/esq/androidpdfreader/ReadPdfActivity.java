package com.esq.androidpdfreader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;

import butterknife.ButterKnife;

public class ReadPdfActivity extends AppCompatActivity {
    private static final String TAG = "ReadPdfActivity";
    static ReadPdfFragment readPdfFragment ;
    static PDFView pdfView ;

    Intent mainIntent;
    HomeActivity homeActivity;

//Called when an activity is started ForResults: Here the activity is started from HomeActivity
 @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == HomeActivity.READ_PDF_CODE && resultCode == Activity.RESULT_OK && data != null)) {
            Log.i(TAG, "onActivityResult -> ReadPDFActivity: result gotten from intent, readPdfFromDevice() is called");
            readPdfFromDevice();
            //Change fragment to pdfView fragment
            Fragment fragment = new ReadPdfFragment();
            homeActivity.setFragment(fragment);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,  " onCreate(): Method called due to Intent received" );
        setContentView(R.layout.activity_read_pdf);
        ButterKnife.bind(this);

       // readPdfFromDevice();
        //this.onResume();
        //When this activity is called for results this method would run
       // receiveHomeActivityResults();

        if (savedInstanceState != null){
            //mainIntent = (Intent) savedInstanceState.getBundle("seconds");
        }else {
            //Use this method if the activity just started
            receiveHomeActivityResults();
        }
        //setFragment(readPdfFragment);
    }

    protected void onSaveInstanceState(Bundle savedInstanceState) {
     super.onSaveInstanceState(savedInstanceState);
       //savedInstanceState.putExtra("intent", this.mainIntent);
        savedInstanceState.putChar("BundleChar", 'B');
        //The value of wasRunning was set in the onStop method ->
        // after the activity was recreated
    }

    /*

     */

    //Send a result to homeActivity
    private void receiveHomeActivityResults() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("pdfData", mainIntent);
        setResult(RESULT_OK, resultIntent);
        //Calling finish makes mainIntent in the onCreate to be null
        finish();
        Log.i(TAG, "receiveHomeActivityResults(): setResult for intent received");
    }

    //Initialize all variables
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: Variables initialized");
        readPdfFragment = new ReadPdfFragment();
        pdfView = (PDFView) readPdfFragment.getView();
        // Use the pdfView view of the ReadFragment in this activity
        // pdfView = ReadPdfFragment.pdfView;
        homeActivity = new HomeActivity();
        mainIntent  = getIntent(); //Get intent from HomeActivity
    }

    //Method for reading pdf from device
    public void readPdfFromDevice(){
        Log.i(TAG, "readPdfFromDevice(): Method is about to set up PDF view");
      //  Log.i(TAG, "readPdfFromDevice(): Bundle is ");
       readPdfFragment = new ReadPdfFragment();
       homeActivity = new HomeActivity();
        if (mainIntent != null) {
            String viewType = mainIntent.getStringExtra("ViewType");
            if (viewType != null && !TextUtils.isEmpty(viewType)) {
                if(viewType.equals("storage")){
                    Uri pdfFile = Uri.parse(mainIntent.getStringExtra("FileUri"));
                    pdfView.fromUri(pdfFile)
                            .password(null)
                            .defaultPage(0) //Open default page
                            .enableSwipe(true)
                            .swipeHorizontal(false)
                            .enableDoubletap(true)//Double tap to zoom
                            .onDraw((canvas, pageWidth, pageHeight, displayedPage) -> {

                            })
                            .onDrawAll((Canvas canvas, float pageWidth, float pageHeight, int displayedPage)-> {

                            })
                            .onPageError((page, t) -> Toast.makeText(ReadPdfActivity.this, "Error while opening page" + page ,Toast.LENGTH_LONG).show())
                            .onPageChange((page, pageCount) -> {

                            })
                            .onTap(new OnTapListener() {
                                @Override
                                public boolean onTap(MotionEvent e) {
                                    return true;
                                }
                            }).onRender(new OnRenderListener() {
                        @Override
                        public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
                            pdfView.fitToWidth(); //Fixed screen size
                        }
                    }).enableAnnotationRendering(true)
                            .invalidPageColor(Color.WHITE)
                            .load();
                }
            }
            Log.i(TAG, "readPdfFromDevice: Intent data is not null; replacing fragment");

           //homeActivity.setFragment(readPdfFragment);
           // Log.i(TAG, "readPdfFromDevice: finish() called");
           // finish();
        }else{
            Log.i(TAG, "readPdfFromDevice: Main Intent is null can't display PDF");
        }
    }

}
