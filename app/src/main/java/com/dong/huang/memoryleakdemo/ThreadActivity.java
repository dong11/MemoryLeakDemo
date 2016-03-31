package com.dong.huang.memoryleakdemo;

import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.lang.ref.WeakReference;

/**
 * 线程造成的内存泄漏
 * 异步任务(AsyncTask)和Runnable都是一个匿名内部类，因此它们对当前Activity都有一个隐式引用，
 * 如果Activity在销毁之前，任务还未完成，那么将导致Activity的内存资源无法回收，造成内存泄漏
 */
public class ThreadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        new Thread(new MyRunnable()).start();
        new MyAsyncTask(this).execute();
    }

    //解决：使用静态内部类的方式
    //这样就避免了Activity的内存资源泄漏，当然在Activity销毁的时候也应当取消相应的任务
    static class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<Context> weakReference;

        public MyAsyncTask(Context context) {
            weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Void... params) {
            SystemClock.sleep(10000);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ThreadActivity activity = (ThreadActivity) weakReference.get();
            if (activity != null) {
                //...
            }
        }
    }
    static class MyRunnable implements Runnable{
        @Override
        public void run() {
            SystemClock.sleep(10000);
        }
    }
}
