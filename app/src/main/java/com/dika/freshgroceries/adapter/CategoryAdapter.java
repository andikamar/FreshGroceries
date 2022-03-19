package com.dika.freshgroceries.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dika.freshgroceries.R;
import com.dika.freshgroceries.model.CategoryModel;

import java.util.List;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    //Deklarasi Variable
    private List<CategoryModel> items;
    private CategoryAdapter.onSelectData onSelectData;
    private int positionSelected = -1;


    //listener dari adapter
    public interface onSelectData {
        void onSelected(CategoryModel categoryModel);
    }

    //Membuat Konstruktor, untuk menerima input dari Data
    public CategoryAdapter(List<CategoryModel> items, CategoryAdapter.onSelectData xSelectData) {
        this.items = items;
        this.onSelectData = xSelectData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (positionSelected == position) {
            holder.linearLayout.setBackgroundResource(R.drawable.bg_category);
            holder.tvMainData.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.linearLayout.setBackgroundResource(R.drawable.bg_category_noselected);
            holder.tvMainData.setTextColor(Color.parseColor("#A09B9B"));
        }
        final CategoryModel data = items.get(position);
        holder.tvMainData.setText(data.getTxtName());
        holder.tvMainData.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                positionSelected = position;
                onSelectData.onSelected(data);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvMainData;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            tvMainData = itemView.findViewById(R.id.tvCategories);
            linearLayout = itemView.findViewById(R.id.linearCategory);
        }
    }
}
