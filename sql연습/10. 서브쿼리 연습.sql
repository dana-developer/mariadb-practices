--
-- Subquery
--

-- 1) select 절
select 1+1 from dual;
-- insert into t1 values(null, (select max(no) + 1 from t1);

--
--
-- 2) from 절의 서브 쿼리
select now() as n, sysdate() as s, 3+1 as r from dual;
select a.n, a.r
  from (select now() as n, sysdate() as s, 3+1 as r from dual) a;
  
-- 
-- 3) where 절의 서브 쿼리
--

-- 예제. 현재 Fai Bale이 근무하는 부서에서 근무하는 직원의 사번, 전체 이름을 출력해보세요.
select b.dept_no
  from employees a, dept_emp b
 where a.emp_no = b.emp_no
   and b.to_date = '9999-01-01'
   and concat(a.first_name, ' ', a.last_name) = 'Fai Bale';
   
-- d004
select a.emp_no, a.first_name
  from employees a, dept_emp b
 where a.emp_no = b.emp_no
  and b.to_date = '9999-01-01'
  and b.dept_no = (
	select b.dept_no
	  from employees a, dept_emp b
	 where a.emp_no = b.emp_no
	   and b.to_date = '9999-01-01'
	   and concat(a.first_name, ' ', a.last_name) = 'Fai Bale'
);
  
-- 3-1) 단일행 연산자 : =, >, <, >=, <=, <>, !=
-- 실습문제 1
-- 현재 전체 사원의 평균 연봉보다 적은 급여를 받는 사원의 이름과 급여를 출력하세요.
select a.first_name, b.salary
  from employees a, salaries b
 where a.emp_no = b.emp_no
   and b.to_date = '9999-01-01'
   and b.salary < (
	   select avg(salary)
         from salaries
        where to_date = '9999-01-01'
	);

-- 실습문제 2
-- 현재 직책별 평균급여 중에 가장 적은 평균급여의 직책 이름과 해당 평균급여를 출력하세요.
select c.title, min(avg_salary)
  from ( 
			 select a.title, avg(b.salary) as avg_salary
			   from titles a, salaries b
			  where a.emp_no = b.emp_no
				and a.to_date = '9999-01-01'
				and b.to_date = '9999-01-01'
		   group by a.title) c;
           
-- sol1 : where절 subqury(=), having 사용
	 select a.title, avg(b.salary)
       from titles a, salaries b
      where a.emp_no = b.emp_no
        and a.to_date = '9999-01-01'
        and b.to_date = '9999-01-01'
   group by a.title
     having avg(b.salary) = (
					select min(avg_salary)
					  from ( 
						 select a.title, avg(b.salary) as avg_salary
						   from titles a, salaries b
						  where a.emp_no = b.emp_no
							and a.to_date = '9999-01-01'
							and b.to_date = '9999-01-01'
					   group by a.title) c);
                       
-- sol2 : top-k
  select b.title, avg(a.salary)
    from salaries a, titles b
   where a.emp_no = b.emp_no
	 and a.to_date = '9999-01-01'
     and b.to_date = '9999-01-01'
group by b.title
order by avg(a.salary) asc
   limit 1;
 
-- 3-1) 복수행 연산자 : in, not in, >=, 비교연산자 any, 비교연산자 All

-- any 사용법 
-- 1. =any : in
-- 2. >any, >=any : 최소값
-- 3. <any, <=any : 최대값
-- 4. <>any, !=any : not in

-- all 사용법
-- 1. =all : (x) (하나의 행과 복수행이 동일할 수 없으므로)
-- 2. >all, >=all : 최대값
-- 3. <all, <=all : 최소값
-- 4. <>all, !=all

-- 실습문제3
-- 현재 급여가 50000 이상인 직원의 이름과 급여를 출력하세요.

-- sol01
select a.first_name, b.salary
 from employees a, salaries b
 where a.emp_no = b.emp_no
  and b.to_date = '9999-01-01'
  and b.salary > 50000; 
  
-- sol02
select a.first_name, b.salary
 from employees a, salaries b
 where a.emp_no = b.emp_no
  and b.to_date = '9999-01-01'
  and (a.emp_no, b.salary) = any (
		   select emp_no, salary
			 from salaries
			where to_date = '9999-01-01'
              and salary > 50000
  ); 
  
-- 실습 문제 4
-- 현재 각 부서별 최고급여를 받고 있는 직원의 이름과 해당 부서이름, 급여를 출력하세요.

-- sol1 : where in
select a.first_name, b.dept_name, d.salary as max_salary
 from employees a, departments b, dept_emp c, salaries d
 where a.emp_no = d.emp_no
  and b.dept_no = c.dept_no
  and c.emp_no = a.emp_no
  and c.to_date = '9999-01-01'
  and d.to_date = '9999-01-01'
  and (b.dept_no, d.salary) in (
	  select a.dept_no, max(b.salary)
		from dept_emp a, salaries b
		where a.emp_no = b.emp_no
		 and a.to_date = '9999-01-01'
		 and b.to_date = '9999-01-01'
		group by a.dept_no
 );

-- sol2 : from절 subquery & join
select a.first_name, b.dept_name, d.salary 
 from employees a, departments b, dept_emp c, salaries d,
	( select a.dept_no, max(b.salary) as max_salary
		from dept_emp a, salaries b
		where a.emp_no = b.emp_no
		 and a.to_date = '9999-01-01'
		 and b.to_date = '9999-01-01'
		group by a.dept_no) e
 where a.emp_no = d.emp_no
  and b.dept_no = c.dept_no
  and c.emp_no = a.emp_no
  and c.to_date = '9999-01-01'
  and d.to_date = '9999-01-01'
  and d.salary = e.max_salary;
