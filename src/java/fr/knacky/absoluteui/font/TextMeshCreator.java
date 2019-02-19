/*
 * Copyright (c) 2019 - present  Knacky34. All rights reserved.
 * License terms: https://github.com/knacky34/AbsoluteUI/blob/master/LICENSE
 */

package fr.knacky.absoluteui.font;

import fr.knacky.absoluteui.util.Loader;
import java.util.ArrayList;

import static fr.knacky.absoluteui.font.MetaFile.*;

class TextMeshCreator {
  private MetaFile metaData;

  TextMeshCreator(String metaRes) {
    metaData = new MetaFile(metaRes);
  }

  TextMeshData createTextMesh(String text) {
    ArrayList<Character> characters = createStructure(text);
    return createQuadVertices(characters);
  }

  float calculateTextMeshWidth(String text) {
    float cursor = 0f;
    for (int ascii : text.toCharArray()) {
      if (ascii == ASCII_SPACE) {
        cursor += metaData.getSpaceWidth();
        continue;
      }

      Character c = metaData.getCharacter(ascii);
      if (c == null) {
        c = metaData.getCharacter(ASCII_UNKNOWN);
      }
      cursor += c.getxAdvance();
    }
    return cursor;
  }

  private ArrayList<Character> createStructure(String text) {
    ArrayList<Character> characters = new ArrayList<>();

    for (int ascii : text.toCharArray()) {
      if (ascii == ASCII_SPACE) {
        characters.add(new Character(ASCII_SPACE,0,0,0,0,0,0,0,0,0));
        continue;
      } else if (ascii == ASCII_NEWLINE) {
        characters.add(new Character(ASCII_NEWLINE,0,0,0,0,0,0,0,0,0));
        continue;
      }
      Character character = metaData.getCharacter(ascii);
      if (character == null) {
        character = metaData.getCharacter(ASCII_UNKNOWN);
      }
      characters.add(character);
    }
    return characters;
  }

  private TextMeshData createQuadVertices(ArrayList<Character> characters) {
    float curserX = 0f;
    float curserY = 0f;
    float maxCursorX = 0f;

    ArrayList<Float> vertices = new ArrayList<>();
    ArrayList<Float> textureCoords = new ArrayList<>();

    for (Character letter : characters) {
      if (letter.getId() == ASCII_SPACE) {
        curserX += metaData.getSpaceWidth();
      } else if (letter.getId() == ASCII_NEWLINE) {
        curserX = 0f;
        curserY += LINE_HEIGHT;
        continue;
      } else {
        addVerticesForCharacter(curserX, curserY, letter, vertices);
        addTexCoords(textureCoords, letter.getxTextureCoord(), letter.getyTextureCoord(), letter.getxTextureCoordMax(), letter.getyTextureCoordMax());
        curserX += letter.getxAdvance();
      }
      if (curserX > maxCursorX) {
        maxCursorX = curserX;
      }
    }

    curserY += LINE_HEIGHT;

    return new TextMeshData(Loader.listToByteBuffer(vertices, textureCoords), maxCursorX, curserY);
  }

  private static void addVerticesForCharacter(double curserX, double curserY, Character character, ArrayList<Float> vertices) {
    double x = curserX + character.getxOffset();
    double y = curserY + character.getyOffset();
    double maxX = x + character.getxSize();
    double maxY = y + character.getySize();
    addVertices(vertices, x, y, maxX, maxY);
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

  MetaFile getMetaData() {
    return metaData;
  }
}
