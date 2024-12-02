package com.example.ph51025_asmgd2_and103.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ph51025_asmgd2_and103.adapter.OrdersAdapter;
import com.example.ph51025_asmgd2_and103.databinding.ActivityOrdersBinding;
import com.example.ph51025_asmgd2_and103.model.GHNCancelRequest;
import com.example.ph51025_asmgd2_and103.model.GHNCancelResponse;
import com.example.ph51025_asmgd2_and103.model.Order;
import com.example.ph51025_asmgd2_and103.model.Response;
import com.example.ph51025_asmgd2_and103.model.ResponseGHN;
import com.example.ph51025_asmgd2_and103.services.GHNRequest;
import com.example.ph51025_asmgd2_and103.services.HttpRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class OrdersActivity extends AppCompatActivity implements OrdersAdapter.OrderClick {
    ActivityOrdersBinding binding;
    ArrayList<Order> orderArrayList = new ArrayList<>();
    OrdersAdapter adapter ;
    private HttpRequest httpRequest;
    private GHNRequest ghnRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityOrdersBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        httpRequest = new HttpRequest();
        ghnRequest = new GHNRequest();
        httpRequest.callAPI().getListOrder(
                getSharedPreferences("INFO",MODE_PRIVATE).getString("id","")
        ).enqueue(responseListOrder);



    userListener();

    }
    private void userListener () {
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    Callback<Response<ArrayList<Order>>> responseListOrder = new Callback<Response<ArrayList<Order>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Order>>> call, retrofit2.Response<Response<ArrayList<Order>>> response) {
            if (response.body().getStatus() == 200) {
                orderArrayList = response.body().getData();
                adapter = new OrdersAdapter(OrdersActivity.this, orderArrayList, OrdersActivity.this);
                binding.rcvOrder.setAdapter(adapter);
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Order>>> call, Throwable t) {
            t.getMessage();
        }
    };

    Callback<ResponseGHN<ArrayList<GHNCancelResponse>>> responseCancelOrder = new Callback<ResponseGHN<ArrayList<GHNCancelResponse>>>() {
        @Override
        public void onResponse(Call<ResponseGHN<ArrayList<GHNCancelResponse>>> call, retrofit2.Response<ResponseGHN<ArrayList<GHNCancelResponse>>> response) {
            Log.d("zzzzzzz", "responseCancelOrder: " + response);
            if (response.isSuccessful()){
                if (response.body().getCode() == 200) {
                    httpRequest.callAPI().deleteOrder(response.body().getData().get(0).getOrder_code())
                            .enqueue(responseDeleteOrderDatabase);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseGHN<ArrayList<GHNCancelResponse>>> call, Throwable t) {
            Log.d("zzzzz", "onFailure: " +t.getMessage());
        }
    };

    Callback<Response<Order>> responseDeleteOrderDatabase = new Callback<Response<Order>>() {
        @Override
        public void onResponse(Call<Response<Order>> call, retrofit2.Response<Response<Order>> response) {
            int x = 2;
            if (response.body().getStatus() == 200) {
                httpRequest.callAPI().getListOrder(
                        getSharedPreferences("INFO",MODE_PRIVATE).getString("id","")
                ).enqueue(responseListOrder);
            }
        }

        @Override
        public void onFailure(Call<Response<Order>> call, Throwable t) {
            Log.d("zzzzz", "onFailure: "+ t.getMessage());
        }
    };

    @Override
    public void deleteOrder(String order_code) {
        ArrayList<String> listCancel = new ArrayList<>();
        listCancel.add(order_code);
        GHNCancelRequest cancelRequest = new GHNCancelRequest();
        cancelRequest.setOrder_code(listCancel);
        Log.d("zzzzz", "deleteOrder: " + cancelRequest.getOrder_code());
        ghnRequest.getApiService().cancelOrder(cancelRequest).enqueue(responseCancelOrder);
    }


}