import pygame
import math

class Enemy:
    def __init__(self, x, y):
        self.image = pygame.Surface((40, 40))
        self.image.fill((0, 0, 255))  # Fill the enemy with blue color
        self.rect = self.image.get_rect()
        self.rect.topleft = (x, y)
        self.speed = 2

    def update(self, target_x, target_y, game_map):
        angle = math.atan2(target_y - self.rect.y, target_x - self.rect.x)
        self.velocity_x = math.cos(angle) * self.speed
        self.velocity_y = math.sin(angle) * self.speed
        self.rect.x += self.velocity_x
        self.rect.y += self.velocity_y

        # Check for collision with map
        if game_map.check_collision(self.rect):
            self.rect.x -= self.velocity_x
            self.rect.y -= self.velocity_y

    def draw(self, screen, camera_x, camera_y):
        screen.blit(self.image, (self.rect.x - camera_x, self.rect.y - camera_y))
