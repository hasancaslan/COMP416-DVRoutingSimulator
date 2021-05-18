public class Project
{
    public final static void main(String[] argv)
    {
        DVSimulator simulator;

        System.out.println("Initializing DV Simulator..");

        simulator = new DVSimulator();

        System.out.println("Running DV Simulator..");

        simulator.runSimulator();
    }
}
