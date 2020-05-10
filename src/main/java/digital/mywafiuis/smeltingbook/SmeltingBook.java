package digital.mywafiuis.smeltingbook;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.prefs.Preferences;


public final class SmeltingBook extends JavaPlugin implements Listener {

    public void alert(String message) {
        getServer().getConsoleSender().sendMessage(message);
    }

    public static class configVariables {
        public static Integer dropPercentage = null;
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        configVariables.dropPercentage = this.getConfig().getInt("dropPercentage");
        alert(String.valueOf(configVariables.dropPercentage));
        alert("Akkoza's Auto Smelting Plugin loaded");
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(this, this);

        //this.getCommand("testlore").setExecutor(new EnchantCommand());
        this.getCommand("smeltingbook").setExecutor(new SmeltingBookGive());
    }

//    public void registerGlow() {
//        try {
//            Field f = Enchantment.class.getDeclaredField("acceptingNew");
//            f.setAccessible(true);
//            f.set(null, true);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            SmeltingBookEnchantment glow = new SmeltingBookEnchantment(70);
//            Enchantment.registerEnchantment(glow);
//        }
//        catch (IllegalArgumentException e){
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
//    }


    @EventHandler
    public void BlockBroken(BlockBreakEvent e) {
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
            Random rand = new Random();
            int randomInt = rand.nextInt(100*5000);
            alert(String.valueOf(randomInt));
            if (randomInt >= 1 && randomInt <= configVariables.dropPercentage*5000) {
                ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta bookMeta = book.getItemMeta();
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.RED + "Smelting I");
                bookMeta.setLore(lore);
                book.setItemMeta(bookMeta);
                book.setAmount(1);
                e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), book);
            } //else {
           //     alert(toString(randomInt));
            //}

            ItemStack PlayerItemInHand = e.getPlayer().getInventory().getItemInMainHand();
            Integer smeltingLevel = 0;
            if (PlayerItemInHand.getType() != Material.AIR) {
                ItemMeta ItemMetaData = PlayerItemInHand.getItemMeta();
                if (ItemMetaData.hasLore() != false) {
                    List<String> itemLore = ItemMetaData.getLore();
                    for (int i = 0; i < itemLore.size(); i++) {
                        String currentLore = itemLore.get(i);
                        if (currentLore.equals(ChatColor.RED + "Smelting I")) {
                            smeltingLevel = 1;
                        }
                    }
                }

                if (smeltingLevel == 1) {
                    Block item = e.getBlock();
                    ItemStack result = null;
                    Iterator<Recipe> iter = Bukkit.recipeIterator();
                    while (iter.hasNext()) {
                        Recipe recipe = iter.next();
                        if (!(recipe instanceof FurnaceRecipe)) continue;
                        if (((FurnaceRecipe) recipe).getInput().getType() != item.getType()) continue;
                        result = recipe.getResult();
                        break;
                    }

                    if (result != null) {
                        ItemStack itemDrop = new ItemStack(result);
                        ItemStack tool = e.getPlayer().getInventory().getItemInMainHand();
                        ItemMeta toolMeta = tool.getItemMeta();
                        if (toolMeta instanceof Damageable) {
                            ((Damageable) toolMeta).setDamage(((Damageable) toolMeta).getDamage() + 2);
                            e.getBlock().setType(Material.AIR);
                            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), itemDrop);
                            tool.setItemMeta(toolMeta);
                        }
                    }
                }
            }
//            if (smeltingLevel == 1) {
//                if (e.getBlock().getType() == Material.IRON_ORE) {
//                    ItemStack ironDrop = new ItemStack(Material.IRON_INGOT);
//                    e.getBlock().setType(Material.AIR);
//                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), ironDrop);
//                } else if (e.getBlock().getType() == Material.GOLD_ORE) {
//                    ItemStack goldDrop = new ItemStack(Material.GOLD_INGOT);
//                    e.getBlock().setType(Material.AIR);
//                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), goldDrop);
//                } else if (e.getBlock().getType() == Material.STONE || e.getBlock().getType() == Material.COBBLESTONE) {
//                    ItemStack stoneDrop = new ItemStack(Material.STONE);
//                    e.getBlock().setType(Material.AIR);
//                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), stoneDrop);
//                }
//            }
        }
    }

    @EventHandler
    public void useEnchant(PlayerInteractEvent interaction) {
        Player player = interaction.getPlayer();
        if (player.getInventory().getItemInOffHand().getType() != Material.AIR && player.getInventory().getItemInMainHand().getType() != Material.AIR) {
            ItemStack offhand = player.getInventory().getItemInOffHand();
            ItemMeta offhandMeta = offhand.getItemMeta();

            ItemStack mainHand = player.getInventory().getItemInMainHand();
            ItemMeta mainHandMeta = mainHand.getItemMeta();
            Material mhType = mainHand.getType();
            if (interaction.getAction() == Action.RIGHT_CLICK_AIR) {
                //alert("Player rightclicked the air");
                if (
                        mhType == Material.DIAMOND_PICKAXE ||
                                mhType == Material.GOLDEN_PICKAXE ||
                                mhType == Material.IRON_PICKAXE ||
                                mhType == Material.STONE_PICKAXE ||
                                mhType == Material.WOODEN_PICKAXE || // end of checking the damn pickaxes lmao.

                                mhType == Material.DIAMOND_AXE ||
                                mhType == Material.GOLDEN_AXE ||
                                mhType == Material.IRON_AXE ||
                                mhType == Material.STONE_AXE ||
                                mhType == Material.WOODEN_AXE || // end of freaking axes.

                                mhType == Material.DIAMOND_SHOVEL ||
                                mhType == Material.GOLDEN_SHOVEL ||
                                mhType == Material.IRON_SHOVEL ||
                                mhType == Material.STONE_SHOVEL ||
                                mhType == Material.WOODEN_SHOVEL // end of all the checks in entirety
                ) {

                    Boolean isSmeltingBook = false;
                    if (offhandMeta.hasLore() && offhand.getType() == Material.ENCHANTED_BOOK) {
                        for (int i = 0; i < offhandMeta.getLore().size(); i++) {
                            String currentLore = offhandMeta.getLore().get(i);
                            if (currentLore.equals(ChatColor.RED + "Smelting I")) {
                                isSmeltingBook = true;
                            }
                        }
                    }

                    if (isSmeltingBook == true) {
                        Boolean hasSmeltingApplied = false;
                        List<String> appliedLore = new ArrayList<>();
                        if (mainHandMeta.hasLore()) {
                            appliedLore = mainHandMeta.getLore();
                        }

                        for (int i = 0; i < appliedLore.size(); i++) {
                            String current = appliedLore.get(i);
                            if (current.equals(ChatColor.RED + "Smelting I")) {
                                hasSmeltingApplied = true;
                            }
                        }

                        if (hasSmeltingApplied == false) {
                            appliedLore.add(ChatColor.RED + "Smelting I");
                            mainHandMeta.setLore(appliedLore);
                            mainHand.setItemMeta(mainHandMeta);
                            offhand.setAmount(0);
                            player.sendMessage(ChatColor.GREEN + "You have applied Smelting I to your tool.");
                        } else {
                            player.sendMessage(ChatColor.RED + "You already have Smelting I applied to this tool.");
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onDisable() {
        this.saveConfig();
    }
}
