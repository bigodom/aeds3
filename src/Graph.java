import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Graph {

    private int countNodes;
    private int countEdges;
    private int[][] adjMatrix;

    public Graph(int countNodes){
        this.countNodes = countNodes;
        this.adjMatrix = new int[countNodes][countNodes];
    }  

    public Graph(String fileName) throws IOException {
        File file = new File(fileName);
        FileReader reader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(reader);

        // Read header
        String[] line = bufferedReader.readLine().split(" ");
        this.countNodes = (Integer.parseInt(line[0]));
        int fileLines = (Integer.parseInt(line[1]));
        // Create and fill adjMatrix with read edges
        this.adjMatrix = new int[this.countNodes][this.countNodes];
        for (int i = 0; i < fileLines; ++i) {
            String[] edgeInfo = bufferedReader.readLine().split(" ");
            int source = Integer.parseInt(edgeInfo[0]);
            int sink = Integer.parseInt(edgeInfo[1]);
            int weight = Integer.parseInt(edgeInfo[2]);
            addEdge(source, sink, weight);
        }
        bufferedReader.close();
        reader.close();
    }

    /**
     * @return int return the countNodes
     */
    public int getCountNodes() {
        return countNodes;
    }

    /**
     * @param countNodes the countNodes to set
     */
    public void setCountNodes(int countNodes) {
        this.countNodes = countNodes;
    }

    /**
     * @return int return the countEdges
     */
    public int getCountEdges() {
        return countEdges;
    }

    /**
     * @param countEdges the countEdges to set
     */
    public void setCountEdges(int countEdges) {
        this.countEdges = countEdges;
    }

    /**
     * @return int[][] return the adjMatrix
     */
    public int[][] getAdjMatrix() {
        return adjMatrix;
    }

    /**
     * @param adjMatrix the adjMatrix to set
     */
    public void setAdjMatrix(int[][] adjMatrix) {
        this.adjMatrix = adjMatrix;
    }

    public String toString(){
        String str = "";
        for(int i = 0; i<this.adjMatrix.length; ++i){
            for (int j = 0; j < adjMatrix.length; ++j) {
                str += this.adjMatrix[i][j] + "\t";
            }
            str += "\n";
        }
        return str;
    }
    public void addEdge(int source, int sink, int weight) {
        if(source < 0 || source > this.countNodes - 1 
        || sink < 0 || sink > this.countNodes - 1 
        || weight <= 0 ){
        System.err.println("invalid edge: " + source + " " + sink + " " + weight);
            return;
        }
        this.adjMatrix[source][sink] = weight;
        this.countEdges++;

    }

    public void addEdgeUnoriented(int u, int v, int w) {
        if(u < 0 || u > this.countNodes - 1 
        || v < 0 || v > this.countNodes - 1 
        || w <= 0 ){
        System.err.println("invalid edge: " + u + " " + v + " " + w);
            return;
        }
        this.adjMatrix[u][v] = w;
        this.adjMatrix[v][u] = w;
        this.countEdges += 2;

    }

    public int degree(int node){
        if(node < 0 || node > this.countNodes - 1){
            System.err.println("invalid node: " + node);
        }
        int count = 0;
        for (int i = 0; i < adjMatrix[node].length; i++) {
            if(adjMatrix[node][i] != 0){
                count++;
            }
        }
        return count;
    }

    public int highestDegree(){
        int hd=0;
        for (int i = 0; i < adjMatrix.length; i++) {
            int degreeNodeI= this.degree(i);
            if(degreeNodeI>hd){
                hd=degreeNodeI;
            }
        }
        return hd;
    }
    
    public int lowestDegree(){

        int ld=0;

        for (int i = 0; i < adjMatrix.length; i++) {
            int degreeNodeI= this.degree(i);
            if(i==0){
                ld=degreeNodeI;
            }
            if(degreeNodeI<ld){
                ld=degreeNodeI;
            }

        }
        return ld;
    }
    public Graph complement(){
        Graph aux= new Graph(this.countNodes);
        for (int i = 0; i < adjMatrix.length; i++) {

            for (int j = 0; j < adjMatrix.length ; j++) {
                if(i!=j && this.adjMatrix[i][j]==0){
                    aux.addEdge(i,j,1);
                }

            }

        }
        return aux;

    }
    public float density() {
        return (float) this.countEdges / (this.countNodes * (this.countNodes - 1));
      }

    public boolean subGraph(Graph g2) {
        if (g2.countNodes > this.countNodes || g2.countEdges > this.countEdges)
          return false;
        for(int i = 0; i < g2.adjMatrix.length; ++i) {
          for(int j = 0; j < g2.adjMatrix[i].length; ++j) {
            if(g2.adjMatrix[i][j] != 0 && this.adjMatrix[i][j] == 0)
              return false;
          }
        }
        return true;
      }
    

    public ArrayList<Integer> bfs(int s){
        int[] desc = new int[this.countNodes];
        ArrayList<Integer> Q = new ArrayList<>();
        Q.add(s);
        ArrayList<Integer> R = new ArrayList<>();
        R.add(s);
        desc[s] = 1;
        //Main loop

        while(Q.size() > 0) {
            int u = Q.remove(0);

            for(int v = 0; v < this.adjMatrix[u].length; ++v){
                if(this.adjMatrix[u][v] != 0){
                    if(desc[v] == 0){
                        Q.add(v);
                        R.add(v);
                        desc[v] = 1;
                    }
                }
            }
        }

        return R;
    }
    public boolean connected() {
        return this.bfs(0).size() == this.countNodes;
    }

    public ArrayList<Integer> dfs(int s){
        int[] desc = new int[this.countNodes];
        ArrayList<Integer> S = new ArrayList<>();
        S.add(s);
        ArrayList<Integer> R = new ArrayList<>();
        R.add(s);
        desc[s] = 1;
        //Main loop

        while(S.size() > 0) {
            int u = S.get(S.size()-1);
            boolean unstack = true;
            for(int v = 0; v < this.adjMatrix[u].length; ++v){
                if(this.adjMatrix[u][v] != 0 && desc[v] == 0){
                    S.add(v);
                    R.add(v);
                    desc[v] = 1;
                    unstack = false;
                    break;
                }
            }if(unstack) {
                S.remove(S.size() -1);
            }
        }

        return R;
    }

    public boolean nonOriented(){
        for (int i = 0; i < adjMatrix.length; i++) {
            for (int j = i+1; j < adjMatrix.length; j++) {
                if(this.adjMatrix[i][j] != this.adjMatrix[j][i]){
                    return false;
                }
            }
        }
        return true;
    }

    public ArrayList<Integer> dfsRec(int s) {
        int[] desc = new int[this.countNodes];
        ArrayList<Integer> R = new ArrayList<>();
        dfsRecAux(s, desc, R);
        return R;
      }
    public void dfsRecAux(int u, int[] desc, ArrayList<Integer> R) {
        desc[u] = 1;
        R.add(u);
        for (int v = 0; v < this.adjMatrix[u].length; ++v) {
          if (this.adjMatrix[u][v] != 0 && desc[v] == 0) {
            dfsRecAux(v, desc, R);
          }
        }
      }

}
