//Project 3, DV Simulator

public class Event
{
    private double time;
    private int entity;
    private Packet packet;
    
    public Event(double t, int ent, Packet p)
    {
        time = t;
        entity = ent;
        packet = new Packet(p);
    }

    
    public double getTime()
    {
        return time;
    }

    public int getEntity()
    {
        return entity;
    }
    
    public Packet getPacket()
    {
        return packet;
    }
    
    public String toString()
    {
        return("time: " + time +  "  entity: " + entity +
               "packet: " + packet);
    }
        
}
