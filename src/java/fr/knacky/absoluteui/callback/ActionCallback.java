/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.absoluteui.callback;

import fr.knacky.absoluteui.view.ClickableView;

public interface ActionCallback {
  void onActionPerformed(ClickableView view, boolean hovered, boolean pressed);
}
