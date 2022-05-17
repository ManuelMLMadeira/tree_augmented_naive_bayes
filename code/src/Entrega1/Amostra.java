package Entrega1;

import java.io.Serializable;

import java.util.ArrayList;


		public class Amostra implements Serializable, AmostraInt {
		
//		Variáveis privadas da classe: am-Amostra, dim-Dimensão da amostra;
			
		private ArrayList <int[]> am;
		private int dim;
		
// 		Construtor da classe, cria uma Amostra vazia de dim=0;
		public Amostra () {
			am = new ArrayList <int[]> ();
			dim=0;
			}
		
//		Recebe um dado Ti e adiciona-o à Amostra;
		public void Add(int[] Ti) {
			am.add(Ti);
			dim++;
		}
		
//		Devolve o número de dados Ti da amostra;
		public int Length () {
			return dim;
		}
		
//		Recebe uma posição e retorna o dado Ti nessa posição da Amostra;
		public int[] Element (int i) {
				if (i<dim)
					return am.get(i);
				else
					return null; 
			}
		
//		Recebe um vetor de variáveis (var) e um vetor de valores (val) e retorna o número de ocorrências desses valores para essas variáveis na Amostra;
		public int Count (int[] var, int[] val) {
			int r=0;
			for(int i=0; i< dim;i++) {
				int[] v= Element(i);
				int m = var.length;
				boolean b = true;
				for(int j=0; j<m && b; j++) 
					if(v[var[j]] != val[j])
						b=false;
				if (b) 
					r++;}
			return r;
		}
		
/** 	Método aplicado à Amostra, retorna um Array de inteiros (v), onde cada posição do Array (k) corresponde ao número de valores que a variável na 
 * 		posição k toma em toda a amostra;				
 */
		public int [] ranges () {
			int l = Element(0).length;
			int [] v = new int [l];
			for (int k =0; k<l; k++) {
				int r = Element(0) [k];
				for (int j=1; j<dim; j++){
					int m = Element(j)[k];
					if (m>r)
						r=m;
				}
				v[k]=r+1;
			}
			return v;
			}

/**		Recebe um vetor de variáveis (var) e um vetor de valores (val) e retorna {Tdiwi,Twi},
*/
		public int[] CountM (int[] var, int[] val) {
			int Tdiwi=0; //Contador para nº de dados em que as variáveis var tomam os valores val respetivamente;
			int Twi = 0; //Contador para nº de dados em que as variáveis var excepto a primeira entrada a tomar val excepto a primeira entrada respetivamente;
			int m = var.length;
			
			for(int i=0; i< dim;i++) {				//Ciclo que percorre todos os dados da Amostra;
				int[] v= Element(i);
				boolean isTwi = true;
				boolean isTdiwi = true;
				
				for(int j=0; j<m && isTwi; j++) {	//Ciclo que percorre cada dado, distinguindo os dados que tomam os respectivos valores em todas
					if(j==0) {						//as variáveis (Tdiwi), dos que tomam os respectivos valores em todas as variáveis à excepção 
						if(v[var[j]]!= val[j]) 		//da primeira (Twi);
							isTdiwi=false;
					}
					else {			
						if(v[var[j]] != val[j])
							isTwi=false;
					}
				}
				if (isTwi) {
					Twi++;
					if(isTdiwi)
						Tdiwi++;
					}
			}
			return new int[] {Tdiwi,Twi};
		}
		
		/** Recebe um vetor de variáveis (var) e um vetor de valores (val) e retorna {XYC,XC,YC,C},
		 */
		
		public int[] CountXYC (int[] varXYC, int[] xyc) {
			int XYC=0; //Contador para nº de dados em que as variáveis X, Y e C a tomarem x, y e c respetivamente;
			int YC=0;  //Contador para nº de dados em que as variáveis Y e C a tomarem y e c respetivamente;
			int XC=0;  //Contador para nº de dados em que as variáveis X e C a tomarem x e c respetivamente;
			int C=0;   //Contador para nº de dados em que a variável C a tomar c respetivamente;
			for(int i=0; i< dim;i++) {				//Cada i é um dado da amostra, percorremos toda a amostra a realizar os joguinhos lógicos abaixo;
				int[] v= Element(i); 
				boolean bYC = true;
				boolean bXC = true;
				boolean bC = true;
		                           					
				if(v[varXYC[2]]!= xyc[2]) {						
					bC=false;							
				}
				else {
					if(v[varXYC[1]]!= xyc[1])
						bYC=false;
					if(v[varXYC[0]]!= xyc[0])
						bXC=false;
					}
				if(bC) {
					C++;
					if(bYC) 
						YC++;
					if(bXC) 
						XC++;
					if(bXC && bYC) 
						XYC++;	
					
			}}
			return new int[] {XYC,XC,YC,C};
		}


		@Override
		public String toString() {
			return "Amostra [am=" + dim + "]" ;
		}
			
}
		
		
			
		
		
		
	
	

	