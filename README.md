# Tic Tac Toe - Smart AI Battle

A dynamic Tic Tac Toe game featuring two intelligent AI agents that play against each other with strategic thinking and early draw detection.

## Features

### Smart AI Agents
- **Strategic Agent (X)** - Aggressive playstyle with higher exploration
- **Defensive Agent (O)** - Cautious playstyle focused on blocking

Both agents use the same intelligent decision-making process:
1. **Win Detection** - Always takes a winning move if available
2. **Threat Blocking** - Identifies and blocks opponent's winning threats
3. **Fork Creation** - Creates positions with multiple winning paths
4. **Fork Blocking** - Prevents opponent from creating forks
5. **Strategic Positioning** - Evaluates positions based on winning potential and board control

### Dynamic Board Size
- Configurable board size from 3x3 up to 10x10
- Adjustable win length (how many in a row needed to win)
- Automatically generates all possible winning patterns for any configuration

### Early Draw Detection
- Analyzes the board after each move
- Detects when no winning paths remain for either player
- Ends the game early as a draw instead of filling all cells
- Shows remaining winning paths count in real-time

### Game Variety
- Randomized move selection among equally good options
- Different personality traits for each agent
- No two games play out exactly the same

## How to Play

1. Open `tic_tac_toe_ui.html` in any modern web browser
2. Configure the game:
   - **Board Size**: Set the grid size (e.g., 5 for a 5x5 board)
   - **Win Length**: Set how many marks in a row are needed to win
   - **Speed**: Adjust how fast the agents make moves
3. Click **Start Game** to watch a single game
4. Click **Play 2 Games** to watch two consecutive games

## Visual Features

- **Thinking Bubbles** - Shows each agent's decision-making process in real-time
- **Threat Highlighting** - Yellow highlight on cells where opponent could win
- **Opportunity Highlighting** - Green highlight on potential winning moves
- **Winning Animation** - Pulsing effect on the winning line
- **Modern Dark Theme** - Clean UI with glowing effects

## Files

- `tic_tac_toe_ui.html` - Main game with visual UI (single HTML file, no dependencies)
- `tic_tac_toe.py` - Python implementation with console output

## Technical Details

### Agent Decision Logic (Priority Order)
1. Take winning move (if available)
2. Block opponent's winning move (if threatened)
3. Create a fork (multiple winning paths)
4. Block opponent's fork
5. Take strategic position (center > corners > edges)

### Early Draw Detection
The game checks if any winning pattern is still achievable:
- A pattern is "blocked" if it contains both X and O
- Game ends when ALL patterns are blocked for both players
- Reports how many empty cells remain when early draw is detected

## Browser Compatibility

Works in all modern browsers:
- Chrome
- Firefox
- Safari
- Edge

No installation or dependencies required - just open the HTML file!

## License

MIT License - Feel free to use and modify as needed.
