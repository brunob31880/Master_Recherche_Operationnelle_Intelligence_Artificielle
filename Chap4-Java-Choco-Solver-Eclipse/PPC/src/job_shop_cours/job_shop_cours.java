package job_shop_cours;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.BoolVar;
public class job_shop_cours {
	 public static void main(String[] args) {
	        int n=3;
	        int m=3;
	        int D=100;
	        int P[][]=new int[n][m];
	        // Gamme pièce 1
	        P[0][0]=10;
	        P[0][1]=20;
	        P[0][2]=30;
	        // Gamme pièce 2
	        P[1][0]=5;
	        P[1][1]=4;
	        P[1][2]=10;
	        // Gamme pièce 3
	        P[2][0]=12;
	        P[2][1]=7;
	        P[2][2]=4;
	        // Description de la "matrice" des machines
	        int M[][]=new int[n][m];
	        // Gamme pièce 1
	        M[0][0]=1;
	        M[0][1]=2;
	        M[0][2]=3;
	        // Gamme pièce 2
	        M[1][0]=2;
	        M[1][1]=3;
	        M[1][2]=1;
	        // Gamme pièce 3
	        M[2][0]=3;
	        M[2][1]=2;
	        M[2][2]=1; 
	        // Précalcul de la linéarisation 
	        int Tab[][]=new int[n][m];
	        Tab[0][0]=0;
	        Tab[0][1]=1;
	        Tab[0][2]=2;
	        Tab[1][0]=3;
	        Tab[1][1]=4;
	        Tab[1][2]=5;
	        Tab[2][0]=6;
	        Tab[2][1]=7;
	        Tab[2][2]=8;
	        Model nom_modele=new Model("Job Shop");
	        // Creation des variables avec leurs bornes
	        // variables de début d'opération
	        IntVar[] date_debut=new IntVar[n*m];
	        for (int i=0;i<n;i++) {
	        	  for (int j=0;j<m;j++) {
	        		  // Ici on aurai pu ecrire i*m+j mais on utilise plutot le tableau Tab
	        		  date_debut[Tab[i][j]]=nom_modele.intVar("ES_"+i+"_"+j,1,D);
	        	  }
	        }
	        // données de durée d'opération 
	        IntVar[] durees=new IntVar[n*m];
	        for (int i=0;i<n;i++) {
	        	  for (int j=0;j<m;j++) {
	        		  // Ici on aurai pu ecrire i*n+j mais on utilise plutot le tableau Tab
	        		  durees[Tab[i][j]]=nom_modele.intVar("p_"+i+"_"+j,P[i][j],P[i][j]);
	        	  }
	        }
	        // variables de fin d'opération
	        IntVar[] date_fin=new IntVar[n*m];
	        for (int i=0;i<n;i++) {
	        	  for (int j=0;j<m;j++) {
	        		  // Ici on aurai pu ecrire i*m+j mais on utilise plutot le tableau Tab
	        		  date_fin[Tab[i][j]]=nom_modele.intVar("EF_"+i+"_"+j,1,D);
	        	  }
	        }
	        // variables disjonctives bijuv
	        BoolVar[][] b=new BoolVar[n*m][n*m];
	        for (int i=0;i<n;i++) {
	        	for (int j=0;j<m;j++) {
	        		 for (int u=0;u<n;u++) {
	     	        	for (int v=0;v<m;v++) {
	     	        	b[Tab[i][j]][Tab[u][v]]=nom_modele.boolVar("b_"+i+"_"+j+"_"+u+"_"+v);
	     	        		
	     	        	}
	     	        }
	        		
	        	}
	        }
	        IntVar OBJ=nom_modele.intVar("Cmax",0,999);
	        // Contraintes
	        // contrainte 1.3 relation entre date de début et date de fin
	        int[] coeffs=new int[3];
	        coeffs[0]=1; //EF
	        coeffs[1]=-1; // ES
	        coeffs[2]=-1; //P
	        for (int i=0;i<n;i++) {
	        	for (int j=0;j<m;j++) {
	        		 int cour=Tab[i][j];
	        		 IntVar[] colonne=new IntVar[3];
	        		 colonne[0]=date_fin[cour];
	        		 colonne[1]=date_debut[cour];
	        		 colonne[2]=durees[cour];
	        		 nom_modele.scalar(colonne, coeffs, "=", 0).post();
	        	}
	        }
	        //Contrainte 1.4 ESij>=EFij-1 le debut de traitement de la piece i sur la machine j est supérieur ou egal à la fin du traitement de cette pièce sur la machine j-1
	        for (int i=0;i<n;i++) {
	        	for (int j=1;j<m;j++) {
	        		int prec=Tab[i][j-1];
	        		int cour=Tab[i][j];
	        		nom_modele.arithm(date_fin[prec], "<=", date_debut[cour]).post();
	        	}
	        }
	        //Contraintes disjonction
	        for (int i=0;i<n;i++) {
	        	for (int j=0;j<m;j++) {
	        		int ope_i=Tab[i][j];
	        		 for (int u=i+1;u<n;u++) {
	     	        	for (int v=0;v<m;v++) {
	     	        	int ope_u=Tab[u][v];
	     	        	// Si l'on est sur la même machine soit ope_i a lieu avant ope_u soit le contraire
	     	        	if (M[i][j]==M[u][j]) {
	     	        		nom_modele.ifThenElse(b[ope_i][ope_u],
	     	        				nom_modele.arithm(date_fin[ope_i], "<=", date_debut[ope_u]), 
	     	        				nom_modele.arithm(date_fin[ope_u], "<=", date_debut[ope_i]));
	     	        	}
	     	        		
	     	        	}
	     	        }
	        		
	        	}
	        }
	        for (int i=0;i<n;i++) {
	        	nom_modele.arithm(date_fin[Tab[i][m-1]], "<=", OBJ).post();
	        }
	        nom_modele.setObjective(Model.MINIMIZE, OBJ);
	        System.out.println(nom_modele.toString());
	        Solution solution=nom_modele.getSolver().findSolution();
	        if (solution!=null) {
	        	System.out.println(solution.toString());
	        }
	        
	  }
}
