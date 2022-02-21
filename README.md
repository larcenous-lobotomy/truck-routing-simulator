# Introduction

Consider a logistics company that ships packages across the given mapped network. The problem is to route multiple trucks across this network of stations and hubs from specified source to destination without confict. The company has a number of
Stations distributed all over the map. Packages can be shipped from any station to any other
station. For simplicity, all packages from a given station to the same destination are loaded
onto one or more Trucks. Stations are connected to Hubs, which are the nodes of a Highway
network. Thus a Truck that goes from a source Station to a destination Station is routed as
follows:
- move to a Hub that is close to the source Station
- Navigate from Hub to Hub using Highways to the Hub that is close to the destination
station.
- Move from this Hub to the destination Station

# Constraints
- Trucks move at a certain maximum speed between Hubs and Stations and at a different
maximum speed between Hubs (on a Highway)
- A Highway has a fixed capacity. At any given instant there is a maximum number of
trucks that can be on the Highway. If a truck reaches a Hub, and it needs to get onto to
Highway, it has to wait at the Hub until that Highway has spare capacity
- Hubs have a max capacity. At any given instant, there are a max number of trucks it can
process, and that can be waiting at the Hub
- A truck travelling towards a Hub has to wait (on the road/highway) if that Hub is at full
capacity
- To manage scale, entities like Hub, Highway, Truck (and their derived classes) keep
minimal state information. For example, Truck does not keep track of the full route - just
the source, destination locations, and previous, current and next Hubs.
Different companies manage the hubs and trucks. However, they all conform to some basic
design. To model this, we have the following:

  - Base classes Hub, Truck,Highway (we will not model Station and Road since they can
be implicit)
  - Each company creates its own derived classes of these, and specifically derived classes
of Hub, Highway and Truck.
  - A Network class maintains information about all the elements of the network
  - A given network can have instances of different sub-types of these classes,
corresponding to the different companies involved.

# Instructions
Enter `javac *.java` after opening the command prompt inside the directory containing the code. Then, merely run the executable.

# Input
The file `lab6In.txt` is an example for the input format at the command prompt. 

The first line contains two integers, specifying the animate and display timesteps respectively. The next series of inputs are to initialise hubs, highways and trucks. For each of these types, the input specs are preceded by the number of such objects. The order of inputs followed is hubs > highways > trucks.

Each argument line for hubs contains the space-separated values: x-coordinate, y-coordinate and capacity

Each argument line for highways contains the space-separated values: hub-1 index, hub-2 index, capacity and speed limit

Each argument line for trucks contains the space-separated values: src-x-coordinate, src-y-coordinate, dest-x-coordinate, dest-y-coordinate and start time

# Output
The program upon execution produces a window (using Swing) that provides a convenient visualisation of the routing. The timesteps and other arguments require tweaking for a stable simulation, taking into account the sceen size as well as human perception of speed.

# Requirements
The program runs correctly with the OpenJDK version 8 JRE.
