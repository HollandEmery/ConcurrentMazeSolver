package ConcurrentMaze;

import java.security.Permission;
import java.util.*;

public class MazeGenerator{
    public static void main(String[] args){
        int Xdim, Ydim,Sx, Sy, Fx, Fy;
        Scanner scan = new Scanner(System.in);
        System.out.print("X Dim: ");
        Xdim = scan.nextInt();
        System.out.print("Y Dim: ");
        Ydim = scan.nextInt();
        System.out.print("X Start: ");
        Sx = scan.nextInt();
        System.out.print("Y Start: ");
        Sy = scan.nextInt();
        System.out.print("X Finish: ");
        Fx = scan.nextInt();
        System.out.print("Y Finsh: ");
        Fy = scan.nextInt();
        Maze Createdmaze = new Maze(Xdim, Ydim, Sx, Sy, Fx, Fy);  

        // recursiveBacktrack(Createdmaze);
        // Prims(Createdmaze);
        System.out.print("Percent: ");
        float per = scan.nextFloat();
        Backtrack(Createdmaze, per);

        // Createdmaze.printMaze();
        System.out.println("Filename: ");
        scan.nextLine();
        String filename = scan.nextLine();
        scan.close();
        // Createdmaze.printMaze();
        Createdmaze.toFile(filename);
    }

    public static void Prims(Maze maze){
        boolean[][] visited = new boolean[maze.Xdim][maze.Xdim];
        Random rand = new Random();
        int startX = rand.nextInt(maze.Xdim);
        int startY = rand.nextInt(maze.Ydim);
        ArrayList<Cell> possible = new ArrayList<Cell>();
        visited[startX][startY] = true;
        possible.addAll(PrimsToAdd(startX, startY, maze, visited));
        while(!possible.isEmpty()){
            int ran = rand.nextInt(possible.size());
            Cell currCell = possible.get(ran);
            possible.remove(currCell);

            int currX = currCell.x;
            int currY = currCell.y;

            char dir = PickVisitedNeighbor(visited, currX, currY);
            currCell.changeDir(dir, true);
            switch(dir){
                case 'u':
                    maze.maze[currX][currY-1].changeDir('d', true);
                    break;
                case 'd':
                    maze.maze[currX][currY+1].changeDir('u', true);
                    break;
                case 'l':
                    maze.maze[currX-1][currY].changeDir('r', true);
                    break;
                case 'r':
                    maze.maze[currX+1][currY].changeDir('l', true);
                    break;
            }

            visited[currX][currY]=true;
            // System.out.println(currX+" "+currY);

            ArrayList toAdd = PrimsToAdd(currX, currY, maze, visited);
            possible.removeAll(toAdd);
            possible.addAll(toAdd);
        }
    }

    public static ArrayList<Cell> PrimsToAdd(int X, int Y, Maze maze, boolean[][] visited){
        ArrayList ret = new ArrayList<Cell>();
        if(X>0 && !visited[X-1][Y])
            ret.add(maze.maze[X-1][Y]);
        if(X<maze.Xdim-1 && !visited[X+1][Y])
            ret.add(maze.maze[X+1][Y]);
        if(Y>0 && !visited[X][Y-1])
            ret.add(maze.maze[X][Y-1]);
        if(Y<maze.Ydim-1 && !visited[X][Y+1])
            ret.add(maze.maze[X][Y+1]);
        return ret;
    }

    public static char PickVisitedNeighbor(boolean[][] visited, int X, int Y){
        String str = "";
        if(X>0 && visited[X-1][Y]){
            str+="l";
        }
        if(X<visited.length-1 && visited[X+1][Y]){
            str+="r";
        }
        if(Y>0 && visited[X][Y-1]){
            str+="u";
        }
        if(Y<visited[0].length-1 && visited[X][Y+1]){
            str+="d";
        }
        Random rand = new Random();
        int index = rand.nextInt(str.length());
        return str.charAt(index);
    }

    public static void recursiveBacktrack(Maze maze){
        boolean[][] visited = new boolean[maze.Xdim][maze.Ydim];
        Random rand = new Random();
        int currX = rand.nextInt(maze.Xdim);
        int currY = rand.nextInt(maze.Ydim);
        visitNeighbor(maze, visited, currX, currY);
    }


