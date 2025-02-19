import random
from constants import TILE_SIZE

class Map:
    def __init__(self, width, height, stone_density, random_instance=None):
        self.width = width
        self.height = height
        self.tiles = [[0 for _ in range(width)] for _ in range(height)]
        self.random = random_instance if random_instance else random
        self.generate_stones(stone_density)

    def generate_stones(self, stone_density):
        num_stones = int(self.width * self.height * stone_density)
        for _ in range(num_stones):
            while True:
                x = self.random.randint(0, self.width - 1)
                y = self.random.randint(0, self.height - 1)
                if self.tiles[y][x] == 0:
                    self.tiles[y][x] = 1
                    break

    def check_collision(self, rect):
        tile_x1 = rect.left // TILE_SIZE
        tile_y1 = rect.top // TILE_SIZE
        tile_x2 = (rect.right-1) // TILE_SIZE
        tile_y2 = (rect.bottom-1) // TILE_SIZE

        #out of map
        if tile_x1 < 0 or tile_y1 < 0 or tile_x2 >= self.width or tile_y2 >= self.height:
            return True

        for x in range(tile_x1, tile_x2 + 1):
            for y in range(tile_y1, tile_y2 + 1):
                if self.tiles[y][x] == 1:
                    return True
        return False
