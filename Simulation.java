import java.util.ArrayList;
import java.util.*;

/**
 * This class implements the next-event simulation engine for your agent-based
 * simulation.  You may choose to define other helper classes (e.g., Event,
 * EventList, etc.), but the main while loop of your next-event engine should
 * appear here, in the run(...) method.
 */

public class Simulation extends SimulationManager
{
	// you may choose to have two separate lists, or only one list of all
	private ArrayList<Macrophage> macrophageList;
	private ArrayList<Bacteria> bacteriaList;
	private ArrayList<Agent> driver; 
	private Cell[][] landscape;
	public static Random rand = new Random(12345);
	private int numCells;
	private int maxTime;

	/**************************************************************************
	 * Constructs a Simulation object.  This should just perform initialization
	 * and setup.  Later use the object to .run() the simulation.
	 *
	 * @param numCells       number of rows and columns in the environment
	 * @param guiCellWidth   width of each cell drawn in the gui
	 * @param numMacrophages number of initial macrophages in the environment
	 * @param numBacteria    number of initial bacteria in the environment
	 **************************************************************************/
	public Simulation(int numCells,       int guiCellWidth,
		int numMacrophages, int numBacteria, int maxTime)
	{
		// call the SimulationManager constructor, which itself makes sure to 
		// construct and store an AgentGUI object for drawing
		super(numCells, guiCellWidth, maxTime);
		this.numCells = numCells;

		time           = 0;
		macrophageList = new ArrayList<Macrophage>();
		bacteriaList   = new ArrayList<Bacteria>();
		landscape	   = new Cell[numCells][numCells];
		driver         = new ArrayList<Agent>();
		this.maxTime   = maxTime;

		//Initializing empty landscape
		for(int i = 0; i < numCells; i++)
		{
			for(int j = 0; j < numCells; j++)
			{
				landscape[i][j] = new Cell(i,j);			
			}
		}

		// as a simple example, construct the initial macrophages and
		// bacteria and add them "at random" to the landscape
		int row = 0, col = 0;
		for (int i = 0; i < numMacrophages; i++)
		{
			row = rand.nextInt(numCells);
			col = rand.nextInt(numCells);
			while (landscape[row][col].isOccupied()) 
			{
				row = rand.nextInt(numCells);
				col = rand.nextInt(numCells);
			}
			Macrophage m = new Macrophage(row,col);
			macrophageList.add(m);
			landscape[row][col].occupy(m);
			updateEventList(m);
		}

		for (int i = 0; i < numBacteria; i++)
		{
			row = rand.nextInt(numCells);
			col = rand.nextInt(numCells);

			while (landscape[row][col].isOccupied()) 
			{
				row = rand.nextInt(numCells);
				col = rand.nextInt(numCells);
			}

			Bacteria b = new Bacteria(row,col);
			bacteriaList.add(b);
			landscape[row][col].occupy(b);
			updateEventList(b);
		}
	}

	private void updateEventList(Agent a) 
	{   

		System.out.println("-----  updating event list ----------------------------");

		driver.remove(a);

		int length = driver.size();
		double time = a.getNextTime();
		for (int i = 0; i < length; i++)
		{
			if (time <= driver.get(i).getNextTime())
			{
				driver.add(i, a);
				System.out.println(driver);
				System.out.println("-----  DONE updating event list ----------------------------");
				return;
			}
		}

		driver.add(a); //add at the end

		System.out.println(driver);
		System.out.println("DONE event list END ----------------------------");
	}

	public double getMaxTime()
	{
		return maxTime;
	}

