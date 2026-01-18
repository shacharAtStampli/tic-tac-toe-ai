import React from 'react';
import './Board.css';

function Board({ board, boardSize, onCellClick, winningPattern, isClickable, currentPlayer }) {
  // Calculate actual grid size from board length (more reliable)
  const actualSize = board.length > 0 ? Math.sqrt(board.length) : boardSize;
  const gridSize = Math.floor(actualSize);
  
  // Generate cells - use actual board if available, otherwise create empty placeholder
  const cells = board.length > 0 
    ? board 
    : Array(boardSize * boardSize).fill('');
  
  const displaySize = board.length > 0 ? gridSize : boardSize;
  const fontSize = Math.max(1.2, 3.5 - (displaySize - 3) * 0.4);
  
  const isWinningCell = (index) => {
    if (!winningPattern) return false;
    return winningPattern.some(pos => pos[0] === index);
  };

  return (
    <div className="board-container">
      <div 
        className="board" 
        style={{ gridTemplateColumns: `repeat(${displaySize}, 1fr)` }}
      >
        {cells.map((cell, index) => (
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
