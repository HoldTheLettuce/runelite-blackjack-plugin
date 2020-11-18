package net.runelite.client.plugins.blackjack;

import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;

import javax.inject.Inject;
import java.awt.*;

public class BlackjackOverlay extends Overlay {

    private final Color color = Color.WHITE;

    private final Client client;
    private final BlackjackPlugin plugin;

    @Inject
    public BlackjackOverlay(Client client, BlackjackPlugin plugin) {
        this.client = client;
        this.plugin = plugin;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        NPC thug = plugin.getThug();

        if(thug == null)
            return null;

        int size = thug.getComposition().getSize();
        LocalPoint lp = thug.getLocalLocation();
        Polygon tilePoly = Perspective.getCanvasTileAreaPoly(client, lp, size);

        graphics.setColor(color);
        graphics.setStroke(new BasicStroke(2));
        graphics.draw(tilePoly);
        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));
        graphics.fill(tilePoly);

        return null;
    }
}
