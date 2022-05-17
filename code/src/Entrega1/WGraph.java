package Entrega1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class WGraph implements Serializable, WGraphInt {
	private static final long serialVersionUID=1L;
	private int dim;
	private double [][] madj;
	
	
//	Construtor da classe, cria um WGraph com n vértices, inicialmente sem pesos entre os diferentes vértices;
	public WGraph (int n) {
		dim = n;
		madj = new double [dim] [dim];
		for(int i=0 ;i<dim;i++)
			Arrays.fill(madj[i], Double.NaN);
	}
	
//	Recebe dois nós (i,j) e um peso (p) e adiciona ao grafo uma aresta entre os nós com o respectivo peso;
	public void add_edge (int i, int j, double p) {
		madj [i][j]=p;
		madj [j][i]=p;
	}
	
//	Recebe dois nís (i,j) e retira ao grafo a aresta entre os dois nós;
	public void remove_edge (int i, int j) {
		madj [i][j]=Double.NaN;
		madj [j][i]=Double.NaN;
	}
	
// 	Método que gera a árvore geradora maximal, em que o nó n é a raíz;
	public DGraph MST (int n) {
		DGraph graph = new DGraph (dim);
		ArrayList<Integer> parents = new ArrayList<Integer>(dim); //Lista em que cada entrada está o nó que é pai do nó numerado com a posição em questão;
		for (int i=0;i<dim; i++)									
			parents.add(-1);									  //Inicializamos a -1 porque os nós são numerados a partir de 0 e no início ainda não há nós pais;
			
		
		ArrayList<Double> keys = new ArrayList<Double>(dim); // Lista em que cada entrada está a chave de prioridade do nó numerado com a posição em questão;
		for (int i=0;i<dim; i++)
			keys.add(Double.NEGATIVE_INFINITY);				 // Inicializamos a -infinito porque é o mínimo possível e qualquer inteiro terá prioridade sobre este;
		
		keys.add(n,0.0); //Tornamos a chave da raíz 0 para ser a prioritária inicialmente
		
		ArrayList<Integer> kuwait = new ArrayList<Integer> (dim); //kuwait dá-nos a lista de nós que ainda não foram adicionados à árvore;
		for(int i=0 ; i<dim; i++)
			kuwait.add(i);

		while (!kuwait.isEmpty()) { 				 //Só paramos quando já tivermos todos os nós na árvore;
			double max = Collections.max(keys); 	 //Dá o máximo das chaves, logo o nó prioritário a adicionar à árvore;
			int index = keys.indexOf(max);      	 //Obtemos o índice deste nó;
			int node = kuwait.get(index);        	 //Identificamos esse mesmo nó como node;
			int papa = parents.get(index);		 	 //Obtemos o pai desse node;
			kuwait.remove(index);				 	 //Retiro o node dos nós em espera;
			keys.remove(index);                   	 //Retiro a chave do node da lista de chaves, uma vez que já vai ser adicionado;
			parents.remove(index);               	 //Retiro o pai do node da lista dos pais; utilizamos sempre index como argumento porque o nº do node só vai coincidir
			                                     	 //com a sua posição nas listas kuwait, keys e papa na primeira iterada;
			
			if(papa!=-1)
				graph.add_edge(papa, node);      //Só não acontece na primeira iterada;
		
				
			for(int i=0;i<dim && madj[node][i]!=Double.NaN;i++) {   // i são os nós ligados ao node, vou percorrê-los;
				int j = kuwait.indexOf(i); 							// indexOf devolve -1 se i não pertencer a kuwait; j é a posição de i na kuwait, que coincide com a sua posição na keys e na parents;
				if(j!=-1 && madj[node][i]>keys.get(j)) {            // Se i ainda estiver em espera para ser adicionado e o peso da aresta do node para i for maior que a chave de prioridade que lhe está 
					parents.set(j,node);                     	    // anteriormente destinada, então o pai da i passa a ser o node;
					keys.set(j, madj[node][i]);             	    // E a chave do i passa a ser o peso da ligação entre o node e esse mesmo i;
				}
			}
		}
		return graph;			//TCHATCHAN!
					
}				
			

	@Override
	public String toString() {
		return "WGraph [dim=" + dim + ", madj=" + Arrays.deepToString(madj) + "]";
	}
	
	
}







	