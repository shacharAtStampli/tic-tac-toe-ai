# Tic Tac Toe - Smart AI Battle

A full-stack Tic Tac Toe game featuring intelligent AI agents, built with **Java Spring Boot** backend and **React** frontend.

## Features

### Flexible Player Modes
- **0 Players (AI vs AI)** - Watch two smart AI agents battle each other
- **1 Player (Human vs AI)** - Play against a challenging AI opponent (random fair first player selection)
- **2 Players (Human vs Human)** - Classic local multiplayer

### Smart AI Agents
- **Strategic Agent (X)** - Aggressive playstyle with higher exploration
- **Defensive Agent (O)** - Cautious playstyle focused on blocking

AI Decision Logic (Priority Order):
1. Take winning move (if available)
2. Block opponent's winning move (if threatened)
3. Create a fork (multiple winning paths)
4. Block opponent's fork
5. Take strategic position (center > corners > edges)

### Dynamic Board Size
- Configurable board size from 3x3 up to 10x10
- Adjustable win length (how many in a row needed to win)

### Early Draw Detection
- Analyzes the board after each move
- Detects when no winning paths remain for either player
- Ends the game early as a draw

### Fair First Player Selection
- When playing Human vs AI, randomly decides who plays first
- Fairness constraint: same choice cannot happen more than 2 times in a row

## Tech Stack

- **Backend**: Java 17, Spring Boot 3.2
- **Frontend**: React 18
- **Build**: Maven (backend), npm (frontend)

## Project Structure

```
TicTakToe/
├── backend/
│   ├── src/main/java/com/tictactoe/
│   │   ├── TicTacToeApplication.java
│   │   ├── config/
│   │   │   └── CorsConfig.java
│   │   ├── controller/
│   │   │   └── GameController.java
│   │   ├── model/
│   │   │   ├── AgentThoughts.java
│   │   │   ├── GameConfig.java
│   │   │   ├── GameState.java
│   │   │   ├── MoveRequest.java
│   │   │   └── PlayerConfig.java
│   │   └── service/
│   │       └── GameService.java
│   └── pom.xml
└── frontend/
    ├── public/
    │   └── index.html
    ├── src/
    │   ├── components/
    │   │   ├── AgentCard.js/css
    │   │   ├── Board.js/css
    │   │   ├── GameLog.js/css
    │   │   └── Settings.js/css
    │   ├── App.js/css
    │   ├── index.js/css
    └── package.json
```

## Getting Started

### Prerequisites
- Java 17+
- Node.js 16+
- Maven 3.6+

### Running the Backend

```bash
cd TicTakToe/backend
mvn spring-boot:run
```

The backend will start on http://localhost:8080

### Running the Frontend

```bash
cd TicTakToe/frontend
npm install
npm start
```

The frontend will start on http://localhost:3000

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/game/state` | Get current game state |
| POST | `/api/game/new` | Start a new game with config |
| POST | `/api/game/reset` | Reset the current game |
| POST | `/api/game/move` | Make a human move |
| POST | `/api/game/ai-move` | Trigger AI move |

## License

MIT License - Feel free to use and modify as needed.
