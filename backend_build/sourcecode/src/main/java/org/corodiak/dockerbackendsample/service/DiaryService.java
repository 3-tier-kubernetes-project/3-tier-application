package org.corodiak.dockerbackendsample.service;

import lombok.RequiredArgsConstructor;
import org.corodiak.dockerbackendsample.repository.DiaryRepository;
import org.corodiak.dockerbackendsample.type.Diary;
import org.corodiak.dockerbackendsample.type.DiaryDto;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final BCryptPasswordEncoder encoder;

    public String findPassword(Long seq){
        Diary result = diaryRepository.findById(seq)
                .orElseThrow(EntityNotFoundException::new);
        return result.getPassword();
    }

    public void validate(String password,String encrypted){
        if(!encoder.matches(password,encrypted)){
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }
    }

    public DiaryDto.Response getDiary(Long seq,String password) {
        validate(password,findPassword(seq));
        Diary result = diaryRepository.findById(seq)
                .orElseThrow(EntityNotFoundException::new);
        return new DiaryDto.Response(result);
    }

    public List<DiaryDto.Response> getDiaryList() {
        List<Diary> results = diaryRepository.findAll(Sort.by(Sort.Direction.DESC, "createDate"));
        return results.stream().map(DiaryDto.Response::new).collect(Collectors.toList());
    }

    @Transactional
    public Long addDiary(DiaryDto.Request diaryDto) {
        Diary diary = Diary.builder()
                .title(diaryDto.getTitle())
                .writer(diaryDto.getWriter())
                .password(encoder.encode(diaryDto.getPassword()))
                .status(diaryDto.getStatus())
                .content(diaryDto.getContent())
                .createDate(LocalDateTime.now())
                .build();
        diaryRepository.save(diary);
        return diary.getSeq();
    }

    @Transactional
    public void deleteDiary(Long seq,String password) {
        validate(password,findPassword(seq));
        diaryRepository.deleteById(seq);
    }

    @Transactional
    public DiaryDto.Response updateDiary(Long seq,DiaryDto.Request diaryDto) {
        validate(diaryDto.getPassword(),findPassword(seq));
        Diary result = diaryRepository.findById(seq)
                .orElseThrow(EntityNotFoundException::new);
        result.updateDiary(diaryDto);
        return new DiaryDto.Response(result);
    }
}
