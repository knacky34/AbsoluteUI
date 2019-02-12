package fr.knacky.absoluteui.callback;

import fr.knacky.absoluteui.view.Checkbox;

public interface CheckedCallback extends ActionCallback {
  void onChecked(Checkbox view, boolean checked);
}
