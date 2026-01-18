import React, { useState, useEffect, useCallback } from 'react';
import Board from './components/Board';
import AgentCard from './components/AgentCard';
import GameLog from './components/GameLog';
import Settings from './components/Settings';
import './App.css';

const API_BASE = 'http://localhost:8080/api/game';

function App() {
  const [gameState, setGameState] = useState(null);
  const [config, setConfig] = useState({
    boardSize: 3,
    winLength: 3,
    humanPlayers: 0
  });
  const [speed, setSpeed] = useState(800);
  const [logs, setLogs] = useState([]);
  const [isPlaying, setIsPlaying] = useState(false);

  const addLog = useCallback((message, type = '') => {
    setLogs(prev => [{ message, type, id: Date.now() }, ...prev]);
  }, []);

  const fetchGameState = async () => {
    try {
      const response = await fetch(`${API_BASE}/state`);
      const data = await response.json();
      setGameState(data);
      return data;
    } catch (error) {
      console.error('Error fetching game state:', error);
    }
  };

  const startNewGame = async () => {
    try {
      setIsPlaying(true);
      setLogs([]);
      const response = await fetch(`${API_BASE}/new`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(config)
      });
      const data = await response.json();
      setGameState(data);
      addLog(`--- New Game (${config.boardSize}x${config.boardSize}, ${config.winLength} to win) ---`);
      
      // If it's AI's turn, make AI move
      if (data && !data.gameOver) {
        const currentPlayerConfig = data.currentPlayer === 'X' ? data.playerX : data.playerO;
        if (!currentPlayerConfig.isHuman) {
          setTimeout(() => makeAIMove(), speed / 2);
        }
      }
    } catch (error) {
      console.error('Error starting new game:', error);
      setIsPlaying(false);
    }
  };

  const makeMove = async (position) => {
    if (!gameState || gameState.gameOver) return;
    
    const currentPlayerConfig = gameState.currentPlayer === 'X' ? gameState.playerX : gameState.playerO;
    if (!currentPlayerConfig.isHuman) return;

    try {
      const response = await fetch(`${API_BASE}/move`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ position })
      });
      const data = await response.json();
      setGameState(data);
      
      const playerName = currentPlayerConfig.name;
      const posName = getPositionName(position, config.boardSize);
      addLog(`${playerName} (${gameState.currentPlayer}) → ${posName}`, 
             gameState.currentPlayer === 'X' ? 'x-move' : 'o-move');

      if (data.gameOver) {
        handleGameEnd(data);
      } else {
        // If next player is AI, make AI move
        const nextPlayerConfig = data.currentPlayer === 'X' ? data.playerX : data.playerO;
        if (!nextPlayerConfig.isHuman) {
          setTimeout(() => makeAIMove(), speed / 2);
        }
      }
    } catch (error) {
      console.error('Error making move:', error);
    }
  };

  const makeAIMove = async () => {
    try {
      const response = await fetch(`${API_BASE}/ai-move`, {
        method: 'POST'
      });
      const data = await response.json();
      setGameState(data);

      if (data.lastThoughts) {
        const playerConfig = data.currentPlayer === 'X' ? data.playerO : data.playerX;
        const prevPlayer = data.currentPlayer === 'X' ? 'O' : 'X';
        const posName = getPositionName(data.lastThoughts.chosenMove, config.boardSize);
        addLog(`${playerConfig.name} (${prevPlayer}) → ${posName} (${data.lastThoughts.reason})`,
               prevPlayer === 'X' ? 'x-move' : 'o-move');
      }

      if (data.gameOver) {
        handleGameEnd(data);
      } else {
        // If next player is also AI, continue
        const nextPlayerConfig = data.currentPlayer === 'X' ? data.playerX : data.playerO;
        if (!nextPlayerConfig.isHuman) {
          setTimeout(() => makeAIMove(), speed);
        }
      }
    } catch (error) {
      console.error('Error making AI move:', error);
    }
  };

  const handleGameEnd = (data) => {
    if (data.winner) {
      addLog(`🏆 ${data.status}`, 'result');
    } else {
      addLog(`🤝 ${data.status}`, 'result');
    }
    setIsPlaying(false);
  };

  const getPositionName = (pos, boardSize) => {
    const row = Math.floor(pos / boardSize);
    const col = pos % boardSize;
    return `(${row + 1}, ${col + 1})`;
  };

  const getCurrentPlayerConfig = () => {
    if (!gameState) return null;
    return gameState.currentPlayer === 'X' ? gameState.playerX : gameState.playerO;
  };

  const getStatus = () => {
    if (!gameState) return 'Click "Start Game" to begin';
    if (gameState.gameOver) return gameState.status;
    
    const currentConfig = getCurrentPlayerConfig();
    if (currentConfig?.isHuman) {
      return `${currentConfig.name}'s turn - click a cell!`;
    }
    return `${currentConfig?.name} is thinking...`;
  };

  useEffect(() => {
    fetchGameState();
  }, []);

  // Reset board preview when config changes (before game starts)
  useEffect(() => {
    if (!isPlaying) {
      // Update local preview board to match config
      setGameState(prev => prev ? {
        ...prev,
        board: Array(config.boardSize * config.boardSize).fill(''),
        boardSize: config.boardSize,
        winLength: config.winLength,
        gameOver: true // Mark as over so it's not clickable
      } : null);
    }
  }, [config.boardSize, config.winLength, isPlaying]);

  return (
    <div className="container">
      <h1>Tic Tac Toe</h1>
      <p className="subtitle">Smart AI Battle - Java + React</p>

      <div className="agents-info">
        <AgentCard 
          player="X"
          config={gameState?.playerX}
          isActive={gameState && !gameState.gameOver && gameState.currentPlayer === 'X'}
          thoughts={gameState?.lastThoughts}
          currentPlayer={gameState?.currentPlayer}
        />
        <AgentCard 
          player="O"
          config={gameState?.playerO}
          isActive={gameState && !gameState.gameOver && gameState.currentPlayer === 'O'}
          thoughts={gameState?.lastThoughts}
          currentPlayer={gameState?.currentPlayer}
        />
      </div>

      <Settings 
        config={config}
        setConfig={setConfig}
        speed={speed}
        setSpeed={setSpeed}
        disabled={isPlaying}
      />

      <div className="board-info">
        {config.boardSize}x{config.boardSize} board - {config.winLength} in a row to win
      </div>

      <div className={`status ${gameState?.winner ? 'winner' : ''} ${gameState?.gameOver && !gameState?.winner ? 'draw' : ''}`}>
        {getStatus()}
      </div>

      <Board 
        board={gameState?.board || Array(config.boardSize * config.boardSize).fill('')}
        boardSize={gameState?.boardSize || config.boardSize}
        onCellClick={makeMove}
        winningPattern={gameState?.winningPattern}
        isClickable={gameState && !gameState.gameOver && getCurrentPlayerConfig()?.isHuman}
        currentPlayer={gameState?.currentPlayer}
      />

      <div className="controls">
        <button onClick={startNewGame} disabled={isPlaying}>
          Start Game
        </button>
      </div>

      <GameLog logs={logs} />
    </div>
  );
}

export default App;
