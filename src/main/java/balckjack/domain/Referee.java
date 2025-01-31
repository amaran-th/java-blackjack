package balckjack.domain;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Referee {

    private static final int DEALER_HIT_NUMBER = 16;
    private static final int MAX_ACE_VALUE = 11;

    private static final int MIN_ACE_VALUE = 1;
    private static final int BLACKJACK_SCORE = 21;

    /**
     * 카드 덱의 점수를 계산하는 메서드
     *
     * @param deck 총 점수를 계산하고자 하는 카드 덱
     * @return 일반적인 경우 카드 덱의 총 점수를 반환하고 Burst되는 경우 -1을 반환한다.
     */
    public int calculateDeckScore(CardDeck deck) {
        int commonSum = calculateCommonCardScore(deck);
        int aceSum = calculateAceCardScore(commonSum, deck);
        return commonSum + aceSum;
    }

    private int calculateCommonCardScore(CardDeck deck) {
        return deck.extractStandardCards().stream()
            .mapToInt(Card::getValue).sum();
    }

    public boolean isBurst(CardDeck deck) {
        return calculateDeckScore(deck) > BLACKJACK_SCORE;
    }

    private int calculateAceCardScore(int commonSum, CardDeck deck) {
        int sum = 0;
        int aceCardCount = deck.extractAceCards().size();
        for (int restCount = aceCardCount; restCount > 0; restCount--) {
            int aceScore = decideAceScore(commonSum, restCount);
            commonSum += aceScore;
            sum += aceScore;
        }
        return sum;
    }

    private int decideAceScore(int sum, int restAceCount) {
        if (isAfford(sum, restAceCount)) {
            return MAX_ACE_VALUE;
        }
        return MIN_ACE_VALUE;
    }

    private boolean isAfford(int sum, int restAceCount) {
        final int ACE_VALUE_GAP = MAX_ACE_VALUE - MIN_ACE_VALUE;
        return (BLACKJACK_SCORE - sum) - restAceCount * MIN_ACE_VALUE >= ACE_VALUE_GAP;
    }

    public List<Result> judgeResult(Participant dealer, Players players) {
        int dealerScore = calculateDeckScore(dealer.getCardDeck());
        List<Integer> playerScores = players.getPlayers().stream()
            .map((player) -> calculateDeckScore(player.getCardDeck())).collect(
                Collectors.toList());
        return playerScores.stream().map((score) -> compareScore(dealerScore, score))
            .collect(Collectors.toList());
    }

    public Map<String, Long> countDealerResult(List<Result> results) {
        return results.stream().collect(groupingBy(Result::getResult, counting()));
    }

    private Result compareScore(int dealerScore, int playerScore) {
        if (playerScore > BLACKJACK_SCORE) {
            return Result.LOSE;
        }
        if (dealerScore > BLACKJACK_SCORE || playerScore > dealerScore) {
            return Result.WIN;
        }
        if (playerScore == dealerScore) {
            return Result.DRAW;
        }
        return Result.LOSE;
    }

    public boolean isContinueDealerTurn(Dealer dealer) {
        return calculateDeckScore(dealer.getCardDeck()) <= Referee.DEALER_HIT_NUMBER
            && isBurst(dealer.getCardDeck());
    }


}
