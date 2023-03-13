from constraint import *
problem=Problem()
problem.addVariable("x", [1,2,3])
problem.addVariable("y", range(10+1))
def our_constraint(x,y):
    if x+y>=5:
        return True
problem.addConstraint(our_constraint ,("x","y"))
sols=problem.getSolutions()
nb=len(sols)
for i in range(nb):
    sol=sols[i]
    print(sol)
