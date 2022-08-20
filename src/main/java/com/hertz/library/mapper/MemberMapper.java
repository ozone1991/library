package com.hertz.library.mapper;

import com.hertz.library.dto.MemberDTO;
import com.hertz.library.model.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    MemberDTO toDto(Member member);

    Member toEntity(MemberDTO memberDTO);
}
