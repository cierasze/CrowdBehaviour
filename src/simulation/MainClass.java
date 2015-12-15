package simulation;

import java.util.ArrayList;
import java.util.LinkedList;

import processing.core.PApplet;
import processing.core.PVector;

public class MainClass extends PApplet {
	
	public static int Width = 640;
	public static int Height = 480;
	public static int updateWidth = 160;
	int NumAgent_Total = 100;
	int NumAgent_TotalPolice = 80;
	int NumAgent_TotalCrowd = 300;
	ArrayList<Agent> Agents;
	int AgentContSize;
	int Field = 10;
	int boardWidth = Width - updateWidth+10; //490
	int boardHeight = Height; //480
	public static Cell[][] Cells;
	public int dangerZoneY = 0;
	public int dangerZoneHeight = 10;
	LinkedList<Cell> CrowdInDangerZone;
	boolean Danger;
	
	
	public void setup() {
		surface.setTitle("Crowd Simulation");
		background(0);
		noStroke();
		smooth();
		setCells();
		//setAgents();
		setAgents(NumAgent_TotalPolice,NumAgent_TotalCrowd);
	}
	
	public void settings() {
		size(Width,Height);
	}
	
	public void setCells() {
		Cells = new Cell[boardWidth/Field][boardHeight/Field];
		
		for(int i=0;i<boardWidth/Field;i++) {
			for(int j=0;j<boardHeight/Field;j++) {
				Cells[i][j] = new Cell(i+1,j+1,false);
			}
		}	
		
	}
	
	public void setAgents() {
		Agents = new ArrayList<Agent>();
		int commitmentLevel=1; //Setting commitment for testing
		
		for(int i=0;i<NumAgent_Total;i++) {
			
			/*
			 * Setting First Position
			 */
			
			int xP = (int) random(0,49);
			int yP = (int) random(0,48);
			Cell _CellP = Cells[xP][yP];
			PVector Position = new PVector();
			Position = _CellP.getVectorCell();
			//_CellP.setIsOccupied(true);
			
			/*
			 * Setting Agent's Destination
			 */
			/*
			int xD = (int) random(1,49);
			int yD = (int) random(1,48);
			Cell _CellD = Cells[xD][yD];
			PVector Destination = new PVector();
			Destination = _CellD.getVectorCell();
			*/
			
			/*--- test - begin ---*/
			int xD = xP;
			int yD = yP;
			Cell _CellD = Cells[xD][yD];
			PVector Destination = new PVector();
			Destination = _CellD.getVectorCell();
			/*--- test - end ---*/
					
			Agent _Agent = new Agent(Position, Destination, new PVector(0,0), commitmentLevel);
			Agents.add(_Agent);
			
		}
	}
	
	
	public void setAgents(int NumAgentTotalPolice, int NumAgentTotalCrowd) {
		Agents = new ArrayList<Agent>();
		
		int commitmentLevelPolice = 0;
		int commitmentLevelCrowdNormal = 1;
		int commitmentLevelCrowdAggresive = 2;
		int temp = 0;
		
		for(int i=1;i<=NumAgentTotalPolice;i++) {
			
			/*
			 * Setting First Position
			 */
			
			int xP =  i+temp;
			int yP =  2;
			
			if(i == 41) temp = 0;
			
			if(i>40) {
				xP = (int) i+temp-40;
				yP = (int) 3;
			}
			
			if(i%10 == 0) temp += 2;
			
			Cell _CellP = Cells[xP][yP];
			PVector Position = new PVector();
			Position = _CellP.getVectorCell();
			//_CellP.setIsOccupied(true);
			
			/*
			 * Setting Agent's Destination
			 */
			int xD = xP;
			int yD = yP;
			Cell _CellD = Cells[xD][yD];
			PVector Destination = new PVector();
			Destination = _CellD.getVectorCell();
					
			Agent _Agent = new Agent(Position, Destination, new PVector(0,0), commitmentLevelPolice);
			Agents.add(_Agent);
			
		}
		
		for(int i=0;i<NumAgentTotalCrowd;i++) {
			
			/*
			 * Setting First Position
			 */
			
			int xP = (int) random(1,47);
			int yP = (int) random(10,48);

			Cell _CellP = Cells[xP][yP];
			PVector Position = new PVector();
			Position = _CellP.getVectorCell();
			//_CellP.setIsOccupied(true);
			
			/*
			 * Setting Agent's Destination
			 */
			int xD = xP;
			int yD = yP;
			Cell _CellD = Cells[xD][yD];
			PVector Destination = new PVector();
			Destination = _CellD.getVectorCell();
			
			if((int)random(0,3) == 0) {
				Agent _Agent = new Agent(Position, Destination, new PVector(0,0), commitmentLevelCrowdAggresive);
				Agents.add(_Agent);
			} else {
				Agent _Agent = new Agent(Position, Destination, new PVector(0,0), commitmentLevelCrowdNormal);
				Agents.add(_Agent);
			}
			
		}
	}
	
	
	public void draw() {
		fill(230);
		rect(Width-updateWidth+10,10,updateWidth-20,Height-20);
		fill(0,50); //Second value
		rect(0, 0, Width-updateWidth+10, Height);
		
		 /*
		  * The method that draws a net. Auxiliary.
		  */
		//drawBoard(Field);
		
		updateAgents();
		updateInfo();
	}
	
