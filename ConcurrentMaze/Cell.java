package ConcurrentMaze;

public class Cell{
    boolean up, down, left, right, start, finish;
    int x, y;

    public Cell(boolean up, boolean down, boolean left, boolean right, boolean start, boolean finish, int x, int y){
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.start = start;
        this.finish = finish;
        this.x = x;
        this.y = y;
    }
    public Cell(Cell copy){
        this.up = copy.up;
        this.down = copy.down;
        this.left = copy.left;
        this.right = copy.right;
        this.start = copy.start;
        this.finish = copy.finish;
        this.x = copy.x;
        this.y = copy.y;
    }
    public Cell(String cellString, int x, int y){
        this.up = cellString.contains("u");
        this.down = cellString.contains("d");
        this.left = cellString.contains("l");
        this.right = cellString.contains("r");
        this.start = cellString.contains("s");
        this.finish = cellString.contains("f");
        this.x = x;
        this.y = y;
    }

    public boolean[] getDirs(){
        boolean[] ret = {this.up, this.right, this.down, this.left};
        return ret;
    }
    public void changeDir(char dir, boolean newValue){
        switch(dir){
            case 'u':
                this.up = newValue;
                break;
            case 'd':
                this.down = newValue;
                break;
            case 'r':
                this.right = newValue;
                break;
            case 'l':
                this.left = newValue;
                break;
        }
    }
    public boolean isStart(){
        return this.start;
    }
    public boolean isFinish(){
        return this.finish;
    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public String writeCell(){
        String str = "";
        if(this.up){
            str+="u";
        }
        if(this.right){
            str+="r";
        }
        if(this.down){
            str+="d";
        }
        if(this.left){
            str+="l";
        }
        if(this.start){
            str+="s";
        }
        if(this.finish){
            str+="f";
        }
        return str;
    }

    public String toString(){
        return x+" "+y;
    }
}