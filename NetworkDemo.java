package demo19083;

import base.*;

import java.util.ArrayList;

public class NetworkDemo extends Network {

    @Override
    public void add(Hub hub) {
        if (hub_list.contains(hub)) return;
        hub_list.add(hub);
        System.out.println(hub_list.size());
    }

    @Override
    public void add(Highway hwy) {
        if (hwy_list.contains(hwy)) return;
        hwy_list.add(hwy);
        for(Hub h : hub_list) {
            if(hwy.getStart() == h) h.add(hwy);
        }
    }

    @Override
    public void add(Truck truck) {
        if (truck_list.contains(truck)) return;
        truck_list.add(truck);
    }

    @Override
    public void start() {
        for(Hub h: hub_list) h.start();
        for(Truck t: truck_list) t.start();
    }

    @Override
    public void redisplay(Display disp) {
        for(Hub h : hub_list) h.draw(disp);
        for(Highway h : hwy_list) h.draw(disp);
        for(Truck t : truck_list) t.draw(disp);
    }

    @Override
    protected Hub findNearestHubForLoc(Location loc) {
        Hub result=null;
        int minima = Integer.MAX_VALUE;
        for(Hub h : hub_list) {
            if(h.getLoc().distSqrd(loc) < minima) {
                minima = h.getLoc().distSqrd(loc);
                result = h;
            }
        }
        return result;
    }

    private static ArrayList<Hub> hub_list = new ArrayList<>();
    private static ArrayList<Highway> hwy_list = new ArrayList<>();
    private static ArrayList<Truck> truck_list = new ArrayList<>();
}
