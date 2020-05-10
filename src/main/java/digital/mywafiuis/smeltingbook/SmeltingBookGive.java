package digital.mywafiuis.smeltingbook;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SmeltingBookGive  implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 1) {
            if (Bukkit.getPlayer(args[0]) != null) {
                if (sender.hasPermission("smeltingbook.giveBook")) {
                    Player target = Bukkit.getPlayer(args[0]);
                    ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
                    ItemMeta bookMeta = book.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    lore.add(ChatColor.RED + "Smelting I");
                    bookMeta.setLore(lore);
                    book.setItemMeta(bookMeta);
                    book.setAmount(1);
                    target.getInventory().addItem(book);
                    if (sender instanceof Player) {
                        sender.sendMessage(ChatColor.RED + "Gave " + sender.getName() + " a Smelting I Enchantment Book.");
                    }
                    return true;
                }
            } else {
                return false;
            }
            return false;
        }
        return false;
    }

}
