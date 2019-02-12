package fr.knacky.absoluteui.font;

public class FontType {
	private int textureAtlas;
	private TextMeshCreator loader;

	public FontType(int textureAtlas, String fontRes) {
		this.textureAtlas = textureAtlas;
		this.loader = new TextMeshCreator(fontRes);
	}

	public int getTextureAtlas() {
		return textureAtlas;
	}

	public TextMeshCreator getLoader() {
		return loader;
	}

	public TextMeshData loadText(String text, float fontSize) {
		return loader.createTextMesh(text, fontSize);
	}
}
