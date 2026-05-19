package com.devnews.api.auth;

import com.devnews.domain.user.User;
import com.devnews.domain.user.User.AuthProvider;
import com.devnews.domain.user.UserRepository;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final UserRepository userRepository;

  @Override
  @Transactional
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);
    Map<String, Object> attributes = oAuth2User.getAttributes();

    String email = (String) attributes.get("email");
    String name = (String) attributes.get("name");
    String avatar = (String) attributes.get("picture");

    User user =
        userRepository
            .findByEmail(email)
            .orElseGet(
                () ->
                    userRepository.save(
                        User.builder()
                            .email(email)
                            .name(name)
                            .avatar(avatar)
                            .provider(AuthProvider.GOOGLE)
                            .build()));

    Map<String, Object> enriched = new HashMap<>(attributes);
    enriched.put("userId", user.getId());

    return new DefaultOAuth2User(
        Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), enriched, "email");
  }
}
