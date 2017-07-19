package com.github.deskid.focusreader.api;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.IOException;

import retrofit2.Response;

public class ApiResponse<T> {
    @Nullable
    public String errorMessage;
    @Nullable
    public int code;
    @Nullable
    public T data;

    public ApiResponse(Throwable throwable) {
        code = 500;
        data = null;
        errorMessage = throwable.getMessage();
    }

    public ApiResponse(Response<T> response) {
        code = response.code();
        if (response.isSuccessful()) {
            data = response.body();
            errorMessage = "";
        } else {
            String message = null;
            if (response.errorBody() != null) {
                try {
                    message = response.errorBody().string();
                } catch (IOException e) {
                    //// TODO: 6/22/17
                    e.printStackTrace();
                }
            }
            errorMessage = TextUtils.isEmpty(message) || TextUtils.getTrimmedLength(message) == 0 ? response.message() : message;
            data = null;
        }
    }
}