	/**************************************************************************
	 * Method used to run the simulation.  This method should contain the
	 * implementation of your next-event simulation engine (while loop handling
	 * various event types).
	 *
	 * @param guiDelay  delay in seconds between redraws of the gui
	 **************************************************************************/
	public void run(double guiDelay) throws InterruptedException
	{  

        this.gui        = new AgentGUI(this, numCells, guiCellWidth);

		// create a simple "simulation" example that just moves all the agents
		// down and to the right...
		int gridSize = this.gui.getGridSize();


		while(true)
		{
			time = driver.get(0).getNextTime();
			System.out.println("****************** BEGIN EVENT ******************");
			printDriver();
			System.out.println("Time is:\t" + time);
			if(driver.get(0).getType() == Agent.AgentType.BACTERIUM)
			{
				Bacteria b = (Bacteria)driver.get(0);
				System.out.println("Handling Bacteria:\t" + b.getID());

				if(b.isMoveNextEvent())
				{
					System.out.println("Move event...");
					Cell newCell = b.calculateMove(landscape); 
				landscape[b.getRow()][b.getCol()].removeBacteria(); // remove agent from landscape
				newCell.occupy(b);
				b.scheduleNextMove(time); //bacteria might get away!
				updateEventList(b);       //update event list

				if(newCell.hasMacrophage())
				{      
					/*SCHEDULE EAT*/
					Macrophage m = newCell.getMacrophage(); //bacteria moved into a macrophage cell.. yum yum
					System.out.println("Scheduling eat by:\t" + m.getID());
					m.scheduleEat(time);
					updateEventList(m);
				}
			}		
				else // divide event
				{
					System.out.println("Divide event...");
					b.scheduleNextDivide(time);
					Cell newCell = b.calculateMove(landscape); 
					updateEventList(b);

					if (! (newCell.getRow() == b.getRow() && newCell.getCol() == b.getCol())) 
					{
						Bacteria newB = new Bacteria(newCell.getRow(), newCell.getCol());
						bacteriaList.add(newB);
						newCell.occupy(newB);
						newB.scheduleNextMove(time); //bacteria might get away!
						newB.scheduleNextDivide(time);
						updateEventList(newB);

						if(newCell.hasMacrophage())
						{      
							/*SCHEDULE EAT*/
							Macrophage m = newCell.getMacrophage(); //bacteria moved into a macrophage cell.. yum yum
							System.out.println("Scheduling eat by:\t" + m.getID());
							m.scheduleEat(time);
							updateEventList(m);
						}
					}
				}
			}
			else // Agent is a MACROPHAGE
			{
				Macrophage m = (Macrophage)driver.get(0);
				System.out.println("Handling Macrophage:\t" + m.getID());

				if(m.isMoveNextEvent())
				{
					System.out.println("Move event...");
					Cell newCell = m.calculateMove(landscape);
					landscape[m.getRow()][m.getCol()].removeMacrophage(); // remove agent from landscape
					newCell.occupy(m);

					//Macrophage moved into cell where bacteria is
					if(newCell.hasBacteria())
					{      
						/*SCHEDULE EAT*/
						Macrophage m1 = newCell.getMacrophage(); 
						System.out.println("Scheduling eat by:\t" + m1.getID());
						m1.scheduleEat(time);
						updateEventList(m1);
					}

					m.scheduleNextMove(time);
				}
				else // eat event
				{
					System.out.println("Attempting to eat bacteria...");
					Cell currentCell = landscape[m.getRow()][m.getCol()];
					if(currentCell.hasBacteria())
					{
						Bacteria b = currentCell.getBacteria();
						System.out.println("Eating bacteria: " + b.getID());


					  currentCell.removeBacteria(); // from landscape... does this work?


					  driver.remove(b); // from event list
					  bacteriaList.remove(b); // from list of bacteria
					}

					m.scheduleEat(Double.MAX_VALUE);
				}

				updateEventList(m);
			}

			// remember to update the gui
			gui.update(guiDelay);
			System.out.println("******************* END EVENT *******************");
			System.out.println();
		}

	}

	public void printDriver()
	{
		System.out.println("------------------------Driver----------------------");
		for(int i = 0; i < driver.size(); i++)
		{
			System.out.println(driver.get(i).getType() + " " + driver.get(i).getID() + ": \t Next event time: " + driver.get(i).getNextTime() + "\t [" + driver.get(i).nextEvents[0] + ", " + driver.get(i).nextEvents[1] + "]");
		}
		System.out.println("------------------------Driver-end------------------");
	}

	public void print()
	{
		for(int i = 0; i < numCells; i++)
		{
			for(int j = 0; j < numCells; j++)
			{
				System.out.print(landscape[i][j] + "\t");
			}
			System.out.println();
		}
	}

	/**************************************************************************
	 * Accessor method that returns the number of macrophages still present.
	 * @return an integer representing the number of macrophages present
	 **************************************************************************/
	public int getNumMacrophages() { return(macrophageList.size()); }

	/**************************************************************************
	 * Accessor method that returns the number of bacteria still present.
	 * @return an integer representing the number of bacteria present
	 **************************************************************************/
	public int getNumBacteria()    { return(bacteriaList.size()); }

	/**************************************************************************
	 * Accessor method that returns the current time of the simulation clocl.
	 * @return a double representing the current time in simulated time
	 **************************************************************************/
	public double getTime()        { return(time); }

	/**************************************************************************
	 * Method that constructs and returns a single list of all agents present.
	 * This method is used by the gui drawing routines to update the gui based
	 * on the number and positions of agents present.
	 *
	 * @return an ArrayList<AgentInterface> containing references to all macrophages and bacteria
	 **************************************************************************/
	public ArrayList<AgentInterface> getListOfAgents()
	{
		// your implementation may differ depending on one or two lists...
		ArrayList<AgentInterface> returnList = new ArrayList<AgentInterface>();
		for (int i = 0; i < macrophageList.size(); i++) returnList.add( macrophageList.get(i) );
			for (int i = 0; i < bacteriaList.size(); i++)   returnList.add( bacteriaList.get(i) );
				return(returnList);
		}
	}
