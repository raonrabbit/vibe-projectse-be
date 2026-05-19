package com.devnews.domain.release;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GitHubReleaseRepository extends JpaRepository<GitHubRelease, Long> {
  boolean existsByRepoNameAndVersion(String repoName, String version);

  Optional<GitHubRelease> findByRepoNameAndVersion(String repoName, String version);

  Page<GitHubRelease> findByRepoNameOrderByReleasedAtDesc(String repoName, Pageable pageable);
}
