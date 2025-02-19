import pygame
import math
import heapq
from constants import TILE_SIZE

class Enemy:
    def __init__(self, x, y):
        self.image = pygame.Surface((40, 40))
        self.image.fill((0, 0, 255))  # Fill the enemy with blue color
        self.rect = self.image.get_rect()
        self.rect.topleft = (x, y)
        self.speed = 2
        self.path = []

    def heuristic(self, a, b):
        return abs(a[0] - b[0]) + abs(a[1] - b[1])

    def find_path(self, start, goal, game_map):
        neighbors = [(0, 1), (0, -1), (1, 0), (-1, 0)]
        close_set = set()
        came_from = {}
        gscore = {start: 0}
        fscore = {start: self.heuristic(start, goal)}
        oheap = []

        heapq.heappush(oheap, (fscore[start], start))

        while oheap:
            current = heapq.heappop(oheap)[1]

            if current == goal:
                data = []
                while current in came_from:
                    data.append(current)
                    current = came_from[current]
                return data

            close_set.add(current)
            for i, j in neighbors:
                neighbor = current[0] + i, current[1] + j
                tentative_g_score = gscore[current] + self.heuristic(current, neighbor)
                if 0 <= neighbor[0] < game_map.width and 0 <= neighbor[1] < game_map.height:
                    if game_map.tiles[neighbor[1]][neighbor[0]] == 1:
                        continue
                else:
                    continue

                if neighbor in close_set and tentative_g_score >= gscore.get(neighbor, 0):
                    continue

                if tentative_g_score < gscore.get(neighbor, 0) or neighbor not in [i[1] for i in oheap]:
                    came_from[neighbor] = current
                    gscore[neighbor] = tentative_g_score
                    fscore[neighbor] = tentative_g_score + self.heuristic(neighbor, goal)
                    heapq.heappush(oheap, (fscore[neighbor], neighbor))

        return []

    def update(self, target_x, target_y, game_map):
        if not self.path:
            start = (self.rect.x // TILE_SIZE, self.rect.y // TILE_SIZE)
            goal = (target_x // TILE_SIZE, target_y // TILE_SIZE)
            self.path = self.find_path(start, goal, game_map)

        if self.path:
            next_step = self.path[-1]  # Peek at the next step
            next_x = next_step[0] * TILE_SIZE
            next_y = next_step[1] * TILE_SIZE

            # Calculate the direction to move towards the next step
            direction_x = next_x - self.rect.x
            direction_y = next_y - self.rect.y
            distance = math.hypot(direction_x, direction_y)

            if distance < self.speed:
                # Move directly to the next step if within speed range
                self.rect.x = next_x
                self.rect.y = next_y
                self.path.pop()  # Remove the step from the path
            else:
                # Move incrementally towards the next step
                self.rect.x += self.speed * (direction_x / distance)
                self.rect.y += self.speed * (direction_y / distance)
   

    def draw(self, screen, camera_x, camera_y):
        screen.blit(self.image, (self.rect.x - camera_x, self.rect.y - camera_y))
