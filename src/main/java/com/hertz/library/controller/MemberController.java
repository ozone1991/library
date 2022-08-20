package com.hertz.library.controller;

import com.hertz.library.dto.MemberDTO;
import com.hertz.library.exceptions.MemberDoesNotExistException;
import com.hertz.library.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<List<MemberDTO>> getAllMembers() {
        try {
            List<MemberDTO> members = memberService.findAll();
            return new ResponseEntity<>(members, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO> getMemberById(@PathVariable Long id) {
        try {
            Optional<MemberDTO> member = memberService.findById(id);
            return member.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<MemberDTO> createNewMember(@RequestBody MemberDTO memberDTO) {
        try {
            MemberDTO newMember = memberService.save(memberDTO);
            return new ResponseEntity<>(newMember, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<MemberDTO> updateMember(@RequestBody MemberDTO memberDTO) {
        Optional<MemberDTO> member = memberService.findById(memberDTO.getId());
        if (member.isPresent()) {
            MemberDTO updatedMember = memberService.save(memberDTO);
            return new ResponseEntity<>(updatedMember, HttpStatus.OK);
        } else {
            throw new MemberDoesNotExistException();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMember(@PathVariable Long id) {
        Optional<MemberDTO> member = memberService.findById(id);
        if (member.isPresent()) {
            memberService.deleteById(id);
            return new ResponseEntity<>("Successfully deleted member: " + id, HttpStatus.OK);
        } else {
            throw new MemberDoesNotExistException();
        }
    }

    @ExceptionHandler(value = MemberDoesNotExistException.class)
    public ResponseEntity<String> handleMemberDoesNotExistException() {
        return new ResponseEntity<>("Member does not exist", HttpStatus.NOT_FOUND);
    }

}
