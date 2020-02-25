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

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;

import butterknife.ButterKnife;

public class ReadPdfActivity extends AppCompatActivity {
    private static final String TAG = "ReadPdfActivity";
    ReadPdfFragment readPdfFragment ;
    PDFView pdfView ;
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
        setContentView(R.layout.activity_read_pdf);
        ButterKnife.bind(this);
        pdfView = (PDFView) findViewById(R.id.pdf_viewer);
        homeActivity = new HomeActivity();
        mainIntent  = getIntent(); //Get intent from HomeActivity
        readPdfFragment = new ReadPdfFragment();
       // receiveHomeActivityResults();
    }

    /*

     */
    private void receiveHomeActivityResults() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("pdfData", mainIntent);
        setResult(ReadPdfActivity.RESULT_OK, resultIntent);
        finish();
        Log.i(TAG, "receiveHomeActivityResults(): setResult for intent received");
    }

    //Method for reading pdf from device
    public void readPdfFromDevice(){
        Log.i(TAG, "readPdfFromDevice(): Method is about to set up  PDF view");
        //pdfView = readPdfFragment.pdfView;
        readPdfFragment = new ReadPdfFragment();
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
            //Log.i(TAG, "readPdfFromDevice: Intent data is not null; replacing fragment");
           // homeActivity.setFragment(readPdfFragment);
        }
    }

    //TODO use replace fragment in HomeActivity. Change layout :no need for a new fragment container

}