	public void updateAgents() {
		float Timer = frameRate/500f;
		AgentContSize = Agents.size();

		CrowdInDangerZone = getCrowdInDangerZone();
		if(!CrowdInDangerZone.isEmpty()) Danger = true;
		else Danger = false;
		
		for(int i=0;((i<AgentContSize) && (AgentContSize!=0));i++) {
			Agent _Agent = Agents.get(i);
			_Agent.AgentControl(Timer);		
			drawAgent(_Agent);
		}
		
	}
	
	public void updateInfo() {
		
		String Title = "Crowd Simulation";
		String NumberOfAgents_TotalPolice = "Total Police:";
		String NumberOfAgents_TotalCrowd = "Total Crowd:";
		
		fill(51,51,255);
		textSize(20);
		textAlign(CENTER,BOTTOM);
		text(Title,Width-updateWidth+10,10,updateWidth-20,60);
		
		fill(0,0,0);
		textSize(10);
		textAlign(LEFT,BOTTOM);
		text(NumberOfAgents_TotalPolice,Width-updateWidth+15,20,updateWidth-60,80);
		
		fill(0,0,0);
		textSize(10);
		text(NumAgent_TotalPolice,Width-50,100);
		
		fill(0,0,0);
		textSize(10);
		textAlign(LEFT,BOTTOM);
		text(NumberOfAgents_TotalCrowd,Width-updateWidth+15,40,updateWidth-60,80);
		
		fill(0,0,0);
		textSize(10);
		text(NumAgent_TotalCrowd,Width-50,120);
		
		
		/*
		 * FrameRate
		 */
		fill(0);
		textSize(20);
		text(frameRate,Width-90,Height-20);
	}
	
	public void drawAgent(Agent _Agent) {
		switch(_Agent.commitmentLevel) {
			case 0:
				fill(0,0,255);
				break;
			case 1:
				fill(255,255,0);
				break;
			case 2:
				fill(255,0,0);
				break;
		}
		ellipse(_Agent.Position.x, _Agent.Position.y, 3, 3);
	}
	
	public ArrayList<Integer> getRowsWhereIsOccupied(boolean isOccupied) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		for(int i=0;i<boardWidth/Field;i++) {
			for(int j=0;j<boardHeight/Field;j++) {
				if(Cells[i][j].isOccupied == isOccupied) result.add(i);
			}
		}
		return result;
	}
	
	public LinkedList<Cell> getNearestNeighbourCells(Cell cell, boolean isOccupied) {
		LinkedList<Cell> result = new LinkedList<Cell>();
		
		if(cell.getRow() - 2 >= 0)
			result.add(Cells[cell.getRow() - 2][cell.getCol() - 1]);
		
		if(cell.getRow() <= boardHeight/Field - 2)
			result.add(Cells[cell.getRow()][cell.getCol() - 1]);
		
		if(cell.getCol() - 2 >= 0)
			result.add(Cells[cell.getRow() - 1][cell.getCol() - 2]);
		
		if(cell.getCol() <= boardWidth/Field - 2)
			result.add(Cells[cell.getRow()- 1][cell.getCol()]);
		
		for(int i = 0; i < result.size();) {
			if(result.get(i).isOccupied != isOccupied)
				result.remove(i);
			else i++;
		}	
		return result;
	}
	
	
	public LinkedList<Cell> getCrowdInDangerZone() {
		LinkedList<Cell> result = new LinkedList<Cell>();
		
		for(int i = 0; i < boardWidth/Field; i++) {
			for(int j = dangerZoneY; j < dangerZoneHeight; j++) {
				if(Cells[i][j].isOccupied && Cells[i][j].agent.commitmentLevel == 2) result.add(Cells[i][j]);
			}
		}
		
		return result;
	}
	
	public void drawBoard(int BoardRate) {
		stroke(255);
		
		for(int i = 0; i <= boardWidth/BoardRate; i++) {
			line(0,i*10,boardWidth,i*10);
		}			
		for(int j=0;j<=boardHeight/BoardRate;j++) {
			line(j*10,0,j*10,boardHeight);
		}
	}
	
	
	static public void main(String[] passedArgs) {
	    String[] appletArgs = new String[] { "simulation.MainClass" };
	    if (passedArgs != null) {
	      PApplet.main(concat(appletArgs, passedArgs));
	    } else {
	      PApplet.main(appletArgs);
	    }	    
	  }

}