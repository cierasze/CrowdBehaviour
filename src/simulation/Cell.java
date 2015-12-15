package simulation;

import processing.core.PVector;

public class Cell {
	
	int Row;
	int Col;
	boolean isOccupied;
	Agent agent;
	
	Cell(int Row, int Col, boolean isOccupied) {
		this.Row = Row;
		this.Col = Col;
		this.isOccupied = isOccupied;
	}
	
	public int getRow() {
		return Row;
	}
	
	public int getCol() {
		return Col;
	}
	
	public boolean getIsOccupied() {
		return isOccupied;
	}
	
	public void setRow(int Row) {
		this.Row = Row;
	}
	
	public void setCol(int Col) {
		this.Col = Col;
	}
	
	public void setIsOccupied(Agent _agent, boolean isOccupied) {
		this.isOccupied = isOccupied;
		this.agent = _agent;
	}
	
	public PVector getVectorCell() {
		PVector Vector = new PVector(5,5);		
		
		for(int i=1;i<this.Row;i++)
			Vector.add(10,0);
		
		for(int j=1;j<this.Col;j++)
			Vector.add(0,10);
		
		return Vector;	
	}
}