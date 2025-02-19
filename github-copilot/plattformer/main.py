import pygame
import sys
from constants import FPS
from game_state import GameState

# Initialize Pygame
pygame.init()

# Create a game state instance
game_state = GameState()

# Main game loop
while True:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            pygame.quit()
            sys.exit()
        elif event.type == pygame.MOUSEBUTTONDOWN:
            if event.button == 1:  # Left mouse button
                target_x, target_y = event.pos
                game_state.player.shoot(target_x, target_y)

    # Update the game state
    game_state.update()

    # Draw everything
    game_state.draw()

    # Cap the frame rate
    game_state.clock.tick(FPS)
