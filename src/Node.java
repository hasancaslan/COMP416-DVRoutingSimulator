import java.util.Arrays;
import java.util.Random;

public class Node {
    private static int count = 0;

    // Each node will store a costs vector, its own distance vector
    // and a distance vector for each of its neighbors
    private int[] neighbors;
    private int[] cost = new int[DVSimulator.NUMNODES];
    private int[] myDV = new int[DVSimulator.NUMNODES];
    private int[][] neighborDV = new int[DVSimulator.NUMNODES][DVSimulator.NUMNODES];
    private int id;
    // fwd table specifies for reaching each destination (index) from current node
    // which neighbor node we should visit first
    private int[] fwdTable = new int[DVSimulator.NUMNODES];
    // bestPath is a temporary changing forwarding table
    private int[] bestPath = new int[DVSimulator.NUMNODES];
    private int numUpdates = 0;

    public Node() {
        this.id = count++;
        this.cost = DVSimulator.cost[this.id];
        this.neighbors = DVSimulator.neighbors[this.id];
        this.myDV = this.cost;

        for (int i = 0; i < DVSimulator.NUMNODES; i++) {
            // reading from the DVSimulator variables,
            // for each node:
            // 1. initialize its cost and myDV value
            // 2. specify the neighbors
            // 3. Initialize the forwarding table (bestPath variable)

            // BestPath to any node should be initialized as follows:
            // If node has id = this node's id, use id
            // Else if node is a direct neighbor, use the neighbor id
            // Otherwise, choose a random neighbor (see randomNeighbor method)

            if (i == this.id) {
                this.bestPath[i] = this.id;
            } else if (Node.contains(neighbors, i)) {
                this.bestPath[i] = i;
            } else {
                this.bestPath[i] = randomNeighbor();
            }
        }

        // send initial DV to neighbors
        notifyNeighbors();
    }

    public static boolean contains(final int[] arr, final int key) {
        return Arrays.stream(arr).anyMatch(i -> i == key);
    }

    public int getId() {
        return id;
    }

    public void printDV() {
        System.out.print("i            ");
        for (int i = 0; i < DVSimulator.NUMNODES; i++) {
            System.out.print(i + "      ");
        }
        System.out.println();
        System.out.print("cost         ");
        for (int i = 0; i < DVSimulator.NUMNODES; i++) {
            System.out.print(myDV[i] + "      ");
        }
        System.out.println();
    }

    public void printFwdTable() {
        System.out.println("dest         next Node");
        for (int i = 0; i < DVSimulator.NUMNODES; i++) {
            System.out.println(i + "            " + fwdTable[i]);
        }
    }

    public int randomNeighbor() {
        int rnd = new Random().nextInt(neighbors.length);
        return neighbors[rnd];
    }

    public void notifyNeighbors() {
        // for each neighbor, create a new packet (see Packet class)
        // with current node id as source, neighbor id as destination
        // and current node's DV as the dv
        // then send packet using helper method sendPacket in DVSimulator

        for (int neighbor : neighbors) {
            Packet packet = new Packet(this.id, neighbor, this.myDV);
            DVSimulator.sendPacket(packet);
        }
    }

    public void updateDV(Packet p) {
        // this method is called by the simulator each time a packet is received from a neighbor
        int neighbor_id = p.getSource();
        neighborDV[neighbor_id] = p.getDV();

        // for each value in the DV received from neighbor, see if it provides a cheaper path to
        // the corresponding node. If it does, update myDV and bestPath accordingly
        // current DV of i is min { current DV of i, cost to neighbor + neighbor's DV to i  }

        // If you do any changes to myDV:
        // 1. Notify neighbors about the new myDV using notifyNeighbors() method
        // 2. Increment the convergence measure numUpdates variable once

        boolean hasDVChanged = false;

        for (int i = 0; i < myDV.length; i++) {
            int new_cost = cost[neighbor_id] + neighborDV[neighbor_id][i];
            int current_cost = myDV[i];

            if (new_cost < current_cost) {
                myDV[i] = new_cost;
                bestPath[i] = neighbor_id;
                hasDVChanged = true;
            }
        }

        if (hasDVChanged) {
            notifyNeighbors();
            numUpdates++;
        }
    }

    public void buildFwdTable() {
        // just copy the final values of bestPath vector
        System.arraycopy(bestPath, 0, fwdTable, 0, DVSimulator.NUMNODES);
    }

    public int getNumUpdates() {
        return numUpdates;
    }
}
