package educanet.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;


public class MazeGen {
    private static final String PATH_TO_MAZE_FILES = "src/main/resources/Maze Codes";
    private static int mazeNumber = 0;

    private static final int WALL = 0;
    private static final int SPACE = 1;

    private static int mazeSize;
    private static byte[][] mazeMap;


    /** Generates new random maze */
    public static int generateMaze(int size) {
        mazeSize = size+2;


        createMaze();

        determineMazeNumber();
        writeToFile();

        return mazeNumber;
    }



    /** Determine max number in maze files and set current maze number one higher */
    private static void determineMazeNumber() {
        mazeNumber = Arrays.stream(new File(PATH_TO_MAZE_FILES).list())
                    .map(s -> s.replace("maze", ""))
                    .map(s -> s.replace(".txt", ""))
                    .mapToInt(Integer::valueOf)
                    .map(i -> i+=1)
                    .max().orElseThrow(NoSuchElementException::new);

    }
    /** Creates maze */
    private static void createMaze() {
        mazeMap = new byte[mazeSize][];

        for(int x = 0; x < mazeSize; x++) {
            mazeMap[x] = new byte[mazeSize];
            for(int y = 0; y < mazeSize; y++) {
                mazeMap[x][y] = WALL;
            }
        }
        for(int x = 0; x < mazeSize; x++) {
            mazeMap[x][0] = SPACE;
            mazeMap[x][mazeSize - 1] = SPACE;
        }
        for(int y = 0; y < mazeSize; y++) {
            mazeMap[0][y] = SPACE;
            mazeMap[mazeSize - 1][y] = SPACE;
        }

        mazeMap[2][2] = SPACE;
        carve(2, 2);

        mazeMap[2][1] = SPACE;
        mazeMap[mazeSize - 3][mazeSize - 2] = SPACE;

    }

    private static void carve(int x, int y) {
        final int[] upx = { 1, -1, 0, 0 };
        final int[] upy = { 0, 0, 1, -1 };
        Random rand = new Random();

        int dir = rand.nextInt(4);
        int count = 0;
        while(count < 4) {
            final int x1 = x + upx[dir];
            final int y1 = y + upy[dir];
            final int x2 = x1 + upx[dir];
            final int y2 = y1 + upy[dir];
            if(mazeMap[x1][y1] == WALL && mazeMap[x2][y2] == WALL) {
                mazeMap[x1][y1] = SPACE;
                mazeMap[x2][y2] = SPACE;
                carve(x2, y2);
            } else {
                dir = (dir + 1) % 4;
                count += 1;
            }
        }
    }
    /** Creates maze file */
    private static void writeToFile() {
        File newMaze = new File(PATH_TO_MAZE_FILES + "/maze" + mazeNumber);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(newMaze));
            StringBuilder code = new StringBuilder();
            for(int y = 0; y < mazeSize; y++) {
                if(y==0 || y==mazeSize-1) continue;
                for(int x = 0; x < mazeSize; x++) {
                    if(x==0 || x==mazeSize-1) continue;

                    if(mazeMap[x][y] == WALL) {
                        code.append("0");
                    } else {
                        code.append("1");
                    }
                }
                code.append("\n");
            }
            writer.write(code.toString());
            writer.close();
            newMaze.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
