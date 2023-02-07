package com.bagushikano.sikedatmobile.adapter.kematian;

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
import com.bagushikano.sikedatmobile.activity.mutasi.kematian.KematianAjuanDetailActivity;
import com.bagushikano.sikedatmobile.activity.mutasi.kematian.KematianDetailActivity;
import com.bagushikano.sikedatmobile.model.kematian.Kematian;
import com.bagushikano.sikedatmobile.util.ChangeDateTimeFormat;

import java.util.ArrayList;

public class KematianListAdapter extends RecyclerView.Adapter<KematianListAdapter.ViewHolder> {
    private Context mContext;
    private final ArrayList<Kematian> kematianArrayList;
    private int position;
    private final String KEMATIAN_DETAIL_KEY = "KEMATIAN_DETAIL_KEY";
    private final String FLAG_KEMATIAN_DETAIL_KEY = "KEMATIAN";

    public KematianListAdapter(Context context, ArrayList<Kematian> kematianArrayList) {
        mContext = context;
        this.kematianArrayList = kematianArrayList;
    }

    @NonNull
    @Override
    public KematianListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.kematian_list_card_layout, parent, false);
        return new KematianListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KematianListAdapter.ViewHolder holder, int position) {
        holder.kematianNamaCacahKrama.setText(kematianArrayList.get(holder.getAdapterPosition()).getCacahKramaMipil().getPenduduk().getNama());
        if ((kematianArrayList.get(holder.getAdapterPosition()).getNomorAktaKematian() != null)) {
            holder.kematianNoAktaKemaitan.setText(kematianArrayList.get(holder.getAdapterPosition()).getNomorAktaKematian());
        }
        else {
            holder.kematianNoAktaKemaitan.setText("-");
        }
        holder.kematianTanggal.setText(ChangeDateTimeFormat.changeDateFormat(kematianArrayList.get(holder.getAdapterPosition()).getTanggalKematian()));

        if (kematianArrayList.get(holder.getAdapterPosition()).getStatus() == 1) {
            holder.kematianStatusAjuan.setText("Sah");
            holder.kematianStatusAjuan.setTextColor(mContext.getColor(R.color.green));
        }
        holder.kematianDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Kematian kematian = kematianArrayList.get(holder.getAdapterPosition());
                Intent kelahiranDetail = new Intent(mContext, KematianDetailActivity.class);
                kelahiranDetail.putExtra(KEMATIAN_DETAIL_KEY, kematian.getId());
                kelahiranDetail.putExtra(FLAG_KEMATIAN_DETAIL_KEY, 0);
                mContext.startActivity(kelahiranDetail);
            }
        });
    }


    @Override
    public int getItemCount() {
        return kematianArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView kematianNamaCacahKrama, kematianNoAktaKemaitan, kematianTanggal, kematianStatusAjuan;
        private final Button kematianDetail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            kematianNamaCacahKrama = itemView.findViewById(R.id.kematian_ajuan_nama);
            kematianNoAktaKemaitan = itemView.findViewById(R.id.kematian_ajuan_no_akta_kematian);
            kematianTanggal = itemView.findViewById(R.id.kematian_ajuan_tanggal_kematian);
            kematianStatusAjuan = itemView.findViewById(R.id.kematian_ajuan_status);
            kematianDetail = itemView.findViewById(R.id.kematian_ajuan_detail);
        }
    }
}