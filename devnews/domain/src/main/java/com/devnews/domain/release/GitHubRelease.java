package com.devnews.domain.release;

import com.devnews.domain.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "github_releases")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GitHubRelease extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String repoName;

  @Column(nullable = false)
  private String repoUrl;

  @Column(nullable = false)
  private String version;

  @Column(columnDefinition = "TEXT")
  private String releaseNote;

  @Column(columnDefinition = "TEXT")
  private String summaryKo;

  @Column(nullable = false)
  private Instant releasedAt;
}
