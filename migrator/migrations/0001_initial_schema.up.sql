CREATE SCHEMA IF NOT EXISTS gauntlet;

CREATE TABLE IF NOT EXISTS gauntlet.question_category (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS gauntlet.question_difficulty (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  color VARCHAR(6) NOT NULL
);

CREATE TABLE IF NOT EXISTS gauntlet.question (
  id BIGSERIAL PRIMARY KEY,
  answer VARCHAR(10) NOT NULL,
  picture VARCHAR(255) NOT NULL,
  date_created TIMESTAMP NOT NULL,
  multiple_choice BOOLEAN NOT NULL,

  category_id INTEGER REFERENCES gauntlet.question_category,
  difficulty_id INTEGER REFERENCES gauntlet.question_difficulty
);

CREATE TABLE IF NOT EXISTS gauntlet.quiz (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  date_created TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS gauntlet.quiz_question (
  id BIGSERIAL PRIMARY KEY,
  question_number INTEGER NOT NULL,

  quiz_id INTEGER REFERENCES gauntlet.quiz,
  question_id INTEGER REFERENCES gauntlet.question
);

CREATE TABLE IF NOT EXISTS gauntlet.quiz_take (
  id BIGSERIAL PRIMARY KEY,
  date_taken TIMESTAMP NOT NULL,

  quiz_id INTEGER REFERENCES gauntlet.quiz
);

CREATE TABLE IF NOT EXISTS gauntlet.quiz_take_answer (
  id BIGSERIAL PRIMARY KEY,
  answer VARCHAR(10) NOT NULL,
  correct BOOLEAN NOT NULL,
  times_skipped INTEGER NOT NULL,
  seconds_elapsed INTEGER NOT NULL,

  question_id INTEGER REFERENCES gauntlet.question,
  quiz_take_id INTEGER REFERENCES gauntlet.quiz_take
);
