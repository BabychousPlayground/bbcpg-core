package fr.tartur.bbcpg.core.data

import fr.tartur.bbcpg.core.util.display.Message.message
import org.bukkit.entity.Player
import java.util.UUID

data class User(
    val spigot: Player,
    var coins: Int,
    var level: Int,
    var experience: Float,
    val new: Boolean = false,
) {
    constructor(spigot: Player) : this(spigot, 0, 0, 0f, true)
}

class UserManager(private val source: UserSource) {
    private val users = mutableMapOf<UUID, User>()

    fun connect(player: Player): User {
        val uuid = player.uniqueId
        val default = User(player)
        val user = source.retrieve(uuid) ?: if (source.create(uuid)) default else null

        if (user == null) {
            player.sendMessage(message("<red>Vos données n'ont pas pu être créées ou récupérées depuis la base " +
                    "de données. Essayez de vous reconnecter ou contactez un administrateur si l'erreur persiste."))
            return default
        }

        users[uuid] = user
        return user
    }

    fun leave(player: Player, save: Boolean = false): User? {
        val uuid = player.uniqueId
        return get(uuid)?.run {
            if (save) {
                source.update(this)
            }

            users.remove(uuid)
        }
    }

    operator fun get(uuid: UUID): User? = users[uuid]
}