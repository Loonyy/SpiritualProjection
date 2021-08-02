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

import com.loony.spiritualprojection.utils.SpiritualProjectionListener;
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

		if (bPlayer.isOnCooldown(this)) {
			remove();
			return;
		}
		// Checks for MultiAbility and sets up each bind
		if (MultiAbilityManager.hasMultiAbilityBound(player)) {
			String abil = MultiAbilityManager.getBoundMultiAbility(player);
			if (abil.equalsIgnoreCase("SpiritualProjection")) {
				switch (player.getInventory().getHeldItemSlot()) {
				case 0:
					if (player.hasPermission("bending.ability.Meditate")) {
						new Meditate(player);
					}
					break;
				case 1:
					if (player.hasPermission("bending.ability.Spirit")) {
						new Spirit(player);
					}
					break;
				case 2:
					if (player.hasPermission("bending.ability.AstralAttack")) {
						new AstralAttack(player);
					}
					break;
				case 3:
					if (player.hasPermission("bending.ability.SpiritualDrain")) {
						new SpiritualDrain(player);
					}
					break;
				case 4:
					if (player.hasPermission("bending.ability.SpiritualProjection")) {
						new Exit(player);
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
			powerAmount.put(player.getName().toString(), 100);
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
		ExtraAbilities.addDefault(path + "Cooldown", Long.valueOf(30000));

		// Meditate
		ExtraAbilities.addDefault(path + "Meditate.Cooldown", Long.valueOf(6000));
		ExtraAbilities.addDefault(path + "Meditate.Duration", Long.valueOf(10000));
		ExtraAbilities.addDefault(path + "Meditate.EnergyAmount", Integer.valueOf(1));

		// Spirit
		ExtraAbilities.addDefault(path + "Spirit.Enabled", Boolean.valueOf(true));
		ExtraAbilities.addDefault(path + "Spirit.Cooldown", Long.valueOf(5000));
		ExtraAbilities.addDefault(path + "Spirit.Duration", Long.valueOf(60000));
		ExtraAbilities.addDefault(path + "Spirit.SpiritualEnergy", Integer.valueOf(20));

		// AstralAttack
		ExtraAbilities.addDefault(path + "AstralAttack.Enabled", Boolean.valueOf(true));
		ExtraAbilities.addDefault(path + "AstralAttack.Duration", Long.valueOf(6000));
		ExtraAbilities.addDefault(path + "AstralAttack.Speed", Double.valueOf(0.8));
		ExtraAbilities.addDefault(path + "AstralAttack.Cooldown", Long.valueOf(3000));
		ExtraAbilities.addDefault(path + "AstralAttack.Damage", Double.valueOf(6));
		ExtraAbilities.addDefault(path + "AstralAttack.Knockback", Double.valueOf(4));
		ExtraAbilities.addDefault(path + "AstralAttack.BlastRadius", Double.valueOf(2));
		ExtraAbilities.addDefault(path + "AstralAttack.SpiritualEnergy", Long.valueOf(35));
		
		//SpiritualDrain
		ExtraAbilities.addDefault(path + "SpiritualDrain.Enabled", Boolean.valueOf(true));
		ExtraAbilities.addDefault(path + "SpiritualDrain.Cooldown", Long.valueOf(15000));
		ExtraAbilities.addDefault(path + "SpiritualDrain.Range", Integer.valueOf(20));
		ExtraAbilities.addDefault(path + "SpiritualDrain.DrainSpeed", Double.valueOf(0.8));
		ExtraAbilities.addDefault(path + "SpiritualDrain.DrainedDuration", Long.valueOf(5500));
		ExtraAbilities.addDefault(path + "SpiritualDrain.SpiritualEnergy", Integer.valueOf(45));
		config.save();

	}

	@Override
	public void remove() {
		if (player != null) {
			SpiritualProjection.bar.remove(player.getName());
			SpiritualProjection.powerAmount.remove(player.getName());
			if (SpiritualProjection.bossBar != null) {
				SpiritualProjection.bossBar.removePlayer(player);
			}
		}
		super.remove();

	}

	@Override
	public void stop() {
		remove();
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

		return false;
	}

	@Override
	public String getAuthor() {

		return "Loony";
	}

	@Override
	public String getVersion() {

		return "1.0.3";
	}
	
	@Override
	public String getDescription() {
		return ChatColor.GRAY + "" + ChatColor.BOLD + "Developed by Loony\n" 
				+ ChatColor.GRAY + "Spiritual Projection is a completely unique and advanced spiritual ability that allows you to focus your spiritual energy into attack, defense or even mobility. These abilities require you to have enough spiritual energy, which can be recharged by meditating. To begin using this ability, just left click.";
		
	}

	@Override
	public String getInstructions() {
		return "\nMeditate - " + ChatColor.GRAY + "Hold sneak to charge up your spiritual energy.\n" + ChatColor.GOLD +
	"Spirit - " + ChatColor.GRAY + "Tap sneak to transform into a spirit. Left click to exit spirit mode.\n" +
				ChatColor.GOLD + "AstralAttack - " + ChatColor.GRAY + "Hold sneak to send out an astral projection in attack form, damaging anyone that comes into contact with it. This astral attack will go in the direction that you're looking.\n" + 
	ChatColor.GOLD + "SpiritualDrain - " + ChatColor.GRAY + "Hold sneak to start draining the spiritual energy out of players that are in range. Once players are drained, they won't be able to use abilities for a certain amount of time. This ability also heals you once you've drained their spiritual connection.\n" +
				ChatColor.GOLD + "Exit - " + ChatColor.GRAY + "Tap sneak to exit the multi ability.";
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
		SpiritualProjection.add(new MultiAbilityInfoSub("SpiritualDrain", Element.SPIRITUAL));
		SpiritualProjection.add(new MultiAbilityInfoSub("Exit", Element.SPIRITUAL));

		return SpiritualProjection;
	}

}
