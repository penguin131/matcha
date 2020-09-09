package com.service;

import com.dto.UserFilterDto;
import com.entity.TUserProfileEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.helper.CloneHelper;
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
		UserFilterDto filter = new UserFilterDto();
		assertEquals(serviceORM.getUsersWithFilter(null, null).size(), 0);
		TUserProfileEntity user = new TUserProfileEntity();
		user.setLogin("123");
		user.setAge(0);
		user.setRating(-1);
		persistEntity(CloneHelper.simpleClone(user));
		persistEntity(CloneHelper.simpleClone(user));
		persistEntity(CloneHelper.simpleClone(user));
		assertEquals(serviceORM.getUsersWithFilter(null, null).size(), 3);
		assertEquals(serviceORM.getUsersWithFilter(filter, "").size(), 3);
		assertEquals(serviceORM.getUsersWithFilter(filter, "smight").size(), 3);
		TUserProfileEntity user2 = new TUserProfileEntity();
		user2.setLogin("smight");
		user2.setAge(2);
		user2.setRating(2);
		persistEntity(user2);
		//Не должен найтись smight, так как есть фильтр
		assertEquals(serviceORM.getUsersWithFilter(filter, "smight").size(), 3);
		assertEquals(serviceORM.getUsersWithFilter(filter, "lalal").size(), 4);
		//максимальный и минимальный возраст
		filter.setAgeMax(0);
		assertEquals(serviceORM.getUsersWithFilter(filter, null).size(), 3);
		filter.setAgeMax(2);
		assertEquals(serviceORM.getUsersWithFilter(filter, null).size(), 4);
		filter.setAgeMin(2);
		assertEquals(serviceORM.getUsersWithFilter(filter, null).size(), 1);
		filter.setAgeMin(0);
		assertEquals(serviceORM.getUsersWithFilter(filter, null).size(), 4);
		//минимальный ретинг. Верхнего фильтра нет
		filter.setRating(0);
		assertEquals(serviceORM.getUsersWithFilter(filter, null).size(), 1);
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
