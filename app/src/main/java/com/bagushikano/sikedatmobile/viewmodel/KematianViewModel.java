package com.bagushikano.sikedatmobile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bagushikano.sikedatmobile.model.kelahiran.KelahiranAjuanGetResponse;
import com.bagushikano.sikedatmobile.model.kelahiran.KelahiranGetResponse;
import com.bagushikano.sikedatmobile.model.kematian.KematianAjuanGetResponse;
import com.bagushikano.sikedatmobile.model.kematian.KematianGetResponse;
import com.bagushikano.sikedatmobile.repository.KelahiranRepository;
import com.bagushikano.sikedatmobile.repository.KematianRepository;

public class KematianViewModel extends ViewModel {
    private MutableLiveData<KematianGetResponse> kematianGetResponseMutableLiveData;
    private MutableLiveData<KematianAjuanGetResponse> kematianAjuanGetResponseMutableLiveData;

    private KematianRepository kematianRepository;

    public void init(String token) {
        if (kematianGetResponseMutableLiveData != null ||
                kematianAjuanGetResponseMutableLiveData != null) {
            return;
        }
        kematianRepository = kematianRepository.getInstance();
        kematianGetResponseMutableLiveData = kematianRepository.getKematian(token);
        kematianAjuanGetResponseMutableLiveData = kematianRepository.getKematianAjuanData(token);
    }
    public void getData(String token) {
        kematianGetResponseMutableLiveData = kematianRepository.getKematian(token);
    }

    public LiveData<KematianGetResponse> getKematian() {
        return kematianGetResponseMutableLiveData;
    }

    public void getDataAjuan(String token) {
        kematianAjuanGetResponseMutableLiveData = kematianRepository.getKematianAjuanData(token);
    }


    public LiveData<KematianAjuanGetResponse> getKematianAjuan() {
        return kematianAjuanGetResponseMutableLiveData;
    }
}
