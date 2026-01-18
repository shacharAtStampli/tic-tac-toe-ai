import React from 'react';
import './Settings.css';

function Settings({ config, setConfig, speed, setSpeed, disabled }) {
  const handleBoardSizeChange = (e) => {
    const size = parseInt(e.target.value) || 3;
    setConfig(prev => ({
      ...prev,
      boardSize: size,
      winLength: Math.min(prev.winLength, size)
    }));
  };

  const handleWinLengthChange = (e) => {
    const len = parseInt(e.target.value) || 3;
    setConfig(prev => ({
      ...prev,
      winLength: Math.min(len, prev.boardSize)
    }));
  };

  const handleHumanPlayersChange = (e) => {
    setConfig(prev => ({
      ...prev,
      humanPlayers: parseInt(e.target.value)
    }));
  };

  return (
    <div className="settings">
      <div className="setting-group">
        <label htmlFor="human-players">Human Players:</label>
        <select 
          id="human-players" 
          value={config.humanPlayers}
          onChange={handleHumanPlayersChange}
          disabled={disabled}
        >
          <option value={0}>0 (AI vs AI)</option>
          <option value={1}>1 (Human vs AI)</option>
          <option value={2}>2 (Human vs Human)</option>
        </select>
      </div>
      
      <div className="setting-group">
        <label htmlFor="board-size">Board Size:</label>
        <input 
          type="number" 
          id="board-size" 
          min="3" 
          max="10" 
          value={config.boardSize}
          onChange={handleBoardSizeChange}
          disabled={disabled}
        />
      </div>
      
      <div className="setting-group">
        <label htmlFor="win-length">Win Length:</label>
        <input 
          type="number" 
          id="win-length" 
          min="3" 
          max={config.boardSize}
          value={config.winLength}
          onChange={handleWinLengthChange}
          disabled={disabled}
        />
      </div>
      
      <div className="setting-group">
        <label htmlFor="speed">AI Speed:</label>
        <select 
          id="speed" 
          value={speed}
          onChange={(e) => setSpeed(parseInt(e.target.value))}
          disabled={disabled}
        >
          <option value={1500}>Slow</option>
          <option value={800}>Normal</option>
          <option value={400}>Fast</option>
        </select>
      </div>
    </div>
  );
}

export default Settings;
