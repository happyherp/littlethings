import pygame
import sys
import random
from player_module import Player
from enemy_module import Enemy
from constants import SCREEN_WIDTH, SCREEN_HEIGHT, FPS

# Initialize Pygame
pygame.init()
# Create the screen
screen = pygame.display.set_mode((SCREEN_WIDTH, SCREEN_HEIGHT))
pygame.display.set_caption("2D Platformer")

# Create a clock object to control the frame rate
clock = pygame.time.Clock()

# Create a player instance
player = Player(SCREEN_WIDTH // 2, SCREEN_HEIGHT // 2)

# Create a list to hold enemies
enemies = []
enemy_spawn_time = 3000  # Initial spawn time in milliseconds
last_spawn_time = pygame.time.get_ticks()

# Main game loop
while True:
    current_time = pygame.time.get_ticks()
    if current_time - last_spawn_time > enemy_spawn_time:
        # Spawn a new enemy
        while True:
            enemy_x = random.randint(0, SCREEN_WIDTH)
            enemy_y = random.randint(0, SCREEN_HEIGHT)
            if abs(enemy_x - player.rect.x) > 100 and abs(enemy_y - player.rect.y) > 100:
                break
        enemies.append(Enemy(enemy_x, enemy_y))
        last_spawn_time = current_time
        enemy_spawn_time = max(50, enemy_spawn_time * 0.9)  # Decrease spawn time

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

    # Check for projectile collisions with enemies
    for projectile in player.projectiles[:]:
        if projectile.check_collision(enemies):
            player.projectiles.remove(projectile)

    # Update enemies
    for enemy in enemies:
        enemy.update(player.rect.centerx, player.rect.centery)
        if enemy.rect.colliderect(player.rect):
            # Game over
            font = pygame.font.Font(None, 74)
            text = font.render("GAME OVER", True, (255, 0, 0))
            screen.blit(text, (SCREEN_WIDTH // 2 - text.get_width() // 2, SCREEN_HEIGHT // 2 - text.get_height() // 2))
            pygame.display.flip()
            pygame.time.wait(2000)
            pygame.quit()
            sys.exit()

    # Draw everything
    screen.fill((0, 0, 0))  # Fill the screen with black
    player.draw(screen)
    for enemy in enemies:
        enemy.draw(screen, player.camera_x, player.camera_y)
    for projectile in player.projectiles:
        projectile.draw(screen, player.camera_x, player.camera_y)
    pygame.display.flip()

    # Cap the frame rate
    clock.tick(FPS)
