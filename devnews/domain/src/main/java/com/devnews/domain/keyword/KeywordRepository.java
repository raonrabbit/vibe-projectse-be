package com.devnews.domain.keyword;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
  List<Keyword> findByPeriodOrderByScoreDesc(Keyword.Period period);

  List<Keyword> findTop20ByPeriodOrderByScoreDesc(Keyword.Period period);
}
