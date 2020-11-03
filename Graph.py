"""2SAT problem
Make the CNF true
for CNF value to be true, every clause (Eg.AvB) should be true.If at least one variable is strongly connected
and cyclic, then CNF is unsatisfiable.Both must lie in the same SCC
"""
"""
The below codes utilise the kosaraju algorithm
that performs 2 rounds of depth first search on the implication graphs.
It makes use of Strongly connected components and the fact that(A OR B) == (~A --> B).
If a literal and its negation exist in the same strongly connected component, the 2 - SAT is unsatisfiable.
Two searches of DFS performed in this algorithm
1. DFS1
- Construct a list of nodes in the order which the DFS processes them
- Conduct DFS on each unprocessed node.
- Each node is added to the List after processing.

2.DFS 2
- Form the strongly connect components
- Reverse every edge of the graph.
- Find the strongly connected components that do not have extra edges
- Go through list of nodes created from first search in reverse order.
if node doesn't belong to component, create new component
start and add the DFS to strongly connected component.
"""
from collections import defaultdict

#kosaraju algorithm

class Graph:
    #the graph is an adjacency matrix
    #use the constructor to create the graph
    def __init__(self,vert):
        self.V = vert #num vertices
        self.graph = defaultdict(list) #store the graph in the dictionary

    #add an edge to the graph
    def addedge(self,u, v):
        self.graph[u].append(v)

    #do the dfs
    def dfs(self,v,alr_visited_vert):
        #print the already visited node
        alr_visited_vert[v] = True
        print(v)
        #recur for all the vertices adjacent to the vertex
        for i in self.graph[v]:
            if alr_visited_vert[i] == False:
                # do a recursive call for all the vertices adjacent to this vertex
                self.dfs(i,alr_visited_vert)

    #represent the graph as an adjacency matrix
    #push the vertex to the stack
    def push_to_stack(self,v,visited_vert,stack):
        #Constant time complexity to create stack to store the vertex briefly visited
        stack = list()
        #mark current as visited
        visited_vert[v] = True
        #Do the recursive call for all the vertices adj to the vertex
        for i in self.graph[v]:
            if visited_vert[i] != True:
                # do a recursive call to visit the adjacent nodes
                self.push_to_stack(i,visited_vert,stack)
        stack.append(v)

    #must transpose the graph to give the scc for the second dfs
    def transpose(self):
        g = Graph(self.v)
        #recursive for all the vertices adjacent to the vertex
        for i in self.graph:
            for j in self.graph[i]:
                g.addedge(j,i)
        return g

    def findscc(self):
        stack = []
        #for first dfs
        visited = [False]*(self.V)
        for i in range(self.V):
            if visited[i] == False:
                self.push_to_stack(i, visited, stack)
                # Create a reversed graph
                gr = self.transpose()

                # Mark all the vertices as not visited (For second DFS)
                visited = [False] * (self.V)

                # Now process all vertices in order defined by Stack
                while stack:
                    i = stack.pop()
                    if visited[i] == False:
                        gr.dfs(i, visited)
                        print("")

#define the following to check if kosaraju satisfies the 2_Sat problem in polynomial time


