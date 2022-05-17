package Entrega1;

import java.util.ArrayList;

public interface DGraphInt {
	public void add_edge (int i, int j);
	public int Length ();
	public void remove_edge (int i, int j);
	public void add_node ();
	public ArrayList<Integer> parents (int n);
}
