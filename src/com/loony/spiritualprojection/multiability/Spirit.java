package com.loony.spiritualprojection.multiability;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.SpiritualAbility;
import com.projectkorra.projectkorra.util.ParticleEffect;

public class Spirit extends SpiritualAbility implements AddonAbility {

	private Location location;
	private long time;
	private long cooldown;
	private double speed;
	private long duration;
	private boolean setTime;
	private GameMode gameMode;
	private Location startLocation;
	private int spiritualEnergy;
	private boolean canSpiritTransfer;

	public Spirit(Player player) {
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

			// Continues ability if they have enough spiritual energy
			if (SpiritualProjection.powerAmount.get(player.getName().toString()) >= spiritualEnergy) {

				start();
			}

		}
	}

	public void setFields() {

		this.cooldown = SpiritualProjection.config.get().getLong(SpiritualProjection.path + "Spirit.Cooldown");
		this.speed = SpiritualProjection.config.get().getDouble(SpiritualProjection.path + "Spirit.Speed");
		this.duration = SpiritualProjection.config.get().getLong(SpiritualProjection.path + "Spirit.Duration");
		this.spiritualEnergy = SpiritualProjection.config.get()
				.getInt(SpiritualProjection.path + "Spirit.SpiritualEnergy");
		this.canSpiritTransfer = SpiritualProjection.config.get()
				.getBoolean(SpiritualProjection.path + "Spirit.SpiritTransfer");
		this.location = player.getLocation();
		this.gameMode = player.getGameMode();
		this.startLocation = player.getLocation().clone();

	}

	@Override
	public void progress() {

		bPlayer.addCooldown(this);
		if (!player.isSneaking()) {
			if (!canSpiritTransfer) {
				player.teleport(startLocation);
				player.setGameMode(gameMode);

			}
			if (player.getLocation().getBlock().getType() != Material.AIR) {
				player.teleport(startLocation);
				player.setGameMode(gameMode);
				player.sendMessage(ChatColor.RED + "Spirit transfer failed.");

				remove();
				return;

			} else {

				player.setGameMode(gameMode);
				remove();
				return;
			}

		}
		// Starts a time for the ability
		if (!setTime) {
			powerProgress();
			setTime = true;
			this.time = System.currentTimeMillis();
		}

		// Checks if the duration is up
		if (System.currentTimeMillis() > time + duration) {
			if (!canSpiritTransfer) {
				player.teleport(startLocation);
				player.setGameMode(gameMode);

			}
			if (player.getLocation().getBlock().getType() != Material.AIR
					|| GeneralMethods.isRegionProtectedFromBuild(player, location)) {
				player.teleport(startLocation);
				player.setGameMode(gameMode);
				player.sendMessage(ChatColor.RED + "Spirit transfer failed.");

				remove();
				return;

			} else {

				player.setGameMode(gameMode);
				remove();
				return;
			}
		}

		// Starts ability
		player.setGameMode(GameMode.SPECTATOR);
		player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 5, 5));
		ParticleEffect.CLOUD.display(player.getLocation(), 0, 0, 0, 0, 1);
		player.playSound(player.getEyeLocation(), Sound.ENTITY_TNT_PRIMED, 2, 20);
		player.setVelocity(
				player.getEyeLocation().getDirection().clone().normalize().multiply(speed).add(new Vector(0, 0.25, 0)));

	}

	// Updates the HashMap spiritual energy & boss bar
	public void powerProgress() {

		int amountPower = SpiritualProjection.powerAmount.get(player.getName().toString());
		SpiritualProjection.powerAmount.put(player.getName().toString(),
				SpiritualProjection.powerAmount.get(player.getName().toString()) - spiritualEnergy);
		if (SpiritualProjection.bar.containsKey(player.getName())) {
			SpiritualProjection.bar.get(player.getName())
					.setProgress((float) (amountPower - spiritualEnergy) / (float) 100);
		}

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

		return "Spirit";
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
	public boolean isHiddenAbility() {
		return true;
	}

	@Override
	public String getAuthor() {

		return "Loony";
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
