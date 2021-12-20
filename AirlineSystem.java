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


  public Set<String> retrieveCityNames() {
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






  //Arrays.asList(cityNames).indexOf(w.destination)
  public Set<ArrayList<String>> fewestStopsItinerary(String source,
    String destination) throws CityNotFoundException {
    Set<ArrayList<String>> ret = new HashSet<ArrayList<String>>();
    ArrayList<String> lis = new ArrayList<String>();
    if(G == null) return ret;

    int s = Arrays.asList(cityNames).indexOf(source);
    int d = Arrays.asList(cityNames).indexOf(destination);
    G.bfs(s);

    if(!G.marked[d]) return ret;
    else{
      Stack<Integer> path = new Stack<>();
          for (int x = d; x != s; x = G.edgeTo[x]) path.push(x);
         
          int prevVertex = s;
          lis.add(cityNames[s]);
          while(!path.empty()){
            int v = path.pop();
            lis.add(cityNames[v]);
            prevVertex = v;
          }

      ret.add(lis);
      return ret;
    }//end else
  }

  public Set<ArrayList<Route>> shortestDistanceItinerary(String source,
    String destination) throws CityNotFoundException {
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





  public Set<ArrayList<Route>> shortestDistanceItinerary(String source,
    String transit, String destination) throws CityNotFoundException {

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

  public boolean addCity(String city){
    if(G == null) return false;
    else if(hSet == null) return false;
    else if(hSet.contains(city)) return false;
    else{
      hSet.add(city);
    String[] neu = new String[cityNames.length + 1];
     for(int i = 0; i<cityNames.length; i++) neu[i] = cityNames[i];
      neu[neu.length-1] = city;
      return true;
    }
  }





  public boolean addRoute(String source, String destination, int distance,
    double price) throws CityNotFoundException {
    try{
      for (int i = 0; i < G.v; i++) {
        for (Route e : G.adj(i)) {
          if((e.source.equals(source) && e.destination.equals(destination))) return false;
          }
        }


    for (int i = 0; i < G.v; i++) {
        for (Route e : G.adj(i)) {
          if((e.source.equals(destination) && e.destination.equals(source))) return false;
          }
        }

    G.addEdge(new Route(source, destination, distance, price));
    G.addEdge(new Route(destination, source, distance, price));
    return true;
  }
  catch(NullPointerException ex){ 
    return false;
  }
  }








  public boolean updateRoute(String source, String destination, int distance,
    double price) throws CityNotFoundException {


    for (int i = 0; i < G.v; i++) {
        for (Route e : G.adj(i)) {
          if(e.source.equals(source) && e.destination.equals(destination))
          {
            e.source = source;
            e.destination = destination;
            e.distance = distance;
            e.price = price;


            for (int k = 0; k < G.v; k++) {
             for (Route r : G.adj(k)) {

              if(r.source.equals(destination) && r.destination.equals(source))
          {
            r.source = destination;
            r.destination = source;
            r.distance = distance;
            r.price = price;
          }
              }
            }

            return true;
          }
        }
      }
      return false;

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
          if (distTo[current]+w.distance < distTo[Arrays.asList(cityNames).indexOf(w.destination)]) {
        //TODO:update edgeTo and distTo
            edgeTo[Arrays.asList(cityNames).indexOf(w.destination)] = current;
            distTo[Arrays.asList(cityNames).indexOf(w.destination)] = distTo[current]+w.distance;
            moneys[Arrays.asList(cityNames).indexOf(w.destination)] = moneys[current]+w.price;

          }
        }
        //Find the vertex with minimim path distance
        //This can be done more effiently using a priority queue!
        int min = INFINITY;
        current = -1;

        for(int i=0; i<distTo.length; i++){
          if(marked[i])
            continue;
          if(distTo[i] < min){
            min = distTo[i];
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
