package com.dika.freshgroceries;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.dika.freshgroceries.adapter.CategoryAdapter;
import com.dika.freshgroceries.adapter.FruitAdapter;
import com.dika.freshgroceries.model.CategoryModel;
import com.dika.freshgroceries.model.FruitModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements CategoryAdapter.onSelectData {
    private Context context;

    List<CategoryModel> categoryModelList = new ArrayList<>();
    CategoryModel categoryModel;
    FruitModel fruitModel;
    List<FruitModel> fruitModelList = new ArrayList<>();
    RecyclerView rvCategory;
    RecyclerView rvFruit;
    FruitAdapter fruitAdapter;
    ImageView imgprofile;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.context = getContext();
        assert context != null;

        TextView tvGrocies = view.findViewById(R.id.tvFreshGrocies);
        String text = "<font color=#FCAF05>Find</font> <font color=#1EA050>Fresh Groceries</font>";
        tvGrocies.setText(Html.fromHtml(text));

        final SearchView searchView = view.findViewById(R.id.searchFruit);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (fruitAdapter != null){
                    fruitAdapter.getFilter().filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (fruitAdapter != null){
                    fruitAdapter.getFilter().filter(newText);
                }
                return false;
            }
        });

        imgprofile = view.findViewById(R.id.imageProfile);

        rvCategory = view.findViewById(R.id.rvCategories);
        rvCategory.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rvCategory.setHasFixedSize(true);

        rvFruit = view.findViewById(R.id.rvdataFruit);
        rvFruit.setLayoutManager(new LinearLayoutManager(context));
        rvFruit.setHasFixedSize(true);

        setMenu();

        getDataProfile();

    }
    private void getDataProfile() {
        //get data from api
        AndroidNetworking
                .get("https://randomuser.me/api")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jSONArray = response.getJSONArray("results");
                            for (int i = 0; i < jSONArray.length(); i++) {
                                JSONObject jSONObject = (JSONObject) jSONArray.get(i);
                                Glide.with(context)
                                        .load(jSONObject.getJSONObject("picture").getString("large"))
                                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                        .skipMemoryCache(true)
                                        .dontAnimate()
                                        .dontTransform()
                                        .priority(com.bumptech.glide.Priority.HIGH)
                                        .override(Target.SIZE_ORIGINAL)
                                        .encodeFormat(Bitmap.CompressFormat.PNG)
                                        .format(DecodeFormat.PREFER_ARGB_8888)
                                        .circleCrop()
                                        .into(imgprofile);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context, anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setMenu() {
        //dummy category
        categoryModel = new CategoryModel("Apple");
        categoryModelList.add(categoryModel);
        categoryModel = new CategoryModel("Orange");
        categoryModelList.add(categoryModel);
        categoryModel = new CategoryModel("Banana");
        categoryModelList.add(categoryModel);

        CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModelList, this);
        rvCategory.setAdapter(categoryAdapter);

    }

    @Override
    public void onSelected(CategoryModel categoryModel) {
        switch (categoryModel.getTxtName()) {
            case "Apple":
                setFruitApple();
                break;
            case "Orange":
                setFruitOrange();
                break;
            case "Banana":
                setFruitBanana();
                break;
        }
    }

    private void setFruitBanana() {
        //dumy data banana
        fruitModelList.clear();
        fruitModel = new FruitModel(R.drawable.bananas, "Sweet Banana Italia", "21000", 4);
        fruitModelList.add(fruitModel);
        fruitModel = new FruitModel(R.drawable.bananas, "Sweet Banana Canada", "15000", 3);
        fruitModelList.add(fruitModel);
        fruitModel = new FruitModel(R.drawable.bananas, "Sweet Banana Japan", "20000", 3);
        fruitModelList.add(fruitModel);
        fruitModel = new FruitModel(R.drawable.bananas, "Sweet Banana Korea", "15000", 2);
        fruitModelList.add(fruitModel);

        fruitAdapter = new FruitAdapter(fruitModelList, context);
        rvFruit.setAdapter(fruitAdapter);
    }

    private void setFruitOrange() {
        //dumy data orange
        fruitModelList.clear();
        fruitModel = new FruitModel(R.drawable.orange, "Sweet Orange Indonesia", "25000", 4);
        fruitModelList.add(fruitModel);
        fruitModel = new FruitModel(R.drawable.orange, "Sweet Orange Canada", "15000", 3);
        fruitModelList.add(fruitModel);
        fruitModel = new FruitModel(R.drawable.orange, "Sweet Orange Japan", "20000", 3);
        fruitModelList.add(fruitModel);
        fruitModel = new FruitModel(R.drawable.orange, "Sweet Orange Korea", "15000", 2);
        fruitModelList.add(fruitModel);

        fruitAdapter = new FruitAdapter(fruitModelList, context);
        rvFruit.setAdapter(fruitAdapter);
    }

    private void setFruitApple() {
        //dummy data apple
        fruitModelList.clear();
        fruitModel = new FruitModel(R.drawable.apple, "Sweet Apple Indonesia", "25000", 4);
        fruitModelList.add(fruitModel);
        fruitModel = new FruitModel(R.drawable.apple, "Sweet Apple Canada", "15000", 3);
        fruitModelList.add(fruitModel);
        fruitModel = new FruitModel(R.drawable.apple, "Sweet Apple Japan", "20000", 3);
        fruitModelList.add(fruitModel);
        fruitModel = new FruitModel(R.drawable.apple, "Sweet Apple India", "10000", 2);
        fruitModelList.add(fruitModel);

        fruitAdapter = new FruitAdapter(fruitModelList, context);
        rvFruit.setAdapter(fruitAdapter);
    }
}