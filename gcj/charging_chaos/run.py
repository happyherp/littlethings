
def parseFile(f):
    T = int(readline(f))
    for t in range(T):
        n, l = map(int, readline(f).split(" "))
        outlets = readline(f).split(" ")
        devices = readline(f).split(" ")
        result = solve(devices, outlets, ["" for x in outlets], 0)
        print("Case #"+str(t+1)+": "+str(result))
        
        
def readline(f):
    return f.readline().rstrip("\n")
        
        
def solve(currents, switches_unset, switches_set, y):
  #check if problem can still be solved
  #print(currents, switches_unset, switches_set)
  
  for current in currents:
    found = False
    for switch in switches_set:
      if current.startswith(switch):
        found = True
    if not found:
      #print("stop")
      return "NOT POSSIBLE"
      
  if len(switches_unset[0]) == 0:
    return y
    
  set1 = []
  set2 = []
  for (u, s) in zip(switches_unset, switches_set):
    set1.append(s + u[0])
    set2.append(s + switchIt(u[0]))
    
  
  new_unset  = list(map(lambda switch:switch[1:], switches_unset))
    
  y1 = solve(currents, new_unset, set1, y)
  y2 = solve(currents, new_unset, set2, y+1)
  if (y1 == "NOT POSSIBLE"): return y2
  if (y2 == "NOT POSSIBLE"): return y1
  return min(y1,y2)
  
  
def switchIt(c):
  if c == "0": return "1"
  return "0"
  
  
parseFile(open("A-large-practice.in"))