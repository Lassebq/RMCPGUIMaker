package lbq.mcp.plugins.guimaker;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class AbstractDialog extends JDialog {

	protected JButton confirmButton;
	private Frame owner;
	public boolean confirmed;

	public AbstractDialog(Frame owner, String s) {
		super(owner, s, true);
		this.owner = owner;
		init();
	}
	
	protected abstract void init();
	
	protected abstract String[] getNames();

	protected void init(List<Component> components) {
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		Container inputContainer = new JPanel(new GridBagLayout());
		String[] names = getNames();
		for (int i = 0; i < components.size(); i += 1) {
			JLabel label = new JLabel(names[i]);
			GridBagConstraintsBuilder cb = new GridBagConstraintsBuilder(new GridBagConstraints()).insetsUnscaled(2);
			inputContainer.add(label, cb.pos(0, i).weightX(0.0).anchor(GridBagConstraints.LINE_END).fill(GridBagConstraints.NONE).build());
			inputContainer.add(components.get(i), cb.pos(1, i).weightX(1.0).anchor(GridBagConstraints.LINE_END).fill(GridBagConstraints.HORIZONTAL).build());
		}
		contentPane.add(inputContainer, BorderLayout.CENTER);
		Container buttonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 4));
		confirmButton = new JButton("Create");
		confirmButton.addActionListener(event -> confirm());
		buttonContainer.add(confirmButton);
		JButton abortButton = new JButton("Cancel");
		abortButton.addActionListener(event -> cancel());
		buttonContainer.add(abortButton);
		contentPane.add(buttonContainer, BorderLayout.SOUTH);

		pack();
		setMinimumSize(getPreferredSize());
		setResizable(false);
		setLocationRelativeTo(owner);
	}

	protected void cancel() {
		confirmed  = false;
		dispose();
	}

	protected void confirm() {
		confirmed = true;
		dispose();
	}

	public final class GridBagConstraintsBuilder {

		private final GridBagConstraints inner;

		private GridBagConstraintsBuilder(GridBagConstraints inner) {
			this.inner = inner;
		}

		public GridBagConstraintsBuilder pos(int x, int y) {
			GridBagConstraintsBuilder copy = this.copy();
			copy.inner.gridx = x;
			copy.inner.gridy = y;
			return copy;
		}

		public GridBagConstraintsBuilder size(int width, int height) {
			GridBagConstraintsBuilder copy = this.copy();
			copy.inner.gridwidth = width;
			copy.inner.gridheight = height;
			return copy;
		}

		public GridBagConstraintsBuilder width(int width) {
			GridBagConstraintsBuilder copy = this.copy();
			copy.inner.gridwidth = width;
			return copy;
		}

		public GridBagConstraintsBuilder height(int height) {
			GridBagConstraintsBuilder copy = this.copy();
			copy.inner.gridheight = height;
			return copy;
		}

		public GridBagConstraintsBuilder dimensions(int x, int y, int width, int height) {
			return this.pos(x, y).size(width, height);
		}

		public GridBagConstraintsBuilder weight(double x, double y) {
			GridBagConstraintsBuilder copy = this.copy();
			copy.inner.weightx = x;
			copy.inner.weighty = y;
			return copy;
		}

		public GridBagConstraintsBuilder weightX(double x) {
			GridBagConstraintsBuilder copy = this.copy();
			copy.inner.weightx = x;
			return copy;
		}

		public GridBagConstraintsBuilder weightY(double y) {
			GridBagConstraintsBuilder copy = this.copy();
			copy.inner.weighty = y;
			return copy;
		}

		public GridBagConstraintsBuilder anchor(int anchor) {
			GridBagConstraintsBuilder copy = this.copy();
			copy.inner.anchor = anchor;
			return copy;
		}

		public GridBagConstraintsBuilder fill(int fill) {
			GridBagConstraintsBuilder copy = this.copy();
			copy.inner.fill = fill;
			return copy;
		}

		public GridBagConstraintsBuilder insetsUnscaled(int all) {
			return this.insetsUnscaled(all, all, all, all);
		}

		public GridBagConstraintsBuilder insetsUnscaled(int vertical, int horizontal) {
			return this.insetsUnscaled(vertical, horizontal, vertical, horizontal);
		}

		public GridBagConstraintsBuilder insetsUnscaled(int top, int horizontal, int bottom) {
			return this.insetsUnscaled(top, horizontal, bottom, horizontal);
		}

		public GridBagConstraintsBuilder insetsUnscaled(int top, int right, int bottom, int left) {
			GridBagConstraintsBuilder copy = this.copy();
			copy.inner.insets.set(top, left, bottom, right);
			return copy;
		}

		public GridBagConstraintsBuilder paddingUnscaled(int pad) {
			return this.paddingUnscaled(pad, pad);
		}

		public GridBagConstraintsBuilder paddingUnscaled(int padX, int padY) {
			GridBagConstraintsBuilder copy = this.copy();
			copy.inner.ipadx = padX;
			copy.inner.ipady = padY;
			return copy;
		}

		public GridBagConstraintsBuilder copy() {
			GridBagConstraints c = (GridBagConstraints) this.inner.clone();
			return new GridBagConstraintsBuilder(c);
		}

		public GridBagConstraints build() {
			return (GridBagConstraints) this.inner.clone();
		}

	}

}
