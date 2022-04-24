package lbq.mcp.plugins.guimaker;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.mcphackers.mcp.MCP;
import org.mcphackers.mcp.tasks.Task;

public class TaskGuiMaker extends Task {

	public TaskGuiMaker(MCP instance) {
		super(Side.ANY, instance);
	}

	@Override
	public void doTask() throws Exception {
		Path p = Paths.get("plugins/gui_maker.cfg");
		SaveData savedata = new SaveData(p, mcp);
		GuiFrame frame = new GuiFrame(savedata);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        while(!frame.closed) {
        	Thread.sleep(1);
        };
	}

}
