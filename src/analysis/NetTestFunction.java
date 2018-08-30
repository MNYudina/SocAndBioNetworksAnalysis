package analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.io.PajekNetReader;
import edu.uci.ics.jung.io.PajekNetWriter;

public class NetTestFunction {
	private static Graph<Integer, Integer> initGraph(String filename) {
		long startTime;
		Graph<Integer, Integer> graph = null;

		// LOG.info("Loading graph from {} file.", parameters.getGraphFile());
		startTime = System.nanoTime();
		try {

			graph = loadGraph(filename);
			// LOG.info("Graph successfully loaded in {}.",
			// FormatUtils.durationToHMS(System.nanoTime() - startTime));
		} catch (IOException e) {
			// LOG.error("Failed to load graph from {} file.",
			// parameters.getGraphFile());
			// LOG.debug("Failed to load graph from {} file.",
			// parameters.getGraphFile(), e);
			System.out.println("ddd");
			System.exit(1);
		}
		return graph;
	}

	public static Graph<Integer, Integer> loadGraph(String path) throws IOException {
		return new PajekNetReader<>(createIntegerFactory(), createIntegerFactory()).load(path, new SparseGraph<>());
	}

	private static Factory<Integer> createIntegerFactory() {
		return new Factory<Integer>() {
			private int n = 0;

			@Override
			public Integer create() {
				return n++;
			}
		};
	}

	// --------------------------------------------------
	static int[][] getTettaMatrix(Graph graph, int size) {
		int[][] ret = new int[size][size];
		Collection list = graph.getEdges();
		for (Object edge : list) {
			Pair p = graph.getEndpoints(edge);
			Object n1 = p.getFirst();
			Object n2 = p.getSecond();
			if (graph.degree(n1) > graph.degree(n2)) {
				Object n3 = n1;
				n1 = n2;
				n2 = n3;
			}
			int degree_n1 = graph.degree(n1);
			int degree_n2 = graph.degree(n2);
			if (degree_n1 < size && degree_n2 < size) {
				ret[degree_n1][degree_n2] = ret[degree_n1][degree_n2] + 1;
				ret[degree_n2][degree_n1] = ret[degree_n2][degree_n1] + 1;

			}
		}
		return ret;
	}

	// -----------------------------------------------------------
	static <V> int getMaxDegree(Graph<V, ?> graph) {
		Iterator<V> it = graph.getVertices().iterator();
		int res = 0;
		while (it.hasNext()) {
			V node = it.next();
			int n = graph.degree(node);
			if (res < n)
				res = n;
		}
		return res;
	}

	// -----------------------------------------------------------
	public static int[][] getQMatrix(Graph graph, int size) {
		int[][] ret = new int[size][size];
		Collection<Object> list = graph.getEdges();
		for (Object edge : list) {
			Pair<Object> p = graph.getEndpoints(edge);
			Object n1 = p.getFirst();
			Object n2 = p.getSecond();
			/*
			 * if(graph.degree(n1)>graph.degree(n2)) {Object n3=n1; n1=n2;
			 * n2=n3;}
			 */
			int degree_n1 = graph.degree(n1);
			int degree_n2 = graph.degree(n2);
			if (degree_n1 < size && degree_n2 < size) {
				ret[degree_n1][degree_n2] = ret[degree_n1][degree_n2] + 1;
				ret[degree_n2][degree_n1] = ret[degree_n2][degree_n1] + 1;

			}
		}
		return ret;
	}

	//-------------------------------------------------------------
	public static int[][] getQMatrixDir(Graph graph, int size) {
		int[][] ret = new int[size][size];
		Collection<Object> list = graph.getEdges();
		for (Object edge : list) {
			//Pair<Object> p = graph.getEndpoints(edge);
			Object n1 = graph.getSource(edge);//p.getFirst();
			Object n2 = graph.getDest(edge);//p.getSecond();
			/*
			 * if(graph.degree(n1)>graph.degree(n2)) {Object n3=n1; n1=n2;
			 * n2=n3;}
			 */
			int degree_n1 = graph.degree(n1);
			int degree_n2 = graph.degree(n2);
			if (degree_n1 < size && degree_n2 < size) {
				ret[degree_n1][degree_n2] = ret[degree_n1][degree_n2] + 1;
				// ret[degree_n2][degree_n1] = ret[degree_n2][degree_n1] + 1;

			}
		}
		return ret;
	}
	// функция для получения массива встречаемости узлов с заданной степенью
	// связности
	public static <V> int[] getNodesDegrees(Graph<V, ?> graph, int length) {
		Iterator<V> it = graph.getVertices().iterator();
		int[] distr = new int[length];
		while (it.hasNext()) {
			V node = it.next();
			int n = graph.degree(node);
			if (n < length)
				distr[n] = distr[n] + 1;
		}
		return distr;
	}

	// функция для получения массива встречаемости узлов с заданной степенью
	// связности
	public static <V> int[] getOutNodesDegrees(Graph<V, ?> graph, int length) {
		Iterator<V> it = graph.getVertices().iterator();
		int[] distr = new int[length];
		while (it.hasNext()) {
			V node = it.next();
			int n = graph.outDegree(node);
			if (n < length)
				distr[n] = distr[n] + 1;
		}
		return distr;
	}

