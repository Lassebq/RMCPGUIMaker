package lbq.mcp.plugins.guimaker;
import org.mcphackers.mcp.MCP;
import org.mcphackers.mcp.plugin.MCPPlugin;
import org.mcphackers.mcp.tasks.Task;

public class PluginGuiMaker implements MCPPlugin {

	@Override
	public String pluginId() {
		return "guiDesigner";
	}

	@Override
	public void init() {
		registerTask(pluginId().toLowerCase(), "GUI Maker", TaskGuiMaker.class);
	}

	@Override
	public void onTaskEvent(TaskEvent event, Task task) {
	}

	@Override
	public void onMCPEvent(MCPEvent event, MCP mcp) {
	}
	
}
