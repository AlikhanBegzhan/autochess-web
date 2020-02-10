package fun.diasonti.autochessweb.controller.websocket;

import fun.diasonti.chessengine.data.ChessBoard;
import fun.diasonti.chessengine.data.Color;
import fun.diasonti.chessengine.data.Move;
import fun.diasonti.chessengine.engine.BitwiseOperationsMoveEngine;
import fun.diasonti.chessengine.engine.MinimaxAlphaBetaSearchEngine;
import fun.diasonti.chessengine.engine.interfaces.MoveEngine;
import fun.diasonti.chessengine.engine.interfaces.SearchEngine;
import fun.diasonti.chessengine.util.BoardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private SimpMessagingTemplate websocket;

    private final MoveEngine moveEngine = new BitwiseOperationsMoveEngine();
    private final SearchEngine searchEngine = new MinimaxAlphaBetaSearchEngine();

    @GetMapping("")
    public String test(@RequestParam(defaultValue = "rnbq1bnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQ1BNR") String fen) {
        websocket.convertAndSend("/topic/test", fen);
        return fen;
    }

    @GetMapping("/game")
    public String game(@RequestParam(defaultValue = "rnbq1bnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQ1BNR") String fen) {
        new Thread(() -> {
            final Random random = new Random();
            Color player = Color.WHITE;
            ChessBoard board = BoardUtils.fenToBitboard(fen);
            websocket.convertAndSend("/topic/test", "BOARD:" + BoardUtils.bitboardToFen(board));
            for (int i = 0; i < 50; i++) {
                try {
                    Thread.sleep(250L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final Move move = searchEngine.getBestMove(board, player, random.nextInt(4) + 1);
                websocket.convertAndSend("/topic/test", "MOVE:" + Long.numberOfTrailingZeros(move.from) + "," + Long.numberOfTrailingZeros(move.to));
                board = moveEngine.makeMove(board, move);
                player = player.getOpposite();
            }
        }).start();
        return fen;
    }

}
