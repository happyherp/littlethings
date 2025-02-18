# 2D Platformer Game

This is a simple 2D platformer game built with Python and Pygame.

## Installation

1. **Clone the repository:**

    ```sh
    git clone https://github.com/happyherp/plattformer.git
    cd plattformer
    ```

2. **Create a virtual environment:**

    ```sh
    python -m venv venv
    ```

3. **Activate the virtual environment:**

    - On Windows:
        ```sh
        venv\Scripts\activate
        ```
    - On macOS and Linux:
        ```sh
        source venv/bin/activate
        ```

4. **Install the required packages:**

    ```sh
    pip install -r requirements.txt
    ```

## Running the Game

1. **Ensure you are in the project directory and the virtual environment is activated.**

2. **Run the game:**

    ```sh
    python main.py
    ```

## Controls

- **W**: Move up
- **A**: Move left
- **S**: Move down
- **D**: Move right
- **Left Mouse Button**: Shoot

## Game Objective

- Avoid enemies and obstacles.
- Shoot enemies to survive.
- The game ends when an enemy collides with the player.

## Assets

- **Grass Image**: `img/grass_100x100.png`
- **Rock Image**: `img/rock_50x50.png`

Ensure these images are placed in the `img` directory within the project.

## License

This project is licensed under the MIT License.