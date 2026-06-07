package fr.tartur.bbcpg.core.util.display

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

object Message {
    private val serializer: MiniMessage by lazy { MiniMessage.miniMessage() }

    fun message(text: String): Component = serializer.deserialize(text)
}