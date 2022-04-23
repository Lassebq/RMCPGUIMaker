package lbq.mcp.plugins.guimaker;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GuiButton {
	private static BufferedImage sourceImage;
	protected int w;
	protected int h;
	public int x;
	public int y;
	public String msg;
	public boolean enabled;
	public boolean visible;
	public Anchor anchor;
	
	static {
		try {
			sourceImage = ImageIO.read(PluginGuiMaker.class.getResource("/gui/gui.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public GuiButton(int i2, int i3, int i4, int i5, String string6, Anchor anchor) {
		this.enabled = true;
		this.visible = true;
		this.x = i2;
		this.y = i3;
		this.w = i4;
		this.h = i5;
		this.msg = string6;
		this.anchor = anchor;
	}

	protected int getTex(boolean z1) {
		byte b2 = 1;
		if(!this.enabled) {
			b2 = 0;
		} else if(z1) {
			b2 = 2;
		}

		return b2;
	}

	public static void drawCenteredString(Graphics g, Font font, String string1, int i2, int i3, int i4) {
		font.drawStringWithShadow(g, string1, i2 - font.getStringWidth(string1) / 2, i3, i4);
	}

	public void draw(Graphics g, GuiPreview gui) {
		if(this.visible) {
			int x = getPosX(gui);
			int y = getPosY(gui);
			boolean hovered = this.enabled && mouseOver(gui);
			int tex = this.getTex(hovered);
			gui.drawTexturedRectangle(g, sourceImage, x, y, 0, 46 + tex * 20, w / 2, h);
			gui.drawTexturedRectangle(g, sourceImage, x + w / 2, y, 200 - w / 2, 46 + tex * 20, w / 2, h);
			if(!this.enabled) {
				drawCenteredString(g, gui.font, msg, x + w / 2, y + (h - 8) / 2, -6250336);
			} else if(hovered) {
				drawCenteredString(g, gui.font, msg, x + w / 2, y + (h - 8) / 2, 16777120);
			} else {
				drawCenteredString(g, gui.font, msg, x + w / 2, y + (h - 8) / 2, 14737632);
			}
		}
	}
	
	public boolean mouseOver(GuiPreview gui) {
		int x = getPosX(gui);
		int y = getPosY(gui);
		return gui.mouseX >= x && gui.mouseY >= y && gui.mouseX < x + w && gui.mouseY < y + h;
	}

	private int getPosX(GuiPreview gui) {
		//move scaling to a different method in GuiPreview
		switch (anchor) {
		case TOP:
		case BOTTOM:
		case CENTER:
		case UPPER_CENTER:
			return gui.getWidth() / (2 * gui.scaleFactor) + x;
		case LEFT:
		case TOP_LEFT:
		case BOTTOM_LEFT:
			return x;
		case RIGHT:
		case TOP_RIGHT:
		case BOTTOM_RIGHT:
			return gui.getWidth() / gui.scaleFactor + x;
		default:
			return 0;
		}
	}
	
	private int getPosY(GuiPreview gui) {
		switch (anchor) {
		case TOP:
		case TOP_LEFT:
		case TOP_RIGHT:
			return y;
		case LEFT:
		case RIGHT:
		case CENTER:
			return gui.getHeight() / (2 * gui.scaleFactor) + y;
		case UPPER_CENTER:
			return gui.getHeight() / (4 * gui.scaleFactor) + y;
		case BOTTOM:
		case BOTTOM_LEFT:
		case BOTTOM_RIGHT:
			return gui.getHeight() / gui.scaleFactor + y;
		default:
			return 0;
		}
	}

	public enum Anchor {
		TOP_LEFT,
		TOP_RIGHT,
		BOTTOM_LEFT,
		BOTTOM_RIGHT,
		UPPER_CENTER,
		LEFT,
		RIGHT,
		TOP,
		BOTTOM,
		CENTER
	}
}
