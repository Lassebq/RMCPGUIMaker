package lbq.mcp.plugins.guimaker;

import java.awt.Component;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import lbq.mcp.plugins.guimaker.GuiButton.Anchor;

public class NewButtonDialog extends AbstractDialog {
	
	public JTextField x;
	public JTextField y;
	public JTextField w;
	public JTextField h;
	public JComboBox<Anchor> anchor;
	public JCheckBox isEnabled;
	public JTextField displayMsg;

	public NewButtonDialog(Frame owner) {
		super(owner, "New button");
	}

	public NewButtonDialog(Frame owner, GuiButton button) {
		super(owner, "Edit button");
		confirmButton.setText("Apply");
		x.setText(String.valueOf(button.x));
		y.setText(String.valueOf(button.y));
		w.setText(String.valueOf(button.w));
		h.setText(String.valueOf(button.h));
		isEnabled.setSelected(button.enabled);
		displayMsg.setText(button.msg);
		anchor.setSelectedItem(button.anchor);
	}
	
	protected void init() {
		List<Component> components = new ArrayList<>();
		displayMsg = new JTextField("Button");
		x = new JTextField("0");
		y = new JTextField("0");
		w = new JTextField("200");
		h = new JTextField("20");
		anchor = new JComboBox<Anchor>(Anchor.values());
		isEnabled = new JCheckBox("", true);
		components.add(displayMsg);
		components.add(x);
		components.add(y);
		components.add(w);
		components.add(h);
		components.add(anchor);
		components.add(isEnabled);
		
		init(components);
	}

	protected String[] getNames() {
		return new String[] {
				"Displayed string",
				"X location",
				"Y location",
				"Width",
				"Height",
				"Anchor",
				"Is enabled"
			};
	}
}
