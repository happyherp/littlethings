from util import * 
from pydantic import BaseModel, Field
from typing import Optional
from describe import describe
import sys, os, subprocess

goal = sys.argv[1]
client = createClient()


class ResponseContent(BaseModel):
    plan:str = Field(description="Description of how you intend to change the code solve the goal")
    pythonCode:Optional[str] = Field(description="The whole python code of the programm")
    done:bool = Field(description="The program is done and works correctly as verified by the output", default = False)
    
    
CODE_PATH = "./tmp/code.py" 
def runCode(code):
    os.makedirs("tmp", exist_ok=True)

    with open(CODE_PATH, "w") as file:
        file.write(code)
       
    result = subprocess.run("python3 "+CODE_PATH, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True, shell=True)

    output = result.stdout
    errors = result.stderr
    print("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<OUTPUT:\n", output)
    
    responseContent = "Return Code: "+str(result.returncode)
    responseContent += "\nstdout:\n"+output+"\n"
    if errors != "":
        print("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<Errors:\n", errors)
        responseContent += "stderr:\n"+errors+"\n "
    return responseContent    
    

mainPrompt="""
Create a python3 programm. 

The programm should: {goal}

After you submit the programm it will be executed and its 
output read back to you. 

{schema}


""".format(goal=goal, schema=describe(ResponseContent))

class Iteration(BaseModel):
    ai:ResponseContent
    userinput:str
    programOutput:Optional[str] = None

def main():
    iterations = []
    
    def buildPrompt():
        messages = [systemMsg("You are a helpful assistant."), userMsg(mainPrompt)]  
        if iterations:
            lastRun = iterations[-1]
            lastRunMessage = "The current Code: \n```python\n"
            lastRunMessage += lastRun.ai.pythonCode
            lastRunMessage += "\n```\n"
            lastRunMessage += "Output: \n"+lastRun.programOutput
            if (lastRun.userinput != ""):
                lastRunMessage += "Message from the user: "+lastRun.userinput
            messages.append(userMsg(lastRunMessage))
        #print("messages", messages)
        return messages
        
    done = False
    while not done:
        response = client.chat.completions.create(
            model="gpt-4o", messages=buildPrompt(), 
            response_format={ "type": "json_object" }
        )

        obj = ResponseContent.parse_raw(response.choices[0].message.content)
        print("PLAN>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        print(obj.plan)
        print("\nCODE >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n", obj.pythonCode)
        print("Done", obj.done)
        userinput = input("continue?(no, new command)")
        if userinput == "no": break
        
        if (obj.done and userinput == ""): break

        programResponse = runCode(obj.pythonCode)
        
        iterations.append(Iteration(ai=obj, userinput=userinput, programOutput=programResponse))

main()