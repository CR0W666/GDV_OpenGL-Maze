package educanet.utils;
import educanet.models.Square;
import educanet.shaders.Rainbow;

public class ColorsList {

    public static final float[] WHITE = {
            //R    G     B
            1.0f, 1.0f, 1.0f,   // 0 -> Top    Right
            1.0f, 1.0f, 1.0f,   // 1 -> Bottom Right
            1.0f, 1.0f, 1.0f,   // 2 -> Bottom Left
            1.0f, 1.0f, 1.0f,   // 3 -> Top    Left
    };

    public static final float[] BLACK = {
            //R    G     B
            0.0f, 0.0f, 0.0f,   // 0 -> Top    Right
            0.0f, 0.0f, 0.0f,   // 1 -> Bottom Right
            0.0f, 0.0f, 0.0f,   // 2 -> Bottom Left
            0.0f, 0.0f, 0.0f,   // 3 -> Top    Left
    };

   public static final float[] TRANSPARENT = {
           //R    G     B     A
           0.0f, 0.0f, 0.0f, 0.0f,   // 0 -> Top    Right
           0.0f, 0.0f, 0.0f, 0.0f,   // 1 -> Bottom Right
           0.0f, 0.0f, 0.0f, 0.0f,   // 2 -> Bottom Left
           0.0f, 0.0f, 0.0f, 0.0f   // 3 -> Top    Left
   };

   public static float[] rainbowRoad(Square square) {
       return Rainbow.road(square);
   }

}
