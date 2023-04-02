package graph_coloring;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;
public class graph_coloring {
	 public static void main(String[] args) {
	        int nbmax_couleurs=3;
	        int n=10;
	        int tab_arc[][]=new int[n][n];
	        for (int i=0;i<n;i++) {
	        	for (int j=0;j<n;j++) {
	        		tab_arc[i][j]=0;
	        	}
	        }
	        tab_arc[0][2]=1;
	        tab_arc[0][1]=1;
	        tab_arc[0][3]=1;
	        
	        tab_arc[1][0]=1;
	        tab_arc[1][4]=1;
	        tab_arc[1][8]=1;
	        
	        Model nom_modele=new Model("Graph_coloring");
	        IntVar[] vars=new IntVar[n*n];
	        for (int q=0;q<n;q++) {
	        	vars[q]=nom_modele.intVar("C_"+q,1,nbmax_couleurs);
	        }
	        for (int i=0;i<n;i++) {
	        	for (int j=0;j<n;j++) {
	        		if (tab_arc[i][j]==1)
	        		nom_modele.arithm(vars[i], "!=", vars[j]).post();
	        		
	        	}
	        }
	        System.out.println(nom_modele.toString());
	        Solution solution=nom_modele.getSolver().findSolution();
	        if (solution!=null) {
	        	System.out.println(solution.toString());
	        }
	        
	  }
}
