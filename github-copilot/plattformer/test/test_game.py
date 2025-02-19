import unittest
import pygame
import sys
from enemy_module import Enemy
from projectile_module import Projectile
from game_state import GameState
from map_module import Map
from player_module import Player
from constants import SCREEN_WIDTH, SCREEN_HEIGHT, TILE_SIZE, MIN_SPAWN_DISTANCE
from weapon_module import Rifle, Pistol, Shotgun  # Newly imported for weapon tests

# Dummy time provider for controlled time progression
class DummyTime:
    def __init__(self, start=0):
        self.current = start
    def advance(self, ms):
        self.current += ms
    def __call__(self):
        return self.current

# Dummy random instance for predictability in tests
import random
class DummyRandom:
    def __init__(self, seed=0):
        self.rnd = random.Random(seed)
    def randint(self, a, b):
        return self.rnd.randint(a, b)
    def choice(self, seq):
        return self.rnd.choice(seq)

class TestGameLogic(unittest.TestCase):
    def setUp(self):
        pygame.init()
        self.dummy_time = DummyTime()
        self.dummy_random = DummyRandom(42)
        # Create a simple map without stones for predictable movement
        self.game_map = Map(20, 20, 0, random_instance=self.dummy_random)
    
    def tearDown(self):
        pygame.quit()

    def test_enemy_pathfinding(self):
        enemy = Enemy(0, 0)
        start = (enemy.rect.x // TILE_SIZE, enemy.rect.y // TILE_SIZE)
        goal = (5, 5)
        path = enemy.find_path(start, goal, self.game_map)
        self.assertTrue(len(path) > 0, "Enemy should find a path")
    
    def test_enemy_update_movement(self):
        enemy = Enemy(0, 0)
        # Set a simple path manually (simulate path computed externally)
        enemy.path = [(1, 0), (2, 0)]
        old_x = enemy.rect.x
        enemy.update(100, 100, self.game_map)
        self.assertNotEqual(enemy.rect.x, old_x, "Enemy should have moved along the path")

    
    def test_game_state_update(self):
        # Create a controlled GameState
        game_state = GameState(time_provider=self.dummy_time, random_instance=self.dummy_random)
        initial_enemy_count = len(game_state.enemies)
        # Advance time to force enemy spawn
        self.dummy_time.advance(game_state.enemy_spawn_time + 1)
        game_state.update()
        self.assertTrue(len(game_state.enemies) > initial_enemy_count, "An enemy should have been spawned")
    
    def test_player_weapon_pickup(self):
        # Create a player and a dummy weapon with a rect
        player = Player(100, 100)
        dummy_weapon = type("DummyWeapon", (), {})()
        dummy_weapon.rect = pygame.Rect(100, 100, 20, 20)
        dummy_weapon.name = "TestWeapon"
        weapons = [dummy_weapon]
        player.check_weapon_pickup(weapons)
        self.assertIsNotNone(player.weapon, "Player should have picked up the weapon")
        self.assertEqual(len(weapons), 0, "Weapon should be removed from the list after pickup")
    
    def test_map_stone_generation(self):
        # Create a map with a specified stone density
        width, height, density = 10, 10, 0.3
        dummy_random = DummyRandom(123)
        game_map = Map(width, height, density, random_instance=dummy_random)
        stone_count = sum(row.count(1) for row in game_map.tiles)
        expected = int(width * height * density)
        self.assertEqual(stone_count, expected, "Stone count must match expected")

    def test_weapon_shooting(self):
        # Test that each weapon, when used to shoot, adds a projectile to the player
        for WeaponClass in [Rifle, Pistol, Shotgun]:
            player = Player(100, 100)
            player.camera_x = 0
            player.camera_y = 0
            weapon = WeaponClass()
            player.weapon = weapon
            initial = len(player.projectiles)
            player.shoot(200, 200)
            self.assertGreater(len(player.projectiles), initial, f"{WeaponClass.__name__} should add a projectile on shoot")

if __name__ == '__main__':
    unittest.main()
