from dotenv import load_dotenv
import os
import nextcord
from nextcord.ext import commands

# Load the .env file
load_dotenv()

# Access the API key
token = os.getenv('DISCORD_BOT_TOKEN')

intents = nextcord.Intents.default()
intents.messages = True
#intents.message_content = True

# Create bot instance
bot = commands.Bot(command_prefix='!', intents=intents)

@bot.event
async def on_ready():
    print(f'Logged in as {bot.user.name}')

@bot.command()
async def send_midjourney_command(ctx):
    # Replace with the actual command you want to send to Midjourney
    command = "/imagine something amazing"
    await ctx.send(command)
    

# Run the bot
bot.run(token)