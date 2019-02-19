package fr.knacky.absoluteui.callback;

import fr.knacky.absoluteui.view.ClickableView;

public interface SlideCallback extends ActionCallback {
  void onValueChanged(ClickableView view, float value);
}
