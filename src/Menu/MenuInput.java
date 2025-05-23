package Menu;

import Input.UserInput;
import data.CommandError;
import data.GameInputReturn;
import data.PrintTemplate;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MenuInput {
    static int ERROR = GameInputReturn.ERROR.getCode();
    static int HELP = GameInputReturn.HELP.getCode();

    static int EXIT = GameInputReturn.EXIT.getCode();
    static int START = GameInputReturn.START.getCode();
    static int QUIT = GameInputReturn.QUIT.getCode();
    static int SAVE = GameInputReturn.SAVE.getCode();
    static int LOAD = GameInputReturn.LOAD.getCode();
    static int DEL_SAVE = GameInputReturn.DEL_SAVE.getCode();
    static int SAVE_FILE = GameInputReturn.SAVE_FILE.getCode();


    public static int number = 0;
    public static String input;

    public static boolean yesOrNoInput(){
        while(true){
            Scanner sc = new Scanner(System.in);
            input = sc.nextLine();

            if(input.isEmpty()){
                System.out.println(CommandError.WRONG_COMMAND);
                continue;
            }

            if(input.charAt(0) == 'y' && input.length() == 1){
                return true;
            }
            if(input.charAt(0) == 'n' && input.length() == 1){
                return false;
            }
            System.out.println(CommandError.WRONG_COMMAND);
        }

    }

    public static int menuInput() {
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
                if(e instanceof NumberFormatException){
                    System.out.println(PrintTemplate.BOLDLINE+ "\n" +
                            CommandError.WRONG_NUMBER+ "\n" +
                            PrintTemplate.BOLDLINE);
                }
                else{
                    System.out.println(PrintTemplate.BOLDLINE + "\n" +
                            CommandError.WRONG_COMMAND+ "\n" +
                            PrintTemplate.BOLDLINE);
                }
                return ERROR;
            }

        }

        System.out.println(PrintTemplate.BOLDLINE+ "\n" +
                CommandError.WRONG_COMMAND + "\n" +
                PrintTemplate.BOLDLINE);
        return ERROR;

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
            parts[1] =  blank(parts[1]);
            if(parts[1].equals("savefile")){
                return SAVE_FILE;
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

        else if(parts[1].startsWith("delsave")){
            String now = parts[1].substring("delsave".length());
            if(checking(now)!=0){
                number= checking(now);
                return DEL_SAVE;
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
                return START;
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
            int num=0;
            try{
                num= Integer.parseInt(now);
            } catch (NumberFormatException e) {
                throw new InputMismatchException();
            }

            if (num >= 1 && num <= 5) {
                return num;
            }
            if(num>=0 && num <=9){
                throw new NumberFormatException(" 1부터 5사이가 아님");
            }
            else {
                throw new InputMismatchException();
            }
        } else {
            throw new InputMismatchException();
        }
    }
}
