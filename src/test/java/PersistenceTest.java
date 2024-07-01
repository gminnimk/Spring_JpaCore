import com.sparta.entity.Memo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PersistenceTest {
    EntityManagerFactory emf;
    EntityManager em;

    @BeforeEach
    void setUp() {
        // "memo"라는 이름의 Persistence Unit을 사용하여 EntityManagerFactory를 생성
        emf = Persistence.createEntityManagerFactory("memo");
        // EntityManagerFactory를 통해 EntityManager를 생성
        em = emf.createEntityManager();
    }

    // 1차 캐시 : Entity 저장
    @Test
    @DisplayName("1차 캐시 : Entity 저장")
    void test1() {
        // 트랜잭션을 얻어옴
        EntityTransaction et = em.getTransaction();

        // 트랜잭션 시작
        et.begin();

        try {
            // 새로운 Memo 엔티티 생성 및 설정
            Memo memo = new Memo();
            memo.setId(1L);
            memo.setUsername("Robbie");
            memo.setContents("1차 캐시 Entity 저장");

            // 엔티티를 영속성 컨텍스트에 저장 (1차 캐시에 저장됨)
            em.persist(memo);

            // 트랜잭션 커밋
            et.commit();

        } catch (Exception ex) {
            ex.printStackTrace();
            // 예외 발생 시 롤백
            et.rollback();
        } finally {
            // EntityManager 닫기
            em.close();
        }

        // EntityManagerFactory 닫기
        emf.close();
    }

    // Entity 조회 : 캐시 저장소에 해당하는 Id가 존재하지 않은 경우
    @Test
    @DisplayName("Entity 조회 : 캐시 저장소에 해당하는 Id가 존재하지 않은 경우")
    void test2() {
        try {
            // Id가 1인 Memo 엔티티 조회 (1차 캐시에 존재하지 않으면 DB에서 조회)
            Memo memo = em.find(Memo.class, 1);
            System.out.println("memo.getId() = " + memo.getId());
            System.out.println("memo.getUsername() = " + memo.getUsername());
            System.out.println("memo.getContents() = " + memo.getContents());

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // EntityManager 닫기
            em.close();
        }

        // EntityManagerFactory 닫기
        emf.close();
    }

    // Entity 조회 : 캐시 저장소에 해당하는 Id가 존재하는 경우
    @Test
    @DisplayName("Entity 조회 : 캐시 저장소에 해당하는 Id가 존재하는 경우")
    void test3() {
        try {
            // Id가 1인 Memo 엔티티 조회 (1차 캐시에 존재하지 않으면 DB에서 조회)
            Memo memo1 = em.find(Memo.class, 1);
            System.out.println("memo1 조회 후 캐시 저장소에 저장\n");

            // 동일한 Id로 다시 조회 (이번에는 1차 캐시에서 조회됨)
            Memo memo2 = em.find(Memo.class, 1);
            System.out.println("memo2.getId() = " + memo2.getId());
            System.out.println("memo2.getUsername() = " + memo2.getUsername());
            System.out.println("memo2.getContents() = " + memo2.getContents());

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // EntityManager 닫기
            em.close();
        }

        // EntityManagerFactory 닫기
        emf.close();
    }

    // 객체 동일성 보장
    @Test
    @DisplayName("객체 동일성 보장")
    void test4() {
        // 트랜잭션을 얻어옴
        EntityTransaction et = em.getTransaction();

        // 트랜잭션 시작
        et.begin();

        try {
            // 새로운 Memo 엔티티 생성 및 설정
            Memo memo3 = new Memo();
            memo3.setId(2L);
            memo3.setUsername("Robbert");
            memo3.setContents("객체 동일성 보장");
            em.persist(memo3);

            // Id가 1인 Memo 엔티티 조회
            Memo memo1 = em.find(Memo.class, 1);
            // 동일한 Id로 다시 조회
            Memo memo2 = em.find(Memo.class, 1);
            // Id가 2인 Memo 엔티티 조회
            Memo memo  = em.find(Memo.class, 2);

            // memo1과 memo2가 동일한 객체인지 확인 (1차 캐시에 의해 동일 객체 보장)
            System.out.println(memo1 == memo2);
            // memo1과 memo가 동일한 객체인지 확인 (다른 Id이므로 동일 객체 아님)
            System.out.println(memo1 == memo);

            // 트랜잭션 커밋
            et.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            // 예외 발생 시 롤백
            et.rollback();
        } finally {
            // EntityManager 닫기
            em.close();
        }

        // EntityManagerFactory 닫기
        emf.close();
    }

    // Entity 삭제
    @Test
    @DisplayName("Entity 삭제")
    void test5() {
        // 트랜잭션을 얻어옴
        EntityTransaction et = em.getTransaction();

        // 트랜잭션 시작
        et.begin();

        try {
            // Id가 2인 Memo 엔티티 조회
            Memo memo = em.find(Memo.class, 2);

            // 엔티티 삭제
            em.remove(memo);

            // 트랜잭션 커밋
            et.commit();

        } catch (Exception ex) {
            ex.printStackTrace();
            // 예외 발생 시 롤백
            et.rollback();
        } finally {
            // EntityManager 닫기
            em.close();
        }

        // EntityManagerFactory 닫기
        emf.close();
    }

    // 쓰기 지연 저장소 (ActionQueue) 확인
    @Test
    @DisplayName("쓰기 지연 저장소 확인")
    void test6() {
        // 트랜잭션을 얻어옴
        EntityTransaction et = em.getTransaction();

        // 트랜잭션 시작
        et.begin();

        try {
            // 새로운 Memo 엔티티 생성 및 설정
            Memo memo = new Memo();
            memo.setId(2L);
            memo.setUsername("Robbert");
            memo.setContents("쓰기 지연 저장소");
            em.persist(memo);

            // 또 다른 Memo 엔티티 생성 및 설정
            Memo memo2 = new Memo();
            memo2.setId(3L);
            memo2.setUsername("Bob");
            memo2.setContents("과연 저장을 잘 하고 있을까?");
            em.persist(memo2);

            // 트랜잭션 커밋 전 상태 출력
            System.out.println("트랜잭션 commit 전");
            // 트랜잭션 커밋 (쓰기 지연 저장소에 있던 작업들이 DB에 반영됨)
            et.commit();
            // 트랜잭션 커밋 후 상태 출력
            System.out.println("트랜잭션 commit 후");

        } catch (Exception ex) {
            ex.printStackTrace();
            // 예외 발생 시 롤백
            et.rollback();
        } finally {
            // EntityManager 닫기
            em.close();
        }

        // EntityManagerFactory 닫기
        emf.close();
    }

    // flush() 메서드 확인
    @Test
    @DisplayName("flush() 메서드 확인")
    void test7() {
        // 트랜잭션을 얻어옴
        EntityTransaction et = em.getTransaction();

        // 트랜잭션 시작
        et.begin();

        try {
            // 새로운 Memo 엔티티 생성 및 설정
            Memo memo = new Memo();
            memo.setId(4L);
            memo.setUsername("Flush");
            memo.setContents("Flush() 메서드 호출");
            em.persist(memo);

            // flush() 호출 전 상태 출력
            System.out.println("flush() 전");
            // 직접 flush() 호출 (영속성 컨텍스트의 변경 내용을 DB에 반영)
            em.flush();
            // flush() 호출 후 상태 출력
            System.out.println("flush() 후\n");

            // 트랜잭션 커밋 전 상태 출력
            System.out.println("트랜잭션 commit 전");
            // 트랜잭션 커밋
            et.commit();
            // 트랜잭션 커밋 후 상태 출력
            System.out.println("트랜잭션 commit 후");

        } catch (Exception ex) {
            ex.printStackTrace();
            // 예외 발생 시 롤백
            et.rollback();
        } finally {
            // EntityManager 닫기
            em.close();
        }

        // EntityManagerFactory 닫기
        emf.close();
    }

    // 변경 감지 확인
    @Test
    @DisplayName("변경 감지 확인")
    void test8() {
        // 트랜잭션을 얻어옴
        EntityTransaction et = em.getTransaction();

        // 트랜잭션 시작
        et.begin();

        try {
            // 변경할 데이터를 조회
            System.out.println("변경할 데이터를 조회합니다.");
            Memo memo = em.find(Memo.class, 4);
            System.out.println("memo.getId() = " + memo.getId());
            System.out.println("memo.getUsername() = " + memo.getUsername());
            System.out.println("memo.getContents() = " + memo.getContents());

            // 데이터 수정
            System.out.println("\n수정을 진행합니다.");
            memo.setUsername("Update");
            memo.setContents("변경 감지 확인");

            // 트랜잭션 커밋 전 상태 출력
            System.out.println("트랜잭션 commit 전");
            // 트랜잭션 커밋 (변경된 내용이 자동으로 반영됨)
            et.commit();
            // 트랜잭션 커밋 후 상태 출력
            System.out.println("트랜잭션 commit 후");

        } catch (Exception ex) {
            ex.printStackTrace();
            // 예외 발생 시 롤백
            et.rollback();
        } finally {
            // EntityManager 닫기
            em.close();
        }

        // EntityManagerFactory 닫기
        emf.close();
    }
}
