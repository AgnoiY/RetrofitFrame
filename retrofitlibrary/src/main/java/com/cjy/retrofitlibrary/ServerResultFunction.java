package com.cjy.retrofitlibrary;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * 服务器结果处理函数
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
class ServerResultFunction implements Function<JsonElement, Object> {
    @Override
    public Object apply(@NonNull JsonElement response) throws Exception {
        /*此处不再处理业务相关逻辑交由开发者重写httpCallback*/
        return new Gson().toJson(response);
    }
}