	// функция для получения массива встречаемости узлов с заданной степенью
	// связности
	public static <V> int[] getInNodesDegrees(Graph<V, ?> graph, int length) {
		Iterator<V> it = graph.getVertices().iterator();
		int[] distr = new int[length];
		while (it.hasNext()) {
			V node = it.next();
			int n = graph.inDegree(node);
			if (n < length)
				distr[n] = distr[n] + 1;
		}
		return distr;
	}

	// -----------------------------------------------------------
	public static void mainUndir(String[] args) throws IOException {// SaveQlk
		//Graph graph = graph = initGraph("C://1//PPI_HS_U.net");
		Graph graph = graph = initGraph("graphs\\soc\\!OmGTU_component.net");

		System.out.println("V:" + graph.getVertexCount());
		System.out.println("E:" + graph.getEdgeCount());
		int k_max = getMaxDegree(graph);
		System.out.println("max_k=" + k_max);
		int[] degrees = getOutNodesDegrees(graph, k_max + 1);
		for (int i = 0; i < degrees.length; i++) {
			System.out.println(degrees[i]);

		}
		{
			int[][] mass = getQMatrix(graph, 50);
			File logFile = new File("C:\\1\\Soc\\RSS_Edges.txt");
			FileWriter writeFile = new FileWriter(logFile);
			for (int i = 1; i < mass.length; i++) {
				for (int j = 1; j < mass.length; j++) {
					writeFile.write(String.format("%.8f", mass[i][j] / (double) graph.getEdgeCount()) + " ");
				}
				writeFile.write("\n");
			}
			writeFile.close();
		}

	}

	public static void main(String[] args) throws IOException{
		//Graph graph = graph = initGraph("C://1//Regulon.net");
		//Graph graph = graph = initGraph("C://1//PathwayCommons.net");
		//Graph graph = graph = initGraph("graphs\\soc\\g_plus_obr_new.net");
		Graph<Long,Long> graph=getNetEdgelist("C:\\1\\G+.txt");

		

		System.out.println("V:" + graph.getVertexCount());
		System.out.println("E:" + graph.getEdgeCount());
		int k_max = getMaxDegree(graph);
		System.out.println("max_k=" + k_max);

/*		 System.out.println("-----------------In-------------------------------------");

		{
			int[] degrees = getInNodesDegrees(graph, k_max + 1);

			for (int i = 0; i < degrees.length; i++) {
				System.out.println(degrees[i]);

			}
		}
		System.out.println("-------------------OUT---------------------------------");
		{
			int[] degrees = getOutNodesDegrees(graph, k_max + 1);

			for (int i = 0; i < degrees.length; i++) {
				System.out.println(degrees[i]);

			}
		}
*/
		{
			int[][] mass = getQMatrixDir(graph, 100);
			File logFile = new File("C:\\1\\Soc\\RSS_EdgesDir2.txt");
			FileWriter writeFile = new FileWriter(logFile);
			for (int i = 1; i < mass.length; i++) {
				for (int j = 1; j < mass.length; j++) {
					writeFile.write(String.format("%.8f", mass[i][j] / (double) graph.getEdgeCount()) + " ");
				}
				writeFile.write("\n");
			}
			writeFile.close();
		}

	}
	public static void mainGetAndSave(String[] args) {
		Graph<Long,Long> g=getNetEdgelist("C:\\1\\twitter.txt");
		saveGraph(g, "C:\\1\\Twitter.net");

		
	}
	private static Graph<Long, Long> getNetEdgelist(String fileName) {
		System.out.println(fileName);
		Graph<Long,Long> gr = new DirectedSparseGraph();
		FileReader reader;
		try {
			reader = new FileReader(fileName);
		
		BufferedReader br = new BufferedReader(reader);
		/*for (int i = 0; i <numV; i++) {
			gr.addVertex(new Integer(i));	
		}
*/		String str =null;int e=0;
		while((str=br.readLine())!=null){
			String[] mass = str.split(" ");
			gr.addEdge(new Long(e++), Long.valueOf(mass[0]), Long.valueOf(mass[1]));
		}
					

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Nodes num=" + gr.getVertexCount());
		System.out.println("Edges num=" + gr.getEdgeCount());
		return gr;
		
	}
	public static void saveGraph(Graph<Long, Long> g, String filename) {
		PajekNetWriter<Long, Long> gm = new PajekNetWriter<Long, Long>();
		Transformer<Long, String> vs = new Transformer<Long, String>() {

			@Override
			public String transform(Long arg0) {
				// TODO Auto-generated method stub
				return arg0.toString();
			}

		};
		Transformer<Long, Number> nev = new Transformer<Long, Number>() {

			@Override
			public Number transform(Long arg0) {
				// TODO Auto-generated method stub
				return arg0;
			}

		};

		try {
			gm.save(g, new FileWriter(filename), vs, nev);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
