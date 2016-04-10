import java.util.*;

public class Macrophage extends Agent
{


		public Macrophage() //DONT USE THIS yet
		{	
			super(AgentType.MACROPHAGE);
		}
		
		public Macrophage(int x, int y)
		{
			super(AgentType.MACROPHAGE);
		    setRowCol(x,y);	
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

	public boolean isMoveNextEvent() 
    { 
    	if(isMove)
    	{
    		nextEvents[0] += Simulation.rand.nextDouble();
    	}
    	return(isMove); 
    	
    }

    public void scheduleEat(double time)
    {
    	nextEvents[0] = Double.MAX_VALUE;
    	nextEvents[1] = time;//+ Simulation.rand.nextDouble();
    	
    	isMove = false;
    }

    public void cantEat() {
    	nextEvents[1] = Double.MAX_VALUE;
    	isMove = true;
    }
}
