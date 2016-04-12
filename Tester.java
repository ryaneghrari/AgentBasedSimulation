public class Tester
{
    public static void main(String[] args) throws InterruptedException
    {
        // construct a simulation object w/ appropriate parameters and then run
        int numCells       = 40;
        int guiCellWidth   = 10;
        int numMacrophages = 50;
        int numBacteria    = 100;
		int maxTime        = 100;

        Simulation s = new Simulation(numCells, guiCellWidth,
                                      numMacrophages, numBacteria,maxTime);
        
		
		double guiDelayInSecs = .01;
		


        s.run(guiDelayInSecs);
    }
}
