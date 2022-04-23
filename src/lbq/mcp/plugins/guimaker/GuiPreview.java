package lbq.mcp.plugins.guimaker;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import lbq.mcp.plugins.guimaker.GuiButton.Anchor;
import lbq.mcp.plugins.guimaker.GuiLabel.Align;

public class GuiPreview extends JPanel implements MouseMotionListener, MouseListener {
	private Image img;
	private static BufferedImage sourceImage;
	public List<GuiButton> buttons = new ArrayList<>();
	public List<GuiLabel> labels = new ArrayList<>();
	public int scaleFactor;
	public Font font;
	public int mouseX;
	public int mouseY;
	private GuiFrame frame;
	
	static {
		try {
			sourceImage = ImageIO.read(PluginGuiMaker.class.getResource("/dirt.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public GuiPreview(GuiFrame frame) {
		font = new Font(this);
		this.frame = frame;
		addMouseMotionListener(this);
		addMouseListener(this);
	}

	public void paintComponent(Graphics g) {
		ScaledRes res = new ScaledRes(0, getWidth(), getHeight());
		scaleFactor = res.scaleFactor;
		final int size = 16;
		final int scaledSize = (size * res.scaleFactor);
		final int n = this.getWidth() / 2 + 1;
		final int n2 = this.getHeight() / 2 + 1;
		if (this.img == null || this.img.getWidth(null) != n || this.img.getHeight(null) != n2) {
			this.img = this.createImage(n, n2);
			Image img2 = sourceImage.getScaledInstance(scaledSize, scaledSize, 16);
			Graphics2D graphics2 = (Graphics2D)this.img.getGraphics();
			graphics2.setPaint(new Color(4210752));
			graphics2.setComposite(BlendComposite.Multiply);
			for (int i = 0; i <= n / scaledSize; ++i) {
				for (int j = 0; j <= n2 / scaledSize; ++j) {
					graphics2.drawImage(img2, i * scaledSize, j * scaledSize, scaledSize, scaledSize, null);
					graphics2.fillRect(i * scaledSize, j * scaledSize, scaledSize, scaledSize);
				}
			}
			graphics2.dispose();
		}
		g.drawImage(this.img, 0, 0, n * 2, n2 * 2, null);
		for(GuiButton b : buttons) {
			b.draw(g, this);
		}
		for(GuiLabel b : labels) {
			b.draw(g, this);
		}
	}

    private static GraphicsConfiguration graphicsConfiguration() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    }

    private static BufferedImage createCompatibleImage(int width, int height, int transparency) {
        BufferedImage image = graphicsConfiguration().createCompatibleImage(width, height, transparency);
        image.coerceData(true);
        return image;
    }

    public static void applyQualityRenderingHints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    private static BufferedImage generateMask(BufferedImage imgSource, Color color) {
        int imgWidth = imgSource.getWidth();
        int imgHeight = imgSource.getHeight();

        BufferedImage imgMask = createCompatibleImage(imgWidth, imgHeight, Transparency.TRANSLUCENT);
        Graphics2D g2 = imgMask.createGraphics();
        applyQualityRenderingHints(g2);

        g2.drawImage(imgSource, 0, 0, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN));
        g2.setColor(color);

        g2.fillRect(0, 0, imgSource.getWidth(), imgSource.getHeight());
        g2.dispose();

        return imgMask;
    }
	
	public void drawTexturedRectangle(Graphics g, BufferedImage image, int x, int y, int uvX, int uvY, int w, int h, Color color) {
//		Image img = image.getSubimage(uvX, uvY, w, h);
//		img = img.getScaledInstance(w, h, 16);
//		Image img2 = this.createImage(w, h);
//		Graphics2D graphics2 = (Graphics2D)img2.getGraphics();
//		graphics2.setPaint(color);
//		graphics2.setComposite(MultiplyComposite.Multiply);
//		graphics2.drawImage(img, 0, 0, w, h, null);
//		graphics2.fillRect(0, 0, w, h);
//		graphics2.dispose();
//		g.drawImage(img2, x * scaleFactor, y * scaleFactor, w * scaleFactor, h *  scaleFactor, null);

		BufferedImage image2 = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		BufferedImage image3 = image.getSubimage(uvX, uvY, w, h);

        Graphics2D g2 = image2.createGraphics();
        g2.setComposite(AlphaComposite.Clear);
        g2.fillRect(0, 0, w, h);

        g2.setComposite(AlphaComposite.Src);
        g2.drawImage(image3, 0, 0, null);
        g2.setComposite(BlendComposite.Multiply);
        g2.drawImage(generateMask(image3, color), 0, 0, null);
        g2.dispose();
		g.drawImage(image2, x * scaleFactor, y * scaleFactor, w * scaleFactor, h *  scaleFactor, null);
		
	}

	public void editButton(GuiButton button) {
		NewButtonDialog buttonDialog = new NewButtonDialog(frame, button);
		buttonDialog.setVisible(true);
		if(buttonDialog.confirmed) {
			try {
				button.x = Integer.parseInt(buttonDialog.x.getText());
				button.y = Integer.parseInt(buttonDialog.y.getText());
				button.w = Integer.parseInt(buttonDialog.w.getText());
				button.h = Integer.parseInt(buttonDialog.h.getText());
				button.anchor = (Anchor)buttonDialog.anchor.getSelectedItem();
				button.enabled = buttonDialog.isEnabled.isSelected();
				button.msg = buttonDialog.displayMsg.getText();
			} catch (NumberFormatException | ClassCastException e) {
				e.printStackTrace();
				return;
			}
			repaint();
			frame.saved = false;
		}
	}

	public void editLabel(GuiLabel label) {
		NewLabelDialog buttonDialog = new NewLabelDialog(frame, label);
		buttonDialog.setVisible(true);
		if(buttonDialog.confirmed) {
			try {
				label.x = Integer.parseInt(buttonDialog.x.getText());
				label.y = Integer.parseInt(buttonDialog.y.getText());
				label.anchor = (Anchor)buttonDialog.anchor.getSelectedItem();
				label.type = (Align)buttonDialog.align.getSelectedItem();
				label.msg = buttonDialog.displayMsg.getText();
				int r = Integer.parseInt(buttonDialog.r.getText());
				int g = Integer.parseInt(buttonDialog.g.getText());
				int b = Integer.parseInt(buttonDialog.b.getText());
				label.color = 0 << 24 | r << 16 | g << 8 | b;
			} catch (NumberFormatException | ClassCastException e) {
				e.printStackTrace();
				return;
			}
			repaint();
			frame.saved = false;
		}
	}
	
	public void drawTexturedRectangle(Graphics g, BufferedImage image, int x, int y, int uvX, int uvY, int w, int h) {
		drawTexturedRectangle(g, image, x, y, uvX, uvY, w, h, Color.WHITE);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX() / scaleFactor;
		mouseY = e.getY() / scaleFactor;
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		for(GuiButton button : buttons) {
			if(button.mouseOver(this)) {
				editButton(button);
				return;
			}
		}
		for(GuiLabel label : labels) {
			if(label.mouseOver(this)) {
				editLabel(label);
				return;
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
