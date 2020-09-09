package com.service;

import com.entity.TUserProfileEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class DatabaseServiceORMImpTest {

	//Подключаю тестовую БД, прописанную в persistence.xml. Каждый раз при запуске таблицы пересоздаются.
	private EntityManager em = Persistence.createEntityManagerFactory("TestModel").createEntityManager();
	private DatabaseServiceORMImp serviceORM = new DatabaseServiceORMImp(em);

	@Test
	public void getUsersWithFilter() throws JsonProcessingException {
		//Чищу таблицу t_user_profile перед тестами
		truncateTable(TUserProfileEntity.class);
		assertEquals(serviceORM.getUsersWithFilter(null, null).size(), 0);
		persistEntity(new TUserProfileEntity());
		persistEntity(new TUserProfileEntity());
		persistEntity(new TUserProfileEntity());
		assertEquals(serviceORM.getUsersWithFilter(null, null).size(), 3);
	}

	@Test
	public void getUserProfileForLogin() throws JsonProcessingException {
		//Чищу таблицу t_user_profile перед тестами
		truncateTable(TUserProfileEntity.class);
		assertNull(serviceORM.getUserProfileForLogin("kkk"));
		TUserProfileEntity newUser = new TUserProfileEntity();
		newUser.setLogin("lll");
		persistEntity(newUser);
		assertNull(serviceORM.getUserProfileForLogin("kkk"));
		assertNotNull(serviceORM.getUserProfileForLogin("lll"));
	}

	//todo найти способ избавиться от повторения с транзакциями
	//Чищу за собой
	private <T> void truncateTable(Class<T> entityType) {
		em.getTransaction().begin();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaDelete<T> query = builder.createCriteriaDelete(entityType);
		query.from(entityType);
		em.createQuery(query).executeUpdate();
		em.getTransaction().commit();
	}

	private void persistEntity(Object entity) {
		em.getTransaction().begin();
		em.persist(entity);
		em.getTransaction().commit();
	}
}
