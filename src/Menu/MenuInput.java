package Menu;

import Input.UserInput;
import data.Command;
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
    static int REGISTER = GameInputReturn.REGISTER.getCode();
    static int LOGIN =  GameInputReturn.LOGIN.getCode();
    static int LOGOUT =  GameInputReturn.LOGOUT.getCode();
    static int TOGGLE =   GameInputReturn.TOGGLE.getCode();
    static int OPTION =    GameInputReturn.OPTION.getCode();

    public static int number = 0;
    public static String input;
    public static int toggleNum = -1;
    public static boolean toggleOn = true;
    public static String idStr;
    public static String pwStr;

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

    public static boolean accountInput(boolean idInput){
        while(true){
            if(idInput){
                System.out.print(PrintTemplate.MENU_PROMPT + Command.INPUT_ID.toString());
            }else{
                System.out.print(PrintTemplate.MENU_PROMPT + Command.INPUT_PW.toString());
            }

            Scanner sc = new Scanner(System.in);
            input = sc.nextLine();

            if(input.isEmpty()){
                System.out.println(CommandError.ACC_INVALID_INPUT);
                continue;
            }

            input = blank(input);

            // escape
            if(input.length() == 1 && input.charAt(0) == '0'){
                return false;
            }

            //check string length
            if(input.length() <= 1 || input.length() >= 11){
                System.out.println(PrintTemplate.BOLDLINE);
                System.out.println(CommandError.ACC_INVALID_INPUT);
                System.out.println(PrintTemplate.BOLDLINE);
                continue;
            }

            // check blank between string
            String[] temp = input.split(" ");
            if(temp.length >= 2){
                System.out.println(PrintTemplate.BOLDLINE);
                System.out.println(CommandError.ACC_INVALID_INPUT);
                System.out.println(PrintTemplate.BOLDLINE);
                continue;
            }

            //skipped each character is alphabet or number
            if(idInput){
                idStr = blank(input);
            }else{
                pwStr = blank(input);
            }

            return true;
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
            String now = parts[1].substring("help".length());
            if(checking(now, HELP)!=0){
                number= checking(now, HELP);
                return HELP;
            }
            else{
                throw new InputMismatchException("save 실패");
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
            if(checking(now, SAVE)!=0){
                number= checking(now, SAVE);
                return SAVE;// 파일매니저로 보내기
            }
            else{
                throw new InputMismatchException("save 실패");
            }

        } //인자있음

        else if(parts[1].startsWith("delsave")){
            String now = parts[1].substring("delsave".length());
            if(checking(now, DEL_SAVE)!=0){
                number= checking(now, DEL_SAVE);
                return DEL_SAVE;
            }
            else{
                throw new InputMismatchException("delsave 실패");
            }


        }//인자있음
        else if(parts[1].startsWith("load")){
            String now = parts[1].substring("load".length());
            if(checking(now, LOAD)!=0){
                number= checking(now, LOAD);
                return LOAD; // 파일매니저로보내기
            }
            else{
                throw new InputMismatchException("load 실패");
            }
        }//인자있음
        else if(parts[1].startsWith("start")){
            String now = parts[1].substring("start".length());
            if(checking(now, START)!=0){
                number= checking(now, START);
                return START; // 파일매니저로보내기
            }
            else{
                throw new InputMismatchException("load 실패");
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
        else if(parts[1].startsWith("register")){
            parts[1]=  blank(parts[1]) ;
            if(parts[1].equals("register")){
                return REGISTER;
            }
            else{
                throw new InputMismatchException("register 실패");
            }
        }
        else if(parts[1].startsWith("login")){
            parts[1]=  blank(parts[1]) ;
            if(parts[1].equals("login")){
                return LOGIN;
            }
            else{
                throw new InputMismatchException("login 실패");
            }
        }
        else if(parts[1].startsWith("logout")){
            parts[1]=  blank(parts[1]) ;
            if(parts[1].equals("logout")){
                return LOGOUT;
            }
            else{
                throw new InputMismatchException("logout 실패");
            }
        }
        else if(parts[1].startsWith("option")){
            parts[1]=  blank(parts[1]) ;
            if(parts[1].equals("option")){
                return OPTION;
            }
            else{
                throw new InputMismatchException("option 실패");
            }
        }
        else if(parts[1].startsWith("toggle")){
            String now = parts[1].substring("toggle".length());
            if(checkStr(now)){
                return TOGGLE;
            }
            else{
                throw new InputMismatchException("toggle 실패");
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

    private static int checking(String now, int cmdCode) {
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
            int max_num;
            if(cmdCode == SAVE || cmdCode == LOAD || cmdCode == DEL_SAVE){ max_num = 3; }
            else{ max_num = 4;}
            try{
                num= Integer.parseInt(now);
            } catch (NumberFormatException e) {
                throw new InputMismatchException();
            }

            if (num >= 1 && num <= max_num) {
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

    private static boolean checkStr(String now){
        if(Character.isWhitespace(now.charAt((0)))){
            String[] parts = now.split(" ");

            //앞의 공백까지 포함해서 parts에 "", 특수규칙, on/off 형식으로 들어감. 추후 함수 수정 예정

            if(parts.length != 3) {
                throw new InputMismatchException();
            }
            parts[1] = blank(parts[1]);
            parts[2] = blank(parts[2]);
            boolean isRuleValid = true;
            boolean isOnValid = true;

            // check special rule str
            if(parts[1].startsWith("enpassant")){ toggleNum = 0; }
            else if(parts[1].startsWith("castling")){ toggleNum = 1; }
            else if(parts[1].startsWith("promotion")){ toggleNum = 2; }
            else isRuleValid = false;

            // check on/off str
            if(parts[2].startsWith("on")){toggleOn = true; }
            else if (parts[2].startsWith("off")){ toggleOn = false; }
            else isOnValid = false;

            if(isRuleValid && isOnValid){
                return true;
            }else{
                toggleNum = -1;
                toggleOn = true;
                throw new InputMismatchException();
            }
        }

        return false;
    }
}
