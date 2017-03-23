package com.loony.spiritualprojection.multiability;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.AirAbility;

public class Meditate extends AirAbility implements AddonAbility {

	@SuppressWarnings("unused")
	private long time;
	@SuppressWarnings("unused")
	private boolean charged;
	private long cooldown;
	private long duration;
	private Location location;
	private int chargeTicks;
	private int ticks;

	public Meditate(Player player) {
		super(player);

		if (bPlayer.canBendIgnoreBinds(this)) {
			setFields();

			start();

		}

	}

	public void setFields() {

		this.duration = SpiritualProjection.config.get().getInt(SpiritualProjection.path + "Meditate.Duration");
		this.cooldown = SpiritualProjection.config.get().getInt(SpiritualProjection.path + "Meditate.Cooldown");
		this.charged = false;
		this.location = player.getLocation();
		this.time = System.currentTimeMillis();
	}

	@Override
	public void progress() {
		if (player.isDead() || !player.isOnline() || !player.isSneaking()) {

			remove();
			return;
		}
		bPlayer.addCooldown(this);

		ticks++;

		// Checks if ability is still charging
		if (System.currentTimeMillis() < getStartTime() + duration) {
			powerProgress();
			Long chargingTime = System.currentTimeMillis() - getStartTime();
			this.chargeTicks = (int) (chargingTime / 50);
			playParticles();
		}

		// Checks if the ability is charged
		if (System.currentTimeMillis() > getStartTime() + duration) {

			charged = true;

		}

	}

	// Updates the spiritual energy HashMap and boss bar
	public void powerProgress() {
		int amountPower = SpiritualProjection.powerAmount.get(player.getName().toString());

		if (amountPower >= 100) {
			player.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Your spiritual connection has fully recharged.");
			remove();
			return;
		}

		SpiritualProjection.powerAmount.put(player.getName().toString(),
				SpiritualProjection.powerAmount.get(player.getName().toString()) + 1);

		if (SpiritualProjection.bar.containsKey(player.getName())) {
			SpiritualProjection.bar.get(player.getName()).setProgress((float) amountPower / (float) 100);
		}

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

		return true;
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
