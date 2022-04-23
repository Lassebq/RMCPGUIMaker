package lbq.mcp.plugins.guimaker;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import lbq.mcp.plugins.guimaker.GuiLabel.Align;
import lbq.mcp.plugins.guimaker.GuiButton.Anchor;

public class GuiFrame extends JFrame implements KeyListener, WindowListener  {
	
	private GuiPreview preview;
	public JMenuBar menuBar;
	public boolean closed = false;
	public boolean fullscreen = false;
	private SaveData saveData;
	public boolean saved;

	public GuiFrame(SaveData savedata) {
		super("GUI Creator");
	    addKeyListener(this);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		menuBar = new JMenuBar();
		this.saveData = savedata;
		JMenu group = new JMenu("GUI");
		JMenuItem item = new JMenuItem("New button...");
		item.addActionListener(a -> addButton());
		JMenuItem item2 = new JMenuItem("New label...");
		item2.addActionListener(a -> addLabel());
		JMenuItem item3 = new JMenuItem("Clear all");
		item3.addActionListener(a -> {
			preview.buttons.clear();
			preview.labels.clear();
			preview.repaint();
		});
		JMenuItem item4 = new JMenuItem("Save");
		item4.addActionListener(a ->  {
			try {
				saveData.save(preview);
				saved = true;
			} catch (IOException e) {
				saved = false;
			}
		});
		group.add(item);
		group.add(item2);
		group.add(item3);
		group.add(item4);
		menuBar.add(group);
		preview = new GuiPreview(this);
		preview.setPreferredSize(new Dimension(854, 480));
		add(preview);
		setJMenuBar(menuBar);
		addWindowListener(this);
	}

	public void windowClosed(WindowEvent e) {
    }
    
	public void keyTyped(KeyEvent e) {
    }
    
    public void keyPressed(KeyEvent e) {
    	if(e.getKeyCode() == KeyEvent.VK_F11) {
	        dispose();
	        fullscreen = !fullscreen;
	        menuBar.setVisible(!fullscreen);
			setUndecorated(fullscreen);
			pack();
			setExtendedState(fullscreen ? JFrame.MAXIMIZED_BOTH : JFrame.NORMAL); 
	        if(!fullscreen) setLocationRelativeTo(null);
			setVisible(true);
    	}
    }
    
    public void keyReleased(KeyEvent e) {
    }

    public void addLabel() {
		NewLabelDialog label = new NewLabelDialog(this);
		label.setVisible(true);
		if(label.confirmed) {
			addLabel(label.x.getText(), label.y.getText(), label.anchor.getSelectedItem(), label.align.getSelectedItem(), label.displayMsg.getText(), label.r.getText(), label.g.getText(), label.b.getText());
		}
	}
    
	private void addLabel(String x, String y, Object anchor, Object typ, String s, String r2, String g2, String b2) {
		int xInt = 0;
		int yInt = 0;
		int r = 0;
		int g = 0;
		int b = 0;
		Anchor anchorValue;
		Align type;
		try {
			xInt = Integer.parseInt(x);
			yInt = Integer.parseInt(y);
			r = Integer.parseInt(r2);
			g = Integer.parseInt(g2);
			b = Integer.parseInt(b2);
			anchorValue = (Anchor)anchor;
			type = (Align)typ;
		} catch (NumberFormatException | ClassCastException e) {
			e.printStackTrace();
			return;
		}
		addLabel(xInt, yInt, anchorValue, s, type, 0 << 24 | r << 16 | g << 8 | b);
		
	}

	public void addLabel(int x, int y, Anchor anchor, String s, Align type, int color) {
		GuiLabel label = new GuiLabel(x, y, s, anchor, type, color);
		preview.labels.add(label);
		preview.repaint();
		saved = false;
	}

	public void addButton() {
		NewButtonDialog button = new NewButtonDialog(this);
		button.setVisible(true);
		if(button.confirmed) {
			addButton(button.x.getText(), button.y.getText(), button.w.getText(), button.h.getText(), button.anchor.getSelectedItem(), button.displayMsg.getText(), button.isEnabled.isSelected());
		}
	}

	private void addButton(String x, String y, String w, String h, Object anchor, String s, boolean b) {
		int xInt = 0;
		int yInt = 0;
		int wInt = 200;
		int hInt = 20;
		Anchor anchorValue;
		try {
			xInt = Integer.parseInt(x);
			yInt = Integer.parseInt(y);
			wInt = Integer.parseInt(w);
			hInt = Integer.parseInt(h);
			anchorValue = (Anchor)anchor;
		} catch (NumberFormatException | ClassCastException e) {
			e.printStackTrace();
			return;
		}
		addButton(xInt, yInt, wInt, hInt, anchorValue, s, b);
	}

	public void addButton(int x, int y, int w, int h, Anchor anchor, String s, boolean b) {
		GuiButton button = new GuiButton(x, y, w, h, s, anchor);
		button.enabled = b;
		preview.buttons.add(button);
		preview.repaint();
		saved = false;
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
        int i = saved ? 0 : JOptionPane.showConfirmDialog(this, "Are you sure you want to quit without saving?", "Confirm exit", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if(i == 0) {
	        closed = true;
			dispose();
		}
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
}
