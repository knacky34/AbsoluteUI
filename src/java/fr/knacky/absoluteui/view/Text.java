package fr.knacky.absoluteui.view;

import fr.knacky.absoluteui.font.FontType;
import fr.knacky.absoluteui.font.MetaFile;
import fr.knacky.absoluteui.font.TextMeshData;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Text extends View {
  private FontType font;
  private Model model;

  private Vector3f color;

  public float width = 0.4f;
  public float edge = 0.04f;
  public float borderWidth = 0.0f;
  public float borderEdge = 0.1f;

  private Vector3f outlineColor = new Vector3f(1.0f, 1.0f, 1.0f);
  private Vector2f offset = new Vector2f(0.0f, 0.0f);

  public Text(Vector2f position, String text, Vector3f color) {
    super(position, new Vector2f());
    this.font = Resources.getDefaultFont();
    this.color = color;

    calculateWidthAndEdge(Resources.getDefaultFontSize());

    TextMeshData data = font.loadText(text, Resources.getDefaultFontSize());
    super.size.set(data.sx, data.sy);
    this.model = Loader.loadToVao(data.data, Loader.VERTEX_POS2D_TEX2D);
    MemoryUtil.memFree(data.data);
  }

  public Text(Vector2f position, FontType font, float fontSize, String text, Vector3f color) {
    super(position, new Vector2f());
    this.font = font;
    this.color = color;

    fontSize = Math.max(font.getLoader().getMetaData().minSize, Math.min(font.getLoader().getMetaData().maxSize, fontSize));
    calculateWidthAndEdge(fontSize);

    TextMeshData data = font.loadText(text, fontSize);
    super.size.set(data.sx, data.sy);
    this.model = Loader.loadToVao(data.data, Loader.VERTEX_POS2D_TEX2D);
    MemoryUtil.memFree(data.data);
  }

  private void calculateWidthAndEdge(float fontSize) {
    MetaFile metaData = font.getLoader().getMetaData();
    //width calculation
    width = (float) (Math.pow((fontSize - metaData.minSize), 0.25f) * (1f / 15f) + metaData.minWidth);
    width = Math.min(width, metaData.maxWidth);

    //edge calculation
    edge = metaData.minEdge / fontSize;
    edge = Math.max(edge, metaData.maxEdge);
  }


  public FontType getFont() {
    return font;
  }

  public Model getModel() {
    return model;
  }

  public Vector3f getColor() {
    return color;
  }

  public Vector3f getOutlineColor() {
    return outlineColor;
  }

  public Vector2f getOffset() {
    return offset;
  }


  @Override
  public void render() {
    FontRenderer.add(this);
  }

  @Override
  public void animate() {

  }
}
