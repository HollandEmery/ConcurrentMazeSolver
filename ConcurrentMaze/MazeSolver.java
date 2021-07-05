package ConcurrentMaze;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MazeSolver{
    public static void main(String[] args){
        ExecutorService threadPool;
        int availableThreads = Runtime.getRuntime().availableProcessors();
    	threadPool = Executors.newFixedThreadPool(availableThreads);
        ArrayList ExecutorTasks = new ArrayList<myThread>();
        Scanner scan = new Scanner(System.in);
        System.out.print("Filename: ");
        String filename = scan.next();
        scan.close();
        Maze toSolve = null;
        try {
            toSolve = new Maze(filename);
        } catch (Exception e) {
            System.out.println("Error reading from file");
        }
        if(toSolve == null || toSolve.solution.size()!=0){
            System.out.println("Already Solved");
            return;
        }
        BlockingQueue path = new PriorityBlockingQueue<CellPath>(toSolve.Xdim*toSolve.Ydim);
        boolean[][] visited = new boolean[toSolve.Xdim][toSolve.Ydim];
        CellPath start = new CellPath(null, 0, Math.abs(toSolve.sX-toSolve.fX)+Math.abs(toSolve.sY-toSolve.fY), toSolve.maze[toSolve.sX][toSolve.sY]);
        path.add(start);

        List<Future<CellPath>> invokeReturn = new LinkedList<Future<CellPath>>();
        for(int i = 0; i < availableThreads;i++){
            ExecutorTasks.add(new myThread(path, toSolve, visited,i));
        }
        try {
            invokeReturn = threadPool.invokeAll(ExecutorTasks);
        } catch (Exception e) {
            System.out.println("error");
        }
        threadPool.shutdown();
        CellPath end = null;
        // System.out.println(invokeReturn);
        for(Future<CellPath> searchRet : invokeReturn) {
            try {
				if(searchRet.get()!=null) {
					end = searchRet.get();
				}
			} catch (Exception e) {
				System.out.println("Problem with getting from Future");
			}
        }
        System.out.println("Testing");
        // for(boolean[] i: visited){
        //     for(boolean j : i){
        //         System.out.print(j+" ");
        //     }
        //     System.out.println("");
        // }
        // System.out.println(end);
        ArrayList pathFinal = new ArrayList<Cell>();
        while(end!=null){
            pathFinal.add(0,end.cell);
            end = end.previous;
            // System.out.println(end.cell.x+", "+end.cell.y+": "+pathFinal.size());
        }
        System.out.println("Path found");
        String str = pathFinal.toString();
        str = str.replace("[", "").replace("]", "");
        // System.out.println(str);
        try {
            FileWriter newFile = new FileWriter(filename,true);
            BufferedWriter bFile = new BufferedWriter(newFile);
            bFile.write(str);
            bFile.newLine();
            bFile.close();
            // newFile.createNewFile();
        } catch (Exception e) {
            System.out.println("error creating file "+filename);
        }
    }
}

class myThread implements Callable{
    public BlockingQueue<CellPath> path;
    public Maze toSolve;
    boolean[][] visited;
    int id;
    public myThread(BlockingQueue path, Maze toSolve, boolean[][] visited, int id){
        this.path = path;
        this.toSolve = toSolve;
        this.visited = visited;
        this.id = id;
    }
    public CellPath call(){

        while(true){
            CellPath curr = null;
            // System.out.println(id);
            try {
                if(path==null){
                    break;
                }
                curr = path.poll();
                if(curr == null){
                    break;
                }
                // System.out.println(path.size());

                // System.out.println("got "+ curr.cell.x+ " "+ curr.cell.y+ " "+curr.value);
            } catch (Exception e) {
                System.out.println("Error pulling from queue");
            }
            if(curr.cell.x == this.toSolve.fX && curr.cell.y == this.toSolve.fY){
                System.out.println("Found");
                path=null;
                return curr;
            }
            ArrayList nextTo = new ArrayList<CellPath>();
            visited[curr.cell.x][curr.cell.y] = true;
            if(curr.cell.down){
                int newX = curr.cell.x;
                int newY = curr.cell.y+1;
                CellPath newCellPath = new CellPath(curr, curr.pathLength+1, curr.pathLength + 1 + Math.abs(newX-this.toSolve.fX)+Math.abs(newY-this.toSolve.fY), this.toSolve.maze[newX][newY]);
                if(!visited[newX][newY]){
                    nextTo.add(newCellPath);
                }
            }
            if(curr.cell.up){
                int newX = curr.cell.x;
                int newY = curr.cell.y-1;
                CellPath newCellPath = new CellPath(curr, curr.pathLength+1, curr.pathLength + 1 + Math.abs(newX-this.toSolve.fX)+Math.abs(newY-this.toSolve.fY), this.toSolve.maze[newX][newY]);
                if(!visited[newX][newY]){
                    nextTo.add(newCellPath);
                }
            }
            if(curr.cell.left){
                int newX = curr.cell.x-1;
                int newY = curr.cell.y;
                CellPath newCellPath = new CellPath(curr, curr.pathLength+1, curr.pathLength + 1 + Math.abs(newX-this.toSolve.fX)+Math.abs(newY-this.toSolve.fY), this.toSolve.maze[newX][newY]);
                if(!visited[newX][newY]){
                    nextTo.add(newCellPath);
                }
            }
            if(curr.cell.right){
                int newX = curr.cell.x+1;
                int newY = curr.cell.y;
                CellPath newCellPath = new CellPath(curr, curr.pathLength+1, curr.pathLength + 1 + Math.abs(newX-this.toSolve.fX)+Math.abs(newY-this.toSolve.fY), this.toSolve.maze[newX][newY]);
                if(!visited[newX][newY]){
                    nextTo.add(newCellPath);
                }
            }
            if(path==null){
                break;
            }
            path.addAll(nextTo);
        }
        return null;
    }
}