package com.dong.huang.memoryleakdemo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.lang.ref.WeakReference;

public class HandlerActivity extends AppCompatActivity {

    //Handler造成内存泄漏
    //问题：由于mHandler是Handler的非静态匿名内部类的实例，所以它持有外部类Activity的引用
    //而消息队列是在一个Looper线程中不断的轮询处理消息，那么当这个Activity退出时，
    // 消息队列中还有未处理的消息或者正在处理消息，而消息队列中的Message持有mHandler实例的引用，
    // mHandler又持有Activity的引用，所以导致该Activity的内存资源无法及时回收，引发内存泄漏
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    //解决：
    private static MyHandler mMyHandler;
    private static class MyHandler extends Handler {
        private WeakReference<Context> reference;
        public MyHandler(Context context){
            reference = new WeakReference<Context>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            HandlerActivity activity = (HandlerActivity) reference.get();
            if(activity != null){
                //操作Activity
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        SingleInstance instance = SingleInstance.getInstance(this);

        mMyHandler = new MyHandler(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除消息队列中所有消息和所有的Runnable
        mMyHandler.removeCallbacksAndMessages(null);
    }
}
