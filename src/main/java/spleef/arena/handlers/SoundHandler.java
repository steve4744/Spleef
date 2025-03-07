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

package spleef.arena.handlers;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import spleef.Spleef;
import spleef.utils.Sounds;

public class SoundHandler extends Sounds {
	
	private Spleef plugin;

	public SoundHandler (Spleef plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void NOTE_PLING(Player p, float volume, float pitch) {
		NamespacedKey key = getKey("block_note_block_pling");
		p.playSound(p.getLocation(), Registry.SOUNDS.get(key), volume, pitch);
	}

	@Override
	public void ARENA_START(Player p) {
		if (isSoundEnabled("arenastart")) {
			p.playSound(p.getLocation(), getSound("arenastart"), getVolume("arenastart"), getPitch("arenastart"));
		}
	}

	@Override
	public void ITEM_SELECT(Player p) {
		if (isSoundEnabled("itemselect")) {
			p.playSound(p.getLocation(), getSound("itemselect"), getVolume("itemselect"), getPitch("itemselect"));
		}
	}

	@Override
	public void INVITE_MESSAGE(Player p) {
		if (isSoundEnabled("invitationmessage")) {
			p.playSound(p.getLocation(), getSound("invitationmessage"), getVolume("invitationmessage"), getPitch("invitationmessage"));
		}
	}

	@Override
	public void BLOCK_BREAK(Block fblock) {
		if (isSoundEnabled("blockbreak")) {
			fblock.getWorld().playSound(fblock.getLocation(), getSound("blockbreak"), getVolume("blockbreak"), getPitch("blockbreak"));
		}
	}
	/**
	 * Get the sound to be played.
	 * Will return null if invalid.
	 *
	 * @param string path
	 * @return sound
	 */
	private Sound getSound(String path) {
		NamespacedKey key = getKey(plugin.getConfig().getString("sounds." + path + ".sound"));
		return Registry.SOUNDS.get(key);
	}

	/**
	 * Get the volume of the sound to be played.
	 * Default is 1.0F
	 *
	 * @param string path
	 * @return volume
	 */
	private float getVolume(String path) {
		float volume = (float) plugin.getConfig().getDouble("sounds." + path + ".volume", 1.0);
		return volume > 0 ? volume : 1.0F;
	}

	/**
	 * Get the pitch of the sound to be played.
	 * Default is 1.0F
	 *
	 * @param string path
	 * @return pitch
	 */
	private float getPitch(String path) {
		float pitch = (float) plugin.getConfig().getDouble("sounds." + path + ".pitch", 1.0);
		return (pitch > 0.5 && pitch < 2.0) ? pitch : 1.0F;
	}

	private boolean isSoundEnabled(String path) {
		return plugin.getConfig().getBoolean("sounds." + path + ".enabled");
	}

	private NamespacedKey getKey(String keyString) {
		return NamespacedKey.minecraft(keyString.toLowerCase().replaceAll("_",  "."));
	}
}
