package Entrega1;

import java.awt.EventQueue;


import javax.swing.JFrame;
import javax.swing.JRadioButton;
import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFormattedTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JScrollBar;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Color;

public class DrHouse  {
	private JFrame frmDrhouse;
	private BN rede;
	private boolean bayb;
	private int maxc;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DrHouse window = new DrHouse();
					window.frmDrhouse.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DrHouse() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmDrhouse = new JFrame();
		frmDrhouse.getContentPane().setBackground(Color.BLACK);
		frmDrhouse.setForeground(Color.WHITE);
		frmDrhouse.setTitle("DrHouse\n");
		frmDrhouse.setResizable(false);
		frmDrhouse.setBounds(100, 100, 600, 600);
		frmDrhouse.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDrhouse.getContentPane().setLayout(null);
		
		//LOGO
		
		JLabel lblNewLabel = new JLabel("New label");
		ImageIcon img = new ImageIcon (this.getClass().getResource("/P3.png"));
		lblNewLabel.setIcon(img);
		lblNewLabel.setBounds(109, 20, 365, 123);
		frmDrhouse.getContentPane().add(lblNewLabel);
		
		
		
		//DIRETORIA
		JLabel Diretoria = new JLabel("Please open a .ser file containing a Bayesian Network");
		Diretoria.setFont(new Font("Eurostile", Font.PLAIN, 15));
		Diretoria.setForeground(Color.WHITE);
		Diretoria.setBounds(174, 175, 420, 23);
		frmDrhouse.getContentPane().add(Diretoria);
		
		JLabel Diretoria1 = new JLabel("");
		Diretoria1.setFont(new Font("Eurostile", Font.PLAIN, 11));
		Diretoria1.setForeground(Color.WHITE);
		Diretoria1.setBounds(55, 199, 513, 23);
		frmDrhouse.getContentPane().add(Diretoria1);
		
		JLabel Aviso1 = new JLabel("");
		Aviso1.setFont(new Font("Eurostile", Font.PLAIN, 15));
		Aviso1.setHorizontalAlignment(SwingConstants.CENTER);
		Aviso1.setForeground(Color.WHITE);
		Aviso1.setBounds(45, 441, 537, 16);
		frmDrhouse.getContentPane().add(Aviso1);
		
		
		
