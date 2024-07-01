package com.sparta.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity // JPA가 관리할 수 있는 Entity 클래스임을 지정
@Table(name = "memo") // 이 클래스가 매핑될 테이블의 이름을 "memo"로 지정
public class Memo {
    @Id // 해당 필드가 테이블의 기본 키임을 지정
    private Long id;

    // nullable: null 허용 여부를 지정 (false일 때 null 값 허용하지 않음)
    // unique: 중복 허용 여부를 지정 (true일 때 중복 값 허용하지 않음)
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    // length: 컬럼의 최대 길이를 지정
    @Column(name = "contents", nullable = false, length = 500)
    private String contents;

    // 기본 생성자
    public Memo() {}

    // id 필드의 getter 메소드
    public Long getId() {
        return id;
    }

    // id 필드의 setter 메소드
    public void setId(Long id) {
        this.id = id;
    }

    // username 필드의 getter 메소드
    public String getUsername() {
        return username;
    }

    // username 필드의 setter 메소드
    public void setUsername(String username) {
        this.username = username;
    }

    // contents 필드의 getter 메소드
    public String getContents() {
        return contents;
    }

    // contents 필드의 setter 메소드
    public void setContents(String contents) {
        this.contents = contents;
    }
}
