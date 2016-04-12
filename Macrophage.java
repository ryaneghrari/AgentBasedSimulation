import java.util.*;

public class Macrophage extends Agent
{
	public Macrophage()
	{	
		super(AgentType.MACROPHAGE);
	}
	
	public Macrophage(int x, int y)
	{
		super(AgentType.MACROPHAGE);
	    setRowCol(x,y);	
	}

	protected Cell getPreferredMove(ArrayList<Cell> list, Cell[][] landscape)
	{
//		System.out.println("Macro's method");
		int row = this.getRow();
		int col = this.getCol();
		if(list.size() == 0)
		{
			Cell cell = landscape[row][col];
			return cell;
		}

		ArrayList<Cell> bacteriaCells = new ArrayList<Cell>();
		for(int i = 0; i < list.size(); i++)
		{
			int x = list.get(i).getRow();
			int y = list.get(i).getCol();
		   
			if(landscape[x][y].hasBacteria())
			{
				bacteriaCells.add(list.get(i));		
			}	
		}

		//no preferred move, pick from neighboorhood
		if(bacteriaCells.size() == 0)
		{
			int index = Simulation.rand.nextInt(list.size());	
			return list.get(index);
		}
		else
		{
			int index = Simulation.rand.nextInt(bacteriaCells.size());	
			return bacteriaCells.get(index);	
		}	
	}

    public void scheduleEat(double time)
    {
    	nextEvents[1] = time; //+ Simulation.rand.nextDouble();
    }
}
