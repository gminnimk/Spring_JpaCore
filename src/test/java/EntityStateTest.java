import com.sparta.entity.Memo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EntityStateTest {
    EntityManagerFactory emf;
    EntityManager em;

    @BeforeEach
    void setUp() {
        // "memo"라는 이름의 Persistence Unit을 사용하여 EntityManagerFactory를 생성
        emf = Persistence.createEntityManagerFactory("memo");
        // EntityManagerFactory를 통해 EntityManager를 생성
        em = emf.createEntityManager();
    }

    // 비영속과 영속 상태
    @Test
    @DisplayName("비영속과 영속 상태")
    void test1() {
        // 트랜잭션을 얻어옴
        EntityTransaction et = em.getTransaction();

        // 트랜잭션 시작
        et.begin();

        try {
            // 새로운 Memo 엔티티 생성 (비영속 상태)
            Memo memo = new Memo();
            memo.setId(1L);
            memo.setUsername("Robbie");
            memo.setContents("비영속과 영속 상태");

            // 엔티티를 영속성 컨텍스트에 저장 (영속 상태로 전환)
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

    // 준영속 상태 : detach()
    @Test
    @DisplayName("준영속 상태 : detach()")
    void test2() {
        // 트랜잭션을 얻어옴
        EntityTransaction et = em.getTransaction();

        // 트랜잭션 시작
        et.begin();

        try {
            // Id가 1인 Memo 엔티티 조회 (영속 상태)
            Memo memo = em.find(Memo.class, 1);
            System.out.println("memo.getId() = " + memo.getId());
            System.out.println("memo.getUsername() = " + memo.getUsername());
            System.out.println("memo.getContents() = " + memo.getContents());

            // 엔티티가 영속성 컨텍스트에 포함되어 있는지 확인
            System.out.println("em.contains(memo) = " + em.contains(memo));

            // 엔티티를 준영속 상태로 전환 (영속성 컨텍스트에서 분리)
            System.out.println("detach() 호출");
            em.detach(memo);
            // 엔티티가 영속성 컨텍스트에 포함되어 있는지 다시 확인
            System.out.println("em.contains(memo) = " + em.contains(memo));

            // 준영속 상태의 엔티티 수정 시도
            System.out.println("memo Entity 객체 수정 시도");
            memo.setUsername("Update");
            memo.setContents("memo Entity Update");

            // 트랜잭션 커밋
            System.out.println("트랜잭션 commit 전");
            et.commit();
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

    // 준영속 상태 : clear()
    @Test
    @DisplayName("준영속 상태 : clear()")
    void test3() {
        // 트랜잭션을 얻어옴
        EntityTransaction et = em.getTransaction();

        // 트랜잭션 시작
        et.begin();

        try {
            // Id가 1인 Memo 엔티티 조회 (영속 상태)
            Memo memo1 = em.find(Memo.class, 1);
            // Id가 2인 Memo 엔티티 조회 (영속 상태)
            Memo memo2 = em.find(Memo.class, 2);

            // 엔티티들이 영속성 컨텍스트에 포함되어 있는지 확인
            System.out.println("em.contains(memo1) = " + em.contains(memo1));
            System.out.println("em.contains(memo2) = " + em.contains(memo2));

            // 영속성 컨텍스트 초기화 (모든 엔티티가 준영속 상태로 전환)
            System.out.println("clear() 호출");
            em.clear();
            // 엔티티들이 영속성 컨텍스트에 포함되어 있는지 다시 확인
            System.out.println("em.contains(memo1) = " + em.contains(memo1));
            System.out.println("em.contains(memo2) = " + em.contains(memo2));

            // Id가 1인 Memo 엔티티 다시 조회 (영속 상태로 전환)
            System.out.println("memo#1 Entity 다시 조회");
            Memo memo = em.find(Memo.class, 1);
            System.out.println("em.contains(memo) = " + em.contains(memo));
            // 다시 조회한 엔티티 수정 시도
            System.out.println("\n memo Entity 수정 시도");
            memo.setUsername("Update");
            memo.setContents("memo Entity Update");

            // 트랜잭션 커밋
            System.out.println("트랜잭션 commit 전");
            et.commit();
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

    // 준영속 상태 : close()
    @Test
    @DisplayName("준영속 상태 : close()")
    void test4() {
        // 트랜잭션을 얻어옴
        EntityTransaction et = em.getTransaction();

        // 트랜잭션 시작
        et.begin();

        try {
            // Id가 1인 Memo 엔티티 조회 (영속 상태)
            Memo memo1 = em.find(Memo.class, 1);
            // Id가 2인 Memo 엔티티 조회 (영속 상태)
            Memo memo2 = em.find(Memo.class, 2);

            // 엔티티들이 영속성 컨텍스트에 포함되어 있는지 확인
            System.out.println("em.contains(memo1) = " + em.contains(memo1));
            System.out.println("em.contains(memo2) = " + em.contains(memo2));

            // EntityManager 닫기 (모든 엔티티가 준영속 상태로 전환)
            System.out.println("close() 호출");
            em.close();

            // EntityManager가 닫힌 후에는 엔티티를 다시 조회할 수 없음 (예외 발생)
            Memo memo = em.find(Memo.class, 2); // Session/EntityManager is closed 메시지와 함께 오류 발생
            System.out.println("memo.getId() = " + memo.getId());

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

    // merge() : 저장
    @Test
    @DisplayName("merge() : 저장")
    void test5() {
        // 트랜잭션을 얻어옴
        EntityTransaction et = em.getTransaction();

        // 트랜잭션 시작
        et.begin();

        try {
            // 새로운 Memo 엔티티 생성 (비영속 상태)
            Memo memo = new Memo();
            memo.setId(3L);
            memo.setUsername("merge()");
            memo.setContents("merge() 저장");

            // merge() 호출하여 비영속 상태의 엔티티를 영속 상태로 전환
            System.out.println("merge() 호출");
            Memo mergedMemo = em.merge(memo);

            // 엔티티들이 영속성 컨텍스트에 포함되어 있는지 확인
            System.out.println("em.contains(memo) = " + em.contains(memo));
            System.out.println("em.contains(mergedMemo) = " + em.contains(mergedMemo));

            // 트랜잭션 커밋
            System.out.println("트랜잭션 commit 전");
            et.commit();
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

    // merge() : 수정
    @Test
    @DisplayName("merge() : 수정")
    void test6() {
        // 트랜잭션을 얻어옴
        EntityTransaction et = em.getTransaction();

        // 트랜잭션 시작
        et.begin();

        try {
            // Id가 3인 Memo 엔티티 조회 (영속 상태)
            Memo memo = em.find(Memo.class, 3);
            System.out.println("memo.getId() = " + memo.getId());
            System.out.println("memo.getUsername() = " + memo.getUsername());
            System.out.println("memo.getContents() = " + memo.getContents());

            // 엔티티가 영속성 컨텍스트에 포함되어 있는지 확인
            System.out.println("em.contains(memo) = " + em.contains(memo));

            // 엔티티를 준영속 상태로 전환
            System.out.println("detach() 호출");
            em.detach(memo);
            // 엔티티가 영속성 컨텍스트에 포함되어 있는지 확인
            System.out.println("em.contains(memo) = " + em.contains(memo));

            // 준영속 상태의 엔티티 수정 시도
            System.out.println("준영속 memo 값 수정");
            memo.setContents("merge() 수정");

            // merge() 호출하여 준영속 상태의 엔티티를 다시 영속 상태로 전환
            System.out.println("\n merge() 호출");
            Memo mergedMemo = em.merge(memo);
            System.out.println("mergedMemo.getContents() = " + mergedMemo.getContents());

            // 엔티티들이 영속성 컨텍스트에 포함되어 있는지 확인
            System.out.println("em.contains(memo) = " + em.contains(memo));
            System.out.println("em.contains(mergedMemo) = " + em.contains(mergedMemo));

            // 트랜잭션 커밋
            System.out.println("트랜잭션 commit 전");
            et.commit();
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