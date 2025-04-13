package fileManager;

import data.FileError;
import java.io.*;
import java.util.*;
import java.util.Random;

public class FileManager {
    private static final int MAX_SAVES = 5;
    private static final String SAVE_DIR = "saves";
    private Deque<String> moveHistory;
    private ArrayList<String> filename;

    public FileManager() {
        this.moveHistory = new ArrayDeque<>();
        filename = new ArrayList<>(Collections.nCopies(MAX_SAVES, ""));
        ensureSaveDirectory();
        loadFileNames();
    }


    public ArrayList<String> getFilename() {
        return new ArrayList<>(filename); //복사본 제공
    }

    public ArrayList<String> getMoveHistory() {
        return new ArrayList<>(moveHistory); // 복사본만 제공
    }

    // 세이브 디렉토리 확인 및 생성
    private void ensureSaveDirectory() {
        File dir = new File(SAVE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // Deque에 입력 받을 때 무결섬을 재차 검사해야하나

//    // Deque에 움직임 저장 (Input으로 정제된 움직임만 받음. Input이 정제되는건 Input 클래스에서 통제)
//    public void overWriteHistory(Input input) {
//        String str = input.makeInput(); // input 객체에서 입력받을 문자열 반환
//        moveHistory.addLast(str);
//    }



    // 세이브 파일 덮어쓰기 (최대 5개 관리, 텍스트 형식)
    public boolean overWriteSavedFile(int slot) {
        if (slot < 1 || slot > MAX_SAVES) {
            return false;
        }
        String saveName = generateRandomSaveName();
        String filePath = getFilePath(slot);
        filename.set(slot -1, saveName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(saveName); // 첫 번째 줄: 무작위 세이브 이름
            writer.newLine();
            writer.newLine(); // 두 번째 줄: 공백
            for (String move : moveHistory) {
                writer.write(move); // 세 번째 줄부터: moveHistory 내용
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            //e.printStackTrace(); //디버깅용 후에 주석처리
            return false;
        }
    }

    // 세이브 파일 불러오기
    public boolean loadSavedFile(int slot) {
        if (slot < 1 || slot > MAX_SAVES) {
            //System.out.println("세이브 슬롯 번호는 1~5 사이여야 합니다.");
            return false;
        }
        String filePath = getFilePath(slot);
        moveHistory.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();// 첫째 줄: 세이브 이름
            reader.readLine();// 둘째 줄: 공백줄
            String line;
            while ((line = reader.readLine()) != null) {
                moveHistory.addLast(line);
            }
            //System.out.println("로드 완료: " + filePath);
            return true;
        } catch (IOException e) {
            //e.printStackTrace(); //디버깅용 후에 주석처리
            return false;
            //System.out.println("파일을 찾을 수 없습니다: " + filePath);
        }
    }

    // 세이브 파일 지우기
    public boolean deleteSavedFile(int slot) {
        if (slot < 1 || slot > MAX_SAVES) {
            //System.out.println("세이브 슬롯 번호는 1~5 사이여야 합니다.");
            return false;
        }
        String filePath = getFilePath(slot);
        File saveFile = new File(filePath);

        if (!saveFile.exists()) {
            //System.out.println("삭제할 파일이 존재하지 않습니다: 슬롯 " + slot);
            return false;
        }

        if (saveFile.delete()) {
            filename.set(slot -1, "");
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

    // 현재 움직임 출력 (디버깅용)
    public void printHistory() {
        System.out.println("현재 움직임 기록: " + moveHistory);
    }
}
