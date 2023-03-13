from constraint import *

problem = Problem()
size = 8
cols = range(size)
rows = range(size)
problem.addVariables(cols, rows)
for col1 in cols:
    for col2 in cols:
        if col1 < col2:
            problem.addConstraint(lambda row1, row2, col1=col1, col2=col2:
                                    abs(row1-row2) != abs(col1-col2) and
                                    row1 != row2, (col1, col2))
sol=problem.getSolutions()
print(sol)