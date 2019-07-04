package com.dhht.nana.util;

import com.yorhp.crashlibrary.CrashUtil;
import com.yorhp.crashlibrary.saveErro.ISaveErro;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaveErroToSDCard implements ISaveErro {
    private static final String FILE_NAME = "crash";
    private static final String FILE_NAME_SUFFIX = ".txt";
    private String saveErroDir;

    public SaveErroToSDCard(String saveErroDir) {
        this.saveErroDir = saveErroDir;
        File saveErroDirFile = new File(saveErroDir);
        if (!saveErroDirFile.exists()) {
            saveErroDirFile.mkdirs();
        }

    }

    @Override
    public void saveErroMsg(Throwable throwable) {
        try {
            long current = System.currentTimeMillis();
            String time = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(current));
            File file = new File(this.saveErroDir + FILE_NAME + time + FILE_NAME_SUFFIX);
            file.createNewFile();
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            pw.println(time);
            pw.println(CrashUtil.PHONE_INFO);
            throwable.printStackTrace(pw);
            pw.close();
        } catch (Exception var7) {
            var7.printStackTrace();
        }

    }
}