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
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.util.Objects;

import butterknife.ButterKnife;

public class ReadPdfActivity extends AppCompatActivity {
    private static final String TAG = "ReadPdfActivity";
    static ReadPdfFragment readPdfFragment ;
    static PDFView pdfView ;
    Intent mainIntent;
    final String VIEW_TYPE = "ViewType";
    final String FILE_URI = "FileUri";
    HomeActivity homeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,  " onCreate(): Method called due to Intent received" );
        setContentView(R.layout.activity_read_pdf);
        ButterKnife.bind(this);
        //setFragment(new ReadPdfFragment());
        //new  ReadPdfFragment().
        mainIntent = getIntent();

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

    //Send a result to homeActivity
    private void receiveHomeActivityResults() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("pdfData", mainIntent);
        setResult(RESULT_OK, mainIntent);//Send the intent data as a parameter to save its value
        Log.i(TAG,mainIntent ==null? "Intent: mainIntent is null" : "Intent: mainIntent is " + mainIntent);
        Log.i(TAG, "receiveHomeActivityResults(): setResult for intent sent to HomeActivity");
        //Calling finish makes mainIntent in the onCreate to be null
        finish();
    }

    public void initializeVariables(Intent intent) {
        Log.d(TAG, "initializeVariables: ");
        //readPdfFragment = (ReadPdfFragment) getSupportFragmentManager().findFragmentById(R.id.detail_frag);
        readPdfFragment = new ReadPdfFragment();
        Log.i(TAG, readPdfFragment==null? "initializeVariables:: readPdfFragment is null"
                : "initializeVariables:: readFragment is not null -> " + readPdfFragment);
        pdfView = readPdfFragment.getPdfView();
        // Use the pdfView view of the ReadFragment in this activity
        mainIntent = intent;
        Log.i(TAG, mainIntent==null? "Intent: mainIntent is null" : "Intent: mainIntent is -> " + mainIntent);
        //When this activity is called for results this method would run
        readPdfFromDevice(mainIntent);
    }

    //Method for reading pdf from device
    public void readPdfFromDevice(Intent intent){
        Log.i(TAG, "readPdfFromDevice(): Method is about to set up PDF view");
      //  Log.i(TAG, "readPdfFromDevice(): Bundle is ");
        mainIntent = intent;
        if (mainIntent != null) {
            String viewType = mainIntent.getStringExtra(VIEW_TYPE);
            if (viewType != null && !TextUtils.isEmpty(viewType)) {
                Log.i(TAG, "readPdfFromDevice: viewType is not null");
                if(viewType.equals("storage")){
                    Log.i(TAG, "readPdfFromDevice: viewType is equals to String -> storage");
                    Uri pdfFile = Uri.parse(mainIntent.getStringExtra(FILE_URI));
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
                            })
                            .onRender(new OnRenderListener() {
                                @Override
                                public void onInitiallyRendered(int nbPages) {
                                  pdfView.fitToWidth(nbPages);
                                }
                            })
                            .spacing(0)
                            .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
                            .pageFitPolicy(FitPolicy.WIDTH) // mode to fit pages in the view
                            .enableAnnotationRendering(false)
                            .enableAntialiasing(true)
                            .fitEachPage(false) // fit each page to the view, else smaller pages are scaled relative to largest page.
                            .pageSnap(false) // snap pages to screen boundaries
                            .pageFling(false) // make a fling change only a single page like ViewPager
                            .nightMode(false) // toggle night mode
                            .load();

                    Log.i(TAG,  pdfView.isShown() ? "readPdfFromDevice: pdfView is  shown" : "readPdfFromDevice: pdfView is not shown" );

                }
            }
        }else{
            Log.i(TAG, "readPdfFromDevice: Null Intent; can't display PDF");
        }
    }

}
