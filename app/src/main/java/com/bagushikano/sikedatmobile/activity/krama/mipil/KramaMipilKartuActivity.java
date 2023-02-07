package com.bagushikano.sikedatmobile.activity.krama.mipil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.view.View;

import com.bagushikano.sikedatmobile.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import im.delight.android.webview.AdvancedWebView;

public class KramaMipilKartuActivity extends AppCompatActivity implements AdvancedWebView.Listener {

    private Toolbar homeToolbar;
    SharedPreferences loginPreferences;
    private AdvancedWebView advancedWebView;
    ExtendedFloatingActionButton printTestFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_krama_mipil_kartu);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        printTestFab = findViewById(R.id.print_fab_test);
        advancedWebView = findViewById(R.id.invoice_transaksi_webview);
        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        advancedWebView.setListener(this, this);
        advancedWebView.setMixedContentAllowed(true);
        advancedWebView.setDesktopMode(true);
        advancedWebView.addHttpHeader("Authorization", "Bearer " +  loginPreferences.getString("token", "empty"));
        advancedWebView.loadUrl(getResources().getString(R.string.api_endpoint) + "/api/user/krama/kartu-keluarga/" + getIntent().getIntExtra("KRAMA_KEY", -1));
        printTestFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createWebPrintJob(advancedWebView, getIntent().getStringExtra("KRAMA_NO_KEY"));
            }
        });

    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(String url) {

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                "Gagal dalam memuat kartu Krama Mipil, silahkan coba lagi nanti",
                Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }

    private void createWebPrintJob(AdvancedWebView webView, String noKrama) {

        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);

        String jobName = "Kartu Keluarga Krama - " + noKrama;

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter(jobName);

        // Create a print job with name and adapter instance


        PrintJob printJob = printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder()
                        .setMediaSize(new PrintAttributes.MediaSize("Roll58mm", "Roll58mm" , 1892, 5676))
                        .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                        .setResolution(new PrintAttributes.Resolution("200dpi", "paperRoll", 203,203))
                        .build()
        );
    }
}