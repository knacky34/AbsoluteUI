package fr.knacky.absoluteui.callback;

import fr.knacky.absoluteui.view.ClickableView;

public interface TextChangeCallback extends ActionCallback {
  void onTextChanged(ClickableView view, String text);
}
