select u.emailaddress,r.rating,r.name_ from quizzical.gnt_ana_cat_rating r
left join quizzical.gnt_ana_testuser a on a.id = r.analytics_id
left join quizzical.gnt_user u on a.userid = u.id
where u.emailaddress = 'mandisakeswa999@gmail.com';

select u.emailaddress,r.rating,r.name_ from quizzical.gnt_ana_cat_rating r
left join quizzical.gnt_ana_testuser a on a.id = r.analytics_id
left join quizzical.gnt_user u on a.userid = u.id
where u.emailaddress = 'mduduzi.keswa@gmail.com';