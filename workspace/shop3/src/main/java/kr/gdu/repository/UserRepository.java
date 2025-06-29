package kr.gdu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import kr.gdu.domain.User;

public interface UserRepository extends JpaRepository<User,String>{
/*
 * Spring Data JPA의 기본 제공 메서드
 *    Optional<User> findById
 *    save(User) : insert, update
 *    countBy..  : 레코드수 반환
 *    deleteById : id에 해당하는 레코드 제거 
 *    
 * 사용자 정의 메서드
 *   @Query("select u from usercipher u where u.userid = :userid") =>자동생성   
 *   findByUserid(String userid) : JPQL 자동 생성
 *   해당 객체가 없는 경우 null로 반환함
 */
	User findByUserid(String userid);

	@Transactional
	@Modifying
	@Query
("update Usercipher u set u.password= :chgpass where u.userid = :userid")
	void chgpass(@Param("userid") String userid, 
			     @Param("chgpass") String chgpass);

	@Query("select u from Usercipher u where u.phoneno = :phoneno")
	List<User> searchByUserid(@Param("email") String email,
			              @Param("phoneno")String phoneno);

	@Query("select u.password from Usercipher u "
+ " where u.userid=:userid and u.email = :email and u.phoneno = :phoneno")
	String searchByPassword(@Param("userid") String userid,
			@Param("email") String email,
			@Param("phoneno") String phoneno);

	List<User> findByUseridIn(List<String> list);
}
