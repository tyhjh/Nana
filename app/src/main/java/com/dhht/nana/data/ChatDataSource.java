package com.dhht.nana.data;

import com.dhht.nana.app.Const;
import com.dhht.nana.network.Result;
import com.dhht.nana.network.Retrofite;
import com.dhht.nana.network.RetrofiteApi;
import com.orhanobut.logger.Logger;

import java.util.Random;

import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Tyhj
 * @date 2019/7/1
 */

public class ChatDataSource {

    private static String resultMsg;
    private static final int WAIT_TIME = 4000;

    String originName = "小豪豪";
    String replaceName = "小娜";
    String replaceName2 = "娜娜";


    RetrofiteApi retrofiteApi;

    public ChatDataSource() {
        Retrofite.init();
        retrofiteApi = Retrofite.getInstance().create(RetrofiteApi.class);
    }

    long startTime;

    public String getReply(String msg, String userId) {
        resultMsg = null;
        startTime = System.currentTimeMillis();
        msg = msg.replaceAll(replaceName, originName);
        msg = msg.replaceAll(replaceName2, originName);
        RequestData requestData = new RequestData(userId, msg);
        retrofiteApi.getReply(requestData.app_id, requestData.time_stamp,
                requestData.nonce_str, requestData.sign, requestData.session, requestData.question)
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<Result<Replay>>() {
                    @Override
                    public void accept(Result<Replay> result) throws Exception {
                        Logger.e(result.toString());
                        if (result != null && result.getRet() == 0) {
                            resultMsg = result.getData().getAnswer().replaceAll(originName, replaceName);
                        }
                    }
                });
        long startTime = System.currentTimeMillis();
        while (resultMsg == null && System.currentTimeMillis() - startTime < WAIT_TIME) {
        }
        if (resultMsg == null) {
            String repalyTxt = Const.Msg.msgs[new Random(System.currentTimeMillis()).nextInt(Const.Msg.msgs.length - 1)];
            repalyTxt = repalyTxt + Const.Msg.AUTO_REPALY_DEFAULT_LAST;
            resultMsg = repalyTxt;
        }
        return resultMsg;
    }
}
