package me.ominous.midasswordplugin

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin

class MidasSwordPlugin : JavaPlugin(), Listener {

    override fun onEnable() {
        // Plugin startup logic

        createSwordRecipe()

        server.pluginManager.registerEvents(this, this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    fun createSwordRecipe() {
        val shapedRecipe = ShapedRecipe(NamespacedKey(this, "midas_sword"), getMidasSwordItem())

        shapedRecipe.shape(" E ", "QDQ", " G ")
        shapedRecipe.setIngredient('E', Material.ENCHANTED_GOLDEN_APPLE)
        shapedRecipe.setIngredient('Q', Material.QUARTZ)
        shapedRecipe.setIngredient('D', Material.DIAMOND_SWORD)
        shapedRecipe.setIngredient('G', Material.GOLDEN_APPLE)

        Bukkit.addRecipe(shapedRecipe)
    }

    fun getMidasSwordItem(): ItemStack {
        val midasSword = ItemStack(Material.GOLDEN_SWORD)
        midasSword.addEnchantment(Enchantment.DAMAGE_ALL, 1)
        val midasSwordMeta = midasSword.itemMeta

        midasSwordMeta.displayName(Component.text("Midas Sword").color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
        midasSwordMeta.setCustomModelData(12345678)

        midasSword.setItemMeta(midasSwordMeta)

        return midasSword
    }

    @EventHandler
    fun entityDeathEvent(event: EntityDeathEvent) {
        val entity = event.entity
        val killer = event.entity.killer ?: return

        if(killer !is Player) return
        if(killer.inventory.itemInMainHand.itemMeta?.customModelData != 12345678) return

        val midasSword = killer.inventory.itemInMainHand

        if(midasSword.getEnchantmentLevel(Enchantment.DAMAGE_ALL) >= 50) return
        midasSword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, midasSword.getEnchantmentLevel(Enchantment.DAMAGE_ALL) + 1)
    }
}