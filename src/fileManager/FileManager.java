package fileManager;

import java.io.*;
import java.util.*;
import java.util.Random;

public class FileManager {
    private static final int MAX_SAVES = 5;
    private static final String SAVE_DIR = "saves";

    private static final FileManager instance = new FileManager();

    private static final Deque<String> moveHistory = new ArrayDeque<>();
    private final ArrayList<String> filename = new ArrayList<>(Collections.nCopies(MAX_SAVES, "NO DATA"));
    private static final ArrayList<Integer> counter = new ArrayList<>(Collections.nCopies(MAX_SAVES, 0));
    private String deFault = "NO DATA";
    private String lastSavedFile = deFault;
    private int lastSaveFileNum;
    private static int count = 0;
    //moveHistroy, counter, count는 공유되야해서 static으로 선언
    public FileManager() {
        ensureSaveDirectory();
        loadFileNames();
    }


    public static FileManager getInstance() { return instance; }

    public ArrayList<String> getFilename() {
        return new ArrayList<>(filename); //복사본 제공
    }

    public ArrayList<String> getMoveHistory() {
        return new ArrayList<>(moveHistory); // 복사본만 제공
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
            dir.mkdirs();
        }
    }

    // Deque에 움직임 저장
    public void addHistory(String string) { moveHistory.addLast(string);}

    public void clearMoveHistory() { moveHistory.clear(); } // 종료 관련 명령어의 경우에만 실행


    // 세이브 파일 덮어쓰기 (최대 5개 관리, 텍스트 형식)
    public boolean overWriteSavedFile(int slot) {
        if (slot < 1 || slot > MAX_SAVES) {
            return false;
        }
        slot--;
        String saveName = generateRandomSaveName();
        String filePath = getFilePath(slot+1);
        filename.set(slot, saveName);
        counter.set(slot, ++count);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(saveName); // 첫 번째 줄: 무작위 세이브 이름
            writer.newLine();
            writer.newLine(); // 두 번째 줄: 공백
            for (String move : moveHistory) {
                writer.write(move); // 세 번째 줄부터: moveHistory 내용
                writer.newLine();
            }
            lastSavedFile = saveName;
            lastSaveFileNum = slot;
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
        slot--;
        String filePath = getFilePath(slot+1);
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
        slot--;
        String filePath = getFilePath(slot+1);
        File saveFile = new File(filePath);

        if (!saveFile.exists()) {
            //System.out.println("삭제할 파일이 존재하지 않습니다: 슬롯 " + slot);
            return false;
        }
        if (lastSavedFile.equals(filename.get(slot))) {
            int secondMax = Integer.MIN_VALUE;
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

    // 현재 움직임 출력 (디버깅용)
    public void printHistory() {
        System.out.println("현재 움직임 기록: " + moveHistory);
    }
}
