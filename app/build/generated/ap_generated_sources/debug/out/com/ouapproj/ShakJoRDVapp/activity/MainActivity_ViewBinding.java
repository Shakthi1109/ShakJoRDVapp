// Generated code from Butter Knife. Do not modify!
package com.ouapproj.ShakJoRDVapp.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.ouapproj.ShakJoRDVapp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding implements Unbinder {
  private MainActivity target;

  @UiThread
  public MainActivity_ViewBinding(MainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainActivity_ViewBinding(MainActivity target, View source) {
    this.target = target;

    target.taskRecycler = Utils.findRequiredViewAsType(source, R.id.taskRecycler, "field 'taskRecycler'", RecyclerView.class);
    target.addTask = Utils.findRequiredViewAsType(source, R.id.addTask, "field 'addTask'", TextView.class);
    target.noDataImage = Utils.findRequiredViewAsType(source, R.id.noDataImage, "field 'noDataImage'", ImageView.class);
    target.calendar = Utils.findRequiredViewAsType(source, R.id.calendar, "field 'calendar'", ImageView.class);
    target.mic = Utils.findRequiredViewAsType(source, R.id.mic, "field 'mic'", ImageView.class);
    target.paint = Utils.findRequiredViewAsType(source, R.id.paint, "field 'paint'", ImageView.class);
    target.appbg = Utils.findRequiredViewAsType(source, R.id.appbg, "field 'appbg'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.taskRecycler = null;
    target.addTask = null;
    target.noDataImage = null;
    target.calendar = null;
    target.mic = null;
    target.paint = null;
    target.appbg = null;
  }
}
