package com.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "t_complaint", schema = "spark_db", catalog = "postgres")
public class TComplaintEntity {
	private int idComplaint;

	@Id
	@Column(name = "id_complaint")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdComplaint() {
		return idComplaint;
	}

	public void setIdComplaint(int idComplaint) {
		this.idComplaint = idComplaint;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TComplaintEntity that = (TComplaintEntity) o;
		return idComplaint == that.idComplaint;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idComplaint);
	}
}
