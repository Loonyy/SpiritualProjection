package com.loony.spiritualprojection.multiability;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.AirAbility;

public class Meditate extends AirAbility implements AddonAbility {

	private long cooldown;
	private boolean charged;
	private long chargeTime;
	private Location location;
	private long time;
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

		if (System.currentTimeMillis() < getStartTime() + chargeTime) {

			Long chargingTime = System.currentTimeMillis() - getStartTime();
			this.chargeTicks = (int) (chargingTime / 50);
			playParticles();
		}

		if (System.currentTimeMillis() > getStartTime() + chargeTime) {

			charged = true;

		}

	}

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
