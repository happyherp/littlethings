from openai import OpenAI
from openai.types.chat import ChatCompletionMessage, ChatCompletionMessageToolCall
client = OpenAI()


tools = [
  {
    "type": "function",
    "function": {
      "name": "register_user",
      "description": "Register a new user on the site. ",
      "parameters": {
        "type": "object",
        "properties": {
          "username": {
            "type": "string",
            "description": "The username the user will use to log on.",
          },
          "password": {
            "type": "string",
            "description": "the password the user uses to log in.",
          },  
          "email": {
            "type": "string",
            "description": "The email of the user. Must be a valid email address.",
          },
          "phone": {
            "type": "string",
            "description": "The users phone number",
          },        },
        "required": ["username, password, email"],
      },
    }
  } 
]


completion = client.chat.completions.create(
  model="gpt-4",
  messages=[
    {"role": "system", "content": open("prompts/registration.txt", 'r').read()},
    {"role": "user", "content": """
    Hi. I would like to register. My Email is carlos@freund.de . 
    """},
    ChatCompletionMessage(role='assistant',
        content="Great! Let's start with creating your account. I will first need you to choose a username and password for your account. Please note that you will need to provide the password twice to ensure it is correct. Also, do you have a phone number we could use for your account?"),
    {"role": "user", "content": "My password will be 'horse'. My phone number i would like to keep private."},
    {"role": "assistant", "content": "Thank you for the information. Could you please confirm your password by typing it again?"},
    {"role": "user", "content": "horse"},
    ChatCompletionMessage(content="blub", role='assistant',
        tool_calls=[ChatCompletionMessageToolCall(
                        id='call_ipq5hJjHhFtAsYBFe55piLb0', 
                        function={"arguments":'{\n"username": "carlos",\n"password": "horse",\n"email": "carlos@freund.de"\n}', "name":'register_user'}, 
                        type='function')]),
    {"role": "tool", "content": "Username already taken.", "name":"register_user",
        "tool_call_id" : "call_ipq5hJjHhFtAsYBFe55piLb0"},
    {"role": "user", "content": "carlos_freund"},
    ChatCompletionMessage(content="blub", role='assistant',
        tool_calls=[ChatCompletionMessageToolCall(
                        id='call_CwMrcZZj06N2zEMvqZOSRhFO', 
                        function={"arguments":'{\n"username": "carlos_freund",\n"password": "horse",\n"email": "carlos@freund.de"\n}', "name":'register_user'}, 
                        type='function')]),
    {"role": "tool", "content": "Username successfully created.", "name":"register_user",
        "tool_call_id" : "call_CwMrcZZj06N2zEMvqZOSRhFO"},
  ],
  tools=tools,
  temperature=1.0
)

print(completion.choices[0].message)
