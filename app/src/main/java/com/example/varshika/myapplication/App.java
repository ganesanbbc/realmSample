package com.example.varshika.myapplication;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by varshika on 21/02/17.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
