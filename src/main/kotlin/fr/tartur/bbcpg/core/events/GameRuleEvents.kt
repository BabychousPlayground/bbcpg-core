package fr.tartur.bbcpg.core.events

import fr.tartur.bbcpg.core.data.config.GameRules
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent

class GameRuleEvents(private val gameRules: GameRules) : Listener {
    @EventHandler
    fun onFoodBarChanges(event: FoodLevelChangeEvent) {
        event.isCancelled = gameRules.loseFood
    }

    @EventHandler
    fun onHealthChanges(event: EntityDamageEvent) {
        event.isCancelled = gameRules.loseHealth
    }
}