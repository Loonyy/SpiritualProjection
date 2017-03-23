package com.loony.spiritualprojection.multiability;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.AirAbility;

public class Meditate extends AirAbility implements AddonAbility {

	@SuppressWarnings("unused")
	private long time;
	@SuppressWarnings("unused")
	private boolean charged;
	private long cooldown;
	private long chargeTime;
	private Location location;
	private int chargeTicks;
	private int ticks;
	public static HashMap<String, Integer> powerAmount = new HashMap<String, Integer>();
	protected static BossBar bar;

	public Meditate(Player player) {
		super(player);

		if (bPlayer.canBendIgnoreBinds(this)) {
			setFields();
			setupPower();

			start();

		}

	}

	public void setFields() {

		this.charged = false;
		this.chargeTime = 10000;
		this.location = player.getLocation();
		this.cooldown = 3000;
		this.time = System.currentTimeMillis();
	}

	@Override
	public void progress() {
		if (player.isDead() || !player.isOnline() || !player.isSneaking()) {

			remove();
			return;
		}

		ticks++;

		// Checks if ability is still charging
		if (System.currentTimeMillis() < getStartTime() + chargeTime) {
			powerProgress();
			Long chargingTime = System.currentTimeMillis() - getStartTime();
			this.chargeTicks = (int) (chargingTime / 50);
			playParticles();
		}

		// Checks if the ability is charged
		if (System.currentTimeMillis() > getStartTime() + chargeTime) {

			charged = true;

		}

	}

	// Sets up the boss bar & puts the player in the spiritual energy HashMap
	public void setupPower() {
		if (!powerAmount.containsKey(player.getName().toString())) {
			powerAmount.put(player.getName().toString(), 0);
			bar = Bukkit.createBossBar(
					ChatColor.YELLOW + "" + ChatColor.MAGIC + "I " + ChatColor.GRAY + "" + ChatColor.BOLD
							+ "Spiritual Energy" + ChatColor.YELLOW + "" + ChatColor.MAGIC + " I",
					BarColor.BLUE, BarStyle.SEGMENTED_12);
			bar.addPlayer(player);
		}

	}

	// Updates the spiritual energy HashMap and boss bar
	public void powerProgress() {
		int amountPower = powerAmount.get(player.getName().toString());

		if (amountPower >= 100) {
			player.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Your spiritual connection has fully recharged.");
			remove();
			return;
		}

		powerAmount.put(player.getName().toString(), powerAmount.get(player.getName().toString()) + 1);
		bar.setProgress((float) amountPower / (float) 100);

	}

	// Charging particles
	public void playParticles() {
		int amount = chargeTicks + 1;
		double maxHeight = 2.5;
		double distanceFromPlayer = 1.5;

		int angle = 5 * amount + 5 * ticks;
		double x = Math.cos(Math.toRadians(angle)) * distanceFromPlayer;
		double z = Math.sin(Math.toRadians(angle)) * distanceFromPlayer;
		double height = (amount * 0.10) % maxHeight;
		Location displayLoc = player.getLocation().clone().add(x, height, z);

		int angle2 = 5 * amount + 180 + 5 * ticks;
		double x2 = Math.cos(Math.toRadians(angle2)) * distanceFromPlayer;
		double z2 = Math.sin(Math.toRadians(angle2)) * distanceFromPlayer;
		Location displayLoc2 = player.getLocation().clone().add(x2, height, z2);
		AirAbility.getAirbendingParticles().display(displayLoc2, 0, 0, 0, 0, 3);
		AirAbility.getAirbendingParticles().display(displayLoc, 0, 0, 0, 0, 3);
		AirAbility.playAirbendingSound(player.getLocation());

	}

	@Override
	public void remove() {
		super.remove();
	}

	@Override
	public long getCooldown() {

		return cooldown;
	}

	@Override
	public Location getLocation() {

		return location;
	}

	@Override
	public String getName() {

		return "Meditate";
	}

	@Override
	public boolean isHarmlessAbility() {

		return false;
	}

	@Override
	public boolean isSneakAbility() {

		return true;
	}

	@Override
	public String getAuthor() {

		return null;
	}

	@Override
	public String getVersion() {

		return null;
	}

	@Override
	public void load() {
	}

	@Override
	public void stop() {
	}

}
