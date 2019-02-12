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
