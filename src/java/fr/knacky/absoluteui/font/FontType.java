/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.absoluteui.font;

import fr.knacky.absoluteui.Gui;
import org.joml.Vector4f;

public class FontType {
	private final int textureAtlas;
	private final TextMeshCreator loader;
	private final Vector4f widthEdge;

	public FontType(int textureAtlas, String fontRes) {
		this.textureAtlas = textureAtlas;
		this.loader = new TextMeshCreator(fontRes);
		this.widthEdge = loader.getMetaData().getWidthEdge();
	}

	public TextMeshData loadText(String text) {
		return loader.createTextMesh(text);
	}

	public float computeTextWidth(String text, float fontSize) {
		return loader.calculateTextMeshWidth(text) * fontSize / Gui.abuiGetAspectRatio();
	}

	public int getTextureAtlas() {
		return textureAtlas;
	}

	public Vector4f getWidthEdge() {
		return widthEdge;
	}
}
