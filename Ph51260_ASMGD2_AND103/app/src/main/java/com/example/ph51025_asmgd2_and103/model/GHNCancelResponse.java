package com.example.ph51025_asmgd2_and103.model;

public class GHNCancelResponse {
    private String order_code, result, message;

    public GHNCancelResponse() {
    }

    public GHNCancelResponse(String order_code, String result, String message) {
        this.order_code = order_code;
        this.result = result;
        this.message = message;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
