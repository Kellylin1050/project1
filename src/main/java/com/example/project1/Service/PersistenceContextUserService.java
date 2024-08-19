package com.example.project1.Service;

import com.example.project1.Entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PersistenceContextUserService {
    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    private EntityManager entityManagerTransactionType;

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManagerExtendedType;

    @Transactional
    void insertUserWithTransactionTypePersistenceContext(User user) {
        entityManagerTransactionType.persist(user);
    }

    User findWithTransactionTypePersistenceContext(long id) {
        return entityManagerTransactionType.find(User.class, id);
    }

    void insertUserWithExtendedTypePersistenceContext(User user) {
        entityManagerExtendedType.persist(user);
    }

    User findWithExtendedTypePersistenceContext(long id) {
        return entityManagerExtendedType.find(User.class, id);
    }
}
