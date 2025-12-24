package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.expression.spel.ast.Projection;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.querydsl.core.types.ExpressionUtils.count;
import static org.example.expert.domain.comment.entity.QComment.comment;
import static org.example.expert.domain.manager.entity.QManager.manager;
import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class TodoCustomRepositoryImpl implements TodoCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(todo)
                .leftJoin(todo.user).fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne());
    }

    @Override
    public Page<TodoSearchResponse> searchTodos(Pageable pageable, String title, String managerNickname, LocalDateTime startDate, LocalDateTime endDate) {
        List<TodoSearchResponse> content =queryFactory
                .select(Projections.constructor(TodoSearchResponse.class,
                    todo.title,
                    manager.id.countDistinct(),
                    comment.id.countDistinct())
                )
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(todo.comments, comment)
                .where(
                        titleContains(title),
                        managerNicknameContains(managerNickname),
                        createdDateBetweenContains(startDate, endDate)
                )
                .groupBy(todo.id)
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(todo.id.countDistinct())
                .from(todo)
                .leftJoin(todo.managers, manager)
                .where(
                        titleContains(title),
                        managerNicknameContains(managerNickname),
                        createdDateBetweenContains(startDate, endDate)
                )
                .fetchOne();

        if(total == null) {
            total = 0L;
        }

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression titleContains(String title) {
        return title != null ? todo.title.contains(title) : null;
    }

    private BooleanExpression managerNicknameContains(String managerNickname) {
        return managerNickname != null ? manager.user.nickname.contains(managerNickname) : null;
    }

    private BooleanExpression createdDateBetweenContains(LocalDateTime startDate, LocalDateTime endDate) {
        if(startDate != null && endDate != null) {
            return todo.createdAt.between(startDate, endDate);
        }
        if(startDate != null) {
            return todo.createdAt.goe(startDate);
        }
        if(endDate != null) {
            return todo.createdAt.loe(endDate);
        }
        return null;
    }
}
