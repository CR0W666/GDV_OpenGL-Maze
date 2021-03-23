package educanet.models;

import educanet.utils.Color;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Square {

    public float[] vertices;
    public int[] indices;
    public Color color;
    public boolean path;

    public        int vaoId;
    private final int vboId;
    private final int eboId;
    private final int colorId;

    public Square(float x, float y, float squareSize, Color color, boolean path) {
        vaoId = GL33.glGenVertexArrays();
        vboId = GL33.glGenBuffers();
        eboId = GL33.glGenBuffers();
        colorId = GL33.glGenBuffers();

        this.indices = new int[] {
                0, 1, 3, // First triangle
                1, 2, 3 // Second triangle
        };

        this.vertices = new float[] {  //square origin point is in Bottom Left
                x + squareSize  ,y + squareSize, 0.0f, // 0 -> Top    Right
                x + squareSize  ,y             , 0.0f, // 1 -> Bottom Right
                x               ,y             , 0.0f, // 2 -> Bottom Left
                x               ,y + squareSize, 0.0f, // 3 -> Top    Left
        }; //some help from Filip Makrlík
        this.color = color;
        this.path = path;

        theFuckingMessyCodeIDontUnderstandAtAll();
    }
    private void theFuckingMessyCodeIDontUnderstandAtAll() {
        GL33.glBindVertexArray(vaoId); // Tell OpenGL I am using this square

        // Tell OpenGL we are currently writing to this buffer (eboId)
        GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, eboId);
        IntBuffer ib = BufferUtils.createIntBuffer(indices.length)
                .put(indices)
                .flip();
        GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, ib, GL33.GL_STATIC_DRAW);


        // Tell OpenGL we are currently writing to this buffer (vboId)
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vboId);

        FloatBuffer fb = BufferUtils.createFloatBuffer(vertices.length)
                .put(vertices)
                .flip();

        // Send the buffer (positions) to the GPU
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, fb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(0);


        // Tell OpenGL we are currently writing to this buffer (colorsId)
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, colorId);

        FloatBuffer cb = BufferUtils.createFloatBuffer(color.getColor().length)
                .put(color.getColor())
                .flip();

        // Send the buffer (positions) to the GPU
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, cb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(1, 3, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(1);

        MemoryUtil.memFree(fb);
        MemoryUtil.memFree(cb);

    }

    public void draw() {
        GL33.glBindVertexArray(vaoId);
        GL33.glDrawElements(GL33.GL_TRIANGLES, indices.length, GL33.GL_UNSIGNED_INT, 0);
    }

    public void setColor(float[] newColor) {
        this.color.setColor(newColor);

        GL33.glBindVertexArray(this.vaoId);
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, colorId);

        // Send the buffer (positions) to the GPU
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, color.getFb(), GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(1, 3, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(1);
    }

    public void update() {

    }
}
