package com.service;

import com.dto.*;
import com.exceptions.AccessDeniedException;
import com.exceptions.ValidateException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helper.Password;
import com.helper.ServiceHelper;
import com.helper.ValidateHelper;
import com.images.ImageManager;
import com.images.ImageManagerImpl;
import com.mail.MailService;
import com.security.SecurityHelper;
import spark.Request;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.List;

import static com.security.JWTHelper.createJWT;

public class LogicServiceBean implements LogicService {
	private DatabaseService databaseService = ServiceHelper.getDatabaseService();
	private ObjectMapper mapper = new ObjectMapper();
	private static final long TTL = 1000000000;
	private ImageManager imageManager = new ImageManagerImpl();

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
			MailService.sendConfirmationEmail(user.getEmail(), hash, request.url().substring(0, request.url().indexOf("createUserProfile")));
		} catch (IOException | MessagingException | SQLException | InvalidKeySpecException | NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		} catch (ValidateException ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	@Override
	public String getUserProfileForLogin(String login) {
		try {
			return mapper.writeValueAsString(databaseService.getUserProfileForLogin(login));
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
	public void updateUserProfile(String requestBody) throws ValidateException, AddressException {
		try {
			UserProfileDto user = mapper.readValue(requestBody, UserProfileDto.class);
			ValidateHelper.validateUserProfile(user);
			databaseService.updateUserProfile(user);
		} catch (SQLException | IOException ex) {
			ex.printStackTrace();
		} catch (ValidateException | AddressException ex) {
			ex.printStackTrace();
			throw ex;
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
	public void confirmUserForToken(String token) {
		try {
			databaseService.confirmUserForToken(token);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
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

	public void downloadImage(String login, byte[] image) {
		try {
			String imageId = databaseService.saveImage(login);
			imageManager.saveImage(login, image, imageId);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void deleteImage(String user, String id) throws AccessDeniedException {
		try {
			databaseService.deleteImage(user, id);
			imageManager.deleteImage(user, id);
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
	public String getUserPhotos(String user, Request req) {
		try {
			List<UserPhotoDto> photos = databaseService.getUserPhotos(user);
			for (UserPhotoDto photo : photos) {
				photo.setUrl(req.url().substring(0, req.url().indexOf("/", 21)) + "/image/" + photo.getImageId());
			}
 			if (photos.size() > 0) {
				return mapper.writeValueAsString(photos);
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
		return imageManager.getImage(name);
	}
}
