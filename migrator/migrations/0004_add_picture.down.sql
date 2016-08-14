ALTER TABLE gauntlet.question
RENAME question_picture TO picture;

ALTER TABLE gauntlet.question
DROP COLUMN IF EXISTS answer_picture;
