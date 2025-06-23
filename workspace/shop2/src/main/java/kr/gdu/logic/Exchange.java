package kr.gdu.logic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Exchange {
	private int eno;
	private String code;
	private String name;
	private float sellamt;
	private float buyamt;
	private float priamt;
	private String deate;
}
