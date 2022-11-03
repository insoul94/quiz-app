CREATE DATABASE IF NOT EXISTS quiz /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

DROP TABLE IF EXISTS quiz.response CASCADE;
DROP TABLE IF EXISTS quiz.question CASCADE;

CREATE TABLE quiz.question (
                          `question_id` int unsigned NOT NULL AUTO_INCREMENT,
                          `topic` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                          `difficulty_rank` int unsigned NOT NULL,
                          `content` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                          PRIMARY KEY (`question_id`),
                          UNIQUE KEY `id_UNIQUE` (`question_id`) /*!80000 INVISIBLE */,
                          KEY `topic_idx` (`topic`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE quiz.response (
                          `response_id` int NOT NULL AUTO_INCREMENT,
                          `fk_question_id` int unsigned NOT NULL,
                          `text` varchar(2048) COLLATE utf8mb4_unicode_ci NOT NULL,
                          `correct` tinyint(1) NOT NULL DEFAULT '0',
                          PRIMARY KEY (`response_id`),
                          KEY `fk_question_id_idx` (`fk_question_id`),
                          CONSTRAINT `fk_question_id` FOREIGN KEY (`fk_question_id`) REFERENCES `question` (`question_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

# INSERT INTO quiz.question (`topic`, `difficulty_rank`, `content`) VALUES ('History', '1', 'How many World Wars happened in the human history?');
# INSERT INTO quiz.question (`topic`, `difficulty_rank`, `content`) VALUES ('Programming', '3', 'What is the result of \'1 OR 0\' statement?');
# INSERT INTO quiz.question (`topic`, `difficulty_rank`, `content`) VALUES ('Geography', '3', 'Which countries are located in Scandinavia?');
# INSERT INTO quiz.question (`topic`, `difficulty_rank`, `content`) VALUES ('Geography', '1', 'What is the capital of France?');
# INSERT INTO quiz.question (`topic`, `difficulty_rank`, `content`) VALUES ('Physics', '3', 'What is the name of the particle which consists of protons, neutrons and electrons?');
# INSERT INTO quiz.question (`topic`, `difficulty_rank`, `content`) VALUES ('Mathematics', '4', 'What is 4 in the power of 3?');
#
# INSERT INTO quiz.response (`fk_question_id`, `text`, `correct`) VALUES ('1', 'One World War', '0');
# INSERT INTO quiz.response (`fk_question_id`, `text`, `correct`) VALUES ('1', 'Two World Wars', '1');
# INSERT INTO quiz.response (`fk_question_id`, `text`, `correct`) VALUES ('2', '0', '0');
# INSERT INTO quiz.response (`fk_question_id`, `text`, `correct`) VALUES ('2', '1', '1');
# INSERT INTO quiz.response (`fk_question_id`, `text`, `correct`) VALUES ('2', '2', '0');
# INSERT INTO quiz.response (`fk_question_id`, `text`, `correct`) VALUES ('2', '3', '0');
# INSERT INTO quiz.response (`fk_question_id`, `text`, `correct`) VALUES ('3', 'Finland', '1');
# INSERT INTO quiz.response (`fk_question_id`, `text`, `correct`) VALUES ('3', 'Norway', '1');
# INSERT INTO quiz.response (`fk_question_id`, `text`, `correct`) VALUES ('3', 'France', '0');
# INSERT INTO quiz.response (`fk_question_id`, `text`, `correct`) VALUES ('3', 'Italy', '0');
# INSERT INTO quiz.response (`fk_question_id`, `text`, `correct`) VALUES ('4', 'Berlin', '0');
# INSERT INTO quiz.response (`fk_question_id`, `text`, `correct`) VALUES ('4', 'Tallinn', '0');
# INSERT INTO quiz.response (`fk_question_id`, `text`, `correct`) VALUES ('4', 'Orlean', '0');
# INSERT INTO quiz.response (`fk_question_id`, `text`, `correct`) VALUES ('4', 'Paris', '1');
# INSERT INTO quiz.response (`fk_question_id`, `text`, `correct`) VALUES ('5', 'Atom', '1');
# INSERT INTO quiz.response (`fk_question_id`, `text`, `correct`) VALUES ('5', 'Photon', '0');
# INSERT INTO quiz.response (`fk_question_id`, `text`, `correct`) VALUES ('5', 'Quark', '0');
# INSERT INTO quiz.response (`fk_question_id`, `text`, `correct`) VALUES ('6', '444', '0');
# INSERT INTO quiz.response (`fk_question_id`, `text`, `correct`) VALUES ('6', '43', '0');
# INSERT INTO quiz.response (`fk_question_id`, `text`, `correct`) VALUES ('6', '64', '1');
