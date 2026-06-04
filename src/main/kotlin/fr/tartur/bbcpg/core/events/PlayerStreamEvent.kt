package fr.tartur.bbcpg.core.events

import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import net.kyori.adventure.text.Component.text as text

class PlayerStreamEvent : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.joinMessage(text("[", NamedTextColor.DARK_GRAY)
            .append(text("+", NamedTextColor.GREEN))
            .append(text("]", NamedTextColor.DARK_GRAY))
            .append(text(" " + event.player.name, NamedTextColor.YELLOW)))
        Bukkit.getOnlinePlayers().forEach { it.playSound(it, Sound.BLOCK_NOTE_BLOCK_PLING, 3.0f, 1f) }
    }

    @EventHandler
    fun onPlayerLeave(event: PlayerQuitEvent) {
        event.quitMessage(text("[", NamedTextColor.DARK_GRAY)
            .append(text("-", NamedTextColor.RED))
            .append(text("]", NamedTextColor.DARK_GRAY))
            .append(text(" " + event.player.name, NamedTextColor.YELLOW)))
        Bukkit.getOnlinePlayers().forEach { it.playSound(it, Sound.ENTITY_ITEM_PICKUP, 3.0f, 3.0f) }
    }
}