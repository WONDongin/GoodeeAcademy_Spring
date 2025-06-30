package kr.gdu.domain;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotEmpty;
import kr.gdu.dto.CommentDto;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="comment")
@IdClass(CommentId.class)
@Data
@NoArgsConstructor
public class Comment {
	@Id
	private int num;
	@Id
	private int seq;
	private String writer;
	private String pass;
	private String content;
	@Temporal(TemporalType.TIMESTAMP)
	private Date regdate; //현재시간으로 등록
	public Comment(CommentDto dto) {
		this.num = dto.getNum();
		this.seq = dto.getSeq();
		this.pass = dto.getPass();
		this.writer = dto.getWriter();
		this.content = dto.getContent();
		this.regdate = dto.getRegdate();
	}
	
	@PrePersist
	public void onPrePersist() {
		this.regdate = new Date();
	}
}
