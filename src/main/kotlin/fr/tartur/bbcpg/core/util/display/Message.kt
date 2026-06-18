package fr.tartur.bbcpg.core.util.display

import fr.tartur.bbcpg.core.data.User
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.Player

private val serializer: MiniMessage by lazy { MiniMessage.miniMessage() }

fun message(text: String): Component = serializer.deserialize(text)

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