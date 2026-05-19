package com.devnews.domain.article;

import com.devnews.domain.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "articles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Article extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  private String titleKo;

  @Column(columnDefinition = "TEXT")
  private String summaryKo;

  @Column(columnDefinition = "TEXT")
  private String summaryOriginal;

  @Column(nullable = false)
  private String source;

  @Column(nullable = false, unique = true)
  private String url;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ArticleCategory category;

  private Instant publishedAt;

  @Column(nullable = false)
  private Instant collectedAt;
}
