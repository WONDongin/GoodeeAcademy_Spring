package kr.gdu.domain;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import kr.gdu.dto.BoardDto;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name="board")
@Data
@NoArgsConstructor
public class Board {
	@Id
	private int num;
	private String boardid;
	private String writer;
	private String pass;
	private String title;
	private String content;
	private String file1; //파일 이름
	@Temporal(TemporalType.TIMESTAMP) //날짜+시간
	private Date regdate;
	private int readcnt;
	private int grp;
	private int grplevel;
	private int grpstep;
	public Board(BoardDto dto) {
		this.num = dto.getNum();
		this.boardid = dto.getBoardid();
		this.writer = dto.getWriter();
		this.pass = dto.getPass();
		this.title = dto.getTitle();
		this.content = dto.getContent();
		this.file1 = dto.getFileurl();
		this.regdate = dto.getRegdate();
		this.readcnt = dto.getReadcnt();
		this.grp = dto.getGrp();
		this.grplevel = dto.getGrplevel();
		this.grpstep = dto.getGrpstep();
	}
	@PrePersist
	public void onPrePersist() { //save() 함수 호출직전에 먼저호출
		this.regdate = new Date();
	}
	
}
