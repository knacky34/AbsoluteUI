/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.absoluteui.font;

import java.nio.ByteBuffer;

public class TextMeshData {
	public final ByteBuffer data;
	public final float sx, sy;

	TextMeshData(ByteBuffer data, float sx, float sy){
		this.data = data;
		this.sx = sx;
		this.sy = sy;
	}
}
