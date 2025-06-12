package logic;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

// int 타입은 @NotEmpty가 안되므로, Integer로 설정 + @NotNull
@Data
public class Item {
	private int id;
	// @NotEmpty : null 또는 공백인 경우, 오류인식
	@NotEmpty(message="상품명을 입력하세요")
	private String name;
	@Min(value=10, message="10원 이상만 가능합니다")
	@Max(value=100000, message="10만원 이하만 가능합니다")
	private int price;
	@NotEmpty(message="상품설명을 입력하세요")
	private String description;
	private String pictureUrl;
	private MultipartFile picture; // 업로드된 파일 저장
}
