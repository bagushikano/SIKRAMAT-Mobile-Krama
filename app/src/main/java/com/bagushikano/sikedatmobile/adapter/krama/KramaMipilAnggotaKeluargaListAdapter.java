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
import com.bagushikano.sikedatmobile.activity.cacahkrama.CacahKramaMipilDetailActivity;
import com.bagushikano.sikedatmobile.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobile.model.krama.AnggotaKramaMipil;
import com.google.gson.Gson;

import java.util.ArrayList;

public class KramaMipilAnggotaKeluargaListAdapter extends RecyclerView.Adapter<KramaMipilAnggotaKeluargaListAdapter.ViewHolder> {
    private Context mContext;
    private final ArrayList<AnggotaKramaMipil> anggotaKramaMipilArrayList;
    private int position;
    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";

    public KramaMipilAnggotaKeluargaListAdapter(Context context, ArrayList<AnggotaKramaMipil> anggotaKramaMipilArrayList) {
        mContext = context;
        this.anggotaKramaMipilArrayList = anggotaKramaMipilArrayList;
    }

    @NonNull
    @Override
    public KramaMipilAnggotaKeluargaListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.anggota_krama_mipil_card_layout, parent, false);
        return new KramaMipilAnggotaKeluargaListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KramaMipilAnggotaKeluargaListAdapter.ViewHolder holder, int position) {
        String namaFormated = anggotaKramaMipilArrayList.get(holder.getAdapterPosition()).getCacahKramaMipil().getPenduduk().getNama();
        if (anggotaKramaMipilArrayList.get(holder.getAdapterPosition()).getCacahKramaMipil().getPenduduk().getGelarDepan() != null) {
            namaFormated = String.format("%s %s",
                    anggotaKramaMipilArrayList.get(holder.getAdapterPosition()).getCacahKramaMipil().getPenduduk().getGelarDepan(),
                    anggotaKramaMipilArrayList.get(holder.getAdapterPosition()).getCacahKramaMipil().getPenduduk().getNama()
            );
        }
        if (anggotaKramaMipilArrayList.get(holder.getAdapterPosition()).getCacahKramaMipil().getPenduduk().getGelarBelakang() != null) {
            namaFormated = String.format("%s %s",
                    namaFormated,
                    anggotaKramaMipilArrayList.get(holder.getAdapterPosition()).getCacahKramaMipil().getPenduduk().getGelarBelakang()
            );
        }
        holder.kramaMipilName.setText(namaFormated);
        holder.kramaMipilAnggotaHubungan.setText(anggotaKramaMipilArrayList.get(holder.getAdapterPosition()).getStatusHubungan());
        holder.kramaMipilDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CacahKramaMipil cacahKramaMipil = anggotaKramaMipilArrayList.get(holder.getAdapterPosition()).getCacahKramaMipil();
                Intent kramaDetail = new Intent(mContext, CacahKramaMipilDetailActivity.class);
                Gson gson = new Gson();
                String kramaJson = gson.toJson(cacahKramaMipil);
                kramaDetail.putExtra(KRAMA_DETAIL_KEY, cacahKramaMipil.getId());
                mContext.startActivity(kramaDetail);
            }
        });
    }


    @Override
    public int getItemCount() {
        return anggotaKramaMipilArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView kramaMipilName, kramaMipilAnggotaHubungan;
        private final Button kramaMipilDetailButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            kramaMipilName = itemView.findViewById(R.id.anggota_krama_nama_text);
            kramaMipilAnggotaHubungan = itemView.findViewById(R.id.anggota_krama_mipil_hubungan_text);
            kramaMipilDetailButton = itemView.findViewById(R.id.anggota_krama_mipil_detail_button);
        }
    }
}
