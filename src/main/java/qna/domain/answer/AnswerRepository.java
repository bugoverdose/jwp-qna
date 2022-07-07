package qna.domain.answer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findByQuestionIdAndDeletedFalse(Long questionId);

    Optional<Answer> findByIdAndDeletedFalse(Long id);
}