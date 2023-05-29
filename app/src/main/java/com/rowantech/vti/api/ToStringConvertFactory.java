package com.rowantech.vti.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigDecimal;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by wandhie on 8/6/2017.
 */

public class ToStringConvertFactory extends Converter.Factory {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json");


    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        if (type == String.class) {
            return (Converter<ResponseBody, String>) value -> value.string();
        } else if(type == Integer.class) {
            return (Converter<ResponseBody, BigDecimal>) value -> BigDecimal.valueOf(Double.valueOf(value.string()));
        } else if(type == BigDecimal.class) {
            return (Converter<ResponseBody, BigDecimal>) value -> BigDecimal.valueOf(Double.valueOf(value.string()));
        } else if(type == Double.class) {
            return (Converter<ResponseBody, BigDecimal>) value -> BigDecimal.valueOf(Double.valueOf(value.string()));
        } else if(type == Float.class) {
            return (Converter<ResponseBody, BigDecimal>) value -> BigDecimal.valueOf(Double.valueOf(value.string()));
        } else if(type == Boolean.class) {
            return (Converter<ResponseBody, Boolean>) value -> Boolean.valueOf(value.string());
        }
        return null;
    }


    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations, Retrofit retrofit) {

        if (String.class.equals(type)) {
            return (Converter<String, RequestBody>) value -> RequestBody.create(MEDIA_TYPE, value);
        }
        return null;
    }
}
