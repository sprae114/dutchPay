Repository에서 세가지 메서드와 인덱싱에 대해서 성능비교를 해보았습니다.

## 리포지토리 구성 및 설명
```java
@RepositoryRestResource  
public interface FriendRepository extends  
        JpaRepository<Friend, Long>,  
        QuerydslPredicateExecutor<Friend> {  
  
    List<Friend> findAllByUserAccountId(Long userAccountId);  
  
    @Query("select f from Friend f where f.userAccount.id = :accountId")  
    List<Friend> findAllByAccountIdQueryOptimization(Long accountId);  
  
    @Query("SELECT f FROM Friend f JOIN FETCH f.userAccount WHERE f.userAccount.id = :accountId")  
    List<Friend> findAllByAccountIdWithFetchJoin(Long accountId);  
}
```

1.  `List<Friend> findAllByUserAccountId(Long userAccountId);`
    이 메서드는 **Spring Data JPA가 제공하는 메서드 이름 규칙에 따라 작성된 메서드**입니다. 이 메서드는 UserAccount 엔티티의 ID를 기반으로 모든 친구 목록을 검색합니다. 이 방법은 메서드 이름만으로 쿼리를 생성하기 때문에 별도의 쿼리 정의가 필요하지 않습니다. 그러나 이 방법은 성능 최적화에 제한이 있을 수 있습니다.


2.  `List<Friend> findAllByAccountIdQueryOptimization(Long accountId);`
    이 메서드는 **JPQL 쿼리를 사용**하여 친구 목록을 검색하는 메서드입니다. @Query 어노테이션을 사용하여 쿼리를 직접 작성하고, 쿼리의 accountId 매개변수를 전달합니다. 이 메서드는 첫 번째 메서드와 동일한 결과를 반환하지만, JPQL을 사용하므로 성능 최적화가 가능합니다.


3.  `List<Friend> findAllByAccountIdWithFetchJoin(Long accountId);`
    이 메서드는 **성능 최적화를 위해 fetch join을 사용**한 JPQL 쿼리를 구현한 메서드입니다. fetch join을 사용하면 관련된 엔티티를 한 번의 쿼리로 함께 가져올 수 있습니다. 이로 인해 N+1 문제를 해결하고 성능을 개선할 수 있습니다.


## 성능 비교하기
- 테스트 환경
  UserAccount 클래스 1,000개의 데이터와 Friend 20,000개의 데이터로 구성하여 성능을 비교했습니다.

---

1. `List<Friend> findAllByUserAccountId(Long userAccountId);`
   ![Pasted image 20230426190055](https://user-images.githubusercontent.com/52237184/234725692-6c875ca5-2765-472e-a593-64e3ac4da7fb.png)
   => 여러 번 테스트 한 결과  120 ~ 130ms의 성능이 나왔습니다. left outer join을 사용하여 다른 메서드에 비해 가장 느린 것으로 나왔습니다.

----

2. `List<Friend> findAllByAccountIdQueryOptimization(Long accountId);`
  ![Pasted image 20230426190211](https://user-images.githubusercontent.com/52237184/234725702-a8be2a9b-35f5-4473-8826-e28563b58d9f.png)
  => 여러 번 테스트 한 결과 80~90ms의 이전에 비해 **30%정도의 성능향상**이 있었습니다. 그 이유는 JPQL 쿼리를 사용하여 더 구체적인 쿼리를 작성 (accountId 매개변수를 전달)해 성능이 향상되었습니다. 하지만 지연로딩을 사용하기 때문에
  N+1 문제가 발생할 가능성이 있습니다.

---

3. `List<Friend> findAllByAccountIdWithFetchJoin(Long accountId);`
  ![Pasted image 20230426190353](https://user-images.githubusercontent.com/52237184/234725711-fc0129a4-1992-4dd9-add0-44b96c9b8a98.png)
  => 여러 번 테스트 한 결과 65~75ms의 처음에 비해 **60%정도의 성능향상**이 있었습니다. fetch join을 사용하면 관련된 엔티티를 한 번의 쿼리로 함께 가져올 수 있습니다. 이로 인해 추가적으로 SQL을 생길 필요가 없어져 성능 향상에 기여했습니다. 또한, 그 쿼리를 한번에 가져와 **N+1 문제를 해결**하고 성능을 개선할 수 있습니다.

---

4. 인덱스 적용

이제는 엔티티에 인덱싱을 적용해 성능 향상을 기대했습니다. 하지만 세가지 메소드 모두 인덱싱에 대해서 **성능의 별차이**가 없었습니다. 데이터 크기가 더 커지거나 구조가 더 복잡해지는 경우에 사용을 고려해보겠습니다.

---

결론적으로, 엔티티에는 인덱스를 적용하지 않고 메모리상의 이점을 주고, 리포지토리에서 제공하는 메서드 중 **fetch join**을 사용한 세 번째 메서드가 성능 측면과 문제해결 측면에서 가장 효율적인 방법으로 보입니다. 


