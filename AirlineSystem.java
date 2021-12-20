import java.util.*;
import java.io.*;

final public class AirlineSystem implements AirlineInterface {
  public String [] cityNames = null;
  public Digraph G = null;
  public static final int INFINITY = Integer.MAX_VALUE;
  public Set<String> hSet = null;



  public boolean loadRoutes(String fileName){
     try{

      Scanner scan = new Scanner(new FileInputStream(fileName));
      int v = Integer.parseInt(scan.nextLine());
      G = new Digraph(v);
      cityNames = new String[v];
      for(int i=0; i<v; i++) cityNames[i] = scan.nextLine();
        hSet = new HashSet<String>();
      for(int i=0; i<cityNames.length; i++) hSet.add(cityNames[i]);

      while(scan.hasNext())
      {
        int from = scan.nextInt();
        int to = scan.nextInt();
        int weight = scan.nextInt();
        double money = scan.nextDouble();
        G.addEdge(new Route(cityNames[from-1], cityNames[to-1], weight, money));
        G.addEdge(new Route(cityNames[to-1], cityNames[from-1], weight, money));
        try{
        scan.nextLine();
        }
        catch(NoSuchElementException ex){
        }
      }
    scan.close();

    }
  catch(IOException ex){
    return false;
  }
    return true;
  }







  public boolean saveRoutes(String fileName){
    return true;
  }





  public Set<String> retrieveCityNames(){
    return hSet;
  }







  public Set<Route> retrieveDirectRoutesFrom(String city)
    throws CityNotFoundException {

      Set<Route> rSet = new HashSet<Route>();
      for (int i = 0; i < G.v; i++) {
        for (Route e : G.adj(i)) {
          if(e.source.equals(city)) rSet.add(e);
        }
      }
    return rSet;
  }











  public Set<ArrayList<Route>> cheapestItinerary(String source,
    String destination) throws CityNotFoundException{
    Set<ArrayList<Route>> ret = new HashSet<ArrayList<Route>>();
    ArrayList<Route> lis = new ArrayList<Route>();

    int s = Arrays.asList(cityNames).indexOf(source);
    int d = Arrays.asList(cityNames).indexOf(destination);
    G.dijkstras(s, d);
    if(!G.marked[d]) return ret;
    else{
      Stack<Integer> path = new Stack<>();
      for (int x = d; x != s; x = G.edgeTo[x])path.push(x);

      int prevVertex = s;
      while(!path.empty())
      {
        int v = path.pop();
        lis.add(new Route(cityNames[prevVertex], cityNames[v], G.distTo[v] - G.distTo[prevVertex] , G.moneys[v] - G.moneys[prevVertex]));
        prevVertex = v;
      }
      ret.add(lis);
      return ret;
    }//end else
  }












  public Set<ArrayList<Route>> cheapestItinerary(String source,
    String transit, String destination) throws CityNotFoundException{
      
    Set<ArrayList<Route>> ret = new HashSet<ArrayList<Route>>();
    ArrayList<Route> lis = new ArrayList<Route>();

    int s = Arrays.asList(cityNames).indexOf(source);
    int d = Arrays.asList(cityNames).indexOf(transit);
    G.dijkstras(s, d);
    if(!G.marked[d]) return ret;
    else{
      Stack<Integer> path = new Stack<>();
      for (int x = d; x != s; x = G.edgeTo[x])  path.push(x);

      int prevVertex = s;
      while(!path.empty())
      {
        int v = path.pop();
        lis.add(new Route(cityNames[prevVertex], cityNames[v], G.distTo[v] - G.distTo[prevVertex] , G.moneys[v] - G.moneys[prevVertex]));
        prevVertex = v;
      }
    }//end else



    s = Arrays.asList(cityNames).indexOf(transit);
    d = Arrays.asList(cityNames).indexOf(destination);
    G.dijkstras(s, d);
    if(!G.marked[d]) return ret;
    else{
      Stack<Integer> path = new Stack<>();
      for (int x = d; x != s; x = G.edgeTo[x])path.push(x);

      int prevVertex = s;
      while(!path.empty())
      {
        int v = path.pop();
        lis.add(new Route(cityNames[prevVertex], cityNames[v], G.distTo[v] - G.distTo[prevVertex] , G.moneys[v] - G.moneys[prevVertex]));
        prevVertex = v;
      }
      ret.add(lis);
      return ret;
    }//end else
    }












