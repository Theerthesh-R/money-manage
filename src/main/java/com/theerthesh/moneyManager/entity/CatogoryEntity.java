package com.theerthesh.moneyManager.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="tbl_catogories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatogoryEntity {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private String name;
@Column(updatable = false)
@CreationTimestamp
private LocalDateTime createdAt;
@UpdateTimestamp
private LocalDateTime updatedAt;

private String type;
private String icon;


@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "profile_id",nullable = false)
private ProfileEntity profile;

}
