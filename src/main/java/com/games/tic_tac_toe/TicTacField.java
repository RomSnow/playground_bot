package com.games.tic_tac_toe;

import com.games.battleship.Point;

class TicTacField {
    private final CharType[][] field;

    public TicTacField(){
        field = new CharType[3][3];
        for (var i = 0; i < 3; i++)
            for (var j = 0; j < 3; j++)
                field[i][j] = CharType.Empty;
    }

    public boolean setCharOnPosition(CharType sym, Point point){
        var x = point.getX();
        var y = point.getY();
        try {
            var currentSym = field[y][x];
            if (currentSym != CharType.Empty)
                return false;

            field[y][x] = sym;
            return true;
        } catch (IndexOutOfBoundsException throwMsg){
            return false;
        }
    }

    public CharType getCharOnPosition(Point point){
        return field[point.getY()][point.getX()];
    }

    public boolean checkOnCharWin(CharType sym){
        return checkDiagonals(sym) || checkLines(sym, true) ||
                checkLines(sym, false);
    }

    private boolean checkLines(CharType sym, boolean isHorizontal){
        boolean isLineFilled;
        for (var i = 0; i < 3; i++) {
            isLineFilled = true;
            for (var j = 0; j < 3; j++) {
                CharType currentChar;
                if (isHorizontal)
                    currentChar = field[i][j];
                else
                    currentChar = field[j][i];

                if (currentChar != sym) {
                    isLineFilled = false;
                    break;
                }
            }
            if (isLineFilled)
                return true;
        }
        return false;
    }

    private boolean checkDiagonals(CharType sym){
        boolean isDiagonalFilled;
        int x;
        int y;
        int yShift;
        x = 0;
        y = 0;
        yShift = 1;

        for (var i = 0; i < 2; i++){
            isDiagonalFilled = true;
            while (true) {
                if (x < 0 || y < 0 || y > 2 || x > 2)
                    break;

                if (field[y][x] != sym) {
                    isDiagonalFilled = false;
                    break;
                }
                x++;
                y += yShift;
            }
            if (isDiagonalFilled)
                return true;
            else {
                x = 0;
                y = 2;
                yShift = -1;
            }
        }
        return false;
    }
}
