from openai import OpenAI
client = OpenAI()

audio_file = open("speech.m4a", "rb")
transcript = client.audio.transcriptions.create(
  model="whisper-1", 
  file=audio_file, 
  language="de",
  prompt="ChatGPT, Whisper-1",
  response_format="text"
)

print(transcript)