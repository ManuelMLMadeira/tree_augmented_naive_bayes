package Entrega1;

import java.awt.Component; 

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.Color;


public class Apprendiz  {

	private JFrame frame;
	private Amostra Sample;
	private BN rede;
	private boolean browb =false;
	
	
	//FUNÇÕES AUXILIARES
	
// Método privado que nos permite calcular a informação mútua condicional;
	 private double IT(int X, int Y) {
		  double sum=0.0;
		  int [] ranges = Sample.ranges();
		  int C = ranges.length-1;
		  int rangeX = ranges [X];
		  int rangeY = ranges [Y];
		  int rangeC = ranges [C];
		  int [] varXYC = {X,Y,C};
		  int N = Sample.Length();
		  
		  for (int x=0; x<rangeX ;x++) {
			  for(int y=0; y<rangeY ;y++) {
				  for(int c=0; c<rangeC ;c++) {
					  int [] counted = Sample.CountXYC(varXYC,new int[] {x,y,c}); //Utilizamos o CountXYC por uma questão de eficiência (evitamos passos 
					  double  Pxyc= (double) counted[0];                        //repetidos e percorremos apenas uma vez a amostra);
					  
					  if(Pxyc != 0.0) {
						  double Pxc= (double) counted[1];
						  double Pyc= (double) counted[2];
						  double Pc= (double) counted[3];
						  sum=sum+(Pxyc/N)*(Math.log10((Pxyc*Pc)/(Pyc*Pxc))); 		// Não considerámos as divisões por N no cálculo de Pxc, Pyc e Pc
					  }	  															// evitando operações desnecessárias.
				  }
				  
			  }
		  }
		  return sum; 
	  }
	 
// Permite-nos obter o grafo pesado, em que o peso da aresta entre o nó i e j é a informação mútua condicional entre as variáveis Xi e Xj;
	 private WGraph pesadao () {
		 int nvars =Sample.Element(0).length-1;     // -1 é para tirar a classe;
		 WGraph gp = new WGraph (nvars);
		 for (int i=0; i<nvars; i++) {
			 for(int j =i+1; j<nvars;j++ ) {
				 gp.add_edge(i, j, IT(i,j));
			 }
		 }
		 return gp;
	 }
	 
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Apprendiz window = new Apprendiz();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	
		
	}

	/**
	 * Create the application.
	 */
	public Apprendiz() {
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("Apprendiz\n");
		frame.getContentPane().setBackground(new Color(0,0,0));
		frame.getContentPane().setEnabled(false);
		frame.setBounds(100, 700, 750, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		//LOGO
		
		JLabel lblLogo = new JLabel("");
		lblLogo.setBackground(new Color(0, 0, 0));
		ImageIcon img = new ImageIcon (this.getClass().getResource("/P1.png"));
		lblLogo.setIcon(img);
		lblLogo.setBounds(443, 22, 269, 195);
		frame.getContentPane().add(lblLogo);
		
		
		//FUNCIONAMENTO DO BROWSE
		
		JLabel Done = new JLabel("Insert your .csv sample!");
		Done.setForeground(new Color(255, 255, 255));
		Done.setFont(new Font("Eurostile", Font.PLAIN, 18));
		Done.setVerticalAlignment(SwingConstants.TOP);
		Done.setBounds(95, 107, 223, 24);
		frame.getContentPane().add(Done);
		
		JLabel lblDiretoria = new JLabel("");
		lblDiretoria.setFont(new Font("Eurostile", Font.PLAIN, 13));
		lblDiretoria.setForeground(new Color(255, 255, 255));
		lblDiretoria.setBounds(105, 229, 639, 24);
		frame.getContentPane().add(lblDiretoria);
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(".csv","csv"));   //programa só aceita ficheiros em formato .csv .
		fileChooser.setAcceptAllFileFilterUsed(false);
		
		JLabel Warning = new JLabel("");
		Warning.setHorizontalAlignment(SwingConstants.CENTER);
		Warning.setFont(new Font("Eurostile", Font.PLAIN, 15));
		Warning.setForeground(new Color(255, 255, 255));
		Warning.setBounds(156, 407, 377, 47);
		frame.getContentPane().add(Warning);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.setForeground(new Color(0, 0, 0));
		btnBrowse.setFont(new Font("Eurostile", Font.PLAIN, 25));
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooser.setDialogTitle("Choose your patient's sample");
				
				int resultValue =fileChooser.showOpenDialog((Component)e.getSource()); // abre a janela para pesquisar
				boolean b=true;
				if (resultValue==JFileChooser.APPROVE_OPTION) { // caso corra tudo bem
					
					try {
						
						List<String> lines = Files.readAllLines(fileChooser.getSelectedFile().toPath(),Charset.defaultCharset());
						Amostra T = new Amostra ();
						int ndados =lines.size();
						for (int i=0; i<ndados; i++) { //i dá-me o índice do dado em que estou da amostra
							
							String [] v = lines.get(i).split(",");
							int l=v.length;
							int[] dados = new int[l];
							for(int j=0;j<l;j++) {    //converter cada string daquele dado num inteiro
								dados[j]=Integer.parseInt(v[j]);
							}
							
							T.Add(dados); //Cada dado vai sendo adicionado à amostra;
							
						}
						Sample=T;         // Guardamos a amostra obtida numa variável privada
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						b=false;                       //Caso alguma coisa corra mal na conversão do ficheiro numa Amostra, b passa a falso; 
						}
					if(b) {			
						String diretoria=fileChooser.getSelectedFile().getPath();
						lblDiretoria.setText("Selected File: "+diretoria);
						browb=true;
					}
					else {
						Done.setText("");
						lblDiretoria.setText("WARNING: Please select a .csv sample in the right format!");
						browb=false;
					}
					Warning.setText("");
				}
			}
		});
		btnBrowse.setBounds(95, 139, 180, 78);
		frame.getContentPane().add(btnBrowse);
		
		//FUNCIONAMENTO DO LABEL INSERIR NOME
		JLabel lblInserirNome = new JLabel("Save sample as:");
		lblInserirNome.setFont(new Font("Eurostile", Font.PLAIN, 25));
		lblInserirNome.setForeground(new Color(255, 255, 255));
		lblInserirNome.setVerticalAlignment(SwingConstants.TOP);
		lblInserirNome.setHorizontalAlignment(SwingConstants.LEFT);
		lblInserirNome.setBounds(156, 337, 223, 30);
		frame.getContentPane().add(lblInserirNome);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(156, 369, 444, 30);
		frame.getContentPane().add(scrollPane);
		
		JTextArea Nome = new JTextArea();
		Nome.setFont(new Font("Eurostile", Font.PLAIN, 18));
		scrollPane.setViewportView(Nome);
		Nome.setLineWrap(true);
		Nome.setWrapStyleWord(true);
		
		//FUNCIONAMENTO DO LEARN
		JFileChooser fileChooser2 = new JFileChooser();
	
		JButton btnLearn = new JButton("Learn & Save");
		btnLearn.setBackground(new Color(255, 255, 255));
		btnLearn.setForeground(new Color(0, 0, 0));
		btnLearn.setFont(new Font("Eurostile", Font.PLAIN, 25));
		btnLearn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (!browb)
					Warning.setText("WARNING: You must insert a sample!");
				
				else if (Nome.getText().equals("")) {
					Warning.setText("WARNING: You must choose the file's name!");
					
				}
				
				
				else {  //Só se o utilizador já tiver carregado uma amostra em formato .csv com sucesso e inserido um nome é que o programa aprende a 
					    //Rede de Bayes e guarda-a na memória do computador;
					
					fileChooser2.setDialogTitle("Choose a directory to save");
					fileChooser2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int resultValue = fileChooser2.showSaveDialog(null); 
					if (resultValue==JFileChooser.APPROVE_OPTION) { 
						
						WGraph pesado = pesadao(); //criamos o grafo pesado com os ITs entre os nós como peso das arestas que os ligam;
						
						DGraph dirigido =  pesado.MST(0); //Obtemos a árvore geradora maximal do grafo anterior;
						
						dirigido.add_node();               //Adicionamos a classe;
						int c = Sample.Element(0).length-1;
						for(int i=0; i<c; i++) {		   //Ciclo para acrescentar as arestas (dirigidas) da classe para todos os nós;
							dirigido.add_edge(c, i);
						}
						
						rede = new BN(dirigido, Sample, 0.5);        //Geramos a Rede de Bayes a partir do grafo acima, da amostra dada pelo utilizador
																	 //e com 0.5 como pseudo-contagem
						
						
						FileOutputStream fos;
						try {
							fos = new FileOutputStream(fileChooser2.getCurrentDirectory()+"/"+ Nome.getText() +".ser");
							ObjectOutputStream oos = new ObjectOutputStream(fos);
							oos.writeObject(rede);
							fos.close();
							oos.close();
						
							
							
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

						JOptionPane.showMessageDialog(null, "Success! Your Bayesian Network was saved!");
						}
					}
				}
			});
		btnLearn.setBounds(233, 464, 230, 78);
		frame.getContentPane().add(btnLearn);		
	}
	}

