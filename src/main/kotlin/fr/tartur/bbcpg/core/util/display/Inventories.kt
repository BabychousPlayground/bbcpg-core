package fr.tartur.bbcpg.core.util.display

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemType
import xyz.xenondevs.invui.dsl.ExperimentalDslApi
import xyz.xenondevs.invui.dsl.by
import xyz.xenondevs.invui.dsl.item
import xyz.xenondevs.invui.dsl.itemProvider
import xyz.xenondevs.invui.item.Item

fun setItem(player: Player, item: Item, slot: Int) {
    player.inventory.setItem(slot, item.getItemProvider(player).get())
}

@OptIn(ExperimentalDslApi::class)
val MENU_SELECTOR: Item by lazy {
    item {
        itemProvider(ItemType.COMPASS) {
            name by "<b><gold>»<yellow>»</yellow>»</gold></b> <red>❤</red> <yellow>•</yellow> " +
                    "<gradient:aqua:red>Sélecteur de MINI-JEUX</gradient> <yellow>•</yellow> <red>❤</red> " +
                    "<b><gold>«<yellow>«</yellow>«</gold></b>"
            hasGlint by true
        }
    }
}