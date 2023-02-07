package com.bagushikano.sikedatmobile.adapter.kelahiran;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bagushikano.sikedatmobile.R;
import com.bagushikano.sikedatmobile.model.master.Penduduk;

import java.util.ArrayList;
import java.util.List;

public class KelahiranOrangTuaSelectionAdapter extends ArrayAdapter<Penduduk> {
    private List<Penduduk> pendudukList = new ArrayList<Penduduk>();

    public KelahiranOrangTuaSelectionAdapter(@NonNull Context context, @NonNull List<Penduduk> pendudukList) {
        super(context, 0, pendudukList);
        this.pendudukList = pendudukList;
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
        kabupatenName.setText(pendudukList.get(position).getNama());
        return convertView;
    }
    @Override
    public int getCount() {
        return pendudukList.size();
    }

    @Nullable
    @Override
    public Penduduk getItem(int position) {
        return pendudukList.get(position);
    }
}
