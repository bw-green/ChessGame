package userinput;

import data.GameInputReturn;

/**
     * UserInput 클래스
     * - GameInput 클래스에서 '/' 없이 들어온 사용자 입력(좌표 문자열)을 넘겨받아
     *   해당 좌표가 문법적으로 유효한지 검사하고, 내부에 from/to 좌표를 저장한다.
     * - 의미 검증(기물이 있는지 등)은 GameManager 또는 Board 클래스에서 수행한다.
     */
    public class UserInput {
        private static int fromRow, fromCol;
        private static int toRow, toCol;
        private static String fromNotation;
        private static String toNotation;

        /**
         * 전달된 좌표 문자열을 파싱하고 문법 유효성을 검사한 뒤, 내부 좌표로 변환
         * @param input 사용자로부터 전달된 좌표 문자열 (예: "e2 e4")
         * @return 유효한 입력이면 true, 그렇지 않으면 false
         */
        public static int handleInput(String input) {
            if (input == null || input.trim().isEmpty()) return GameInputReturn.ERROR.getCode();

            String[] tokens = input.trim().split("\\s+");
            if (tokens.length != 2) return GameInputReturn.ERROR.getCode();

            String from = tokens[0].toLowerCase();
            String to = tokens[1].toLowerCase();

            if (!isValidNotation(from) || !isValidNotation(to)) return GameInputReturn.ERROR.getCode();

            // 좌표값 저장
            fromRow = 8 - Character.getNumericValue(from.charAt(1));
            fromCol = from.charAt(0) - 'a';
            toRow = 8 - Character.getNumericValue(to.charAt(1));
            toCol = to.charAt(0) - 'a';

            fromNotation = from;
            toNotation = to;

            return GameInputReturn.COORDINATE_TRUE.getCode();
        }

        /**
         * a1 ~ h8 범위인지 확인
         */
        private static boolean isValidNotation(String pos) {
            return pos.length() == 2 &&
                    pos.charAt(0) >= 'a' && pos.charAt(0) <= 'h' &&
                    pos.charAt(1) >= '1' && pos.charAt(1) <= '8';
        }

        // getter methods
        public static int getFromRow() { return fromRow; }
        public static int getFromCol() { return fromCol; }
        public static int getToRow() { return toRow; }
        public static int getToCol() { return toCol; }
        public static String getFromNotation() { return fromNotation; }
        public static String getToNotation() { return toNotation; }

        /**
         * "e2 e4" 형태로 움직임을 문자열로 반환 (FileManager 기록용)
         */
        @Override
        public String toString() {
            return fromNotation + " " + toNotation;
        }
    }

