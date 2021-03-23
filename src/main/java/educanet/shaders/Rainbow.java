package educanet.shaders;

import educanet.Game;
import educanet.models.Square;

public class Rainbow {
    private static int mazeLength = Game.getMazeLength();
    private static float[] color = new float[12];
    private static int step = -1;
    private static float red = 1.0f;
    private static float green = 1.0f;
    private static float blue = 1.0f;

    public static float[] road(Square square) {
        if (step > mazeLength) step = 0;
        step++;
        if (square.path) {
            red   = (float) (Math.sin(step            ) * 127 + 128)/255;
            green = (float) (Math.sin(step + Math.PI/2) * 127 + 128)/255;
            blue  = (float) (Math.sin(step + Math.PI  ) * 127 + 128)/255;

            //System.out.println("R: " + red + "\nG: " + green + "\nB: " + blue);
            for (int i = 0; i < 12; i++) {
                if (i   == 0) { color[i] = red;  continue; }

                if (i%3 == 0) { color[i] = red;   }
                if (i%3 == 1) { color[i] = green; }
                if (i%3 == 2) { color[i] = blue;  }

            }
            return color;
        }
        return square.color.getColor();
    }

}
