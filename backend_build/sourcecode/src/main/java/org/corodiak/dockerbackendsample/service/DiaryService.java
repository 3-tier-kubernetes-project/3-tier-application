package org.corodiak.dockerbackendsample.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.corodiak.dockerbackendsample.repository.DiaryRepository;
import org.corodiak.dockerbackendsample.type.Diary;
import org.corodiak.dockerbackendsample.type.DiaryDto;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiaryService {

	private final DiaryRepository diaryRepository;

	public DiaryDto.Response getDiary(Long seq) {
		Optional<Diary> result = diaryRepository.findById(seq);
		if(result.isEmpty()) {
			return null;
		}
		return new DiaryDto.Response(result.get());
	}

	public List<DiaryDto.Response> getDiaryList() {
		List<Diary> results = diaryRepository.findAll(Sort.by(Sort.Direction.DESC, "createDate"));
		return results.stream().map(DiaryDto.Response::new).collect(Collectors.toList());
	}

	@Transactional
	public Long addDiary(DiaryDto.Request diaryDto) {
		Diary diary = Diary.builder()
			.title(diaryDto.getTitle())
			.content(diaryDto.getContent())
			.createDate(LocalDateTime.now())
			.build();
		diaryRepository.save(diary);
		return diary.getSeq();
	}

	@Transactional
	public void deleteDiary(Long seq){
		diaryRepository.deleteById(seq);
	}
}
