package com.kh.springJpa241217.dto;

import com.kh.springJpa241217.constant.Authority;
import com.kh.springJpa241217.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

// DTO : 다른 레이어간의 데이터를 교환 할 때 사용,
// 주로 Frontend와  Backend사이에 데이터를 주고 받는 용도
// 회원 가입용
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberReqDto {
    private String email;
    private String pwd;
    private String name;
    private String imgPath;
    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                .pwd(passwordEncoder.encode(pwd))
                .name(name)
                .img(imgPath)
                .authority(Authority.ROLE_USER)
                .build();
    }
}
