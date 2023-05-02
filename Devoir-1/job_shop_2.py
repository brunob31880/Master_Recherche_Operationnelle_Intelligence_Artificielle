from constraint import Problem, AllDifferentConstraint

# Définition des pièces et des machines
pieces = ['p1', 'p2', 'p3']
machines = ['m1', 'm2', 'm3']

# Définition des durées de chaque opération sur chaque machine
durations = {'p1_m1': 10, 'p1_m2': 5, 'p1_m3': 30,
             'p2_m1': 20, 'p2_m2': 25, 'p2_m3': 5,
             'p3_m1': 14, 'p3_m2': 15, 'p3_m3': 2}

# Création du problème
problem = Problem()

# Ajout des variables de décision pour chaque opération
for piece in pieces:
    for machine in machines:
        problem.addVariables([f'{piece}_{machine}'], range(100))

# Ajout des contraintes pour chaque opération
for piece in pieces:
    for i, machine in enumerate(machines):
        if i == 0:
            # Première machine de la pièce
            problem.addConstraint(lambda x: x == durations[f'{piece}_{machine}'], [f'{piece}_{machine}'])
        else:
            # Machines suivantes de la pièce
            previous_machine = machines[i-1]
            problem.addConstraint(lambda x, y: y - x == durations[f'{piece}_{machine}'], 
                                  [f'{piece}_{previous_machine}', f'{piece}_{machine}'])

# Ajout des contraintes pour chaque machine
for machine in machines:
    # On récupère les variables associées aux pièces pour cette machine
    machine_pieces = [f'{piece}_{machine}' for piece in pieces]
    # On ajoute la contrainte AllDifferent
    problem.addConstraint(AllDifferentConstraint(), machine_pieces)

# Ajout de la contrainte d'ordre des machines pour chaque pièce
for piece in pieces:
    # On ajoute la contrainte d'ordre des machines
    # On récupère les variables associées à cette pièce
    piece_variables = [f'{piece}_{machine}' for machine in machines]
    # On ajoute la contrainte d'ordre des machines
    problem.addConstraint(lambda x, y, z: x < y < z, piece_variables)
    # On ajoute la contrainte d'ordre de passage m1 -> m3 -> m2 pour cette pièce
    problem.addConstraint(lambda x, y, z: x < z < y, [f'{piece}_m1', f'{piece}_m3', f'{piece}_m2'])

# Résolution du problème
solutions = problem.getSolutions()
print(solutions)