		//BROWSE
		JFileChooser fileChooser=new JFileChooser();
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(".ser","ser"));
		fileChooser.setAcceptAllFileFilterUsed(false);
		JLabel lblDataInf = new JLabel("");
		lblDataInf.setForeground(Color.WHITE);
		lblDataInf.setFont(new Font("Eurostile", Font.PLAIN, 11));
		lblDataInf.setBounds(45, 349, 537, 16);
		frmDrhouse.getContentPane().add(lblDataInf);
		
		JButton btnRowse = new JButton("Open");
		btnRowse.setFont(new Font("Eurostile", Font.PLAIN, 20));
		btnRowse.setBounds(45, 155, 117, 46);
		btnRowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooser.setDialogTitle("Select a Bayesian Network");
				int resultValue =fileChooser.showOpenDialog((Component)e.getSource()); 
				String diretoria;
				
				if (resultValue==JFileChooser.APPROVE_OPTION) {
					diretoria = fileChooser.getSelectedFile().getPath();
					bayb=false;
					boolean b=true;
				try {
					FileInputStream fis = new FileInputStream (diretoria);
					ObjectInputStream ois =new ObjectInputStream (fis);
					rede= (BN) ois.readObject();         //fica definida a nossa variável privada;
					fis.close();
					ois.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					b=false;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					b=false;
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					b=false;	
				} catch (Exception e1) {
					b=false;
				}
				try {
				if(b) {             // Se tudo correr bem ao importar o ficheiro .ser
				int checkmark=0x2713;
				String simbolo= Character.toString((char)checkmark);
				Diretoria.setText(simbolo);
				Diretoria1.setText("Selected File: "+ diretoria);
				bayb=true;
				int i = rede.getRanges().length -1;
				lblDataInf.setText("Patient's Data: " + i + " integers following the discretization of each variable, separated by commas.");
				Aviso1.setText("");
				}
				else {
					Diretoria.setText("WARNING: the selected .ser file isn't a Bayesian Network!");
					Diretoria1.setText("                                        Please select other .ser file!");
				}
				} catch (Exception e1) {
					Diretoria.setText("WARNING: the selected Bayesian Network is corrupted!");
					Diretoria1.setText("                                        Please select other .ser file!");
				}
				
					
				}
			}
		});
		frmDrhouse.getContentPane().add(btnRowse);
		
		
		
		//DADOS
		JLabel pacienteAviso = new JLabel("Please insert patient's data:");
		pacienteAviso.setFont(new Font("Eurostile", Font.PLAIN, 20));
		pacienteAviso.setForeground(Color.WHITE);
		pacienteAviso.setBounds(45, 260, 288, 23);
		frmDrhouse.getContentPane().add(pacienteAviso);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(45, 284, 512, 62);
		frmDrhouse.getContentPane().add(scrollPane);
		
		JTextArea Dados = new JTextArea();
		Dados.setFont(new Font("Eurostile", Font.PLAIN, 13));
		scrollPane.setViewportView(Dados);
		Dados.setLineWrap(true);
		Dados.setWrapStyleWord(true);
		
		//CLASSIFY
		JLabel Aviso = new JLabel("");
		Aviso.setFont(new Font("Eurostile", Font.PLAIN, 15));
		Aviso.setForeground(Color.WHITE);
		Aviso.setHorizontalAlignment(SwingConstants.CENTER);
		Aviso.setBounds(45, 465, 537, 19);
		frmDrhouse.getContentPane().add(Aviso);

		JButton btnClassify = new JButton("Classify");
		btnClassify.setFont(new Font("Eurostile", Font.PLAIN, 24));
		btnClassify.setBounds(174, 496, 275, 64);
		btnClassify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//bayb - controlado no open;
				//patb - controlado aqui;
				if (bayb) {
				String paciente = Dados.getText();
				String [] vetors = paciente.split(",");
				int l=vetors.length;
				boolean patb;
				int [] ranges = rede.getRanges();
				try {
					patb = (l == ranges.length-1);
					if (patb) {
						int [] vetor = new int [l+1];
						for (int i =0 ; i<l && patb; i++) {
							vetor[i]=Integer.parseInt(vetors[i]);
							patb= 0 <= vetor[i] && vetor[i] < ranges [i];
						}
						if(bayb && patb) {             //Se estiver tudo bem com a Rede de Bayes e com os dados do paciente
							int rangeC = ranges [l];
							maxc=0;
							double max=0;
							for (int i=0; i<rangeC; i++) {      //i vai tomar os valores da classe
								vetor[l]=i;
								double p = rede.prob(vetor);
								if(p>max) {                    //guardo a classe que me dá a probabilidade mais alta
									maxc = i;
									max = p;
							}
						}
//							double s=0;                          //Caso pretenda obter o valor da probabilidade  da classe que maximiza 
//							for (int i=0; i<rangeC;i++) {        //a verosimilhança, é só "descomentar" este segmento de código.
//								vetor[l]=i;
//								double p = rede.prob(vetor);
//								s=s+p;
//							}
//							System.out.println(max/s);
					} 
				
				}	
				} catch (Exception e1) {
						patb=false;
						}
				
				if(!patb) {
					Aviso.setText("WARNING: your patient's data don't match the parameters above!");
				}
				else {
				 	Aviso.setText("");
					JOptionPane.showMessageDialog(null, "Diagnosed Result: "+maxc);
				}
				}
				
				else {
					Aviso1.setText("WARNING: you must provide a Bayesian Network!");
				}
				
			}
			});
		frmDrhouse.getContentPane().add(btnClassify);	
	}
}

