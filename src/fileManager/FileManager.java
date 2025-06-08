package fileManager;

import User.User;
import board.Board;
import board.Chaturanga;
import board.ThreeCheckBoard;
import data.PieceColor;
//import data.FileError;
import piece.King;
import piece.Pawn2;
import piece.Rook;
import piece.Pawn;

import java.io.*;
import java.util.*;
import java.util.Random;

import static gameManager.GameManager.USER_ID;

public class FileManager {
    private static final int MAX_SAVES = 3;
    private static final String SAVE_DIR = "saves";
    private static final String USER_DIR = SAVE_DIR + "/User_" + USER_ID;
    private final String deFault = "No Data"; //기획서 일치
    private final String LSFdeFault = "Last saved file";

    private final ArrayList<String> filename = new ArrayList<>(Collections.nCopies(MAX_SAVES, "NO DATA"));
    private static final ArrayList<Integer> counter = new ArrayList<>(Collections.nCopies(MAX_SAVES, 0));
    private String lastSavedFile = LSFdeFault;
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
                //System.err.println(FileError.FAILED_MAKDIR + SAVE_DIR); //임시 출력본
                throw new IllegalStateException();
            }
        }
    }

    // 유저별 폴더 있는지 확인
    private void ensureDirectoryByID() {
        File dir = new File(USER_DIR);
        if (!dir.exists()) {
            boolean success = dir.mkdirs();
            if (!success) {
                // 로그 출력이나 에러 코드 사용 가능
                // System.err.println(FileError.FAILED_MAKDIR + USER_DIR);
                throw new IllegalStateException("Failed to create user directory: " + USER_DIR);
            }
        }
    }

    // 세이브 파일 덮어쓰기 (최대 3개 관리, 텍스트 형식)
    public boolean overWriteSavedFile(int slot, Board board) {
        if (slot < 1 || slot > MAX_SAVES) return false;
        slot--;

        if (!new File(SAVE_DIR).exists()) {
            ensureSaveDirectory();
        }

        if (!new File(USER_DIR).exists()) {
            ensureDirectoryByID();
        }

        String saveName = generateRandomSaveName();
        String filePath = getFilePath(slot + 1); //savefile 1~3생성을 위해 +1

        if (board == null) return false;
        int gameType = 1;

        if(board instanceof ThreeCheckBoard) gameType = 2;
        if(board instanceof Chaturanga) gameType = 3;
        //if(board instanceof PawnGame) gameType = 4;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(USER_ID); //1. ID
            writer.newLine();

            writer.write(saveName); //2. 세이브파일이름
            writer.newLine();

            writer.write(gameType); //3. 게임유형
            writer.newLine();

            writer.write(board.canCastling ? 1:0); //4. 캐슬링
            writer.newLine();

            writer.write(board.canPromotion ? 1:0); //5. 프로모션
            writer.newLine();

            writer.write(board.canEnpassant ? 1:0); //6. 앙파상
            writer.newLine();

            if(board instanceof ThreeCheckBoard threeCheckBoard)
                writer.write(threeCheckBoard.ThreeCheckW); //7. 쓰리체크 W
            else {writer.write("-1");} //7. 쓰리체크 W
            writer.newLine();

            if(board instanceof ThreeCheckBoard threeCheckBoard)
                writer.write(threeCheckBoard.ThreeCheckB); //8. 쓰리체크 B
            else {writer.write("-1");} //8. 쓰리체크 B
            writer.newLine();

            writer.write("board:"); //9. 보드 구분 쓰기
            writer.newLine();
            writer.write(board.getCurrentTurn() == PieceColor.WHITE ? "WHITE" : "BLACK"); // 10. 턴 정보
            writer.newLine();

            // 여기서 특수 룰 상태 저장 시작
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    var piece = board.getCell(row, col).getPiece();
                    if (piece == null) continue;

                    // Pawn 앙파상 체크
                    if (piece instanceof Pawn pawn) {
                        if (pawn.enPassantable && pawn.enPassantCounter == 1) {
                            writer.write(pawn.getSymbol() + " " + row + " " + col);
                            writer.newLine();
                        }
                        if (!pawn.isMoved) {
                            writer.write(pawn.getSymbol() + "f " + row + " " + col); //f을 붙여줘서 일반 Pawn과 구분
                            writer.newLine();
                        }
                    }

                    // Pawn2 첫 움직임 체크(폰게임 전문)
                    else if (piece instanceof Pawn2 pawn2) {
                        if (!pawn2.isMoved) {
                            writer.write(pawn2.getSymbol() + " " + row + " " + col); //z로 저장됨.
                            writer.newLine();
                        }
                    }

                    // King
                    else if (piece instanceof King king) {
                        if (king.firstMove) {
                            writer.write(king.getSymbol() + " " + row + " " + col);
                            writer.newLine();
                        }
                    }

                    // Rook
                    else if (piece instanceof Rook rook) {
                        if (rook.firstMove) {
                            writer.write(rook.getSymbol() + " " + row + " " + col);
                            writer.newLine();
                        }
                    }


                }
            }

            // 여기서 보드 상태 직접 저장
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    var piece = board.getCell(row, col).getPiece();
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
            //System.out.println(FileError.DEBUG_ERROR_OVERWRITE); //디버깅용
            return false;
        }
    }

    // 세이브 파일 불러오기
    public Board loadSavedFile(int slot) {
        if (slot < 1 || slot > MAX_SAVES ) return null;
        if(filename.get(slot-1).equals(deFault)) return null;
        String filePath = getFilePath(slot);

        List<String> checkList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                checkList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace(); // 또는 로깅/예외 처리
        }
        SaveIntegrityChecker check = new SaveIntegrityChecker(checkList);
        return check.validateFile();
    }


    // 세이브 파일 지우기
    public boolean deleteSavedFile(int slot) {
        if (slot < 1 || slot > MAX_SAVES) {
            //System.out.println("세이브 슬롯 번호는 1~5 사이여야 합니다.");
            return false;
        }
        slot--;
        String filePath = getFilePath(slot+1); //savefile 1~5삭제을 위해 +1
        File saveFile = new File(filePath);

        if (!saveFile.exists()) {
            //System.out.println("삭제할 파일이 존재하지 않습니다: 슬롯 " + slot);
            return false;
        }
        if (!filename.get(slot).equals(deFault) &&
                lastSavedFile.equals(filename.get(slot))) { //last saved file update (start)
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
                lastSavedFile = LSFdeFault;
                lastSaveFileNum = -1;
            }
        }   //last saved file update (end)

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

    public boolean isEmptySlot(int slot) {
        if (filename.get(slot - 1).equals(deFault)) return true; // 기본값이면 비어있음으로 간주

        String filePath = getFilePath(slot);
        File file = new File(filePath);

        if (!file.exists()) return true; // 파일이 없으면 비어있음

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.readLine() == null; // 첫 줄이 없으면 비어있는 파일
        } catch (IOException e) {
            // 읽기 실패는 비어있는 것으로 간주하거나 false 반환 가능
            return true;
        }
    }

    // 저장
    public boolean saveUserList(Map<String, User> users) {

        if (!new File(SAVE_DIR).exists()) {
            ensureSaveDirectory();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_DIR + "/userlist.txt"))) {
            for (Map.Entry<String, User> entry : users.entrySet()) {
                User user = entry.getValue();
                writer.write(user.getId() + "," + user.getPw() + ",");
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            // System.err.println(FileError.FAILED_TO_SAVE_USER);
            return false;
        }
    }

    // 불러오기
    public Map<String,User> loadUserList() {
        ensureSaveDirectory(); // 디렉토리 존재 확인

        File file = new File(SAVE_DIR + "/userlist.txt");
        Map<String, User> users = new HashMap<>();

        // 파일이 존재하지 않으면 빈 리스트 반환
        if (!file.exists()) return users;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] tokens = line.split(",", 2); // "," 기준으로 최대 2개 분리
                if (tokens.length != 2) continue;

                String id = tokens[0].trim();
                String pw = tokens[1].trim();

                // 마지막 쉼표 제거 (파일에 "id, pw," 형식일 경우)
                if (pw.endsWith(",")) pw = pw.substring(0, pw.length() - 1);

                if (!id.isEmpty() && !pw.isEmpty()) {
                    users.put(id, new User(id, pw));
                }
            }
        } catch (IOException e) {
            // 에러 출력 또는 FileError 사용
            // System.err.println("사용자 파일 로딩 실패: " + e.getMessage());
        }

        return users;
    }







    public void resetTestState() { //Test 제작을 위한 함수 절대 임의로 호출하지 말것!!
        for (int i = 0; i < MAX_SAVES; i++) {
            filename.set(i, "NO DATA");
            counter.set(i, 0);
        }
        count = 0;
        lastSavedFile = LSFdeFault;
        lastSaveFileNum = -1;
    }


    private void loadFileNames() {
        for (int i = 1; i <= MAX_SAVES; i++) {
            String filePath = getFilePath(i);
            File file = new File(filePath);

            if (!file.exists()) {
                filename.set(i - 1, deFault);
                counter.set(i - 1, 0);
                continue;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String saveName = reader.readLine();// 첫 줄
                if (saveName != null && saveName.length() == 10) {
                    filename.set(i - 1, saveName);
                }
            } catch (IOException e) {
                //System.out.println(FileError.DEBUG_ERROR_LOAD_FN); //디버깅용
                //e.printStackTrace(); //디버깅용 후에 주석처리
                //손상된 파일이나 존재하지 않는 파일이나 똑같이 리스트에는 안들어옵니다.
            }
        }
    }

    private String generateRandomSaveName() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random r = new Random();

        while (true) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                sb.append(chars.charAt(r.nextInt(chars.length())));
            }
            String candidate = sb.toString();
            // 중복 검사
            if (!filename.contains(candidate)) {
                return candidate;
            }
            // 중복이면 다시 생성
        }
    }

    //중복 문자열 함수 처리
    private String getFilePath(int slot) {return USER_DIR + "/savefile" + slot + ".txt";}
}
