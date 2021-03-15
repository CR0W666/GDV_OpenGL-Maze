package educanet;

import educanet.models.Square;
import educanet.utils.FileUtils;
import educanet.utils.MazeGen;
import org.lwjgl.opengl.GL33;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game {
    private static final int MAZE_NUMBER = MazeGen.generateMaze(100);//4; //number of file, to read from -> mazeCode
    private static String mazeCode; //maze code. determines colors

    private static Square[][] maze;  //array of squares
    private static int mazeRows; //number of rows
    private static int mazeCols; //Number of columns

    private static float squareSize; //Square size | 0.2f is just temp

    public static void init(long window) {

        // get the maze code
        getMazeCode(MAZE_NUMBER);
        if(mazeCode != null) {
            // calculate maze dimensions (Width & Height)
            getMazeDims();
            //square size scaling (by width)
            squareSize = 1.0f/(mazeCols/2);
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

    public static void render(long window) {
        GL33.glUseProgram(Shaders.shaderProgramId);
        if(maze != null) {
            for (int y = 0; y < mazeRows; y++) {
                for (int x = 0; x < mazeCols ; x++) {
                    drawSquare(maze[y][x]); //the unreadable OpenGlDrawElements part
                }
            }
        }
    }

    public static void update(long window) {

    }

    /** Fills the maze array with squares */
    private static void makeSquares() {
        boolean color; //determines square color
        int mazeCodePos = 0; //position in mazeCode string
        int binaryValue; //int value of mazeCode color char

        float mazeX = -1.0f; //starting point on X axis
        float mazeY = 1.0f-squareSize; //starting point on Y axis

        for (int y = 0; y < mazeRows; y++) {
            for (int x = 0; x < mazeCols; x++) {
                binaryValue = Integer.parseInt(String.valueOf(mazeCode.charAt(mazeCodePos))); //reads the string and saves the value at mazeCodePos position
                mazeCodePos++;

                color = binaryValue == 1; //boolean condition

                Square square = newSquare(mazeX, mazeY, color); //creates square
                maze[y][x] = square; //saves in array

                mazeX += squareSize; //moves in X
            }
            mazeX = -1.0f; //reset X back to start
            mazeY -= squareSize; //moves in Y
        }
    }

    /** Creates new Square */
    private static Square newSquare(float x, float y, boolean color) {

        int[] indices = {
                0, 1, 3, // First triangle
                1, 2, 3 // Second triangle
        };

        float[] vertices = {  //square origin point is in Bottom Left
                x + squareSize  ,y + squareSize, 0.0f, // 0 -> Top    Right
                x + squareSize  ,y             , 0.0f, // 1 -> Bottom Right
                x               ,y             , 0.0f, // 2 -> Bottom Left
                x               ,y + squareSize, 0.0f, // 3 -> Top    Left
        }; //some help from Filip Makrlík

       return new Square(vertices, indices, (color) ? getColor("white") : getColor("black"));
    }

    /** draws the square using OpenGL*/
    public static void drawSquare(Square square) {
        if (square != null) {
            GL33.glBindVertexArray(square.vaoId);
            GL33.glDrawElements(GL33.GL_TRIANGLES, square.indices.length, GL33.GL_UNSIGNED_INT, 0);
        }
    }

    /** Returns selected color array*/
    public static float[] getColor(String color) {
        float[] whiteColor = {
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
        };

        float[] blackColor = {
                0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f,
        };

        if ("black".equals(color)) {
            return blackColor;
        }
        return whiteColor;
    }

    /** gets the selected maze code */
    private static void getMazeCode(int number) {
        String path = "src/main/resources/Maze Codes/maze" + number; //path to maze file
        File mazeFile = new File(path);

        if(mazeFile.exists() && mazeFile.canRead()) //checks if maze file exists and is readable
            mazeCode = FileUtils.readFile(path);

        System.out.println("Maze Code:\n" + mazeCode); //debug print
    }

    /** calculates the maze dimensions (Width & Height) */
    public static void getMazeDims() {

        //calculate the height of the maze
        Matcher m = Pattern.compile("\r\n|\r|\n").matcher(mazeCode); //using matcher to not fill up gc with useless strings from .split();
        while (m.find()) { mazeRows ++; } //increments every new line

        //calculate thw width of the maze
        mazeCols = mazeCode.indexOf("\n"); //number of chars until newline char
        System.out.println("Maze Rows: " + mazeRows + "\nMaze Cols: " + mazeCols); //debug
    }

    /** removes new line chars from the mazeCode string */
    public static void cleanUpMazeCode() {
        mazeCode = mazeCode.replace("\n", "");
        System.out.println("\nEdited Maze Code: " + mazeCode); //debug
    }

    /** prints out the maze with color values interpreted in binary */
    public static void testMaze() {
        //test maze sizing
        System.out.println("");
        int counter = 0;
        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[y].length; x++) {
                //System.out.print("X");
                System.out.print(mazeCode.charAt(counter));
                counter++;
            }
            System.out.println();
        }
    }
}
