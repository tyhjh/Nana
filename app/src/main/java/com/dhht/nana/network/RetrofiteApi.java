package com.dhht.nana.network;

import com.dhht.nana.data.Replay;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author Tyhj
 * @date 2019/7/1
 */

public interface RetrofiteApi {


    /**
     * 获取聊天回复
     *
     * @param app_id
     * @param time_stamp
     * @param nonce_str
     * @param sign
     * @param session
     * @param question
     * @return
     */
    @FormUrlEncoded
    @POST("nlp_textchat")
    Observable<Result<Replay>> getReply(
            @Field("app_id") String app_id,
            @Field("time_stamp") String time_stamp,
            @Field("nonce_str") String nonce_str,
            @Field("sign") String sign,
            @Field("session") String session,
            @Field("question") String question);


}
