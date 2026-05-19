package com.devnews.domain.notification;

import com.devnews.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification_settings")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NotificationSetting {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private User user;

  @Builder.Default private boolean keywordAlert = true;

  @Builder.Default private boolean releaseAlert = true;

  @Builder.Default private boolean trendingAlert = true;

  public void update(boolean keywordAlert, boolean releaseAlert, boolean trendingAlert) {
    this.keywordAlert = keywordAlert;
    this.releaseAlert = releaseAlert;
    this.trendingAlert = trendingAlert;
  }
}
