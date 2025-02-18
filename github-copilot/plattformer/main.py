import pygame
import sys
from player_module import Player

# Initialize Pygame
pygame.init()

# Constants
SCREEN_WIDTH, SCREEN_HEIGHT = 800, 600
FPS = 60

# Create the screen
screen = pygame.display.set_mode((SCREEN_WIDTH, SCREEN_HEIGHT))
pygame.display.set_caption("2D Platformer")

# Create a clock object to control the frame rate
clock = pygame.time.Clock()

# Create a player instance
player = Player(SCREEN_WIDTH // 2, SCREEN_HEIGHT // 2)

# Main game loop
while True:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            pygame.quit()
            sys.exit()
        elif event.type == pygame.MOUSEBUTTONDOWN:
            if event.button == 1:  # Left mouse button
                target_x, target_y = event.pos
                player.shoot(target_x, target_y)

    # Update the player
    player.update()

    # Draw everything
    screen.fill((0, 0, 0))  # Fill the screen with black
    player.draw(screen)
    pygame.display.flip()

    # Cap the frame rate
    clock.tick(FPS)
