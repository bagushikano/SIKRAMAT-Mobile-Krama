package com.bagushikano.sikedatmobile.activity.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bagushikano.sikedatmobile.R;
import com.bagushikano.sikedatmobile.activity.register.RegisterCompleteActivity;
import com.bagushikano.sikedatmobile.api.ApiRoute;
import com.bagushikano.sikedatmobile.api.RetrofitClient;
import com.bagushikano.sikedatmobile.model.RegisterResponse;
import com.bagushikano.sikedatmobile.model.ResponseGeneral;
import com.bagushikano.sikedatmobile.model.master.Penduduk;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPasswordActivity extends AppCompatActivity {

    private Button changePasswordButton;
    private TextInputEditText oldPassField, passwordField, cPasswordField;
    private TextInputLayout oldPassLayout, passwordLayout, cPasswordLayout;
    private LinearLayout changePassLoadingLayout;
    private Boolean isPasswordValid = false, isPasswordCorrect = false;
    private Toolbar homeToolbar;
    SharedPreferences loginPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        changePasswordButton = findViewById(R.id.edit_pass_submit_button);
        oldPassField = findViewById(R.id.edit_password_lama_field);
        passwordField = findViewById(R.id.edit_password_field);
        cPasswordField = findViewById(R.id.edit_cpassword_field);
        passwordLayout = findViewById(R.id.edit_password_form);
        cPasswordLayout = findViewById(R.id.edit_cpassword_form);
        changePassLoadingLayout = findViewById(R.id.edit_pass_progress);
        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        passwordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // TODO add more password criteria
                if (passwordField.getText().toString().length() < 8) {
                    passwordLayout.setError("Password kurang dari 8 karakter");
                    isPasswordValid = false;
                } else {
                    passwordLayout.setError(null);
                    isPasswordValid = true;
                }
            }
        });

        cPasswordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!(passwordField.getText().toString().equals(cPasswordField.getText().toString()))) {
                    cPasswordLayout.setError("Password tidak cocok");
                    isPasswordCorrect = false;
                } else {
                    cPasswordLayout.setError(null);
                    isPasswordCorrect = true;
                }
            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });
    }

    public void submitData() {
        if (isPasswordValid && isPasswordCorrect) {
            changePasswordButton.setVisibility(View.GONE);
            changePassLoadingLayout.setVisibility(View.VISIBLE);

            ApiRoute submitData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
            Call<ResponseGeneral> registerResponseCall = submitData.changePassword(
                    "Bearer " + loginPreferences.getString("token", "empty"),
                    oldPassField.getText().toString(),
                    cPasswordField.getText().toString()
            );
            registerResponseCall.enqueue(new Callback<ResponseGeneral>() {
                @Override
                public void onResponse(Call<ResponseGeneral> call, Response<ResponseGeneral> response) {
                    changePasswordButton.setVisibility(View.VISIBLE);
                    changePassLoadingLayout.setVisibility(View.GONE);

                    if (response.code() == 200 && response.body().getStatusCode() == 200 && response.body().getMessage().equals("Berhasil mengubah password")) {
                        Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Password berhasil diubah.", Snackbar.LENGTH_SHORT).show();
                    } else if (response.code() == 200 && response.body().getStatusCode() == 200
                            && response.body().getMessage().equals("Gagal mengubah password. Password lama tidak sesuai")) {
                        Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                "Password lama tidak sesuai. Harap periksa password kembali.", Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseGeneral> call, Throwable t) {
                    changePasswordButton.setVisibility(View.VISIBLE);
                    changePassLoadingLayout.setVisibility(View.GONE);
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
            });
        }

        else{
            Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Terdapat data yang tidak valid, periksa data kembali", Snackbar.LENGTH_SHORT).show();
        }
    }
}