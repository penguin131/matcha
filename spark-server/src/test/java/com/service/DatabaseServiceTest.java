package com.service;

import com.TestConfig;
import com.dto.UserFilterDto;
import com.entity.TUserProfileEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.helper.CloneHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class DatabaseServiceTest {

	//Подключаю тестовую БД, прописанную в persistence.xml
	private static EntityManager em = Persistence.createEntityManagerFactory("TestModel").createEntityManager();
	private DatabaseService service;

	public DatabaseServiceTest(DatabaseService service) {
		super();
		this.service = service;
	}

	//Тестирую обе имплементации
	@Parameterized.Parameters
	public static List<DatabaseService> input() throws SQLException {
		return Arrays.asList(
				new DatabaseServiceSQLImpl(DriverManager.getConnection(TestConfig.getUrl(), TestConfig.getTestConfig())),
				new DatabaseServiceORMImp(em));
	}

	@Test
	public void getUsersWithFilter() throws JsonProcessingException, SQLException {
		//Чищу таблицу t_user_profile перед тестами. Инициализация обьектов.
		truncateTable(TUserProfileEntity.class);
		assertEquals(service.getUsersWithFilter(null, null).size(), 0);
		UserFilterDto filter = new UserFilterDto();
		TUserProfileEntity user = new TUserProfileEntity();
		TUserProfileEntity user2 = new TUserProfileEntity();
		user.setLogin("123");
		user.setPassword("");
		user.setAge(0);
		user.setRating(-1);
		user.setSex(1);
		user.setSexPreferences(1);
		user.setLocation1(new BigDecimal("55.752220000000000"));
		user.setLocation2(new BigDecimal("37.615560000000000"));
		persistEntity(CloneHelper.simpleClone(user));
		persistEntity(CloneHelper.simpleClone(user));
		persistEntity(CloneHelper.simpleClone(user));
		user2.setLogin("smight");
		user2.setPassword("");
		user2.setAge(2);
		user2.setRating(2);
		user2.setSex(0);
		user2.setLocation1(new BigDecimal("55.739300000000000"));
		user2.setLocation2(new BigDecimal("49.161400000000000"));
		persistEntity(user2);
		//Тесты
		assertEquals(service.getUsersWithFilter(null, null).size(), 4);
		assertEquals(service.getUsersWithFilter(null, "smight").size(), 3);
		assertEquals(service.getUsersWithFilter(filter, "").size(), 4);
		assertEquals(service.getUsersWithFilter(filter, "smight").size(), 3);
		//Не должен найтись smight, так как есть фильтр
		assertEquals(service.getUsersWithFilter(filter, "smight").size(), 3);
		assertEquals(service.getUsersWithFilter(filter, "lalal").size(), 4);
		//максимальный и минимальный возраст
		filter.setAgeMax(0);
		assertEquals(service.getUsersWithFilter(filter, null).size(), 3);
		filter.setAgeMax(2);
		assertEquals(service.getUsersWithFilter(filter, null).size(), 4);
		filter.setAgeMin(2);
		assertEquals(service.getUsersWithFilter(filter, null).size(), 1);
		filter.setAgeMin(0);
		assertEquals(service.getUsersWithFilter(filter, null).size(), 4);
		//минимальный ретинг. Верхнего фильтра нет
		filter.setRating(0);
		assertEquals(service.getUsersWithFilter(filter, null).size(), 1);
		filter.setRating(null);
		assertEquals(service.getUsersWithFilter(filter, null).size(), 4);
		//пол
		filter.setSex("male");
		assertEquals(service.getUsersWithFilter(filter, null).size(), 1);
		filter.setSex("female");
		assertEquals(service.getUsersWithFilter(filter, null).size(), 3);
		filter.setSex(null);
		assertEquals(service.getUsersWithFilter(filter, null).size(), 4);
		//предпочтения
		filter.setSexPreferences("male");
		assertEquals(service.getUsersWithFilter(filter, null).size(), 0);
		filter.setSexPreferences("female");
		assertEquals(service.getUsersWithFilter(filter, null).size(), 3);
		filter.setSexPreferences(null);
		assertEquals(service.getUsersWithFilter(filter, null).size(), 4);
		//Расстояние (между москвой и Казанью, там чуть меньше 800км)
		filter.setDistance(800);
		assertEquals(service.getUsersWithFilter(filter, "smight").size(), 3);
		assertEquals(service.getUsersWithFilter(filter, "123").size(), 1);
		assertEquals(service.getUsersWithFilter(filter, null).size(), 0);
		filter.setDistance(700);
		assertEquals(service.getUsersWithFilter(filter, "smight").size(), 0);
		assertEquals(service.getUsersWithFilter(filter, "123").size(), 0);
	}

	@Test
	public void getUserProfileForLogin() throws JsonProcessingException, SQLException {
		//Чищу таблицу t_user_profile перед тестами
		truncateTable(TUserProfileEntity.class);
		assertNull(service.getUserProfileForLogin("kkk"));
		TUserProfileEntity newUser = new TUserProfileEntity();
		newUser.setLogin("lll");
		newUser.setPassword("lll");
		newUser.setSex(1);
		persistEntity(newUser);
		assertNull(service.getUserProfileForLogin("kkk"));
		assertNotNull(service.getUserProfileForLogin("lll"));
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
