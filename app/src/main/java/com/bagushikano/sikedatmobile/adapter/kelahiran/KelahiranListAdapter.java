package com.bagushikano.sikedatmobile.adapter.kelahiran;

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
import com.bagushikano.sikedatmobile.activity.mutasi.kelahiran.KelahiranAjuanDetailActivity;
import com.bagushikano.sikedatmobile.activity.mutasi.kelahiran.KelahiranDetailActivity;
import com.bagushikano.sikedatmobile.model.kelahiran.Kelahiran;

import java.util.ArrayList;

public class KelahiranListAdapter extends RecyclerView.Adapter<KelahiranListAdapter.ViewHolder> {
    private Context mContext;
    private final ArrayList<Kelahiran> kelahiranArrayList;
    private int position;
    private final String KELAHIRAN_DETAIL_KEY = "KELAHIRAN_DETAIL_KEY";
    private final String FLAG_KELAHIRAN_DETAIL_KEY = "KELAHIRAN";

    public KelahiranListAdapter(Context context, ArrayList<Kelahiran> kelahiranArrayList) {
        mContext = context;
        this.kelahiranArrayList = kelahiranArrayList;
    }

    @NonNull
    @Override
    public KelahiranListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.kelahiran_pengajuan_card_layout, parent, false);
        return new KelahiranListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KelahiranListAdapter.ViewHolder holder, int position) {
        holder.kelahiranNamaAnak.setText(kelahiranArrayList.get(holder.getAdapterPosition()).getCacahKramaMipil().getPenduduk().getNama());
        if (kelahiranArrayList.get(holder.getAdapterPosition()).getNomorAktaKelahiran() != null) {
            holder.kelahiranNoAktaAnak.setText(kelahiranArrayList.get(holder.getAdapterPosition()).getNomorAktaKelahiran());
        }
        else {
            holder.kelahiranNoAktaAnak.setText("-");
        }
        if (kelahiranArrayList.get(holder.getAdapterPosition()).getCacahKramaMipil().getPenduduk().getNik() != null ) {
            holder.kelahiranNikAnak.setText(kelahiranArrayList.get(holder.getAdapterPosition()).getCacahKramaMipil().getPenduduk().getNik());
        }
        else {
            holder.kelahiranNikAnak.setText("-");
        }


        if (kelahiranArrayList.get(holder.getAdapterPosition()).getStatus() == 1) {
            holder.kelahiranStatusAjuan.setText("Sah");
            holder.kelahiranStatusAjuan.setTextColor(mContext.getColor(R.color.green));
        }

        holder.kelahiranDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Kelahiran kelahiran = kelahiranArrayList.get(holder.getAdapterPosition());
                Intent kelahiranDetail = new Intent(mContext, KelahiranDetailActivity.class);
                kelahiranDetail.putExtra(KELAHIRAN_DETAIL_KEY, kelahiran.getId());
                kelahiranDetail.putExtra(FLAG_KELAHIRAN_DETAIL_KEY, 0);
                mContext.startActivity(kelahiranDetail);
            }
        });
    }


    @Override
    public int getItemCount() {
        return kelahiranArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView kelahiranNamaAnak, kelahiranNikAnak, kelahiranNoAktaAnak, kelahiranStatusAjuan;
        private final Button kelahiranDetail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            kelahiranNamaAnak = itemView.findViewById(R.id.kelahiran_ajuan_nama);
            kelahiranStatusAjuan = itemView.findViewById(R.id.kelahiran_ajuan_status);
            kelahiranNikAnak = itemView.findViewById(R.id.kelahiran_ajuan_nik);
            kelahiranNoAktaAnak = itemView.findViewById(R.id.kelahiran_ajuan_no_akta_kelahiran);
            kelahiranDetail = itemView.findViewById(R.id.kelahiran_ajuan_detail);
        }
    }
}