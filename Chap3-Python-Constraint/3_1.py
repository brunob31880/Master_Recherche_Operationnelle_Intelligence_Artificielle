from constraint import *
problem=Problem()
problem.addVariable("x1", [1,2,3])
problem.addVariable("x2", [1,2,3])
problem.addVariable("x3", [1,2,3])
problem.addConstraint(lambda x1,x2,x3 : x1+x2==x3 ,("x1","x2","x3"))
sol=problem.getSolutions()
print(sol)
