package be.ketsu.bingo.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;

public class ItemBuilder implements Cloneable {
    private final ItemStack is;

    /**
     * Create a new ItemBuilder from scratch.
     *
     * @param material The material to create the ItemBuilder with.
     */
    @ConstructorProperties({"material"})
    public ItemBuilder(Material material) {
        this(material, 1);
    }

    /**
     * Create a new ItemBuilder over an existing itemstack.
     *
     * @param is The itemstack to create the ItemBuilder over.
     */
    public ItemBuilder(ItemStack is) {
        this.is = is;
    }

    /**
     * Create a new ItemBuilder from scratch.
     *
     * @param m      The material of the item.
     * @param amount The amount of the item.
     */
    public ItemBuilder(Material m, int amount) {
        is = new ItemStack(m, amount);
    }

    /**
     * Create a new ItemBuilder from scratch with data.
     *
     * @param m      The material of the item.
     * @param amount The amount of the item.
     * @param data   data like color, alternative.
     */
    public ItemBuilder(Material m, int amount, short data) {
        is = new ItemStack(m, amount, data);
    }


    /**
     * Clone the ItemBuilder into a new one.
     *
     * @return The cloned instance.
     */
    @Override
    public ItemBuilder clone() {
        return new ItemBuilder(is);
    }

    /**
     * Set the displayname of the item.
     *
     * @param name The name to change it to.
     * @return
     */
    public ItemBuilder setName(String name) {
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Set items unbreakable.
     *
     * @param name The name to change it to.
     * @return
     */
    public ItemBuilder setUnbreakable() {
        ItemMeta im = is.getItemMeta();
        im.setUnbreakable(true);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Make item glow
     *
     * @return
     */
    public ItemBuilder addGLow() {
        is.addUnsafeEnchantment(Enchantment.LURE, 1);
        ItemMeta im = is.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Add a lore line.
     *
     * @param line The lore line to add.
     */
    public ItemBuilder addLoreLine(String line) {
        ItemMeta im = is.getItemMeta();
        List<String> lore = im.hasLore() ? new ArrayList<>(im.getLore()) : new ArrayList<>();
        lore.add(line);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Add a lore line.
     *
     * @param line The lore line to add.
     * @param pos  The index of where to put it.
     */
    public ItemBuilder addLoreLine(String line, int pos) {
        ItemMeta im = is.getItemMeta();
        List<String> lore = im.hasLore() ? new ArrayList<>(im.getLore()) : new ArrayList<>();
        lore.set(pos, line);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Add multi lore line
     *
     * @param lines The lore line to add
     */
    public ItemBuilder addLoreLine(List<String> lines) {
        ItemMeta im = is.getItemMeta();
        List<String> lore = im.hasLore() ? new ArrayList<>(im.getLore()) : new ArrayList<>();
        lore.addAll(lines);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Return lore line
     */
    public List<String> getLoreLine() {
        return is.getItemMeta().getLore();
    }

    /**
     * Retrieves the itemstack from the ItemBuilder.
     *
     * @return The itemstack created/modified by the ItemBuilder instance.
     */
    public ItemStack build() {
        return is;
    }
}