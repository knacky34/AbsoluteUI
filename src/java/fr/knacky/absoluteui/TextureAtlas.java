package fr.knacky.absoluteui;

public class TextureAtlas {
  private int texture;
  private float[] x;
  private float[] y;
  private float size;

  public TextureAtlas(int texture, int rows) {
    this.texture = texture;
    this.x = new float[rows * rows];
    this.y = new float[x.length];
    this.size = 1f / rows;

    for (int i = 0; i < x.length; i++) {
      x[i] = (float) (i % rows) / (float) rows;
      y[i] = (float) (i / rows) / (float) rows;
    }
  }

  public int getTexture() {
    return texture;
  }

  public float getX(int index) {
    return x[index];
  }

  public float getY(int index) {
    return y[index];
  }

  public float getSize() {
    return size;
  }
}
