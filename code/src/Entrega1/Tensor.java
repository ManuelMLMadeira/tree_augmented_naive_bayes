package Entrega1;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Tensor implements Serializable, TensorInt {
	private static final long serialVersionUID=1L;
	private ArrayList t;


// Construtor do tensor;
	public Tensor (int[] ranges) {
		int n=ranges.length;
		
		
		if(n==1) { 									//Se o tensor for de 1ª ordem, é um vetor com tantas entradas quanto o range relativo a essa dimensão;
			t=new ArrayList<Double> (Collections.nCopies(ranges[0],Double.NaN));  ;
		}
		
		else {
			if (n==2) { 							//Se o tensor for de 2ª ordem, é uma matriz em que o nº de colunas corresponde ao nº de valores que a primeira variável do ranges;
			ArrayList<ArrayList> mat = new ArrayList<ArrayList> ();  // mat adi o nº de linhas ao nº de valores que a segunda variável do ranges toma;
			for(int i=0; i<ranges[1];i++) {
				mat.add(new ArrayList<Double> (Collections.nCopies(ranges[0],Double.NaN)));
			}
				t=mat;
		}
			
			else { 						// Neste ciclo é criado um novo tensor de ordem n, a partir de tensores de ordem imediatamente inferior, recursivamente;
				int m=ranges[n-1];
				ArrayList<Tensor> tens = new ArrayList<Tensor> ();
				for(int i=1; i<=m;i++) {
					int[] v = Arrays.copyOfRange(ranges, 0, n-1);
					tens.add(new Tensor(v));
						}
				t=tens;
					}
				}	
			}	
	
//  Método Entrada, devolve a entrada cujas "coordenadas" em n dimensões são dadas no vetor índices;
	public double Entrada (int[] indices) {
		int l =indices.length;
		if (l==1)   							//Se só tiver um índice quer dizer que é um vetor;
			return (double) t.get(indices[0]);
		
		else{ 
			if(l==2) { 							//Se só tiver 2 índices quer dizer que é uma matriz;
			return (double) ((ArrayList<Double>) t.get(indices[1])).get(indices[0]); //Seleciono a linha que quero primeiro (segundo indice do indices);
			}                 														 // e só depois a coluna pretendida (primeiro indice do indices);
			
			else {        					    //Recursivamente vou buscar a entrada ao tensor de ordem imediatamente inferiorcujo indice na arraylist
				int [] v=Arrays.copyOfRange(indices, 0, l-1);    					//  é o último do indices;
				return ((Tensor) t.get(indices[l-1])).Entrada(v);
			}
		} 
	}
	
//  Substitui a entrada cujas "coordenadas" são dadas no índices, pelo valor p;
	public void Valor (int[] indices, double p) {
		int l =indices.length;
		if (l==1)  							//Se só tem um indice, é um vetor;
			t.set(indices[0],p);   
		
		else{ 
			if(l==2) { 						// Se só tiver 2 indices quer dizer que é uma matriz, logo acedo da mesma forma que o entrada e,no último passo, 
				((ArrayList<Double>) t.get(indices[1])).set(indices[0], p);			//  em vez de get, faço set para alterar o seu valor;
			}
			
			else {     					    //Recursivamente vou buscar a entrada ao tensor de ordem imediatamente inferior cujo indice na arraylist;			
				int [] v= Arrays.copyOfRange(indices, 0, l-1); 						//é o último do indices até que chego a uma das ordens acima e
				((Tensor) t.get(indices[l-1])).Valor(v, p);							//substitui a entrada desejada;
			}
				
			}		
		}

	@Override
	public String toString() {
		return  ""+ t +"";
	}
	
	
	
	}
	
	

	
	