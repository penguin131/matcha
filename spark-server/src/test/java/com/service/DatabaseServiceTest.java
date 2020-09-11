package com.service;

import com.TestConfig;
import com.dictionary.Sex;
import com.dto.BaseUserProfileDto;
import com.dto.UserFilterDto;
import com.entity.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
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

	//Тестирую обе имплементации. Одна берется из persistence.xml, вторая из application.properties(в тестовых ресурсах)
	@Parameterized.Parameters
	public static List<DatabaseService> input() throws SQLException {
		return Arrays.asList(
				new DatabaseServiceSQLImpl(DriverManager.getConnection(TestConfig.getUrl(), TestConfig.getTestConfig())),
				new DatabaseServiceORMImp(em));
	}

	//Чищу все таблицы перед каждым тестом.
	@Before
	public void before() {
		truncateUserTable(TComplaintEntity.class);
		truncateUserTable(TImageEntity.class);
		truncateUserTable(TMessageEntity.class);
		truncateUserTable(TUserProfileEntity.class);
		truncateUserTable(TUsersUnityEntity.class);
	}

	@Test
	public void getUsersWithFilterTest() throws JsonProcessingException, SQLException {
		assertEquals(service.getUsersWithFilter(null, null).size(), 0);
		UserFilterDto filter = new UserFilterDto();
		TUserProfileEntity user0 = createUserProfileDto("1", "1", 0, -1, 1, 1);
		TUserProfileEntity user1 = createUserProfileDto("2", "3", 0, -1, 1, 1);
		TUserProfileEntity user2 = createUserProfileDto("3", "3", 0, -1, 1, 1);
		TUserProfileEntity smight = createUserProfileDto("smight", "222", 2, 2, 0, null);
		user0.setLocation1(new BigDecimal("55.752220000000000"));
		user0.setLocation2(new BigDecimal("37.615560000000000"));
		user1.setLocation1(new BigDecimal("55.752220000000000"));
		user1.setLocation2(new BigDecimal("37.615560000000000"));
		user2.setLocation1(new BigDecimal("55.752220000000000"));
		user2.setLocation2(new BigDecimal("37.615560000000000"));
		persistEntity(user0);
		persistEntity(user1);
		persistEntity(user2);
		smight.setLocation1(new BigDecimal("55.739300000000000"));
		smight.setLocation2(new BigDecimal("49.161400000000000"));
		persistEntity(smight);
		//Тесты
		assertEquals(service.getUsersWithFilter(null, null).size(), 4);
		assertEquals(service.getUsersWithFilter(null, "smight").size(), 3);
		assertEquals(service.getUsersWithFilter(filter, "").size(), 4);
		assertEquals(service.getUsersWithFilter(filter, "smight").size(), 3);
		//Не должен найтись smight
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
		//минимальный рейтинг. Верхнего фильтра нет
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
		assertEquals(service.getUsersWithFilter(filter, "1").size(), 3);
		assertEquals(service.getUsersWithFilter(filter, null).size(), 0);
		filter.setDistance(700);
		assertEquals(service.getUsersWithFilter(filter, "smight").size(), 0);
		assertEquals(service.getUsersWithFilter(filter, "123").size(), 0);
	}

	@Test
	public void usersWithFilterOrderTest() throws SQLException, JsonProcessingException {
		assertEquals(service.getUsersWithFilter(null, null).size(), 0);
		TUserProfileEntity user = new TUserProfileEntity();
		user.setLogin("1");
		user.setRating(-1);
		user.setPassword("");
		user.setAge(0);
	}

	@Test
	public void getUserProfileForLoginTest() throws JsonProcessingException, SQLException {
		assertNull(service.getUserProfileForLogin("kkk"));
		persistEntity(createUserProfileDto("lll", "lll", 0, 0, 1, null));
		assertNull(service.getUserProfileForLogin("kkk"));
		assertNotNull(service.getUserProfileForLogin("lll"));
	}

	@Test
	public void checkEmailExistTest() throws SQLException {
		assertFalse(service.checkEmailExist("kkk"));
		TUserProfileEntity user = createUserProfileDto("lll", "lll", 0, 0, 1, null);
		user.setEmail("aaaaa");
		persistEntity(user);
		assertTrue(service.checkEmailExist("aaaaa"));
		assertFalse(service.checkEmailExist("kkk"));
	}

	@Test
	public void createUserProfileTest() throws Exception {
		BaseUserProfileDto user = new BaseUserProfileDto();
		user.setLogin("smight");
		user.setPassword("123");
		user.setEmail("aaa@sss.ru");
		user.setSex("male");
		service.createUserProfile(user, null);
		TypedQuery<TUserProfileEntity> q = em.createQuery(
				"SELECT c FROM TUserProfileEntity c where c.login=:login", TUserProfileEntity.class)
				.setParameter("login", user.getLogin());
		TUserProfileEntity entity = q.getResultList().stream().findFirst().orElse(null);
		assertNotNull(entity);
		assertEquals(entity.getLogin(), user.getLogin());
		assertEquals(entity.getPassword(), user.getPassword());
		assertEquals(entity.getEmail(), user.getEmail());
		assertEquals(Sex.convertStringToCode(user.getSex()), entity.getSex() == null ? -1 : entity.getSex());
		//invalid sex. Если получаю что-то кроме male или female, сохраняю в базюке -1.
		user.setLogin("smight2");
		user.setSex("1");
		service.createUserProfile(user, null);
		q = em.createQuery(
				"SELECT c FROM TUserProfileEntity c where c.login=:login", TUserProfileEntity.class)
				.setParameter("login", user.getLogin());
		entity = q.getResultList().stream().findFirst().orElse(null);
		assertNotNull(entity);
		assertEquals(entity.getLogin(), user.getLogin());
		assertEquals(Sex.convertStringToCode(user.getSex()), entity.getSex() == null ? -1 : entity.getSex());
	}

	@Test
	public void getAllFriendsForLoginTest() {

	}




	//Чищу за собой
	private <T>void truncateUserTable(Class<T> c) {
		em.getTransaction().begin();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaDelete<T> query = builder.createCriteriaDelete(c);
		query.from(c);
		em.createQuery(query).executeUpdate();
		em.getTransaction().commit();
	}

	private void persistEntity(Object entity) {
		em.getTransaction().begin();
		em.persist(entity);
		em.getTransaction().commit();
	}

	private TUserProfileEntity createUserProfileDto(String login,
													String password,
													int age,
													int rating,
													int sex,
													Integer sexPreferences) {
		TUserProfileEntity user = new TUserProfileEntity();
		user.setLogin(login);
		user.setPassword(password);
		user.setAge(age);
		user.setRating(rating);
		user.setSex(sex);
		user.setSexPreferences(sexPreferences);
		return user;
	}
}
