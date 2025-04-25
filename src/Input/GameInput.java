package Input;

import data.CommandError;
import data.GameInputReturn;
import userinput.UserInput;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.stream.Collectors;

public class GameInput {
    static int NOT_MINE = -1; //유신님 코드 불러야함
    static int ERROR = GameInputReturn.ERROR.getCode();
    static int HELP = GameInputReturn.HELP.getCode();

    static int EXIT = GameInputReturn.EXIT.getCode();
//    static int START = GameInputReturn.START.getCode();
    static int QUIT = GameInputReturn.QUIT.getCode();
    static int SAVE = GameInputReturn.SAVE.getCode();
    static int LOAD = GameInputReturn.LOAD.getCode();
//    static int DEL_SAVE = GameInputReturn.DEL_SAVE.getCode();
    static int SAVE_FILE = GameInputReturn.SAVE_FILE.getCode();


    public static int number = 0;
    public static String input;

    public static void main(String[] args) {
        gameInput();
    }

    public static int gameInput() {
        // 이거 input을 받아서 튀어 나오게 해야할듯
        //return은 int로 하고
        Scanner sc = new Scanner(System.in);
        input = sc.nextLine();
        return checkInput();
    }

    private static int checkInput() {
        if(input.isEmpty()){
            return UserInput.handleInput(input);
        }
        if(input.charAt(0)=='/'){
            try{
                return checkOrderInput();
            } catch (Exception e) {
                if(e instanceof InputMismatchException){
                    System.out.println(CommandError.WRONG_COMMAND);
                }
                else{
                    System.out.println(CommandError.WRONG_NUMBER);
                }
                return ERROR;
            }

        }
        // 유신님 인풋으로 보내기
        return UserInput.handleInput(input);

    }
    private static int checkOrderInput(){
        for(int i = 1; i<input.length(); i++){
            if(input.charAt(i)=='/'){
                throw new InputMismatchException("/가 너무 많음");
            }
        }
        String[] parts = input.split("/");
        if(parts[1].startsWith("help")){
            parts[1]=  blank(parts[1]) ;
            if(parts[1].equals("help")){
                return HELP;
            }
            else{
                throw new InputMismatchException("help 실패");
            }
        }
        else if(parts[1].startsWith("savefile")){//인자있음
            String now = parts[1].substring("savefile".length());
            if(checking(now)!=0){
                number= checking(now);
                return SAVE_FILE;  // 파일매니저에게 넘겨주기
            }
            else{
                throw new InputMismatchException("savefile 실패");
            }

        }
        else if(parts[1].startsWith("save")){
            String now = parts[1].substring("save".length());
            if(checking(now)!=0){
                number= checking(now);
                return SAVE;// 파일매니저로 보내기
            }
            else{
                throw new InputMismatchException("save 실패");
            }

        } //인자있음

        else if(parts[1].startsWith("delsave")){//아님
            String now = parts[1].substring("delsave".length());
            if(checking(now)!=0){
                number= checking(now);
                System.out.println(CommandError.DELSAVE_BLOCK);
                return ERROR;
            }
            else{
                throw new InputMismatchException("delsave 실패");
            }


        }//인자있음
        else if(parts[1].startsWith("load")){
            String now = parts[1].substring("load".length());
            if(checking(now)!=0){
                number= checking(now);
                return LOAD; // 파일매니저로보내기
            }
            else{
                throw new InputMismatchException("load 실패");
            }
        }//인자있음
        else if(parts[1].startsWith("start")){
            parts[1]=blank(parts[1]) ;
            if(parts[1].equals("start")){
                System.out.println(CommandError.DELSAVE_BLOCK);
                return ERROR;
            }
            else{
                throw new InputMismatchException("start 실패");
            }

        }
        else if(parts[1].startsWith("quit")){
            parts[1]=  blank(parts[1]) ;
            if(parts[1].equals("quit")){
                return QUIT;
            }
            else{
                throw new InputMismatchException("quit 실패");
            }
        }
        else if(parts[1].startsWith("exit")){
            parts[1]=  blank(parts[1]) ;
            if(parts[1].equals("exit")){
                return EXIT;
            }
            else{
                throw new InputMismatchException("exit 실패");
            }
        }

        else{
            throw new InputMismatchException("없는 구문");
        }
    }

    private static String blank(String parts){
        return parts.chars()
                .filter(c -> !Character.isWhitespace(c))
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.joining());
    }

    private static int checking(String now) {
        if (Character.isWhitespace(now.charAt(0)) && now.charAt(1) >= '0' && now.charAt(1) <= '9') {
            boolean number = true;
            for(int i=2; i<now.length(); i++){
                boolean b = now.charAt(i) >= '0' && now.charAt(i) <= '9';
//                    if(!b&&!Character.isWhitespace(now.charAt(i))){ throw new Exception(); }
                if(number){
                    if(!(b)){
                        number =false;
                    }
                }
                else{
                    if(b){
                        throw new InputMismatchException();
                    }
                }
            }

            now = blank(now);
            int num;
            try{
                num= Integer.parseInt(now);
            } catch (NumberFormatException e) {
                throw new InputMismatchException();
            }

            if (num >= 1 && num <= 5) {
                return num;
            }
            else {
                throw new NumberFormatException(" 1부터 5사이가 아님");
            }
        } else {
            throw new InputMismatchException();
        }
    }

}
