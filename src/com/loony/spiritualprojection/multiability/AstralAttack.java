package com.loony.spiritualprojection.multiability;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.AirAbility;
import com.projectkorra.projectkorra.ability.SpiritualAbility;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.ParticleEffect;

public class AstralAttack extends SpiritualAbility implements AddonAbility {

	ArrayList<Integer> stands = new ArrayList<Integer>();
	@SuppressWarnings("unused")
	private Location standLocation;
	private boolean spawnStand;
	private ArmorStand stand;
	private long time;
	private double timefactor;
	private long duration;
	private double speed;
	private Location location;
	private boolean setTime;
	private long cooldown;
	private double damage;
	private double knockback;
	private double radius;
	private int spiritualEnergy;

	public AstralAttack(Player player) {
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
		
		this.duration = SpiritualProjection.config.get().getLong(SpiritualProjection.path + "AstralAttack.Duration");
		this.speed = SpiritualProjection.config.get().getDouble(SpiritualProjection.path + "AstralAttack.Speed");
		this.cooldown = SpiritualProjection.config.get().getLong(SpiritualProjection.path + "AstralAttack.Cooldown");
		this.damage = SpiritualProjection.config.get().getDouble(SpiritualProjection.path + "AstralAttack.Damage");
		this.knockback = SpiritualProjection.config.get().getDouble(SpiritualProjection.path + "AstralAttack.Knockback");
		this.radius = SpiritualProjection.config.get().getDouble(SpiritualProjection.path + "AstralAttack.BlastRadius");;
		this.spiritualEnergy = SpiritualProjection.config.get().getInt(SpiritualProjection.path + "AstralAttack.SpiritualEnergy");;
		this.time = System.currentTimeMillis();
		this.location = player.getLocation();
		this.setTime = false;
	}

	@Override
	public void progress() {
		if (!player.isSneaking()) {
			removeStand();
			remove();
			return;
		}
		bPlayer.addCooldown(this);

		if (!setTime) {
			setTime = true;
			this.time = System.currentTimeMillis();
		}

		if (!spawnStand) {
			powerProgress();
			Stand(location);

		}

		for (Entity entity : GeneralMethods.getEntitiesAroundPoint(stand.getLocation(), radius)) {
			affect(entity);
			if (entity instanceof LivingEntity) {
				break;
			}
		}

		if (System.currentTimeMillis() > time + duration) {

			removeStand();
			remove();
			return;
		}

		progressStand();

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

	public void progressStand() {
		timefactor = 1 - (System.currentTimeMillis() - time) / (speed * duration);
		Vector velocity = player.getEyeLocation().getDirection().clone().normalize().multiply(speed * timefactor);
		stand.setVelocity(velocity);
		AirAbility.playAirbendingParticles(stand.getLocation(), 10, 0, 0, 0);
		AirAbility.playAirbendingParticles(stand.getEyeLocation(), 10, 0, 0, 0);
		AirAbility.playAirbendingParticles(stand.getLocation().add(1, 0, 0), 10, 0, 0, 0);
		AirAbility.playAirbendingParticles(stand.getLocation().add(0, 0, 1), 10, 0, 0, 0);
		AirAbility.playAirbendingParticles(stand.getEyeLocation().add(1, 0, 0), 10, 0, 0, 0);
		AirAbility.playAirbendingParticles(stand.getEyeLocation().add(1, 0, 1), 10, 0, 0, 0);

	}

	public ArmorStand Stand(Location location) {
		this.stand = location.getWorld().spawn(location, ArmorStand.class);
		stand.setGravity(true);
		stand.setSmall(true);
		ItemStack helm = new ItemStack(Material.LEATHER_HELMET, 1);
		ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);

		LeatherArmorMeta metaHead = (LeatherArmorMeta) helm.getItemMeta();
		LeatherArmorMeta metaChest = (LeatherArmorMeta) chest.getItemMeta();
		LeatherArmorMeta metaLegs = (LeatherArmorMeta) legs.getItemMeta();
		LeatherArmorMeta metaBottom = (LeatherArmorMeta) boots.getItemMeta();

		metaHead.setColor(Color.WHITE);
		metaChest.setColor(Color.WHITE);
		metaLegs.setColor(Color.WHITE);
		metaBottom.setColor(Color.WHITE);

		helm.setItemMeta(metaHead);
		chest.setItemMeta(metaChest);
		legs.setItemMeta(metaLegs);
		boots.setItemMeta(metaBottom);

		stand.setHelmet(helm);
		stand.setChestplate(chest);
		stand.setLeggings(legs);
		stand.setBoots(boots);
		stand.setSmall(false);
		stand.setVisible(false);
		stand.setMarker(true);
		// stand.setCustomName("§5Rewards");
		stand.setCustomNameVisible(false);
		stands.add(stand.getEntityId());
		this.spawnStand = true;
		this.standLocation = stand.getLocation();

		return stand;
	}

	private void affect(Entity entity) {

		if (!entity.getUniqueId().equals(player.getUniqueId()) && entity != stand) {

			GeneralMethods.setVelocity(entity, player.getLocation().getDirection().clone().multiply(knockback));
			if (entity instanceof LivingEntity) {
				DamageHandler.damageEntity(entity, damage, this);
				AirAbility.breakBreathbendingHold(entity);
				ParticleEffect.EXPLOSION_HUGE.display(stand.getLocation(), 0, 0, 0, 0, 5);
				AirAbility.playAirbendingSound(stand.getLocation());
				removeStand();
				remove();

			}
		}
	}

	public void removeStand() {
		for (World world : Bukkit.getServer().getWorlds()) {
			for (org.bukkit.entity.Entity entity : world.getEntities()) {
				if (stands.contains(entity.getEntityId())) {
					entity.remove();
				}
			}
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

		return "AstralAttack";
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
