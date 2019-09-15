package world.bentobox.magiccobblestonegenerator.listeners;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;


import world.bentobox.magiccobblestonegenerator.StoneGeneratorAddon;

public class MainGeneratorListener implements Listener
{
	private final BlockFace[] faces;
	public MainGeneratorListener(StoneGeneratorAddon addon)
	{
		this.addon = addon;
		this.faces = new BlockFace[] { BlockFace.SELF, BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onBlockFromToEvent(BlockFromToEvent event)
	{
		Block eventSourceBlock = event.getBlock();
		final Block source = event.getBlock();
		final Block to = event.getToBlock();
		if (!this.addon.getManager().canOperateInWorld(eventSourceBlock.getWorld())) {
			return;
		}
		if (!this.addon.getManager().isMembersOnline(eventSourceBlock.getLocation())) {
			return;
		}

		if (source.getType() == Material.WATER || source.getType() == Material.LAVA) {
			if ((to.getType() == Material.AIR || to.getType() == Material.WATER) && this.generatesCobble(source.getType(), to)) {
				if ((source.getType() == Material.LAVA) && !this.isSurroundedByWater(to.getLocation())) {
					return;
				}
				event.setCancelled(this.addon.getGenerator().isReplacementGenerated(to));
			}
		}
	}


	// ---------------------------------------------------------------------
	// Section: Private Methods
	// ---------------------------------------------------------------------

	private boolean generatesCobble(final Material material, final Block b) {
		final Material mirMat1 = (material == Material.WATER) ? Material.LAVA : Material.WATER;
		for (final BlockFace face : faces) {
			final Block check = b.getRelative(face, 1);
			if (check.getType() == mirMat1) {
				return true;
			}
		}
		return false;
	}

	private boolean isSurroundedByWater(final Location fromLoc) {
		final Block[] array;
		final Block[] blocks = array = new Block[] { fromLoc.getWorld().getBlockAt(fromLoc.getBlockX() + 1, fromLoc.getBlockY(), fromLoc.getBlockZ()), fromLoc.getWorld().getBlockAt(fromLoc.getBlockX() - 1, fromLoc.getBlockY(), fromLoc.getBlockZ()), fromLoc.getWorld().getBlockAt(fromLoc.getBlockX(), fromLoc.getBlockY(), fromLoc.getBlockZ() + 1), fromLoc.getWorld().getBlockAt(fromLoc.getBlockX(), fromLoc.getBlockY(), fromLoc.getBlockZ() - 1) };
		for (final Block b : array) {
			if (b.getType() == Material.WATER) {
				return true;
			}
		}
		return false;
	}
	private StoneGeneratorAddon addon;
}
