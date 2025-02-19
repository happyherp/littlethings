import unittest
import pygame
from projectile_module import Projectile

class MockEnemy:
    def __init__(self, x, y, width, height):
        self.rect = pygame.Rect(x, y, width, height)

class TestProjectile(unittest.TestCase):
    def test_update(self):
        projectile = Projectile(0, 0, 100, 0)
        projectile.update()
        self.assertEqual(projectile.rect.centerx, 10)
        self.assertEqual(projectile.rect.centery, 0)

        projectile = Projectile(0, 0, 0, 100)
        projectile.update()
        self.assertEqual(projectile.rect.centerx, 0)
        self.assertEqual(projectile.rect.centery, 10)

    def test_check_collision(self):
        projectile = Projectile(0, 0, 10, 0)
        enemy = MockEnemy(5, 0, 50, 50)
        enemies = [enemy]
        projectile.update()  # Move the projectile to collide with the enemy
        self.assertTrue(projectile.check_collision(enemies))
        self.assertEqual(len(enemies), 0)

        enemy = MockEnemy(100, 100, 50, 50)
        enemies = [enemy]
        self.assertFalse(projectile.check_collision(enemies))
        self.assertEqual(len(enemies), 1)

if __name__ == '__main__':
    unittest.main()
