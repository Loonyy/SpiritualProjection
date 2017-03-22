package com.loony.spiritualprojection.multiability;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.AirAbility;
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

	public Spirit(Player player) {
		super(player);

		if (bPlayer.isOnCooldown(this) || !bPlayer.canBendIgnoreBinds(this)) {
			remove();
			return;
		}

		setFields();
		start();

	}

	public void setFields() {
		this.location = player.getLocation();
		this.cooldown = 8000;
		this.speed = 0.6;
		this.duration = 4000;
		this.gameMode = player.getGameMode();
		this.startLocation = player.getLocation().clone();

	}

	@Override
	public void progress() {

		bPlayer.addCooldown(this);
		if (!setTime) {
			setTime = true;
			this.time = System.currentTimeMillis();
		}
		if (System.currentTimeMillis() > time + duration) {
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

		player.setGameMode(GameMode.SPECTATOR);
		player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 5, 5));
		ParticleEffect.CLOUD.display(player.getLocation(), 0, 0, 0, 0, 1);
		player.playSound(player.getEyeLocation(), Sound.ENTITY_TNT_PRIMED, 2, 20);
		player.setVelocity(player.getEyeLocation().getDirection().clone().normalize().multiply(speed));

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

		return true;
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
