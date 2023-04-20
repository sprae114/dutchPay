package com.example.dutchpay.dto;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class KakaoOAuth2Response {
    private Long id;
    private LocalDateTime connectedAt;
    private Map<String, Object> properties;
    private KakaoAccount kakaoAccount;

    public KakaoOAuth2Response(Long id, LocalDateTime connectedAt, Map<String, Object> properties, KakaoAccount kakaoAccount) {
        this.id = id;
        this.connectedAt = connectedAt;
        this.properties = properties;
        this.kakaoAccount = kakaoAccount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getConnectedAt() {
        return connectedAt;
    }

    public void setConnectedAt(LocalDateTime connectedAt) {
        this.connectedAt = connectedAt;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public KakaoAccount getKakaoAccount() {
        return kakaoAccount;
    }

    public void setKakaoAccount(KakaoAccount kakaoAccount) {
        this.kakaoAccount = kakaoAccount;
    }

    public String getEmail() {
        return this.kakaoAccount.getEmail();
    }

    public String getNickname() {
        return this.kakaoAccount.getNickname();
    }

    public static class KakaoAccount {
        private Boolean profileNicknameNeedsAgreement;
        private Profile profile;
        private Boolean hasEmail;
        private Boolean emailNeedsAgreement;
        private Boolean isEmailValid;
        private Boolean isEmailVerified;
        private String email;

        public KakaoAccount(Boolean profileNicknameNeedsAgreement, Profile profile, Boolean hasEmail, Boolean emailNeedsAgreement, Boolean isEmailValid, Boolean isEmailVerified, String email) {
            this.profileNicknameNeedsAgreement = profileNicknameNeedsAgreement;
            this.profile = profile;
            this.hasEmail = hasEmail;
            this.emailNeedsAgreement = emailNeedsAgreement;
            this.isEmailValid = isEmailValid;
            this.isEmailVerified = isEmailVerified;
            this.email = email;
        }

        public Boolean getProfileNicknameNeedsAgreement() {
            return profileNicknameNeedsAgreement;
        }

        public void setProfileNicknameNeedsAgreement(Boolean profileNicknameNeedsAgreement) {
            this.profileNicknameNeedsAgreement = profileNicknameNeedsAgreement;
        }

        public Profile getProfile() {
            return profile;
        }

        public void setProfile(Profile profile) {
            this.profile = profile;
        }

        public Boolean getHasEmail() {
            return hasEmail;
        }

        public void setHasEmail(Boolean hasEmail) {
            this.hasEmail = hasEmail;
        }

        public Boolean getEmailNeedsAgreement() {
            return emailNeedsAgreement;
        }

        public void setEmailNeedsAgreement(Boolean emailNeedsAgreement) {
            this.emailNeedsAgreement = emailNeedsAgreement;
        }

        public Boolean getIsEmailValid() {
            return isEmailValid;
        }

        public void setIsEmailValid(Boolean isEmailValid) {
            this.isEmailValid = isEmailValid;
        }

        public Boolean getIsEmailVerified() {
            return isEmailVerified;
        }

        public void setIsEmailVerified(Boolean isEmailVerified) {
            this.isEmailVerified = isEmailVerified;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getNickname() {
            return this.profile.getNickname();
        }

        public static class Profile {
            private String nickname;

            public Profile(String nickname) {
                this.nickname = nickname;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public static Profile from(Map<String, Object> attributes) {
                return new Profile(String.valueOf(attributes.get("nickname")));
            }
        }

        public static KakaoAccount from(Map<String, Object> attributes) {
            return new KakaoAccount(
                    Boolean.valueOf(String.valueOf(attributes.get("profile_nickname_needs_agreement"))),
                    Profile.from((Map<String, Object>) attributes.get("profile")),
                    Boolean.valueOf(String.valueOf(attributes.get("has_email"))),
                    Boolean.valueOf(String.valueOf(attributes.get("email_needs_agreement"))),
                    Boolean.valueOf(String.valueOf(attributes.get("is_email_valid"))),
                    Boolean.valueOf(String.valueOf(attributes.get("is_email_verified"))),
                    String.valueOf(attributes.get("email"))
            );
        }
    }

    public static KakaoOAuth2Response from(Map<String, Object> attributes) {
        return new KakaoOAuth2Response(
                Long.valueOf(String.valueOf(attributes.get("id"))),
                LocalDateTime.parse(
                        String.valueOf(attributes.get("connected_at")),
                        DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault())
                ),
                (Map<String, Object>) attributes.get("properties"),
                KakaoAccount.from((Map<String, Object>) attributes.get("kakao_account"))
        );
    }
}

