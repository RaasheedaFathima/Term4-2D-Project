from collections import defaultdict
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
    
class Graph:
    def __init__(self):
        # create an empty directed graph, represented by a dictionary
        #  Key = node u , List = nodes, v, such that (u,v) is an edge
        self.graph = defaultdict(set)
        self.nodes = set()

    # Function that adds an edge (u,v) to the graph
    # O(1)
    def addEdge(self, u, v):
        self.graph[u].add(v)
        self.nodes.add(u)
        self.nodes.add(v)

    # outputs the edges of all nodes in the graph
    #  O(m+n) m = #edges , n = #nodes
    def print(self):
        edges = []
        # for each node in graph
        for node in self.graph:
            # for each neighbour node of a single node
            for adj in self.graph[node]:
                # if edge exists
                edges.append((node, adj))
        return edges


# 2-CNF class
#  Class storing a boolean formula in Conjunctive Normal Form of literals where size of clauses is at most 2

class two_cnf:
    def __init__(self,formula):
        self.con = formula

    # adds a clause to the CNF
    # O(1) complexity
    def add_clause(self, clause):
        if len(clause) <= 2:
            self.con.append(clause)
        else:
            return "Cannot contain more than 2 literals!"

    # returns a set of all the variables in CNF formula
    def get_variables(self):
        vars = set()
        for clau in self.con:
            for literal in clau:
                vars.add(literal)
        return vars

    def print(self):
        print(self.con)


# removes all double negation from  formula
def double_negation(formula):
    return formula.replace((neg+neg), '')


# Function that performs Depth First Search on a directed graph O(|V|+|E|)
def dfs(Graph, alr_visit, stack, scc):
    for node in Graph.nodes:
        if node not in alr_visit:
            exp(Graph, alr_visit, node, stack, scc)


# DFS helper function that 'explores' as far as possible from a node
def exp(Graph, alr_visit, node, stack, scc):
    if node not in alr_visit:
        alr_visit.append(node)
        for neighbour in Graph.graph[node]:
            #keep doing it recursively till all the surrounding nodes have been explored
            exp(Graph, alr_visit, neighbour, stack, scc)
        stack.append(node)
        scc.append(node)
    return alr_visit


# generates the transpose of a given directed graph O(|V|+|E|)

def transposing(d_graph):
    transpose = Graph()
    # for each node in graph
    for node in d_graph.graph:
        # for each neighbour node of a single node
        for adj in d_graph.graph[node]:
            transpose.addEdge(adj, node)
    return transpose


# finds all the strongly connected components in a given graph
# Implement Kosaraju’s algorithm
# O(|V|+|E|) for a directed graph G=(V,E)
# INPUT : directed graph, G
# OUTPUT: list of lists containing the strongly connected components of G
def s_c_c(Graph):
    stack = []
    sccs = []
    dfs(Graph, [], stack, [])
    t_g = transposing(Graph)
    alr_visit = []
    while stack:
        node = stack.pop()
        if node not in alr_visit:
            scc = []
            scc.append(node)
            exp(t_g, alr_visit, node, [], scc)
            sccs.append(scc)
    return sccs


# finds a contradiction in a list of strong connected components
def find_contradiction(sccs):
    for component in sccs:
        for lit in component:
            for other_literal in component[component.index(lit):]:
                if other_literal == double_negation(neg + lit):
                    return True
    return False


#  determines if a given 2-CNF is Satisfiable or not
def two_sat_solver(two_cnf_formula):
    # setup the edges of the graph
    uniqueliterals=[]
    solutionstring=""
    graph = Graph()
    for clause in two_cnf_formula.con:
        if len(clause) == 2:
            u = clause[0]
            v = clause[1]
            graph.addEdge(double_negation(neg+str(u)), str(v))
            graph.addEdge(double_negation(neg+str(v)), str(u))
        else:
            graph.addEdge(double_negation(neg+str(clause[0])), str(clause[0]))
    kontra = find_contradiction(s_c_c(graph))
    if kontra == False:
        print("SATISFIABLE")
        for clauses in s_c_c(graph):
            for literal in clauses:
                if int(literal) not in uniqueliterals and -(int(literal)) not in uniqueliterals:
                    uniqueliterals.append(int(literal))
        uniqueliterals = sorted(uniqueliterals, key=abs)
        for literal in uniqueliterals:
            if str(literal)[0]==neg:
                solutionstring += "1 "
            else:
                solutionstring += "0 "
        print(solutionstring)                               
    else:
        print("UNSATISFIABLE")
        
def readFile(filename):
    f = open(filename, "r")
    formula = []
    for line in f:
        if line[0] == "c":
            pass
        elif line[0] == "p":
            x = line.split()
            is_cnf = x[1]
            no_of_lit = int(x[2])
            no_of_clauses = int(x[3])
        else:
            templit = []
            for x in line.split():
                if int(x) != 0:
                    templit.append(int(x))
            formula.append(templit)
    for clause in formula:
        if len(clause)==0:
            formula.remove(clause)
    if len(formula[len(formula)-1])==0:
        formula.remove(formula[len(formula)-1])
    f.close
    if is_cnf =="cnf":
        return (formula)
    else:
        print("Invalid CNF File")
        return ("EXIT")
#test cases
#use the usual OOP
filename = "algorithmtestC.cnf"
formula = readFile(filename)
if formula == "EXIT":
    sys.exit("File not in CNF Format")
else:
    formula2 = two_cnf(formula)
    two_sat_solver(formula2)