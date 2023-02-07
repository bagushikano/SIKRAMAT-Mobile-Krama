package com.bagushikano.sikedatmobile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bagushikano.sikedatmobile.model.kelahiran.KelahiranAjuanGetResponse;
import com.bagushikano.sikedatmobile.model.kelahiran.KelahiranGetResponse;
import com.bagushikano.sikedatmobile.model.notifikasi.NotifikasiGetResponse;
import com.bagushikano.sikedatmobile.repository.HomeRepository;
import com.bagushikano.sikedatmobile.repository.KelahiranRepository;

public class BerandaViewModel extends ViewModel {
    private MutableLiveData<NotifikasiGetResponse> notifikasiGetResponseMutableLiveData;

    private HomeRepository homeRepository;

    public void init(String token) {
        if (notifikasiGetResponseMutableLiveData != null) {
            return;
        }
        homeRepository = homeRepository.getInstance();
        notifikasiGetResponseMutableLiveData = homeRepository.getNotifikasi(token);
    }

    public void getDataNotifikasi(String token) {
        notifikasiGetResponseMutableLiveData = homeRepository.getNotifikasi(token);
    }

    public LiveData<NotifikasiGetResponse> getNotifikasi() {
        return notifikasiGetResponseMutableLiveData;
    }
}