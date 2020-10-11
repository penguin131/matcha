package com.service;

import com.dictionary.MessageType;
import com.dto.*;
import com.exceptions.AccessDeniedException;
import com.exceptions.BusinessException;
import com.exceptions.ValidateException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helper.Password;
import com.helper.ServiceHelper;
import com.helper.ValidateHelper;
import com.mail.MailService;
import com.security.SecurityHelper;
import com.sockets.WebSockets;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import spark.Request;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.security.JWTHelper.createJWT;

public class LogicServiceImpl implements LogicService {
	private Logger logger = Logger.getLogger(LogicServiceImpl.class);
	private DatabaseService databaseService = ServiceHelper.getDatabaseService();
	private Intra42Service intra42Service = new Intra42ServiceImpl();
	private ObjectMapper mapper = new ObjectMapper();
	private static final long TTL = 1000000000;

	@Override
	public String getAllUsers(String login) {
		try {
			List<UserProfileDto> users = databaseService.getAllUsers(login);
			if (users.size() == 0) {
				return "[]";
			} else {
				return mapper.writeValueAsString(users);
			}
		} catch (SQLException | JsonProcessingException ex) {
			ex.printStackTrace();
			return "[]";
		}
	}

	@Override
	public String getAllCoordinates() {
		try {
			List<UserProfileDto> users = databaseService.getAllUsers(null);
			List<CoordinateDto> coordinates = new ArrayList<>();

			for (UserProfileDto user : users) {
				CoordinateDto coordinate = new CoordinateDto();
				coordinate.setLatitude(user.getLatitude());
				coordinate.setLongitude(user.getLongitude());
				coordinates.add(coordinate);
			}
			return mapper.writeValueAsString(coordinates);
		} catch (SQLException | JsonProcessingException ex) {
			ex.printStackTrace();
			return "[]";
		}

	}

