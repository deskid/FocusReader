package com.github.deskid.focusreader.api;

import android.support.annotation.Nullable;

public class Resource<T> {
    public int error;
    @Nullable
    public String msg;
    @Nullable
    public T data;

    public Resource(int error, @Nullable T data, @Nullable String message) {
        this.error = error;
        this.data = data;
        this.msg = message;
    }

    public static <T> Resource<T> success(@Nullable T data) {
        return new Resource<>(0, data, null);
    }

    public static <T> Resource<T> error(String msg, @Nullable T data) {
        return new Resource<>(-1, data, msg);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resource<?> resource = (Resource<?>) o;

        if (error != resource.error) return false;
        if (msg != null ? !msg.equals(resource.msg) : resource.msg != null) return false;
        return data != null ? data.equals(resource.data) : resource.data == null;
    }

    @Override
    public int hashCode() {
        int result = error;
        result = 31 * result + (msg != null ? msg.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "error=" + error +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
