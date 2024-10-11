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

package spleef.arena.structure;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.Vector;

public class WaitingSpawn {

	private Vector p1 = null;
	private float yaw;
	private float pitch;

	protected Vector getVector() {
		return p1;
	}

	protected float getYaw() {
		return yaw;
	}

	protected float getPitch() {
		return pitch;
	}

	protected boolean isConfigured() {
		return p1 != null;
	}

	protected void setWaitingSpawn(Location loc) {
		p1 = loc.toVector();
		yaw = loc.getYaw();
		pitch = loc.getPitch();
	}

	protected void remove() {
		p1 = null;
	}

	/**
	 * Save the waiting spawn vector.
	 *
	 * @param config
	 */
	protected void saveToConfig(FileConfiguration config) {
		if (!isConfigured()) {
			config.set("waitingspawn", null);
			return;
		}
		config.set("waitingspawn.p1", p1);
		config.set("waitingspawn.yaw", yaw);
		config.set("waitingspawn.pitch", pitch);
	}

	/**
	 * Load the waiting spawn vector.
	 *
	 * @param config
	 */
	protected void loadFromConfig(FileConfiguration config) {
		p1 = config.getVector("waitingspawn.p1", null);
		yaw = (float) config.getDouble("waitingspawn.yaw", 0.0);
		pitch = (float) config.getDouble("waitingspawn.pitch", 0.0);
	}
}