    public static char PickNeighbor(boolean[][] visited, int X, int Y){
        String str = "";
        if(X>0 && !visited[X-1][Y]){
            str+="l";
        }
        if(X<visited.length-1 && !visited[X+1][Y]){
            str+="r";
        }
        if(Y>0 && !visited[X][Y-1]){
            str+="u";
        }
        if(Y<visited[0].length-1 && !visited[X][Y+1]){
            str+="d";
        }
        Random rand = new Random();
        int index = rand.nextInt(str.length());
        return str.charAt(index);
    }
    public static void visitNeighbor(Maze maze, boolean[][] visited, int X, int Y){
        visited[X][Y] = true;
        while(unvisitedNeighbor(visited, X, Y)){
            char dir = PickNeighbor(visited, X, Y);
            // System.out.println(X+" "+Y);
            switch(dir){
                case 'u':
                    maze.maze[X][Y].changeDir('u', true);
                    maze.maze[X][Y-1].changeDir('d', true);
                    visitNeighbor(maze, visited, X, Y-1);
                    break;
                case 'd':
                    maze.maze[X][Y].changeDir('d', true);
                    maze.maze[X][Y+1].changeDir('u', true);
                    visitNeighbor(maze, visited, X, Y+1);
                    break;
                case 'l':
                    maze.maze[X][Y].changeDir('l', true);
                    maze.maze[X-1][Y].changeDir('r', true);
                    visitNeighbor(maze, visited, X-1, Y);
                    break;
                case 'r':
                    maze.maze[X][Y].changeDir('r', true);
                    maze.maze[X+1][Y].changeDir('l', true);
                    visitNeighbor(maze, visited, X+1, Y);
                    break;
            }
        }
    }
    public static boolean unvisitedNeighbor(boolean[][] visited, int X, int Y){
        if(X>0 && !visited[X-1][Y]){
            return true;
        }else if(X<visited.length-1 && !visited[X+1][Y]){
            return true;
        }else if(Y>0 && !visited[X][Y-1]){
            return true;
        }else if(Y<visited[0].length-1 && !visited[X][Y+1]){
            return true;
        }
        return false;
    }
    public static void Backtrack(Maze maze){
        Backtrack(maze,1);
    }
    public static void Backtrack(Maze maze, float method){
        boolean[][] visited = new boolean[maze.Xdim][maze.Ydim];
        Random rand = new Random();
        int currX = rand.nextInt(maze.Xdim);
        int currY = rand.nextInt(maze.Ydim);
        LinkedList<Cell> path = new LinkedList<Cell>();
        path.add(maze.maze[currX][currY]);
        while(!path.isEmpty()){
            float percent = rand.nextFloat();
            Cell currCell;
            if(percent<=method){
                currCell = path.peekLast();
            }else{
                currCell = path.get(rand.nextInt(path.size()));
            }
            currX = currCell.x;
            currY = currCell.y;
            visited[currX][currY] = true;
            if(unvisitedNeighbor(visited, currX, currY)){
                char dir = PickNeighbor(visited, currX, currY);
                    // System.out.println(currX+" "+currY);
                switch(dir){
                    case 'u':
                        maze.maze[currX][currY].changeDir('u', true);
                        maze.maze[currX][currY-1].changeDir('d', true);
                        path.add(maze.maze[currX][currY-1]);
                        break;
                    case 'd':
                        maze.maze[currX][currY].changeDir('d', true);
                        maze.maze[currX][currY+1].changeDir('u', true);
                        path.add(maze.maze[currX][currY+1]);
                        break;
                    case 'l':
                        maze.maze[currX][currY].changeDir('l', true);
                        maze.maze[currX-1][currY].changeDir('r', true);
                        path.add(maze.maze[currX-1][currY]);
                        break;
                    case 'r':
                        maze.maze[currX][currY].changeDir('r', true);
                        maze.maze[currX+1][currY].changeDir('l', true);
                        path.add(maze.maze[currX+1][currY]);
                        break;
                }
            }else{
                path.removeLast();
            }
        }
    }
}