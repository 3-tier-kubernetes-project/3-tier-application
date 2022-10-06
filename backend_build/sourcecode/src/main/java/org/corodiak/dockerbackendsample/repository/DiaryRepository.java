package org.corodiak.dockerbackendsample.repository;

import org.corodiak.dockerbackendsample.type.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
}
