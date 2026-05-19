package com.devnews.domain.subscription;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
  List<Subscription> findByUserId(Long userId);

  List<Subscription> findByTypeAndTargetId(Subscription.SubscriptionType type, String targetId);

  boolean existsByUserIdAndTypeAndTargetId(
      Long userId, Subscription.SubscriptionType type, String targetId);
}
