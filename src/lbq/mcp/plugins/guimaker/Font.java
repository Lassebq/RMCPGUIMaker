package lbq.mcp.plugins.guimaker;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Font {
	private int[] charWidth = new int[256];
	private int[] colorCode = new int[32];
	BufferedImage bufferedImage;
	GuiPreview gui;

	public Font(GuiPreview gui) {
		this.gui = gui;
		try {
			bufferedImage = ImageIO.read(PluginGuiMaker.class.getResourceAsStream("/default.png"));
		} catch (IOException iOException18) {
			throw new RuntimeException(iOException18);
		}

		int i5 = bufferedImage.getWidth();
		int i6 = bufferedImage.getHeight();
		int[] i7 = new int[i5 * i6];
		bufferedImage.getRGB(0, 0, i5, i6, i7, 0, i5);

		int i9;
		int i10;
		int i11;
		int i12;
		int i15;
		int i16;
		int i13;
		for(int i8 = 0; i8 < 256; ++i8) {
			i9 = i8 % 16;
			i10 = i8 / 16;

			for(i11 = 7; i11 >= 0; --i11) {
				i12 = i9 * 8 + i11;
				boolean z13 = true;

				for(int i14 = 0; i14 < 8 && z13; ++i14) {
					i15 = (i10 * 8 + i14) * i5;
					i16 = i7[i12 + i15] & 255;
					if(i16 > 0) {
						z13 = false;
					}
				}

				if(!z13) {
					break;
				}
			}

			if(i8 == 32) {
				i11 = 2;
			}

			this.charWidth[i8] = i11 + 2;
		}

		for(i9 = 0; i9 < 32; ++i9) {
			i10 = (i9 >> 3 & 1) * 85;
			i11 = (i9 >> 2 & 1) * 170 + i10;
			i12 = (i9 >> 1 & 1) * 170 + i10;
			i13 = (i9 >> 0 & 1) * 170 + i10;
			if(i9 == 6) {
				i11 += 85;
			}

			if(i9 >= 16) {
				i11 /= 4;
				i12 /= 4;
				i13 /= 4;
			}

			this.colorCode[i9] = (i11 & 255) << 16 | (i12 & 255) << 8 | i13 & 255;
		}

	}

	public void drawStringWithShadow(Graphics g, String string1, int i2, int i3, int i4) {
		this.renderString(g, string1, i2 + 1, i3 + 1, i4, true);
		this.drawString(g, string1, i2, i3, i4);
	}

	public void drawString(Graphics g, String string1, int i2, int i3, int i4) {
		this.renderString(g, string1, i2, i3, i4, false);
	}

	public void renderString(Graphics g, String string1, int i2, int i3, int i4, boolean z5) {
		if(string1 != null) {
			int i6;
			if(z5) {
				i6 = i4 & 0xFF000000;
				i4 = (i4 & 16579836) >> 2;
				i4 += i6;
			}
			
			float f10 = (float)(i4 >> 16 & 255) / 255.0F;
			float f7 = (float)(i4 >> 8 & 255) / 255.0F;
			float f8 = (float)(i4 & 255) / 255.0F;
			float f9 = (float)(i4 >> 24 & 255) / 255.0F;
			if(f9 == 0.0F) {
				f9 = 1.0F;
			}
			Color color = new Color(f10, f7, f8, f9);

			for(i6 = 0; i6 < string1.length(); ++i6) {
				int i11;
				for(; string1.charAt(i6) == 167 && string1.length() > i6 + 1; i6 += 2) {
					i11 = "0123456789abcdef".indexOf(string1.toLowerCase().charAt(i6 + 1));
					int i10 = this.colorCode[i11 + (z5 ? 16 : 0)];
					color = new Color((float)(i10 >> 16) / 255.0F, (float)(i10 >> 8 & 255) / 255.0F, (float)(i10 & 255) / 255.0F);
				}

				i11 = " !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_\'abcdefghijklmnopqrstuvwxyz{|}~\u2302\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb".indexOf(string1.charAt(i6));
				
				if(i11 >= 0) {
					i11 += 32;
					int i10 = i11 % 16 * 8;
					int i12 = i11 / 16 * 8;
					gui.drawTexturedRectangle(g, bufferedImage, i2, i3, i10, i12, this.charWidth[i11], 8, color);
					i2 += this.charWidth[i11];
				}
			}
		}
	}

	public int getStringWidth(String string1) {
		if(string1 == null) {
			return 0;
		} else {
			int i2 = 0;

			for(int i3 = 0; i3 < string1.length(); ++i3) {
				if(string1.charAt(i3) == 167) {
					++i3;
				} else {
					int i4 = " !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_\'abcdefghijklmnopqrstuvwxyz{|}~\u2302\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb".indexOf(string1.charAt(i3));
					if(i4 >= 0) {
						i2 += this.charWidth[i4 + 32];
					}
				}
			}

			return i2;
		}
	}
}
