package kr.gdu.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import kr.gdu.dto.BoardDto;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@NoArgsConstructor
public class BoardEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int num;
	private String name;
	private String pass;
	private String subject;
	private String content;
	private String file1;
	@Temporal(TemporalType.TIMESTAMP)
	private Date regdate;
	private int readcnt;
	private String boardid;
	@PrePersist
	public void onPrePersist() {
		this.regdate = new Date();
	}
	public BoardEntity(BoardDto dto) {
		this.num = dto.getNum();
		this.name = dto.getName();
		this.pass = dto.getPass();
		this.subject = dto.getSubject();
		this.content = dto.getContent();
		this.file1 = dto.getFile1();
		this.regdate = dto.getRegdate();
		this.readcnt = dto.getReadcnt();
		this.boardid = dto.getBoardid();
	}

}
