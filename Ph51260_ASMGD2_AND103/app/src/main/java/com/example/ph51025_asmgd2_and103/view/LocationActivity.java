package com.example.ph51025_asmgd2_and103.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ph51025_asmgd2_and103.adapter.Adapter_Item_District_Select_GHN;
import com.example.ph51025_asmgd2_and103.adapter.Adapter_Item_Province_Select_GHN;
import com.example.ph51025_asmgd2_and103.adapter.Adapter_Item_Ward_Select_GHN;
import com.example.ph51025_asmgd2_and103.databinding.ActivityLocationBinding;
import com.example.ph51025_asmgd2_and103.model.District;
import com.example.ph51025_asmgd2_and103.model.DistrictRequest;
import com.example.ph51025_asmgd2_and103.model.Fruit;
import com.example.ph51025_asmgd2_and103.model.GHNItem;
import com.example.ph51025_asmgd2_and103.model.GHNOrderRequest;
import com.example.ph51025_asmgd2_and103.model.GHNOrderRespone;
import com.example.ph51025_asmgd2_and103.model.Order;
import com.example.ph51025_asmgd2_and103.model.Province;
import com.example.ph51025_asmgd2_and103.model.ResponseGHN;
import com.example.ph51025_asmgd2_and103.model.Ward;
import com.example.ph51025_asmgd2_and103.services.GHNRequest;
import com.example.ph51025_asmgd2_and103.services.HttpRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationActivity extends AppCompatActivity {
    ActivityLocationBinding binding;
    GHNRequest ghnRequest;
    HttpRequest httpRequest;
    Adapter_Item_Province_Select_GHN adapterItemProvinceSelectGhn;
    Adapter_Item_District_Select_GHN adapterItemDistrictSelectGhn;
    Adapter_Item_Ward_Select_GHN adapterItemWardSelectGhn;
    ArrayList<District> list_District = new ArrayList<>();
    ArrayList<Province> list_Province = new ArrayList<>();
    ArrayList<Ward> list_Ward = new ArrayList<>();

    private String WardCode;
    private int DistrictID, ProvinceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityLocationBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        ghnRequest = new GHNRequest();
        httpRequest = new HttpRequest();
        configGHN();
        userListener();
    }
    private void userListener() {
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (WardCode.equals("")) return;

                Fruit fruit = (Fruit) getIntent().getExtras().getSerializable("item");
                Log.d("zzzzzz", "fruit: " + fruit);
                GHNItem ghnItem = new GHNItem();

                ghnItem.setName(fruit.getName());
                ghnItem.setPrice(Integer.parseInt(fruit.getPrice()));
                ghnItem.setCode(fruit.get_id());
                ghnItem.setQuantity(1);
                ghnItem.setWeight(50);

                ArrayList<GHNItem> items = new ArrayList<>();
                items.add(ghnItem);
                String name = binding.edName.getText().toString().trim();
                String phone = binding.edPhone.getText().toString().trim();
                String location = binding.edLocation.getText().toString().trim();
                Log.d("zzzzzzzzzzzz", "war: " + WardCode + " dis : " +DistrictID);

                GHNOrderRequest abc = new GHNOrderRequest(2,"","KHONGCHOXEMHANG","0987654321",
                "so nha 10 ngo 382 son tay , ha noi"
                ,"oke", phone,location,WardCode, DistrictID,200000,
                "Theo New York Times",1,items.size(),10,10,10000000,0,2
                ,items);


                GHNOrderRequest ghnOrderRequest = new GHNOrderRequest(
                        name,
                        phone,
                        location,
                        WardCode,
                        DistrictID,
                        items
                );

                Log.d("zzzzzzz", "onClick: "+abc.getReturn_address());
                ghnRequest.getApiService().GHNOrder(abc).enqueue(responseOrder);
            }
        });
    }

    Callback<ResponseGHN<GHNOrderRespone>> responseOrder = new Callback<ResponseGHN<GHNOrderRespone>>() {
        @Override
        public void onResponse(Call<ResponseGHN<GHNOrderRespone>> call, Response<ResponseGHN<GHNOrderRespone>> response) {
            Log.d("zzzzzz", "onResponse: "+response.code());
            if (response.isSuccessful()){
                if (response.body().getCode()==200) {
                    Toast.makeText(LocationActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                    Order order = new Order();
                    order.setOrder_code(response.body().getData().getOrder_code());
                    Log.d("zzzzzzzz", "onResponse getOrder_code : "+response.body().getData().getOrder_code());

                    order.setId_user(getSharedPreferences("INFO", MODE_PRIVATE).getString("id",""));
                    Log.d("zzzzzzzz", "onResponse INFO : "+getSharedPreferences("INFO", MODE_PRIVATE).getString("id",""));
                    httpRequest.callAPI().order(order).enqueue(responseOrderDatabase);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseGHN<GHNOrderRespone>> call, Throwable t) {
            Log.d("zzz  ", "onFailure: faild order  " + t.getMessage());
        }
    };
    Callback<com.example.ph51025_asmgd2_and103.model.Response<Order>> responseOrderDatabase = new Callback<com.example.ph51025_asmgd2_and103.model.Response<Order>>() {
        @Override
        public void onResponse(Call<com.example.ph51025_asmgd2_and103.model.Response<Order>> call, Response<com.example.ph51025_asmgd2_and103.model.Response<Order>> response) {
            if (response.isSuccessful()){
                if (response.body().getStatus() == 200){
                    Toast.makeText(LocationActivity.this, "Cảm ơn đã đặt hàng", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }

        @Override
        public void onFailure(Call<com.example.ph51025_asmgd2_and103.model.Response<Order>> call, Throwable t) {

        }
    };

    private void configGHN() {
        Call<ResponseGHN<ArrayList<Province>>> call = ghnRequest.getApiService().getListProvince();
        call.enqueue(new Callback<ResponseGHN<ArrayList<Province>>>() {
            @Override
            public void onResponse(Call<ResponseGHN<ArrayList<Province>>> call, Response<ResponseGHN<ArrayList<Province>>> response) {
                if(response.isSuccessful()){
                    if(response.body().getCode() == 200){
                        list_Province = new ArrayList<>(response.body().getData());
                        Log.e("TAG", "Province List: " + list_Province.size());
                        adapterItemProvinceSelectGhn = new Adapter_Item_Province_Select_GHN(LocationActivity.this,list_Province);
                        binding.spProvince.setAdapter(adapterItemProvinceSelectGhn);
                    }else{
                        Log.e("TAG", "API Call Failed");
                    }
                } else {
                    Log.e("TAG", "API Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseGHN<ArrayList<Province>>> call, Throwable t) {
                Log.e("TAG", "Failure: " + t.getMessage());
            }
        });

        binding.spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProvinceID = ((Province) parent.getAdapter().getItem(position)).getProvinceID();
                DistrictRequest districtRequest = new DistrictRequest(ProvinceID);
                ghnRequest.getApiService().getListDistrict(districtRequest).enqueue(new Callback<ResponseGHN<ArrayList<District>>>() {
                    @Override
                    public void onResponse(Call<ResponseGHN<ArrayList<District>>> call, Response<ResponseGHN<ArrayList<District>>> response) {
                        if(response.isSuccessful()){
                            list_District = response.body().getData();
                            adapterItemDistrictSelectGhn = new Adapter_Item_District_Select_GHN(LocationActivity.this,list_District);
                            binding.spDistrict.setAdapter(adapterItemDistrictSelectGhn);
                        }else{
                            Log.e("TAG", "District API Failed");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseGHN<ArrayList<District>>> call, Throwable t) {
                        Log.e("TAG", "District API Failure: " + t.getMessage());
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Ward
        binding.spDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                District district = list_District.get(position);
                DistrictID = district.getDistrictID();
                ghnRequest.getApiService().getListWard(district.getDistrictID()).enqueue(new Callback<ResponseGHN<ArrayList<Ward>>>() {
                    @Override
                    public void onResponse(Call<ResponseGHN<ArrayList<Ward>>> call, Response<ResponseGHN<ArrayList<Ward>>> response) {
                        if(response.isSuccessful()){
                            list_Ward = new ArrayList<>(response.body().getData());
                            adapterItemWardSelectGhn = new Adapter_Item_Ward_Select_GHN(LocationActivity.this, list_Ward);
                            binding.spWard.setAdapter(adapterItemWardSelectGhn);
                            Log.d("WARD_LIST", "Ward List: " + list_Ward.size());
                            adapterItemWardSelectGhn.notifyDataSetChanged();
                        } else {
                            Log.e("TAG", "Ward API Failed");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseGHN<ArrayList<Ward>>> call, Throwable t) {
                        Log.e("TAG", "Ward API Failure: " + t.getMessage());
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }





}