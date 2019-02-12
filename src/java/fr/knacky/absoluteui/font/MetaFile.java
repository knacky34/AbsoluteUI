/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.absoluteui.font;

import fr.knacky.absoluteui.Gui;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class MetaFile {
	private static final int PAD_TOP = 0;
	private static final int PAD_LEFT = 1;
	private static final int PAD_BOTTOM = 2;
	private static final int PAD_RIGHT = 3;

	private static final int DESIRED_PADDING = 8;

	private static final String SPLITTER = " ";
	private static final String NUMBER_SEPARATOR = ",";

	private double verticalPerPixelSize;
	private double horizontalPerPixelSize;
	private double spaceWidth;
	private int[] padding;
	private int paddingWidth;
	private int paddingHeight;

	public float minSize = 1.0f, maxSize = 50.0f;
	public float minWidth = 0.4f, maxWidth = 0.55f;
	public float minEdge = 0.2f, maxEdge = 0.04f;

	private HashMap<Integer, Character> metaData = new HashMap<>();
	private HashMap<String, String> values = new HashMap<>();

	MetaFile(String resource) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(resource)))) {
			loadPaddingData(reader);
			loadLineSizes(reader);
			int imageWidth = getValueOfVariableInt("scaleW");
			loadFontWidthAndEdge(reader);
			loadCharacterData(imageWidth, reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		values.clear();
	}

	double getSpaceWidth() {
		return spaceWidth;
	}

	Character getCharacter(int ascii) {
		return metaData.get(ascii);
	}

	private boolean processNextLine(BufferedReader reader) throws IOException {
		values.clear();
		String line = reader.readLine();
		if (line == null) {
			return false;
		}
		for (String part : line.split(SPLITTER)) {
			String[] valuePairs = part.split("=");
			if (valuePairs.length == 2) {
				values.put(valuePairs[0], valuePairs[1]);
			}
		}
		return true;
	}

	private int getValueOfVariableInt(String variable) {
		return Integer.parseInt(values.get(variable));
	}

	private int[] getValuesOfVariableInt(String variable) {
		String[] numbers = values.get(variable).split(NUMBER_SEPARATOR);
		int[] actualValues = new int[numbers.length];
		for (int i = 0; i < actualValues.length; i++) {
			actualValues[i] = Integer.parseInt(numbers[i]);
		}
		return actualValues;
	}

	private float[] getValuesOfVariableFloat(String variable) {
		String var = values.get(variable);
		if (var == null) return null;
		String[] numbers =	var.split(NUMBER_SEPARATOR);
		float[] actualValues = new float[numbers.length];
		for (int i = 0; i < actualValues.length; i++) {
			actualValues[i] = Float.parseFloat(numbers[i]);
		}
		return actualValues;
	}

	private void loadPaddingData(BufferedReader reader) throws IOException {
		processNextLine(reader);
		this.padding = getValuesOfVariableInt("padding");
		this.paddingWidth = padding[PAD_LEFT] + padding[PAD_RIGHT];
		this.paddingHeight = padding[PAD_TOP] + padding[PAD_BOTTOM];
	}

	private void loadLineSizes(BufferedReader reader) throws IOException {
		processNextLine(reader);
		int lineHeightPixels = getValueOfVariableInt("lineHeight") - paddingHeight;
		verticalPerPixelSize = TextMeshCreator.LINE_HEIGHT / (double) lineHeightPixels;
		horizontalPerPixelSize = verticalPerPixelSize / Gui.abuiGetAspectRatio();
	}

	private void loadFontWidthAndEdge(BufferedReader reader) throws IOException {
		processNextLine(reader);
		float[] size = getValuesOfVariableFloat("fontsize");
		float[] width = getValuesOfVariableFloat("fontwidth");
		float[] edge = getValuesOfVariableFloat("fontedge");
		if (size != null) {
			minSize = size[0];
			maxSize = size[1];
		}
		if (width != null) {
			minWidth = width[0];
			maxWidth = width[1];
		}
		if (edge != null) {
			minEdge = edge[0];
			maxEdge = edge[1];
		}
	}

	private void loadCharacterData(int imageWidth, BufferedReader reader) throws IOException {
		processNextLine(reader);
		processNextLine(reader);
		while (processNextLine(reader)) {
			Character c = loadCharacter(imageWidth);
			if (c != null) {
				metaData.put(c.getId(), c);
			}
		}
	}

	private Character loadCharacter(int imageSize) {
		int id = getValueOfVariableInt("id");
		if (id == TextMeshCreator.ASCII_SPACE) {
			this.spaceWidth = (getValueOfVariableInt("xadvance") - paddingWidth) * horizontalPerPixelSize;
			return null;
		}
		double xTex = ((double) getValueOfVariableInt("x") + (padding[PAD_LEFT] - DESIRED_PADDING)) / imageSize;
		double yTex = ((double) getValueOfVariableInt("y") + (padding[PAD_TOP] - DESIRED_PADDING)) / imageSize;
		int width = getValueOfVariableInt("width") - (paddingWidth - (2 * DESIRED_PADDING));
		int height = getValueOfVariableInt("height") - ((paddingHeight) - (2 * DESIRED_PADDING));
		double quadWidth = width * horizontalPerPixelSize;
		double quadHeight = height * verticalPerPixelSize;
		double xTexSize = (double) width / imageSize;
		double yTexSize = (double) height / imageSize;
		double xOff = (getValueOfVariableInt("xoffset") + padding[PAD_LEFT] - DESIRED_PADDING) * horizontalPerPixelSize;
		double yOff = (getValueOfVariableInt("yoffset") + (padding[PAD_TOP] - DESIRED_PADDING)) * verticalPerPixelSize;
		double xAdvance = (getValueOfVariableInt("xadvance") - paddingWidth) * horizontalPerPixelSize;
		return new Character(id, xTex, yTex, xTexSize, yTexSize, xOff, yOff, quadWidth, quadHeight, xAdvance);
	}
}
