import stackless

def Sending(channel):
    print "sending"
    channel.send("foo")

def Receiving(channel):
    print "receiving"
    print channel.receive()

ch = stackless.channel()

task = stackless.tasklet(Sending)(ch)
task2 = stackless.tasklet(Receiving)(ch)

stackless.run()
