package fr.knacky.absoluteui.view;

import com.sun.istack.internal.Nullable;
import fr.knacky.absoluteui.callback.ActionCallback;
import org.joml.Vector2f;

public abstract class ClickableView extends View {
  protected boolean hovered = false;
  protected boolean pressed = false;

  protected ActionCallback callback;

  public ClickableView(Vector2f position, Vector2f size, @Nullable ActionCallback callback) {
    super(position, size);
    this.callback = callback;
  }

  public abstract void update(float x, float y, boolean mouse);


  public boolean isHovered() {
    return hovered;
  }

  public boolean isPressed() {
    return pressed;
  }


  public void setHovered(boolean hovered) {
    this.hovered = hovered;
  }

  public void setPressed(boolean pressed) {
    this.pressed = pressed;
  }

  public void setCallback(ActionCallback callback) {
    this.callback = callback;
  }
}
