// Generated code from Butter Knife. Do not modify!
package com.ouapproj.ShakJoRDVapp.bottomSheetFragment;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.ouapproj.ShakJoRDVapp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CreateTaskBottomSheetFragment_ViewBinding implements Unbinder {
  private CreateTaskBottomSheetFragment target;

  @UiThread
  public CreateTaskBottomSheetFragment_ViewBinding(CreateTaskBottomSheetFragment target,
      View source) {
    this.target = target;

    target.addTaskTitle = Utils.findRequiredViewAsType(source, R.id.addTaskTitle, "field 'addTaskTitle'", EditText.class);
    target.addTaskDescription = Utils.findRequiredViewAsType(source, R.id.addTaskDescription, "field 'addTaskDescription'", EditText.class);
    target.taskDate = Utils.findRequiredViewAsType(source, R.id.taskDate, "field 'taskDate'", EditText.class);
    target.taskTime = Utils.findRequiredViewAsType(source, R.id.taskTime, "field 'taskTime'", EditText.class);
    target.taskEvent = Utils.findRequiredViewAsType(source, R.id.taskEvent, "field 'taskEvent'", EditText.class);
    target.addTask = Utils.findRequiredViewAsType(source, R.id.addTask, "field 'addTask'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CreateTaskBottomSheetFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.addTaskTitle = null;
    target.addTaskDescription = null;
    target.taskDate = null;
    target.taskTime = null;
    target.taskEvent = null;
    target.addTask = null;
  }
}
