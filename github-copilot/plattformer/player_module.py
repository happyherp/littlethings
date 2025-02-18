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

    def draw(self, screen):
        screen.blit(self.image, self.rect.topleft)

class Player:
    def __init__(self, x, y):
        self.image = pygame.Surface((50, 50))
        self.image.fill((255, 0, 0))  # Fill the player with red color
        self.rect = self.image.get_rect()
        self.rect.topleft = (x, y)
        self.speed = 5
        self.projectiles = []

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
        for projectile in self.projectiles:
            projectile.update()

    def draw(self, screen):
        screen.blit(self.image, self.rect.topleft)
        for projectile in self.projectiles:
            projectile.draw(screen)

    def shoot(self, target_x, target_y):
        projectile = Projectile(self.rect.centerx, self.rect.centery, target_x, target_y)
        self.projectiles.append(projectile)
