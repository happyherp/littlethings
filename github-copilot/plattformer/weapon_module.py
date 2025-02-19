import pygame
import math
from projectile_module import Projectile

class Weapon:
    def __init__(self, name, fire_rate):
        self.name = name
        self.fire_rate = fire_rate
        self.last_shot_time = 0

    def shoot(self, player, target_x, target_y):
        current_time = pygame.time.get_ticks()
        if current_time - self.last_shot_time >= self.fire_rate:
            self.last_shot_time = current_time
            self._fire(player, target_x, target_y)

    def _fire(self, player, target_x, target_y):
        raise NotImplementedError

class Rifle(Weapon):
    def __init__(self):
        super().__init__("Rifle", 200)

    def _fire(self, player, target_x, target_y):
        projectile = Projectile(player.rect.centerx, player.rect.centery, target_x, target_y)
        player.projectiles.append(projectile)

class Pistol(Weapon):
    def __init__(self):
        super().__init__("Pistol", 500)

    def _fire(self, player, target_x, target_y):
        projectile = Projectile(player.rect.centerx, player.rect.centery, target_x, target_y)
        player.projectiles.append(projectile)

class Shotgun(Weapon):
    def __init__(self):
        super().__init__("Shotgun", 1000)

    def _fire(self, player, target_x, target_y):
        for angle_offset in [-0.2, -0.1, 0, 0.1, 0.2]:
            angle = math.atan2(target_y - player.rect.centery, target_x - player.rect.centerx) + angle_offset
            dx = math.cos(angle) * 10
            dy = math.sin(angle) * 10
            projectile = Projectile(player.rect.centerx, player.rect.centery, player.rect.centerx + dx, player.rect.centery + dy)
            player.projectiles.append(projectile)
