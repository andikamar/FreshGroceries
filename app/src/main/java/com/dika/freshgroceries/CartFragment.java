package com.dika.freshgroceries;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dika.freshgroceries.adapter.CartAdapter;
import com.dika.freshgroceries.data.DatabaseHandler;
import com.dika.freshgroceries.model.Constants;
import com.dika.freshgroceries.model.Grocery;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    private Context context;
    private List<Grocery> listfromdb;
    private List<Grocery> groceryList;
    private DatabaseHandler db;
    RecyclerView rvCart;
    CartAdapter cartAdapter;
    TextView tvCount, tvTotal;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.context = getContext();
        assert context != null;


        tvCount = view.findViewById(R.id.countFruit);
        tvTotal = view.findViewById(R.id.totalPrice);

        rvCart = view.findViewById(R.id.rvCart);
        rvCart.setLayoutManager(new LinearLayoutManager(context));
        rvCart.setHasFixedSize(true);

        db = new DatabaseHandler(context);


        listfromdb = new ArrayList<>();
        groceryList = new ArrayList<>();

        getDataCart();
    }

    @SuppressLint("SetTextI18n")
    private void getDataCart() {
        // Get items from database
        listfromdb = db.getAllGroceries();

        groceryList.clear();
        for (Grocery c : listfromdb) {
            Grocery grocery = new Grocery();
            grocery.setName(c.getName());
            grocery.setQuantity(c.getQuantity());
            grocery.setId(c.getId());
            grocery.setDateItemAdded(c.getDateItemAdded());
            grocery.setPrice(c.getPrice());
            grocery.setRating(c.getRating());
            grocery.setImage(c.getImage());
            groceryList.add(grocery);
        }
        if (groceryList.size() > 0){
            cartAdapter = new CartAdapter(groceryList, context);
            rvCart.setAdapter(cartAdapter);

            //listener from adapter
            cartAdapter.setCartListener(new CartAdapter.CartListener() {
                @Override
                public void onChange(List<Grocery> list) {
                    if (list.size()>0){
                        double mtotal = 0;
                        int count = 0;
                        for (int i = 0; i < list.size(); i++) {
                            count += list.get(i).getQuantity();
                            double sub = (list.get(i).getPrice()*count);
                            mtotal += sub;
                        }
                        tvCount.setText("Total " + count + " Item");
                        tvTotal.setText(Constants.formatRupiah((long) mtotal));
                    } else {
                        tvCount.setText("Total " + 0 + " Item");
                        tvTotal.setText(Constants.formatRupiah((long) 0));
                    }
                }
            });
        } else {
            tvCount.setText("Total " + 0 + " Item");
            tvTotal.setText(Constants.formatRupiah((long) 0));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //change status bar
        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getActivity().getResources().getColor(R.color.colorPrimary));
    }
}