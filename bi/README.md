References:
===========

1. Backup/Restore Best Practice (https://support.pentaho.com/hc/en-us/articles/205714836-Best-Practice-Backup-and-Recovery)



Queries:
========
1) Find all answered problems:
select distinct problem.id as problem_id from quizzical.gnt_ana_cat_attempt attempt
left join quizzical.gnt_ana_cat_rating_submit sub  on sub.id = attempt.ratingsubmission_id
left join quizzical.gnt_ana_cat_rating rat  on sub.rating_id = rat.id
left join quizzical.gnt_ana_testuser ana  on rat.analytics_id = ana.id
left join quizzical.gnt_user usr  on ana.userid = usr.id
left join quizzical.quizzical.gnt_quiz_prob qproblem  on attempt.testproblemid = qproblem.id
left join quizzical.gnt_quiz_problem_response qproblem_resp on qproblem.id = qproblem_resp.quizproblemid
left join quizzical.gnt_problem problem  on qproblem.problemid = problem.id
left join quizzical.gnt_problem_category problem_cat  on problem.category_id = problem_cat.id
left join quizzical.gnt_problem_difficulty problem_diff  on problem.difficulty_id = problem_diff.id
left join quizzical.gnt_problem_source problem_src  on problem.source_id = problem_src.id
WHERE usr.emailaddress = 'mandisakeswa999@gmail.com';


2) List problem not answered by category:
select pcat.id,pcat.name_,count(pcat.name_) from quizzical.gnt_problem prblm
left join quizzical.gnt_problem_category pcat on prblm.category_id = pcat.id
where prblm.id not in (
  select distinct problem.id as problem_id from quizzical.gnt_ana_cat_attempt attempt
  left join quizzical.gnt_ana_cat_rating_submit sub  on sub.id = attempt.ratingsubmission_id
  left join quizzical.gnt_ana_cat_rating rat  on sub.rating_id = rat.id
  left join quizzical.gnt_ana_testuser ana  on rat.analytics_id = ana.id
  left join quizzical.gnt_user usr  on ana.userid = usr.id
  left join quizzical.quizzical.gnt_quiz_prob qproblem  on attempt.testproblemid = qproblem.id
  left join quizzical.gnt_quiz_problem_response qproblem_resp on qproblem.id = qproblem_resp.quizproblemid
  left join quizzical.gnt_problem problem  on qproblem.problemid = problem.id
  left join quizzical.gnt_problem_category problem_cat  on problem.category_id = problem_cat.id
  left join quizzical.gnt_problem_difficulty problem_diff  on problem.difficulty_id = problem_diff.id
  left join quizzical.gnt_problem_source problem_src  on problem.source_id = problem_src.id
  WHERE usr.emailaddress = 'mandisakeswa999@gmail.com'  
  )
group by pcat.id


3) Not answered - order by id
select distinct problem.id as problem_id from quizzical.gnt_ana_cat_attempt attempt
left join quizzical.gnt_ana_cat_rating_submit sub  on sub.id = attempt.ratingsubmission_id
left join quizzical.gnt_ana_cat_rating rat  on sub.rating_id = rat.id
left join quizzical.gnt_ana_testuser ana  on rat.analytics_id = ana.id
left join quizzical.gnt_user usr  on ana.userid = usr.id
left join quizzical.quizzical.gnt_quiz_prob qproblem  on attempt.testproblemid = qproblem.id
left join quizzical.gnt_quiz_problem_response qproblem_resp on qproblem.id = qproblem_resp.quizproblemid
left join quizzical.gnt_problem problem  on qproblem.problemid = problem.id
left join quizzical.gnt_problem_category problem_cat  on problem.category_id = problem_cat.id
left join quizzical.gnt_problem_difficulty problem_diff  on problem.difficulty_id = problem_diff.id
left join quizzical.gnt_problem_source problem_src  on problem.source_id = problem_src.id
WHERE usr.emailaddress = 'mandisakeswa999@gmail.com'
order by problem.id
