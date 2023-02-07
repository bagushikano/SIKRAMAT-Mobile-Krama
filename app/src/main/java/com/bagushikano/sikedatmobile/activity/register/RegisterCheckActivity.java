package com.bagushikano.sikedatmobile.activity.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bagushikano.sikedatmobile.R;
import com.bagushikano.sikedatmobile.api.ApiRoute;
import com.bagushikano.sikedatmobile.api.RetrofitClient;
import com.bagushikano.sikedatmobile.model.RegisterCheckNikResponse;
import com.bagushikano.sikedatmobile.util.CloseKeyboard;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterCheckActivity extends AppCompatActivity {

    private Button registerContinue;
    private TextInputLayout nikLayout, tglLahirLayout;
    private TextInputEditText nikField, tglLahirField;
    private MaterialCardView nikNotFoundCard, tglLahirNotMatchCard, nikAccountExistCard;
    private LinearLayout registerLoadingContainer;
    private boolean isNikValid = false, isTglLahirValid = false;
    private String tglLahir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_check);

        registerContinue = findViewById(R.id.register_continue_button);
        nikLayout = findViewById(R.id.register_nik_form);
        tglLahirLayout = findViewById(R.id.register_tgllahir_form);
        nikField = findViewById(R.id.register_nik_field);
        tglLahirField = findViewById(R.id.register_tgllahir_field);
        nikNotFoundCard = findViewById(R.id.register_nik_notfound_card);
        tglLahirNotMatchCard = findViewById(R.id.register_tgllahir_wrong_card);
        nikAccountExistCard = findViewById(R.id.register_nik_account_existed_card);
        registerLoadingContainer = findViewById(R.id.register_check_progress_layout);

        nikField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(nikField.getText().toString().length() == 0) {
                    nikLayout.setError("NIK tidak boleh kosong");
                    isNikValid = false;
                }
                else {
                    if (nikField.getText().toString().length() < 16) {
                        nikLayout.setError("NIK tidak boleh kurang dari 16 karakter");
                        isNikValid = false;
                    }
                    else if (nikField.getText().toString().length() > 16) {
                        nikLayout.setError("NIK tidak boleh lebih dari 16 karakter");
                        isNikValid = false;
                    }
                    else {
                        nikLayout.setError(null);
                        isNikValid = true;
                    }
                }
            }
        });

        MaterialDatePicker.Builder<Long> datePickerBuilderTglLahir = MaterialDatePicker.Builder.datePicker();
        datePickerBuilderTglLahir.setTitleText("Pilih tanggal lahir");
        final MaterialDatePicker<Long> datePickerTglLahir = datePickerBuilderTglLahir.build();

        datePickerTglLahir.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selectedDate) {
                // link: https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
                TimeZone timeZoneUTC = TimeZone.getDefault();
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                SimpleDateFormat simpleFormat = new SimpleDateFormat("EEE, dd-MM-yyyy", Locale.US);
                Date date = new Date(selectedDate + offsetFromUTC);
                tglLahirField.setText(simpleFormat.format(date));
                tglLahir = changeDateFormat(simpleFormat.format(date));
                isTglLahirValid = true;
            }
        });

        tglLahirField.setShowSoftInputOnFocus(false);
        tglLahirField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(datePickerTglLahir.isVisible())) {
                    datePickerTglLahir.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                }
            }
        });

        tglLahirField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    if (!(datePickerTglLahir.isVisible())) {
                        datePickerTglLahir.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                }
            }
        });

        registerContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CloseKeyboard.CloseKeyboard(getCurrentFocus(), getApplicationContext());
                submitData();
            }
        });
    }

    public void submitData() {
        if (isNikValid && isTglLahirValid) {
            setLoadingContainerVisible();
            ApiRoute submitData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
            Call<RegisterCheckNikResponse> registerCheckNikResponseCall = submitData.registerCheckNik(nikField.getText().toString());
            registerCheckNikResponseCall.enqueue(new Callback<RegisterCheckNikResponse>() {
                @Override
                public void onResponse(Call<RegisterCheckNikResponse> call, Response<RegisterCheckNikResponse> response) {
                    setLoadingContainerGone();
                    if (response.code() == 200 && response.body().getStatusCode() == 200 && response.body().getMessage().equals("data penduduk ditemukan")) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date tglLahir1 = null, tglLahir2 = null;
                        try {
                            tglLahir1 = simpleDateFormat.parse(response.body().getPenduduk().getTanggalLahir());
                            tglLahir2 = simpleDateFormat.parse(tglLahir);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (tglLahir1.compareTo(tglLahir2) != 0) {
                            nikNotFoundCard.setVisibility(View.GONE);
                            nikAccountExistCard.setVisibility(View.GONE);
                            tglLahirNotMatchCard.setVisibility(View.VISIBLE);
                        } else {
                            Intent registerContinueIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                            Gson gson = new Gson();
                            String registerJson = gson.toJson(response.body().getPenduduk());
                            registerContinueIntent.putExtra("REGISTER_KEY", registerJson);
                            startActivity(registerContinueIntent);
                            finish();
                        }

                    } else if (response.code() == 200 && response.body().getStatusCode() == 500 && response.body().getMessage().equals("data penduduk tidak ditemukan")) {
                        nikNotFoundCard.setVisibility(View.VISIBLE);
                        nikAccountExistCard.setVisibility(View.GONE);
                        tglLahirNotMatchCard.setVisibility(View.GONE);
                    } else if (response.code() == 200 && response.body().getStatusCode() == 500 && response.body().getMessage().equals("akun sudah ada")) {
                        nikNotFoundCard.setVisibility(View.GONE);
                        nikAccountExistCard.setVisibility(View.VISIBLE);
                        tglLahirNotMatchCard.setVisibility(View.GONE);
                    } else {
                        Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RegisterCheckNikResponse> call, Throwable t) {
                    setLoadingContainerGone();
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Terdapat data yang tidak valid, periksa data kembali", Snackbar.LENGTH_SHORT).show();
        }
    }


    public void setLoadingContainerVisible() {
        registerLoadingContainer.setVisibility(View.VISIBLE);
        registerContinue.setVisibility(View.GONE);
        nikNotFoundCard.setVisibility(View.GONE);
        nikAccountExistCard.setVisibility(View.GONE);
        tglLahirNotMatchCard.setVisibility(View.GONE);
    }
    public void setLoadingContainerGone() {
        registerLoadingContainer.setVisibility(View.GONE);
        registerContinue.setVisibility(View.VISIBLE);
    }

    public String changeDateFormat(String time) {
        String inputPattern = "EEE, dd-MM-yyyy";
        String outputPattern = "yyyy-MM-dd";
        SimpleDateFormat input = new SimpleDateFormat(inputPattern);
        SimpleDateFormat output = new SimpleDateFormat(outputPattern);

        Date date;
        String str = null;

        try {
            date = input.parse(time);
            str = output.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}