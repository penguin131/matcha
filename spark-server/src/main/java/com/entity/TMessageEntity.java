package com.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "t_message", schema = "spark_db", catalog = "postgres")
public class TMessageEntity {
	private int idMessage;
	private String text;
	private Long date;
	private TUserProfileEntity from;
	private TUserProfileEntity to;

	@Id
	@Column(name = "id_message")
	public int getIdMessage() {
		return idMessage;
	}

	public void setIdMessage(int idMessage) {
		this.idMessage = idMessage;
	}

	@Basic
	@Column(name = "text")
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Basic
	@Column(name = "date")
	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "from", referencedColumnName = "user_profile_id")
	public TUserProfileEntity getFrom() {
		return from;
	}

	public void setFrom(TUserProfileEntity from) {
		this.from = from;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "to", referencedColumnName = "user_profile_id")
	public TUserProfileEntity getTo() {
		return to;
	}

	public void setTo(TUserProfileEntity to) {
		this.to = to;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TMessageEntity that = (TMessageEntity) o;
		return idMessage == that.idMessage &&
				Objects.equals(text, that.text) &&
				Objects.equals(date, that.date) &&
				Objects.equals(from, that.from) &&
				Objects.equals(to, that.to);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idMessage, text, date, from, to);
	}
}
