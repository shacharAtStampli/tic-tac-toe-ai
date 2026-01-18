import React from 'react';
import './Board.css';

function Board({ board, boardSize, onCellClick, winningPattern, isClickable, currentPlayer }) {
  const fontSize = Math.max(1.5, 4 - (boardSize - 3) * 0.5);
  
  const isWinningCell = (index) => {
    if (!winningPattern) return false;
    return winningPattern.some(pos => pos[0] === index);
  };

  return (
    <div className="board-container">
      <div 
        className="board" 
        style={{ gridTemplateColumns: `repeat(${boardSize}, 1fr)` }}
      >
        {board.map((cell, index) => (
          <div
            key={index}
            className={`cell 
              ${cell === 'X' ? 'x' : ''} 
              ${cell === 'O' ? 'o' : ''} 
              ${isWinningCell(index) ? 'winning' : ''}
              ${isClickable && !cell ? 'clickable' : ''}`}
            style={{ fontSize: `${fontSize}rem` }}
            onClick={() => isClickable && !cell && onCellClick(index)}
          >
            {cell}
          </div>
        ))}
      </div>
    </div>
  );
}

export default Board;
