package com.hertz.library.service;

import com.hertz.library.dto.MemberDTO;
import com.hertz.library.mapper.MemberMapper;
import com.hertz.library.model.Book;
import com.hertz.library.model.Member;
import com.hertz.library.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;

    private final MemberRepository memberRepository;

    public List<MemberDTO> findAll() {
        return memberRepository.findAll().stream()
                .map(memberMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<MemberDTO> findById(Long id) {
        return memberRepository.findById(id).map(memberMapper::toDto);
    }

    public MemberDTO save(MemberDTO memberDTO) {
        Member entity = memberMapper.toEntity(memberDTO);
        Member saved = memberRepository.save(entity);
        return memberMapper.toDto(saved);
    }

    public MemberDTO saveWithEntityPassed(Member member) {
        Member saved = memberRepository.save(member);
        return memberMapper.toDto(saved);
    }

    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }

    public List<Book> getBooksLoanedByUser(Long id) {
        return memberRepository.checkIfMemberHasOutstandingLoans(id);
    }
}
