package fr.tartur.bbcpg.core.events

import fr.tartur.bbcpg.core.data.User
import fr.tartur.bbcpg.core.data.UserManager
import fr.tartur.bbcpg.core.util.display.Message.message
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import net.kyori.adventure.text.Component.text as text

class PlayerStreamEvent(private val players: UserManager) : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val user = players.connect(event.player)
        val players = Bukkit.getOnlinePlayers()

        if (user.new) {
            players.forEach { it.playSound(it, Sound.ENTITY_PLAYER_LEVELUP, 3f, 1f) }
        }

        event.joinMessage(joinMessage(user))
        players.forEach { it.playSound(it, Sound.BLOCK_NOTE_BLOCK_PLING, 3f, 1f) }
    }

    @EventHandler
    fun onPlayerLeave(event: PlayerQuitEvent) {
        players.leave(event.player, true)
        event.quitMessage(quitMessage(event.player))
        Bukkit.getOnlinePlayers().forEach { it.playSound(it, Sound.ENTITY_ITEM_PICKUP, 3f, 1f) }
    }

    fun joinMessage(user: User): Component = text {
        if (user.new) {
            it.append(message("<rainbow>Bienvenue à <white><bold>${user.spigot.name}</bold></white> parmi " +
                    "nous !</rainbow>\n"))
        }

        it.append(message("<gray>[<green>+</green>]</gray> <yellow>${user.spigot.name}</yellow>"))
    }

    fun quitMessage(player: Player): Component = message(
        "<gray>[<red>-</red>]</gray> <yellow>${player.name}</yellow>"
    )
}