//Project 3, Distance Vector Simulator

public class Packet
{
    private int source;
    private int dest;
    private int[] dv;
    
    public Packet(Packet p)
    {
        source = p.getSource();
        dest = p.getDest();
        dv = new int[DVSimulator.NUMNODES];
        for (int i = 0; i < DVSimulator.NUMNODES; i++)
        {
            dv[i] = p.getDV(i);
        }
    }
    
    public Packet(int s, int d, int[] dv)
    {
        source = s;
        dest = d;
        
        if (dv.length != DVSimulator.NUMNODES)
        {
            System.out.println("Packet(): Invalid data format.");
            System.exit(1);
        }

        this.dv = new int[DVSimulator.NUMNODES];
        for (int i = 0; i < DVSimulator.NUMNODES; i++)
        {
            this.dv[i] = dv[i];
        }
    }        
    
    public int getSource()
    {
        return source;
    }
    
    public int getDest()
    {
        return dest;
    }
    
    public int getDV(int node)
    {
        return dv[node];
    }

    public int[] getDV() {
        return dv;
    }

    public String toString()
    {
        String str;
        str = "source: " + source + "  dest: " + dest + "  DV: ";
        
        for (int i = 0; i < DVSimulator.NUMNODES; i++)
        {
            str = str + i + "=" + getDV(i) + " ";
        }
        
        return str;
        
    }
    
}
