-- practice04
-- 서브쿼리(SUBQUERY) SQL 문제입니다.

-- 단 조회결과는 급여의 내림차순으로 정렬되어 나타나야 합니다. 

-- 문제1.
-- 현재 전체 사원의 평균 급여보다 많은 급여를 받는 사원은 몇 명이나 있습니까?  ✅
select count(distinct(emp_no))
  from salaries
 where to_date = '9999-01-01'
   and salary > (
					select avg(salary)
					  from salaries
					  where to_date = '9999-01-01'
				 );

-- 문제2. 
-- 현재, 각 부서별로 최고의 급여를 받는 사원의 사번, 이름, 부서, 급여을 조회하세요. 단 조회결과는 급여의 내림차순으로 정렬합니다. ✅
 select a.emp_no as '사번', concat(a.first_name, ' ', a.last_name) as '이름', d.dept_name as '부서명', b.salary as '급여'
   from employees a, salaries b, dept_emp c, departments d
  where a.emp_no = b.emp_no
    and a.emp_no = c.emp_no
    and c.dept_no = d.dept_no
	and (c.dept_no, b.salary) in (
								select a.dept_no, max(b.salary)
								  from dept_emp a, salaries b
								 where a.emp_no = b.emp_no
							  group by a.dept_no
                              )
order by b.salary desc;

-- 문제3.
-- 현재, 사원 자신들의 부서의 평균급여보다 급여가 많은 사원들의 사번, 이름 그리고 급여를 조회하세요 ✅
 select a.emp_no as '사번', concat(a.first_name, ' ', a.last_name) as '이름', b.salary as '급여'
   from employees a, salaries b, dept_emp c,   (  select a.dept_no, avg(b.salary) as avg_salary
												  from dept_emp a, salaries b
												 where a.emp_no = b.emp_no
												   and a.to_date = '9999-01-01'
												   and b.to_date = '9999-01-01'
											  group by a.dept_no) d
   where a.emp_no = b.emp_no
     and a.emp_no = c.emp_no
     and c.dept_no = d.dept_no
     and b.salary > d.avg_salary
order by b.salary desc;
  
 
        
-- 문제4.
-- 현재, 사원들의 사번, 이름, 그리고 매니저 이름과 부서 이름을 출력해 보세요. ✅
select a.emp_no as '사번', concat(a.first_name, ' ', a.last_name) as '이름', d.name as '매니저 이름', c.dept_name
  from employees a, dept_emp b, departments c,
											(select a.dept_no, concat(b.first_name, ' ', b.last_name) as name
											   from dept_manager a, employees b
											  where a.emp_no = b.emp_no
												and a.to_date = '9999-01-01') d
 where a.emp_no = b.emp_no
   and b.dept_no = c.dept_no
   and b.dept_no = d.dept_no
   and b.to_date = '9999-01-01';

-- 문제5.
-- 현재, 평균급여가 가장 높은 부서의 사원들의 사번, 이름, 직책 그리고 급여를 조회하고 급여 순으로 출력하세요. ✅
  select a.emp_no as '사번', concat(a.first_name, ' ', a.last_name) as '이름', d.title as '직책', b.salary as '급여'
    from employees a, salaries b, dept_emp c, titles d
   where a.emp_no = b.emp_no
	 and a.emp_no = c.emp_no
	 and a.emp_no = d.emp_no
	 and b.to_date = '9999-01-01'
	 and c.to_date = '9999-01-01'
	 and d.to_date = '9999-01-01'
	 and c.dept_no = (select a.dept_no
				   from dept_emp a, salaries b
			      where a.emp_no = b.emp_no
			        and a.to_date = '9999-01-01'
			        and b.to_date = '9999-01-01'
		       group by a.dept_no
               order by avg(b.salary) desc
                  limit 1)
order by b.salary desc;


-- 문제6.
-- 현재, 평균 급여가 가장 높은 부서의 이름 그리고 평균급여를 출력하세요. ✅
 select a.dept_name as '부서명', b.avg_salary as '최고 평균급여'
   from departments a, (
			select a.dept_no, avg(b.salary) as avg_salary
			  from dept_emp a, salaries b
			 where a.emp_no = b.emp_no
			   and a.to_date = '9999-01-01'
			   and b.to_date = '9999-01-01'
		  group by a.dept_no
          order by avg_salary desc
             limit 1
          ) b
where a.dept_no = b.dept_no;

-- 문제7.
-- 현재, 평균 급여가 가장 높은 직책의 타이틀 그리고 평균급여를 출력하세요. ✅
  select a.title as '직책', avg(b.salary) as '평균급여'
    from titles a, salaries b
   where a.emp_no = b.emp_no
     and a.to_date = '9999-01-01'
     and b.to_date = '9999-01-01'
group by a.title
order by avg(b.salary) desc
   limit 1;
 
