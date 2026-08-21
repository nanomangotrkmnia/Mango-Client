package mangoclient.module;

import mangoclient.MangoClientMod;
import mangoclient.settings.BlockListSetting;

public class Xray extends Module {
	public final BlockListSetting blocks;

	public Xray() {
		super("Xray", "Render only selected ores/blocks");
		blocks = new BlockListSetting("Xray Blocks");
		addSetting(blocks);
		blocks.add("minecraft:ancient_debris");
		blocks.add("minecraft:diamond_ore");
		blocks.add("minecraft:deepslate_diamond_ore");
		blocks.add("minecraft:emerald_ore");
		blocks.add("minecraft:deepslate_emerald_ore");
		blocks.add("minecraft:gold_ore");
		blocks.add("minecraft:deepslate_gold_ore");
		blocks.add("minecraft:iron_ore");
		blocks.add("minecraft:deepslate_iron_ore");
		blocks.add("minecraft:copper_ore");
		blocks.add("minecraft:deepslate_copper_ore");
		blocks.add("minecraft:coal_ore");
		blocks.add("minecraft:deepslate_coal_ore");
		blocks.add("minecraft:redstone_ore");
		blocks.add("minecraft:deepslate_redstone_ore");
		blocks.add("minecraft:lapis_ore");
		blocks.add("minecraft:deepslate_lapis_ore");
		blocks.add("minecraft:nether_gold_ore");
		blocks.add("minecraft:nether_quartz_ore");
		blocks.add("minecraft:raw_iron_block");
		blocks.add("minecraft:raw_gold_block");
		blocks.add("minecraft:raw_copper_block");
		blocks.add("minecraft:diamond_block");
		blocks.add("minecraft:gold_block");
		blocks.add("minecraft:emerald_block");
	}

	@Override
	public void onEnable() {
		if (MangoClientMod.mc.level != null) MangoClientMod.mc.levelRenderer.allChanged();
	}

	@Override
	public void onDisable() {
		if (MangoClientMod.mc.level != null) MangoClientMod.mc.levelRenderer.allChanged();
	}

	public boolean isXrayBlock(String id) {
		return blocks.contains(id);
	}
}
