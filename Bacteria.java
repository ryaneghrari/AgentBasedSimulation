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

	public Cell divide(Cell[][] landscape)
	{
			ArrayList<Cell> avail = getNeighborhood(this.getRow(),this.getCol(),landscape);
							
			Cell cell = getPreferredMove(avail,this,landscape);

			return cell;
	}

    public void scheduleNextDivide(double time)
    {
    	nextEvents[1] = time + Simulation.rand.nextDouble();
    }

	protected Cell getPreferredMove(ArrayList<Cell> list, Agent a, Cell[][] landscape)
	{
		int row = a.getRow();
		int col = a.getCol();

		if(list.size() == 0)
		{
				Cell cell = new Cell(row,col);
				return cell;
		}

		int index = Simulation.rand.nextInt(list.size());	
		return list.get(index);		
	}

	public boolean isMoveNextEvent() 
    { 
    	if(isMove)
    	{
    		nextEvents[0] += Simulation.rand.nextDouble();
    	}
    	else
    	{
    		nextEvents[1] += Simulation.rand.nextDouble();
    	}

    	return(isMove); 
    	
    }

}
