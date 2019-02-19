/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.absoluteui.callback;

import fr.knacky.absoluteui.view.Checkbox;

public interface CheckCallback extends ActionCallback {
  void onChecked(Checkbox view, boolean checked);
}
