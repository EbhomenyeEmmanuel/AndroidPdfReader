package com.esq.androidpdfreader.ui

import android.app.Activity
import android.content.Intent
import android.content.Intent.getIntent
import android.graphics.Canvas
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.esq.androidpdfreader.R
import com.esq.androidpdfreader.databinding.FragmentHomeBinding
import com.esq.androidpdfreader.utils.Constants.FILE_URI
import com.esq.androidpdfreader.utils.Constants.VIEW_TYPE
import com.esq.androidpdfreader.utils.longToast
import com.github.barteksc.pdfviewer.util.FitPolicy

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var bind: FragmentHomeBinding

    private val TAG = this.javaClass.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentHomeBinding.inflate(layoutInflater)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
        } else {
            //Use this method if the activity just started
            //receiveHomeActivityResults(getIntent())
        }
        //setFragment(readPdfFragment);
        //readPdfFromDevice(getIntent()!!)

    }

    //Send a result to homeActivity
    private fun receiveHomeActivityResults(intent: Intent?) {
        val resultIntent = Intent()
        resultIntent.putExtra("pdfData", intent)
        requireActivity().setResult(
            Activity.RESULT_OK,
            intent
        )
        Log.i(
            TAG,
            "receiveHomeActivityResults(): setResult for intent sent to HomeActivity"
        )
        //Calling finish makes mainIntent in the onCreate to be null
        //finish()
    }

    //Method for reading pdf from device
    private fun readPdfFromDevice(intent: Intent?) {
        Log.i(TAG, "readPdfFromDevice(): Method is about to set up PDF view")
        //  Log.i(TAG, "readPdfFromDevice(): Bundle is ");
        if (intent != null) {
            val viewType = intent.getStringExtra(VIEW_TYPE)
            if (viewType != null && !TextUtils.isEmpty(viewType)) {
                Log.i(TAG, "readPdfFromDevice: viewType is not null")
                if (viewType == "storage") {
                    Log.i(
                        TAG,
                        "readPdfFromDevice: viewType is equals to String -> storage"
                    )
                    val pdfFile = Uri.parse(intent.getStringExtra(FILE_URI))
                    bind.pdfView.fromUri(pdfFile)
                        .password(null)
                        .defaultPage(0) //Open default page
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .enableDoubletap(true) //Double tap to zoom
                        .onDraw { canvas: Canvas?, pageWidth: Float, pageHeight: Float, displayedPage: Int -> }
                        .onDrawAll { canvas: Canvas?, pageWidth: Float, pageHeight: Float, displayedPage: Int -> }
                        .onPageError { page: Int, t: Throwable? ->
                            requireActivity().longToast(
                                "Error while opening page$page"
                            )
                        }
                        .onPageChange { page: Int, pageCount: Int -> }
                        .onTap { true }
                        .onRender { nbPages -> bind.pdfView.fitToWidth(nbPages) }
                        .spacing(0)
                        .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
                        .pageFitPolicy(FitPolicy.WIDTH) // mode to fit pages in the view
                        .enableAnnotationRendering(false)
                        .enableAntialiasing(true)
                        .fitEachPage(false) // fit each page to the view, else smaller pages are scaled relative to largest page.
                        .pageSnap(false) // snap pages to screen boundaries
                        .pageFling(false) // make a fling change only a single page like ViewPager
                        .nightMode(false) // toggle night mode
                        .load()
                    Log.i(
                        TAG,
                        if (bind.pdfView.isShown) "readPdfFromDevice: pdfView is  shown" else "readPdfFromDevice: pdfView is not shown"
                    )
                }
            }
        } else {
            Log.i(TAG, "readPdfFromDevice: Null Intent; can't display PDF")
        }
    }

}