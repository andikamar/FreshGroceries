package com.dika.freshgroceries.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.dika.freshgroceries.model.Grocery;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class CartAdapter  extends RecyclerView.Adapter<CartAdapter.ViewHolder> {


    //Deklarasi Variable
    private List<Grocery> groceryList;
    private final Context context;
    private CartListener listener;




    //Membuat Konstruktor, untuk menerima input dari Data
    public CartAdapter(List<Grocery> groceries, Context context) {
        this.groceryList = groceries;
        this.context = context;
    }

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    static class ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView fotoFruit;
        private final TextView namaFruit;
        private final TextView hargaFruit;
        private final RatingBar ratingBar;
        private final TextView tvCount;
        private final ImageView delete;
        private final TextView imgMin;
        private final TextView imgPlus;


        public ViewHolder(View itemView) {
            super(itemView);

            fotoFruit = itemView.findViewById(R.id.imgCart);
            namaFruit = itemView.findViewById(R.id.tvNameCart);
            hargaFruit = itemView.findViewById(R.id.priceCart);
            ratingBar = itemView.findViewById(R.id.ratingCart);
            tvCount = itemView.findViewById(R.id.tvCount);

            delete = itemView.findViewById(R.id.delete);

            imgMin = itemView.findViewById(R.id.imageMin);
            imgPlus = itemView.findViewById(R.id.imagePlus);

        }
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final CartAdapter.ViewHolder holder, @SuppressLint("RecyclerView")
    final int position) {
        final Grocery cart = groceryList.get(position);

        final String nama = cart.getName();

        Glide.with(context).load(cart.getImage())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(true)
                .dontAnimate()
                .override(Target.SIZE_ORIGINAL)
                .encodeFormat(Bitmap.CompressFormat.PNG)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .into(holder.fotoFruit);

        holder.namaFruit.setText(nama);
        holder.hargaFruit.setText(Constants.formatRupiah((long) cart.getPrice()));

        float newValue = (float) cart.getRating();
        holder.ratingBar.setNumStars(5);
        holder.ratingBar.setStepSize((float) 0.5);
        holder.ratingBar.setRating(newValue);
        holder.tvCount.setText(String.valueOf(cart.getQuantity()));

        holder.imgMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Update item
                DatabaseHandler db = new DatabaseHandler(context);
                cart.setName(cart.getName());
                cart.setQuantity(cart.getQuantity()-1);
                db.updateGrocery(cart);
                notifyItemChanged(holder.getAdapterPosition(), cart);

                if(cart.getQuantity() <= 0) {
                    db.deleteGrocery(groceryList.get(position).getId());
                    groceryList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, groceryList.size());
                }
            }
        });

        holder.imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Update item
                DatabaseHandler db = new DatabaseHandler(context);
                final Grocery grocery= groceryList.get(position);
                grocery.setName(grocery.getName());
                grocery.setQuantity(grocery.getQuantity()+1);
                db.updateGrocery(grocery);
                notifyItemChanged(holder.getAdapterPosition(), grocery);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Remove item
                DatabaseHandler db = new DatabaseHandler(context);
                db.deleteGrocery(groceryList.get(position).getId());
                groceryList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, groceryList.size());
                Snackbar.make(view, "Berhasil menghapus item", Snackbar.LENGTH_LONG).show();
            }
        });
        if(listener != null)
            listener.onChange(groceryList);
    }

    @Override
    public int getItemCount() {
        return groceryList.size();
    }

    //listener dari adapter
    public void setCartListener(CartListener listener)
    {
        this.listener = listener;
    }

    public interface CartListener {
        void onChange(List<Grocery> list);
    }
}
