package com.loony.spiritualprojection.multiability;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.AirAbility;
import com.projectkorra.projectkorra.util.ParticleEffect;

public class SpiritualDrain extends AirAbility implements AddonAbility {

	private Map<Entity, Location> particleLocation = new HashMap<Entity, Location>();
	public static List<Entity> drainedEntities = new CopyOnWriteArrayList<Entity>();
	private long cooldown;
	private Location location;
	private int range;
	private double drainSpeed;
	private int spiritualEnergy;
	private long drainedDuration;
	private boolean powerTake;

	public SpiritualDrain(Player player) {
		super(player);
		if (bPlayer.isOnCooldown(this) || !bPlayer.canBendIgnoreBinds(this)) {
			remove();
			return;
		}
		setFields();

		// Checks if the player is in the HashMap
		if (SpiritualProjection.powerAmount.containsKey(player.getName().toString())) {

			// Checks if they have enough spiritual energy to use the ability
			if (SpiritualProjection.powerAmount.get(player.getName().toString()) < spiritualEnergy) {
				player.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD
						+ "You do not possess enough spiritual connection to use this ability.");

			}
		}

		// Continues ability if they have enough spiritual energy
		if (SpiritualProjection.powerAmount.get(player.getName().toString()) >= spiritualEnergy) {

			start();
		}

	}

	public void setFields() {
		this.cooldown = SpiritualProjection.config.get().getLong(SpiritualProjection.path + "SpiritualDrain.Cooldown");
		this.range = SpiritualProjection.config.get().getInt(SpiritualProjection.path + "SpiritualDrain.Range");
		this.drainSpeed = SpiritualProjection.config.get().getDouble(SpiritualProjection.path + "SpiritualDrain.DrainSpeed");
		this.spiritualEnergy = SpiritualProjection.config.get().getInt(SpiritualProjection.path + "SpiritualDrain.SpiritualEnergy");
		this.drainedDuration = SpiritualProjection.config.get().getLong(SpiritualProjection.path + "SpiritualDrain.DrainedDuration");
		this.powerTake = false;
		this.location = player.getLocation();

	}

	@Override
	public void progress() {
		bPlayer.addCooldown(this);
		if (!player.isSneaking()) {
			remove();
			return;
		}

		drainPlayers();

	}

	// Gets the players in a radius
	public void drainPlayers() {
		for (Entity entity : player.getNearbyEntities(range, range, range)) {
			if (entity instanceof Player || !entity.getUniqueId().equals(player.getUniqueId())) {

				// Checks if entity has already been drained
				if (drainedEntities.contains(entity)) {

					return;
				}
				// Checks if entity's location is in HashMap
				if (!particleLocation.containsKey(entity)) {
					particleLocation.put(entity, entity.getLocation());
					if (!powerTake) {
						powerProgress();
					}
				}

				Location entityLocation = particleLocation.get(entity);
				entityLocation.setY(player.getEyeLocation().getY());

				// Checks if the particleLocation is at the player's location
				if (entityLocation.distance(player.getEyeLocation()) < 1) {
					drainedEntities.add(entity);
					particleLocation.remove(entity);

					// Removes them from drainedEntities after the duration
					new BukkitRunnable() {

						@Override
						public void run() {
							drainedEntities.remove(entity);
						}

					}.runTaskLater(ProjectKorra.plugin, drainedDuration / 50);

					return;

					// Progresses particles
				} else {
					Vector vec = GeneralMethods.getDirection(entityLocation, player.getEyeLocation());
					entityLocation.add(vec.normalize().multiply(drainSpeed));
					ParticleEffect.SPELL_MOB_AMBIENT.display(entityLocation, 0, 0, 0, 0, 25);
				}

			}

		}
		return;

	}

	// Updates the HashMap spiritual energy & boss bar
	public void powerProgress() {
		powerTake = true;

		int amountPower = SpiritualProjection.powerAmount.get(player.getName().toString());
		SpiritualProjection.powerAmount.put(player.getName().toString(),
				SpiritualProjection.powerAmount.get(player.getName().toString()) - spiritualEnergy);
		if (SpiritualProjection.bar.containsKey(player.getName())) {
			SpiritualProjection.bar.get(player.getName())
					.setProgress((float) (amountPower - spiritualEnergy) / (float) 100);
		}

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

		return "SpiritualDrain";
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
