public class Tester
{
    public static void main(String[] args) throws InterruptedException
    {
        // construct a simulation object w/ appropriate parameters and then run
        int numCells       = 8;
        int guiCellWidth   = 20;
        int numMacrophages = 2;
        int numBacteria    = 2;
		int maxTime        = 100;

        Simulation s = new Simulation(numCells, guiCellWidth,
                                      numMacrophages, numBacteria,maxTime);
        double guiDelayInSecs = 1.0;
        s.run(guiDelayInSecs);
    }
}
