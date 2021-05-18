//Project 3, Distance Vector Simulator

public class DVSimulator
{    
    // This is the number of nodes in the simulator
    public static final int NUMNODES = 7;


    // Parameters of the simulation
    private static EventList eventList;
    private static double time;

    // Data used for the simulation
    private Node[] nodes;
    public static int[][] cost;
    public  static int[][] neighbors;

    // Initializes the simulator
    public DVSimulator()
    {
        eventList = new EventListImpl();
        time = 0.0;

        neighbors = new int[NUMNODES][];
        neighbors[0] = new int[] {1, 3};
        neighbors[1] = new int[] {0, 2, 3, 4};
        neighbors[2] = new int[] {1, 4, 6};
        neighbors[3] = new int[] {0, 1, 5, 6};
        neighbors[4] = new int[] {1, 2, 6};
	neighbors[5] = new int[] {3, 6};
	neighbors[6] = new int[] {2, 3, 4, 5};

        //Weight of the edges in the graph
        cost = new int[NUMNODES][NUMNODES];
        cost[0][0] = 0;
        cost[0][1] = 2;
        cost[0][2] = 999;
	cost[0][3] = 4;
        cost[0][4] = 999;
	cost[0][5] = 999;
	cost[0][6] = 999;
        
	cost[1][0] = 2;
        cost[1][1] = 0;
        cost[1][2] = 3;
        cost[1][3] = 5;
	cost[1][4] = 8;
	cost[1][5] = 999;
	cost[1][6] = 999;
		
	cost[2][0] = 999;
        cost[2][1] = 3;
        cost[2][2] = 0;
        cost[2][3] = 999;
	cost[2][4] = 5;
	cost[2][5] = 999;
	cost[2][6] = 8;
        
	cost[3][0] = 4;
        cost[3][1] = 5;
        cost[3][2] = 999;
        cost[3][3] = 0;
	cost[3][4] = 999;
	cost[3][5] = 7;
	cost[3][6] = 11;
		
	cost[4][0] = 999 ;
	cost[4][1] = 8;
	cost[4][2] = 5;
	cost[4][3] = 999;
	cost[4][4] = 0;
	cost[4][5] = 999;
	cost[4][6] = 1;

	cost[5][0] = 999 ;
	cost[5][1] = 999;
	cost[5][2] = 999;
	cost[5][3] = 7;
	cost[5][4] = 999;
	cost[5][5] = 0;
	cost[5][6] = 2;

	cost[6][0] = 999 ;
	cost[6][1] = 999;
	cost[6][2] = 8;
	cost[6][3] = 11;
	cost[6][4] = 1;
	cost[6][5] = 2;
	cost[6][6] = 0;

        nodes = new Node[NUMNODES];
        for(int i = 0; i< NUMNODES; i++) {
            nodes[i] = new Node();
        }
    }
    
    // Starts the simulation. It will end when no more packets are in the medium
    // you may skip reading this method
    public void runSimulator()
    {
        Event next;
        Packet p;
        
        while(true)
        {
            next = eventList.removeNext();
            
            if (next == null)
            {
                break;
            }

            System.out.println();
            System.out.println("main(): event received.  t=" +
                               next.getTime() +", node=" +
                               next.getEntity());
            p = next.getPacket();
            System.out.print("  src=" + p.getSource() + ", ");
            System.out.print("dest=" + p.getDest() + ", ");
            System.out.print("DV=[");
            for (int i = 0; i < NUMNODES - 1; i++)
            {
                System.out.print(p.getDV(i) + ", ");
            }
            System.out.println(p.getDV(NUMNODES - 1) + "]");
            
            time = next.getTime();

            p = next.getPacket();
            if ((next.getEntity() < 0) || (next.getEntity() >= NUMNODES))
            {
                System.out.println("main(): Panic. Unknown event node.");
            }
            else {
                nodes[next.getEntity()].updateDV(p);
            }
        }
        System.out.println();

        // build Forwarding table for each node
        for (Node n:nodes) {
            System.out.println("Node# " + n.getId());
            System.out.println("Converged after " + n.getNumUpdates() + " updates.");
            System.out.println();
            System.out.println("Distance Vector:");
            n.printDV();
            System.out.println();
            n.buildFwdTable();
            System.out.println("Forwarding Table");
            n.printFwdTable();
            System.out.println();
        }

        System.out.println();

        System.out.println("Simulator terminated at t=" + time +
                           ", no packets in medium.");        
    }
    
    // Sends a packet into the medium
    public static void sendPacket(Packet p)
    {
        Packet currentPacket;
        double arrivalTime;
    
        if ((p.getSource() < 0) || (p.getSource() >= NUMNODES))
        {
            System.out.println("sendPacket(): WARNING: Illegal source id in " +
                               "packet; ignoring.");
            return;
        }
        if ((p.getDest() < 0) || (p.getDest() >= NUMNODES))
        {
            System.out.println("sendPacket(): WARNING: Illegal destination id " +
                               "in packet; ignoring.");
            return;
        }
        if (p.getSource() == p.getDest())
        {
            System.out.println("sendPacket(): WARNING: Identical source and " +
                               "destination in packet; ignoring.");
            return;
        }
        if (cost[p.getSource()][p.getDest()] == 999)
        {
            System.out.println("sendPacket(): WARNING: Source and destination " +
                               "not connected; ignoring.");
            return;
        }

        
        arrivalTime = eventList.getLastPacketTime(p.getSource(), p.getDest());
        if (arrivalTime == 0.0)
        {
            arrivalTime = time;
        }
        arrivalTime = arrivalTime + 9.0;

        currentPacket = new Packet(p);
        eventList.add(new Event(arrivalTime,
                                currentPacket.getDest(), currentPacket));
    }

}
