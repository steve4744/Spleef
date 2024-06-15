package spleef.arena.structure;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.scheduler.BukkitRunnable;
import spleef.Spleef;
import spleef.arena.Arena;

public class GameZone {

	private LinkedList<BlockState> blocks = new LinkedList<>();

	public Arena arena;

	public GameZone(Arena arena){
		this.arena = arena;
	}

	public void handleBlockBreak(Block block) {
		Spleef.getInstance().getSound().BLOCK_BREAK(block);
		block.getDrops().clear();
		removeGLBlocks(block);
	}

	public void regenNow() {
		Iterator<BlockState> bsi = blocks.iterator();
		while (bsi.hasNext()) {
			BlockState bs = bsi.next();
			bs.update(true);
			bsi.remove();
		}
	}

	private void removeGLBlocks(Block block) {
		blocks.add(block.getState());
	}

	private final int MAX_BLOCKS_PER_TICK = 10;

	/**
	 * Regenerate the broken blocks in the arena.
	 *
	 * @return delay in ticks before arena regeneration begins.
	 */
	public int regen() {
		final Iterator<BlockState> bsit = blocks.iterator();
		new BukkitRunnable() {
			@Override
			public void run() {
				for(int i = MAX_BLOCKS_PER_TICK; i >= 0;i--){
					if(bsit.hasNext()){
						try {
							BlockState bs = bsit.next();
							bs.update(true);
							bsit.remove();
						} catch(ConcurrentModificationException ex) {

						}
					} else {
						cancel();
					}
				}
			}
		}.runTaskTimer(Spleef.getInstance(), 0L, 1L);

		return arena.getStructureManager().getRegenerationDelay();
	}
}
