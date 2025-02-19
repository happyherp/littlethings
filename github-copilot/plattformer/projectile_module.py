import pygame
import math

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
