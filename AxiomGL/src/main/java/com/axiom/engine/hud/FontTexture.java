/**
 * Represents a font
 * <p>
 * <br>
 * This class represents a font. At initialization
 * it saves a texture containing the font in white
 * over transparent to a texture file "Temp.png".
 * </p>
 * <p>
 * @author Antonio Hernández Bejarano (@lwjglgamedev)
 * @author The Axiom Corp, 2017.
 * </p>
 */
package com.axiom.engine.hud;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

import com.axiom.engine.item.model.Texture;

public class FontTexture {

    private static final String IMAGE_FORMAT = "png";

    private final Font font;

    private final String charSetName;

    private final Map<Character, CharInfo> charMap;

    private Texture texture;

    private int height;

    private int width;

    /**
     * Construct a font from the given font
     * @param font the font type
     * @param charSetName name of the font
     * @throws Exception if the texture file is not found
     */
    public FontTexture(Font font, String charSetName) throws Exception {
        this.font = font;
        this.charSetName = charSetName;
        charMap = new HashMap<>();

        buildTexture();
    }

    /**
     * Get the width
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the height
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get the texture
     * @return texture
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Get the character info for a given character
     * <br>
     * CharInfo is a class that contains the starting
     * position and the width of a textual character.
     * @param c character to check
     * @return information
     */
    public CharInfo getCharInfo(char c) {
        return charMap.get(c);
    }

    /**
     * Get characters from a charset
     * <br>
     * Get all characters from a charset. This method
     * is to deal with non-Latin ISO characters.
     * @param charsetName the font to check
     * @return a string of all the characters
     */
    private String getAllAvailableChars(String charsetName) {
        CharsetEncoder ce = Charset.forName(charsetName).newEncoder();
        StringBuilder result = new StringBuilder();
        for (char c = 0; c < Character.MAX_VALUE; c++) {
            if (ce.canEncode(c)) {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * Build the texture for the font
     * @throws Exception texture file is not found
     */
    private void buildTexture() throws Exception {
        // Get the font metrics for each character for the selected font by using image
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2D = img.createGraphics();
        g2D.setFont(font);
        FontMetrics fontMetrics = g2D.getFontMetrics();

        String allChars = getAllAvailableChars(charSetName);
        this.width = 0;
        this.height = 0;
        for (char c : allChars.toCharArray()) {
            // Get the size for each character and update global image size
            CharInfo charInfo = new CharInfo(width, fontMetrics.charWidth(c));
            charMap.put(c, charInfo);
            width += charInfo.getWidth();
            height = Math.max(height, fontMetrics.getHeight());
        }
        g2D.dispose();

        // Create the image associated to the charset
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2D = img.createGraphics();
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setFont(font);
        fontMetrics = g2D.getFontMetrics();
        g2D.setColor(Color.WHITE);
        g2D.drawString(allChars, 0, fontMetrics.getAscent());
        g2D.dispose();

        // Dump image to a byte buffer
        InputStream is;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(img, IMAGE_FORMAT, out);
            ImageIO.write(img, IMAGE_FORMAT, new java.io.File("Temp.png"));
            out.flush();
            is = new ByteArrayInputStream(out.toByteArray());
        }

        texture = new Texture(is);
    }

    /**
     * A CharInfo class
     * <p>
     * <br>
     * The CharInfo for a character contains
     * its starting position and its width.
     * </p><p>
	 * @author Antonio Hernández Bejarano (@lwjglgamedev)
	 * @author The Axiom Corp, 2017.
     * </p>
     */
    public static class CharInfo {

        private final int startX;

        private final int width;

        /**
         * Construct a CharInfo
         * @param startX starting x
         * @param width width
         */
        public CharInfo(int startX, int width) {
            this.startX = startX;
            this.width = width;
        }

        /**
         * Get the start
         * @return start
         */
        public int getStartX() {
            return startX;
        }
        	
        /**
         * Get the width
         * @return width
         */
        public int getWidth() {
            return width;
        }
    }
}

