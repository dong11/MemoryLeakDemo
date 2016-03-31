package com.dong.huang.memoryleakdemo;

import android.content.Context;

/**
 * Created by dong on 16/3/31.
 * 单例造成的内存泄漏
 */
public class SingleInstance {
    //由于单例的静态特性使得单例的生命周期一样长，
    //这就说明了如果一个对象已经不需要使用，而单例的对象还持有该对象的引用，
    // 那么这个对象将不能被正常回收，从而导致内存泄漏
    // 解决：使用Application的Context来代替
    private static SingleInstance instance;
    private Context context;

    private SingleInstance(Context context) {
        this.context = context;
    }

    public static SingleInstance getInstance(Context context) {
        if (instance != null) {
            //使用getApplicationContext来代替context
            instance = new SingleInstance(context.getApplicationContext());
        }
        return instance;
    }
}
