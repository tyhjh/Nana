package com.dhht.nana.app;

/**
 * @author Tyhj
 * @date 2019/6/30
 */

public interface Const {


    int RUN_MODEL_QUICK_JUMP = 0;
    int RUN_MODEL_SAVE_PIC = 1;
    //测试图片
    int RUN_MODEL_TEST_PIC= 2;
    //跳一次
    int RUN_MODEL_SINGLE_JUMP = 3;


    interface Msg {

        String AUTO_REPALY_DEFAULT = "主人暂时不在呢，可以@我和我一起聊天 --Nana";

        String AUTO_REPALY_DEFAULT_LAST = "，恩恩";

        String WX_AUTO_REPALY_TXT = "wx_auto_repaly_txt";


        String[] msgs = new String[]{
                "٩(•̤̀ᵕ•̤́๑)ᵒᵏᵎᵎᵎᵎ",
                "( ˘ ³˘)♡",
                "*\\(^o^)/*",
                "(*๓´╰╯`๓)♡", "(＾▽＾)", "٩(๑^o^๑)۶", "(-^〇^-)", "(σ°∀°)σ..:*☆",
                "✧٩(ˊωˋ*)و✧", "(´▽｀)ノ♪", "(*¯︶¯*)", "(๑`灬´๑)", "(๑•̀ㅁ•́ฅ)",
                "(ฅ´ω`ฅ)", "<(￣︶￣)/", "(๑ºั╰╯ºั๑)", "٩۹(๑•̀ω•́ ๑)۶", "(๑ºั╰╯ºั๑)",
                "(๑ʘ̅ д ʘ̅๑)!!!", "d(ŐдŐ๑)", "ヾ(。￣□￣)ツ", "ヾ(Ő∀Ő3)ノ", "( •̥́ ˍ •̀ू )",
                "(๑′°︿°๑)", "(*'へ'*)", "(ಥ﹏ಥ)", "( •̩̩̩̩＿•̩̩̩̩ )", "╮(╯_╰)╭",
                "(๑‾᷅^‾᷅๑)", "ヽ(｀д´)ノ", "(•́へ•́ ╬)", "٩(๑`^´๑)۶", "(•̀へ •́ ╮ )",
                "( ˘ ³˘)♡", "٩(๛ ˘ ³˘)۶", "(´ ▽｀).。ｏ♡", "ε٩(๑> ₃ <)7з", "(´∀｀)♡",
                "ू(ʚ̴̶̷́ .̠ ʚ̴̶̷̥̀ ू)", "✯⸜(ّᶿ̷ധّᶿ̷)⸝✯", "(｡•̀ᴗ-)✧", "( •́ _ •̀)", "o(･౪･´o ≡ ò౪ó))—̳͟͞͞o我不听",
                "ଘ(੭ˊ꒳ˋ)੭✧"
        };


    }



    String screenWidth="screenWidth";
    String screenHeight="screenHeight";



}
