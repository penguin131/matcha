package com.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "t_image", schema = "spark_db", catalog = "postgres")
public class TImageEntity {
	private int idImage;
	private boolean isMain;
	private TUserProfileEntity user;

	@Id
	@Column(name = "id_image")
	public int getIdImage() {
		return idImage;
	}

	public void setIdImage(int idImage) {
		this.idImage = idImage;
	}

	@Basic
	@Column(name = "is_main")
	public boolean isMain() {
		return isMain;
	}

	public void setMain(boolean main) {
		isMain = main;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", referencedColumnName = "user_profile_id")
	public TUserProfileEntity getUser() {
		return user;
	}

	public void setUser(TUserProfileEntity user) {
		this.user = user;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TImageEntity that = (TImageEntity) o;
		return idImage == that.idImage &&
				isMain == that.isMain;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idImage, isMain);
	}

}
