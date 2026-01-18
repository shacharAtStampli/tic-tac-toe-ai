import React from 'react';
import './GameLog.css';

function GameLog({ logs }) {
  return (
    <div className="game-log">
      <h4>Game Log</h4>
      <div className="log-entries">
        {logs.map(log => (
          <div key={log.id} className={`log-entry ${log.type}`}>
            {log.message}
          </div>
        ))}
      </div>
    </div>
  );
}

export default GameLog;
