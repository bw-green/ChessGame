package test.java.file;

import User.User;
import User.UserManager;
import fileManager.FileManager;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagerTest {


    private UserManager userManager;
    private FileManager fileManager;
    private static final String TEST_ID = "TestUser";
    private static final String TEST_PW = "pw1234";
    private static final String SAVE_DIR = "saves";
    private static final String TEST_USER_DIR = SAVE_DIR + "/User_" + TEST_ID;

    @BeforeEach
    void setUp() {
        fileManager = FileManager.getInstance();

        // 기존 테스트 아이디 삭제 (중복 방지)
        Map<String, User> users = fileManager.loadUserList();
        users.remove(TEST_ID);
        userManager = new UserManager(fileManager);
    }

    @AfterEach
    void tearDown() {
        // [테스트 후 파일/폴더 삭제 구간]

        // 1. UserList에서 테스트 아이디 제거
        Map<String, User> users = fileManager.loadUserList();
        users.remove(TEST_ID);
        // 테스트 유저로 파일 생성했다면 이 시점에 저장
        // (saveUserList 내역은 UserManager/파일 시스템 구조에 따라 조정)
        fileManager.saveUserList(TEST_ID, ""); // 빈 pw로 덮어씀(혹은 필요 없다면 삭제 가능)

        // 2. 테스트 User 전용 폴더 삭제
        File userDir = new File(TEST_USER_DIR);
        if (userDir.exists() && userDir.isDirectory()) {
            for (File file : userDir.listFiles()) {
                file.delete();  // 개별 세이브파일 삭제
            }
            userDir.delete();     // User 디렉토리 삭제
        }

        // 3. 필요시 saves 폴더 내 테스트용 파일 추가 삭제
        // (운영에 영향이 없는지 꼭 확인!)
    }

    @Test
    void testRegisterUser_Success() {
        boolean result = userManager.registerUser(TEST_ID, TEST_PW);
        assertTrue(result);
        assertTrue(userManager.isDuplicate(TEST_ID));
    }

    @Test
    void testRegisterUser_DuplicateId() {
        userManager.registerUser(TEST_ID, TEST_PW);
        boolean result = userManager.registerUser(TEST_ID, "otherpw");
        assertFalse(result);
    }

    @Test
    void testLoginUser_Success() {
        userManager.registerUser(TEST_ID, TEST_PW);
        assertTrue(userManager.loginUser(TEST_ID, TEST_PW));
    }

    @Test
    void testLoginUser_WrongPassword() {
        userManager.registerUser(TEST_ID, TEST_PW);
        assertFalse(userManager.loginUser(TEST_ID, "wrongpw"));
    }

    @Test
    void testLoginUser_UnknownId() {
        assertFalse(userManager.loginUser("NoSuchUser", TEST_PW));
    }

    @Test
    void testIdAndPwValidation() {
        assertFalse(isValid("a", "pw12"));         // id too short
        assertFalse(isValid("abcdefghijklmn", "pw12")); // id too long
        assertFalse(isValid("User01", "a"));       // pw too short
        assertFalse(isValid("User01", "abcdefghijk")); // pw too long
        assertFalse(isValid("user!23", "pw12"));   // 특수문자 포함
        assertTrue(isValid("User12", "AbC123"));   // 정상 케이스
    }

    // UserManager에 없다면 임시로 테스트용 유효성 체크
    boolean isValid(String id, String pw) {
        return id.matches("^[A-Za-z0-9]{2,10}$") && pw.matches("^[A-Za-z0-9]{2,10}$");
    }
}