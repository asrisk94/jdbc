-----------------------
-- 관리자(system)계정
-----------------------

-- student 계정 생성 및 권한 부여

create user student
identified by student
default tablespace users;

grant connect, resource to student;





------------------------
-- student 계정
------------------------

-- member 테이블 생성

create table member (
    member_id varchar2(15),
    password varchar2(15) not null,
    member_name varchar2(50) not null,
    gender char(1),
    age number,
    email varchar2(300),
    phone char(11) not null,
    address varchar2(300),
    hobby varchar2(100),
    enroll_date date default sysdate,
    constraint pk_member_id primary key(member_id),
    constraint ck_gender check(gender in ('M', 'F'))
);

-- sample data 추가

insert into member
values('honggd', '1234', '홍길동', 'M', 30, 'honggd@naver.com', '01012341234',
        '서울 강남구 테헤란로', '등산, 그림, 요리', default);
insert into member
values('gogd', '1234', '고길동', 'M', 35, 'gogd@naver.com', '01012341234',
        '서울 영등포구 대림동', '운동, 그림', default);
insert into member
values('sinsa', '1234', '신사임당', 'F', 28, 'sinsa@naver.com', '01012341234',
        '이천시 중리동', '춤', default);
insert into member
values('sejong', '1234', '세종대왕', 'M', 42, 'sejong@naver.com', '01012341234',
        '서울 서초구 방배동', '음악, 책', default);
insert into member
values('kein', '1234', '케인인', 'M', 35, 'kein@naver.com', '01012341234',
        '서울 은평구', '왕오브', default);
commit;

select * from member;
