package ConcurrentMaze;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JComponent;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

public class Maze extends JComponent{
    int Xdim, Ydim, sX, sY, fX, fY;
    Cell[][] maze;
    ArrayList<Cell> solution = new ArrayList<Cell>();

    public Maze(int Xdim, int Ydim, int sX, int sY, int fX, int fY){
        if(sX==fX && sY == fY){
            throw new IllegalArgumentException("Start position cannot be the same as Finish position");
        }
        this.Xdim = Xdim;
        this.Ydim = Ydim;
        this.sX = sX;
        this.sY = sY;
        this.fX = fX;
        this.fY = fY;
        this.maze = new Cell[Xdim][Ydim];
        for(int x = 0; x < this.Xdim; x++){
            for(int y = 0; y < this.Ydim; y++){
                if(x == this.sX && y == this.sY){
                    this.maze[x][y] = new Cell(false, false, false, false, true, false,x,y);
                }else if(x == this.fX && y == this.fY){
                    this.maze[x][y] = new Cell(false, false, false, false, false, true,x,y);
                }else{
                    this.maze[x][y] = new Cell(false, false, false, false, false, false,x,y);
                }
            }
        }
    }

    public Maze(String filename) throws FileNotFoundException{
        File mazeFile = new File(filename);
        Scanner scan = new Scanner(mazeFile);
        String header = scan.nextLine();
        String[] headInfo = header.split(",");
        this.Xdim = Integer.parseInt(headInfo[0]);
        this.Ydim = Integer.parseInt(headInfo[1]);
        this.sX = Integer.parseInt(headInfo[2]);
        this.sY = Integer.parseInt(headInfo[3]);
        this.fX = Integer.parseInt(headInfo[4]);
        this.fY = Integer.parseInt(headInfo[5]);
        this.maze = new Cell[this.Xdim][this.Ydim];
        while(scan.hasNext()){
            String line = scan.nextLine();
            if(!line.contains(")")){
                String[] cords = line.split(", ");

                for(String i : cords){
                    String[] pos = i.split(" ");
                    // System.out.println(pos.toString());
                    int x = Integer.parseInt(pos[0]);
                    int y = Integer.parseInt(pos[1]);
                    this.solution.add(this.maze[x][y]);
                }
            }else{
                int endPos = line.indexOf(")");
                String[] cords = line.substring(1, endPos).split(",");
                int x = Integer.parseInt(cords[0]);
                int y = Integer.parseInt(cords[1]);
                String cellString = line.substring(endPos+1);
                this.maze[x][y] = new Cell(cellString,x,y);
            }
        }
        scan.close();
    }

    public void setCell(int X, int Y, Cell newCell){
        if(X >= this.Xdim || Y >= this.Ydim || X < 0 || Y < 0 || newCell == null){
            throw new IndexOutOfBoundsException("invalid Cell index");
        }else{
            maze[X][Y] = newCell;
        }
    }

    public Cell getCell(int X, int Y){
        if(X >= this.Xdim || Y >= this.Ydim || X < 0 || Y < 0){
            throw new IndexOutOfBoundsException("invalid Cell index");
        }else{
            return this.maze[X][Y];
        }
    }
    public int getXdim(){
        return this.Xdim;
    }
    public int getYdim(){
        return this.Ydim;
    }
    public void printMaze(){
        for(int y = 0; y < maze[0].length; y++){
            String line1 = "";
            String line2 = "";
            String line3 = "";
            for(int x = 0; x < maze.length; x++){
                // line1+="*";
                if(x==0){
                    line1 += "*";
                    line2 += maze[x][y].left ? " " : "*";
                    line3 += "*";
                }
                line1 += maze[x][y].up ? " *" : "**";
                line2 += maze[x][y].start ? "s" : maze[x][y].finish ? "f" : " ";
                line2 += maze[x][y].right ? " " : "*";
                line3 += maze[x][y].down ? " *" : "**";
            }
            if(y==0)
                System.out.println(line1);
            System.out.println(line2);
            System.out.println(line3);
        }
    }

    public void toFile(String filename){
        try {
            File newFile = new File(filename);
            newFile.createNewFile();
        } catch (Exception e) {
            System.out.println("error creating file "+filename);
        }
        try {
            StringBuffer str = new StringBuffer();;
            str.append(this.Xdim + "," + this.Ydim + "," + this.sX + ","+ this.sY + "," + this.fX + ","+ this.fY + "\n");
            FileWriter fileToWrite = new FileWriter(filename);
            for(int x = 0; x < this.Xdim; x++){
                for(int y = 0; y < this.Ydim; y++){
                    str.append("("+x+","+y+") ");
                    str.append(maze[x][y].writeCell());
                    if(y != this.Ydim-1){
                        str.append("\n");
                    }
                }
                // System.out.println("line: "+ x);
                str.append("\n");
                fileToWrite.append(str.toString());
                str = new StringBuffer();
            }
            // fileToWrite.write(str.toString());
            fileToWrite.close();
        } catch (Exception e) {
            System.out.println("error writing to file "+filename);
        }
        
    }

    public void paint(Graphics g){
        int scale = 1000/Math.max(this.Xdim, this.Ydim);
        for(int x = 0; x < this.maze.length;x++){
            for(int y = 0; y < this.maze[0].length;y++){
                // g.fillRect(scale*x, scale*y, scale-1, scale-1);
                Cell currCell = this.getCell(x, y);
                g.setColor(Color.BLACK);
                if(!currCell.left)
                g.drawLine(scale*x, scale*y, scale*x, scale*(y+1));
                if(!currCell.up)
                g.drawLine(scale*x, scale*y, scale*(x+1), scale*y);
                if(!currCell.down)
                g.drawLine(scale*x, scale*(y+1), scale*(x+1), scale*(y+1));
                if(!currCell.right)
                g.drawLine(scale*(x+1), scale*y, scale*(x+1), scale*(y+1));
                if(currCell.isFinish()){
                    g.setColor(Color.red);
                    g.fillRect(scale*x+1, scale*y+1, scale-2, scale-2);
                }else if(currCell.isStart()){
                    g.setColor(Color.green);
                    g.fillRect(scale*x+1, scale*y+1, scale-2, scale-2);
                }
                
            }
        }
        if(solution!=null){
            g.setColor(Color.blue);
            for(int i = 0; i < solution.size(); i++){
                Cell pathCurr = solution.get(i);
                if(i!=solution.size()-1){
                    Cell next = solution.get(i+1);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setStroke(new BasicStroke(scale/10));
                    g2.drawLine(scale*pathCurr.getX()+(scale/2), scale*pathCurr.getY()+(scale/2), scale*(next.getX())+(scale/2), scale*next.getY()+(scale/2));
                }
                // g.fillRect(scale*pathCurr.getX()+(scale/4), scale*pathCurr.getY()+(scale/4), scale-(scale/2), scale-(scale/2));
            }
        }
    }
}
