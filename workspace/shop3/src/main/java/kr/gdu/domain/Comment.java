package kr.gdu.domain;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
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
	private Date regdate;
}
