from functools import lru_cache

def run(path):
  f = open(path)
  T = int(readline(f))
  for t in range(1,T+1):
    N = int(readline(f))
    numbers = map(int, readline(f).split(" "))
    result = isGoodNumber(N,numbers)
    print("Case #%d: %s"%(t, result))
  
def readline(f):
    return f.readline().rstrip("\n")
    
def isGoodNumber(n,numbers):
  goodP = (1.0/n)**n
  badP = 1.0  
  badmatrix = createMatrix(n)
  pos = 0
  for number in numbers:
    badP *= badmatrix[number][pos]
    pos += 1
    
  print(goodP, badP)
  if goodP >= badP: return "GOOD" 
  return "BAD"
    
@lru_cache(maxsize=None)
def createMatrix(n):
    
    switchchance = 1.0/n

    matrix = []
    for x in range(n):
      row = []
      for y in range(n):
        if x == y:
          row.append(1.0)
        else: row.append(0.0)
      matrix.append(row)
    
    for source in range(n):   
      new_matrix = list([list([[] for x in range(n)]) for y in range(n)])
      for number in range(n):
        for target in range(n):
          if target == source:
            new_matrix[number][target] = 0
            for othersource in range(n):
              new_matrix[number][target] += matrix[number][othersource] * switchchance

          else:
            new_matrix[number][target] = matrix[number][target] * (1.0-switchchance) \
                                       + matrix[number][source] * switchchance
      #print(new_matrix)
      matrix = new_matrix
      
    return matrix

print(isGoodNumber(3, [0,0,0]))
    
#run("sample.in")
      
      
      