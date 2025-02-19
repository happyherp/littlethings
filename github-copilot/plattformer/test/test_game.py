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
        self.dummy_time = DummyTime(500)
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
            player.weapon = WeaponClass(time_source=DummyTime(1000))
            initial = len(player.projectiles)
            player.shoot(200, 200)
            print(player.projectiles)
            self.assertGreater(len(player.projectiles), initial, f"{WeaponClass.__name__} should add a projectile on shoot")

    def test_weapon_pickup_and_shoot_sequence(self):
        # Create a controlled GameState instance
        game_state = GameState(time_provider=self.dummy_time, random_instance=self.dummy_random)
        player = game_state.player
        # Position player at a known location
        player.rect.topleft = (100, 100)
        
        # Place a Rifle at player's location
        rifle = Rifle(time_source=self.dummy_time)
        rifle.rect = pygame.Rect(100, 100, 20, 20)
        game_state.weapons = [rifle]
        # Simulate pickup
        player.check_weapon_pickup(game_state.weapons)
        self.assertEqual(player.weapon.name, "Rifle", "Player should have picked up the Rifle")
        
        # Fire with Rifle and expect 1 projectile
        player.projectiles = []
        player.shoot(200, 200)
        self.assertEqual(len(player.projectiles), 1, "Rifle should fire 1 projectile")
        
        # Clear current weapon and simulate Shotgun pickup
        player.weapon = None
        shotgun = Shotgun(time_source=self.dummy_time)
        shotgun.rect = pygame.Rect(100, 100, 20, 20)
        game_state.weapons = [shotgun]
        player.check_weapon_pickup(game_state.weapons)
        self.assertEqual(player.weapon.name, "Shotgun", "Player should have picked up the Shotgun")
        
        # Fire with Shotgun and expect 5 projectiles
        player.projectiles = []
        self.dummy_time.advance(1000)  # Advance time to allow for Shotgun fire_rate
        player.shoot(200, 200)
        self.assertEqual(len(player.projectiles), 5, "Shotgun should fire 5 projectiles")

    def test_fire_rate_comparison(self):
        # Use DummyTime from the test suite for controlled time progression
        dummy_time = DummyTime(500)
        # Create a player for testing
        player = Player(100, 100)
        player.projectiles = []
        
        # Instantiate a Rifle and a Pistol with the same time source
        rifle = Rifle(time_source=dummy_time)
        pistol = Pistol(time_source=dummy_time)
        
        # Assign the rifle to the player and shoot at time 0
        player.weapon = rifle
        player.shoot(200, 200)
        rifle_projectiles = len(player.projectiles)
        
        # Advance time by 250 ms which is > rifle fire_rate (200) but < pistol fire_rate (500)
        dummy_time.advance(250)
        # Attempt to shoot with Rifle again
        player.shoot(200, 200)
        rifle_projectiles_after = len(player.projectiles)
        
        # Reset projectiles and switch weapon to pistol for test
        player.projectiles = []
        player.weapon = pistol
        dummy_time.current = 500
        player.shoot(200, 200)
        pistol_projectiles = len(player.projectiles)
        
        dummy_time.advance(250)
        player.shoot(200, 200)
        pistol_projectiles_after = len(player.projectiles)
        
        self.assertEqual(rifle_projectiles, 1, "Initial rifle shot should add 1 projectile")
        self.assertEqual(rifle_projectiles_after, 2, "Rifle should fire again after 250ms")
        self.assertEqual(pistol_projectiles, 1, "Initial pistol shot should add 1 projectile")
        self.assertEqual(pistol_projectiles_after, 1, "Pistol should not fire again after 250ms")

if __name__ == '__main__':
    unittest.main()
