/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.absoluteui.font;

class Character {
	private final int id;
	private final float xTextureCoord;
	private final float yTextureCoord;
	private final float xTextureCoordMax;
	private final float yTextureCoordMax;
	private final float xOffset;
	private final float yOffset;
	private final float xSize;
	private final float ySize;
	private final float xAdvance;

	Character(int id, float xTextureCoord, float yTextureCoord, float xTextureCoordMax, float yTextureCoordMax, float xOffset, float yOffset, float xSize, float ySize, float xAdvance) {
		this.id = id;
		this.xTextureCoord = xTextureCoord;
		this.yTextureCoord = yTextureCoord;
		this.xTextureCoordMax = xTextureCoordMax;
		this.yTextureCoordMax = yTextureCoordMax;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.xSize = xSize;
		this.ySize = ySize;
		this.xAdvance = xAdvance;
	}

	int getId() {
		return id;
	}

	float getxTextureCoord() {
		return xTextureCoord;
	}

	float getyTextureCoord() {
		return yTextureCoord;
	}

	float getxTextureCoordMax() {
		return xTextureCoordMax;
	}

	float getyTextureCoordMax() {
		return yTextureCoordMax;
	}

	float getxOffset() {
		return xOffset;
	}

	float getyOffset() {
		return yOffset;
	}

	float getxSize() {
		return xSize;
	}

	float getySize() {
		return ySize;
	}

	float getxAdvance() {
		return xAdvance;
	}
}
