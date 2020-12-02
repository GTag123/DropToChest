package live.tagir.mc.droptochest;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DeathHandler implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        List<ItemStack> drops = e.getDrops();
        Player p = e.getEntity();

        if (drops.size() == 0) {
            p.sendMessage("Вы умерли. У вас не было вещей");
            return;
        }

        Location pos1 = p.getLocation();
        Block block1 = pos1.getBlock();
        Material old1 = block1.getType();
        block1.setType(Material.CHEST);
        Chest chest1 = (Chest) block1.getState();

        chest1.setCustomName(ChatColor.RED + "" + ChatColor.BOLD + p.getName() + ChatColor.RESET + "'s drop");
        chest1.update(true, true);

        ItemStack[] dropsarray1 = new ItemStack[27];

        int firstchestsize = old1 == Material.AIR ? 27 : 26;

        if (drops.size() > firstchestsize){
            Location pos2 = pos1.clone();
            pos2.setX(pos1.getX() + (double) 1);

            Block block2 = pos2.getBlock();

            Material old2 = block2.getType();

            org.bukkit.block.data.type.Chest chestData1;
            org.bukkit.block.data.type.Chest chestData2;

            block2.setType(Material.CHEST);

            Chest chest2 = (Chest) block2.getState();

            chest2.setCustomName(ChatColor.RED + "" + ChatColor.BOLD + p.getName() + ChatColor.RESET + "'s drop");
            chest2.update(true, true);

            chestData1 = (org.bukkit.block.data.type.Chest) chest1.getBlockData();
            chestData2 = (org.bukkit.block.data.type.Chest) chest2.getBlockData();
            chestData1.setType(org.bukkit.block.data.type.Chest.Type.LEFT);
            block1.setBlockData(chestData1);
            chestData2.setType(org.bukkit.block.data.type.Chest.Type.RIGHT);
            block2.setBlockData(chestData2);

            ItemStack[] dropsarray2 = new ItemStack[drops.size() - 27];
            int i = 0;
            for (ItemStack item: drops) {
                if (i < 27) {
                    dropsarray1[i] = item;
                } else {
                    dropsarray2[i - 27] = item;
                }
                i++;
            }

            chest1.getInventory().setContents(dropsarray1);
            chest2.getInventory().setContents(dropsarray2);
            chest2.getInventory().addItem(new ItemStack(old1));
            chest2.getInventory().addItem(new ItemStack(old2));
        }
        else {
            int i = 0;
            for (ItemStack item: drops) {
                dropsarray1[i] = item;
                i++;
            }
            chest1.getInventory().setContents(dropsarray1);
            if (old1 != Material.AIR) chest1.getInventory().addItem(new ItemStack(old1));

        }
        drops.clear();
        p.sendMessage("Вы умерли. Не забудьте забрать свои вещи на координатах:\n" + ChatColor.GREEN + "X: " + ChatColor.RED + block1.getX() +
                ChatColor.GREEN + " Y: " + ChatColor.RED + block1.getY() + ChatColor.GREEN + " Z: " + ChatColor.RED + block1.getZ());
    }
}
