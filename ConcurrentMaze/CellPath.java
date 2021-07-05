package ConcurrentMaze;

public class CellPath implements Comparable<CellPath>{
    Cell cell;
    int pathLength;
    int value;
    CellPath previous;
    public CellPath(CellPath previous, int pathLength, int value, Cell cell){
        this.cell = cell;
        this.previous = previous;
        this.value = value;
        this.pathLength = pathLength;
    }

    public int compareTo(CellPath toCompare){
        if(this.value==toCompare.value){
            return this.pathLength-toCompare.pathLength;
        }
        return this.value-toCompare.value;
    }
}