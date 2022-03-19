package com.dika.freshgroceries.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.dika.freshgroceries.R;
import com.dika.freshgroceries.data.DatabaseHandler;
import com.dika.freshgroceries.model.Constants;
import com.dika.freshgroceries.model.FruitModel;
import com.dika.freshgroceries.model.Grocery;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder>implements Filterable {


    //Deklarasi Variable
    private List<FruitModel> dataFruit;
    private final Context context;
    protected final List<FruitModel> originalData;
    private DatabaseHandler db;



    //Membuat Konstruktor, untuk menerima input dari Database
    public FruitAdapter(List<FruitModel> dataFruit, Context context) {
        this.originalData = dataFruit;
        this.dataFruit = dataFruit;
        this.context = context;
    }

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    class ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView fotoFruit;
        private final TextView namaFruit;
        private final TextView hargaFruit;
        private final RatingBar ratingBar;
        private final Button btnbuy;


        public ViewHolder(View itemView) {
            super(itemView);

            fotoFruit = itemView.findViewById(R.id.imgFruit);
            namaFruit = itemView.findViewById(R.id.tvNameFruit);
            hargaFruit = itemView.findViewById(R.id.priceFruit);
            ratingBar = itemView.findViewById(R.id.rating);
            btnbuy = itemView.findViewById(R.id.btn_buy);

            db = new DatabaseHandler(context);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fruit, parent, false);
        return new FruitAdapter.ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView")
    final int position) {
        final String nama = dataFruit.get(position).getNameFruit();
        final String harga = dataFruit.get(position).getPriceFruit();

        Glide.with(context).load(dataFruit.get(position).getImgFruit())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(true)
                .dontAnimate()
                .override(Target.SIZE_ORIGINAL)
                .encodeFormat(Bitmap.CompressFormat.PNG)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .into(holder.fotoFruit);

        holder.namaFruit.setText(nama);
        double formatHarga = Double.parseDouble(harga);
        holder.hargaFruit.setText(Constants.formatRupiah((long) formatHarga));

        float newValue = (float) dataFruit.get(position).getRatingFruit();
        holder.ratingBar.setNumStars(5);
        holder.ratingBar.setStepSize((float) 0.5);
        holder.ratingBar.setRating(newValue);

        holder.btnbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Grocery grocery = new Grocery();

                grocery.setName(nama);
                grocery.setQuantity(1);
                grocery.setPrice(Double.parseDouble(harga));
                grocery.setRating(dataFruit.get(position).getRatingFruit());
                grocery.setImage(dataFruit.get(position).getImgFruit());

                //Save to DB
                db.addGrocery(grocery);

                Snackbar.make(view, "Berhasil menambahkan produk ke keranjang", Snackbar.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataFruit.size();
    }

    //filter utk searchview by keyword
    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressLint("NotifyDataSetChanged")
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                dataFruit = (List<FruitModel>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<FruitModel> filteredResults;
                if (constraint.length() == 0) {
                    filteredResults = originalData;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toUpperCase().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }
    protected List<FruitModel> getFilteredResults(String constraint) {
        List<FruitModel> results = new ArrayList<>();

        for (FruitModel item : originalData) {
            if (item.getNameFruit().toUpperCase().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }
}
