// Generated code from Butter Knife. Do not modify!
package com.ouapproj.ShakJoRDVapp.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.ouapproj.ShakJoRDVapp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AlarmActivity_ViewBinding implements Unbinder {
  private AlarmActivity target;

  @UiThread
  public AlarmActivity_ViewBinding(AlarmActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AlarmActivity_ViewBinding(AlarmActivity target, View source) {
    this.target = target;

    target.imageView = Utils.findRequiredViewAsType(source, R.id.imageView, "field 'imageView'", ImageView.class);
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    target.description = Utils.findRequiredViewAsType(source, R.id.description, "field 'description'", TextView.class);
    target.timeAndData = Utils.findRequiredViewAsType(source, R.id.timeAndData, "field 'timeAndData'", TextView.class);
    target.closeButton = Utils.findRequiredViewAsType(source, R.id.closeButton, "field 'closeButton'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    AlarmActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageView = null;
    target.title = null;
    target.description = null;
    target.timeAndData = null;
    target.closeButton = null;
  }
}
