package qna.domain.deletehistory;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import qna.domain.user.User;

@Table(name = "delete_history", uniqueConstraints = {@UniqueConstraint(columnNames = {"contentId", "contentType"})})
@Entity
public class DeleteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long contentId;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    private final LocalDateTime createDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "deleted_by_id")
    private User deleter;

    protected DeleteHistory() {
    }

    public DeleteHistory(ContentType contentType, Long contentId, User deleter) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deleter = deleter;
    }

    public Long getId() {
        return id;
    }

    public Long getContentId() {
        return contentId;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public static DeleteHistory ofQuestion(Long contentId, User deleter) {
        return new DeleteHistory(ContentType.QUESTION, contentId, deleter);
    }

    public static DeleteHistory ofAnswer(Long contentId, User deleter) {
        return new DeleteHistory(ContentType.ANSWER, contentId, deleter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id)
                && Objects.equals(contentId, that.contentId)
                && contentType == that.contentType
                && Objects.equals(deleter, that.deleter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentId, contentType, createDate, deleter);
    }
}
