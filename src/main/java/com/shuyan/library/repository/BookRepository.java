package com.shuyan.library.repository;

import com.shuyan.library.model.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaRoot;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class BookRepository {

    private final SessionFactory sessionFactory;

    private Session session() {
        return sessionFactory.getCurrentSession();
    }

    public BookRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Optional<Book> findById(int id) {
        return Optional.ofNullable(session().get(Book.class, id));
    }

    public Optional<Book> findByTitle(String title) {
        HibernateCriteriaBuilder criteriaBuilder = session().getCriteriaBuilder();
        JpaCriteriaQuery<Book> query = criteriaBuilder.createQuery(Book.class);
        JpaRoot<Book> root = query.from(Book.class);
        query.select(root).where(criteriaBuilder.equal(root.get("title"), title));

        return session().createQuery(query)
                .setCacheable(true)
                .uniqueResultOptional();
    }

    public List<Book> findAll() {
         String hql = "from Book";
         return   session()
                 .createQuery(hql, Book.class)
                 .setCacheable(true)
                 .list();
    }

    @Transactional
    public Book save(Book book) {
        return (Book) session().merge(book);
    }

    @Transactional
    public boolean deleteById(int id) {
        Book b = session().get(Book.class, id);
        if(b == null) return false;
        session().remove(b);
        return true;
    }


}

