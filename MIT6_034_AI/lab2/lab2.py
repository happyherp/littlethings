# Fall 2012 6.034 Lab 2: Search
#
# Your answers for the true and false questions will be in the following form.  
# Your answers will look like one of the two below:
#ANSWER1 = True
#ANSWER1 = False

# 1: True or false - Hill Climbing search is guaranteed to find a solution
#    if there is a solution
ANSWER1 = False

# 2: True or false - Best-first search will give an optimal search result
#    (shortest path length).
#    (If you don't know what we mean by best-first search, refer to
#     http://courses.csail.mit.edu/6.034f/ai3/ch4.pdf (page 13 of the pdf).)
ANSWER2 = False

# 3: True or false - Best-first search and hill climbing make use of
#    heuristic values of nodes.
ANSWER3 = True

# 4: True or false - A* uses an extended-nodes set.
ANSWER4 = True

# 5: True or false - Breadth first search is guaranteed to return a path
#    with the shortest number of nodes.
ANSWER5 = True

# 6: True or false - The regular branch and bound uses heuristic values
#    to speed up the search for an optimal path.
ANSWER6 = False

# Import the Graph data structure from 'search.py'
# Refer to search.py for documentation
from search import Graph

## Optional Warm-up: BFS and DFS
# If you implement these, the offline tester will test them.
# If you don't, it won't.
# The online tester will not test them.

def generic_search(insertion_method, next_node_sort = lambda x,y,z:x, use_extended=True):

    def search(graph, start, goal):
        agenda = [[start]]
        extended_set = [start]
        while len(agenda) > 0:
            #print "Agenda: ", agenda
            path = agenda[0]
            if path[-1] == goal:
                return path
            del agenda[0]
            nextnodes = graph.get_connected_nodes(path[-1])
            next_node_sort(nextnodes, goal, graph)
            for nextnode in nextnodes:
                if use_extended and (not nextnode in extended_set) \
                or not use_extended and (not nextnode in path):
                    insertion_method(agenda, path+[nextnode])
                    extended_set.append(nextnode)
        return []
    return search


def insertLast(agenda, path):
    agenda.append(path)

bfs = generic_search(insertLast)
    

## Once you have completed the breadth-first search,
## this part should be very simple to complete.

def insertFront(agenda, path):
    agenda.insert(0,path)

dfs = generic_search(insertFront)


## Now we're going to add some heuristics into the search.  
## Remember that hill-climbing is a modified version of depth-first search.
## Search direction should be towards lower heuristic values to the goal.

def sortNodes(nodes, goal, graph):
    #print "before", nodes
    nodes.sort(key=lambda n: graph.get_heuristic(n,goal), reverse=True)
    #print "after", nodes

hill_climbing = generic_search(insertFront, sortNodes, use_extended = False)

## Now we're going to implement beam search, a variation on BFS
## that caps the amount of memory used to store paths.  Remember,
## we maintain only k candidate paths of length n in our agenda at any time.
## The k top candidates are to be determined using the 
## graph get_heuristic function, with lower values being better values.
def beam_search(graph, start, goal, beam_width):
    def addAndCut(agenda, path):
        agenda.append(path)

        levels = {}
        for path in agenda:
            if not levels.has_key(len(path)):
                levels[len(path)] = []
            levels[len(path)].append(path)

        to_remove = []
        for paths in levels.values():
            paths.sort(key=lambda p: graph.get_heuristic(p[-1],goal))
            to_remove += paths[beam_width:]
            
        for path in to_remove:
            agenda.remove(path)
                    
    return generic_search(addAndCut, use_extended = False)(graph, start, goal)

## Now we're going to try optimal search.  The previous searches haven't
## used edge distances in the calculation.

## This function takes in a graph and a list of node names, and returns
## the sum of edge lengths along the path -- the total distance in the path.
def path_length(graph, node_names):
    if len(node_names) == 0:
        return 0
    sum = 0
    for a,b in zip(node_names, node_names[1:]):
        sum += graph.get_edge(a,b).length
    return sum    




def branch_and_bound(graph, start, goal):
    def sortByPathLength(agenda, path):
        agenda.append(path)
        agenda.sort(key=lambda p:path_length(graph, p))                                    
    return generic_search(sortByPathLength)(graph, start, goal)

def a_star(graph, start, goal):
    def sortByPathLengthAndHeuristic(agenda, path):
        agenda.append(path)
        agenda.sort(key=lambda p:path_length(graph, p)+graph.get_heuristic(p[-1],goal))                                    
    return generic_search(sortByPathLengthAndHeuristic)(graph, start, goal)

## It's useful to determine if a graph has a consistent and admissible
## heuristic.  You've seen graphs with heuristics that are
## admissible, but not consistent.  Have you seen any graphs that are
## consistent, but not admissible?

def is_admissible(graph, goal):
    for node in graph.nodes:
        distance = path_length(graph, branch_and_bound(graph, node, goal))
        heuristic = graph.get_heuristic(node, goal)
        if not heuristic <= distance:
            return False

    return True


def is_consistent(graph, goal):
    for edge in graph.edges:
        abs_dist_diff = abs(graph.get_heuristic(edge.node1, goal)
                            - graph.get_heuristic(edge.node2, goal))
        if not edge.length >= abs_dist_diff:
            return False
    return True

HOW_MANY_HOURS_THIS_PSET_TOOK = 2.5
WHAT_I_FOUND_INTERESTING = 'lotta'
WHAT_I_FOUND_BORING = 'little'
