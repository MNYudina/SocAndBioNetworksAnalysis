package games;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections15.Buffer;
import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.buffer.UnboundedFifoBuffer;


import edu.uci.ics.jung.algorithms.generators.random.BarabasiAlbertGenerator;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.io.PajekNetReader;

public class CoordGame {
	static Set<MyNode> freeSet = null;
	static Set<MyNode> S = new HashSet();
	// static int resS = 0;

	static Factory<Graph<MyNode, Integer>> graphFactory = new Factory<Graph<MyNode, Integer>>() {
		private int n = 0;

		public Graph<MyNode, Integer> create() {
			return new UndirectedSparseMultigraph<MyNode, Integer>();
		}
	};
	public static Factory<Integer> edgeFactory = new Factory<Integer>() {
		int n = 0;

		public Integer create() {
			return n++;
		}
	};
	private static Factory<MyNode> vertexFactory = new Factory<MyNode>() {
		long l = 0l;

		public MyNode create() {
			l = l + 1l;
			return new MyNode("" + l);
		}
	};

	public static void main2(String[] args) throws IOException {
		Graph<MyNode, Integer> graph = loadGraph("C:\\1.txt");
		System.out.println(" чилов узлов " + graph.getVertexCount()
				+ " чилов ребер " + graph.getEdgeCount());
		// Set<Set<MyNode>> set = getClusters(graph);
		// System.out.println(set.size());
		/*
		 * for (Set<MyNode> set2 : set) { System.out.println("размер: "
		 * +set2.size());
		 * 
		 * }
		 */

	}

	public static void main(String[] args) throws IOException {
		// генерация графа
		/*
		 * Graph<MyNode, Integer> gr = new UndirectedSparseGraph<MyNode,
		 * Integer>(); BarabasiAlbertGenerator<MyNode, Integer> gen = new
		 * BarabasiAlbertGenerator<MyNode, Integer>(graphFactory, vertexFactory,
		 * edgeFactory, 4, 2, 1, new HashSet()); gen.evolveGraph(1000);
		 * Graph<MyNode, Integer> graph = gen.create();
		 */

		// загрузка графа
		Graph<MyNode, Integer> graph = loadGraph("C:\\1.txt");
		System.out.println("  узлов " + graph.getVertexCount()
				+ " чиcло ребер " + graph.getEdgeCount());

		long t1 = System.currentTimeMillis();
		// S - пустое множество решений
		freeSet = new HashSet(graph.getVertices());
		// Set<MyNode> S = new HashSet();
		// добавляем первый узел (на данном этапе все узлы равны)
		// MyNode o = graph.getVertices().iterator().next();
		// S.add(o);
		// freeSet.remove(o);
		// resS = 1;// он никого не убедит, если граф связный и сила убеждения
		// 1/2,
		// иначе для него нужно делать отдельный прогон

		// выбираем вершину и бросаем ее в рекурсию, пока не начнут заражаться
		// все узлы
		// Set<MyNode> S_=null;
		int j = 0;
		do {

			j++;
			// S_=new HashSet(S);
			// получаем максимальную вершину, параллельно меняется resS
			MyNode v = getmaxSigmaVertex(graph);

			// добавляем все что можем заразить в множество S и удаляем из
			// freeSet
			v.setA(true);
			S.add(v);
			freeSet.remove(v);
			List<MyNode> L = new ArrayList<MyNode>(graph.getNeighbors(v));

			/*System.out.println("------L-------------");

			for (MyNode myNode : graph.getNeighbors(v)) {
				System.out.println("*" + myNode.hashCode() + " " + myNode
						+ "  " + myNode.isA());
			}
			System.out.println("проверка на совпадения");
			for (MyNode myNode : graph.getNeighbors(v)) {
				for (MyNode myNode2 : graph.getVertices()) {
					if (myNode == myNode2) {
						System.out.println("совпало " + myNode);
					}
				}
			}

			System.out.println("--S--");
			for (MyNode myNode2 : graph.getVertices()) {
				for (MyNode myNode : S) {
					if (myNode == myNode2)
						;// System.out.println("*" + myNode.hashCode() + " " +
							// myNode + "  " + myNode.isA());
				}
			}*/
			// ---------------------------------------

			List<MyNode> L2 = new ArrayList<MyNode>();

			while (L.size() > 0) {
				L2 = new ArrayList<MyNode>();

				for (MyNode n : L) {
					if (n.isA() == false) { // если сосед не заражен, то его
											// можно
						// заразить
						Collection<MyNode> col = new ArrayList(
								graph.getNeighbors(n));
						int countA = 0;
						for (MyNode nn : col)
							if (nn.isA())
								countA++;
						if ((double) countA / (double) graph.degree(n) > 0.5) {
							L2.add(n);
							n.setA(true);
							S.add(n);
							freeSet.remove(n);

						}
					}
				}
				L.clear();

				for (MyNode myNode : L2) {
					L.addAll(graph.getNeighbors(myNode));
				}
				// L = L2;

			}
			
			// if(j%100==0)
			System.out.println(j + " шаг" + "  s= " + S);
			// ---------------------------------
		/*	System.out.println("--------G-------------");

			for (MyNode myNode : graph.getVertices()) {
				System.out.println("" + myNode + "  " + myNode.isA());
			}*/

			
						
		} while (S.size() < graph.getVertexCount()); // пока все не добавятся в
														// имитации
		System.out.println("время затрачено:"+ (System.currentTimeMillis() - t1));
	}

