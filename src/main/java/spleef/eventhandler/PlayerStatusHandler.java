/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package spleef.eventhandler;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import spleef.Spleef;
import spleef.arena.Arena;
import spleef.arena.structure.StructureManager.DamageEnabled;

import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerStatusHandler implements Listener {

	private Spleef plugin;

	public PlayerStatusHandler(Spleef plugin) {
		this.plugin = plugin;
	}

	// handle damage based on arena settings
	// fall damage is always cancelled
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			Arena arena = plugin.amanager.getPlayerArena(player.getName());
			if (arena == null) {
				return;
			}
			if (!arena.getStatusManager().isArenaRunning()) {
				e.setCancelled(true);
				return;
			}
			if (e.getCause() == DamageCause.FALL) {
				e.setCancelled(true);
				return;
			}
			DamageEnabled status = arena.getStructureManager().getDamageEnabled();
			switch (status) {
				case YES: {
					return;
				}
				case ZERO: {
					e.setDamage(0);
					return;
				}
				case NO: {
					e.setCancelled(true);
					return;
				}
			}
		}
	}

	// cancel all damage to and from spectators
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onDamageByPlayer(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			Player player = (Player) e.getEntity();
			Player damager = (Player) e.getDamager();
			Arena arena = plugin.amanager.getPlayerArena(player.getName());
			if (arena == null) {
				return;
			}
			if (!arena.getStructureManager().getDamageEnabled().toString().equals("NO")) {
				if (damager.getInventory().getItemInMainHand().getType() == Material.AIR && !arena.getStructureManager().isPunchDamage()) {
					e.setCancelled(true);
					return;
				}
			}
			if (arena.getPlayersManager().isSpectator(player.getName()) || arena.getPlayersManager().isSpectator(damager.getName())) {
				e.setCancelled(true);
			}
		}
	}

	// player should have infinite food while in arena
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerDamage(FoodLevelChangeEvent e) {
		if (e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			if (plugin.amanager.getPlayerArena(player.getName()) != null) {
				e.setCancelled(true);
			}
		}
	}

	// give snowballs an impact effect
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onSnowballHit(ProjectileHitEvent e) {
		Projectile projectile = e.getEntity();
		if (!(projectile instanceof Snowball)) {
			return;
		}
		if (e.getHitEntity() == null || e.getHitEntity().getType() != EntityType.PLAYER) {
			return;
		}
		Player player = (Player) e.getHitEntity();
		Arena arena = plugin.amanager.getPlayerArena(player.getName());
		if (arena == null) {
			return;
		}
		if (!arena.getStatusManager().isArenaRunning()) {
			return;
		}
		double knockback = plugin.getConfig().getDouble("items.snowball.knockback", 1.5);
		player.damage(0.5, projectile);
		player.setVelocity(projectile.getVelocity().multiply(knockback));
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onSnowballThrow(ProjectileLaunchEvent e) {
		if (!(e.getEntity() instanceof Snowball)) {
			return;
		}

		Player player = (Player) e.getEntity().getShooter();
		Arena arena = plugin.amanager.getPlayerArena(player.getName());
		if (arena == null) {
			return;
		}
		if (!arena.getStatusManager().isArenaRunning()) {
			return;
		}
		int slot = plugin.getConfig().getInt("items.snowball.slot", 1);
		plugin.getServer().getScheduler().runTaskLater(plugin, () ->
				player.getInventory().setItem(slot, new ItemStack(Material.SNOWBALL)), 1L);
	}
}
