package lbq.mcp.plugins.guimaker;

public class ScaledRes {
	private int scaledWidth;
	private int scaledHeight;
	private double scaledWidthD;
	private double scaledHeightD;
	public int scaleFactor;

	public ScaledRes(int scale, int w, int h) {
		this.scaledWidth = w;
		this.scaledHeight = h;
		this.scaleFactor = 1;
		int maxScale = scale;
		if(maxScale == 0) {
			maxScale = 1000;
		}

		while(this.scaleFactor < maxScale && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
			++this.scaleFactor;
		}

		this.scaledWidthD = (double)this.scaledWidth / (double)this.scaleFactor;
		this.scaledHeightD = (double)this.scaledHeight / (double)this.scaleFactor;
		this.scaledWidth = (int)Math.ceil(this.scaledWidthD);
		this.scaledHeight = (int)Math.ceil(this.scaledHeightD);
	}

	public int getScaledWidth() {
		return this.scaledWidth;
	}

	public int getScaledHeight() {
		return this.scaledHeight;
	}
}
