package com.bagushikano.sikedatmobile.activity.mutasi.kelahiran;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bagushikano.sikedatmobile.R;
import com.bagushikano.sikedatmobile.activity.register.RegisterCompleteActivity;
import com.bagushikano.sikedatmobile.api.ApiRoute;
import com.bagushikano.sikedatmobile.api.RetrofitClient;
import com.bagushikano.sikedatmobile.model.RegisterResponse;
import com.bagushikano.sikedatmobile.model.kelahiran.KelahiranAjuan;
import com.bagushikano.sikedatmobile.model.krama.KramaMipilGetResponse;
import com.bagushikano.sikedatmobile.model.master.Penduduk;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KelahiranPengajuanBaruActivity extends AppCompatActivity {

    private Button kelahiranPengajuanNextButton, kelahiranPengajuanAktaFileCaptureButton;

    private TextInputEditText kelahiranNikField, kelahiranNoAktaKelahiranField, kelahiranBerkasAktaKelahiranField;
    private TextInputLayout kelahiranNikLayout, kelahiranNoAktaKelahiranLayout, kelahiranBerkasAktaKelahiranLayout;
    private LinearLayout kelahiranPengajuanFirstProgressContainer;
    private boolean isNikValid = true, isFileSelected = false;

    private Uri uriFile;

    private String AKTA_FILE_KEY = "AKTA_FILE", AKTA_NO_KEY = "AKTA_NO", NIK_KEY = "NIK", KRAMA_MIPIL_KEY = "KRARMA_MIPIL";

    SharedPreferences loginPreferences;


    private static final int REQUEST_CAMERA = 100;
    private static final int MY_PERMISSION_REQUEST_CAMERA = 101;

    private Toolbar homeToolbar;
    ActivityResultLauncher<Intent> startActivityIntent;

    private final String KELAHIRAN_RESUBMIT_KEY = "KELAHIRAN_RESUBMIT_DETAIL_KEY";
    Gson gson = new Gson();
    KelahiranAjuan kelahiranAjuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelahiran_pengajuan_baru);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        kelahiranAjuan = gson.fromJson(getIntent().getStringExtra(KELAHIRAN_RESUBMIT_KEY), KelahiranAjuan.class);

        loginPreferences = getSharedPreferences("login_preferences",Context.MODE_PRIVATE);

        kelahiranPengajuanFirstProgressContainer = findViewById(R.id.kelahiran_pengajuan_progress_first_layout);
        kelahiranNikField = findViewById(R.id.kelahiran_pengajuan_nik_field);
        kelahiranNoAktaKelahiranField = findViewById(R.id.kelahiran_pengajuan_no_akta_kelahiran_field);
        kelahiranBerkasAktaKelahiranField = findViewById(R.id.kelahiran_pengajuan_berkas_akta_kelahiran_field);
        kelahiranPengajuanAktaFileCaptureButton = findViewById(R.id.kelahiran_pengajuan_berkas_akta_capture_button);

        kelahiranNikLayout = findViewById(R.id.kelahiran_pengajuan_nik_form);
        kelahiranNoAktaKelahiranLayout = findViewById(R.id.kelahiran_pengajuan_no_akta_kelahiran_form);
        kelahiranBerkasAktaKelahiranLayout = findViewById(R.id.kelahiran_pengajuan_berkas_akta_kelahiran_form);

        if (kelahiranAjuan != null) {
            if (kelahiranAjuan.getCacahKramaMipil().getPenduduk().getNik() != null) {
                kelahiranNikField.setText(kelahiranAjuan.getCacahKramaMipil().getPenduduk().getNik());
            }
            if (kelahiranAjuan.getNomorAktaKelahiran() != null) {
                kelahiranNoAktaKelahiranField.setText(kelahiranAjuan.getNomorAktaKelahiran());
            }
        }

        kelahiranBerkasAktaKelahiranField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFile();
            }
        });

        kelahiranPengajuanAktaFileCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPermissionCameraGranted()) {
                    captureImage();
                }
            }
        });


        kelahiranBerkasAktaKelahiranField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    pickFile();
                }
            }
        });


        kelahiranNikField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(kelahiranNikField.getText().toString().length() == 0) {
                    kelahiranNikLayout.setError(null);
                    isNikValid = true;
                }
                else {
                    if (kelahiranNikField.getText().toString().length() < 16) {
                        kelahiranNikLayout.setError("NIK tidak boleh kurang dari 16 karakter");
                        isNikValid = false;
                    }
                    else if (kelahiranNikField.getText().toString().length() > 16) {
                        kelahiranNikLayout.setError("NIK tidak boleh lebih dari 16 karakter");
                        isNikValid = false;
                    }
                    else {
                        kelahiranNikLayout.setError(null);
                        isNikValid = true;
                    }
                }
            }
        });



        kelahiranPengajuanNextButton = findViewById(R.id.kelahiran_pengajuan_next_button);
        kelahiranPengajuanNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNikValid) {
                    continueToPengajuanNext();
                }
                else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Terdapat data yang belum valid, mohon periksa kembali.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        startActivityIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 1) {
                            setResult(1);
                            finish();
                        }
                    }
                });
    }

    private void captureImage() {
        try {
            File.createTempFile("profile_camera.jpg", null, getApplicationContext().getCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
        }
        File profilePictCamera = new File(getApplicationContext().getCacheDir(), "profile_camera.jpg");
        PackageManager pm = getPackageManager();
        if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
            i.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
                    getApplicationContext(),
                    "com.bagushikano.sikedatmobile.fileprovider",
                    profilePictCamera));
            startActivityForResult(i, REQUEST_CAMERA);
        } else {
            Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Kamera tidak tersedia", Snackbar.LENGTH_SHORT).show();
        }
    }

    private boolean isPermissionCameraGranted() {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Log.v("TAG", "Permission is granted");
            return true;
        } else {
            Log.v("TAG", "Permission is revoked");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSION_REQUEST_CAMERA);
            return false;
        }
    }

    public void pickFile() {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        String[] mimeTypes = {"image/jpg", "image/jpeg", "application/pdf"};
        chooseFile.setType("*/*");
        chooseFile.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            uriFile = data.getData();
            kelahiranBerkasAktaKelahiranField.setText("");
            isFileSelected = false;
            if (DocumentFile.fromSingleUri(this, uriFile).length() > 2000000) {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Berkas terlalu besar", Snackbar.LENGTH_SHORT).show();
                kelahiranBerkasAktaKelahiranLayout.setError("Berkas terlalu besar");
            } else {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Berkas akta kelahiran anak berhasil di pilih", Snackbar.LENGTH_SHORT).show();
                kelahiranBerkasAktaKelahiranField.setText(DocumentFile.fromSingleUri(this, uriFile).getName());
                isFileSelected = true;
                kelahiranBerkasAktaKelahiranLayout.setError(null);
            }
        }
    }

    public void continueToPengajuanNext() {
        kelahiranPengajuanNextButton.setVisibility(View.GONE);
        kelahiranPengajuanFirstProgressContainer.setVisibility(View.VISIBLE);
        ApiRoute continueToNext = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<KramaMipilGetResponse> kramaMipilGetResponseCall = continueToNext.getKramaMipil(
                "Bearer " + loginPreferences.getString("token", "empty")
        );
        kramaMipilGetResponseCall.enqueue(new Callback<KramaMipilGetResponse>() {
            @Override
            public void onResponse(Call<KramaMipilGetResponse> call, Response<KramaMipilGetResponse> response) {
                kelahiranPengajuanNextButton.setVisibility(View.VISIBLE);
                kelahiranPengajuanFirstProgressContainer.setVisibility(View.GONE);
                if (response.code() == 200 && response.body().getStatusCode() == 200 && response.body().getMessage().equals("data krama mipil sukses")) {
                    Intent kelahiranPengajuanNextIntent = new Intent(getApplicationContext(), KelahiranPengajuanBaruNextActivity.class);
                    kelahiranPengajuanNextIntent.putExtra(AKTA_NO_KEY, kelahiranNoAktaKelahiranField.getText().toString());
                    kelahiranPengajuanNextIntent.putExtra(NIK_KEY, kelahiranNikField.getText().toString());
                    if (isFileSelected) {
                        kelahiranPengajuanNextIntent.putExtra(AKTA_FILE_KEY, uriFile.toString());
                    }
                    if (kelahiranAjuan != null) {
                        kelahiranPengajuanNextIntent.putExtra(KELAHIRAN_RESUBMIT_KEY, getIntent().getStringExtra(KELAHIRAN_RESUBMIT_KEY));
                    }

                    Gson gson = new Gson();
                    String kramaMipilJson = gson.toJson(response.body().getKramaMipil());;
                    kelahiranPengajuanNextIntent.putExtra(KRAMA_MIPIL_KEY, kramaMipilJson);
                    startActivityIntent.launch(kelahiranPengajuanNextIntent);

                } else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<KramaMipilGetResponse> call, Throwable t) {
                kelahiranPengajuanNextButton.setVisibility(View.VISIBLE);
                kelahiranPengajuanFirstProgressContainer.setVisibility(View.GONE);
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}