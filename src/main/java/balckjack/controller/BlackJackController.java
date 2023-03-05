package balckjack.controller;

import balckjack.domain.Command;
import balckjack.domain.Dealer;
import balckjack.domain.Player;
import balckjack.domain.Players;
import balckjack.domain.Referee;
import balckjack.domain.Result;
import balckjack.strategy.CardPicker;
import balckjack.util.Repeater;
import balckjack.view.InputView;
import balckjack.view.OutputView;
import java.util.List;

public class BlackJackController {

    private static final int BURST_CODE = -1;
    private static final int DEALER_HIT_NUMBER = 16;

    public void run(CardPicker cardPicker) {
        final Players players = Repeater.repeatIfError(this::inputPlayerNames,
            OutputView::printErrorMessage);
        final Dealer dealer = new Dealer();
        Referee referee = new Referee();
        dealer.initHit(cardPicker);
        players.initHit(cardPicker);
        OutputView.printInitCardDeck(dealer, players);

        for (Player player : players.getPlayers()) {
            askPlayer(referee, player, cardPicker);
        }
        System.out.println();
        while (referee.calculateDeckScore(dealer.getCardDeck()) <= DEALER_HIT_NUMBER
            && referee.calculateDeckScore(dealer.getCardDeck()) != BURST_CODE) {
            dealer.hit(cardPicker);
            OutputView.printDealerPickMessage(dealer);
        }
        List<Result> results = referee.judgeResult(dealer, players);
        OutputView.printFinalCardDeck(dealer, players, referee);
        OutputView.printResult(referee.countDealerResult(results), dealer, players,
            results);
    }

    private void askPlayer(Referee referee, Player player, CardPicker cardPicker) {
        Command command;
        while (true) {
            command = Repeater.repeatIfError(() -> inputCommand(player),
                OutputView::printErrorMessage);
            if (command == Command.NO) {
                break;
            }
            player.hit(cardPicker);
            OutputView.printParticipantCardDeck(player);
            int score = referee.calculateDeckScore(player.getCardDeck());
            if (score == BURST_CODE) {
                OutputView.printBurstMessage();
                break;
            }
        }
    }

    private Players inputPlayerNames() {
        return new Players(InputView.inputPlayerNames());
    }

    private Command inputCommand(Player player) {
        return Command.toCommand(InputView.inputReply(player.getName().getValue()));
    }
}

