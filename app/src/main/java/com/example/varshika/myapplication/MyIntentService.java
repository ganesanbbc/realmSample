package com.example.varshika.myapplication;

import android.app.IntentService;
import android.content.Intent;

import io.realm.Realm;

public class MyIntentService extends IntentService {


    private Realm realm;

    public MyIntentService() {
        super("");

    }

    public MyIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Use them like regular java objects
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(2000);
                insertAction();
            } catch (InterruptedException e) {

            }

            System.out.println("Attempt:::" + i);
        }


    }

    private void insertAction() {
        // Get a Realm instance for this thread
        realm = Realm.getDefaultInstance();

// Asynchronously update objects on a background thread
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Dog dog = realm.createObject(Dog.class);
                dog.setName("Rex");
                dog.setAge(1);
                System.out.println("inserted");
            }
        });

        realm.close();
    }

}
