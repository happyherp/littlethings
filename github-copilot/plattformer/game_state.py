import pygame
import random
import sys
from player_module import Player
from enemy_module import Enemy
from map_module import Map
from weapon_module import Rifle, Pistol, Shotgun
from constants import SCREEN_WIDTH, SCREEN_HEIGHT, TILE_SIZE, MIN_SPAWN_DISTANCE

class GameState:
    def __init__(self, time_provider=None, random_instance=None):
        # Optional dependency injections for testing
        self.time_provider = time_provider if time_provider else pygame.time.get_ticks
        self.random = random_instance if random_instance else random

        self.screen = pygame.display.set_mode((SCREEN_WIDTH, SCREEN_HEIGHT))
        pygame.display.set_caption("2D Platformer")
        self.clock = pygame.time.Clock()
        self.player = Player(SCREEN_WIDTH // 2, SCREEN_HEIGHT // 2)
        # Pass the random instance to Map
        self.game_map = Map(100, 100, 0.2, random_instance=self.random)
        self.enemies = []
        self.weapons = self.spawn_weapons()
        self.enemy_spawn_time = 3000  # Initial spawn time in milliseconds
        self.last_spawn_time = self.time_provider()
        self.grass_image = pygame.image.load('img/grass_100x100.png').convert()
        self.rock_image = pygame.image.load('img/rock_50x50.png').convert()

    def spawn_weapons(self):
        weapons = []
        for _ in range(100):
            while True:
                x = self.random.randint(0, self.game_map.width * TILE_SIZE)
                y = self.random.randint(0, self.game_map.height * TILE_SIZE)
                weapon_type = self.random.choice([Rifle, Pistol, Shotgun])
                weapon = weapon_type()
                weapon.rect = pygame.Rect(x, y, 20, 20)
                if not self.game_map.check_collision(weapon.rect):
                    weapons.append(weapon)
                    break
        return weapons

    def update(self):
        current_time = self.time_provider()
        if current_time - self.last_spawn_time > self.enemy_spawn_time:
            # Spawn a new enemy
            while True:
                enemy_x = self.random.randint(0, self.game_map.width * TILE_SIZE)
                enemy_y = self.random.randint(0, self.game_map.height * TILE_SIZE)
                enemy = Enemy(enemy_x, enemy_y)
                if abs(enemy_x - self.player.rect.x) > MIN_SPAWN_DISTANCE and abs(enemy_y - self.player.rect.y) > MIN_SPAWN_DISTANCE and not self.game_map.check_collision(enemy.rect):
                    break
            self.enemies.append(enemy)
            self.last_spawn_time = current_time
            self.enemy_spawn_time = max(50, self.enemy_spawn_time * 0.9)  # Decrease spawn time

        self.player.update(self.game_map, self.weapons)
        for enemy in self.enemies:
            enemy.update(self.player.rect.centerx, self.player.rect.centery, self.game_map)
            if enemy.rect.colliderect(self.player.rect):
                self.game_over()
        for projectile in self.player.projectiles[:]:
            if projectile.check_collision(self.enemies) or self.game_map.check_collision(projectile.rect):
                self.player.projectiles.remove(projectile)

    def draw(self):
        self.screen.fill((0, 0, 0))  # Fill the screen with black
        self.draw_background()
        self.draw_map()
        self.player.draw(self.screen)
        self.draw_weapons()
        for enemy in self.enemies:
            enemy.draw(self.screen, self.player.camera_x, self.player.camera_y)
        for projectile in self.player.projectiles:
            projectile.draw(self.screen, self.player.camera_x, self.player.camera_y)
        pygame.display.flip()

    def draw_background(self):
        tile_width = self.grass_image.get_width()
        tile_height = self.grass_image.get_height()
        start_x = max(-self.player.camera_x % tile_width - tile_width, -self.player.camera_x)
        start_y = max(-self.player.camera_y % tile_height - tile_height, -self.player.camera_y)
        end_x = min(SCREEN_WIDTH, self.game_map.width * TILE_SIZE - self.player.camera_x)
        end_y = min(SCREEN_HEIGHT, self.game_map.height * TILE_SIZE - self.player.camera_y)

        for x in range(start_x, end_x, tile_width):
            for y in range(start_y, end_y, tile_height):
                self.screen.blit(self.grass_image, (x, y))

    def draw_map(self):
        for y, row in enumerate(self.game_map.tiles):
            for x, tile in enumerate(row):
                if tile == 1:  # Stone
                    self.screen.blit(self.rock_image, (x * TILE_SIZE - self.player.camera_x, y * TILE_SIZE - self.player.camera_y))

    def draw_weapons(self):
        font = pygame.font.Font(None, 24)
        for weapon in self.weapons:
            text = font.render(weapon.name, True, (0, 0, 0))
            self.screen.blit(text, (weapon.rect.x - self.player.camera_x, weapon.rect.y - self.player.camera_y))

    def game_over(self):
        font = pygame.font.Font(None, 74)
        text = font.render("GAME OVER", True, (255, 0, 0))
        self.screen.blit(text, (SCREEN_WIDTH // 2 - text.get_width() // 2, SCREEN_HEIGHT // 2 - text.get_height() // 2))
        pygame.display.flip()
        pygame.time.wait(2000)
        pygame.quit()
        sys.exit()
