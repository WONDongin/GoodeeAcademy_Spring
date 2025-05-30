package ex01_lombok;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
/*@Data는 위에서 설명한 
@Getter, @Setter, @RequiredArgsConstructor, 
@ToString, @EqualsAndHashCode를 한번에 설정해주는 어노테이션
*/
public class Ex01_User {
	private String id;
	private String pw;
}
