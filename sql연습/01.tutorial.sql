-- 표준처럼 FROM 절 작성하기
select version(), current_date, current_date(), now() from dual; 

-- 수학 함수, 사칙연수도 된다
select sin(pi()/4), 1+2 * 3 -4 /5 from dual;

-- 대소문자 구분이 없다.
select VERSION(), current_DATE, NOW() From DUal;

-- table 생성, varchar는 가변(남는 공간은 조절된다), char는 불변
create table pet (
	name varchar(100),
    owner varchar(20),
    species varchar(20),
    gender char(1),
    birth date,
    death date
);

-- schema 확인
describe pet;
desc pet;

-- table 삭제
drop table pet;

-- insert (C = Create DML)
insert 
	into pet 
values ('시루', '홍길동', 'dog', 'w', '2020-01-01', null);

-- select (R = Read DML)
select * from pet;

-- update (U = Update DML), where 조건문을 함께 작성하기
update pet set name = '백설' where name = '시루';
-- update pet set death = null where death = '0000-00-00';

-- delete (D = Delete DML), where 조건문을 함께 작성하기
delete from pet where name = '백설';

-- load data : mysql(CLI) Local 전용(원격에서는 못한다)
load data local infile '/root/pet.txt' into table pet;

-- select 연습
select name, species, birth
    from pet
where name = 'bowser';

select name, species, birth
    from pet
where birth >= '1998-01-01';

select name, species, birth
    from pet
where species = 'dog'
	and gender = 'f';
    
select name, species, birth
    from pet
where species = 'snake'
	or species = 'bird';
    
select name, birth, death
    from pet
where death is not null;

select name
    from pet
where name like 'b%';

select name
    from pet
where name like '%fy';

select name
    from pet
where name like '%w%';

-- 글자수가 4개인 경우
select name
    from pet
where name like '____';

select name
    from pet
where name like 'b____';

select count(*), max(birth)
 from pet;
 
