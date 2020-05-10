package digital.mywafiuis.smeltingbook;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EnchantCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.isOp()) {
                Player player = (Player) sender;
                ItemStack mainHand = player.getInventory().getItemInMainHand();
                if (!mainHand.equals(new ItemStack(Material.AIR))) {
                    ItemMeta mainHandMetaData = mainHand.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    lore.add(ChatColor.RED + "Smelting I");
                    mainHandMetaData.setLore(lore);
                    mainHand.setItemMeta(mainHandMetaData);
                } else {
                    return false;
                }
                return true;
            }
        }
        return false;
    }
}
