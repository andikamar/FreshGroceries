package com.dika.freshgroceries;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {
    private Context context;
    ImageView imgprofile;
    TextView tvName,tvUsername, tvEmail, tvMobilephone, tvlocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.context = getContext();
        assert context != null;

        imgprofile = view.findViewById(R.id.imgProfile);
        tvName = view.findViewById(R.id.tvNameuser);
        tvUsername= view.findViewById(R.id.tv_username);
        tvEmail= view.findViewById(R.id.tv_email);
        tvMobilephone = view.findViewById(R.id.tv_mobilePhone);
        tvlocation = view.findViewById(R.id.tv_location);

        getDataProfile();

    }

    private void getDataProfile() {
        //get data from api
        AndroidNetworking
                .get("https://randomuser.me/api")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("SetTextI18n")
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
                                tvName.setText(jSONObject.getJSONObject("name").getString("first")+jSONObject.getJSONObject("name").getString("last"));
                                tvUsername.setText(jSONObject.getJSONObject("login").getString("username"));
                                tvEmail.setText(jSONObject.getString("email"));
                                tvMobilephone.setText(jSONObject.getString("phone"));
                                tvlocation.setText(jSONObject.getJSONObject("location").getString("city")+
                                "\n" + jSONObject.getJSONObject("location").getString("state") +" " + jSONObject.getJSONObject("location").getString("country")
                                + " - "+ jSONObject.getJSONObject("location").getString("postcode"));
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