import pygame
import math
from constants import SCREEN_WIDTH, SCREEN_HEIGHT, TILE_SIZE

class Projectile:
    def __init__(self, x, y, target_x, target_y):
        self.image = pygame.Surface((10, 10))
        self.image.fill((0, 255, 0))  # Fill the projectile with green color
        self.rect = self.image.get_rect()
        self.rect.center = (x, y)
        self.speed = 10
        angle = math.atan2(target_y - y, target_x - x)
        self.dx = math.cos(angle) * self.speed
        self.dy = math.sin(angle) * self.speed

    def update(self):
        self.rect.x += self.dx
        self.rect.y += self.dy

    def draw(self, screen, camera_x, camera_y):
        screen.blit(self.image, (self.rect.x - camera_x, self.rect.y - camera_y))

    def check_collision(self, enemies):
        for enemy in enemies:
            if self.rect.colliderect(enemy.rect):
                enemies.remove(enemy)
                return True
        return False

class Player:
    def __init__(self, x, y):
        self.image = pygame.Surface((50, 50))
        self.image.fill((255, 0, 0))  # Fill the player with red color
        self.rect = self.image.get_rect()
        self.rect.topleft = (x, y)
        self.speed = 5
        self.projectiles = []
        self.camera_x = 0
        self.camera_y = 0
        self.velocity_x = 0
        self.velocity_y = 0

    def update(self, game_map):
        keys = pygame.key.get_pressed()
        self.velocity_x = 0
        self.velocity_y = 0
        if keys[pygame.K_w]:
            self.velocity_y = -self.speed
        if keys[pygame.K_s]:
            self.velocity_y = self.speed
        if keys[pygame.K_a]:
            self.velocity_x = -self.speed
        if keys[pygame.K_d]:
            self.velocity_x = self.speed

        self.rect.x += self.velocity_x
        if game_map.check_collision(self.rect):
            if self.velocity_x > 0:  # Moving right; hit the left side of the stone
                self.rect.right = (self.rect.right // TILE_SIZE) * TILE_SIZE
            elif self.velocity_x < 0:  # Moving left; hit the right side of the stone
                self.rect.left = (self.rect.left // TILE_SIZE + 1) * TILE_SIZE

        self.rect.y += self.velocity_y
        if game_map.check_collision(self.rect):
            if self.velocity_y > 0:  # Moving down; hit the top side of the stone
                self.rect.bottom = (self.rect.bottom // TILE_SIZE) * TILE_SIZE
            elif self.velocity_y < 0:  # Moving up; hit the bottom side of the stone
                self.rect.top = (self.rect.top // TILE_SIZE + 1) * TILE_SIZE

        self.camera_x = self.rect.centerx - SCREEN_WIDTH // 2
        self.camera_y = self.rect.centery - SCREEN_HEIGHT // 2
        for projectile in self.projectiles:
            projectile.update()

    def draw(self, screen):
        screen.blit(self.image, (self.rect.x - self.camera_x, self.rect.y - self.camera_y))
        for projectile in self.projectiles:
            projectile.draw(screen, self.camera_x, self.camera_y)

    def shoot(self, target_x, target_y):
        # Adjust target position based on camera position
        adjusted_target_x = target_x + self.camera_x
        adjusted_target_y = target_y + self.camera_y
        projectile = Projectile(self.rect.centerx, self.rect.centery, adjusted_target_x, adjusted_target_y)
        self.projectiles.append(projectile)
