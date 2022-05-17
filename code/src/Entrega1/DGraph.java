package Entrega1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class DGraph implements Serializable, DGraphInt {
	private static final long serialVersionUID=1L;
	private ArrayList<ArrayList<Integer>> vector;
	
	
//	Construtor da classe, cria um DGraph, conjunto de n vértices disconexos;
	public DGraph (int n) {
		 vector = new ArrayList <ArrayList<Integer>> ();
		 for(int i=0;i<n;i++)
			 vector.add( new ArrayList<Integer> ());
		 }
	
// Dá-nos o nº de nós do grafo;
	public int Length () {
		return vector.size();
		}
	
//	Adiciona um nó ao DGraph vector;
	public void add_node () {
		vector.add(new ArrayList<Integer> ());
	}
	
//	Recebe dois nós e adiciona ao grafo uma aresta de um nó para o outro; Adiciona o nó i da lista de pais de j e ordenando-os;
	public void add_edge (int i, int j) {
		vector.get(j).add(i);
		Collections.sort( vector.get(j) );
	}
	
//	Recebe dois nós e retira ao grafo a aresta de um nó para o outro; Retira o nó i da lista de pais do nó j;
	public void remove_edge (int i, int j) {
		vector.get(j).remove(new Integer (i));
	}
	
//	Recebe um nó e retorna a lista de nós que são pais do nó;
	
	public ArrayList<Integer> parents (int n) {
		return vector.get(n);
	}
	
	@Override
	public String toString() {
		return "DGraph [vector=" + vector + "]";
	}

	
}



