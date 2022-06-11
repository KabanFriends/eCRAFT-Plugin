package io.github.kabanfriends.ecraft.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class ComponentUtils {

    private static final LegacyComponentSerializer SECTION_SERIALIZER = LegacyComponentSerializer.builder()
            .hexColors()
            .character('§')
            .hexCharacter('#')
            .extractUrls()
            .build();

    private static final LegacyComponentSerializer AMPERSAND_SERIALIZER = LegacyComponentSerializer.builder()
            .hexColors()
            .character('&')
            .hexCharacter('#')
            .extractUrls()
            .build();

    public static Component legacy(String legacy) {
        return SECTION_SERIALIZER.deserializeOrNull(legacy);
    }

    public static String legacy(Component component) {
        return SECTION_SERIALIZER.serializeOrNull(component);
    }

    public static Component legacyAmpersand(String legacy) {
        return AMPERSAND_SERIALIZER.deserializeOrNull(legacy);
    }

    public static String plain(Component component) {
        return PlainTextComponentSerializer.plainText().serializeOrNull(component);
    }
}
