package qna.domain.answer;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import qna.domain.EntityHistory;
import qna.domain.deletehistory.ContentType;
import qna.domain.deletehistory.DeleteHistory;
import qna.domain.question.Question;
import qna.domain.user.User;
import qna.exception.AlreadyDeletedException;
import qna.exception.CannotDeleteException;
import qna.exception.NotFoundException;
import qna.exception.UnAuthorizedException;

@Table(name = "answer")
@Entity
@SQLDelete(sql = "UPDATE answer SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Answer extends EntityHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question; // FK 관리되는 필드

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private User writer;

    protected Answer() {
    }

    public Answer(User writer, Question question, String contents) {
        this(null, writer, question, contents);
    }

    public Answer(Long id, User writer, Question question, String contents) {
        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }
        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }
        this.id = id;
        this.writer = writer;
        this.question = question;
        this.contents = contents;
    }

    public Long getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public DeleteHistory deleteBy(User user) {
        validateDeletableBy(user);
        this.deleted = true;
        return DeleteHistory.ofAnswer(id, writer);
    }

    private void validateDeletableBy(User user) {
        if (!writer.equals(user)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변은 삭제할 수 없습니다.");
        }
        if (deleted) {
            throw new AlreadyDeletedException("이미 삭제된 답입니다.");
        }
    }

    public DeleteHistory toDeleteHistory() {
        return new DeleteHistory(ContentType.ANSWER, id, writer);
    }

    public User getWriter() {
        return writer;
    }

    public Long getWriterId() {
        return writer.getId();
    }

    public Question getQuestion() {
        return question;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                ", questionId=" + question.getId() +
                ", writerId=" + writer.getId() +
                '}';
    }
}
