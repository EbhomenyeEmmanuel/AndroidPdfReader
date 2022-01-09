package com.esq.androidpdfreader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReadPdfFragment extends Fragment {

    private static final String TAG = "ReadPdfFragment";
    public static PDFView pdfView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_read_pdf, container, false);
            pdfView = view.findViewById(R.id.pdf_viewer);
            Log.i(TAG, view != null ?"onCreateView(): pdfView has been initialized" : "onCreateView(): RootView is null");
        return view;
    }

    public PDFView getPdfView() {
        //pdfView = this.getView().findViewById(R.id.pdf_viewer);
        Log.i(TAG, pdfView==null? "PDFView: pdfView is null" : "PDFView: pdfView is not null");
        return pdfView;
    }
}
