import stackless

def foo(ch,i):
  #print "running", i
  ch.send(ch.receive()+i)
  #print "done", i


stackless.run()

channels = []

for i in range(10**6):
  ch = stackless.channel()
  stackless.tasklet(foo)(ch,i)
  channels.append(ch)

for ch in channels:
  ch.send(i)
  res = ch.receive()
  #print res
  
