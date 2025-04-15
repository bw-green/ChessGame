package fileManager;

import board.Board;
import board.PieceFactory;
import piece.Piece;

import java.io.*;
import java.util.*;
import java.util.Random;

public class FileManager {
    private static final int MAX_SAVES = 5;
    private static final String SAVE_DIR = "saves";
    private final String deFault = "NO DATA";

    private Board currentBoard;
    private String currentTurn;
    private final ArrayList<String> filename = new ArrayList<>(Collections.nCopies(MAX_SAVES, "NO DATA"));
    private static final ArrayList<Integer> counter = new ArrayList<>(Collections.nCopies(MAX_SAVES, 0));
    private String lastSavedFile = deFault;
    private int lastSaveFileNum;
    private static int count = 0;
    //moveHistroy, counter, count는 공유되야해서 static으로 선언

    private static FileManager instance = null;

    private FileManager() { //싱글턴 확보
        ensureSaveDirectory();
        loadFileNames();
    }

    public static FileManager getInstance() {
        if (instance == null) {
            instance = new FileManager();
        }
        return instance;
    }

    public ArrayList<String> getFilename() {
        return new ArrayList<>(filename); //복사본 제공
    }

    public void setCurrentBoard(Board board) { this.currentBoard = board; }

    public Board getCurrentBoard() { return currentBoard; }

    public String getLastSavedFile() {
        return lastSavedFile;
    }

    public int getLastSaveFileNum() {
        return lastSaveFileNum;
    }

    // 세이브 디렉토리 확인 및 생성
    private void ensureSaveDirectory() {
        File dir = new File(SAVE_DIR);
        if (!dir.exists()) {
            boolean success = dir.mkdirs();
            if (!success) {
                System.err.println(" Failed to create save directory: " + SAVE_DIR); //임시 출력본
                throw new IllegalStateException("Unable to create save directory. The program will terminate.");
            }
        }
    }

    // 세이브 파일 덮어쓰기 (최대 5개 관리, 텍스트 형식)
    public boolean overWriteSavedFile(int slot) {
        if (slot < 1 || slot > MAX_SAVES) return false;
        slot--;

        String saveName = generateRandomSaveName();
        String filePath = getFilePath(slot + 1);

        if (currentBoard == null) return false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(saveName);
            writer.newLine(); // 두 번째 줄 공백
            writer.newLine();
            writer.write(Objects.equals(currentTurn, "WHITE") ? "White" : "Black"); // 세 번째 줄 턴 정보
            writer.newLine();

            // 💡 여기서 보드 상태 직접 저장 (네 번째 줄부터)
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    var piece = currentBoard.getCell(row, col).getPiece();
                    writer.write((piece == null ? "." : piece.getSymbol()) + " ");
                }
                writer.newLine();
            }

            filename.set(slot, saveName);
            counter.set(slot, ++count);
            lastSavedFile = saveName;
            lastSaveFileNum = slot;

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // 세이브 파일 불러오기
    public boolean loadSavedFile(int slot) {
        if (slot < 1 || slot > MAX_SAVES) {
            //System.out.println("세이브 슬롯 번호는 1~5 사이여야 합니다.");
            return false;
        }
        slot--;
        String filePath = getFilePath(slot + 1);
        Board loadedBoard = new Board();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // 첫 줄: 저장 이름
            reader.readLine(); // 둘째 줄: 공백줄
            currentTurn = reader.readLine(); // 셋째 줄: 턴

            for (int row = 0; row < 8; row++) {
                String line = reader.readLine();
                if (line == null) {return false;} // 줄 수가 부족하면 실패
                String[] tokens = line.trim().split(" ");
                if (tokens.length != 8) {return false;}

                for (int col = 0; col < 8; col++) {
                    String symbol = tokens[col];
                    Piece piece = symbol.equals(".") ? null : PieceFactory.createPieceFromSymbol(symbol);
                    loadedBoard.getCell(row, col).setPiece(piece);
                }
            }

            // 로드된 보드를 현재 보드로 설정
            this.currentBoard = loadedBoard;

            return true;
        } catch (IOException e) {
            return false;
        }
    }


    // 세이브 파일 지우기
    public boolean deleteSavedFile(int slot) {
        if (slot < 1 || slot > MAX_SAVES) {
            //System.out.println("세이브 슬롯 번호는 1~5 사이여야 합니다.");
            return false;
        }
        slot--;
        String filePath = getFilePath(slot+1);
        File saveFile = new File(filePath);

        if (!saveFile.exists()) {
            //System.out.println("삭제할 파일이 존재하지 않습니다: 슬롯 " + slot);
            return false;
        }
        if (lastSavedFile.equals(filename.get(slot))) {
            int secondMax = -1;
            int secondIndex = -1;

            for (int i = 0; i < MAX_SAVES; i++) {
                if (i == slot) continue;
                if (filename.get(i).equals(deFault)) continue;

                int value = counter.get(i);
                if (value > secondMax) {
                    secondMax = value;
                    secondIndex = i;
                }
            }
            if (secondIndex != -1) {
                lastSavedFile = filename.get(secondIndex);
                lastSaveFileNum = secondIndex;
            } else {
                lastSavedFile = deFault;
                lastSaveFileNum = -1;
            }
        }

        if (saveFile.delete()) {
            filename.set(slot, deFault);
            counter.set(slot, 0);
            //System.out.println("세이브 파일이 성공적으로 삭제되었습니다: 슬롯 " + slot);
            return true;
        } else {
            //System.out.println("세이브 파일 삭제에 실패했습니다: 슬롯 " + slot);
            return false;
        }
    }



    private void loadFileNames() {
        for (int i = 1; i <= MAX_SAVES; i++) {
            String filePath = getFilePath(i);
            File file = new File(filePath);

            if (!file.exists()) continue;

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String saveName = reader.readLine();// 첫 줄
                if (saveName != null && saveName.length() == 10) {
                    filename.set(i - 1, saveName);
                }
            } catch (IOException e) {
                //e.printStackTrace(); //디버깅용 후에 주석처리
                //손상된 파일이나 존재하지 않는 파일이나 똑같이 리스트에는 안들어옵니다.
            }
        }
    }

    private static String generateRandomSaveName() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    //중복 문자열 함수 처리
    private String getFilePath(int slot) {return SAVE_DIR + "/savefile" + slot + ".txt";}
}
