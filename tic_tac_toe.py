"""
Tic Tac Toe with two AI agents playing against each other.
- Agent 1 (X): Uses Minimax algorithm (optimal play)
- Agent 2 (O): Uses random moves
"""

import random
from typing import Optional


class TicTacToe:
    """Tic Tac Toe game board."""
    
    def __init__(self):
        self.board = [' '] * 9  # 3x3 board as flat list
        self.current_player = 'X'
    
    def display(self):
        """Print the current board state."""
        print()
        for i in range(3):
            row = self.board[i*3:(i+1)*3]
            print(f" {row[0]} | {row[1]} | {row[2]} ")
            if i < 2:
                print("-----------")
        print()
    
    def available_moves(self) -> list[int]:
        """Return list of available positions (0-8)."""
        return [i for i, cell in enumerate(self.board) if cell == ' ']
    
    def make_move(self, position: int) -> bool:
        """Make a move at the given position. Returns True if valid."""
        if self.board[position] == ' ':
            self.board[position] = self.current_player
            self.current_player = 'O' if self.current_player == 'X' else 'X'
            return True
        return False
    
    def check_winner(self) -> Optional[str]:
        """Check if there's a winner. Returns 'X', 'O', or None."""
        win_patterns = [
            [0, 1, 2], [3, 4, 5], [6, 7, 8],  # rows
            [0, 3, 6], [1, 4, 7], [2, 5, 8],  # columns
            [0, 4, 8], [2, 4, 6]               # diagonals
        ]
        for pattern in win_patterns:
            if self.board[pattern[0]] == self.board[pattern[1]] == self.board[pattern[2]] != ' ':
                return self.board[pattern[0]]
        return None
    
    def is_draw(self) -> bool:
        """Check if the game is a draw."""
        return ' ' not in self.board and self.check_winner() is None
    
    def is_game_over(self) -> bool:
        """Check if the game has ended."""
        return self.check_winner() is not None or self.is_draw()
    
    def copy(self) -> 'TicTacToe':
        """Create a copy of the game state."""
        new_game = TicTacToe()
        new_game.board = self.board.copy()
        new_game.current_player = self.current_player
        return new_game


class RandomAgent:
    """Agent that makes random moves."""
    
    def __init__(self, symbol: str):
        self.symbol = symbol
        self.name = f"Random Agent ({symbol})"
    
    def choose_move(self, game: TicTacToe) -> int:
        """Choose a random available move."""
        available = game.available_moves()
        move = random.choice(available)
        print(f"{self.name} chooses position {move}")
        return move


class MinimaxAgent:
    """Agent that uses Minimax algorithm for optimal play."""
    
    def __init__(self, symbol: str):
        self.symbol = symbol
        self.opponent = 'O' if symbol == 'X' else 'X'
        self.name = f"Minimax Agent ({symbol})"
    
    def choose_move(self, game: TicTacToe) -> int:
        """Choose the best move using minimax."""
        best_score = float('-inf')
        best_move = None
        
        for move in game.available_moves():
            game_copy = game.copy()
            game_copy.make_move(move)
            score = self._minimax(game_copy, False)
            if score > best_score:
                best_score = score
                best_move = move
        
        print(f"{self.name} chooses position {best_move}")
        return best_move
    
    def _minimax(self, game: TicTacToe, is_maximizing: bool) -> int:
        """Minimax algorithm implementation."""
        winner = game.check_winner()
        if winner == self.symbol:
            return 10
        elif winner == self.opponent:
            return -10
        elif game.is_draw():
            return 0
        
        if is_maximizing:
            best_score = float('-inf')
            for move in game.available_moves():
                game_copy = game.copy()
                game_copy.make_move(move)
                score = self._minimax(game_copy, False)
                best_score = max(score, best_score)
            return best_score
        else:
            best_score = float('inf')
            for move in game.available_moves():
                game_copy = game.copy()
                game_copy.make_move(move)
                score = self._minimax(game_copy, True)
                best_score = min(score, best_score)
            return best_score


def play_game(agent_x, agent_o, verbose: bool = True) -> Optional[str]:
    """Play a game between two agents. Returns winner or None for draw."""
    game = TicTacToe()
    agents = {'X': agent_x, 'O': agent_o}
    
    if verbose:
        print("=" * 40)
        print("Starting Tic Tac Toe!")
        print(f"X: {agent_x.name}")
        print(f"O: {agent_o.name}")
        print("=" * 40)
        print("\nBoard positions:")
        print(" 0 | 1 | 2 ")
        print("-----------")
        print(" 3 | 4 | 5 ")
        print("-----------")
        print(" 6 | 7 | 8 ")
    
    while not game.is_game_over():
        current_agent = agents[game.current_player]
        
        if verbose:
            game.display()
            print(f"\n{current_agent.name}'s turn:")
        
        move = current_agent.choose_move(game)
        game.make_move(move)
    
    if verbose:
        game.display()
    
    winner = game.check_winner()
    if verbose:
        if winner:
            print(f"\n{agents[winner].name} wins!")
        else:
            print("\nIt's a draw!")
    
    return winner


def run_tournament(num_games: int = 10):
    """Run multiple games and show statistics."""
    print("\n" + "=" * 50)
    print(f"Running Tournament: {num_games} games")
    print("=" * 50)
    
    results = {'X': 0, 'O': 0, 'Draw': 0}
    
    for i in range(num_games):
        print(f"\n--- Game {i + 1} ---")
        # Alternate who plays X
        if i % 2 == 0:
            agent_x = MinimaxAgent('X')
            agent_o = RandomAgent('O')
        else:
            agent_x = RandomAgent('X')
            agent_o = MinimaxAgent('O')
        
        winner = play_game(agent_x, agent_o, verbose=False)
        
        if winner == 'X':
            results['X'] += 1
            print(f"{agent_x.name} wins!")
        elif winner == 'O':
            results['O'] += 1
            print(f"{agent_o.name} wins!")
        else:
            results['Draw'] += 1
            print("Draw!")
    
    print("\n" + "=" * 50)
    print("Tournament Results:")
    print(f"  X wins: {results['X']}")
    print(f"  O wins: {results['O']}")
    print(f"  Draws:  {results['Draw']}")
    print("=" * 50)


if __name__ == "__main__":
    # Play a single game with Minimax (X) vs Random (O)
    print("\n>>> Single Game: Minimax vs Random <<<")
    agent_x = MinimaxAgent('X')
    agent_o = RandomAgent('O')
    play_game(agent_x, agent_o)
    
    # Play a game with two Minimax agents (should always draw)
    print("\n>>> Single Game: Minimax vs Minimax <<<")
    agent_x = MinimaxAgent('X')
    agent_o = MinimaxAgent('O')
    play_game(agent_x, agent_o)
    
    # Run a tournament
    run_tournament(10)
