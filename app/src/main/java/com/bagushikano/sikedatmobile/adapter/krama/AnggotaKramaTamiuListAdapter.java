package com.bagushikano.sikedatmobile.adapter.krama;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bagushikano.sikedatmobile.R;
import com.bagushikano.sikedatmobile.activity.cacahkrama.CacahKramaTamiuDetailActivity;
import com.bagushikano.sikedatmobile.model.cacahkrama.CacahKramaTamiu;
import com.bagushikano.sikedatmobile.model.krama.AnggotaKramaTamiu;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AnggotaKramaTamiuListAdapter extends RecyclerView.Adapter<AnggotaKramaTamiuListAdapter.ViewHolder> {
    private Context mContext;
    private final ArrayList<AnggotaKramaTamiu> anggotaKramaTamiuArrayList;
    private int position;
    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";

    public AnggotaKramaTamiuListAdapter(Context context, ArrayList<AnggotaKramaTamiu> anggotaKramaTamiuArrayList) {
        mContext = context;
        this.anggotaKramaTamiuArrayList = anggotaKramaTamiuArrayList;
    }

    @NonNull
    @Override
    public AnggotaKramaTamiuListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.anggota_krama_tamiu_card_layout, parent, false);
        return new AnggotaKramaTamiuListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnggotaKramaTamiuListAdapter.ViewHolder holder, int position) {
        String namaFormated = anggotaKramaTamiuArrayList.get(holder.getAdapterPosition()).getCacahKramaTamiu().getPenduduk().getNama();
        if (anggotaKramaTamiuArrayList.get(holder.getAdapterPosition()).getCacahKramaTamiu().getPenduduk().getGelarDepan() != null) {
            namaFormated = String.format("%s %s",
                    anggotaKramaTamiuArrayList.get(holder.getAdapterPosition()).getCacahKramaTamiu().getPenduduk().getGelarDepan(),
                    anggotaKramaTamiuArrayList.get(holder.getAdapterPosition()).getCacahKramaTamiu().getPenduduk().getNama()
            );
        }
        if (anggotaKramaTamiuArrayList.get(holder.getAdapterPosition()).getCacahKramaTamiu().getPenduduk().getGelarBelakang() != null) {
            namaFormated = String.format("%s %s",
                    namaFormated,
                    anggotaKramaTamiuArrayList.get(holder.getAdapterPosition()).getCacahKramaTamiu().getPenduduk().getGelarBelakang()
            );
        }
        holder.kramaTamiuName.setText(namaFormated);
        holder.kramaTamiuAnggotaHubungan.setText(anggotaKramaTamiuArrayList.get(holder.getAdapterPosition()).getStatusHubungan());
        holder.kramaTamiuDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CacahKramaTamiu cacahKramaTamiu = anggotaKramaTamiuArrayList.get(holder.getAdapterPosition()).getCacahKramaTamiu();
                Intent kramaDetail = new Intent(mContext, CacahKramaTamiuDetailActivity.class);
                Gson gson = new Gson();
                String kramaJson = gson.toJson(cacahKramaTamiu);
                kramaDetail.putExtra(KRAMA_DETAIL_KEY, cacahKramaTamiu.getId());
                mContext.startActivity(kramaDetail);
            }
        });
    }


    @Override
    public int getItemCount() {
        return anggotaKramaTamiuArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView kramaTamiuName, kramaTamiuAnggotaHubungan;
        private final Button kramaTamiuDetailButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            kramaTamiuName = itemView.findViewById(R.id.anggota_krama_nama_text);
            kramaTamiuAnggotaHubungan = itemView.findViewById(R.id.anggota_krama_tamiu_hubungan_text);
            kramaTamiuDetailButton = itemView.findViewById(R.id.anggota_krama_tamiu_detail_button);
        }
    }
}