  /**
   * finds one Minimum Spanning Tree (MST) for each connected component of
   * the graph
   * @return a (possibly empty) of MSTs. Each MST is a Set<Route>
   * of Route objects representing the MST edges.
   */
  public Set<Set<Route>> getMSTs(){

    for(int i =0; i<cityNames.length; i++){
      G.bfs(i);
      for(int j = 0; j<G.marked.length;j++){
        if(G.marked[j]){
          //add to a set of verticesXXXXX

        }//end
      }//end

      //make sub graph by adding vertices


    }//end 





    //run kreskalls on each sub graph

    return new HashSet<Set<Route>>();
  }











  /**
   * finds all itineraries starting out of a source city and within a given
   * price
   * @param city the String city name
   * @param budget the double budget amount in dollars
   * @return a (possibly empty) Set<ArrayList<Route>> of paths with a total cost
   * less than or equal to the budget. Each path is an ArrayList<Route> of Route
   * objects starting with a Route object out of the source city.
   */
  public Set<ArrayList<Route>> tripsWithin(String city, double budget)
    throws CityNotFoundException {
      return new HashSet<ArrayList<Route>>();
  }

  /**
   * finds all itineraries within a given price regardless of the
   * starting city
   * @param  budget the double budget amount in dollars
   * @return a (possibly empty) Set<ArrayList<Route>> of paths with a total cost
   * less than or equal to the budget. Each path is an ArrayList<Route> of Route
   * objects.
   */
  public Set<ArrayList<Route>> tripsWithin(double budget){
    return new HashSet<ArrayList<Route>>();
  }









  /**
   * delete a given non-stop route from the Airline's schedule. Both directions
   * of the route have to be deleted.
   * @param  source the String source city name
   * @param  destination the String destination city name
   * @return true if the route is deleted successfully and false if no route
   * existed between the two cities
   * @throws CityNotFoundException if any of the two cities are not found in the
   * Airline system
   */
  public boolean deleteRoute(String source, String destination)
    throws CityNotFoundException{


      for (int i = 0; i < G.v; i++) {
        for (Route e : G.adj(i)) {
          if((e.source.equals(source) && e.destination.equals(destination))) e = null;
          }
        }


        for (int i = 0; i < G.v; i++) {
        for (Route e : G.adj(i)) {
          if((e.source.equals(destination) && e.destination.equals(source)))
          {
            e = null
            return true;
          }
          }
        }
      return false;
    }








  /**
   * delete a given city and all non-stop routes out of and into the city from
   * the Airline schedule.
   * @param  city  the String city name
   * @throws CityNotFoundException if the city is not found in the Airline system
   */
  public void deleteCity(String city) throws CityNotFoundException{

    for (int i = 0; i < G.v; i++) {
        for (Route e : G.adj(i)) {
          if((e.source.equals(city) || e.destination.equals(city))) e = null;
          }
        }

        int[] hold = new int[cityNames.length];
        for(int i = 0; i < cityNames.length; i++){
          hold[i] = cityNames[i];
        }


        cityNames = new String[hold.length - 1];

        for(int i = 0; i< hold.length-1; i++){
          if(!hold[i].equals(i)){
            cityNames[i] = hold[i]; 
          }//end if
        }//end for
        hSet.remove(city);

  }

















