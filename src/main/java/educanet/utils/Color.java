package educanet.utils;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.Arrays;

public class Color {
    private  FloatBuffer  fb;
    private  float[]   color;

    public Color(float... color) {
        this.color = color;
        if (color.length > 0) {
            fb = BufferUtils.createFloatBuffer(color.length);
        }
    }

    public float[] getColor() {
        return this.color;
    }

    public void setColor(float[] color) {
        this.color = color;
        if (fb == null) fb = BufferUtils.createFloatBuffer(color.length);
        fb.clear().put(color).flip();
    }

    public FloatBuffer getFb() {
        return this.fb;
    }


}
