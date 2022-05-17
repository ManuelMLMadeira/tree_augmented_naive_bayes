package Entrega1;

import java.io.Serializable;
import java.util.ArrayList;


public class BN implements Serializable, BNInt {
	private static final long serialVersionUID=1L;
	private DGraph graph;
	private ArrayList<Tensor> list=new ArrayList<Tensor>(); 
	private int [] ranges;
	
//	Se receber o vetor ({0,0,0},3,1) devolve a sua "família" (de 3 elementos) no indice 1, isto é, {0,0,0},{0,1,0} e {0,2,0};
	private static ArrayList<int[]> geraFam (int [] v, int max, int index) { 
		ArrayList<int[]> A = new ArrayList<int[]> ();
		A.add(v);
		for (int j=1; j<max; j++) {
			int [] w = v.clone();   			// Clona o elemento inicial e varia a entrada naquele indice até max-1 (inclusive);
			w[index]=j;
			A.add(w);
			}
		return A; 
	}
	
//	Este método devolve uma arraylist de vetores. Os vetores devolvidos preenchem todas as entradas do tensor com os ranges recebidos;
//	Ex: dado {2,2,2}, sai [{0,0,0},{1,0,0},{0,1,0},{1,1,0},{0,0,1},{1,0,1},{0,1,1},{1,1,1}];
	private static ArrayList<int[]> coordenadas (int [] ranges) {
		int l=ranges.length;
		ArrayList<int[]> A = new ArrayList<int[]> (); 		//Onde iremos guardar os vetores que vamos criando;
		A.add(new int [l]);
		for (int i=l-1; i>=0; i--) { 						// i corresponde ao indice em que estamos. A ideia é fazer geraFams sucessivamente, a começar pelo último índice;
			int compA=A.size();      						// como o último indice a ser desdobrado será o 0, obtemos uma lista ordenada de vetores, em que vario o primeiro;  
			int max= ranges [i];	 						// indice apenas e mantenho os restantes constantes;
			ArrayList<int[]> B = new ArrayList<int[]> (); 
			for (int j=0; j< compA; j++) {				 	//j percorrerá os elementos de A, onde aplicaremos geraFam e vai guardando em B;
				int [] emA= A.get(j);                   
				B.addAll(geraFam(emA, max, i));			 	//B vai ser onde vamos acumular os vários geraFams para os vetores em A;
			}
			A= (ArrayList<int[]>) B.clone();             	//Passamos tudo para A, para guardar todos os vetores enquanto B será de novo inicializado para 
		}     											 	// ir acumular os geraFam da iterada seguinte;
		return A;
	}

//	Construtor da classe, constrói uma ArrayList com os Tensores relativos a cada nó;
	public BN (DGraph grafo, Amostra T, double S) {
		graph=grafo;					
		int l = graph.Length();
		ranges = T.ranges();
		int size = T.Length();
		
		for (int i=0; i<l; i++) {                          //Cada i é um nó;
			ArrayList<Integer> pais = graph.parents(i);
			int tp = pais.size();
			int[] v= new int[tp+1];
			
			v[0]=ranges[i];                               //Preparo um vetor v para entrar com os ranges pretendidos no Tensor e no coordenadas;
			for(int j=0; j<tp; j++) {
				v[j+1]=ranges[pais.get(j)];
			}
			
			Tensor t= new Tensor (v);                      //Tensor associado ao nó i;
			ArrayList<int[]> coord = coordenadas (v);      //Lista com todas as coordenadas do tensor (ordenadas da forma já explicada);
			
			double soma = 0;                               //Variáveis inicializadas para que eu tenha que percorrer menos uma vez a amostra, uma vez 
			int contador=1;                                // que para o último valor que i pode tomar, posso fazer 1-(soma de todos os outros valores 
			int q=ranges[i];							                                                             // que i toma, para os mesmos pais);
			
			while (!coord.isEmpty()) {                     //Enquanto houver coordenadas do tensor por preencher analiso a que me surge primeiro;
				int [] coordpretendida=coord.get(0);      
				
				
				if(contador%q==0) {                        // Caso em que posso fazer 1-soma, pois o contador é múltiplo do domínio do i (nó em que estamos);
					t.Valor(coordpretendida, 1-soma);
					soma=0;
				
				}										   //Caso contrário, tenho que ir contar entradas na amostra;
				else { if(tp==0) {                         //O caso em que o nó não tem pais distingue-se dos restantes, uma vez que neste caso todos os 
					int[] c = new int [] {i};              //dados cumprem vacuosamente a condição dos pais a tomar valor wi;
					int Tdiwi = T.Count(c,coordpretendida);
					int Twi = size;                         
					double p = (Tdiwi+S)/(Twi+S*v[0]);     //DFO
					t.Valor(coordpretendida, p);           // Insiro o valor resultante das DFOs na entrada certa do tensor associado a i;
					soma=soma+p;
					}
				
				
				else {                                     //Caso em que tenho pelo menos 1 pai
				int[] xipaisvec= new int [tp+1];           //Preparo o vetor xipaisvec que é um vetor que me indica quais as variáveis em questão
				xipaisvec[0]=i;                            // da forma: {Xi,P1,...,Pn}, sendo que P indica que é nó pai;
				for (int k=0; k<tp; k++) {
					xipaisvec[k+1]=pais.get(k);
				}
				int [] w=T.CountM(xipaisvec, coordpretendida); //Count Melhorado para evitarmos repetições de passos e assim podemos percorrer apenas 1
				int Tdiwi=w[0];                                //vez a amostra; Ver melhor na class Amostra;
				int Twi=w[1];
				double p = (Tdiwi+S)/(Twi+S*v[0]);             //DFO;
				t.Valor(coordpretendida, p);                   //Insiro o valor resultante das DFOs na entrada certa do tensor associado a i;
				soma=soma+p;
				}
				}
				
				contador++;      
				coord.remove(0); 							//Como foi esta a coord que estive a analisar, posso removê-la das coordenadas a analisar;
			}
			
			list.add(t); 
		}
	}
		
//	getter do Ranges
	public int[] getRanges() {
		return ranges;
	}

//	prob: Recebe uma rede de Bayes e um vetor e retorna a probabilidade desse vetor.
	public double prob (int[] patient) {
		double p = 1;
		int l=patient.length;
		for (int i=0; i<l; i++) {                          //i indica-nos o nó em que estamos. Vamos fazer um produtório (para todos os nós) 
			ArrayList<Integer> pais = graph.parents(i);    //das entradas certas de cada tensor associado a cada nó;
			int comp = pais.size();
			int values [] = new int [comp+1];              //values vai sair com as coordenadas da entrada a que devo aceder no tensor do nó i;
			values[0]=patient[i];                          //Determino a coordenada do tensor relativa a i (nó em que estamos);
			for (int j =0; j< comp; j++) {                 //Determino as coordenadas do tensor relativas aos pais;
				values[j+1]=patient [pais.get(j)];         
			}
			p=list.get(i).Entrada(values)*p;               //Fica explícito, desta forma, que as "entradas certas" no primeiro comentário relativo ao prob
		}   											   //são as cujas coordenads são dadas pelo values;
		return p;
	}



	@Override
	public String toString() {
		return "grafo=" + graph + ", tensores " + list + "";
	}

}
	
