package lbq.mcp.plugins.guimaker;

import java.awt.Component;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;

import lbq.mcp.plugins.guimaker.GuiButton.Anchor;
import lbq.mcp.plugins.guimaker.GuiLabel.Align;

public class NewLabelDialog extends AbstractDialog {

	public JTextField x;
	public JTextField y;
	public JTextField r;
	public JTextField g;
	public JTextField b;
	public JComboBox<Align> align;
	public JComboBox<Anchor> anchor;
	public JTextField displayMsg;

	public NewLabelDialog(Frame owner) {
		super(owner, "New label");
	}
	
	public NewLabelDialog(JFrame owner, GuiLabel label) {
		super(owner, "Edit label");
		confirmButton.setText("Apply");
		x.setText(String.valueOf(label.x));
		y.setText(String.valueOf(label.y));
		displayMsg.setText(label.msg);
		anchor.setSelectedItem(label.anchor);
		align.setSelectedItem(label.type);
		int r = label.color >> 16 & 255;
		int g = label.color >> 8 & 255;
		int b = label.color & 255;
		this.r.setText(String.valueOf(r));
		this.g.setText(String.valueOf(g));
		this.b.setText(String.valueOf(b));
	}

	protected void init() {
		List<Component> components = new ArrayList<>();
		displayMsg = new JTextField("Label");
		x = new JTextField("0");
		y = new JTextField("0");
		anchor = new JComboBox<Anchor>(Anchor.values());
		align = new JComboBox<Align>(Align.values());
		r = new JTextField("255");
		g = new JTextField("255");
		b = new JTextField("255");
		components.add(displayMsg);
		components.add(x);
		components.add(y);
		components.add(anchor);
		components.add(align);
		components.add(r);
		components.add(g);
		components.add(b);
		
		init(components);
	}

	protected String[] getNames() {
		return new String[] {
				"Displayed string",
				"X location",
				"Y location",
				"Anchor",
				"Align",
				"Red",
				"Green",
				"Blue"
			};
	}

}
