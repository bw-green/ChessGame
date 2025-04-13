package Input;

import data.Command;
import data.CommandError;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.stream.Collectors;

public class GameInput {

    public static String input;



    public static void main(String[] args) {
        gameInput();
    }

    public static void gameInput() { // int로 바꿔서 하기
        System.out.println("again");
        Scanner sc = new Scanner(System.in);
        input = sc.nextLine();
        checkInput();

    }

    private static void checkInput() {
        if(input.isEmpty()){
            System.out.println("Please enter a valid input");
            gameInput();
            return;
        }
        if(input.charAt(0)=='/'){
//            System.out.println("명령어임");
            try{
                checkOrderInput();
            } catch (Exception e) {
                if(e instanceof InputMismatchException){
                    System.out.println(CommandError.WRONG_COMMAND);
                    gameInput();
                }
                else{
                    System.out.println(CommandError.WRONG_NUMBER);
                    gameInput();
                }
            }

        }
        else{
            // 유신님 인풋으로 보내기
            return;
        }

    }
    private static void checkOrderInput()throws Exception{
        String[] parts = input.split("/");
        if(parts.length!=2){
            throw new InputMismatchException("/가 너무 많음");
        }
        if(parts[1].startsWith("help")){
            parts[1]=  blank(parts[1]) ;
            if(parts[1].equals("help")){
                System.out.println(Command.HELP);
            }
            else{
                throw new InputMismatchException("help 실패");
            }
        }
        else if(parts[1].startsWith("savefile")){//인자있음
            String now = parts[1].substring("savefile".length());
            if(checking(now)!=0){
                System.out.println(Command.SAVEFILE);  // 파일매니저에게 넘겨주기
            }
            else{
                throw new InputMismatchException("savefile 실패");
            }

        }
        else if(parts[1].startsWith("save")){
            String now = parts[1].substring("save".length());
            if(checking(now)!=0){
                System.out.println(Command.SAVE.formatStr(checking(now)));// 파일매니저로 보내기
            }
            else{
                throw new InputMismatchException("save 실패");
            }

        } //인자있음

        else if(parts[1].startsWith("delsave")){
            String now = parts[1].substring("delsave".length());
            if(checking(now)!=0){
                System.out.println(CommandError.DELSAVE_BLOCK);
            }
            else{
                throw new InputMismatchException("delsave 실패");
            }


        }//인자있음
        else if(parts[1].startsWith("load")){
            String now = parts[1].substring("load".length());
            if(checking(now)!=0){
                System.out.println(Command.LOAD.formatStr(checking(now))); // 파일매니저로보내기
            }
            else{
                throw new InputMismatchException("load 실패");
            }
        }//인자있음
        else if(parts[1].startsWith("start")){
            parts[1]=blank(parts[1]) ;
            if(parts[1].equals("start")){
                System.out.println(CommandError.DELSAVE_BLOCK);
            }
            else{
                throw new InputMismatchException("start 실패");
            }

        }
        else if(parts[1].startsWith("quit")){
            parts[1]=  blank(parts[1]) ;
            if(parts[1].equals("quit")){
                System.out.println(Command.QUIT);
            }
            else{
                throw new InputMismatchException("quit 실패");
            }
        }
        else if(parts[1].startsWith("exit")){
            parts[1]=  blank(parts[1]) ;
            if(parts[1].equals("exit")){
                System.out.println(Command.EXIT);
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
            int num = Integer.parseInt(now);
            if (num >= 1 && num <= 5) {
                return num;
            }
//            else {
//                System.out.println(num);
//                throw new NumberFormatException(" 1부터 5사이가 아님");
//            }
        } else {
            throw new InputMismatchException();
        }

        return 0;
    }

}