	private static MyNode getmaxSigmaVertex(Graph<MyNode, Integer> graph) {
		// получаю профит при S
		// int prof=resS;//getProfit(graph,S);

		// получаю профит при разных узлах
		MyNode tecV = null;
		int max_d = 0;
		int resS_t = 0;
		for (MyNode myNode : freeSet) {
			int nResS = propogate(graph, myNode) + 1;

			if (nResS >= resS_t) {
				int deg = graph.degree(myNode);
				if ((nResS >= resS_t) || (deg > max_d)) {
					tecV = myNode;
					resS_t = nResS;
					max_d = deg;
				}
			}
		}
		// resS = resS_t;

		return tecV;
	}

	// возвращает количество зараженных узлов
	private static int propogate(Graph<MyNode, Integer> graph, MyNode v) {
		// Занеси в L всех соседей новой вершины v
		// цикл пока L.size()>0
		// Просмотри всех соседей из списка L
		// Если удалось их завербовать, то
		// помести их в L2
		// увеличь счетчик для данной вершины
		// L=L2
		// конец цикла
		// вернуть значение счетчика+1, поскольку заразается сама вершина

		List<MyNode> tempS = new ArrayList<MyNode>();
		v.setA(true);
		tempS.add(v);
		int count = 0;
		Collection<MyNode> L = new ArrayList<MyNode>(graph.getNeighbors(v));
		List<MyNode> L2 = new ArrayList<MyNode>();
		while (L.size() > 0) {
			L2 = new ArrayList<MyNode>();

			for (MyNode n : L) {
				if (n.isA() == false) { // если сосед не заражен, то его можно
					// заразить
					final Collection<MyNode> col = new ArrayList<MyNode>(
							graph.getNeighbors(n));
					int countA = 0;
					for (MyNode nn : col)
						if (nn.isA() == true)
							countA++;
					if ((double) countA / (double) graph.degree(n) > 0.5) {
						L2.add(n);
						n.setA(true);
						tempS.add(n);
						count++;
					}
				}
			}
			L.clear();
			for (MyNode myNode : L2) {
				L.addAll(graph.getNeighbors(myNode));
			}
		}
		// удалить из а
		for (MyNode myNode : tempS) {
			myNode.setA(false);

		}

		return count;
	}

