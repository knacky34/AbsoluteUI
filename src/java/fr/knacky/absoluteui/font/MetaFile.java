/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.absoluteui.font;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import org.joml.Vector4f;

public class MetaFile {
	static final float LINE_HEIGHT = 0.03f;

	static final int ASCII_SPACE = 32;
	static final int ASCII_NEWLINE = 10;
	static final int ASCII_UNKNOWN = 127;

	private float size;
	private int paddingWith;
	private int imageWidth, imageHeight;

	private HashMap<Integer, Character> characters = new HashMap<>();
	private float spaceWidth;
	private Vector4f widthEdge = new Vector4f();

	MetaFile(String resource) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(resource)))) {
			HashMap<String, String> variables = new HashMap<>();
			String line;

			while ((line = reader.readLine()) != null) {
				if (line.startsWith("char")) {
					getVariables(variables, line);

					Character c = loadCharacter(variables);
					if (c != null) {
						characters.put(c.getId(), c);
					}
				} else if (line.startsWith("info")) {
					getVariables(variables, line);

					int[] padding = getVariableia("padding", variables);
					paddingWith = padding[1] + padding[3];
					size = LINE_HEIGHT / (float) (getVariablei("lineHeight", variables) - padding[0] - padding[2]);

					imageWidth = getVariablei("scaleW", variables);
					imageHeight = getVariablei("scaleH", variables);

					widthEdge.x = getVariablef("minWidth", variables);
					widthEdge.y = getVariablef("maxWidth", variables);
					widthEdge.z = getVariablef("minEdge", variables);
					widthEdge.w = getVariablef("maxEdge", variables);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getVariables(HashMap<String, String> variables, String line) {
		variables.clear();

		for (String part : line.split(" ")) {
			String[] pair = part.split("=");
			if (pair.length == 2) {
				variables.put(pair[0], pair[1]);
			}
		}
	}

	private int getVariablei(String variable, HashMap<String, String> variables) {
		return Integer.parseInt(variables.get(variable));
	}

	private int[] getVariableia(String variable, HashMap<String, String> variables) {
		String[] numbers = variables.get(variable).split(",");
		int[] values = new int[numbers.length];
		for (int i = 0; i < values.length; i++) {
			values[i] = Integer.parseInt(numbers[i]);
		}
		return values;
	}

	private float getVariablef(String variable, HashMap<String, String> variables) {
		return Float.parseFloat(variables.get(variable));
	}

	private Character loadCharacter(HashMap<String, String> variables) {
		int id = getVariablei("id", variables);
		if (id == ASCII_SPACE) {
			spaceWidth = (getVariablei("xadvance", variables) - paddingWith) * size;
			return null;
		}
		float xTextureCoord =  (float) (getVariablei("x", variables)) / imageWidth;
		float yTextureCoord =  (float) (getVariablei("y", variables)) / imageHeight;
		int width = getVariablei("width", variables);
		int height = getVariablei("height", variables);
		float xTextureCoordMax = xTextureCoord + (float) (width) / imageWidth;
		float yTextureCoordMax = yTextureCoord + (float) (height) / imageHeight;
		float xSize = width * size;
		float ySize = height * size;
		float xOffset = getVariablei("xoffset", variables) * size;
		float yOffset = getVariablei("yoffset", variables) * size;
		float xAdvance = (getVariablei("xadvance", variables) - paddingWith) * size;
		return new Character(id, xTextureCoord, yTextureCoord, xTextureCoordMax, yTextureCoordMax, xOffset, yOffset, xSize, ySize, xAdvance);
	}

	Character getCharacter(int ascii) {
		return characters.get(ascii);
	}

	float getSpaceWidth() {
		return spaceWidth;
	}

	Vector4f getWidthEdge() {
		return widthEdge;
	}
}
