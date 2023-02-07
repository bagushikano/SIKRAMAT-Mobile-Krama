package com.bagushikano.sikedatmobile.adapter.kematian;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bagushikano.sikedatmobile.R;
import com.bagushikano.sikedatmobile.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobile.model.master.Penduduk;

import java.util.ArrayList;
import java.util.List;

public class KematianAnggotaKeluargaSelectionAdapter extends ArrayAdapter<CacahKramaMipil> {
    private List<CacahKramaMipil> cacahKramaMipilList = new ArrayList<CacahKramaMipil>();

    public KematianAnggotaKeluargaSelectionAdapter(@NonNull Context context, @NonNull List<CacahKramaMipil> cacahKramaMipilList) {
        super(context, 0, cacahKramaMipilList);
        this.cacahKramaMipilList = cacahKramaMipilList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.dropdown_menu_item, parent, false
            );
        }
        TextView kabupatenName = convertView.findViewById(R.id.auto_complete_text);
        kabupatenName.setText(cacahKramaMipilList.get(position).getPenduduk().getNama());
        return convertView;
    }
    @Override
    public int getCount() {
        return cacahKramaMipilList.size();
    }

    @Nullable
    @Override
    public CacahKramaMipil getItem(int position) {
        return cacahKramaMipilList.get(position);
    }
}
