package com.dhht.nana.util;

/**
 * @author Tyhj
 * @date 2019/7/1
 */

public class MyInt {
    int hashCode;

    public MyInt(int hashCode) {
        this.hashCode = hashCode;
    }


    public int getHashCode() {
        return hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj instanceof MyInt) && ((MyInt) obj).hashCode == hashCode) {
            return true;
        }
        return false;
    }


    @Override
    public int hashCode() {
        return hashCode;
    }
}
