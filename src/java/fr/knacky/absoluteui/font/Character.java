/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.absoluteui.font;

class Character {
	private int id;
	private double xTextureCoord;
	private double yTextureCoord;
	private double xMaxTextureCoord;
	private double yMaxTextureCoord;
	private double xOffset;
	private double yOffset;
	private double sizeX;
	private double sizeY;
	private double xAdvance;

	Character(int id, double xTextureCoord, double yTextureCoord, double xTexSize, double yTexSize, double xOffset, double yOffset, double sizeX, double sizeY, double xAdvance) {
		this.id = id;
		this.xTextureCoord = xTextureCoord;
		this.yTextureCoord = yTextureCoord;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.xMaxTextureCoord = xTexSize + xTextureCoord;
		this.yMaxTextureCoord = yTexSize + yTextureCoord;
		this.xAdvance = xAdvance;
	}

	int getId() {
		return id;
	}

	double getxTextureCoord() {
		return xTextureCoord;
	}

	double getyTextureCoord() {
		return yTextureCoord;
	}

	double getXMaxTextureCoord() {
		return xMaxTextureCoord;
	}

	double getYMaxTextureCoord() {
		return yMaxTextureCoord;
	}

	double getxOffset() {
		return xOffset;
	}

	double getyOffset() {
		return yOffset;
	}

	double getSizeX() {
		return sizeX;
	}

	double getSizeY() {
		return sizeY;
	}

	double getxAdvance() {
		return xAdvance;
	}
}
