import pygame
import sys
import random
from player_module import Player
from enemy_module import Enemy
from constants import SCREEN_WIDTH, SCREEN_HEIGHT, FPS, TILE_SIZE
from map_module import Map
from constants import MIN_SPAWN_DISTANCE

# Initialize Pygame
pygame.init()
# Create the screen
screen = pygame.display.set_mode((SCREEN_WIDTH, SCREEN_HEIGHT))
pygame.display.set_caption("2D Platformer")

# Load the grass image
grass_image = pygame.image.load('img/grass_100x100.png').convert()
# Load the rock image
rock_image = pygame.image.load('img/rock_50x50.png').convert()

def draw_background(screen, player):
    tile_width = grass_image.get_width()
    tile_height = grass_image.get_height()
    start_x = -player.camera_x % tile_width - tile_width
    start_y = -player.camera_y % tile_height - tile_height

    for x in range(start_x, SCREEN_WIDTH, tile_width):
        for y in range(start_y, SCREEN_HEIGHT, tile_height):
            screen.blit(grass_image, (x, y))

def draw_map(screen, game_map, player):
    for y, row in enumerate(game_map.tiles):
        for x, tile in enumerate(row):
            if tile == 1:  # Stone
                screen.blit(rock_image, (x * TILE_SIZE - player.camera_x, y * TILE_SIZE - player.camera_y))

# Create a clock object to control the frame rate
clock = pygame.time.Clock()

# Create a player instance
player = Player(SCREEN_WIDTH // 2, SCREEN_HEIGHT // 2)

# Create a map instance
game_map = Map(100, 100, 0.2)

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
            if abs(enemy_x - player.rect.x) > MIN_SPAWN_DISTANCE and abs(enemy_y - player.rect.y) > MIN_SPAWN_DISTANCE:
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
    player.update(game_map)

    # Check for projectile collisions with enemies and stones
    for projectile in player.projectiles[:]:
        if projectile.check_collision(enemies) or game_map.check_collision(projectile.rect):
            player.projectiles.remove(projectile)

    # Update enemies
    for enemy in enemies:
        enemy.update(player.rect.centerx, player.rect.centery, game_map)
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
    draw_background(screen, player)
    draw_map(screen, game_map, player)
    player.draw(screen)
    for enemy in enemies:
        enemy.draw(screen, player.camera_x, player.camera_y)
    for projectile in player.projectiles:
        projectile.draw(screen, player.camera_x, player.camera_y)
    pygame.display.flip()

    # Cap the frame rate
    clock.tick(FPS)
