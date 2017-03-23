package com.loony.spiritualprojection.multiability;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import com.loony.spiritualprojection.SpiritualProjectionListener;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.MultiAbility;
import com.projectkorra.projectkorra.ability.SpiritualAbility;
import com.projectkorra.projectkorra.ability.util.MultiAbilityManager;
import com.projectkorra.projectkorra.ability.util.MultiAbilityManager.MultiAbilityInfoSub;
import com.projectkorra.projectkorra.configuration.ConfigManager;

public class SpiritualProjection extends SpiritualAbility implements AddonAbility, MultiAbility {

	public static HashMap<String, BossBar> bar = new HashMap<String, BossBar>();
	public static HashMap<String, Integer> powerAmount = new HashMap<String, Integer>();

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
				default:
					break;

				}
			}
		}

		else {
			MultiAbilityManager.bindMultiAbility(player, "SpiritualProjection");
			setupPower();

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

			if (!bar.containsKey(player.getName())) {
				BossBar bossBar = Bukkit.createBossBar(
						ChatColor.YELLOW + "" + ChatColor.MAGIC + "I " + ChatColor.GRAY + "" + ChatColor.BOLD
								+ "Spiritual Energy" + ChatColor.YELLOW + "" + ChatColor.MAGIC + " I",
						BarColor.BLUE, BarStyle.SEGMENTED_12);
				bar.put(player.getName(), bossBar);
				bossBar.addPlayer(player);
			}

		}

	}

	@Override
	public void progress() {

	}

	@Override
	public void load() {
		Bukkit.getPluginManager().registerEvents(new SpiritualProjectionListener(), ProjectKorra.plugin);

		ProjectKorra.log.info(getName() + " by " + getAuthor() + " " + getVersion() + " has been loaded!");

		// ConfigManager.defaultConfig.get().addDefault(path + "Range",
		// Integer.valueOf(3));

		ConfigManager.defaultConfig.save();

		// range = ConfigManager.defaultConfig.get().getInt(path + "Range");
	}

	@Override
	public void remove() {
		super.remove();
		if (bar.containsKey(player.getName())) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				bar.get(player.getName()).removePlayer(p);
			}
		}

		if (CoreAbility.getAbility(player, Meditate.class) != null
				|| CoreAbility.getAbility(player, Spirit.class) != null
				|| CoreAbility.getAbility(player, AstralAttack.class) != null) {
			remove();
		}

	}

	@Override
	public void stop() {
		for (SpiritualProjection sp : CoreAbility.getAbilities(this.getClass())) {
			sp.remove();
		}
	}

	@Override
	public long getCooldown() {

		return 0L;
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

	public void displayBoundMsg(int slot) {
		String name = bPlayer.getAbilities().get(slot);
		if (name != null) {
			player.sendMessage(getElement().getColor() + "Ability:" + " " + name);
		}
	}

	@Override
	public ArrayList<MultiAbilityInfoSub> getMultiAbilities() {

		ArrayList<MultiAbilityInfoSub> SpiritualProjection = new ArrayList<MultiAbilityInfoSub>();
		SpiritualProjection.add(new MultiAbilityInfoSub("Meditate", Element.SPIRITUAL));
		SpiritualProjection.add(new MultiAbilityInfoSub("Spirit", Element.SPIRITUAL));
		SpiritualProjection.add(new MultiAbilityInfoSub("AstralAttack", Element.SPIRITUAL));

		return SpiritualProjection;
	}

}
