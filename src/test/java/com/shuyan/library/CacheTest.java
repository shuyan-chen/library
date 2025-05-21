package com.shuyan.library;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.shuyan.library.config.HibernateConfig;
import com.shuyan.library.model.Book;
import com.shuyan.library.repository.BookRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ch.qos.logback.classic.Logger;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateConfig.class})
class CacheTest {

    private final SessionFactory sf;
    private final BookRepository repo;

    @Autowired
    public CacheTest(SessionFactory sf, BookRepository repo) {
        this.sf = sf;
        this.repo = repo;
    }

    static class SqlCounterAppender extends AppenderBase<ILoggingEvent> {
        private int selectCount = 0;
        @Override
        protected void append(ILoggingEvent evt) {
            if (evt.getFormattedMessage().trim().startsWith("select")) {
                selectCount++;
            }
        }
        int getSelectCount() { return selectCount; }
    }

    @Test
    @Transactional
    void firstLevelCacheHitInSameSession() {
        SqlCounterAppender counter = new SqlCounterAppender();
        counter.start();
        Logger sqlLogger = (Logger)LoggerFactory.getLogger("org.hibernate.SQL");
        sqlLogger.addAppender(counter);

        Integer id = 1;
        Book first = repo.findById(id).orElseThrow();
        Book second = repo.findById(id).orElseThrow();

        assertEquals(1, counter.getSelectCount(), "Within the same  Session SQL only excute once");
        assertSame(first, second, "return the same instance after hitting the second level cache");

        Session session = sf.getCurrentSession();
        assertTrue(session.contains(first));

        sqlLogger.detachAppender(counter);
        counter.stop();
    }

    @Test
    @Transactional
    void cacheEvictedOnUpdate() {
        Integer id = 2;

        Book b = repo.findById(id).orElseThrow();
        assertTrue(sf.getCache().containsEntity(Book.class, id));

        String newAuthor = "Cache Test Author";
        b.setAuthor(newAuthor);
        repo.save(b);
        sf.getCurrentSession().flush();

        sf.getCurrentSession().clear();
        assertFalse(sf.getCache().containsEntity(Book.class, id),
                "Cache should be expired after updating");
        Book after = repo.findById(id).orElseThrow();
        assertEquals(newAuthor, after.getAuthor());
        assertTrue(sf.getCache().containsEntity(Book.class, id));
    }

    @Test
    @Transactional
    void secondLevelCachePutAndHit() {
        Integer id = 1;

        Book first = repo.findById(id).orElseThrow();
        assertTrue(sf.getCache()
                        .containsEntity(Book.class, id),
                "Entity not cached on the second level");
        sf.getCurrentSession().clear();

        Book second = repo.findById(id).orElseThrow();
        assertNotSame(first, second);
        assertEquals(first.getTitle(), second.getTitle());
    }

}
