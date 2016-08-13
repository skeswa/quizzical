
CREATE TABLE IF NOT EXISTS gauntlet.quiz_type (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

ALTER TABLE gauntlet.quiz
ADD quiz_type_id INTEGER REFERENCES gauntlet.quiz_type;

ALTER TABLE gauntlet.question
ADD source VARCHAR(255),
ADD origin_book_page INTEGER,
ADD requires_calculator BOOLEAN NOT NULL DEFAULT FALSE;
