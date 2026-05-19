package com.devnews.domain.article;

import java.time.Instant;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
  boolean existsByUrl(String url);

  Optional<Article> findByUrl(String url);

  Page<Article> findByCategoryOrderByPublishedAtDesc(ArticleCategory category, Pageable pageable);

  Page<Article> findByPublishedAtAfterOrderByPublishedAtDesc(Instant since, Pageable pageable);
}
