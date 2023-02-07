package com.bagushikano.sikedatmobile.activity.mutasi.kematian;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.documentfile.provider.DocumentFile;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bagushikano.sikedatmobile.R;
import com.bagushikano.sikedatmobile.adapter.kematian.KematianAnggotaKeluargaSelectionAdapter;
import com.bagushikano.sikedatmobile.api.ApiRoute;
import com.bagushikano.sikedatmobile.api.RetrofitClient;
import com.bagushikano.sikedatmobile.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobile.model.kelahiran.KelahiranAjuan;
import com.bagushikano.sikedatmobile.model.kematian.KematianAjuan;
import com.bagushikano.sikedatmobile.model.kematian.KematianGetCacahMipilResponse;
import com.bagushikano.sikedatmobile.model.kematian.KematianPengajuanResponse;
import com.bagushikano.sikedatmobile.util.ChangeDateTimeFormat;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KematianPengajuanBaruActivity extends AppCompatActivity {

    private Button kematianPengajuanSubmitButton, kematianPengajuanRefreshButton;

    private TextInputEditText kematianPengajuanTanggalKematianField, kematianPengajuanPenyebabKematianField,
            kematianPengajuanNoAktaKematianField, kematianPengajuanNoSuratKeteranganKematianField,
            kematianPengajuanAktaKematianBerkasField, kematianPengajuanSuratKeteranganKematianBerkasField,
            kematianKeteranganField;

    private AutoCompleteTextView kematianPengajuanAnggotaKeluargaField;

    private TextInputLayout kematianPengajuanTanggalKematianLayout, kematianPengajuanPenyebabKematianLayout,
            kematianPengajuanNoAktaKematianLayout, kematianPengajuanNoSuratKeteranganKematianLayout,
            kematianPengajuanAktaKematianBerkasLayout, kematianPengajuanSuratKeteranganKematianBerkasLayout,
            kematianPengajuanAnggotaKeluargaLayout;

    private LinearLayout kematianPengajuanLoadingContainer,
            kematianPengajuanFailedContainer, kematianPengajuanProgressContanier;
    private NestedScrollView kematianPengajuanContainer;

    SharedPreferences loginPreferences;

    ArrayList<CacahKramaMipil> anggotaKeluargaSelectionList = new ArrayList<>();
    CacahKramaMipil anggotaKeluargaSelected;
    KematianAnggotaKeluargaSelectionAdapter kematianAnggotaKeluargaSelectionAdapter;

    private boolean isAnggotakeluargaValid = false, isTanggalKematianValid = false, isPenyebabKematianValid = false;

    private Uri uriAktaKelahiran, uriSuketKematian;

    ApiRoute retro;

    private Toolbar homeToolbar;


    private final String KEMATIAN_RESUBMIT_KEY = "KEMATIAN_RESUBMIT_DETAIL_KEY";
    Gson gson = new Gson();
    KematianAjuan kematianAjuan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kematian_pengajuan_baru);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loginPreferences = getSharedPreferences("login_preferences",Context.MODE_PRIVATE);
        retro = RetrofitClient.buildRetrofit().create(ApiRoute.class);

        kematianAjuan = gson.fromJson(getIntent().getStringExtra(KEMATIAN_RESUBMIT_KEY), KematianAjuan.class);

        kematianPengajuanFailedContainer = findViewById(R.id.pengajuan_kematian_failed_container);
        kematianPengajuanLoadingContainer = findViewById(R.id.pengajuan_kematian_loading_container);
        kematianPengajuanProgressContanier = findViewById(R.id.kematian_pengajuan_progress_layout);
        kematianPengajuanContainer = findViewById(R.id.kematian_pengajuan_container);

        kematianPengajuanTanggalKematianField = findViewById(R.id.kematian_pengajuan_tanggal_kematian_field);
        kematianPengajuanPenyebabKematianField = findViewById(R.id.kematian_pengajuan_penyebab_kematian_field);
        kematianPengajuanNoAktaKematianField = findViewById(R.id.kematian_pengajuan_no_akta_kematian_field);
        kematianPengajuanNoSuratKeteranganKematianField = findViewById(R.id.kematian_pengajuan_no_suket_kematian_field);
        kematianPengajuanAktaKematianBerkasField = findViewById(R.id.kematian_pengajuan_berkas_akta_kematian_field);
        kematianPengajuanSuratKeteranganKematianBerkasField = findViewById(R.id.kematian_pengajuan_berkas_suket_kematian_field);
        kematianPengajuanAnggotaKeluargaField = findViewById(R.id.kematian_pengajuan_anggota_keluarga_field);
        kematianKeteranganField = findViewById(R.id.kematian_pengajuan_keterangan_field);

        kematianPengajuanTanggalKematianLayout = findViewById(R.id.kematian_pengajuan_tanggal_kematian_form);
        kematianPengajuanPenyebabKematianLayout = findViewById(R.id.kematian_pengajuan_penyebab_kematian_form);
        kematianPengajuanNoAktaKematianLayout = findViewById(R.id.kematian_pengajuan_no_akta_kematian_form);
        kematianPengajuanNoSuratKeteranganKematianLayout = findViewById(R.id.kematian_pengajuan_no_suket_kematian_form);
        kematianPengajuanAktaKematianBerkasLayout = findViewById(R.id.kematian_pengajuan_berkas_akta_kematian_form);
        kematianPengajuanSuratKeteranganKematianBerkasLayout = findViewById(R.id.kematian_pengajuan_berkas_suket_kematian_form);
        kematianPengajuanAnggotaKeluargaLayout = findViewById(R.id.kematian_pengajuan_anggota_keluarga_form);

        if (kematianAjuan != null) {
            if (kematianAjuan.getPenyebabKematian() != null) {
                kematianPengajuanPenyebabKematianField.setText(kematianAjuan.getPenyebabKematian());
            }
            if (kematianAjuan.getKeterangan() != null) {
                kematianKeteranganField.setText(kematianAjuan.getKeterangan());
            }

            if (kematianAjuan.getNomorAktaKematian() != null) {
                kematianPengajuanNoAktaKematianField.setText(kematianAjuan.getNomorAktaKematian());
            }

            if (kematianAjuan.getNomorSuketKematian() != null) {
                kematianPengajuanNoSuratKeteranganKematianField.setText(kematianAjuan.getNomorSuketKematian());
            }
        }


        kematianPengajuanSubmitButton = findViewById(R.id.kematian_pengajuan_submit_button);
        kematianPengajuanSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });

        kematianPengajuanPenyebabKematianField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(kematianPengajuanPenyebabKematianField.getText().toString().length() == 0) {
                    kematianPengajuanPenyebabKematianLayout.setError("Penyebab kematian tidak boleh kosong");
                    kematianPengajuanPenyebabKematianLayout.setErrorEnabled(true);
                    isPenyebabKematianValid = false;
                }
                else {
                    kematianPengajuanPenyebabKematianLayout.setError(null);
                    kematianPengajuanPenyebabKematianLayout.setErrorEnabled(false);
                    isPenyebabKematianValid = true;
                }
            }
        });

        kematianPengajuanAktaKematianBerkasField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFile(1);
            }
        });


        kematianPengajuanAktaKematianBerkasField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    pickFile(1);
                }
            }
        });

        kematianPengajuanSuratKeteranganKematianBerkasField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFile(2);
            }
        });


        kematianPengajuanSuratKeteranganKematianBerkasField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    pickFile(2);
                }
            }
        });

        MaterialDatePicker.Builder<Long> datePickerBuilderAppointmentDate = MaterialDatePicker.Builder.datePicker();
        datePickerBuilderAppointmentDate.setTitleText("Pilih tanggal kematian");
        final MaterialDatePicker<Long> datePickerAppointmentDate = datePickerBuilderAppointmentDate.build();
        datePickerAppointmentDate.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selectedDate) {
                // link: https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
                TimeZone timeZoneUTC = TimeZone.getDefault();
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                SimpleDateFormat simpleFormat = new SimpleDateFormat("EEE, dd-MM-yyyy", Locale.US);
                Date date = new Date(selectedDate + offsetFromUTC);
                kematianPengajuanTanggalKematianField.setText(simpleFormat.format(date));
                isTanggalKematianValid = true;
            }
        });

        kematianPengajuanTanggalKematianField.setShowSoftInputOnFocus(false);
        kematianPengajuanTanggalKematianField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(datePickerAppointmentDate.isVisible())) {
                    datePickerAppointmentDate.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                }
            }
        });

        kematianPengajuanTanggalKematianField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    if (!(datePickerAppointmentDate.isVisible())) {
                        datePickerAppointmentDate.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                }
            }
        });

        kematianAnggotaKeluargaSelectionAdapter = new KematianAnggotaKeluargaSelectionAdapter(this, anggotaKeluargaSelectionList);
        kematianPengajuanAnggotaKeluargaField.setAdapter(kematianAnggotaKeluargaSelectionAdapter);
        kematianPengajuanAnggotaKeluargaField.setThreshold(100);

        kematianPengajuanAnggotaKeluargaField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                anggotaKeluargaSelected = (CacahKramaMipil) adapterView.getItemAtPosition(i);
                kematianPengajuanAnggotaKeluargaField.setText(anggotaKeluargaSelected.getPenduduk().getNama());
                isAnggotakeluargaValid = true;
            }
        });
        getAnggotaKeluarga();
    }

    /**
     * @param flag: 1 untuk akta, 2 suket
     */
    public void pickFile(int flag) {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        String[] mimeTypes = {"image/jpg", "image/jpeg", "application/pdf"};
        chooseFile.setType("*/*");
        chooseFile.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, flag);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            uriAktaKelahiran = data.getData();
            kematianPengajuanAktaKematianBerkasField.setText("");
            if (DocumentFile.fromSingleUri(this, uriAktaKelahiran).length() > 2000000) {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Berkas terlalu besar", Snackbar.LENGTH_SHORT).show();
                kematianPengajuanAktaKematianBerkasLayout.setError("Berkas terlalu besar");
            } else {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Berkas akta kematian berhasil di pilih", Snackbar.LENGTH_SHORT).show();
                kematianPengajuanAktaKematianBerkasField.setText(DocumentFile.fromSingleUri(this, uriAktaKelahiran).getName());
                kematianPengajuanAktaKematianBerkasLayout.setError(null);
            }
        }
        else if (resultCode == RESULT_OK && requestCode == 2) {
            uriSuketKematian = data.getData();
            kematianPengajuanSuratKeteranganKematianBerkasField.setText("");
            if (DocumentFile.fromSingleUri(this, uriAktaKelahiran).length() > 2000000) {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Berkas terlalu besar", Snackbar.LENGTH_SHORT).show();
                kematianPengajuanSuratKeteranganKematianBerkasLayout.setError("Berkas terlalu besar");
            } else {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Berkas surat keterangan kematian berhasil di pilih", Snackbar.LENGTH_SHORT).show();
                kematianPengajuanSuratKeteranganKematianBerkasField.setText(DocumentFile.fromSingleUri(this, uriSuketKematian).getName());
                kematianPengajuanSuratKeteranganKematianBerkasLayout.setError(null);
            }
        }
    }

    public void submitData() {
        if (isAnggotakeluargaValid && isTanggalKematianValid && isPenyebabKematianValid) {
            RequestBody cacahKramaData, tanggalKematianData, penyebabKematianData, nomorSuratAktaKematianData,
                    noSuratKeteranganKematianData, aktaKematianData, suketKematianData, keteranganData;
            Call<KematianPengajuanResponse> kematianPengajuanResponseCall = null;

            cacahKramaData = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(anggotaKeluargaSelected.getId()));
            tanggalKematianData = RequestBody.create(MediaType.parse("text/plain"),
                    ChangeDateTimeFormat.changeDateFormatForForm(kematianPengajuanTanggalKematianField.getText().toString()));
            nomorSuratAktaKematianData = RequestBody.create(MediaType.parse("text/plain"), kematianPengajuanNoAktaKematianField.getText().toString());
            noSuratKeteranganKematianData = RequestBody.create(MediaType.parse("text/plain"), kematianPengajuanNoSuratKeteranganKematianField.getText().toString());
            penyebabKematianData = RequestBody.create(MediaType.parse("text/plain"), kematianPengajuanPenyebabKematianField.getText().toString());
            keteranganData = RequestBody.create(MediaType.parse("text/plain"), kematianKeteranganField.getText().toString());

            MultipartBody.Part aktaKematianFile = null;
            MultipartBody.Part suketKematianFile = null;

            if (uriAktaKelahiran != null) {
                File filesDir = getApplicationContext().getCacheDir();
                File aktaFile = new File(filesDir, DocumentFile.fromSingleUri(this, uriAktaKelahiran).getName());
                OutputStream os;
                ContentResolver cr = getApplicationContext().getContentResolver();
                InputStream inputStream = null;
                try {
                    inputStream = cr.openInputStream(uriAktaKelahiran);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    os = new FileOutputStream(aktaFile);
                    int length;
                    byte[] bytes = new byte[1024];

                    while ((length = inputStream.read(bytes)) != -1) {
                        os.write(bytes, 0, length);
                    }
                    os.flush();
                    os.close();
                    inputStream.close();
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Error writing temp file", e);
                }
                aktaKematianData = RequestBody.create(MediaType.parse("multipart/form-data"), aktaFile);
                aktaKematianFile = MultipartBody.Part.createFormData("file_akta_kematian", aktaFile.getName(), aktaKematianData);
            }

            if (uriSuketKematian != null) {
                File filesDir = getApplicationContext().getCacheDir();
                File suketFile = new File(filesDir, DocumentFile.fromSingleUri(this, uriSuketKematian).getName());
                OutputStream os;
                ContentResolver cr = getApplicationContext().getContentResolver();
                InputStream inputStream = null;
                try {
                    inputStream = cr.openInputStream(uriSuketKematian);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    os = new FileOutputStream(suketFile);
                    int length;
                    byte[] bytes = new byte[1024];

                    while ((length = inputStream.read(bytes)) != -1) {
                        os.write(bytes, 0, length);
                    }
                    os.flush();
                    os.close();
                    inputStream.close();
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Error writing temp file", e);
                }
                suketKematianData = RequestBody.create(MediaType.parse("multipart/form-data"), suketFile);
                suketKematianFile = MultipartBody.Part.createFormData("file_suket_kematian", suketFile.getName(), suketKematianData);
            }

            kematianPengajuanResponseCall = retro.kematianPengajuan(
                    "Bearer " + loginPreferences.getString("token", "empty"),
                    cacahKramaData, tanggalKematianData, penyebabKematianData, nomorSuratAktaKematianData, noSuratKeteranganKematianData, keteranganData,
                    suketKematianFile, aktaKematianFile);

            kematianPengajuanSubmitButton.setVisibility(View.GONE);
            kematianPengajuanProgressContanier.setVisibility(View.VISIBLE);

            kematianPengajuanResponseCall.enqueue(new Callback<KematianPengajuanResponse>() {
                @Override
                public void onResponse(Call<KematianPengajuanResponse> call, Response<KematianPengajuanResponse> response) {
                    kematianPengajuanSubmitButton.setVisibility(View.VISIBLE);
                    kematianPengajuanProgressContanier.setVisibility(View.GONE);
                    if (response.code() == 200 && response.body().getStatusCode() == 200 && response.body().getMessage().equals("pengajuan kematian berhasil")) {
                        Intent kematianPengajuanComplateIntent = new Intent(getApplicationContext(), KematianPengajuanBaruCompleteActivity.class);
                        Gson gson = new Gson();
                        String kelahiranJSon = gson.toJson(response.body().getKematian());
                        kematianPengajuanComplateIntent.putExtra("KEMATIAN_KEY", kelahiranJSon);
                        startActivity(kematianPengajuanComplateIntent);
                        setResult(1);
                        finish();
                    } else {
                        Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<KematianPengajuanResponse> call, Throwable t) {
                    kematianPengajuanSubmitButton.setVisibility(View.VISIBLE);
                    kematianPengajuanProgressContanier.setVisibility(View.GONE);
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Terdapat data yang tidak valid, periksa data kembali", Snackbar.LENGTH_SHORT).show();
        }
    }

    public void getAnggotaKeluarga() {
        kematianPengajuanLoadingContainer.setVisibility(View.VISIBLE);
        kematianPengajuanFailedContainer.setVisibility(View.GONE);
        kematianPengajuanContainer.setVisibility(View.GONE);
        ApiRoute getAnggota = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<KematianGetCacahMipilResponse> kematianGetCacahMipilResponseCall = getAnggota.kematianGetCacahMipil(
                "Bearer " + loginPreferences.getString("token", "empty")
        );
        kematianGetCacahMipilResponseCall.enqueue(new Callback<KematianGetCacahMipilResponse>() {
            @Override
            public void onResponse(Call<KematianGetCacahMipilResponse> call, Response<KematianGetCacahMipilResponse> response) {
                if (response.code() == 200 && response.body().getStatusCode() == 200 && response.body().getMessage().equals("data cacah krama mipil sukses")) {
                    anggotaKeluargaSelectionList.clear();
                    anggotaKeluargaSelectionList.addAll(response.body().getCacahKramaMipilList());
                    kematianAnggotaKeluargaSelectionAdapter.notifyDataSetChanged();
                    kematianPengajuanLoadingContainer.setVisibility(View.GONE);
                    kematianPengajuanFailedContainer.setVisibility(View.GONE);
                    kematianPengajuanContainer.setVisibility(View.VISIBLE);

                } else {
                    kematianPengajuanLoadingContainer.setVisibility(View.GONE);
                    kematianPengajuanFailedContainer.setVisibility(View.VISIBLE);
                    kematianPengajuanContainer.setVisibility(View.GONE);
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<KematianGetCacahMipilResponse> call, Throwable t) {
                kematianPengajuanLoadingContainer.setVisibility(View.GONE);
                kematianPengajuanFailedContainer.setVisibility(View.VISIBLE);
                kematianPengajuanContainer.setVisibility(View.GONE);
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}