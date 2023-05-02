from constraint import *
from numpy import zeros
# Graphe
n=3 #nb de pieces
m=3 #nb de machines
# Description de la "matrice" des durées de production
P=zeros((n,m),int)
# Horizon de planification
D=120;
# Gamme pièce 1
P[0][0]=10
P[0][1]=20
P[0][2]=5
# Gamme pièce 2
P[1][0]=20
P[1][1]=5
P[1][2]=25
# Gamme pièce 3
P[2][0]=14
P[2][1]=2
P[2][2]=15
# Description de la "matrice" des machines
M=zeros((n,m),int)
# Gamme pièce 1
M[0][0]=1
M[0][1]=3
M[0][2]=2
# Gamme pièce 2
M[1][0]=1
M[1][1]=3
M[1][2]=2
# Gamme pièce 3
M[2][0]=1
M[2][1]=3
M[2][2]=2
# CONJONCTION ESij>=EFij-1=ESij-1+pij-1
def conjonction(sti,stj,dureej):
    if (sti>=stj+int(dureej)):
        return True;
    else:
        return False;
# DISJONCTION
def disjunction(sti,dureei,stj,dureej):
    if ((stj>=sti+int(dureei)) or (sti>=stj+int(dureej))):
         return True;
    else:
        return False;
def calculer_cout(cout,f1,f2,f3,d1,d2,d3):
    if (cout>=f1+int(d1)) and (cout>=f2+int(d2)) and (cout>=f3+int(d3)):
        return True;
    else:
        return False;
problem = Problem();
# Creation des variables avec leurs bornes
# Variables de début d'opération
for i in range (n):
    somme = 0
    for j in range (m):
        nom = "st_"+str(i)+"_"+str(j);
        problem.addVariable(nom, range(somme, D))
        print(str(nom)+">="+str(somme))
        somme = somme  + P[i][j]
# Données de durées => Variables dont la plage est limitée à une valeur
for i in range (n):
    for j in range (m):
        nom = "duree_"+str(i)+"_"+str(j);
        print(str(nom)+"="+str(P[i][j]))
        problem.addVariable(nom, range(P[i][j], P[i][j]+1))

Cmax = "Cmax";
problem.addVariable(Cmax, range(0, D))
# Contraintes
# Contraintes sur les conjonctions
for i in range(0,3):
    for j in range(1,3):
        nom_prec = "st_"+str(i)+"_"+str(j-1);
        nom = "st_"+str(i)+"_"+str(j);
        duree_prec = "duree_"+str(i)+"_"+str(j-1);
        print(str(nom)+">="+str(nom_prec))
        problem.addConstraint(conjonction, ([nom,nom_prec, duree_prec]))
# Contraintes sur les disjonctions
for i in range(n-1):
    for j in range(m):
        for i2 in range(i+1,n):
            for j2 in range(m):
                nom = "st_"+str(i)+"_"+str(j);
                nom2 = "st_"+str(i2)+"_"+str(j2);
                duree = "duree_"+str(i)+"_"+str(j);
                duree2 = "duree_"+str(i2)+"_"+str(j2);
                if (M[i][j]==M[i2][j2]):
                   print("Add constraint "+str(nom)+" disjonction "+str(nom2))
                   problem.addConstraint(disjunction, ([nom,duree,nom2, duree2]))
cout = "Cmax";
nom = "st_"+str(0)+"_"+str(2);
nom2 = "st_"+str(1)+"_"+str(2);
nom3 = "st_"+str(2)+"_"+str(2);
duree = "duree_"+str(0)+"_"+str(2);
duree2 = "duree_"+str(1)+"_"+str(2);
duree3 = "duree_"+str(2)+"_"+str(2);
problem.addConstraint(calculer_cout, ([cout, nom,nom2, nom3, duree, duree2, duree3]));


# recherche d'une solution
solutions = problem.getSolution()



print("-- affichage par défaut --")
print(solutions)

print("-- affichage detaille --")
nom = "Cmax"
valeur = solutions[nom]
print("cout = "+str(valeur))

for i in range(n):
    for j in range(m):
        nom = 'st_'+str(i)+'_'+str(j);
        valeur = solutions[nom]
        print(valeur," \t ",end='')  
    print(" \n ")  

print("\n fin...")