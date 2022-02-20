package demo19083;

import base.*;

import java.util.*;

public class HubDemo extends Hub {

	public HubDemo(Location loc) {
		super(loc);
		hubArrayList.add(this);
	}

	@Override
	public synchronized void add(Highway hwy) {
		if(!this.getHighways().contains(hwy)){
			super.add(hwy);
			if(!highwayArrayList.contains(hwy)) highwayArrayList.add(hwy);
			initGraph();
		}
	}

	@Override
	public synchronized boolean add(Truck truck) {
		//if (Q is not Full) {add; return True;}
		if(truckQueue.size() >= getCapacity()) return false;
		truckQueue.add(truck);
		return true;
	}

	@Override
	public void remove(Truck truck) {
		truckQueue.remove(truck);
	}

	public static boolean isAdjacent(Hub hub1, Hub hub2) {
		for(Highway hwy : highwayArrayList) {
			if(hwy.getStart() == hub1 && hwy.getEnd() == hub2) return true;
		}
		return false;
	}

	private static Highway getConnector(Hub hub1, Hub hub2) {
		for(Highway hwy : highwayArrayList) {
			if(hwy.getStart() == hub1 && hwy.getEnd() == hub2) return hwy;
		}
		return null;
	}

	//next node in the shortest path from src to dest after src
	@Override
	public Highway getNextHighway(Hub from, Hub dest) {
		int index1=hubArrayList.indexOf(from),index2=hubArrayList.indexOf(dest);
		int next_index = AllPairsSP.constructPath(index1,index2).get(1);
		return getConnector(hubArrayList.get(index1),hubArrayList.get(next_index));
	}

	@Override
	protected synchronized void processQ(int deltaT) {
		//process truckQueue - if last hub before station, send it towards station
		for(Truck truck : truckQueue) {
			//final hub before station
			if(this == Network.getNearestHub(truck.getDest())) {
				this.remove(truck);
				continue;
			}
			//intermediate hub - if entry to highway is successful
			truck.enter(this.getNextHighway(this,Network.getNearestHub(truck.getDest())));
			if(truck.getLastHub() == this) this	.remove(truck);
		}
	}

	public static void initGraph() {
		int V = hubArrayList.size();
		int [][] Graph = new int[V][V];
		for(int i=0; i<V; i++){
			for (int j = 0; j <V; j++) {
				if(i==j) Graph[i][j] = 0;
				else if(HubDemo.isAdjacent(hubArrayList.get(i),hubArrayList.get(j)))
					Graph[i][j] = (getConnector(hubArrayList.get(i),hubArrayList.get(j))).getEnd().getLoc().distSqrd((getConnector(hubArrayList.get(i),hubArrayList.get(j))).getStart().getLoc());
				else Graph[i][j] = INF;
			}
		}
		GraphObj = new AllPairsSP(V,Graph);
	}

	private ArrayList<Truck> truckQueue = new ArrayList<>();
	private static int INF = (int) 1e7;
	private static AllPairsSP GraphObj;
	private static ArrayList<Hub> hubArrayList = new ArrayList<>();
	private static ArrayList<Highway> highwayArrayList = new ArrayList<>();
}

class AllPairsSP {

	static final int MAXN = 1000;

	// Infinite value for array
	static int INF = (int) 1e7;

	static int[][] dis;
	static int[][] Next;
	static int V;

	AllPairsSP(int V, int[][] graph) {
		AllPairsSP.V = V;
		dis = new int[V][V];
		Next = new int[V][V];
		AllPairsSP.initialise(V,graph);
		AllPairsSP.floydWarshall(V);
	}

	// Initializing the distance and
// Next array
	static void initialise(int V,
						   int[][] graph) {
		for (int i = 0; i < V; i++) {
			for (int j = 0; j < V; j++) {
				dis[i][j] = graph[i][j];

				// No edge between node
				// i and j
				if (graph[i][j] == INF)
					Next[i][j] = -1;
				else
					Next[i][j] = j;
			}
		}
	}

	static Vector<Integer> constructPath(int u, int v) {

		// If there's no path between
		// node u and v, simply return
		// an empty array
		if (Next[u][v] == -1)
			return null;

		// Storing the path in a vector
		Vector<Integer> path = new Vector<Integer>();
		path.add(u);

		while (u != v) {
			u = Next[u][v];
			path.add(u);
		}
		return path;
	}

	static void floydWarshall(int V) {
		for (int k = 0; k < V; k++) {
			for (int i = 0; i < V; i++) {
				for (int j = 0; j < V; j++) {

					// We cannot travel through
					// edge that doesn't exist
					if (dis[i][k] == INF ||
							dis[k][j] == INF)
						continue;

					if (dis[i][j] > dis[i][k] +
							dis[k][j]) {
						dis[i][j] = dis[i][k] +
								dis[k][j];
						Next[i][j] = Next[i][k];
					}
				}
			}
		}
	}
}
