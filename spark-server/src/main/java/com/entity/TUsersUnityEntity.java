package com.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "t_users_unity", schema = "spark_db", catalog = "postgres")
public class TUsersUnityEntity {
	private int tUsersUnityId;
	private Boolean confirmed;

	@Id
	@Column(name = "t_users_unity_id")
	public int gettUsersUnityId() {
		return tUsersUnityId;
	}

	public void settUsersUnityId(int tUsersUnityId) {
		this.tUsersUnityId = tUsersUnityId;
	}

	@Basic
	@Column(name = "confirmed")
	public Boolean getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TUsersUnityEntity that = (TUsersUnityEntity) o;
		return tUsersUnityId == that.tUsersUnityId &&
				Objects.equals(confirmed, that.confirmed);
	}

	@Override
	public int hashCode() {
		return Objects.hash(tUsersUnityId, confirmed);
	}
}
