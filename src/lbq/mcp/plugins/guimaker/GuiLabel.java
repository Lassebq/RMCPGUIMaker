package lbq.mcp.plugins.guimaker;

import java.awt.Graphics;

import lbq.mcp.plugins.guimaker.GuiButton.Anchor;

public class GuiLabel {
	
	public String msg;
	public int color;
	public int x;
	public int y;
	public Align type; 
	public Anchor anchor; 
	
	public GuiLabel(int i, int i2, String s, Anchor anchor, Align type, int color) {
		this.msg = s;
		this.color = color;
		this.x = i;
		this.y = i2;
		this.anchor = anchor;
		this.type = type;
	}

	public void draw(Graphics g, GuiPreview gui) {
		int x = getPosX(gui);
		int y = getPosY(gui);
		if(type == Align.CENTERED) {
			GuiButton.drawCenteredString(g, gui.font, msg, x, y, color);
		}
		else if(type == Align.RIGHT) {
			gui.font.drawStringWithShadow(g, msg, x - gui.font.getStringWidth(msg), y, color);
		}
		else {
			gui.font.drawStringWithShadow(g, msg, x, y, color);
		}
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
	
	public enum Align {
		LEFT,
		RIGHT,
		CENTERED;
	}
	
	public boolean mouseOver(GuiPreview gui) {
		int x = getPosX(gui);
		int y = getPosY(gui);
		int w = gui.font.getStringWidth(msg);
		switch (type) {
		case RIGHT:
			x -= gui.font.getStringWidth(msg);
			break;
		case CENTERED:
			x -= gui.font.getStringWidth(msg) / 2;
			break;
		case LEFT:
			break;
		}
		return gui.mouseX >= x && gui.mouseY >= y && gui.mouseX < x + w  && gui.mouseY < y + 8;
	}
	
}
