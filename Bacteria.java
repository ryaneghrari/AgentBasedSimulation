import java.util.*;

public class Bacteria extends Agent
{
	public Bacteria() 
	{
			super(AgentType.BACTERIUM);
	}
	
	public Bacteria(int x, int y)
	{
			super(AgentType.BACTERIUM);
			setRowCol(x,y);	
			nextEvents[1] = Simulation.rand.nextDouble();
	}

/*
	public Cell divide(Cell[][] landscape)
	{
			ArrayList<Cell> avail = getNeighborhood(this.getRow(),this.getCol(),landscape);
							
			Cell cell = getPreferredMove(avail, landscape);

			return cell;
	}
	*/

    public void scheduleNextDivide(double time)
    {
    	nextEvents[1] = time + Simulation.rand.nextDouble();
    }

	protected Cell getPreferredMove(ArrayList<Cell> list, Cell[][] landscape)
	{
		System.out.println("Bacteria's method");

		int row = this.getRow();
		int col = this.getCol();

		if(list.size() == 0)
		{
			Cell cell = landscape[row][col];
			return cell;
		}

		int index = Simulation.rand.nextInt(list.size());	
		return list.get(index);		
	}

	

}
