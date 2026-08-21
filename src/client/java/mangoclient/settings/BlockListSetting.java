package mangoclient.settings;

import java.util.HashSet;
import java.util.Set;

public class BlockListSetting extends Setting {
	public final Set<String> blocks = new HashSet<>();

	public BlockListSetting(String name) {
		super(name);
	}

	public void add(String id) {
		blocks.add(id);
	}

	public boolean contains(String id) {
		return blocks.contains(id);
	}

	@Override
	public String getDisplay() {
		return name + " (" + blocks.size() + " blocks)";
	}
}
