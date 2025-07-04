package kr.gdu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.gdu.entity.MemberEntity;
import kr.gdu.repository.MemberRepository;

@Service
public class MemberService {
	@Autowired
	MemberRepository repository;

	public void memberInsert(MemberEntity memberEntity) {
		repository.save(memberEntity);
	}
	public MemberEntity getMember(String id) {
		return repository.findById(id).orElse(null);
	}
}