	@Override
	public void createUserProfile(Request request) throws ValidateException {
		try {
			BaseUserProfileDto user = mapper.readValue(request.body(), BaseUserProfileDto.class);
			createProfile(user, request.url().substring(0, request.url().indexOf("createUserProfile")), false);
		} catch (IOException | MessagingException | SQLException | InvalidKeySpecException | NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		} catch (ValidateException ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	private void createProfile(BaseUserProfileDto user, String url, boolean fromIntra)
			throws MessagingException,
			SQLException,
			ValidateException,
			JsonProcessingException,
			InvalidKeySpecException,
			NoSuchAlgorithmException,
			UnsupportedEncodingException,
			UnknownHostException {
		ValidateHelper.validateBaseUserProfile(user);
		user.setPassword(Password.getSaltedHash(user.getPassword()));
		String hash = SecurityHelper.generateHash();
		databaseService.createUserProfile(user, hash, fromIntra);
		if (!fromIntra) {
			MailService.sendConfirmationEmail(
					user.getEmail(),
					hash,
					url,
					"verification/");
		}
	}

	@Override
	public String getUserProfileForLogin(String login, String from) {
		try {
			return mapper.writeValueAsString(databaseService.getUserProfileForLogin(login, from));
		} catch (SQLException | JsonProcessingException ex) {
			ex.printStackTrace();
			return "";
		}
	}

	@Override
	public String getAllFriendsForLogin(String login) {
		try {
			List<FriendDto> friendList = databaseService.getAllFriendsForLogin(login);
			if (friendList.size() == 0) {
				return "[]";
			} else {
				return mapper.writeValueAsString(friendList);
			}
		} catch (SQLException | JsonProcessingException ex) {
			ex.printStackTrace();
			return "[]";
		}
	}

	@Override
	public void setLike(String from, String to) throws ValidateException {
		try {
			if (StringUtils.equals(from, to)) {
				logger.info("from == to");
				return;
			}
			if (noAvatar(from)) {
				logger.info("checkAvatar() false");
				return;
			}
			UserProfileDto profile = databaseService.getUserProfileForLogin(to, from);
			if (profile.getHasDislike()) {
				databaseService.setComplaint(from, to);
			}
			boolean result = databaseService.setLike(from, to);
			MessageDto notification = new MessageDto();
			notification.setType(MessageType.NOTIFICATION.getName());
			notification.setTo(to);
			notification.setFrom(from);
			notification.setMsgText("liked you!");
			WebSockets.sendMessage(null, notification);
			//Если лайк совпал, то дополнительное уведомление
			if (result) {
				notification.setTo(from);
				notification.setFrom(to);
				notification.setMsgText("added to your friends list!");
				WebSockets.sendMessage(null, notification);
			}
		} catch (ValidateException ex) {
			ex.printStackTrace();
			throw ex;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private boolean noAvatar(String login) throws SQLException {
		List<UserPhotoDto> photos = databaseService.getUserPhotos(login);
		for (UserPhotoDto photo : photos) {
			if (photo.isMain()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void setComplaint(String from, String to) {
		try {
			if (StringUtils.equals(from, to)) {
				logger.info("from == to");
				return;
			}
			if (noAvatar(from)) {
				logger.info("checkAvatar() false");
				return;
			}
			UserProfileDto profile = databaseService.getUserProfileForLogin(to, from);
			if (profile.getHasLike()) {
				databaseService.setLike(from, to);
			}
			databaseService.setComplaint(from, to);
			MessageDto notification = new MessageDto();
			notification.setType(MessageType.NOTIFICATION.getName());
			notification.setTo(to);
			notification.setFrom(from);
			notification.setMsgText("disliked you!");
			WebSockets.sendMessage(null, notification);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void setComplaintAboutFake(String from, String to) {
		try {
			if (noAvatar(from)) {
				logger.info("checkAvatar() false");
				return;
			}
			databaseService.setFakeComplaint(from, to);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void deleteUserProfileForLogin(String login) {
		try {
			databaseService.deleteUserProfileForLogin(login);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void updateUserProfile(Request request, String login) throws ValidateException, MessagingException, AccessDeniedException {
		try {
			InnerProfileDto user = mapper.readValue(request.body(), InnerProfileDto.class);
			if (user.getTags() != null && user.getTags().size() > 10) {
				throw new ValidateException("To many tags");
			}
			ValidateHelper.validateUserProfile(user);
			//Если есть поля без специальной логики
			if (user.hasFields()) {
				databaseService.updateUserProfile(user, login);
			}
			//Смена пароля
			if (!StringUtils.isEmpty(user.getNewPassword())) {
				UserProfileDto oldProfile = databaseService.getUserProfileForLogin(login, null);
				if (!oldProfile.getIntraFirst() && !databaseService.checkPassword(login, user.getOldPassword())) {//Если регался с интры, в первый раз не проверяет старый пароль
					throw new AccessDeniedException("Invalid old password");
				}
				databaseService.changePassword(login, Password.getSaltedHash(user.getNewPassword()));
			}
			//Смена почты
			if (user.getNewEmail() != null && user.getPassword() != null) {
				if (!databaseService.checkPassword(login, user.getPassword())) {
					throw new AccessDeniedException("Invalid login/password");
				}
				ValidateHelper.validateEmail(user.getNewEmail());
				UserProfileDto profile = databaseService.getUserProfileForLogin(login, null);
				String hash = profile.getConfirmedToken();
				MailService.sendConfirmationEmail(
						user.getNewEmail(),
						hash,
						request.url().substring(0, request.url().indexOf("protected")),
						"changeMail/");
				databaseService.saveNewEmail(login, user.getNewEmail());
			}
			//Апдейт списка тэгов
			if (user.getTags() != null) {
				databaseService.updateUserTags(user, login);
			}
			//Ошибки, связанные с отправкой письма, валидацией и несовпадением старого пароля
		} catch (ValidateException | MessagingException | AccessDeniedException ex) {
			ex.printStackTrace();
			throw ex;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void updateUserCoordinates(String requestBody, String login) {
		try {
			CoordinateDto coordinates = mapper.readValue(requestBody, CoordinateDto.class);
			databaseService.updateUserCoordinates(coordinates, login);
		} catch (SQLException | IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public String getToken(Request request) throws AccessDeniedException {
		try {
			TokenDto tokenDto = new TokenDto();
			AuthDataDto authData = mapper.readValue(request.body(), AuthDataDto.class);
			if (authData.getOauth2Code() == null) {//Если авторизация по логину и паролю
				String login = authData.getLogin();
				String password = authData.getPassword();
				if (databaseService.checkPassword(login, password)) {
					String token = createJWT(login, "securityService", "security", TTL);
					databaseService.updateLastAuthDate(login, System.currentTimeMillis());
					tokenDto.setLogin(login);
					tokenDto.setToken(token);
					return mapper.writeValueAsString(tokenDto);
				}
			} else {//Если авторизация через токен из интры
				String intra42Token = intra42Service.getToken(authData.getOauth2Code());
				BaseUserProfileDto user = intra42Service.getCurrentUser(intra42Token);
				UserProfileDto databaseUser = databaseService.getUserProfileForIntraLogin(user.getLogin());
				if (databaseUser == null) {//Если юзера еще нет, то добавляю
					logger.info("No user with login " + user.getLogin());
					if (getUserProfileForLogin(user.getLogin(), null) != null) {//если логин занят, то приписываю циферку в конце
						int addition = 1;
						while ((databaseService.getUserProfileForLogin(user.getLogin() + addition, null)) != null) {
							addition++;
						}
						user.setLogin(user.getLogin() + addition);
						logger.info("Generated login: " + user.getLogin());
					}
					createProfile(user, request.url().substring(0, request.url().indexOf("getToken")), true);
				}
				String login = databaseUser == null ? user.getLogin() : databaseUser.getLogin();
				String token = createJWT(login,
						"securityService",
						"security",
						TTL);
				tokenDto.setToken(token);
				tokenDto.setLogin(login);
				databaseService.updateLastAuthDate(login, System.currentTimeMillis());
				logger.info("New token: " + mapper.writeValueAsString(tokenDto));
				return mapper.writeValueAsString(tokenDto);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		throw new AccessDeniedException("Invalid auth data");
	}

	@Override
	public String confirmUserForToken(String token) {
		try {
			if (StringUtils.isEmpty(token)) {
				throw new BusinessException("Zero confirmed token");
			}
			databaseService.confirmUserForToken(token);
		} catch (SQLException | BusinessException ex) {
			ex.printStackTrace();
			return "Error";
		}
		return "Success";
	}

	@Override
	public String getChatHistory(String user1, String user2) {
		try {
			List<MessageDto> messages = databaseService.getChatHistory(user1, user2);
			return mapper.writeValueAsString(messages);
		} catch (SQLException | IOException ex) {
			ex.printStackTrace();
			return "[]";
		}
	}

	@Override
	public void saveChatMessage(String requestBody) {
		try {
			MessageDto message = mapper.readValue(requestBody, MessageDto.class);
			databaseService.saveChatMessage(message);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String downloadImage(String login, byte[] data) {
		List<Integer> imagesId = new ArrayList<>();
		try {
			String[] dataArray = new String(data).split("\"");
			for (int i = 1; i < dataArray.length; i+= 2) {
				imagesId.add(databaseService.saveImage(login, dataArray[i]));
			}
			logger.info("images id: " + mapper.writeValueAsString(imagesId));
		} catch (SQLException | JsonProcessingException ex) {
			ex.printStackTrace();
		}
		return imagesId.toString();
	}

	//Не дает удалить чужие фотки
	@Override
	public void deleteImage(String user, String id) {
		try {
			List<UserPhotoDto> photos = databaseService.getUserPhotos(user);
			int intId = Integer.parseInt(id);
			if (photos != null && photos.stream().anyMatch(image -> image.getImageId() == intId)) {
				databaseService.deleteImage(id);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void setMainImage(String imageId, String userLogin) {
		try {
			databaseService.setMainImage(imageId, userLogin);
		} catch (SQLException | NumberFormatException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public String getUserPhotos(String user) {
		try {
			List<UserPhotoDto> photos = databaseService.getUserPhotos(user);
 			if (photos.size() > 0) {
				return mapper.writeValueAsString(photos);
			}
		} catch (SQLException | JsonProcessingException ex) {
			ex.printStackTrace();
		}
		return "[]";
	}

	@Override
	public String getUsersWithFilter(String filter, String login) {
		try {
			UserFilterDto filterDto = mapper.readValue(filter, UserFilterDto.class);
			return mapper.writeValueAsString(databaseService.getUsersWithFilter(filterDto, login));
		} catch (SQLException | IOException ex) {
			ex.printStackTrace();
			return "[]";
		}
	}

	@Override
	public byte[] getImage(String name) {
		try {
			return databaseService.getImage(name).getBytes();
		} catch (SQLException ex) {
			ex.printStackTrace();
			return new byte[0];
		}
	}

	@Override
	public String getNextUser(String login, String filter) {
		try {
			UserFilterDto filterDto = null;
			if (!StringUtils.isEmpty(filter)) {
				filterDto = mapper.readValue(filter, UserFilterDto.class);
			}
			filterDto = upgradeFilter(filterDto, login);
			databaseService.createSearchData(filterDto, login);
			UserProfileDto nextUser = databaseService.nextUserWithFilter(filterDto, login);
			if (nextUser == null) {
				return "";
			}
			//Уведомление
			MessageDto notification = WebSockets.prepareMessage(
					login,
					nextUser.getLogin(),
					MessageType.NOTIFICATION.getName(),
					"watch you profile!");
			WebSockets.sendMessage(null, notification);

			return mapper.writeValueAsString(nextUser);
		} catch (IOException | SQLException ex) {
			ex.printStackTrace();
			return "";
		}
	}

	private UserFilterDto upgradeFilter(UserFilterDto oldFilter, String login) throws SQLException, JsonProcessingException {
		UserProfileDto userProfile = databaseService.getUserProfileForLogin(login, null);
		if (userProfile.getSexPreferences() != null || userProfile.getSex() != null) {
			if (oldFilter == null) {
				oldFilter = new UserFilterDto();
			}
			oldFilter.setSex(userProfile.getSexPreferences());
			oldFilter.setSexPreferences(userProfile.getSex());
		}
		return oldFilter;
	}

	public String updateUserMail(String token) {
		try {
			if (StringUtils.isEmpty(token)) {
				throw new BusinessException("Zero confirmed token");
			}
			databaseService.updateUserMailFromTemp(token);
		} catch (SQLException | BusinessException ex) {
			ex.printStackTrace();
			return "Error";
		}
		return "Success";
	}

	public String getAllLikedUsers(String login) {
		try {
			List<UserProfileDto> users = databaseService.getAllLikedUsers(login);
			if (users.size() == 0) {
				return "[]";
			} else {
				return mapper.writeValueAsString(users);
			}
		} catch (SQLException | JsonProcessingException ex){
			ex.printStackTrace();
			return "[]";
		}
	}

	public String getAllLookedUsers(String login) {
		try {
			List<UserProfileDto> users = databaseService.getAllLookedUsers(login);
			if (users.size() == 0) {
				return "[]";
			} else {
				return mapper.writeValueAsString(users);
			}
		} catch (SQLException | JsonProcessingException ex){
			ex.printStackTrace();
			return "[]";
		}
	}
}
