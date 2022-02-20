package demo19083;

import base.Highway;
import base.Truck;

import java.util.ArrayList;

class HighwayDemo extends Highway {

	public HighwayDemo() {
		
	}

	@Override
	public synchronized boolean hasCapacity() {
		if(trucks.size() < this.getCapacity()) return true;
		return false;
	}

	@Override
	public synchronized boolean add(Truck truck) {
		if(this.hasCapacity()) {
			trucks.add(truck);
			return true;
		}
		return false;
	}

	@Override
	public synchronized void remove(Truck truck) {
		trucks.remove(truck);
	}

	private ArrayList<Truck> trucks = new ArrayList<>();
}
