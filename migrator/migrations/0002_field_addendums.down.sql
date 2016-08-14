ALTER TABLE gauntlet.question
DROP COLUMN IF EXISTS source,
DROP COLUMN IF EXISTS origin_book_page,
DROP COLUMN IF EXISTS requires_calculator;

ALTER TABLE gauntlet.quiz
DROP COLUMN IF EXISTS quiz_type_id;

DROP TABLE IF EXISTS gauntlet.quiz_type;
