package com.hertz.library.service;

import com.hertz.library.dto.AuthorDTO;
import com.hertz.library.mapper.AuthorMapper;
import com.hertz.library.model.Author;
import com.hertz.library.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorMapper authorMapper;

    private final AuthorRepository authorRepository;

    public List<AuthorDTO> findAll() {
        return authorRepository.findAll().stream()
                .map(authorMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<AuthorDTO> findById(Long id) {
        return authorRepository.findById(id).map(authorMapper::toDto);
    }

    public AuthorDTO save(AuthorDTO authorDTO) {
        Author entity = authorMapper.toEntity(authorDTO);
        Author saved = authorRepository.save(entity);
        return authorMapper.toDto(saved);
    }

    public void deleteById(Long id) {
        authorRepository.deleteById(id);
    }
}
