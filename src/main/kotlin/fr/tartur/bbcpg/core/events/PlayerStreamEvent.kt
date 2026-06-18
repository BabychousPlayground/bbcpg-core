package fr.tartur.bbcpg.core.events

import fr.tartur.bbcpg.core.data.UserManager
import fr.tartur.bbcpg.core.util.display.MENU_SELECTOR
import fr.tartur.bbcpg.core.util.display.joinMessage
import fr.tartur.bbcpg.core.util.display.quitMessage
import fr.tartur.bbcpg.core.util.display.setItem
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class PlayerStreamEvent(private val players: UserManager) : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val user = players.connect(player)

        val players = Bukkit.getOnlinePlayers()

        if (user.new) {
            players.forEach { it.playSound(it, Sound.ENTITY_PLAYER_LEVELUP, 3f, 1f) }
        }

        event.joinMessage(joinMessage(user))
        players.forEach { it.playSound(it, Sound.BLOCK_NOTE_BLOCK_PLING, 3f, 1f) }

        player.gameMode = GameMode.ADVENTURE
        player.healthScale = 2.0
        player.heal(2.0)
        player.foodLevel = 20
        player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, -1, 1))
        setItem(player, MENU_SELECTOR, 4)
    }

    @EventHandler
    fun onPlayerLeave(event: PlayerQuitEvent) {
        players.leave(event.player, true)
        event.quitMessage(quitMessage(event.player))
        Bukkit.getOnlinePlayers().forEach { it.playSound(it, Sound.ENTITY_ITEM_PICKUP, 3f, 1f) }
    }
}