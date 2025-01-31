package balckjack.domain;

import java.util.List;

public enum Number {
    ACE("A", 11),
    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6", 6),
    SEVEN("7", 7),
    EIGHT("8", 8),
    NINE("9", 9),
    TEN("10", 10),
    JACK("J", 10),
    QUEEN("Q", 10),
    KING("K", 10);

    private final String symbol;
    private final int value;

    Number(String symbol, int value) {
        this.symbol = symbol;
        this.value = value;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getValue() {
        return value;
    }

    public static Number convertNumberToSymbol(int number) {
        try {
            return valueOf(String.valueOf(number));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("입력 값은 2에서 10 사이의 숫자여야 합니다.");
        }
    }
}

