package n_reines;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;
//https://choco-solver.org/tutos/first-example/first-model/
public class n_reines {
	  public static void main(String[] args) {
	        int n=8;
	        Model nom_modele=new Model(n+"-queens problem");
	        IntVar[] vars=new IntVar[n];
	        for (int q=0;q<n;q++) {
	        	vars[q]=nom_modele.intVar("Q_"+q,1,n);
	        }
	        for (int i=0;i<n-1;i++) {
	        	for (int j=i+1;j<n;j++) {
	        		nom_modele.arithm(vars[i], "!=", vars[j]).post();
	        		nom_modele.arithm(vars[i], "!=", vars[j],"-",j-i).post();
	        		nom_modele.arithm(vars[i], "!=", vars[j],"+",j-i).post();
	        	}
	        }
	        System.out.println(nom_modele.toString());
	        Solution solution=nom_modele.getSolver().findSolution();
	        if (solution!=null) {
	        	System.out.println(solution.toString());
	        }
	        
	  }
}
