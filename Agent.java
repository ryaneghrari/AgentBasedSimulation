import java.util.*;

public class Agent implements AgentInterface
{
	protected static int ID = 0;

    private int row;
    private int col;
    protected AgentType type;	
	protected int id;
    //CHANGE THIS BACK TO PROTECTED
	public double[] nextEvents;
    protected boolean isMove;
    
	public Agent(AgentType which)
    {
		ID++;
		id = ID;
	    row = col = -1;
	    type = which;
	    nextEvents = new double[2];
	    nextEvents[0] = Simulation.rand.nextDouble();
	    nextEvents[1] = Double.MAX_VALUE;
	    isMove = true;
    }

    public int getRow() { return(row); }
    public int getCol() { return(col); }
    
    public AgentType getType() { return(type); }

    

    public void setRowCol(int row, int col)
    {
        this.row = row;
        this.col = col;
    }

    public int getID()
    {
		return id;
    }

    public double getNextTime()
    {
    	if(nextEvents[0] < nextEvents[1]) //prioritize eat
    	{
    		isMove = true;
    		return nextEvents[0];
    	}
    	else
    	{
    		isMove = false;
    		return nextEvents[1];
    	}
    }

    public void scheduleNextMove(double time)
    {
    	nextEvents[0] = time + Simulation.rand.nextDouble();
    }

    public Cell calculateMove(Cell[][] landscape)
	{	
		ArrayList<Cell> avail = getNeighborhood(this.getRow(),this.getCol(), landscape);
				                
		Cell cell = getPreferredMove(avail,this, landscape);															  
			
		return cell;																									                
	}
		
	protected ArrayList<Cell> getNeighborhood(int x, int y, Cell[][] landscape)
	{
		ArrayList<Cell> available = new ArrayList<Cell>();
				
		int numCells  = landscape[0].length;		
		int index = 0;
				
		for(int i = -1; i <= 1; i++)
		{
			for(int j = -1; j <= 1; j++)
			{
				if(i == 1 && j == 1)
				{
					continue;
				}
				int row = ((x + i) + numCells) % numCells;
				int col = ((y + j) + numCells) % numCells;
				
				Cell newCell = landscape[row][col];		
				Cell currentCell = landscape[x][y];		

				if(!newCell.isOccupied()) // is null
				{
					available.add(newCell);	
				}	
				/*			
				else if(landscape[x][y] == null)
				{
					System.out.println("WTFFFFFF: " + x + ",,,,,"  + y);
				}
				*/
				//else if(newCell.getType() != landscape[x][y].getType()) 
				// WE NEED TO CHANGE THIS!!!
				else if(newCell.hasMacrophage() && type == AgentType.BACTERIUM)
				{
					available.add(newCell);
				}
				else if(newCell.hasBacteria() && type == AgentType.MACROPHAGE)
				{
					available.add(newCell);
				}

				index++;
				}
			}		
			return available;
		}

		protected Cell getPreferredMove(ArrayList<Cell> list, Agent a, Cell[][] landscape)
		{
				int row = a.getRow();
				int col = a.getCol();

				Cell cell = new Cell(row,col);
				return cell;
		}

}
