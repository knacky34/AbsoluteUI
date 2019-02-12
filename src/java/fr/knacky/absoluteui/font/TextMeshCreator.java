/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.absoluteui.font;

import fr.knacky.absoluteui.util.Loader;
import java.util.ArrayList;

public class TextMeshCreator {
  static final float LINE_HEIGHT = 0.03f;
  static final int ASCII_SPACE = 32;
  private static final int ASCII_NEWLINE = 10;

  private MetaFile metaData;

  TextMeshCreator(String metaRes) {
    metaData = new MetaFile(metaRes);
  }

  TextMeshData createTextMesh(String text, float fontSize) {
    ArrayList<Character> characters = createStructure(text);
    return createQuadVertices(fontSize, characters);
  }

  public MetaFile getMetaData() {
    return metaData;
  }

  private ArrayList<Character> createStructure(String text) {
    ArrayList<Character> characters = new ArrayList<>();
    char[] chars = text.toCharArray();

    for (char c : chars) {
      int ascii = (int) c;
      if (ascii == ASCII_SPACE) {
        characters.add(new Character(32,0,0,0,0,0,0,0,0,30));
        continue;
      } else if (ascii == ASCII_NEWLINE) {
        characters.add(new Character(10,0,0,0,0,0,0,0,0,0));
        continue;
      }
      Character character = metaData.getCharacter(ascii);
      characters.add(character);
    }
    return characters;
  }

  private TextMeshData createQuadVertices(float fontSize, ArrayList<Character> characters) {
    float curserX = 0f;
    float curserY = 0f;
    float maxCursorX = 0f;

    ArrayList<Float> vertices = new ArrayList<>();
    ArrayList<Float> textureCoords = new ArrayList<>();

    for (Character letter : characters) {
      if (letter.getId() == ASCII_SPACE) {
        curserX += metaData.getSpaceWidth() * fontSize;
        continue;
      } else if (letter.getId() == ASCII_NEWLINE) {
        curserX = 0f;
        curserY += LINE_HEIGHT * fontSize;
        continue;
      }
      addVerticesForCharacter(curserX, curserY, letter, fontSize, vertices);
      addTexCoords(textureCoords, letter.getxTextureCoord(), letter.getyTextureCoord(), letter.getXMaxTextureCoord(), letter.getYMaxTextureCoord());
      curserX += letter.getxAdvance() * fontSize;
      if (curserX > maxCursorX) {
        maxCursorX = curserX;
      }
    }

    curserY += LINE_HEIGHT * fontSize;

    return new TextMeshData(Loader.listToByteBuffer(vertices, textureCoords), maxCursorX * 2f, curserY * 2f);
  }

  private void addVerticesForCharacter(double curserX, double curserY, Character character, double fontSize, ArrayList<Float> vertices) {
    double x = curserX + (character.getxOffset() * fontSize);
    double y = curserY + (character.getyOffset() * fontSize);
    double maxX = x + (character.getSizeX() * fontSize);
    double maxY = y + (character.getSizeY() * fontSize);
    double properX = (2 * x) - 1;
    double properY = (-2 * y) + 1;
    double properMaxX = (2 * maxX) - 1;
    double properMaxY = (-2 * maxY) + 1;
    addVertices(vertices, properX, properY, properMaxX, properMaxY);
  }

  private static void addVertices(ArrayList<Float> vertices, double x, double y, double maxX, double maxY) {
    vertices.add((float) x);
    vertices.add((float) y);
    vertices.add((float) x);
    vertices.add((float) maxY);
    vertices.add((float) maxX);
    vertices.add((float) maxY);
    vertices.add((float) maxX);
    vertices.add((float) maxY);
    vertices.add((float) maxX);
    vertices.add((float) y);
    vertices.add((float) x);
    vertices.add((float) y);
  }

  private static void addTexCoords(ArrayList<Float> texCoords, double x, double y, double maxX, double maxY) {
    texCoords.add((float) x);
    texCoords.add((float) y);
    texCoords.add((float) x);
    texCoords.add((float) maxY);
    texCoords.add((float) maxX);
    texCoords.add((float) maxY);
    texCoords.add((float) maxX);
    texCoords.add((float) maxY);
    texCoords.add((float) maxX);
    texCoords.add((float) y);
    texCoords.add((float) x);
    texCoords.add((float) y);
  }
}
