ALTER TABLE gauntlet.question
RENAME picture TO question_picture;

ALTER TABLE gauntlet.question
ADD answer_picture VARCHAR(255) NOT NULL DEFAULT 'missing.jpg';
