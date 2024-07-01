import com.sparta.entity.Memo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EntityTest {

    // 엔티티 매니저 팩토리와 엔티티 매니저 객체 선언
    EntityManagerFactory emf;
    EntityManager em;

    // 각 테스트 실행 전 설정 작업
    @BeforeEach
    void setUp() {
        // 'memo'라는 이름의 퍼시스턴스 유닛을 기반으로 엔티티 매니저 팩토리 생성
        emf = Persistence.createEntityManagerFactory("memo");
        // 엔티티 매니저 팩토리에서 엔티티 매니저 생성
        em = emf.createEntityManager();
    }

    // 테스트 메소드 (구현 필요)
    @Test
    void test1() {
        // 테스트 코드 작성
    }
}






// EntityTransaction 성공 테스트
    @Test
    @DisplayName("EntityTransaction 성공 테스트")
    void test1() {
        EntityTransaction et = em.getTransaction(); // EntityManager 에서 EntityTransaction 을 가져옵니다.

        et.begin(); // 트랜잭션을 시작합니다.

        try { // DB 작업을 수행합니다.

            Memo memo = new Memo(); // 저장할 Entity 객체를 생성합니다.
            memo.setId(1L); // 식별자 값을 넣어줍니다.
            memo.setUsername("Robbie");
            memo.setContents("영속성 컨텍스트와 트랜잭션 이해하기");

            em.persist(memo); // EntityManager 사용하여 memo 객체를 영속성 컨텍스트에 저장합니다.

            et.commit(); // 오류가 발생하지 않고 정상적으로 수행되었다면 commit 을 호출합니다.
            // commit 이 호출되면서 DB 에 수행한 DB 작업들이 반영됩니다.
        } catch (Exception ex) {
            ex.printStackTrace();
            et.rollback(); // DB 작업 중 오류 발생 시 rollback 을 호출합니다.
        } finally {
            em.close(); // 사용한 EntityManager 를 종료합니다.
        }

        emf.close(); // 사용한 EntityManagerFactory 를 종료합니다.
    }



    // EntityTransaction 실패 테스트
    @Test
    @DisplayName("EntityTransaction 실패 테스트")
    void test2() {
        EntityTransaction et = em.getTransaction(); // EntityManager 에서 EntityTransaction 을 가져옵니다.

        et.begin(); // 트랜잭션을 시작합니다.

        try { // DB 작업을 수행합니다.

            Memo memo = new Memo(); // 저장할 Entity 객체를 생성합니다.
            memo.setUsername("Robbert");
            memo.setContents("실패 케이스");

            em.persist(memo); // EntityManager 사용하여 memo 객체를 영속성 컨텍스트에 저장합니다.

            et.commit(); // 오류가 발생하지 않고 정상적으로 수행되었다면 commit 을 호출합니다.
            // commit 이 호출되면서 DB 에 수행한 DB 작업들이 반영됩니다.
        } catch (Exception ex) {
            System.out.println("식별자 값을 넣어주지 않아 오류가 발생했습니다.");
            ex.printStackTrace();
            et.rollback(); // DB 작업 중 오류 발생 시 rollback 을 호출합니다.
        } finally {
            em.close(); // 사용한 EntityManager 를 종료합니다.
        }

        emf.close(); // 사용한 EntityManagerFactory 를 종료합니다.
    }
}