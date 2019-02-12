package fr.knacky.absoluteui.callback;

import fr.knacky.absoluteui.view.ClickableView;

public interface PressedCallback extends ActionCallback {
  void onPressed(ClickableView view, boolean pressed);
}
