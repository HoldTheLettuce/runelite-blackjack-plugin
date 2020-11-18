package net.runelite.client.plugins.blackjack;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ClientTick;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@PluginDescriptor(name = "Blackjack")
@Slf4j
public class BlackjackPlugin extends Plugin {

    @Inject
    private Client client;

    @Inject
    private BlackjackOverlay overlay;

    @Inject
    private OverlayManager overlayManager;

    private NPC thug;

    @Override
    protected void startUp() {
        overlayManager.add(overlay);
    }

    @Override
    protected void shutDown() {
        overlayManager.remove(overlay);
    }

    @Subscribe
    public void onClientTick(ClientTick tick) {
        if(client.getGameState() != GameState.LOGGED_IN || client.isMenuOpen())
            return;

        thug = null;

        List<NPC> npcs = client.getNpcs().stream().filter(n -> n.getId() == NpcID.MENAPHITE_THUG_3550 && n.getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation()) <= 1).collect(Collectors.toList());

        if(npcs.size() > 0) {
            thug = npcs.get(0);

            int animId = thug.getAnimation();

            if(animId == -1 || animId == 808) {
                // Idle
                swapActions(2, 5);
            } else if(animId == 838 || animId == 395) {
                // Knocked out or being stunned
                swapActions(4, 5);
            }
        }
    }

    public void swapActions(int index1, int index2) {
        MenuEntry[] entries = client.getMenuEntries();

        for(int i = 0; i < entries.length; i++) {
            MenuEntry entry = entries[i];

            final String target = Text.removeTags(entry.getTarget()).toLowerCase();
            final String option = Text.removeTags(entry.getOption()).toLowerCase();

            log.debug("Index: " + i + " & Option: " + option);

            if(target.contains("menaphite")) {
                MenuEntry entry1 = entries[index1],
                        entry2 = entries[index2];

                entries[index1] = entry2;
                entries[index2] = entry1;

                client.setMenuEntries(entries);
            }
        }
    }

    public NPC getThug() {
        return thug;
    }
}
