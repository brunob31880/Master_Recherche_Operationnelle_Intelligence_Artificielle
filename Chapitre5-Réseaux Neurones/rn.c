#include "matrices.h"
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
const int n1 = 3; //  3 neurones en entrée
const int n2 = 1; //  1 neurone en sortie
/*
Définition d'un réseau de neurones
*/
typedef struct T_Reseau
{
    double X[n1];
    double t[n2];
    double W[n1][n2];
    //  variable pour la couche de sortie
    double a[n2]; //  n2 neurones
    double o[n2]; //  n2 neurones
    double epsilon[n2];
    double derive_o[n2];
    double delta_sortie[n2];
    double somme_erreur;
    double somme_totale; //  erreur sommée sur la totalité des exemples
    double delta_W[n1][n2];
    double mu;
} T_Reseau;
/*
Fonction f
*/
double f(double x)
{
    return (double)1.0 / ((double)1 + exp(-x));
}
/*
Différentielle de f
*/
double df(double x)
{
    return x * ((double)1 - x);
}
/*
Evaluation d'un réseau
*/
void evaluer(T_Reseau *Un_Reseau)
{
    // printf("---------- Evaluation ----------\n");
    for (int i = 0; i < n2; i++)
    {
        double somme = 0;
        for (int j = 0; j < n1; j++)
        {
            somme = somme + Un_Reseau->W[j][i] * Un_Reseau->X[j];
        }
        Un_Reseau->a[i] = somme;
        Un_Reseau->o[i] = f(Un_Reseau->a[i]);
    }
}
/*
Calcul d'un réseau
*/
void calculer_erreur(T_Reseau *Un_Reseau)
{
    Un_Reseau->somme_erreur = 0;
    for (int i = 0; i < n2; i++)
    {
        Un_Reseau->epsilon[i] = Un_Reseau->t[i] - Un_Reseau->o[i];
        Un_Reseau->somme_erreur += fabs(Un_Reseau->epsilon[i]);
    }
}
/*
Affichae d'un réseau
*/
void afficher(T_Reseau Un_Reseau)
{
    printf("------- W --------\n");
    for (int i = 0; i < n1; i++)
    {
        for (int j = 0; j < n2; j++)
        {
            printf("%f \n", Un_Reseau.W[i][j]);
        }
        printf("\n");
    }
}
/*
Mettre à jour un réseau
*/
void mettre_a_jour_le_reseau(T_Reseau *Un_Reseau)
{
    // calcul de la dérivée du résultat:dérivée de o
    // calcul du delta sur la sortie
    for (int i = 0; i < n2; i++)
    {
        Un_Reseau->derive_o[i] = df(Un_Reseau->o[i]);
        Un_Reseau->delta_sortie[i] = Un_Reseau->derive_o[i] * Un_Reseau->epsilon[i];
    }
    //  Calcul de W
    //  Mise à jour de W
    for (int i = 0; i < n1; i++)
    {
        for (int j = 0; j < n2; j++)
        {
            Un_Reseau->delta_W[i][j] = Un_Reseau->mu * Un_Reseau->X[i] * Un_Reseau->delta_sortie[j];
            Un_Reseau->W[i][j] = Un_Reseau->W[i][j] + Un_Reseau->delta_W[i][j];
            //printf("MAJ Poids %f",Un_Reseau->W[i][j]);
        }
    }
}
void afficher_erreur(int iter, T_Reseau *Un_Reseau)
{
}
/**
 *
 */
void apprentissage(T_Reseau *Un_Reseau, double Data[][3], double valeur_attendu[4])
{
    int stop = 0;
    int point_depart = 0;
    int iter = 0;

    while ((stop == 0) && (point_depart < 5))
    {
        point_depart++;
        // poids random
        Un_Reseau->W[0][0] = (rand() % 50) / (float)50;
        Un_Reseau->W[1][0] = (rand() % 50) / (float)50;
        Un_Reseau->W[2][0] = (rand() % 50) / (float)50;
        //afficher(*Un_Reseau);
        Un_Reseau->mu = 0.9;
        iter = 0;
        stop = 0;
        while ((stop == 0) && (iter <= 1500))
        {
            float somme_prec = Un_Reseau->somme_totale;
            Un_Reseau->somme_totale = 0;
            for (int j = 0; j < 4; j++)
            {
                Un_Reseau->X[0] = Data[j][0];
                Un_Reseau->X[1] = Data[j][1];
                Un_Reseau->X[2] = Data[j][2];
                Un_Reseau->t[0] = valeur_attendu[j];
                evaluer(Un_Reseau);
                calculer_erreur(Un_Reseau);
                afficher_erreur(iter, Un_Reseau);
                Un_Reseau->somme_totale += fabs(Un_Reseau->somme_erreur);
                mettre_a_jour_le_reseau(Un_Reseau);
            } //  fin des exemples
           
            if (fabs(Un_Reseau->somme_totale - somme_prec) < 0.00001)
            {
                printf("Stop delta erreur totale=%f\n",fabs(Un_Reseau->somme_totale - somme_prec));
                stop = 1;
            }
            if (fabs(Un_Reseau->somme_totale) < 0.27)
            {
                printf("Stop erreur totale=%f\n",fabs(Un_Reseau->somme_totale));
                stop = 1;
            }
            Un_Reseau->mu = Un_Reseau->mu - 0.01;
            if (Un_Reseau->mu < 0.1)
            {
                Un_Reseau->mu = 0.1;
            }
            iter++;
        }
        printf("Iteration %d\n",iter);
        //  fin des itérations
    }
}

void secondePhase(T_Reseau *Un_Reseau, double Data[][3], double valeur_attendu[4])
{
    printf("====== Test de la performance du réseau trouvé =====\n");
    for (int j = 0; j < 4; j++)
    {
        Un_Reseau->X[0] = Data[j][0];
        Un_Reseau->X[1] = Data[j][1];
        Un_Reseau->X[2] = Data[j][2];
        Un_Reseau->t[0] = valeur_attendu[j];
        evaluer(Un_Reseau);
        calculer_erreur(Un_Reseau);
        printf("Entrée %f %f %f %f/%f Erreur=%f\n", Un_Reseau->X[0], Un_Reseau->X[1], Un_Reseau->X[2], Un_Reseau->t[0], Un_Reseau->o[0], Un_Reseau->somme_erreur);
    }
}

int main(int argc, char **argv)
{
    T_Reseau reseau;
    T_Reseau b_reseau;
    double valeur_attendu[4] = {0, 1, 1, 0};
    double datas[4][3] = {
        {0, 0, 1},
        {1, 1, 1},
        {1, 0, 1},
        {0, 1, 1}};
    apprentissage(&reseau, datas, valeur_attendu);
    printf("Reseau trouvé\n");
    afficher(reseau);
    secondePhase(&reseau, datas, valeur_attendu);
}