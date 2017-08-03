package com.loony.spiritualprojection.multiability;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPig;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.loony.spiritualprojection.utils.NPC;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.SpiritualAbility;

public class Spirit extends SpiritualAbility implements AddonAbility {

	private Location location;
	private long time;
	private long cooldown;
	private long duration;
	private boolean setTime;
	public Location startLocation;
	private int spiritualEnergy;
	public boolean SNPC = false;
	private  NPC npc;

	private Entity pig;

	public Spirit(Player player) {
		super(player);

		if (bPlayer.isOnCooldown(this) || !bPlayer.canBendIgnoreBinds(this)) {

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

				if (startLocation.add(0, -1, 0).getBlock().isEmpty()) {
					Bukkit.broadcastMessage(ChatColor.GRAY + "" + ChatColor.BOLD
							+ "You must not be moving and be standing on solid ground to use this ability.");
					remove();
					return;
				}
				start();
			}

		}
	}

	public void setFields() {

		this.cooldown = SpiritualProjection.config.get().getLong(SpiritualProjection.path + "Spirit.Cooldown");
		this.duration = SpiritualProjection.config.get().getLong(SpiritualProjection.path + "Spirit.Duration");
		this.spiritualEnergy = SpiritualProjection.config.get()
				.getInt(SpiritualProjection.path + "Spirit.SpiritualEnergy");

		this.location = player.getLocation();
		this.startLocation = player.getLocation().clone();

	}

	@Override
	public void progress() {

		if (SNPC == false) {
			spawnNPC();

		}
		bPlayer.addCooldown(this);

		// Starts a time for the ability
		if (!setTime) {
			powerProgress();
			setTime = true;
			this.time = System.currentTimeMillis();
		}

		// Checks if the duration is up
		if (System.currentTimeMillis() > time + duration) {
			player.teleport(startLocation.add(0, 1, 0));
			player.setGlowing(false);
			player.setCollidable(true);
			player.setCanPickupItems(true);
			player.setGliding(false);
			player.setInvulnerable(false);
			player.setFlying(false);
			player.setAllowFlight(false);
			((CraftPig) pig).removePotionEffect(PotionEffectType.INVISIBILITY);
			remove();
			return;

		}

		player.setGlowing(true);
		player.setCanPickupItems(false);
		player.setInvulnerable(true);
		player.setAllowFlight(true);
		player.setFlying(true);
	}

	public void spawnNPC() {

		npc = new NPC(player.getName(), startLocation);

		pig = player.getWorld().spawnEntity(player.getLocation().add(0, -0.9, 0), EntityType.PIG);
		((CraftPig) pig).setInvulnerable(true);
		((CraftPig) pig).setGravity(true);
		PotionEffect poe = new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 0, false, false);

		((LivingEntity) pig).addPotionEffect(poe);

		((Pig) pig).setAI(false);
		npc.setPassenger(pig);

		SNPC = true;
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
		player.setGlowing(false);
		player.setCollidable(true);
		player.setCanPickupItems(true);
		player.setGliding(false);
		player.setInvulnerable(false);
		player.setFlying(false);
		player.setAllowFlight(false);

		if (npc != null) {
			npc.destroy();

		}
		if (pig != null) {
			((CraftPig) pig).removePotionEffect(PotionEffectType.INVISIBILITY);
			pig.remove();
		}
		player.teleport(startLocation.add(0, 1, 0));
		SNPC = false;
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

	public Boolean canBendIgnoreBinds() {
		return true;
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
