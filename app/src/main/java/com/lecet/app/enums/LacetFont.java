package com.lecet.app.enums;

/**
 * Created by Josué Rodríguez on 20/10/2016.
 */

public enum LacetFont {
    LATO_BLACK("fonts/Lato-Black.ttf"),
    LATO_BLACK_ITALIC("fonts/Lato-BlackItalic.ttf"),
    LATO_BOLD("fonts/Lato-Bold.ttf"),
    LATO_BOLD_ITALIC("fonts/Lato-BoldItalic.ttf"),
    LATO_HAIRLINE("fonts/Lato-Hairline.ttf"),
    LATO_HAIRLINE_ITALIC("fonts/Lato-HairlineItalic.ttf"),
    LATO_HEAVY("fonts/Lato-Heavy.ttf"),
    LATO_HEAVY_ITALIC("fonts/Lato-HeavyItalic.ttf"),
    LATO_ITALIC("fonts/Lato-Italic.ttf"),
    LATO_LIGHT("fonts/Lato-Light.ttf"),
    LATO_LIGHT_ITALIC("fonts/Lato-LightItalic.ttf"),
    LATO_MEDIUM("fonts/Lato-Medium.ttf"),
    LATO_MEDIUM_ITALIC("fonts/Lato-MediumItalic.ttf"),
    LATO_REGULAR("fonts/Lato-Regular.ttf"),
    LATO_SEMIBOLD("fonts/Lato-Semibold.ttf"),
    LATO_SEMIBOLD_ITALIC("fonts/Lato-SemiboldItalic.ttf"),
    LATO_THIN("fonts/Lato-Thin.ttf"),
    LATO_THIN_ITALIC("fonts/Lato-ThinItalic.ttf");

    private String font;

    LacetFont(String fontName) {
        this.font = fontName;
    }

    public String getFont() {
        return font;
    }
}
