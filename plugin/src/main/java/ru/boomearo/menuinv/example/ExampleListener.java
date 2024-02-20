package ru.boomearo.menuinv.example;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import ru.boomearo.menuinv.api.Menu;

public class ExampleListener implements Listener {

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e) {
        Player pl = e.getPlayer();

        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            e.setUseInteractedBlock(Event.Result.DENY);
            e.setUseItemInHand(Event.Result.DENY);

            Menu.open(ExampleMenuPage.MAIN, pl, new ExampleSession());
        }
    }
}
