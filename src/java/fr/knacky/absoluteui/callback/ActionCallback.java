package fr.knacky.absoluteui.callback;

import fr.knacky.absoluteui.view.ClickableView;

public interface ActionCallback {
  void onActionPerformed(ClickableView view, boolean hovered, boolean pressed);
}
