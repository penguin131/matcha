package com.service;

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
import org.apache.commons.lang.StringUtils;
import spark.Request;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.security.JWTHelper.createJWT;

public class LogicServiceBean implements LogicService {
	private DatabaseService databaseService = ServiceHelper.getDatabaseService();
	private ObjectMapper mapper = new ObjectMapper();
	private static final long TTL = 1000000000;

	@Override
	public String getAllUsers(String login) {
		try {
			List<UserProfileDto> users = databaseService.getAllUsers(login);
			if (users.size() == 0) {
				return "";
			} else {
				return mapper.writeValueAsString(users);
			}
		} catch (SQLException | JsonProcessingException ex) {
			ex.printStackTrace();
			return "";
		}
	}

	@Override
	public void createUserProfile(Request request) throws ValidateException {
		try {
			BaseUserProfileDto user = mapper.readValue(request.body(), BaseUserProfileDto.class);
			ValidateHelper.validateBaseUserProfile(user);
			user.setPassword(Password.getSaltedHash(user.getPassword()));
			String hash = SecurityHelper.generateHash();
			databaseService.createUserProfile(user, hash);
			MailService.sendConfirmationEmail(
					user.getEmail(),
					hash,
					request.url().substring(0, request.url().indexOf("createUserProfile")),
					"verification/");
		} catch (IOException | MessagingException | SQLException | InvalidKeySpecException | NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		} catch (ValidateException ex) {
			ex.printStackTrace();
			throw ex;
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
				return "";
			} else {
				return mapper.writeValueAsString(friendList);
			}
		} catch (SQLException | JsonProcessingException ex) {
			ex.printStackTrace();
			return "";
		}
	}

	@Override
	public void setLike(String from, String to) throws ValidateException {
		try {
			databaseService.setLike(from, to);
		} catch (ValidateException ex) {
			ex.printStackTrace();
			throw ex;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void setComplaint(String from, String to) {
		try {
			databaseService.setComplaint(from, to);
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
			if (user.getOldPassword() != null && user.getNewPassword() != null) {
				if (!databaseService.checkPassword(login, user.getOldPassword())) {
					throw new AccessDeniedException("Invalid password");
				}
				databaseService.changePassword(login, Password.getSaltedHash(user.getNewPassword()));
			}
			//Смена почты
			if (user.getEmail() != null && user.getPassword() != null) {
				if (!databaseService.checkPassword(login, user.getPassword())) {
					throw new AccessDeniedException("Invalid password");
				}
				ValidateHelper.validateEmail(user.getEmail());
				UserProfileDto profile = databaseService.getUserProfileForLogin(login, null);
				if (profile == null) {
					throw new BusinessException("No user profile with login " + login);
				}
				String hash = profile.getConfirmedToken();
				MailService.sendConfirmationEmail(
						user.getEmail(),
						hash,
						request.url().substring(0, request.url().indexOf("protected")),
						"changeMail/");
				databaseService.saveNewEmail(login, user.getEmail());
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
	public String getToken(String requestBody) throws AccessDeniedException {
		try {
			CredentialsDto credentials = mapper.readValue(requestBody, CredentialsDto.class);
			String login = credentials.getLogin();
			String password = credentials.getPassword();
			if (databaseService.checkPassword(login, password)) {
				return createJWT(login, "securityService", "security", TTL);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
		throw new AccessDeniedException("Invalid login/password");
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
			return "";
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

	public void downloadImage(String login, byte[] data) {
		try {
			String[] dataArray = new String(data).split("\"");
			for (int i = 1; i < dataArray.length; i+= 2) {
				databaseService.saveImage(login, dataArray[i]);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
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
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public String getUserPhotos(String user) {
		try {
			List<UserPhotoDto> photos = databaseService.getUserPhotos(user);
 			if (photos.size() > 0) {
				return mapper.writeValueAsString(photos);
			} else {
				return mapper.writeValueAsString(new ArrayList<>());
			}
		} catch (SQLException | JsonProcessingException ex) {
			ex.printStackTrace();
		}
		return "";
	}

	@Override
	public String getUsersWithFilter(String filter, String login) {
		try {
			UserFilterDto filterDto = mapper.readValue(filter, UserFilterDto.class);
			return mapper.writeValueAsString(databaseService.getUsersWithFilter(filterDto, login));
		} catch (SQLException | IOException ex) {
			ex.printStackTrace();
			return "";
		}
	}

	@Override
	public boolean checkEmailExist(String email) throws SQLException {
		return databaseService.checkEmailExist(email);
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
			databaseService.createSearchData(filterDto, login);
			return mapper.writeValueAsString(databaseService.nextUserWithFilter(filterDto, login));
		} catch (IOException | SQLException ex) {
			ex.printStackTrace();
			return "";
		}
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
}
