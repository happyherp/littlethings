import pygame
import math
from constants import SCREEN_WIDTH, SCREEN_HEIGHT

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

    def update(self):
        keys = pygame.key.get_pressed()
        if keys[pygame.K_w]:
            self.rect.y -= self.speed
        if keys[pygame.K_s]:
            self.rect.y += self.speed
        if keys[pygame.K_a]:
            self.rect.x -= self.speed
        if keys[pygame.K_d]:
            self.rect.x += self.speed
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
