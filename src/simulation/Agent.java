package simulation;

import java.util.LinkedList;

import processing.core.PVector;

public class Agent extends MainClass{
	
	PVector Position;
	PVector Destination;
	PVector FinalDestination;
	PVector Velocity;
	int commitmentLevel; // 0 -> Police | 1 -> CrowdNormal | 2 -> CrowdAggresive
	
	int Anger;
	
	Agent(){}
	
	@SuppressWarnings("deprecation")
	Agent(PVector Position, PVector FinalDestination, PVector Velocity, int commitmentLevel) {
		this.Position = Position.get();
		this.FinalDestination = FinalDestination.get();
		this.Velocity = Velocity.get();
		this.commitmentLevel = commitmentLevel;
		
		this.Destination = FinalDestination.get();
		
		this.Anger = 0;
	}
	
	public void AgentControl(float Timer) {		
		
		
		int _Row = round(Position.x)/10;
		int _Col = round(Position.y)/10;
		
		Cells[_Row][_Col].setIsOccupied(this, true);
		
		behaviour(Cells[_Row][_Col]);
		
		
		if(round(Position.y) < Destination.y)
			Bottom(Velocity);
		if(round(Position.y) > Destination.y)
			Top(Velocity);		
		if(round(Position.x) > Destination.x)
			Left(Velocity);
		if(round(Position.x) < Destination.x)
			Right(Velocity);
		if(round(Position.x) == Destination.x && round(Position.y) == Destination.y) {
			Stay(Velocity);	
			//Cell dest = Cells[(int) random(0, boardWidth/Field)][(int) random(0, boardHeight/Field)];			
			//Destination = dest.getVectorCell();
			movementBehaviour(_Row, _Col);
		}
			
		
		PVector Position1 = new PVector(Position.x,Position.y);
		Position1.add(PVector.mult(Velocity, Timer));
		
		int __Row = round(Position1.x)/10;
		int __Col = round(Position1.y)/10;
		
		if(_Row != __Row || _Col != __Col) {
			if(!Cells[__Row][__Col].isOccupied) {
				Cells[__Row][__Col].setIsOccupied(this, true);
				Cells[_Row][_Col].setIsOccupied(null, false);
				Position.add(PVector.mult(Velocity, Timer));
			} else {
				LinkedList<Cell> _cells = getNearestNeighbourCells(Cells[_Row][_Col], false);			
				
				if(!_cells.isEmpty()) {
					Cell dest = _cells.get((int) random(0, _cells.size()));				
					Destination = dest.getVectorCell();
					
					if(round(Position.y) < Destination.y)
						Bottom(Velocity);
					if(round(Position.y) > Destination.y)
						Top(Velocity);		
					if(round(Position.x) > Destination.x)
						Left(Velocity);
					if(round(Position.x) < Destination.x)
						Right(Velocity);
					if(round(Position.x) == Destination.x && round(Position.y) == Destination.y) {
						Stay(Velocity);	
						Destination = FinalDestination;
					}
									
					Position1 = new PVector(Position.x,Position.y);
					Position1.add(PVector.mult(Velocity, Timer));
					
					__Row = round(Position1.x)/10;
					__Col = round(Position1.y)/10;
									
					Cells[__Row][__Col].setIsOccupied(this, true);
					Cells[_Row][_Col].setIsOccupied(null, false);
					Position.add(PVector.mult(Velocity, Timer));
				}
			}
			
		} else {
			Position.add(PVector.mult(Velocity, Timer));
		}
			
		if (Position.x > MainClass.Width - MainClass.updateWidth +10)
			Position.x = 0;
		if (Position.x < 0)
			Position.x = MainClass.Width - MainClass.updateWidth +10;
		if (Position.y > MainClass.Height)
			Position.y = 0;
		if (Position.y < 0)
			Position.y = MainClass.Height;
		
	}
	
	public PVector Left(PVector Output) {
		Output.x = -5;
		Output.y = 0;
		return Output;
	}
	
	public PVector Right(PVector Output) {
		Output.x = 5;
		Output.y = 0;
		return Output;
	}
	
	public PVector Top(PVector Output) {
		Output.x = 0;
		Output.y = -5;
		return Output;
	}
	
	public PVector Bottom(PVector Output) {
		Output.x = 0;
		Output.y = 5;
		return Output;
	}
	
	public PVector Stay(PVector Output) {
		Output.x = 0;
		Output.y = 0;
		return Output;
	}
	
	public void behaviour(Cell cell) {
		int Normal=0,Aggresive=0;
		LinkedList<Cell> neighbours = getNearestNeighbourCells(cell, true);
		switch(commitmentLevel) {
			case 0:
				
				break;
			case 1:
				for(int i=0;i<neighbours.size();i++) {
					if(neighbours.get(i).agent.commitmentLevel == 1) Normal += 1;
					else if(neighbours.get(i).agent.commitmentLevel == 2) Aggresive += 1;
					
					if(Aggresive == Normal) Anger += 50;
					else if(Aggresive > Normal) Anger += 100;
					
					if(Anger >= 1000) commitmentLevel = 2;
				}
				break;
			case 2:
				
				break;
		}
	}
	
	public void movementBehaviour(int Row, int Col) {
		Cell dest;
		int newX, newY;
		switch(commitmentLevel) {
			case 0:
				
				break;
			case 1:
				newX = (int) random(Row - 2, Row + 3);
				newY = (int) random(Col - 5, Col + 4);
				
				if(newY < 10) newY = (int) random(Col, Col + 6);
				
				while(newY < 0 || newY > 47) {
					if(newY < 10) newY = (int) random(Col, Col + 6);
					else newY = (int) random(Col - 5, Col + 4);
				}
					
				while(newX < 0 || newX > 48)
					newX = (int) random(Row - 2, Row + 3);
				
				System.out.println(Row + "-"+newX + "    " + Col + "-"+ newY);
				
				dest = Cells[newX][newY];			
				Destination = dest.getVectorCell();
				FinalDestination = Destination;
				break;
			case 2:
				newX = (int) random(Row - 2, Row + 3);
				newY = (int) random(Col - 8, Col + 4);
				
				if(newY < 3) newY = (int) random(Col, Col + 3);
				
				while(newY < 0 || newY > 47) {
					if(newY < 10) newY = (int) random(Col, Col + 3);
					else newY = (int) random(Col - 8, Col + 4);
				}
					
				while(newX < 0 || newX > 48)
					newX = (int) random(Row - 2, Row + 3);
				
				System.out.println(Row + "-"+newX + "    " + Col + "-"+ newY);
				
				dest = Cells[newX][newY];			
				Destination = dest.getVectorCell();
				FinalDestination = Destination;
				break;
		}
	}
}