	/*
	 * public static Set<Set<MyNode>> getClusters(Graph<MyNode, Integer> graph)
	 * {
	 * 
	 * Set<Set<MyNode>> clusterSet = new HashSet<Set<MyNode>>();
	 * 
	 * HashSet<MyNode> unvisitedVertices = new
	 * HashSet<MyNode>(graph.getVertices());
	 * 
	 * while (!unvisitedVertices.isEmpty()) { Set<MyNode> cluster = new
	 * HashSet<MyNode>(); MyNode root = unvisitedVertices.iterator().next();
	 * unvisitedVertices.remove(root); cluster.add(root);
	 * 
	 * Buffer<MyNode> queue = new UnboundedFifoBuffer<MyNode>();
	 * queue.add(root);
	 * 
	 * while (!queue.isEmpty()) { MyNode currentVertex = queue.remove();
	 * Collection<MyNode> neighbors = graph.getNeighbors(currentVertex);
	 * 
	 * for (MyNode neighbor : neighbors) { if
	 * (unvisitedVertices.contains(neighbor)) { queue.add(neighbor);
	 * unvisitedVertices.remove(neighbor); cluster.add(neighbor); } } }
	 * clusterSet.add(cluster); } return clusterSet; }
	 */

	static Graph<MyNode, Integer> loadGraph(String str) throws IOException {
		Graph<MyNode, Integer> gr = new UndirectedSparseMultigraph<MyNode, Integer>();
		Graph<MyNode, Integer> gr2 = new UndirectedSparseMultigraph<MyNode, Integer>();
		Graph<MyNode, Integer> gr3 = new UndirectedSparseMultigraph<MyNode, Integer>();

		Factory<MyNode> vertexFactory = new Factory<MyNode>() {
            int n = 0;
            public MyNode create() { return new MyNode(""+n++); }
        };
        Factory<Integer> edgeFactory = new Factory<Integer>()  {
            int n = 0;
            public Integer create() { return n++; }
        };

        PajekNetReader<Graph<MyNode, Integer>, MyNode,Integer> pnr = 
            new PajekNetReader<Graph<MyNode, Integer>, MyNode,Integer>(vertexFactory, edgeFactory);
        
       // final Graph<Number,Number> graph = new SparseMultigraph<Number, Number>();
        BufferedReader br2 = new BufferedReader(new FileReader("C:\\1p.net"));

        pnr.load(br2, gr3);
		System.out.println(gr3);

		
		MyNode n1= new MyNode("1");
		MyNode n2= new MyNode("2");
		MyNode n3= new MyNode("3");
		MyNode n4= new MyNode("4");
		MyNode n5= new MyNode("5");
		MyNode n6= new MyNode("6");
		MyNode n7= new MyNode("7");
		MyNode n8= new MyNode("8");
		MyNode n9= new MyNode("9");
		MyNode n10= new MyNode("10");

		gr2.addEdge(1, n1, n2);
		gr2.addEdge(2, n2, n3);
		gr2.addEdge(3, n2, n4);
		gr2.addEdge(4, n3, n5);
		gr2.addEdge(5, n4, n5);
		gr2.addEdge(6, n5, n6);
		gr2.addEdge(7, n4, n10);
		gr2.addEdge(8, n4, n7);
		gr2.addEdge(9, n10, n6);
		gr2.addEdge(10, n10, n7);
		gr2.addEdge(11, n6, n8);
		gr2.addEdge(12, n7, n9);
		gr2.addEdge(13, n8, n9);

		System.out.println(gr2);
		
		// System.out.println("go");
		BufferedReader br = null;

		try {

			String sCurrentLine;
			br = new BufferedReader(new FileReader(str));
			// BufferedWriter bw = new BufferedWriter(new FileWriter(str));
			System.out.println(str);
			int i = 1;
			while ((sCurrentLine = br.readLine()) != null) {
				String[] strMass = sCurrentLine.split(";");
				// System.out.println(strMass[1]+"#"+strMass[2]);
				// gr.addEdge(new Integer(i++),strMass[1], strMass[3]);
				if (strMass.length == 2) {
					gr.addEdge(new Integer(i++), new MyNode(strMass[0]),
							new MyNode(strMass[1]));
					// bw.write(strMass[0] + " " + strMass[1]);
					// bw.newLine();

				}

				else
					gr.addVertex(new MyNode(strMass[0]));
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		System.out.println(gr);

		return gr3;
	}

}
