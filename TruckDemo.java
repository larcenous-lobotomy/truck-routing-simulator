package demo19083;

import base.*;

class TruckDemo extends Truck {

	TruckDemo() {
		count += 1;
		index = count;
	}

	@Override
	public String getTruckName() {

		return "Truck" + 19083 + index;
	}

	@Override
	protected synchronized void update(int deltaT)
	{
		currentTime += deltaT; // increment the time
		//if before start time
		if (currentTime < this.getStartTime()) return;
		//if reached
		if(this.sameSpot(this.getLoc(), this.getDest())) return;

		if(!started) {
			this.setLoc(Network.getNearestHub(this.getSource()).getLoc());
			this.handleHubAdd();
		}
		else if(highway != null && !inHub) {
			int v = highway.getMaxSpeed();
			double DELTA = 1D*(double)v*(double)deltaT;
			Location startloc=this.getLoc(),endloc=highway.getEnd().getLoc();
			//2 cases - tan is finite and non-finite
			if(startloc.getX() == endloc.getX()) {
				//reached
				if(startloc.getY() == endloc.getY()) {
					this.handleHubAdd();
				}

				//tan -> INF
				else {
					if(DELTA < Math.abs(endloc.getY() - startloc.getY())) {
						int SIGN = (endloc.getY() > startloc.getY()) ? 1 : -1;
						this.setLoc(new Location(startloc.getX(),startloc.getY() + SIGN*(int)Math.round(DELTA)));
					}
					else this.handleHubAdd();
				}
			}
			else {
				double angle = Math.atan(Math.abs((endloc.getY() - startloc.getY()) / (double) (endloc.getX() - startloc.getX())));
				double sin = Math.sin(angle), cos = Math.cos(angle);
				double SIN = DELTA * sin, COS = DELTA * cos;

				if(startloc.distSqrd(endloc) < DELTA*DELTA) {
					this.setLoc(endloc);
					this.handleHubAdd();
					return;
				}

				SIN = (SIN < 1) ? 1 : SIN;
				SIN = (startloc.getY() < endloc.getY()) ? 1 : -1;
				COS = (COS < 1) ? 1 : COS;
				COS = (startloc.getX() < endloc.getX()) ? 1 : -1;

				this.setLoc(new Location(startloc.getX() + (int)Math.round(COS),startloc.getY() + (int)Math.round(SIN)));
			}
		}
		else if(inHub && this.sameSpot(this.getLoc(),Network.getNearestHub(this.getDest()).getLoc())){
			this.setLoc(this.getDest());
			highway = null;
			inHub = false;
		}
	}

	@Override
	public synchronized void enter(Highway hwy) {
		if(hwy.add(this))highway = hwy;
		flag = true;
		inHub = false;
	}

	@Override
	public Hub getLastHub() {
		if(highway!=null) return highway.getStart();
			//if on source-network link
		else if(!flag) return null;
			//from last hub to dest
		else return Network.getNearestHub(this.getDest());
	}

	private boolean sameSpot(Location l1, Location l2) {
		if((l1.getY() == l2.getY()) && (l1.getX() == l2.getX())){
			return true;
		}
		return false;
	}

	private void handleHubAdd() {
		//desthub
		if(this.sameSpot(Network.getNearestHub(this.getDest()).getLoc(),this.getLoc())) {
			if(Network.getNearestHub(this.getDest()).add(this)) {
				highway.remove(this);
				this.setLoc(this.getDest());
				highway = null;
				inHub = false;
			}
		}
		//srchub
		else if(this.sameSpot(Network.getNearestHub(this.getSource()).getLoc(),this.getLoc())) {
			this.setLoc(Network.getNearestHub(this.getSource()).getLoc());
			if(Network.getNearestHub(this.getSource()).add(this)) {
				inHub = true;
				started = true;
			}
		}
		//hub within network
		else{
			if(highway.getEnd().add(this)) {
				highway.remove(this);
				this.setLoc(highway.getEnd().getLoc());
				inHub = true;
			}
		}
	}

	static int count = 0;
	private int index;
	private int currentTime = 0;
	private Highway highway = null;
	private boolean flag = false,inHub = false,started=false;
}
