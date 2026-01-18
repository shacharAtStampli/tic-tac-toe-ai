package com.tictactoe.service;

import com.tictactoe.model.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameService {
    
    private GameState gameState = new GameState();
    private List<int[]> winPatterns = new ArrayList<>();

    public GameState getGameState() {
        return gameState;
    }

    public GameState newGame(GameConfig config) {
        gameState.setBoardSize(config.getBoardSize());
        gameState.setWinLength(Math.min(config.getWinLength(), config.getBoardSize()));
        gameState.setHumanPlayers(config.getHumanPlayers());
        generateWinPatterns();
        return gameState;
    }

    public GameState resetGame() {
        gameState.reset();
        generateWinPatterns();
        return gameState;
    }

    public GameState makeMove(int position) {
        if (gameState.isGameOver()) {
            return gameState;
        }

        String[] board = gameState.getBoard();
        if (position < 0 || position >= board.length || !board[position].isEmpty()) {
            return gameState;
        }

        // Make the move
        board[position] = gameState.getCurrentPlayer();
        gameState.setBoard(board);

        // Check for game end
        if (checkGameEnd()) {
            return gameState;
        }

        // Switch player
        switchPlayer();
        
        return gameState;
    }

    public GameState makeAIMove() {
        if (gameState.isGameOver()) {
            return gameState;
        }

        PlayerConfig currentPlayerConfig = gameState.getCurrentPlayer().equals("X") 
            ? gameState.getPlayerX() 
            : gameState.getPlayerO();

        if (currentPlayerConfig.isHuman()) {
            gameState.setStatus("Waiting for human move");
            return gameState;
        }

        AgentThoughts thoughts = calculateAIMove(gameState.getCurrentPlayer());
        gameState.setLastThoughts(thoughts);
        
        // Make the AI move
        String[] board = gameState.getBoard();
        board[thoughts.getChosenMove()] = gameState.getCurrentPlayer();
        gameState.setBoard(board);
        gameState.setStatus(thoughts.getReason());

        // Check for game end
        if (checkGameEnd()) {
            return gameState;
        }

        // Switch player
        switchPlayer();
        
        return gameState;
    }

    private void switchPlayer() {
        gameState.setCurrentPlayer(gameState.getCurrentPlayer().equals("X") ? "O" : "X");
    }

    private void generateWinPatterns() {
        winPatterns.clear();
        int size = gameState.getBoardSize();
        int len = gameState.getWinLength();

        // Rows
        for (int row = 0; row < size; row++) {
            for (int startCol = 0; startCol <= size - len; startCol++) {
                int[] pattern = new int[len];
                for (int i = 0; i < len; i++) {
                    pattern[i] = row * size + startCol + i;
                }
                winPatterns.add(pattern);
            }
        }

        // Columns
        for (int col = 0; col < size; col++) {
            for (int startRow = 0; startRow <= size - len; startRow++) {
                int[] pattern = new int[len];
                for (int i = 0; i < len; i++) {
                    pattern[i] = (startRow + i) * size + col;
                }
                winPatterns.add(pattern);
            }
        }

        // Diagonals (top-left to bottom-right)
        for (int row = 0; row <= size - len; row++) {
            for (int col = 0; col <= size - len; col++) {
                int[] pattern = new int[len];
                for (int i = 0; i < len; i++) {
                    pattern[i] = (row + i) * size + (col + i);
                }
                winPatterns.add(pattern);
            }
        }

        // Diagonals (top-right to bottom-left)
        for (int row = 0; row <= size - len; row++) {
            for (int col = len - 1; col < size; col++) {
                int[] pattern = new int[len];
                for (int i = 0; i < len; i++) {
                    pattern[i] = (row + i) * size + (col - i);
                }
                winPatterns.add(pattern);
            }
        }
    }

    private boolean checkGameEnd() {
        String[] board = gameState.getBoard();

        // Check for winner
        for (int[] pattern : winPatterns) {
            String first = board[pattern[0]];
            if (!first.isEmpty()) {
                boolean won = true;
                for (int i = 1; i < pattern.length; i++) {
                    if (!board[pattern[i]].equals(first)) {
                        won = false;
                        break;
                    }
                }
                if (won) {
                    gameState.setGameOver(true);
                    gameState.setWinner(first);
                    List<int[]> winningPattern = new ArrayList<>();
                    for (int pos : pattern) {
                        winningPattern.add(new int[]{pos});
                    }
                    gameState.setWinningPattern(winningPattern);
                    
                    PlayerConfig winner = first.equals("X") ? gameState.getPlayerX() : gameState.getPlayerO();
                    gameState.setStatus(winner.getName() + " (" + first + ") wins!");
                    return true;
                }
            }
        }

        // Check if game is still winnable
        if (!isGameStillWinnable()) {
            gameState.setGameOver(true);
            gameState.setStatus("Draw - No winning paths remaining!");
            return true;
        }

        // Check for draw (board full)
        boolean isFull = true;
        for (String cell : board) {
            if (cell.isEmpty()) {
                isFull = false;
                break;
            }
        }
        if (isFull) {
            gameState.setGameOver(true);
            gameState.setStatus("It's a draw!");
            return true;
        }

        return false;
    }

    private boolean isGameStillWinnable() {
        return canPlayerWin("X") || canPlayerWin("O");
    }

    private boolean canPlayerWin(String player) {
        String opponent = player.equals("X") ? "O" : "X";
        String[] board = gameState.getBoard();

        for (int[] pattern : winPatterns) {
            boolean hasOpponent = false;
            for (int pos : pattern) {
                if (board[pos].equals(opponent)) {
                    hasOpponent = true;
                    break;
                }
            }
            if (!hasOpponent) {
                return true;
            }
        }
        return false;
    }

    private AgentThoughts calculateAIMove(String player) {
        AgentThoughts thoughts = new AgentThoughts();
        String opponent = player.equals("X") ? "O" : "X";
        String[] board = gameState.getBoard();
        int boardSize = gameState.getBoardSize();

        String personality = player.equals("X") ? "Aggressive" : "Cautious";
        double randomFactor = player.equals("X") ? 20 : 15;

        thoughts.addThought("Analyzing " + boardSize + "x" + boardSize + " board (" + personality + " mode)...", "");

        // Check if game is still winnable
        if (!isGameStillWinnable()) {
            thoughts.addThought("Board analysis: NO WINNING PATHS LEFT", "block");
            thoughts.setChosenMove(getFirstAvailableMove());
            thoughts.setReason("NO WINS POSSIBLE");
            return thoughts;
        }

        // Step 1: Check if I can win
        List<Integer> winningMoves = findWinningMoves(player);
        if (!winningMoves.isEmpty()) {
            int move = winningMoves.get((int)(Math.random() * winningMoves.size()));
            thoughts.addThought("Found winning move at " + getPositionName(move) + "!", "win");
            thoughts.setChosenMove(move);
            thoughts.setReason("WINNING MOVE");
            return thoughts;
        }
        thoughts.addThought("No immediate win available", "");

        // Step 2: Check if opponent can win - MUST BLOCK
        List<Integer> threats = findWinningMoves(opponent);
        if (!threats.isEmpty()) {
            int threat = threats.get((int)(Math.random() * threats.size()));
            thoughts.addThought("DANGER! " + opponent + " can win at " + getPositionName(threat), "block");
            thoughts.addThought("Must block immediately!", "block");
            if (threats.size() > 1) {
                thoughts.addThought(threats.size() + " threats detected! Trouble...", "block");
            }
            thoughts.setChosenMove(threat);
            thoughts.setReason("BLOCKING THREAT");
            return thoughts;
        }
        thoughts.addThought("No immediate threats from " + opponent, "");

        // Step 3: Find forks
        List<Integer> forkMoves = findForkMoves(player);
        if (!forkMoves.isEmpty()) {
            int move = forkMoves.get((int)(Math.random() * forkMoves.size()));
            thoughts.addThought("Creating fork with multiple win paths!", "strategy");
            thoughts.setChosenMove(move);
            thoughts.setReason("Creating fork!");
            return thoughts;
        }

        // Step 4: Block opponent forks
        List<Integer> opponentForks = findForkMoves(opponent);
        if (!opponentForks.isEmpty()) {
            int move = opponentForks.get((int)(Math.random() * opponentForks.size()));
            thoughts.addThought("Blocking opponent fork!", "strategy");
            thoughts.setChosenMove(move);
            thoughts.setReason("Blocking opponent fork!");
            return thoughts;
        }

        // Step 5: Strategic move
        int strategicMove = findStrategicMove(player, randomFactor);
        String reason = getPositionReason(strategicMove);
        thoughts.addThought(reason, "strategy");
        thoughts.setChosenMove(strategicMove);
        thoughts.setReason(reason);
        return thoughts;
    }

    private List<Integer> findWinningMoves(String player) {
        List<Integer> moves = new ArrayList<>();
        String[] board = gameState.getBoard();
        int winLength = gameState.getWinLength();

        for (int[] pattern : winPatterns) {
            int playerCount = 0;
            int emptyPos = -1;
            
            for (int pos : pattern) {
                if (board[pos].equals(player)) {
                    playerCount++;
                } else if (board[pos].isEmpty()) {
                    emptyPos = pos;
                }
            }
            
            if (playerCount == winLength - 1 && emptyPos != -1) {
                if (!moves.contains(emptyPos)) {
                    moves.add(emptyPos);
                }
            }
        }
        return moves;
    }

    private List<Integer> findForkMoves(String player) {
        List<Integer> forkMoves = new ArrayList<>();
        String[] board = gameState.getBoard();
        List<Integer> available = getAvailableMoves();

        for (int pos : available) {
            board[pos] = player;
            List<Integer> winningAfter = findWinningMoves(player);
            board[pos] = "";
            
            if (winningAfter.size() >= 2) {
                forkMoves.add(pos);
            }
        }
        return forkMoves;
    }

    private int findStrategicMove(String player, double randomFactor) {
        List<Integer> available = getAvailableMoves();
        int boardSize = gameState.getBoardSize();
        
        // Score each position
        Map<Integer, Double> scores = new HashMap<>();
        for (int pos : available) {
            double score = evaluatePosition(pos, player) + Math.random() * randomFactor;
            scores.put(pos, score);
        }

        // Get top moves
        double maxScore = scores.values().stream().max(Double::compare).orElse(0.0);
        double threshold = maxScore * 0.85;
        List<Integer> topMoves = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : scores.entrySet()) {
            if (entry.getValue() >= threshold) {
                topMoves.add(entry.getKey());
            }
        }

        return topMoves.get((int)(Math.random() * topMoves.size()));
    }

    private double evaluatePosition(int pos, String player) {
        double score = 0;
        String opponent = player.equals("X") ? "O" : "X";
        String[] board = gameState.getBoard();
        int boardSize = gameState.getBoardSize();

        for (int[] pattern : winPatterns) {
            boolean inPattern = false;
            for (int p : pattern) {
                if (p == pos) {
                    inPattern = true;
                    break;
                }
            }
            if (!inPattern) continue;

            int playerCount = 0;
            int opponentCount = 0;
            for (int p : pattern) {
                if (board[p].equals(player)) playerCount++;
                if (board[p].equals(opponent)) opponentCount++;
            }

            if (opponentCount == 0) {
                score += Math.pow(10, playerCount);
            }
        }

        // Center bonus
        int centerRow = boardSize / 2;
        int centerCol = boardSize / 2;
        int row = pos / boardSize;
        int col = pos % boardSize;
        int distFromCenter = Math.abs(row - centerRow) + Math.abs(col - centerCol);
        score += (boardSize - distFromCenter) * 2;

        return score;
    }

    private List<Integer> getAvailableMoves() {
        List<Integer> available = new ArrayList<>();
        String[] board = gameState.getBoard();
        for (int i = 0; i < board.length; i++) {
            if (board[i].isEmpty()) {
                available.add(i);
            }
        }
        return available;
    }

    private int getFirstAvailableMove() {
        String[] board = gameState.getBoard();
        for (int i = 0; i < board.length; i++) {
            if (board[i].isEmpty()) {
                return i;
            }
        }
        return 0;
    }

    private String getPositionName(int pos) {
        int row = pos / gameState.getBoardSize();
        int col = pos % gameState.getBoardSize();
        return "(" + (row + 1) + ", " + (col + 1) + ")";
    }

    private String getPositionReason(int pos) {
        int boardSize = gameState.getBoardSize();
        int row = pos / boardSize;
        int col = pos % boardSize;
        boolean isCenter = row == boardSize / 2 && col == boardSize / 2;
        boolean isCorner = (row == 0 || row == boardSize - 1) && (col == 0 || col == boardSize - 1);

        if (isCenter) return "Taking center - strongest position";
        if (isCorner) return "Taking corner - good control";
        return "Strategic position";
    }
}
