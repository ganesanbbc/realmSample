package com.example.varshika.myapplication;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private LinearLayout viewById;
    private LinearLayout targetContainer;
    private Realm realm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.myimage1).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.myimage2).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.myimage3).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.myimage4).setOnTouchListener(new MyTouchListener());

        viewById = (LinearLayout) findViewById(R.id.maincontainer);
        targetContainer = (LinearLayout) findViewById(R.id.maincontainer);
        viewById.setOnDragListener(new MyDragListener());

        findViewById(R.id.container1).setOnDragListener(new MyDragListener());
        findViewById(R.id.container2).setOnDragListener(new MyDragListener());
        findViewById(R.id.container3).setOnDragListener(new MyDragListener());
        findViewById(R.id.container4).setOnDragListener(new MyDragListener());


    }

    public void checkResult(View view) {
        startService(new Intent(this, MyIntentService.class));
    }

    public void checkResult1(View view) {
        realm = Realm.getDefaultInstance();
        final RealmResults<Dog> puppies = realm.where(Dog.class).findAll();
        System.out.println("::::::puppies::::"+puppies.size());
    }

    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    view.startDragAndDrop(data, shadowBuilder, view, 0);
                } else {
                    view.startDrag(data, shadowBuilder, view, 0);
                }
                return true;
            } else {
                return false;
            }
        }
    }

    class MyDragListener implements View.OnDragListener {
        Drawable enterShape = getResources().getDrawable(
                R.drawable.shape_dragtarget);
        Drawable normalShape = getResources().getDrawable(R.drawable.shape_exit);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    // Invalidate the view to force a redraw in the new tint
                    v.setBackgroundDrawable(enterShape);
                    v.invalidate();
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundDrawable(normalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    View view = (View) event.getLocalState();
                    ViewGroup parentContainer = (ViewGroup) view.getParent();
                    parentContainer.removeView(view);
                    parentContainer.invalidate();
                    LinearLayout targetContainer = (LinearLayout) v;

                    if (targetContainer.getChildCount() > 0) {
                        View oldView = targetContainer.getChildAt(0);
                        targetContainer.removeView(oldView);
                        viewById.addView(oldView);
                        viewById.invalidate();
                    }


                    targetContainer.addView(view);

                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundDrawable(normalShape);
                    v.invalidate();
                default:
                    break;
            }
            return true;
        }
    }
}
