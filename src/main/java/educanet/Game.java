package educanet;

import educanet.models.Square;
import educanet.utils.Color;
import educanet.utils.ColorsList;
import educanet.utils.FileUtils;
import educanet.utils.MazeGen;
import org.lwjgl.opengl.GL33;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game {
    private static final int MAZE_NUMBER = 8;//MazeGen.generateMaze(100);//8; //number of file, to read from -> mazeCode
    private static String shader = "RainbowRoad";
    private static int count = 0;
    private static final int updateFrequency = 1;


    private static String mazeCode; //maze code. determines colors
    private static Square[][] maze;  //array of squares
    private static int mazeRows; //number of rows
    private static int mazeCols; //Number of columns
    private static float squareSize; //Square size
    private static final Color color = new Color(); //Color we are going to use


    public static void init(long window) {

        // get the maze code
        getMazeCode();
        if(mazeCode != null) {
            // calculate maze dimensions (Width & Height)
            getMazeDims();
            //square size scaling (by width)
            squareSize = 1.0f/((float)mazeCols/2);
            // init maze with proper sizing
            maze = new Square[mazeRows][mazeCols];
            //removes newline chars from mazeCode
            cleanUpMazeCode();
            //fills the maze with squares
            makeSquares();
        }
        // Setup shaders
        Shaders.initShaders();
    }


        public static void render ( long window){
        GL33.glUseProgram(Shaders.shaderProgramId);

        if (maze != null) {
            for (int y = 0; y < mazeRows; y++) {
                for (int x = 0; x < mazeCols; x++) {
                    drawSquare(maze[y][x]); //the unreadable OpenGlDrawElements part
                }
            }
        }
    }

        public static void update ( long window){
        count++;
        if(count == updateFrequency) {
            if (maze != null) {
                for (int y = 0; y < mazeRows; y++) {
                    for (int x = 0; x < mazeCols; x++) {
                        Square currentSquare = maze[y][x];

                        updateSquareColor(currentSquare);
                    }
                }
            }
            count=0;
        }
    }

    private static void updateSquareColor (Square square){
        switch (shader) {
            case "RainbowRoad":
                if(square.path) {
                    square.setColor(ColorsList.rainbowRoad(square));
                }
                break;
            case "flip":
                //todo
                break;
            default:
                break;
        }

    }



        /** Fills the maze array with squares */
        private static void makeSquares () {
        //determines square color
        int pos = 0; //position in mazeCode string
        float mazeX = -1.0f; //starting point on X axis
        float mazeY = 1.0f - squareSize; //starting point on Y axis

        for (int y = 0; y < mazeRows; y++) {
            for (int x = 0; x < mazeCols; x++) {

                Square square = newSquare(mazeX, mazeY, pos); //creates square
                pos++;
                maze[y][x] = square; //saves in array

                mazeX += squareSize; //moves in X
            }
            mazeX = -1.0f; //reset X back to start
            mazeY -= squareSize; //moves in Y
        }
    }

        /** Creates new Square */
        private static Square newSquare ( float x, float y, int pos){

            boolean path = mazeCode.charAt(pos) == '1';
            Color color = (path) ? getColor("white") : getColor("black");

            return new Square(x, y, squareSize, color, path);
        }

        /** draws the square using OpenGL*/
        public static void drawSquare (Square square){
        if (square != null) {
            square.draw();
        }
    }

        /** Returns selected color array*/
        public static Color getColor (String colorName){

        if ("black".equals(colorName)) {
            color.setColor(ColorsList.BLACK);
            return color;
        }
        color.setColor(ColorsList.WHITE);
        return color;
    }

        /** gets the selected maze code */
        private static void getMazeCode () {
        String path = "src/main/resources/Maze Codes/maze" + MAZE_NUMBER; //path to maze file
        File mazeFile = new File(path);

        if (mazeFile.exists() && mazeFile.canRead()) //checks if maze file exists and is readable
            mazeCode = FileUtils.readFile(path);

        System.out.println("Maze Code:\n" + mazeCode); //debug print
    }

        /** calculates the maze dimensions (Width & Height) */
        public static void getMazeDims () {

        //calculate the height of the maze
        Matcher m = Pattern.compile("\r\n|\r|\n").matcher(mazeCode); //using matcher to not fill up gc with useless strings from .split();
        while (m.find()) {
            mazeRows++;
        } //increments every new line

        //calculate thw width of the maze
        mazeCols = mazeCode.indexOf("\n"); //number of chars until newline char
        System.out.println("Maze Rows: " + mazeRows + "\nMaze Cols: " + mazeCols); //debug
    }

        /** removes new line chars from the mazeCode string */
        public static void cleanUpMazeCode () {
        mazeCode = mazeCode.replace("\n", "");
        System.out.println("\nEdited Maze Code: " + mazeCode); //debug
    }

        public static int getMazeLength() {
            return mazeRows*mazeCols;
        }

}
