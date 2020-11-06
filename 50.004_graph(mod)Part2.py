from collections import defaultdict
import random
import math
import sys
neg = '-'
class Clause:
    def __init__(self, lit1, lit2):
        self.lit1 = lit1
        self.lit2 = lit2

    def clauseSatisfying(self, boollist):
        if self.lit1 < 0:
            bool1 = not (boollist[abs(self.lit1)])
        else:
            bool1 = boollist[self.lit1]

        if self.lit2 < 0:
            bool2 = not (boollist[abs(self.lit2)])
        else:
            bool2 = boollist[self.lit2]

        return bool1 or bool2

def solveFile(filename):
    file = open(filename, "r")
    clauses = []
    for line in file:
        if line[0] == "c":
            pass
        elif line[0] == "p":
            x = line.split()
            is_cnf = x[1]
            no_of_lit = int(x[2])
            no_of_clauses = int(x[3])
        else:
            clauseLine = line.split()
            if len(clauseLine)>0:
                clause = Clause(lit1=int(clauseLine[0]),lit2=int(clauseLine[1]))
                clauses.append(clause)
    file.close
    if is_cnf =="cnf":
        return (clauses, no_of_lit, no_of_clauses)
    else:
        print("Invalid CNF File")
        return ("EXIT")


def findSolution(clauses,no_of_lit,no_of_clauses):
    satisfiability = False
    randomanswers=[]
    for i in range(1,no_of_clauses+1):
        i = random.random()
        if i > 0.5:
            randomanswers.append(True)
        else:
            randomanswers.append(False)
            
    for n in range(int(math.log(no_of_clauses,2))):
        for m in range(2*(no_of_clauses**2)):
            clauses_all_satisfy = True
            no_satisfy_clauses = 0
            for clause in clauses:
                if clause.clauseSatisfying(randomanswers) == False:
                    clauses_all_satisfy = False
                    chooseRandomLit = random.choices([clause.lit1, clause.lit2])[0]
                    randomanswers[abs(chooseRandomLit)] = not(randomanswers[abs(chooseRandomLit)])
                    break
                else:
                    no_satisfy_clauses += 1
            if clauses_all_satisfy == True:
                satisfiability = True
                break
        if satisfiability == True:
            print("SATISFIABLE")
            solutionstring = ""
            for i in range(1, no_of_lit + 1):
                if randomanswers[i] == False:
                    solutionstring += "0 "
                else:
                    solutionstring += "1 "
            print(solutionstring)
            break
    if satisfiability == False:
            print("UNSATISFIABLE")
            solutionstring = "No solution for 2 literals problem"
            print(solutionstring)

#test cases
#use the usual OOP
filename = "algorithmtest.cnf"
solutionParsing = solveFile(filename)
if solutionParsing == "EXIT":
    sys.exit("File not in CNF Format")
else:
    findSolution(solutionParsing[0],solutionParsing[1],solutionParsing[2])
