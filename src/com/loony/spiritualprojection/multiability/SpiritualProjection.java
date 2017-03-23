package com.loony.spiritualprojection.multiability;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.loony.spiritualprojection.SpiritualProjectionListener;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.MultiAbility;
import com.projectkorra.projectkorra.ability.SpiritualAbility;
import com.projectkorra.projectkorra.ability.util.MultiAbilityManager;
import com.projectkorra.projectkorra.ability.util.MultiAbilityManager.MultiAbilityInfoSub;
import com.projectkorra.projectkorra.configuration.Config;
import com.projectkorra.projectkorra.configuration.ConfigManager;

public class SpiritualProjection extends SpiritualAbility implements AddonAbility, MultiAbility {

	public static HashMap<String, BossBar> bar = new HashMap<String, BossBar>();
	public static HashMap<String, Integer> powerAmount = new HashMap<String, Integer>();
	public static BossBar bossBar;

	public static Config config;
	private long cooldown;
	public static String path = "ExtraAbilities.Loony.SpiritualProjection.";

	public SpiritualProjection(Player player) {
		super(player);

		// Checks for MultiAbility and sets up each bind
		if (MultiAbilityManager.hasMultiAbilityBound(player)) {
			String abil = MultiAbilityManager.getBoundMultiAbility(player);
			if (abil.equalsIgnoreCase("SpiritualProjection")) {
				switch (player.getInventory().getHeldItemSlot()) {
				case 0:
					if (player.hasPermission("bending.ability.SpiritualProjection.Meditate")) {
						new Meditate(player);
					}
					break;
				case 1:
					if (player.hasPermission("bending.ability.SpiritualProjection.Spirit")) {
						new Spirit(player);
					}
					break;
				case 2:
					if (player.hasPermission("bending.ability.SpiritualProjection.AtralAttack")) {
						new AstralAttack(player);
					}
					break;
				case 3:
					if (player.hasPermission("bending.ability.SpiritualProjection.AstralDrain")) {
						new AstralDrain(player);
					}
					break;
				default:
					break;

				}
			}
		}

		else {
			this.cooldown = ConfigManager.defaultConfig.get().getInt(path + "Cooldown");
			MultiAbilityManager.bindMultiAbility(player, "SpiritualProjection");
			setupPower();
			bPlayer.addCooldown(this);

		}
		if (ChatColor.stripColor(bPlayer.getBoundAbilityName()) == null)

		{
			remove();
			return;

		}

	}

	// Sets up the boss bar & puts the player in the spiritual energy HashMap
	public void setupPower() {

		if (!powerAmount.containsKey(player.getName().toString())) {
			powerAmount.put(player.getName().toString(), 0);
		}

		if (!bar.containsKey(player.getName())) {
			bossBar = Bukkit.createBossBar(
					ChatColor.YELLOW + "" + ChatColor.MAGIC + "I " + ChatColor.GRAY + "" + ChatColor.BOLD
							+ "Spiritual Energy" + ChatColor.YELLOW + "" + ChatColor.MAGIC + " I",
					BarColor.BLUE, BarStyle.SEGMENTED_12);
			bar.put(player.getName(), bossBar);
			bossBar.addPlayer(player);

		}

	}

	@Override
	public void progress() {

	}

	@Override
	public void load() {
		Bukkit.getPluginManager().registerEvents(new SpiritualProjectionListener(), ProjectKorra.plugin);
		ProjectKorra.log.info(getName() + " by " + getAuthor() + " " + getVersion() + " has been loaded!");

		config = new Config(new File("ExtraAbilities.yml"));
		FileConfiguration ExtraAbilities = config.get();

		// SpiritualProjection
		ExtraAbilities.addDefault(path + "Cooldown", Long.valueOf(6000));

		// Meditate
		ExtraAbilities.addDefault(path + "Meditate.Cooldown", Long.valueOf(6000));
		ExtraAbilities.addDefault(path + "Meditate.Duration", Long.valueOf(10000));

		// Spirit
		ExtraAbilities.addDefault(path + "Spirit.Cooldown", Long.valueOf(5000));
		ExtraAbilities.addDefault(path + "Spirit.Speed", Double.valueOf(0.6));
		ExtraAbilities.addDefault(path + "Spirit.Duration", Long.valueOf(4500));
		ExtraAbilities.addDefault(path + "Spirit.SpiritualEnergy", Integer.valueOf(20));

		// AstralAttack
		ExtraAbilities.addDefault(path + "AstralAttack.Duration", Long.valueOf(6000));
		ExtraAbilities.addDefault(path + "AstralAttack.Speed", Double.valueOf(0.8));
		ExtraAbilities.addDefault(path + "AstralAttack.Cooldown", Long.valueOf(3000));
		ExtraAbilities.addDefault(path + "AstralAttack.Damage", Double.valueOf(6));
		ExtraAbilities.addDefault(path + "AstralAttack.Knockback", Double.valueOf(4));
		ExtraAbilities.addDefault(path + "AstralAttack.BlastRadius", Double.valueOf(2));
		ExtraAbilities.addDefault(path + "AstralAttack.SpiritualEnergy", Long.valueOf(35));

		config.save();

	}

	@Override
	public void remove() {
		super.remove();

	}

	@Override
	public void stop() {
	}

	@Override
	public long getCooldown() {

		return cooldown;
	}

	@Override
	public Location getLocation() {

		return null;
	}

	@Override
	public String getName() {

		return "SpiritualProjection";
	}

	@Override
	public boolean isHarmlessAbility() {

		return true;
	}

	@Override
	public boolean isSneakAbility() {

		return true;
	}

	@Override
	public String getAuthor() {

		return "Loony";
	}

	@Override
	public String getVersion() {

		return "1.0.0";
	}

	//This doesn't work because MultiAbilities are shit
	public void displayBoundMsg(int slot) {
		String name = bPlayer.getAbilities().get(slot);
		if (name != null) {
			player.sendMessage(getElement().getColor() + "Ability: "  + name);
		}
	}

	@Override
	public ArrayList<MultiAbilityInfoSub> getMultiAbilities() {

		ArrayList<MultiAbilityInfoSub> SpiritualProjection = new ArrayList<MultiAbilityInfoSub>();
		SpiritualProjection.add(new MultiAbilityInfoSub("Meditate", Element.SPIRITUAL));
		SpiritualProjection.add(new MultiAbilityInfoSub("Spirit", Element.SPIRITUAL));
		SpiritualProjection.add(new MultiAbilityInfoSub("AstralAttack", Element.SPIRITUAL));
		SpiritualProjection.add(new MultiAbilityInfoSub("AstralDrain", Element.SPIRITUAL));

		return SpiritualProjection;
	}

}