    private class Digraph {
    private final int v;
    private int e;
    private LinkedList<Route>[] adj;
    private boolean[] marked;  // marked[v] = is there an s-v path
    private int[] edgeTo;      // edgeTo[v] = previous edge on shortest s-v path
    private int[] distTo;      // distTo[v] = number of edges shortest s-v path
    private double[] moneys;


    /**
    * Create an empty digraph with v vertices.
    */
    public Digraph(int v) {
      if (v < 0) throw new RuntimeException("Number of vertices must be nonnegative");
      this.v = v;
      this.e = 0;
      @SuppressWarnings("unchecked")
      LinkedList<Route>[] temp =
      (LinkedList<Route>[]) new LinkedList[v];
      adj = temp;
      for (int i = 0; i < v; i++)
        adj[i] = new LinkedList<Route>();
    }

    /**
    * Add the edge e to this digraph.
    */
    public void addEdge(Route edge) {
      int from = Arrays.asList(cityNames).indexOf(edge.source);
      adj[from].add(edge);
      e++;
    }


    /**
    * Return the edges leaving vertex v as an Iterable.
    * To iterate over the edges leaving vertex v, use foreach notation:
    * <tt>for (WeightedDirectedEdge e : graph.adj(v))</tt>.
    */
    public Iterable<Route> adj(int v) {
      return adj[v];
    }

    public void bfs(int source) {
      marked = new boolean[this.v];
      distTo = new int[this.e];
      edgeTo = new int[this.v];
      moneys = new double[this.e];

      Queue<Integer> q = new LinkedList<Integer>();
      for (int i = 0; i < v; i++){
        distTo[i] = INFINITY;
        moneys[i] = 200000.00;
        marked[i] = false;
      }
      distTo[source] = 0;
      moneys[source] = 0.0;
      marked[source] = true;
      q.add(source);

      while (!q.isEmpty()) {
        int v = q.remove();
        for (Route w : adj(v)) {
          if (!marked[Arrays.asList(cityNames).indexOf(w.destination)]) {
            edgeTo[Arrays.asList(cityNames).indexOf(w.destination)] = v;
            distTo[Arrays.asList(cityNames).indexOf(w.destination)] = distTo[v] + 1;
            moneys[Arrays.asList(cityNames).indexOf(w.destination)] = moneys[v] +1;
            marked[Arrays.asList(cityNames).indexOf(w.destination)] = true;
            q.add(Arrays.asList(cityNames).indexOf(w.destination));
          }
        }
      }
    }

    public void dijkstras(int source, int destination) {
      marked = new boolean[this.v];
      distTo = new int[this.v];
      edgeTo = new int[this.v];
      moneys = new double[this.v];


      for (int i = 0; i < v; i++){
        distTo[i] = INFINITY;
        moneys[i] = 2000000.0;
        marked[i] = false;
      }
      distTo[source] = 0;
      moneys[source] = 0;
      marked[source] = true;
      int nMarked = 1;

      int current = source;
      while (nMarked < this.v) {
        for (Route w : adj(current)) {
          if (moneys[current]+w.price < moneys[Arrays.asList(cityNames).indexOf(w.destination)]) {
        //TODO:update edgeTo and distTo
            edgeTo[Arrays.asList(cityNames).indexOf(w.destination)] = current;
            distTo[Arrays.asList(cityNames).indexOf(w.destination)] = distTo[current]+w.distance;
            moneys[Arrays.asList(cityNames).indexOf(w.destination)] = moneys[current]+w.price;

          }
        }
        //Find the vertex with minimim path distance
        //This can be done more effiently using a priority queue!
        double min = 200000000.0;
        current = -1;

        for(int i=0; i<moneys.length; i++){
          if(marked[i])
            continue;
          if(moneys[i] < min){
            min = moneys[i];
            current = i;
          }
        }

  //TODO: Update marked[] and nMarked. Check for disconnected graph.
          if(current < 0) break;
          else{
          nMarked++;
          marked[current] = true;
          }
          


      }
    }
  }





}
