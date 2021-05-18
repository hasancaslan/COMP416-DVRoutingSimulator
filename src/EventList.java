//Project 3, DV Simulator

public interface EventList
{
    boolean add(Event e);
    Event removeNext();
    String toString();
    double getLastPacketTime(int entityFrom, int entityTo);
}
