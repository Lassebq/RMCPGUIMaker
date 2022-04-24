package lbq.mcp.plugins.guimaker;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.mcphackers.mcp.MCP;
import org.mcphackers.mcp.MCPPaths;
import org.mcphackers.mcp.TaskParameter;

import lbq.mcp.plugins.guimaker.GuiButton.Anchor;

public class SaveData {
	
	private Path configFile;
	private MCP mcp;
	private String guiClass = "net/minecraft/src/GuiTest";
	private String guiBase = "net/minecraft/src/GuiScreen";
	private String guiButton = "net/minecraft/src/GuiButton";
	
	public SaveData(Path saveFile, MCP mcp) {
		this.configFile = saveFile;
		this.mcp = mcp;
	}

	public void save(GuiPreview data) throws IOException {
		Path p = MCPPaths.get(mcp, MCPPaths.CLIENT_SOURCES + "/" + guiClass + ".java");
		String GuiScreen = getShortName(guiBase);
		String GuiCustom = getShortName(guiClass);
		String GuiButton = getShortName(guiButton);
		String initGui = "initGui";
		String controlList = "controlList";
		String enabled = "enabled";
		String drawScreen = "drawScreen";
		if(!Files.exists(p.getParent())) {
			Files.createDirectories(p.getParent());
		}
		try(SaveWriter writer = new SaveWriter(Files.newBufferedWriter(p))) {
			writer.writeln("package " + guiClass.substring(0, guiClass.lastIndexOf("/")).replaceAll("/", ".") + ";");
			writer.writeln();
			writer.writeln("import " + guiBase.replaceAll("/", ".") + ";");
			writer.writeln("import " + guiButton.replaceAll("/", ".") + ";");
			writer.writeln();
			writer.write("public class " + GuiCustom + " extends " + GuiScreen + " ");
			writer.openBlock();
				writer.writeln("private " + GuiScreen + " parentScreen;");
				writer.writeln();
				writer.write("public " + GuiCustom + "(" + GuiScreen + " parent) ");
				writer.openBlock();
					writer.writeln("parentScreen = parent;");
				writer.closeBlock();
				writer.write("public void " + initGui + "() ");
				writer.openBlock();
					writer.writeln(controlList + ".clear();");
					int index = 0;
					for(GuiButton button : data.buttons) {
						writer.writeln(controlList + ".add(" + buttonConstructor(button, index) + ");");
						if(!button.enabled) {
							writer.writeln("((" + GuiButton + ")" +controlList+ ".get("+index+"))."+enabled+" = false;");
						}
						index++;
					}
				writer.closeBlock();
				writer.write("public void " + drawScreen + "(int i, int i2, float f3) ");
				writer.openBlock();
					writer.writeln("drawDefaultBackground();");
					for(GuiLabel label : data.labels) {
						writer.writeln(drawLabel(label));
						index++;
					}
					writer.writeln("super."+drawScreen+"(i, i2, f3);");
				writer.closeBlock();
			writer.closeBlock();
		}
	}
	
	private String drawLabel(GuiLabel label) {
		String drawString = "drawString";
		String drawCenteredString = "drawCenteredString";
		String fontRenderer = "fontRenderer";
		String x = getX(label.x, label.anchor);
		String y = getY(label.y, label.anchor);
		String msg = "\"" + label.msg + "\"";
		switch (label.type) {
		case RIGHT:
			return drawString + "(" + String.join(", ", fontRenderer, msg, x + " - " + fontRenderer +".getStringWidth("+msg+")", y, String.valueOf(label.color)) + ");";
		case CENTERED:
			return drawCenteredString + "(" + String.join(", ", fontRenderer, msg, x, y, String.valueOf(label.color)) + ");";
		case LEFT:
		default:
			return drawString + "(" + String.join(", ", fontRenderer, msg, x, y, String.valueOf(label.color)) + ");";
		}
	}

	private String buttonConstructor(GuiButton button, int index) {
		String GuiButton = getShortName(guiButton);
		String x = getX(button.x, button.anchor);
		String y = getY(button.y, button.anchor);
		String msg = "\"" + button.msg + "\"";
		return "new " + GuiButton + "(" + String.join(", ", new String[] {String.valueOf(index), x, y, String.valueOf(button.w), String.valueOf(button.h), msg}) +")";
	}

	private String getX(int x, Anchor anchor) {
		String width = "width";
		String xAdded = x >= 0 ? (" + " + String.valueOf(x)) : (" - " + String.valueOf(x * -1));
		switch (anchor) {
		case TOP:
		case BOTTOM:
		case CENTER:
		case UPPER_CENTER:
			return width + " / 2" + xAdded;
		case RIGHT:
		case TOP_RIGHT:
		case BOTTOM_RIGHT:
			return width + xAdded;
		default:
			return String.valueOf(x);
		}
	}
	
	private String getY(int y, Anchor anchor) {
		String height = "height";
		String yAdded = y >= 0 ? (" + " + String.valueOf(y)) : (" - " + String.valueOf(y * -1));
		switch (anchor) {
		case LEFT:
		case RIGHT:
		case CENTER:
			return height + " / 2" + yAdded;
		case UPPER_CENTER:
			return height + " / 4" + yAdded;
		case BOTTOM:
		case BOTTOM_LEFT:
		case BOTTOM_RIGHT:
			return height + yAdded;
		default:
			return String.valueOf(y);
		}
	}

	private String getShortName(String guiClass2) {
		return guiClass2.substring(guiClass2.lastIndexOf("/") + 1);
	}
	
	private class SaveWriter extends BufferedWriter {
		private int indent = 0;
		private String indString = mcp.getOptions().getStringParameter(TaskParameter.INDENTION_STRING);
		
		public SaveWriter(BufferedWriter out) {
			super(out);
		}
		
		private void appendInd(StringBuilder s) {
			for(int i = 0; i < indent; i++) {
				s.append(indString);
			}
		}
		
		public void writeln() throws IOException {
			writeln("");
		}
		
		public void write(String s) throws IOException {
			StringBuilder b = new StringBuilder();
			appendInd(b);
			b.append(s);
			super.write(b.toString());
		}
		
		public void writeln(String s) throws IOException {
			StringBuilder b = new StringBuilder();
			appendInd(b);
			b.append(s);
			b.append("\n");
			super.write(b.toString());
		}
		
		public void openBlock() throws IOException {
			super.write("{");
			writeln();
			indent++;
		}
		
		public void closeBlock() throws IOException {
			indent--;
			writeln("}");
		}
		
	}

}
