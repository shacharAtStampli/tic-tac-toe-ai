import React from 'react';
import './AgentCard.css';

function AgentCard({ player, config, isActive, thoughts, currentPlayer }) {
  const isHuman = config?.human || false;
  const name = config?.name || (player === 'X' ? 'Strategic Agent' : 'Defensive Agent');
  const description = config?.description || '';

  const getThoughts = () => {
    if (!thoughts || currentPlayer === player) {
      if (isHuman && isActive) {
        return [{ text: 'Your turn - click a cell!', type: 'strategy' }];
      }
      return [{ text: 'Waiting...', type: '' }];
    }
    return thoughts.thoughts || [];
  };

  return (
    <div className={`agent-card ${player.toLowerCase()} ${isActive ? 'active' : ''} ${isHuman ? 'human' : ''}`}>
      <h3>{player} - {name}</h3>
      <p>{description}</p>
      <div className={`thinking-bubble ${isActive ? 'active' : ''}`}>
        {getThoughts().map((thought, index) => (
          <div key={index} className={`thought ${thought.type || ''}`}>
            {thought.text}
          </div>
        ))}
      </div>
    </div>
  );
}

export default AgentCard;
