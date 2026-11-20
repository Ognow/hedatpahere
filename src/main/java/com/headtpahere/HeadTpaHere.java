package com.headtpahere;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;

import java.util.Optional;
import java.util.regex.Pattern;

public final class HeadTpaHere {

    /** Command sent when you click a head. DonutSMP also accepts "tphere" as an alias. */
    public static final String COMMAND = "tpahere";

    /** If true, the click is swallowed so the server menu doesn't also react to it. */
    public static final boolean CANCEL_ORIGINAL_CLICK = true;

    /** Minimum time between two sends, so a double-click can't fire twice. */
    private static final long COOLDOWN_MS = 500;

    private static final Pattern USERNAME = Pattern.compile("[A-Za-z0-9_]{3,16}");
    private static final Pattern DEFAULT_HEAD_NAME = Pattern.compile("(?i)^(.+?)'s head$");

    private static long lastSent = 0;

    private HeadTpaHere() {}

    /** @return true if the click should be cancelled */
    public static boolean onSlotClicked(Slot slot, ClickType type) {
        if (slot == null || type != ClickType.PICKUP) return false;

        // Ignore clicks in your own inventory (so moving a head item around does nothing).
        if (slot.container instanceof Inventory) return false;

        ItemStack stack = slot.getItem();
        if (!stack.is(Items.PLAYER_HEAD)) return false;

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return false;

        String name = extractPlayerName(stack);
        if (name == null) return false;

        // Don't /tpahere yourself.
        if (name.equalsIgnoreCase(mc.getUser().getName())) return false;

        long now = System.currentTimeMillis();
        if (now - lastSent >= COOLDOWN_MS) {
            lastSent = now;
            player.connection.sendCommand(COMMAND + " " + name);
        }
        return CANCEL_ORIGINAL_CLICK;
    }

    /** Reads the player name from the head's skull profile, falling back to its item name. */
    private static String extractPlayerName(ItemStack stack) {
        ResolvableProfile profile = stack.get(DataComponents.PROFILE);
        if (profile != null) {
            Optional<String> profileName = profile.name();
            if (profileName.isPresent() && USERNAME.matcher(profileName.get()).matches()) {
                return profileName.get();
            }
        }

        String display = ChatFormatting.stripFormatting(stack.getHoverName().getString());
        if (display == null) return null;
        display = display.trim();

        var defaultName = DEFAULT_HEAD_NAME.matcher(display);
        if (defaultName.matches()) display = defaultName.group(1).trim();

        return USERNAME.matcher(display).matches() ? display : null;
    }
}
