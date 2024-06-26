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

public class LoseLevel {

	private Vector p1 = null;
	private Vector p2 = null;

	public boolean isConfigured() {
		return p1 != null;
	}

	public boolean isLoseLocation(Location loc) {
		return loc.getY() < p1.getBlockY() + 1;
	}

	protected void setLoseLocation(Location p1) {
		this.p1 = p1.toVector();
	}

	/**
	 * Save the lose level vector. P2 is now redundant, so remove from config.
	 *
	 * @param config
	 */
	protected void saveToConfig(FileConfiguration config) {
		config.set("loselevel.p1", p1);
		config.set("loselevel.p2", null);
	}

	/**
	 * Load the lose level vector. P2 is now redundant and is loaded for
	 * compatibility. P1 is set to the higher of the 2 Y values.
	 *
	 * @param config
	 */
	protected void loadFromConfig(FileConfiguration config) {
		p1 = config.getVector("loselevel.p1", null);
		p2 = config.getVector("loselevel.p2", null);
		if (p2 != null) {
			if (p1 != null && p2.getY() > p1.getY()) {
				p1.setY(p2.getY());
			}
		}
	}

}
